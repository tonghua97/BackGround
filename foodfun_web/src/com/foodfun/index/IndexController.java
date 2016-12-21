package com.foodfun.index;

import com.foodfun.common.model.Commend;
import com.foodfun.common.model.Fun;
import com.foodfun.common.model.Mclassify;
import com.foodfun.common.model.Openuser;
import com.foodfun.common.model.Recipes;
import com.foodfun.common.model.User;
import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;

/**
 * IndexController
 */
public class IndexController extends Controller {
	public void index() {
		
		Recipes recipes = Recipes.dao.findFirst("select count(*) from recipes");
		Commend commend = Commend.dao.findFirst("select count(*) from commend");
		Fun fun = Fun.dao.findFirst("select count(*) from fun");
		User user = User.dao.findFirst("select count(*) from user");
		Openuser openuser = Openuser.dao.findFirst("select count(*) from openuser");
		
		setAttr("Recipes", recipes.getLong("count(*)"));
		setAttr("Commend", commend.getLong("count(*)"));
		setAttr("Fun", fun.getLong("count(*)"));
		int uNum = (int) (user.getLong("count(*)")+0);
		int oNum = (int) (openuser.getLong("count(*)")+0);
		int total = uNum + oNum;
		setAttr("UserTotal", total);
		setAttr("User", uNum/(total*0.01));
		setAttr("Openuser", oNum/(total*0.01));
		
		render("index.html");
	}
	
	public void exit(){
		removeSessionAttr("user");
		setAttr("msg","success");
		renderJson();
	}
}
