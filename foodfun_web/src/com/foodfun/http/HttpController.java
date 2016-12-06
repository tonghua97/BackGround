package com.foodfun.http;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.foodfun.common.model.Classify;
import com.foodfun.common.model.Commend;
import com.foodfun.common.model.Fun;
import com.foodfun.common.model.Mclassify;
import com.foodfun.common.model.Recipes;
import com.foodfun.common.model.Test;
import com.foodfun.common.model.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.json.Json;
import com.jfinal.upload.UploadFile;

import net.sf.json.JSONArray;

/**
 * BlogController
 * 锟斤拷锟斤拷 sql 锟斤拷业锟斤拷锟竭硷拷写锟斤拷 Model 锟斤拷 Service 锟叫ｏ拷锟斤拷要写锟斤拷 Controller 锟叫ｏ拷锟斤拷锟缴猴拷习锟竭ｏ拷锟斤拷锟斤拷锟节达拷锟斤拷锟斤拷目锟侥匡拷锟斤拷锟斤拷维锟斤拷
 */
//@Before(BlogInterceptor.class)
public class HttpController extends Controller {
	/**
	 * 获取推荐的名称和简介及美食id
	 */
	public void getCommend(){
		List<Recipes> list = Recipes.dao.find("select recipesIntro,recipesName,recipesId from recipes,commend "
				+ "where commend.FKcommendId = recipes.recipesId");
		renderJson(list);
	}
	
	/**
	 * 获得拾趣的标题
	 */
	public void getFunTitle(){
		List<Fun> list = Fun.dao.find("select funTitle from fun");
		renderJson(list);
	}
	
	/**
	 * 获得美食名称及收藏数量
	 */
	public void getRecipesName(){
		List<Recipes> list = Recipes.dao.find("select recipesName,recipesCollect from recipes "
				+ "order by recipesCollect desc");
		renderJson(list);
	}
	
	/**
	 * 根据recipesId获得美食集合
	 */
	public void getRecipesById(){
		HttpServletRequest r = getRequest();
		String recipesId = r.getParameter("recipesId");
		List<Recipes> list = Recipes.dao.find("select * from recipes where "
				 + "recipesId=" + "\"" + recipesId + "\"");
		renderJson(list);
	}
	
	/**
	 * 根据名称获得美食集合
	 */
	public void getRecipesByName(){
		HttpServletRequest r = getRequest();
		String recipesName = r.getParameter("recipesName");
		List<Recipes> list = Recipes.dao.find("select * from recipes where "
				 + "recipesName=" + "\"" + recipesName + "\"");
		renderJson(list);
	}
	
	/**
	 * 根据funTitle获得拾趣集合
	 */
	public void getFunByTitle(){
		HttpServletRequest r = getRequest();
		String funTitle = r.getParameter("funTitle");
		List<Fun> list = Fun.dao.find("select * from fun where "
				 + "funTitle=" + "\"" + funTitle + "\"");
		renderJson(list);
	}
	
	/**
	 * 根据funTitle获得拾趣集合
	 */
	public void getFunById(){
		HttpServletRequest r = getRequest();
		String funTitle = r.getParameter("funTitle");
		List<Fun> list = Fun.dao.find("select * from fun where "
				 + "funTitle=" + "\"" + funTitle + "\"");
		renderJson(list);
	}
	
	/**
	 * 获取主分类的集合
	 */
	public void getMClassify(){
		List<Mclassify> list = Mclassify.dao.find("select * from mclassify");
		renderJson(list);
	}
	
	/**
	 * 根据主分类id得到分类名称及id的集合
	 */
	public void getClassifyByMid(){
		HttpServletRequest r = getRequest();
		String FKclassifyId = r.getParameter("FKclassifyId");
		List<Classify> list = Classify.dao.find("select classifyName,classifyId  from classify where "
				 + "FKclassifyId=" + "\"" + FKclassifyId + "\"");
		renderJson(list);
	}
	
	/**
	 * 根据分类的id以及主分类的id获取美食的列表
	 */
	public void getClassifyListById(){ 
		HttpServletRequest r = getRequest();
		String classifyId = r.getParameter("classifyId");
		String mclassifyId = r.getParameter("");
	}
	
	/**
	 * 根据账号密码登录
	 */
	public void isLogin(){
		HttpServletRequest r = getRequest();
		String userAccount = r.getParameter("userAccount");
		String userPassword = r.getParameter("userPassword");
		
		User user = User.dao.findFirst("select * from user where"
				+ "userAccount=" + "\"" + userAccount + "\"");
		if(user != null){
			User user2 = User.dao.findFirst("select * from user where"
					+ "userAccount=" + "\"" + userAccount + "\"" 
					+ "userPassword=" + "\"" + userPassword + "\"");
			if(user2 != null){
				renderText("1");
			}else{
				renderText("2");
			}
		}else{
			renderText("0");
		}
	}
}