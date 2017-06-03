package cn.yclv;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlParserTool {
	
	public static Set<String> parseHtmlLink(String url){
		Set<String> links = new HashSet<String>();
		Document doc;
		try {
			doc = Jsoup.connect(url).ignoreContentType(true).get();
			Elements linkEles = doc.getElementsByTag("link");
			Elements scriptEles = doc.getElementsByTag("script");
			Elements aEles = doc.getElementsByTag("a");
			Elements imgEles = doc.getElementsByTag("img");
			Elements bgEles = doc.getElementsByAttribute("background");
			for (Element aEle : aEles) {
				String aHref = aEle.attr("href");
				if(!aHref.startsWith("http://") && !aHref.equals("javascript:;")){
					aHref = "http://www.dusso.com.cn/" + aHref;
					String replaceHref = aHref.replace("cn//", "cn/");
					links.add(replaceHref);
				}
			}
			for (Element lEle : linkEles) {
				String linkHref = lEle.attr("href");
				if(linkHref.startsWith("http://") && !linkHref.endsWith("ico")){
					links.add(linkHref);
				}else if(!linkHref.startsWith("http") && !linkHref.endsWith("ico")){
					linkHref = "http://www.dusso.com.cn/" + linkHref;
					links.add(linkHref);
				}
				
			}
			for (Element scriptEle : scriptEles) {
				String scriptSrc = scriptEle.attr("src");
				if(scriptSrc.startsWith("http://")){
					links.add(scriptSrc);
				}
//				String text = scriptEle.text();
//				String html = scriptEle.html();
			}
			for (Element imgEle : imgEles) {
				String imgSrc = imgEle.attr("src");
				if(imgSrc.startsWith("http://")){
					links.add(imgSrc);
				}
			}
			for (Element bgEle : bgEles) {
				String attr = bgEle.attr("background");
				System.out.println(attr);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return links;
	}
	
	public static void main(String[] args) {
//		parseHtmlLink("http://www.dusso.com.cn", );
	}
	
}
