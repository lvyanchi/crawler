package cn.yclv;

import java.util.HashSet;
import java.util.Set;

public class Crawling{
	
	public static void main(String[]args){
		Crawling crawler = new Crawling();
		crawler.crawling(new String[]{"http://www.dusso.com.cn/"});
	}
	
	/**
	 * 抓取过程
	 * @return
	 * @param seeds
	 */
	public void crawling(String[] seeds){
		//初始化 URL队列
		initCrawlerWithSeeds(seeds);
		int count = 1;
		while(!LinkQueue.unVisitedUrlIsEmpty()){
			if(count >= 100){
				System.out.println("count>=100");
			}
			String visitingUrl = (String) LinkQueue.unVisitedUrlDeQueue();
			if(visitingUrl == null){
				continue;
			}
			boolean downloadResult = DownLoadFile.downloadFile(visitingUrl);
			if(downloadResult){
				System.out.println(visitingUrl + "下载完成");
			}
			LinkQueue.addVisitedUrl(visitingUrl);
			//提取出下载网页中的 URL
			Set<String> links = new HashSet<String>();
			if((visitingUrl.contains(".js") && !visitingUrl.contains(".jsp")) || visitingUrl.contains(".css")){
			}else{
				links = HtmlParserTool.parseHtmlLink(visitingUrl);
			}
			for(String link : links){
				LinkQueue.addUnvisitedUrl(link);
			}
			count++;
		}
	}
	
	/**
	 * 使用种子初始化 URL 队列
	 * @param seeds 种子URL
	 */ 
	private void initCrawlerWithSeeds(String[] seeds){
		for(int i = 0; i < seeds.length; i++){
			LinkQueue.addUnvisitedUrl(seeds[i]);
		}
	}
	

}
