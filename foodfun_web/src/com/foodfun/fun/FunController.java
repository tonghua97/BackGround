package com.foodfun.fun;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.foodfun.common.model.Classify;
import com.foodfun.common.model.Fun;
import com.foodfun.common.model.Mclassify;
import com.foodfun.common.model.Recipes;
import com.foodfun.common.model.Test;
import com.foodfun.utils.CutImage;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.render.JsonRender;
import com.jfinal.upload.UploadFile;

/**
 * FunController
 */
//@Before(BlogInterceptor.class)
public class FunController extends Controller {
	public void index() {
		setAttr("FunPage", Fun.dao.paginate(getParaToInt(0, 1), 20));
		render("fun.html");
	}

	public void add() {
		
	}
	
	public void upload(){
		// 获取上传的文件
		UploadFile uf = getFile("img_poster");
		//获取文件名
		String fileName = uf.getFileName();
		
		// 拼接文件上传的完整路径
		String fileUrl = "http://" + this.getRequest().getRemoteAddr() + ":"
				+ this.getRequest().getLocalPort() + "/upload/"
				+ uf.getFileName();
		//返回图片名称、url路径
		setAttr("fileName", fileName);
		setAttr("fileUrl", fileUrl);
			
		//以json格式进行渲染
		render(new JsonRender().forIE());
		//renderJson();
	}
	
	public void ajaxImageCut() throws IOException{
        String imageName =getPara("imgName");
        int left= getParaToInt("x1");
        int top= getParaToInt("y1");
        int width= getParaToInt("cw");
        int height= getParaToInt("ch");
        System.out.println("================"+left);
     
        File source = new File(PathKit.getWebRootPath() + "\\upload\\" + imageName);
        String fileName = source.getName();
        //String prefix=fileName.substring(fileName.lastIndexOf(".")+1);
        System.out.println(fileName);
        
        boolean isSave = CutImage.saveImage(source,PathKit.getWebRootPath()+"\\upload\\img\\" + fileName,top,left,width,height);
        if(isSave){
        	String url = "http://" + this.getRequest().getRemoteAddr() + ":"
    				+ this.getRequest().getLocalPort() + "/upload/img/"
    				+ fileName;
            setAttr("msg", "图片更新成功！");
            setAttr("url", url);
        }else{
        	setAttr("msg", "图片更新失败！");
        }
        renderJson();
	}
	
	
	public void save() {
		getModel(Fun.class).save();
		redirect("/fun");
	}
	
	public void edit() {
//		setAttr("recipes", Recipes.dao.findById(getParaToInt()));
		setAttr("fun", Fun.dao.findById(getPara()));
	}
	
	public void update() {
		getModel(Fun.class).update();
		redirect("/fun");
	}
	
	public void delete() {
//		Recipes.dao.deleteById(getParaToInt());
		Fun.dao.deleteById(getPara());
		redirect("/fun");
	}
}