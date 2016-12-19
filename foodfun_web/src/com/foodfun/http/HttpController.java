package com.foodfun.http;

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
import com.foodfun.common.model.Recipes;
import com.foodfun.common.model.Test;
import com.foodfun.common.model.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.json.Json;
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
//@Before(BlogInterceptor.class)
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
	 * 获得拾趣的标题
	 */
	public void getFunTitle(){
		List<Fun> list = Fun.dao.find("select funTitle,funImage from fun");
		
		//返回拾趣标题与图片集合的json串
		renderJson(list);
	}
	
	/**
	 * 获得美食名称及收藏数量
	 */
	public void getRecipesName(){
		List<Recipes> list = Recipes.dao.find("select recipesName,recipesCollect from recipes "
				+ "order by recipesCollect desc");
		
		//返回食谱名称及收藏数量集合的json串
		renderJson(list);
	}
	
	/**
	 * 根据recipesId获得美食集合
	 * 参数：recipesId String类型
	 */
	public void getRecipesById(){
		HttpServletRequest r = getRequest();
		String recipesId = r.getParameter("recipesId");
		List<Recipes> list = Recipes.dao.find("select recipesId,recipesName,recipesMfood,recipesFood,recipesLevel,recipesIntro,recipesTime,recipesStep,recipesCollect,recipesImage,recipesEffect,classify.classifyName"
				+ " from recipes"
				+ " join classify on (recipes.FKrecipesTaste = classify.classifyId)"
				+ " where recipesId=" + "\"" + recipesId + "\"");
		//Ret ret = Ret.create("Recipes",list);
		//食谱详情的json串
		//renderJson(ret.getData());
		renderJson(list);
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
		HttpServletRequest r = getRequest();
		String funId = r.getParameter("funId");
		List<Fun> list = Fun.dao.find("select * from fun where "
				+ "funId=" + "\"" + funId + "\"");
		
		//拾趣详细信息的json串
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
	 * 根据分类的id获取美食的列表
	 * 参数：分类id：classifyId 
	 * 返回值：食谱的id，名称，时间，收藏数量，简介，图片
	 */
	public void getClassifyListById(){ 
		HttpServletRequest r = getRequest();
		String classifyId = r.getParameter("classifyId");
		Classify type = Classify.dao.findFirst("select FKclassifyId from "
				+ "classify where classifyId=" + "\"" + classifyId + "\"");
		String str = type.toString();
		String string = str.substring(str.indexOf(":")+1, str.lastIndexOf("}"));
		
		if (string.equals("5")) {
			String sql = "select recipesId,recipesName,recipesTime,recipesCollect,recipesIntro,recipesImage from recipes where FKrecipesType="
					+ "\"" + classifyId + "\"";
			List<Recipes> list = Recipes.dao.find(sql);
			renderJson(list);
		}
		if (string.equals("6")) {
			String sql = "select recipesId,recipesName,recipesTime,recipesCollect,recipesIntro,recipesImage from recipes where FKrecipesType="
					+ "\"" + classifyId + "\"";
			List<Recipes> list = Recipes.dao.find(sql);
			renderJson(list);
		}
		if (string.equals("6")) {
			String sql = "select recipesId,recipesName,recipesTime,recipesCollect,recipesIntro,recipesImage from recipes where FKrecipesType="
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
				+ "userAccount=" + "\"" + userAccount + "\"");
		if(user != null){
			User user2 = User.dao.findFirst("select userId,userImage from user where"
					+ "userAccount=" + "\"" + userAccount + "\"" 
					+ "userPassword=" + "\"" + userPassword + "\"");
			if(user2 != null){
				//返回用户的id及头像
				renderJson(user2);
			}else{
				renderText("2");
			}
		}else{
			renderText("0");
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
			renderText("0");
		}else if(uNum != null){
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
	 * 食谱收藏接口
	 * 参数：1、用户id：FKcollectUser 2、食谱id：FKrecipesId
	 */
	public void recipesCollect(){
		HttpServletRequest r = getRequest();
		String FKcollectUser = r.getParameter("FKcollectUser");
		String FKrecipesId = r.getParameter("FKrecipesId");
		
		Collect c = Collect.dao.findFirst("select * from collect "
				+ "where collectUser=" + "\"" + FKcollectUser + "\""
				+ " and FKcollectId=" + "\"" + FKrecipesId + "\"");
		
		if (c != null) {
			//该食谱已经收藏
			renderText("0");
		}else{
			Collect collect = new Collect();
			collect.setFKcollectUser(Integer.parseInt(FKcollectUser));
			collect.setFKrecipesId(Integer.parseInt(FKrecipesId));
			
			try {
				collect.save();
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
	 * 取消食谱收藏接口
	 * 参数：1、用户id：FKcollectUser 2、食谱id：FKrecipesId 
	 */
	public void recipesCollectDelete(){
		HttpServletRequest r = getRequest();
		String FKrecipesId = r.getParameter("FKrecipesId");
		String FKcollectUser = r.getParameter("FKcollectUser");
		Collect c = Collect.dao.findFirst("select * from collect "
				+ "where collectUser=" + "\"" + FKcollectUser + "\""
				+ " and FKcollectId=" + "\"" + FKrecipesId + "\"");
		
		if (c == null) {
			//此食谱没有收藏
			renderText("0");
		}else{
			String str = c.toString();
			String string = str.substring(str.indexOf(":")+1, str.lastIndexOf("}"));
			boolean b = Collect.dao.deleteById(string);
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
	 * 拾趣收藏接口
	 * 参数：1、用户id：funcollectUser 2、拾趣id：FKfunId 
	 */
	public void funCollect(){
		HttpServletRequest r = getRequest();
		String funcollectUser = r.getParameter("funcollectUser");
		String FKfunId = r.getParameter("FKfunId");
		
		String sql = "select * from collect "
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
		
		String sql = "select * from collect "
				+ "where funcollectUser=" + "\"" + funcollectUser + "\""
				+ " and FKfunId=" + "\"" + FKfunId + "\"";
		
		Funcollect fun = Funcollect.dao.findFirst(sql);
		
		if (fun == null) {
			//该拾趣没有收藏
			renderText("0");
		}else{
			String str = fun.toString();
			String string = str.substring(str.indexOf(":")+1, str.lastIndexOf("}"));
			boolean b = Funcollect.dao.deleteById(string);
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
		String sql = "select recipesId,recipesName,recipesIntro,recipesImage from "
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
	 * 参数：用户id：userId 修改后昵称：userNum
	 */
	public void setUserNum(){
		HttpServletRequest r = getRequest();
		String userId = r.getParameter("userId");
		String userNum = r.getParameter("userNum");
		
		String sql = "select * from user where userNum=" + "\"" + 
				userNum + "\"";
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
		
		String sql = "select * from user where userPost=" + "\"" + 
				userPost + "\"";
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
		
		String sql = "select * from user where userSex=" + "\"" + 
				userSex + "\"";
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
		System.out.println(json);
		renderJson(list);
	}
	
	/**
	 * 统计数量
	 */
	public void Count(){
		Mclassify list = Mclassify.dao.findFirst("select count(*) from mclassify");
		System.out.println(list.toString());
		String str = list.toString();
		String string = str.substring(str.indexOf(":")+1, str.lastIndexOf("}"));
		System.out.println(string);
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
			System.out.println(map);
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
			List<Recipes> list = Recipes.dao.find("select recipesName,recipesMfood,recipesId from"
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
				if(map.get(j) != 0){
					lists.add(list.get(j));
				}
				map.remove(j);
			}
			System.out.println(map);
			renderJson(lists);
		}
	}
	
	/**
	 * 按食材搜索
	 * 参数：时间：recipesTime
	 */
	public void searchByRecipesTime(){
		HttpServletRequest r = getRequest();
		String recipesTime = r.getParameter("recipesTime");
		
		if (recipesTime == "") {
			renderText("");
			return;
		}else{
			List<Recipes> list = Recipes.dao.find("select recipesName,recipesTime,recipesId from"
					+ " recipes where recipesTime <=" + "\"" + recipesTime + "\""
					+ " order by recipesTime asc");
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
				System.out.println(key);
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
  
                //System.out.println("--"+item);  
  
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
	        
	        System.out.println(new String(result));
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
	        
	        System.out.println(new String(result));
	        return new String(result); 
		} 
  
    }  
}