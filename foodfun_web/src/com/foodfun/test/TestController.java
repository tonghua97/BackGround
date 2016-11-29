package com.foodfun.test;

import java.util.List;

import com.foodfun.common.model.Test;
import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;

/**
 * TestController
 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
//@Before(BlogInterceptor.class)
public class TestController extends Controller {
	public void index() {
		setAttr("testPage", Test.me.paginate(getParaToInt(0, 1), 10));
		render("test.html");
	}
	public void list(){
		String username = getPara("Username");
		List<Test> lb = Test.me.find("select * from test ",username);
		renderJson(lb);
	}

	public void add() {
	}
	public void upload() {
		// 获取上传的文件
		UploadFile uf = getFile("Filedata");

		// 拼接文件上传的完整路径
		String fileName = "http://" + this.getRequest().getRemoteHost() + ":"
				+ this.getRequest().getLocalPort() + "/upload/"
				+ uf.getFileName();
		
		this.setAttr("fileName", fileName);
		System.out.println("================fileName(test):"+fileName);
		
		//以json格式进行渲染
		renderJson();
	}
	public void save() {
		getModel(Test.class).save();
		redirect("/test");
	}
	
	public void edit() {
		setAttr("test", Test.me.findById(getParaToInt()));
	}
	
	public void update() {
		getModel(Test.class).update();
		redirect("/test");
	}
	
	public void delete() {
		Test.me.deleteById(getParaToInt());
		redirect("/test");
	}
}