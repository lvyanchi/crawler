package cn.yclv.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.jsoup.select.Elements;

import cn.yclv.util.FileUtil;

public class FileTest {
	static String filePath = "E:\\java\\lb\\dusso\\bak";
	static String imageDomain = "http://10022671.s21i-10.faiusr.com";
	static String cssDomain = "http://2.ss.faisys.com";
	static String jsDomain = "http://1.ss.faisys.com";
	static String jspContextPath =  "<%=request.getContextPath() %>";

	public static void main(String[] args) {
		for (int i = 0; i < 2; i++) {
			renameManyFile(filePath);
		}
		renameJspFile(filePath);
		 rewriteManyFile(filePath);
//		replaceJspContextPath(filePath);
	}
	
	public static File[] getFilesByPath(String filePath) {
		File[] files = null;
		File file = new File(filePath);
		if (file.isDirectory()) {
			files = file.listFiles();
		}
		return files;
	}

	public static void rewriteManyFile(String filePath) {
		File[] files = getFilesByPath(filePath);
		if (files != null) {
			for (File file : files) {
				String fileName = file.getName();
				try {
					if (fileName.startsWith("col") || fileName.equals("index.jsp")) {
						String colHtml = parseHtml(file);
						FileUtil.writeTxt(colHtml, file.getAbsolutePath());
						System.out.println(fileName + "更新了");
					} else if (fileName.startsWith("pd")) {
						
					} else if (fileName.startsWith("nd")) {
						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	
	public static void replaceJspContextPath(String filePath) {
		File[] files = getFilesByPath(filePath);
		if (files != null) {
			for (File file : files) {
				String fileName = file.getName();
				try {
					if (fileName.startsWith("col") /*|| fileName.equals("index.jsp")*/) {
						String readTxt = FileUtil.readTxt(file.getAbsolutePath());
						String replaceTxt = readTxt.replace("&lt;%=request.getContextPath() %&gt;", "<%=request.getContextPath() %>");
						FileUtil.writeTxt(replaceTxt, file.getAbsolutePath());
						System.out.println(fileName + "更新了");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	public static String parseHtml(File file) {
		String fileContent = FileUtil.readTxt(file.getAbsolutePath());
		Entities.EscapeMode.base.getMap().clear();
		Document doc = Jsoup.parse(fileContent);
		Elements itemCenterEles = doc.getElementsByClass("itemCenter");
		Elements itemEles = doc.getElementsByClass("item");
		Element webNavEle = doc.getElementById("webNav");
		Elements navAEles = webNavEle.select("a");
		Elements linkTags = doc.getElementsByTag("link");
		Elements scriptTags = doc.getElementsByTag("script");
		Elements imgTags = doc.getElementsByTag("img");
		Elements aTags = doc.getElementsByTag("a");
		for (Element linkTag : linkTags) {
			if(linkTag.hasAttr("href")){
				String linkHref = linkTag.attr("href");
				if(linkHref.startsWith(cssDomain)){
					linkHref = linkHref.replace(cssDomain, jspContextPath);
					linkTag.attr("href", linkHref);
				}else if(linkHref.startsWith(imageDomain)){
					linkHref = linkHref.replace(imageDomain, jspContextPath + "/image");
					linkTag.attr("href", linkHref);
				}
				if(linkHref.contains("jzcusstyle.jsp")){
					if(!linkHref.endsWith(".css")){
						linkHref = linkHref.replace("?", "_");
						linkHref += ".css";
						linkTag.attr("href", linkHref);
					}
				}
			}
		}
		
		for (Element scriptTag : scriptTags) {
			if(scriptTags.hasAttr("src")){
				String scriptSrc = scriptTag.attr("src");
				if(scriptSrc.startsWith(jsDomain)){
					scriptSrc = scriptSrc.replace(jsDomain, jspContextPath);
					scriptTag.attr("src", scriptSrc);
				}
			}
		}
		
		for (Element imgTag : imgTags) {
			if(imgTag.hasAttr("src")){
				String imgSrc = imgTag.attr("src");
				if(imgSrc.startsWith(imageDomain)){
					imgSrc = imgSrc.replace(imageDomain, jspContextPath);
					imgTag.attr("src", imgSrc);
					imgTag.attr("imgpath", imgSrc);
				}
			}
		}
		
		for (Element aEle : aTags) {
			String href = aEle.attr("href");
			if(aEle.hasAttr("href") && !aEle.attr("href").equals("javascript:;")){
				if(href.equals("/")){
					
				}else{
					href = href.replace("?", "_");
					href = href.replace(".jsp", "");
					href += ".jsp";
					aEle.attr("href", href);
				}
			}
		}
		
		for (Element aEle : itemCenterEles) {
			Element firstChild = aEle.child(0);
			String href = firstChild.attr("href");
			if(firstChild.hasAttr("href")){
				if(href.equals("/")){
					
				}else{
					href = href.replace("?", "_");
					href = href.replace(".jsp", "");
					href += ".jsp";
					aEle.attr("href", href);
				}
			}
		}
		
		for (Element tableEle : itemEles) {
			String onclickVal = tableEle.attr("onclick");
			onclickVal = onclickVal.substring(onclickVal.indexOf("(\"") + 2, onclickVal.indexOf(",") - 1);
			if(!onclickVal.equals("/")){
				onclickVal = onclickVal.replace("?", "_");
				onclickVal = onclickVal.replace(".jsp", "");
				onclickVal += ".jsp";
			}
			tableEle.attr("onclick", "window.open('" + onclickVal + "', '_self')");
			tableEle.attr("_jump", "window.open('" + onclickVal + "', '_self')");
		}
		return doc.body().html();
	}

	
	public static void renameManyFile(String filePath) {
		File[] files = getFilesByPath(filePath);
		if(files != null){
			for (File f : files) {
				String name = f.getName();
				if(name.startsWith("www.dusso.com.cn_")){
					name = name.replace("www.dusso.com.cn_", "");
					FileUtil.moveFile(f.getAbsolutePath(), filePath + "\\" + name, true);
				}
				if(name.startsWith("www.dusso.com.cn")){
					if(name.equals("www.dusso.com.cn.html")){
						name = name.replace("www.dusso.com.cn", "index.jsp");
						FileUtil.moveFile(f.getAbsolutePath(), filePath + "\\" + name, true);
					}else{
						name = name.replace("www.dusso.com.cn", "");
						FileUtil.moveFile(f.getAbsolutePath(), filePath + "\\" + name, true);
					}
				}
				if(name.endsWith(".x-javascript")){
					name = name.replace(".x-javascript", "");
					FileUtil.moveFile(f.getAbsolutePath(), filePath + "\\" + name, true);
				}
				if(name.endsWith("html")){
					name = name.replace(".html", "");
					FileUtil.moveFile(f.getAbsolutePath(), filePath + "\\" + name, true);
				}
				if(name.endsWith("; charset=UTF-8")){
					name = name.replace("; charset=UTF-8", "");
					FileUtil.moveFile(f.getAbsolutePath(), filePath + "\\" + name, true);
				}
			}
		}
	}

	public static void renameJspFile(String filePath) {
		File[] files = getFilesByPath(filePath);
		if (files != null) {
			for (File f : files) {
				if (f.getName().contains(".jsp") && !f.getName().contains("#") && !f.getName().endsWith(".css")) {
					String name2 = f.getName();
					String name3 = name2.replace(".jsp", "");
					String name4 = name3 + ".jsp";
					FileUtil.moveFile(f.getAbsolutePath(), filePath + "\\" + name4, true);
				} else if (f.getName().contains(".jsp") && f.getName().contains("#")) {
					String name2 = f.getName();
					String module = name2.substring(name2.indexOf("#"), name2.length());
					String name3 = name2.replace(".jsp", "");
					name3 = name3.replace(module, "");
					String name4 = name3 + ".jsp";
					String name5 = name4 + module;
					FileUtil.moveFile(f.getAbsolutePath(), filePath + "\\" + name5, true);
				}
			}
		}
	}
}
