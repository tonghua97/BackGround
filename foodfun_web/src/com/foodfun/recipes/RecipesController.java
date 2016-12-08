package com.foodfun.recipes;

import java.util.List;

import com.foodfun.common.model.Classify;
import com.foodfun.common.model.Mclassify;
import com.foodfun.common.model.Recipes;
import com.foodfun.common.model.Test;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;

/**
 * RecipesController
 */
//@Before(BlogInterceptor.class)
public class RecipesController extends Controller {
	public void index() {
		setAttr("testPage", Recipes.dao.paginate(getParaToInt(0, 1), 20));
		render("recipes.html");
	}

	public void add() {
		
	}
	
	public void test(){
		Recipes r = Recipes.dao.findById("r006");
		renderJson(r);
	}
	
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
		getModel(Recipes.class).save();
		redirect("/recipes");
	}
	
	public void edit() {
//		setAttr("recipes", Recipes.dao.findById(getParaToInt()));
		setAttr("recipes", Recipes.dao.findById(getPara()));
	}
	
	public void update() {
		getModel(Recipes.class).update();
		redirect("/recipes");
	}
	
	public void delete() {
//		Recipes.dao.deleteById(getParaToInt());
		Recipes.dao.deleteById(getPara());
		redirect("/recipes");
	}
}