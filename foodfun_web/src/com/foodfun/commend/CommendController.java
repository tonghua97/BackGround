package com.foodfun.commend;

import java.util.List;

import com.foodfun.common.model.Commend;
import com.foodfun.common.model.Mclassify;
import com.foodfun.common.model.Test;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;

/**
 * BlogController
 * 锟斤拷锟斤拷 sql 锟斤拷业锟斤拷锟竭硷拷写锟斤拷 Model 锟斤拷 Service 锟叫ｏ拷锟斤拷要写锟斤拷 Controller 锟叫ｏ拷锟斤拷锟缴猴拷习锟竭ｏ拷锟斤拷锟斤拷锟节达拷锟斤拷锟斤拷目锟侥匡拷锟斤拷锟斤拷维锟斤拷
 */
//@Before(BlogInterceptor.class)
public class CommendController extends Controller {
	public void index() {
		setAttr("testPage", Commend.dao.paginate(getParaToInt(0, 1), 20));
		render("commend.html");
	}

	public void add() {
		
	}
	
	
	public void save() {
		getModel(Commend.class).save();
		redirect("/commend");
	}
	
	public void edit() {
		setAttr("commend", Commend.dao.findById(getPara()));
	}
	
	public void update() {
		getModel(Commend.class).update();
		redirect("/commend");
	}
	
	public void delete() {
		Commend.dao.deleteById(getPara());
		redirect("/commend");
	}
}