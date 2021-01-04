package com.autopai.common.utils.utils.shitu;

import android.graphics.Bitmap;

import com.autopai.common.utils.utils.file.FileUtils;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;

/**
* 通用物体和场景识别
*/
public class AdvancedGeneral {


    /**
    * 重要提示代码中所需工具类
    * 下载
    */
    public static String advancedGeneral(String path) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/image-classify/v2/advanced_general";
        try {
            // 本地文件路径
            byte[] imgData = FileUtils.readFileByBytes(path);
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = AuthService.getToken();

            String result = HttpUtil.post(url, accessToken, param);
            System.out.println("jjjjjjkkkkkkk: " + result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 重要提示代码中所需工具类
     * 下载
     */
    public static String advancedGeneral(Bitmap bitmap) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/image-classify/v2/advanced_general";
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            /** 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中*/
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);

            byte[] imgData = baos.toByteArray();
            String imgStr = Base64Util.encode(imgData);
            String imgParam = URLEncoder.encode(imgStr, "UTF-8");

            String param = "image=" + imgParam;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = AuthService.getToken();

            String result = HttpUtil.post(url, accessToken, param);
            System.out.println("jjjjjjkkkkkkk: " + result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}