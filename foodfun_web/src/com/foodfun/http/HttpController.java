package com.foodfun.http;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.foodfun.common.model.Classify;
import com.foodfun.common.model.Collect;
import com.foodfun.common.model.Commend;
import com.foodfun.common.model.Fun;
import com.foodfun.common.model.Funcollect;
import com.foodfun.common.model.Mclassify;
import com.foodfun.common.model.Openuser;
import com.foodfun.common.model.Recipes;
import com.foodfun.common.model.Test;
import com.foodfun.common.model.User;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.json.Json;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * BlogController
 * 锟斤拷锟斤拷 sql 锟斤拷业锟斤拷锟竭硷拷写锟斤拷 Model 锟斤拷 Service 锟叫ｏ拷锟斤拷要写锟斤拷 Controller 锟叫ｏ拷锟斤拷锟缴猴拷习锟竭ｏ拷锟斤拷锟斤拷锟节达拷锟斤拷锟斤拷目锟侥匡拷锟斤拷锟斤拷维锟斤拷
 */
@Clear
public class HttpController extends Controller {
	/**
	 * 获取推荐的名称和简介及美食id,图片
	 */
	public void getCommend(){
		String number = getPara("num");
		int num = Integer.parseInt(number);
		
		String sql = "select commend.commendId,recipesIntro,recipesName,recipesId,recipesImage from recipes,commend "
				+ "where commend.FKcommendId = recipes.recipesId";
		
		Page<Recipes> pageRecipes = Recipes.dao.paginate(num, 4, 
				"select commend.commendId,recipesIntro,recipesName,recipesId,recipesImage",
				"from recipes,commend where commend.FKcommendId = recipes.recipesId");
		//List<Recipes> list = Recipes.dao.find(sql);
		List<Recipes> list = pageRecipes.getList();
		
		setAttr("totalPage", pageRecipes.getTotalPage());
		setAttr("Recipes",list);
		//返回推荐名称、简介及食谱id的json串
		renderJson();
	}
	
	/**
	 * 获得拾趣的标题，图片
	 */
	public void getFunTitle(){
		String number = getPara("num");
		int num = Integer.parseInt(number);
		Page<Fun> pageFun = Fun.dao.paginate(num, 5, "select funId,funTitle,funImage", "from fun");
        List<Fun> list = pageFun.getList();
		setAttr("totalPage",pageFun.getTotalPage());
		setAttr("Fun",list);
		
		//返回拾趣标题与图片集合的json串

		renderJson();
	}
	
	/**
	 * 获得排行榜（美食名称及收藏数量）
	 */
	public void getRanklist(){
		List<Recipes> list = Recipes.dao.find("select recipesId,recipesName,recipesImage,recipesCollect "
				+ "from recipes order by recipesCollect desc "
				+ "limit 10");
		setAttr("Ranklist",list);
		//返回食谱名称及收藏数量集合的json串

		renderJson();
	}
	
	/**
	 * 根据recipesId获得美食集合
	 * 参数：recipesId String类型
	 * 参数:userId
	 */
	public void getRecipesById(){
		HttpServletRequest r = getRequest();
		String recipesId = r.getParameter("recipesId");
		String userId = r.getParameter("userId");
		String isCollect = "false";
		List<Recipes> list = Recipes.dao.find("select recipesId,recipesName,recipesMfood,recipesFood,recipesLevel,recipesIntro,recipesTime,recipesStep,recipesCollect,recipesImage,recipesEffect,classify.classifyName"
				+ " from recipes"
				+ " join classify on (recipes.FKrecipesTaste = classify.classifyId)"
				+ " where recipesId=" + "\"" + recipesId + "\"");
		Collect c = Collect.dao.findFirst("select * from collect "
				+ "where FKcollectUser=" + "\"" + userId + "\""
				+ " and FKrecipesId=" + "\"" + recipesId + "\"");
		
		//Ret ret = Ret.create("Recipes",list);
		//食谱详情的json串
		if(c != null){
			isCollect = "true";
		}
		setAttr("IsCollect",isCollect);
		setAttr("Recipes",list);
		renderJson();
	}
	
	/**
	 * 根据名称获得美食集合
	 * 参数：recipesName String类型
	 */
	public void getRecipesByName(){
		HttpServletRequest r = getRequest();
		String recipesName = r.getParameter("recipesName");
		List<Recipes> list = Recipes.dao.find("select * from recipes where "
				 + "recipesName=" + "\"" + recipesName + "\"");
		
		//食谱详情的json串
		renderJson(list);
	}
	
	/**
	 * 根据名称获得美食id
	 * 参数：recipesName String类型
	 */
	public void getRecipesIdByName(){
		HttpServletRequest r = getRequest();
		String recipesName = r.getParameter("recipesName");
		Recipes list = Recipes.dao.findFirst("select recipesId from recipes where "
				 + "recipesName=" + "\"" + recipesName + "\"");
		
		JSONObject json = new JSONObject(list);
		int id = json.getInt("recipesId");

		//食谱的id
		renderText(id+"");
	}
	
	/**
	 * 根据拾趣标题获得拾趣集合
	 * 参数：funTitle String类型
	 */
	public void getFunByTitle(){
		HttpServletRequest r = getRequest();
		String funTitle = r.getParameter("funTitle");
		List<Fun> list = Fun.dao.find("select * from fun where "
				 + "funTitle=" + "\"" + funTitle + "\"");
		
		//拾趣详细信息的json串
		renderJson(list);
	}
	
	/**
	 * 根据拾趣id获得拾趣集合
	 * 参数：funId String类型
	 */
	public void getFunById(){
		String funId = getPara("funId");
		String userId = getPara("userId");
		String isCollect = "false";
		List<Fun> list = Fun.dao.find("select * from fun where "
				+ "funId=" + "\"" + funId + "\"");
		Funcollect fun = Funcollect.dao.findFirst("select * from funcollect "
				+ "where funcollectUser=" + "\"" + userId + "\""
				+ " and FKfunId=" + "\"" + funId + "\"");
		//拾趣详细信息的json串
		if(fun != null){
			isCollect = "true";
		}
		setAttr("IsCollect",isCollect);
		setAttr("Fun",list);
		renderJson();
	}
	
	/**
	 * 获取主分类的集合
	 */
	public void getMClassify(){
		List<Mclassify> list = Mclassify.dao.find("select * from mclassify");
		renderJson(list);
	}
	
	/**
	 * 根据主分类id得到分类名称及id,图标的集合
	 * 参数：主分类id：mclassifyId String类型
	 */
	public void getClassifyByMid(){
		HttpServletRequest r = getRequest();
		String FKclassifyId = r.getParameter("FKclassifyId");
		List<Classify> list = Classify.dao.find("select classifyName,classifyId,classifyImage  from classify where "
				 + "FKclassifyId=" + "\"" + FKclassifyId + "\"");
		renderJson(list);
	}
	
//	public void test(){
//		int mclassifyId = 5;
//		List<Mclassify> list = Mclassify.dao.find("select mclassifyName  from mclassify where "
//				 + "mclassifyId=" + "\"" + mclassifyId + "\"");
//		renderJson(list);
//	}
	
	/**
	 * 根据分类的名称获取美食的列表
	 * 参数：分类id：classifyName
	 * 返回值：食谱的id，名称，时间，收藏数量，简介，图片
	 */
	public void getClassifyListByName(){ 
		HttpServletRequest r = getRequest();
		String classifyName = r.getParameter("classifyName");
		List<Classify> type = Classify.dao.find("select FKclassifyId,classifyId from "
				+ "classify where classifyName=" + "\"" + classifyName + "\"");
		JSONArray json = new JSONArray(type);
		int FKID = json.getJSONObject(0).getInt("FKclassifyId");
		int classifyId = json.getJSONObject(0).getInt("classifyId");
		
		if (FKID == 1) {
			String sql = "select recipesId,recipesName,recipesTime,recipesCollect,recipesIntro,recipesImage from recipes where FKrecipesType="
					+ "\"" + classifyId + "\"";
			List<Recipes> list = Recipes.dao.find(sql);
			renderJson(list);
		}
		if (FKID == 2) {
			String sql = "select recipesId,recipesName,recipesTime,recipesCollect,recipesIntro,recipesImage from recipes where FKrecipesEffect="
					+ "\"" + classifyId + "\"";
			List<Recipes> list = Recipes.dao.find(sql);
			renderJson(list);
		}
		if (FKID == 3) {
			String sql = "select recipesId,recipesName,recipesTime,recipesCollect,recipesIntro,recipesImage from recipes where FKrecipesTaste="
					+ "\"" + classifyId + "\"";
			List<Recipes> list = Recipes.dao.find(sql);
			renderJson(list);
		}
	}
	
	/**
	 * 根据账号密码登录
	 */
	public void isLogin(){
		HttpServletRequest r = getRequest();
		String userAccount = r.getParameter("userAccount");
		String userPassword = r.getParameter("userPassword");
		
		User user = User.dao.findFirst("select * from user where"
				+ " userAccount=" + "\"" + userAccount + "\"");
		if(user != null){
			User user2 = User.dao.findFirst("select userId,userName from user where"
					+ " userAccount=" + "\"" + userAccount + "\"" 
					+ " and userPassword=" + "\"" + userPassword + "\"");
			if(user2 != null){
				//返回用户的id及头像
				renderJson(user2);
			}else{
				//密码不正确
				renderText("2");
			}
		}else{
			//账号不存在
			renderText("0");
		}
	}
	
	/**
	 * 根据用户id获取用户信息
	 * 参数：id：userId
	 */
	public void getUser(){
		HttpServletRequest r = getRequest();
		String userId = r.getParameter("userId");
		
//		String userId = "3";		
		User user = User.dao.findFirst("select * from user where"
				+ " userId=" + "\"" + userId + "\"");
		
		if(user == null){
			//错误
			renderText("0");
		}else{
			//用户信息
			renderJson(user);
		}
	}
	
	/**
	 * 注册接口
	 * 参数：1、账号：userAccount（String类型） 2、密码：userPassword（String类型）
	 * 3、昵称：userName（String类型）4、手机号：userNum（String类型） 5、邮箱：userPost（String类型）
	 */
	public void register(){
		HttpServletRequest r = getRequest();
		String userAccount = r.getParameter("userAccount");
		String userPassword = r.getParameter("userPassword");
		String userName = r.getParameter("userName");
		String userNum = r.getParameter("userNum");
		String userPost = r.getParameter("userPost");
		
		User uAccount = User.dao.findFirst("select * from user where "
				+ "userAccount=" + "\"" + userAccount + "\"");
		User uNum = User.dao.findFirst("select * from user where "
				+ "userNum=" + "\"" + userNum + "\"");
		if (uAccount != null) {
			//账号已存在
			renderText("0");
		}else if(uNum != null){
			//手机号已经使用
			renderText("1");
		}else{
			User user = new User();
			user.setUserAccount(userAccount);
			user.setUserPassword(userPassword);
			user.setUserName(userName);
			user.setUserNum(userNum);
			user.setUserPost(userPost);
			
			try {
				//注册成功
				user.save();
				renderText("2");
			} catch (Exception e) {
				// TODO: handle exception
				//注册失败
				renderText("3");
			}
		}
	}
	
	/**
	 * 判断账号是否存在
	 */
	public void isUserAccount(){
		HttpServletRequest r = getRequest();
		String userAccount = r.getParameter("userAccount");
		User uAccount = User.dao.findFirst("select * from user where "
				+ "userAccount=" + "\"" + userAccount + "\"");
		
		if (uAccount != null) {
			//账号已存在
			renderText("0");
		}else{
			//账号不存在
			renderText("1");
		}
	}
	
	/**
	 * 拾趣收藏接口
	 * 参数：1、用户id：funcollectUser 2、拾趣id：FKfunId 
	 */
	public void funCollect(){
		HttpServletRequest r = getRequest();
		String funcollectUser = r.getParameter("funcollectUser");
		String FKfunId = r.getParameter("FKfunId");
		
		String sql = "select * from funcollect "
				+ "where funcollectUser=" + "\"" + funcollectUser + "\""
				+ " and FKfunId=" + "\"" + FKfunId + "\"";
		Funcollect fun = Funcollect.dao.findFirst(sql);
		
		if (fun != null) {
			//该拾趣已经收藏
			renderText("0");
		}else{
			Funcollect funcollect = new Funcollect();
			funcollect.setFuncollectUser(Integer.parseInt(funcollectUser));
			funcollect.setFKfunId(Integer.parseInt(FKfunId));
			
			try {
				funcollect.save();
				//收藏成功
				renderText("1");
			} catch (Exception e) {
				// TODO: handle exception
				//收藏失败
				renderText("2");
			}
		}
	}
	
	/**
	 * 取消拾趣收藏接口
	 * 参数：1、用户id：funcollectUser 2、拾趣id：FKfunId 
	 */
	public void funCollectDelete(){
		HttpServletRequest r = getRequest();
		String funcollectUser = r.getParameter("funcollectUser");
		String FKfunId = r.getParameter("FKfunId");
		
		String sql = "select * from funcollect "
				+ "where funcollectUser=" + "\"" + funcollectUser + "\""
				+ " and FKfunId=" + "\"" + FKfunId + "\"";
		
		Funcollect fun = Funcollect.dao.findFirst(sql);
		
		JSONObject json = new JSONObject(fun);
		int funcollectId = json.getInt("funcollectId");
		
		if (fun == null) {
			//该拾趣没有收藏
			renderText("0");
		}else{
			boolean b = Funcollect.dao.deleteById(funcollectId);
			if (b) {
				//删除成功
				renderText("1");
			}else{
				//删除失败
				renderText("2");
			}
		}
	}
	
	/**
	 * 查看食谱收藏
	 * 参数：用户id：FKcollectUser
	 */
	public void selectCollectRecipes(){
		HttpServletRequest r = getRequest();
		String FKcollectUser = r.getParameter("FKcollectUser");
		
		String SQL = "select * from collect where FKcollectUser="
				+ "\"" + FKcollectUser + "\"";
		String sql = "select recipesId,recipesName,recipesTime,recipesCollect,recipesIntro,recipesImage from "
				+ "recipes,collect where FKrecipesId=recipesId and "
				+ "FKcollectUser=" + "\"" + FKcollectUser + "\"";
		List<Collect> collect = Collect.dao.find(SQL);
		if (collect == null) {
			//该用户没有收藏食谱
			renderText("0");
		}else{
			//返回该用户收藏的食谱的json串
			List<Recipes> list = Recipes.dao.find(sql);
			renderJson(list);
		}
		
	}
	
	/**
	 * 查看拾趣收藏
	 * 参数：用户id：funcollectUser
	 */
	public void selectCollectFun(){
		HttpServletRequest r = getRequest();
		String funcollectUser = r.getParameter("funcollectUser");
		
		String SQL = "select * from funcollect where funcollectUser="
				+ "\"" + funcollectUser + "\"";
		String sql = "select funId,funTitle,funContent,funImage from "
				+ "fun,funcollect where funId=FKfunId  and "
				+ "funcollectUser=" + "\"" + funcollectUser + "\"";
		
		List<Funcollect> fun = Funcollect.dao.find(SQL);
		if (fun == null) {
			//该用户没有收藏拾趣
			renderText("0");
		}else{
			//返回该用户收藏的拾趣集合的json串
			List<Fun> list = Fun.dao.find(sql);
			renderJson(list);
		}
	}
	
	/**
	 * 根据用户的id获取用户的昵称
	 * 参数：userId
	 */
	public void getUserName(){
		HttpServletRequest r = getRequest();
		String userId = r.getParameter("userId");
		
		String sql = "select userName from user where userId=" + "\"" + 
				userId + "\"";
		User user = User.dao.findFirst(sql);
		
		JSONObject json = new JSONObject(user);
		String userName = json.getString("userName");
		renderText(userName);
	}
	
	/**
	 * 昵称修改
	 * 参数：用户id：userId 修改后昵称：userName
	 */
	public void setUserName(){
		HttpServletRequest r = getRequest();
		String userId = r.getParameter("userId");
		String userName = r.getParameter("userName");
		
		String sql = "select * from user where userId=" + "\"" + 
				userId + "\"";
		User user = User.dao.findFirst(sql);
		
		if (user == null) {
			//用户不存在
			renderText("0");
		}else{
			user.setUserName(userName);
			try {
				user.update();
				//修改成功
				renderText("1");
			} catch (Exception e) {
				// TODO: handle exception
				//修改失败
				renderText("2");
			}
			
		}
	}
	
	/**
	 * 手机号修改
	 * 参数：用户id：userId 修改后手机号：userNum
	 */
	public void setUserNum(){
		HttpServletRequest r = getRequest();
		String userId = r.getParameter("userId");
		String userNum = r.getParameter("userNum");
		
		String sql = "select * from user where userId=" + "\"" + 
				userId + "\"";
		User user = User.dao.findFirst(sql);
		
		if (user == null) {
			//用户不存在
			renderText("0");
		}else{
			user.setUserNum(userNum);
			try {
				user.update();
				//修改成功
				renderText("1");
			} catch (Exception e) {
				// TODO: handle exception
				//修改失败
				renderText("2");
			}
			
		}
	}
	
	/**
	 * 邮箱修改
	 * 参数：用户id：userId 修改后昵称：userPost
	 */
	public void setUserPost(){
		HttpServletRequest r = getRequest();
		String userId = r.getParameter("userId");
		String userPost = r.getParameter("userPost");
		
		String sql = "select * from user where userId=" + "\"" + 
				userId + "\"";
		User user = User.dao.findFirst(sql);
		
		if (user == null) {
			//用户不存在
			renderText("0");
		}else{
			user.setUserPost(userPost);
			try {
				user.update();
				//修改成功
				renderText("1");
			} catch (Exception e) {
				// TODO: handle exception
				//修改失败
				renderText("2");
			}
			
		}
	}
	
	/**
	 * 性别修改
	 * 参数：用户id：userId 修改后昵称：userSex
	 */
	public void setUserSex(){
		HttpServletRequest r = getRequest();
		String userId = r.getParameter("userId");
		String userSex = r.getParameter("userSex");
		
		String sql = "select * from user where userId=" + "\"" + 
				userId + "\"";
		User user = User.dao.findFirst(sql);
		
		if (user == null) {
			//用户不存在
			renderText("0");
		}else{
			user.setUserSex(userSex);
			try {
				user.update();
				//修改成功
				renderText("1");
			} catch (Exception e) {
				// TODO: handle exception
				//修改失败
				renderText("2");
			}
			
		}
	}
	
	public void get(){
		List<Mclassify> list = Mclassify.dao.find("select * from mclassify");
		JSONArray json = new JSONArray(list);
		renderJson(list);
	}
	
	
	/**
	 * 得到美食名称及id
	 */
	public void getRecipesName2(){
		List<Recipes> list = Recipes.dao.find("select recipesName from recipes");
		renderJson(list);
	}
	
	public void set(){
		String url = "http://127.0.0.1:80/http/getCommend";
		String string = url.substring(7, url.indexOf("/", 7));
		url = url.replaceAll(string, "10.7.88.80");
		String u = url.replaceAll(string, "10.7.88.80");
		u = u.substring(u.lastIndexOf("/")-5, u.lastIndexOf("/"));
		url = url.replaceAll(u, "");
		renderText(url);
	}
	
	/**
	 * 按名称搜索
	 * 参数：名称：recipesName
	 */
	public void searchShowByRecipesName(){
		HttpServletRequest r = getRequest();
		String recipesName = r.getParameter("recipesName");
//		String recipesName = "新疆大盘肚";
		if (recipesName == "") {
			renderText("");
			return;
		}else{
			List<Recipes> list = Recipes.dao.find("select recipesId,recipesName,recipesTime,recipesCollect,recipesIntro,recipesImage from"
					+ " recipes");
			JSONArray json = new JSONArray(list);
			
			Map<Integer,Double> map = new TreeMap<>();
			for (int i = 0; i < json.length(); i++) {
				JSONObject jsonObject = json.getJSONObject(i);
				String str = jsonObject.getString("recipesName");
				double f = SimilarDegree(str,recipesName);
				map.put(i, f);
			}
			
			List<Recipes> lists = new ArrayList<>();
			
			int k = map.size();
			for (int i = 0; i < k; i++) {
				int j = getMax(map);
//				lists.add(list.get(j));
				if(map.get(j) != 0){
					lists.add(list.get(j));
				}
				map.remove(j);
			}
			renderJson(lists);
		}
		
	}
	
	/**
	 * 按名称搜索
	 * 参数：名称：recipesName
	 */
	public void searchByRecipesName(){
		HttpServletRequest r = getRequest();
		String recipesName = r.getParameter("recipesName");
		if (recipesName == "") {
			renderText("");
			return;
		}else{
			List<Recipes> list = Recipes.dao.find("select recipesName,recipesId from"
					+ " recipes");
			JSONArray json = new JSONArray(list);
			
			Map<Integer,Double> map = new TreeMap<>();
			for (int i = 0; i < json.length(); i++) {
				JSONObject jsonObject = json.getJSONObject(i);
				String str = jsonObject.getString("recipesName");
				double f = SimilarDegree(str,recipesName);
				map.put(i, f);
			}
			
			List<Recipes> lists = new ArrayList<>();
			
			int k = map.size();
			for (int i = 0; i < k; i++) {
				int j = getMax(map);
//				lists.add(list.get(j));
				if(map.get(j) != 0){
					lists.add(list.get(j));
				}
				map.remove(j);
			}
			renderJson(lists);
		}
		
	}
	
	/**
	 * 按食材搜索
	 * 参数：主料：recipesMfood
	 */
	public void searchByRecipesMfood(){
		HttpServletRequest r = getRequest();
		String recipesMfood = r.getParameter("recipesMfood");
		
		if (recipesMfood == "") {
			renderText("");
			return;
		}else{
			List<Recipes> list = Recipes.dao.find("select recipesId,recipesName,recipesTime,recipesCollect,recipesIntro,recipesImage,recipesMfood from"
					+ " recipes");
			JSONArray json = new JSONArray(list);
			
			Map<Integer,Double> map = new TreeMap<>();
			for (int i = 0; i < json.length(); i++) {
				JSONObject jsonObject = json.getJSONObject(i);
				String str = jsonObject.getString("recipesMfood");
				double f = SimilarDegree(str,recipesMfood);
				map.put(i, f);
			}
			
			List<Recipes> lists = new ArrayList<>();
			
			int k = map.size();
			for (int i = 0; i < k; i++) {
				int j = getMax(map);
//				lists.add(list.get(j));
				if(map.get(j) != 0.0){
					lists.add(list.get(j));
				}
				map.remove(j);
			}
			renderJson(lists);
		}
	}
	
	/**
	 * 按时间搜索
	 * 参数：时间：recipesTime
	 */
	public void searchByRecipesTime(){
		HttpServletRequest r = getRequest();
		String recipesTime = r.getParameter("recipesTime");
		String sTime = recipesTime.split("分")[0];
		int time = Integer.parseInt(sTime);
		
		if (recipesTime == "") {
			renderText("");
			return;
		}else{
			List<Recipes> list = Recipes.dao.find("select recipesId,recipesName,recipesTime,recipesCollect,recipesIntro,recipesImage from"
					+ " recipes where convert(substring(recipesTime,1,instr(recipesTime,\"分\")),SIGNED)"
					+ " <=" + time
					+ " order by convert(substring(recipesTime,1,instr(recipesTime,\"分\")),SIGNED) asc");
			renderJson(list);
		}
	}
	
	public int getMax(Map<Integer,Double> map){
		int i = 0;
		double d = 0.0;
		
		for (Integer key:map.keySet()) {
			if(map.get(key) >= d){
				d = map.get(key);
				i = key;
			}
		}
		return i;
	}
	
	/** 
    
     * 相似度比较 
 
     * @param strA 
 
     * @param strB 
 
     * @return 
 
     */  
  
    public static double SimilarDegree(String strA, String strB){  
  
        String newStrA = removeSign(strA);  
  
        String newStrB = removeSign(strB);  
  
        int temp = Math.max(newStrA.length(), newStrB.length());  
  
        int temp2 = longestCommonSubstring(newStrA, newStrB).length();  
  
        return temp2 * 1.0 / temp;  
  
    }  
    
    private static String removeSign(String str) {  
  	  
        StringBuffer sb = new StringBuffer();  
  
        for (char item : str.toCharArray())  
  
            if (charReg(item)){  
  
  
                sb.append(item);  
  
            }  
  
        return sb.toString();  
  
    }  
    
    private static boolean charReg(char charValue) {  
    	  
        return (charValue >= 0x4E00 && charValue <= 0X9FA5)  
  
                || (charValue >= 'a' && charValue <= 'z')  
  
                || (charValue >= 'A' && charValue <= 'Z')  
  
                || (charValue >= '0' && charValue <= '9');  
  
    }  
  
  
  
    private static String longestCommonSubstring(String strA, String strB) {  
  
        char[] chars_strA = strA.toCharArray();  
  
        char[] chars_strB = strB.toCharArray();  
  
        if (chars_strA.length >= chars_strB.length) {
			int m = chars_strA.length;  
			int n = chars_strB.length; 

			int[][] matrix = new int[m + 1][n + 1];  
			  
	        for (int i = 1; i <= m; i++) {  
	  
	            for (int j = 1; j <= n; j++) {  
	  
	                if (chars_strA[i - 1] == chars_strB[j - 1])  
	  
	                    matrix[i][j] = matrix[i - 1][j - 1] + 1;  
	  
	                else  
	  
	                    matrix[i][j] = Math.max(matrix[i][j - 1], matrix[i - 1][j]);  
	  
	            }  
	  
	        }  
	  
	        char[] result = new char[matrix[m][n]];  
	  
	        int currentIndex = result.length - 1;  
	  
	        while (matrix[m][n] != 0) {  
	  
	            if (matrix[n] == matrix[n - 1])  
	  
	                n--;  
	  
	            else if (matrix[m][n] == matrix[m - 1][n])   
	  
	                m--;  
	  
	            else {  
	  
	                result[currentIndex] = chars_strA[m - 1];  
	  
	                currentIndex--;  
	  
	                n--;  
	  
	                m--;  
	  
	            }  
	        }  
	        
	        return new String(result); 
		}else{
			int n = chars_strA.length;  
			int m = chars_strB.length; 

			int[][] matrix = new int[m + 1][n + 1];  
			  
	        for (int i = 1; i <= m; i++) {  
	  
	            for (int j = 1; j <= n; j++) {  
	  
	                if (chars_strB[i - 1] == chars_strA[j - 1])  
	  
	                    matrix[i][j] = matrix[i - 1][j - 1] + 1;  
	  
	                else  
	  
	                    matrix[i][j] = Math.max(matrix[i][j - 1], matrix[i - 1][j]);  
	  
	            }  
	  
	        }  
	  
	        char[] result = new char[matrix[m][n]];  
	  
	        int currentIndex = result.length - 1;  
	  
	        while (matrix[m][n] != 0) {  
	  
	            if (matrix[n] == matrix[n - 1])  
	  
	                n--;  
	  
	            else if (matrix[m][n] == matrix[m - 1][n])   
	  
	                m--;  
	  
	            else {  
	  
	                result[currentIndex] = chars_strB[m - 1];  
	  
	                currentIndex--;  
	  
	                n--;  
	  
	                m--;  
	  
	            }  
	        }  
	        
	        return new String(result); 
		} 
  
    }  
    /**
     * 修改食谱收藏
     * 参数：operate操作  add表示添加    delete表示删除；
     * 		userId 用户Id
     * 		recipesId 食谱id
     * 返回值： msg 结果   success 表示成功  exist表示重复添加  null表示重复删除
     * 		num 收藏数量
     */
    public void setRecipesCollect(){
    	String Operate = getPara("operate");
		String FKcollectUser =getPara("userId");
		String recipesId = getPara("recipesId");
		Collect c = Collect.dao.findFirst("select * from collect "
				+ "where FKcollectUser=" + "\"" + FKcollectUser + "\""
				+ " and FKrecipesId=" + "\"" + recipesId + "\"");
		Recipes list = Recipes.dao.findFirst("select * from recipes where "
				 + "recipesId=" + "\"" + recipesId + "\"");
		if(Operate.equals("add")){
			if(c == null){
				Collect collect = new Collect();
				collect.setFKcollectUser(Integer.parseInt(FKcollectUser));
				collect.setFKrecipesId(Integer.parseInt(recipesId));
				try {
					list.setRecipesCollect(list.getRecipesCollect() + 1);		
					collect.save();
					list.update();
					//收藏成功
					setAttr("msg","success");
				} catch (Exception e) {
					//收藏失败
					setAttr("msg","false");
				}
			}else{
				setAttr("msg","exist");
			}
		}else{
			if(c != null){
				JSONObject json = new JSONObject(c);
				int collectId = json.getInt("collectId");
				try {
					list.setRecipesCollect(list.getRecipesCollect() - 1);
					Collect.dao.deleteById(collectId);
					list.update();
					//删除成功
					setAttr("msg","success");
				} catch (Exception e) {
					// TODO: handle exception
					//删除失败
					setAttr("msg","false");
				}
			}else{
				setAttr("msg","null");
			}
		}
		setAttr("num",list.getRecipesCollect());
		renderJson();
	}
    /**
     * 修改食趣收藏
     */
    public void setFunCollect(){
    	String Operate = getPara("operate");
		String funcollectUser =getPara("userId");
		String FKfunId = getPara("funId");
		Funcollect fun = Funcollect.dao.findFirst("select * from funcollect "
				+ "where funcollectUser=" + "\"" + funcollectUser + "\""
				+ " and FKfunId=" + "\"" + FKfunId + "\"");
		if(Operate.equals("add")){
			if(fun == null){
				Funcollect funcollect = new Funcollect();
				funcollect.setFuncollectUser(Integer.parseInt(funcollectUser));
				funcollect.setFKfunId(Integer.parseInt(FKfunId));
				try {
					funcollect.save();
					//收藏成功
					setAttr("msg","success");
				} catch (Exception e) {
					//收藏失败
					setAttr("msg","false");
				}
			}else{
				setAttr("msg","exist");
			}
		}else{
			if(fun != null){
				JSONObject json = new JSONObject(fun);
				int funcollectId = json.getInt("funcollectId");
				try {
					Funcollect.dao.deleteById(funcollectId);
					//删除成功
					setAttr("msg","success");
				} catch (Exception e) {
					// TODO: handle exception
					//删除失败
					setAttr("msg","false");
				}
			}else{
				setAttr("msg","null");
			}
		}
		renderJson();
    }
    public void userHeadSculpture(){		
		UploadFile uf = this.getFile();
		String userId = this.getPara("userId");
		String filename = uf.getFileName();
		//获取图片扩展名
		String prefix=filename.substring(filename.lastIndexOf(".")+1);
		
		//更新数据库存储的头像地址
		String sql = "select * from user where userId=" + "\"" + 
					userId + "\"";
		User user = User.dao.findFirst(sql);
		
		if (user == null) {
			//用户不存在
			renderText("0");
		}else{
			String username = user.getUserName();
			uf.getFile().renameTo(new File(PathKit.getWebRootPath() + "\\upload\\" + username +"."+prefix));
			String Url = "http://" + this.getRequest().getRemoteHost() + ":"
					+ this.getRequest().getLocalPort() + "/upload/" + username +"." + prefix;
			user.setUserImage(Url);
			try {
				user.update();
				//修改成功
				renderText("1");
			} catch (Exception e) {
				// TODO: handle exception
				//修改失败
				renderText("2");
			}
		}
		
	}

}