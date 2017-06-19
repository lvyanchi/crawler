package cn.yclv.shache.jd;

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

import com.google.gson.Gson;

import cn.yclv.model.jd.PriceResult;
import cn.yclv.util.FileUtil;
import cn.yclv.util.HttpUtil;

public class ShaCheCrawer {
	
	static String shacheUrlPre = "https://list.jd.com/list.html?cat=6728,6742,11859&ev=3680_69977%7C%7C76147&page=";
	static String shacheUrlSuff = "&sort=sort_totalsales15_desc&trans=1&JL=6_0_0&ms=6#J_main";
	static String boshShaCheUrlPre = "https://list.jd.com/list.html?cat=6728,6742,11859&ev=exbrand_5125&page=";
	static String boshShaCheUrlSuff = "&delivery=1&sort=sort_totalsales15_desc&trans=1&JL=6_0_0#J_main";
	static String ferodoShaCheUrl = "https://list.jd.com/list.html?cat=6728,6742,11859&ev=exbrand_6816&delivery=1&sort=sort_totalsales15_desc&trans=1&JL=2_1_0#J_crumbsBar";
	static String queryPriceUrlPre = "https://p.3.cn/prices/mgets?callback=jQuery5312593&ext=10000000&type=1&area=1_72_2799_0&skuIds=J_";
	static String queryPriceUrlSuff = "&pdbp=0&pdtk=&pdpin=&pduid=1497837842964260037671&source=list_pc_front&_=1497841796348";
	
	public static void main(String[] args) {
		crawlerShachepian();
	}


	public static void crawlerBoshShaChe() {
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i <= 3; i++) {
			String shacheHtmlPage = HttpUtil.buildGet("https://mall.jd.com/view_search-420967-5626177-1-0-24-1.html");
			Document doc = Jsoup.parse(shacheHtmlPage);
			String html = doc.html();
			System.out.println(html);
			Elements jSubObjectEles = doc.getElementsByClass("jSubObject");
			for (Element jSubObjectEle : jSubObjectEles) {
				Elements aEle = jSubObjectEle.children().select("a");
				System.out.println(aEle.attr("href"));
			}
		}
	}


	public static void crawlerShachepian() {
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i < 2; i++) {
			String shacheHtmlPage = HttpUtil.buildGet(ferodoShaCheUrl);
			Document doc = Jsoup.parse(shacheHtmlPage);
			Elements glitemEles = doc.getElementsByClass("gl-item");
			for (Element glitemEle : glitemEles) {
				Elements pNameEles = glitemEle.getElementsByClass("p-name");
				String pNameText = pNameEles.get(0).getElementsByTag("em").get(0).text();
				String dataSku = glitemEle.children().get(0).attr("data-sku");
				PriceResult pr = null;
				Double price = 0D;
				if(dataSku != null && !dataSku.equals("")){
					pr = getPriceByDataSku(dataSku);
					price = pr.getOp();
				}
				sb.append(pNameText + "\t" + price + "\r");
				System.out.println("品名:" + pNameText + "; 价格:" + price);
			}
		}
		FileUtil.writeTxt(sb.toString(), "C:\\Users\\Administrator\\Desktop\\ferodoShaChe.txt");
	}

	
	/**
	 * 京东根据dataSku参数, 查找价格数据
	 * @param dataSku
	 * @return
	 */
	public static PriceResult getPriceByDataSku(String dataSku) {
		String priceResult = HttpUtil.buildGet(queryPriceUrlPre + dataSku + queryPriceUrlSuff);
		priceResult = priceResult.replace("jQuery5312593([", "");
		priceResult = priceResult.replace("]);", "");
		PriceResult pr = new Gson().fromJson(priceResult, PriceResult.class);
		return pr;
	}
	
	
	public static void resetTxt() {
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
