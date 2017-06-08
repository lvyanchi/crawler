package cn.yclv.test;

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
	static String filePath = "E:\\lvyanchi\\sucai\\rename-sucai";
	static String imageDomain = "http://10022671.s21i-10.faiusr.com";
	static String cssDomain = "http://2.ss.faisys.com";
	static String jsDomain = "http://1.ss.faisys.com";
	static String jspContextPath =  "<%=request.getContextPath() %>";
	static String jspPage = "<%@ page contentType=\"text/html;charset=UTF-8\" language=\"java\" pageEncoding=\"UTF-8\" %><br>";

	public static void main(String[] args) {
//		for (int i = 0; i < 2; i++) {
//			renameManyFile(filePath);
//		}
//		renameJspFile(filePath);
		 rewriteManyFile(filePath);
//		renamePdFile(filePath);
//		 rewirteModuleFile(filePath);
//		replaceJspContextPath(filePath);
	}
	
	private static void renamePdFile(String filePath2) {
		File[] files = getFilesByPath(filePath);
		if (files != null) {
			for (File f : files) {
				if (f.getName().startsWith("nd") && f.getName().contains("#module") && !f.getName().endsWith(".css")) {
					String name = f.getName();
					name = name.substring(0, name.indexOf("#"));
					FileUtil.moveFile(f.getAbsolutePath(), filePath + "\\" + name, true);
				} 
			}
		}
	}

	private static void rewirteModuleFile(String filePath) {
		File[] files = getFilesByPath(filePath);
		if (files != null) {
			for (File file : files) {
				String fileName = file.getName();
				try {
					if (fileName.startsWith("nd")) {
						String colHtml = parseModuleHtml(file);
						FileUtil.writeTxt(colHtml, file.getAbsolutePath());
						System.out.println(fileName + "更新了");
					} 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void rewrietTempFile(String filePath2) {
		File[] files = getFilesByPath(filePath);
		if (files != null) {
			for (File file : files) {
				String fileName = file.getName();
				try {
					if (fileName.startsWith("pd")) {
						String colHtml = parseTempHtml(file);
						FileUtil.writeTxt(colHtml, file.getAbsolutePath());
						System.out.println(fileName + "更新了");
					} 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
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
					if (/*fileName.startsWith("col") || fileName.equals("index.jsp")
							|| fileName.equals("contact.jsp")
							|| fileName.equals("about.jsp")
							|| */fileName.equals("nr.jsp")
							) {
						String colHtml = parseHtml(file);
						colHtml = jspPage + colHtml;
						FileUtil.writeTxt(colHtml, file.getAbsolutePath());
						System.out.println(fileName + "更新了");
					} else if (fileName.startsWith("pd")) {
//						String colHtml = parseHtml(file);
//						colHtml = jspPage + colHtml;
//						FileUtil.writeTxt(colHtml, file.getAbsolutePath());
//						System.out.println(fileName + "更新了");
					} else if (fileName.startsWith("nd")) {
//						String colHtml = parseHtml(file);
//						colHtml = jspPage + colHtml;
//						FileUtil.writeTxt(colHtml, file.getAbsolutePath());
//						System.out.println(fileName + "更新了");
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
	
	
	public static String parseTempHtml(File file){
		String fileContent = FileUtil.readTxt(file.getAbsolutePath());
		Entities.EscapeMode.base.getMap().clear();
		Document doc = Jsoup.parse(fileContent);
		Element detailedDescEle = doc.getElementById("detailedDesc");
		if(detailedDescEle.hasAttr("style")){
			detailedDescEle.attr("style", "");
		}
		return doc.html();
	}
	
	
	public static String parseModuleHtml(File file){
		String fileContent = FileUtil.readTxt(file.getAbsolutePath());
		Entities.EscapeMode.base.getMap().clear();
		Document doc = Jsoup.parse(fileContent);
		Elements aTags = doc.getElementsByTag("a");
		for (Element aTag : aTags) {
			if(aTag.hasAttr("href")){
				String href = aTag.attr("href");
				if(href.indexOf("#module") > -1){
					String preHref = href.substring(0, href.indexOf("#"));
					String suffHref = href.substring(href.lastIndexOf("."), href.length());
					href =  preHref + suffHref;
					aTag.attr("href", href);
				}
			}
		}
		return doc.html();
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
		Element gMainEle = doc.getElementById("g_main");
		Element gMainPre = gMainEle.previousElementSibling();
		String gMainPreHtml = gMainPre.html();
		if(gMainPreHtml.contains("(function(FUN,undefined)")){
			gMainPre.html("");
			gMainPre.attr("src", jspContextPath + "/js/index.js");
		}
		
		Element detailedDescEle = doc.getElementById("detailedDesc");
		if(detailedDescEle != null && detailedDescEle.hasAttr("style")){
			detailedDescEle.attr("style", "");
		}
		
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
						linkHref = jspContextPath + "/css/jzcus/" + linkHref;
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
					if(href.contains("pd")){
						href = href.replace("?", "_");
						href = href.replace(".jsp", "");
						href += ".jsp";
						href = jspContextPath + "/" + href;
						aEle.attr("href", href);
					}else if(href.contains("nd")){
						href = href.replace("?", "_");
						href = href.replace(".jsp", "");
						href += ".jsp";
						href = jspContextPath + "/" + href;
						aEle.attr("href", href);
					}else{
						href = href.replace("?", "_");
						href = href.replace(".jsp", "");
						href += ".jsp";
						href = jspContextPath + href;
						aEle.attr("href", href);
					}
				}
			}
		}
		
//		for (Element aEle : itemCenterEles) {
//			Element firstChild = aEle.child(0);
//			String href = firstChild.attr("href");
//			if(firstChild.hasAttr("href") && !href.equals("javascript:;")){
//				if(href.equals("/")){
//					
//				}else{
//					href = href.replace(jspContextPath, "");
//					href = jspContextPath + href;
//					aEle.attr("href", href);
//				}
//			}
//		}
//		for (Element aEle : itemCenterEles) {
//			Element firstChild = aEle.child(0);
//			String href = firstChild.attr("href");
//			if(firstChild.hasAttr("href") && !href.equals("javascript:;")){
//				if(href.equals("/")){
//					
//				}else{
//					href = href.replace("?", "_");
//					href = href.replace(".jsp", "");
//					href += ".jsp";
//					href = jspContextPath + href;
//					aEle.attr("href", href);
//				}
//			}
//		}
		
		for (Element tableEle : itemEles) {
			String onclickVal = tableEle.attr("onclick");
			onclickVal = onclickVal.substring(onclickVal.indexOf("(\"") + 2, onclickVal.indexOf(",") - 1);
			if(!onclickVal.equals("/")){
				onclickVal = onclickVal.replace("?", "_");
				onclickVal = onclickVal.replace(".jsp", "");
				onclickVal += ".jsp";
				onclickVal = jspContextPath + onclickVal;
			}
			tableEle.attr("onclick", "window.open('" + onclickVal + "', '_self')");
			tableEle.attr("_jump", "window.open('" + onclickVal + "', '_self')");
		}
		return doc.html();
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
				/*if(name.endsWith(".jpeg")){
					name = name.replace("10022671.s21i-10.faiusr.com", "");
					name = name.replace(".jpeg", "");
					FileUtil.moveFile(f.getAbsolutePath(), filePath + "\\" + name, true);
				}
				if( name.endsWith(".png")){
					name = name.replace("10022671.s21i-10.faiusr.com", "");
					name = name.replace(".png", "");
					name += ".png";
					FileUtil.moveFile(f.getAbsolutePath(), filePath + "\\" + name, true);
				}
				if( name.endsWith(".gif")){
					name = name.replace("10022671.s21i-10.faiusr.com", "");
					name = name.replace(".gif", "");
					name += ".gif";
					FileUtil.moveFile(f.getAbsolutePath(), filePath + "\\" + name, true);
					
				}*/
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
