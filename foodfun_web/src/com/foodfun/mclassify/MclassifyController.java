package com.foodfun.mclassify;

import java.util.List;

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
public class MclassifyController extends Controller {
	public void index() {
		setAttr("testPage", Mclassify.dao.paginate(getParaToInt(0, 1), 20));
		render("mclassify.html");
	}

	public void add() {
		
	}
	
	
	public void save() {
		getModel(Mclassify.class).save();
		redirect("/mclassify");
	}
	
	public void edit() {
		setAttr("mclassify", Mclassify.dao.findById(getParaToInt()));
	}
	
	public void update() {
		getModel(Mclassify.class).update();
		redirect("/mclassify");
	}
	
	public void delete() {
		Mclassify.dao.deleteById(getParaToInt());
		redirect("/mclassify");
	}
}