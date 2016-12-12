package com.foodfun.mclassify;

import java.util.List;

import com.foodfun.common.model.Mclassify;
import com.foodfun.common.model.Test;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;

/**
 * MclassifyController
 */
//@Before(BlogInterceptor.class)
public class MclassifyController extends Controller {
	public void index() {
		setAttr("mclassifyPage", Mclassify.dao.paginate(getParaToInt(0, 1), 10));
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