package cn.yclv;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.yclv.constrant.StringConstants;
import cn.yclv.util.FileUtil;
import cn.yclv.util.HttpUtil;

public class DownLoadFile {
	/**
	 * 根据 url 和网页类型生成需要保存的网页的文件名 去除掉 url 中非文件名字符
	 */
	public static String getFileNameByUrl(String url,String contentType){
		//remove http://
		url = url.substring(7);
		//text/html类型
		if(contentType.indexOf("html") != -1){
			url= url.replaceAll("[\\?/:*|<>\"]", "_")+".html";
			return url;
		}else{
			//如application/pdf类型
	        return url.replaceAll("[\\?/:*|<>\"]", "_")+"."+
	        contentType.substring(contentType.lastIndexOf("/")+1);
		}	
	}

	/**
	 * 保存网页字节数组到本地文件 filePath 为要保存的文件的相对地址
	 */
	private static void saveToLocal(byte[] data, String filePath) {
		try {
			File file = FileUtil.createFile(filePath);
			DataOutputStream out = new DataOutputStream(new FileOutputStream(file));
			for (int i = 0; i < data.length; i++){
				out.write(data[i]);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/* 下载 url 指向的网页 */
	public static boolean downloadFile(String url) {
		String filePath = null;
		try {
			String responseStr = HttpUtil.buildGet(url);
			if(responseStr != null && !responseStr.equals("")){
				filePath = StringConstants.SAVE_PATH
						+ getFileNameByUrl(url, HttpUtil.getBuildGetResponse(url).getFirstHeader(
								"Content-Type").getValue());
				saveToLocal(responseStr.getBytes(), filePath);
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			//发生致命的异常，可能是协议不对或者返回的内容有问题
			System.out.println("Please check your provided http address!");
			e.printStackTrace();
			return false;
		} 
	}
}
