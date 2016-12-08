package com.foodfun.openuser;

import java.util.List;

import com.foodfun.common.model.Classify;
import com.foodfun.common.model.Mclassify;
import com.foodfun.common.model.Openuser;
import com.foodfun.common.model.Test;
import com.foodfun.common.model.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;

/**
 * BlogController
 * 锟斤拷锟斤拷 sql 锟斤拷业锟斤拷锟竭硷拷写锟斤拷 Model 锟斤拷 Service 锟叫ｏ拷锟斤拷要写锟斤拷 Controller 锟叫ｏ拷锟斤拷锟缴猴拷习锟竭ｏ拷锟斤拷锟斤拷锟节达拷锟斤拷锟斤拷目锟侥匡拷锟斤拷锟斤拷维锟斤拷
 */
//@Before(BlogInterceptor.class)
public class OpenuserController extends Controller {
	public void index() {
		setAttr("testPage", Openuser.dao.paginate(getParaToInt(0, 1), 20));
		render("openuser.html");
	}

//	public void add() {
//		
//	}
	
	public void upload() {
		// 获取上传的文件
		UploadFile uf = getFile("Filedata");

		// 拼接文件上传的完整路径
		String fileName = "http://" + this.getRequest().getRemoteHost() + ":"
				+ this.getRequest().getLocalPort() + "/upload/"
				+ uf.getFileName();
		
		this.setAttr("fileName", fileName);
		System.out.println("================fileName:"+fileName);
		
		//以json格式进行渲染
		renderJson();
	}
	
	public void save() {
		getModel(Openuser.class).save();
		redirect("/openuser");
	}
	
//	public void edit() {
//		setAttr("user", User.dao.findById(getParaToInt()));
//	}
	
	public void update() {
		getModel(Openuser.class).update();
		redirect("/openuser");
	}
	
	public void delete() {
		Openuser.dao.deleteById(getParaToInt());
		redirect("/openuser");
	}
}