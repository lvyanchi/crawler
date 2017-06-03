package cn.yclv.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import cn.yclv.DownLoadFile;
import cn.yclv.constrant.StringConstants;


public class HttpUtil {
	
	public static void main(String[] args) throws IOException, SQLException {
		
	}


	/**
	 * 执行get请求
	 * @param url
	 * @return
	 */
	public static String buildGet(String url){
		String entityStr = "";
		String htmlContent = "";
		try {
			HttpResponse response = getBuildGetResponse(url);
			HttpEntity entity = response.getEntity();
			Header contentEncoding = entity.getContentEncoding();
			Header contentType = entity.getContentType();
			InputStream in = null;
			if(contentType.getValue().toLowerCase().contains("image")){
				FileUtil.downloadPicture(url, StringConstants.SAVE_PATH, DownLoadFile.getFileNameByUrl(url, contentType.getValue()));
				return null;
			}else if(contentEncoding != null){
				if(contentEncoding.getValue().toLowerCase().contains("gzip")){
					GzipDecompressingEntity gzipEntity = new GzipDecompressingEntity(entity);  
					in = gzipEntity.getContent();  
				}else{  
					in = entity.getContent();  
				}
				htmlContent = getHTMLContent(in);
				return htmlContent;
			}else {
				entityStr = EntityUtils.toString(entity, "UTF-8");
				return entityStr;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}


	public static HttpResponse getBuildGetResponse(String url) throws IOException, ClientProtocolException {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		HttpResponse response = null;
		response = httpclient.execute(httpget);
		return response;
	}
	
	
	/**
	 * 执行post请求
	 * 需要添加json请求参数
	 * @param url
	 * @return
	 */
	public static String buildPost(String url, String requestJson){
        DefaultHttpClient httpclient = new DefaultHttpClient();  
        HttpPost httppost = new HttpPost(url);  
        StringEntity entity = null;
        String responseJson = "";
		try {
			entity = new StringEntity(requestJson,"utf-8");
			//解决中文乱码问题    
	        entity.setContentEncoding("UTF-8");    
	        entity.setContentType("application/json");
            httppost.setEntity(entity);  
            HttpResponse response = httpclient.execute(httppost);  
            HttpEntity responseEntity = response.getEntity();  
            if (responseEntity != null) {
            	responseJson = EntityUtils.toString(responseEntity, "UTF-8");
            }
        } catch (Exception e) {  
            e.printStackTrace();  
        }
        return responseJson;
	}
	
	
	/**
	 * 模拟登陆获取cookie
	 * @param url
	 * @param param
	 * @return
	 */
	public static String getLoginCookie(String loginUrl, String name, String password){
		String set_cookie = "";
	 	DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(loginUrl);  
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("name", name));
			params.add(new BasicNameValuePair("password", password));
			buildEncode(httppost, params);
            HttpResponse response = httpclient.execute(httppost); 
            
            set_cookie = response.getFirstHeader("Set-Cookie").getValue();
            set_cookie = set_cookie.substring(0,set_cookie.indexOf(";"));
        } catch (Exception e) {  
            e.printStackTrace();  
        }
        return set_cookie;
	}
	
	
	
	/**
	 * 执行post请求
	 * @param url
	 * @return
	 */
	public static String buildPost(String url, String loginUrl, String mobile, String code){
		String responseJson = "";
		DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);  
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		try {
			buildEncode(httppost, params);
            httppost.setHeader("Cookie", getLoginCookie(loginUrl, mobile, mobile));
            HttpResponse response = httpclient.execute(httppost); 
            HttpEntity responseEntity = response.getEntity();  
            if (responseEntity != null) {
            	responseJson = EntityUtils.toString(responseEntity, "UTF-8");
            	System.out.println(responseJson);
            }
        } catch (Exception e) {  
            e.printStackTrace();  
        }
        return responseJson;
	}


	public static void buildEncode(HttpPost httppost, List<NameValuePair> params) throws UnsupportedEncodingException {
		UrlEncodedFormEntity uefEntity;
		uefEntity = new UrlEncodedFormEntity(params, "UTF-8");
		uefEntity.setContentEncoding("UTF-8");    
		uefEntity.setContentType("application/json");
		httppost.setEntity(uefEntity);
	}
	
	
	
	public static String getHTMLContent(InputStream in) {  
        StringBuffer sb = new StringBuffer();  
        BufferedReader br = new BufferedReader(new InputStreamReader(in));  
        try {  
            String line = null;  
            while((line=br.readLine())!=null){  
                sb.append(line);  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        }finally{  
            try {  
                br.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return sb.toString();  
    }  
	
	
}

