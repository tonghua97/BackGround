package com.foodfun.test;

import java.util.List;

import com.foodfun.common.model.Test;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;

/**
 * BlogController
 * 锟斤拷锟斤拷 sql 锟斤拷业锟斤拷锟竭硷拷写锟斤拷 Model 锟斤拷 Service 锟叫ｏ拷锟斤拷要写锟斤拷 Controller 锟叫ｏ拷锟斤拷锟缴猴拷习锟竭ｏ拷锟斤拷锟斤拷锟节达拷锟斤拷锟斤拷目锟侥匡拷锟斤拷锟斤拷维锟斤拷
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
				+ this.getRequest().getLocalPort() + "/upload/12/"
				+ uf.getFileName();
		
		this.setAttr("fileName", fileName);
		System.out.println("================fileName:"+fileName);
		
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