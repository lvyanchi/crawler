package cn.yclv.tuhu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import cn.yclv.util.FileUtil;
import cn.yclv.util.HttpUtil;

public class ShaCheCrawer {
	
	public static void main(String[] args) {
//		String shacheUrl = "https://item.tuhu.com/Search.html?s=%E5%88%B9%E8%BD%A6%E7%89%87&pageNumber=";
//		for (int i = 1; i <= 56; i++) {
//			String shacheHtmlPage = HttpUtil.buildGet(shacheUrl + i);
//			Document doc = Jsoup.parse(shacheHtmlPage);
//			Elements trEles = doc.getElementsByTag("tr");
//			for (Element trEle : trEles) {
//				Elements trA = trEle.getElementsByClass("DisplayName");
//				Elements trPrice = trEle.getElementsByClass("price");
//				String priceText = trPrice.get(0).children().get(0).text();
//				String proName = trA.get(0).text();
////				FileUtil.writeTxt(content, path)
//				System.out.println("品名：" + proName + ";价格：" + priceText);
//			}
//		}
		
//		String readTxt = FileUtil.readTxt("C:\\Users\\Administrator\\Desktop\\shache.txt");
		File file = new File("C:\\Users\\Administrator\\Desktop\\shache.txt");
		BufferedReader br;
		StringBuffer sb = new StringBuffer();
		try {
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader reader = new InputStreamReader(fis);
			br = new BufferedReader(reader);
			String lineText = null;
			while((lineText = br.readLine()) != null){
				lineText = lineText.replace("品名：", "");
				lineText = lineText.replace("价格：", "");
				String[] split = lineText.split(";");
				sb.append(split[0] + "\t" + split[1] + "\r");
			}
			br.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		System.out.println(sb.toString());
		
		FileUtil.writeTxt(sb.toString(), "C:\\Users\\Administrator\\Desktop\\shacheBak.txt");
	}
}
