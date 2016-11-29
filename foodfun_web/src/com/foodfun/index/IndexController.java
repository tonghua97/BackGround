package com.foodfun.index;

import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;

/**
 * IndexController
 */
public class IndexController extends Controller {
	public void index() {
		render("index.html");
	}

}
