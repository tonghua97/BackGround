package com.foodfun.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CutImage {
	/**
     * 对图片进行剪裁，只保存选中的区域
     * @param img 原图路径
     * @param dest 目标路径
     * @param top
     * @param left
     * @param width
     * @param height
     * @return
     * @throws IOException
     */
    public static boolean saveImage(File img,String dest,int top,int left,int width,int height) throws IOException {
        File fileDest = new File(dest);     
        if(!fileDest.getParentFile().exists())     
            fileDest.getParentFile().mkdirs();     
        BufferedImage bi = (BufferedImage)ImageIO.read(img);     
        height = Math.min(height, bi.getHeight());     
        width = Math.min(width, bi.getWidth());     
        if(height <= 0) height = bi.getHeight();     
        if(width <= 0) width = bi.getWidth();     
        top = Math.min(Math.max(0, top), bi.getHeight()-height);     
        left = Math.min(Math.max(0, left), bi.getWidth()-width);     
                
        BufferedImage bi_cropper = bi.getSubimage(left, top, width, height);     
        return ImageIO.write(bi_cropper, "png", fileDest);
    }
}
