package cn.yclv.baihe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

import cn.yclv.model.baihe.Member;
import cn.yclv.model.baihe.MemberResult;
import cn.yclv.util.FileUtil;
import cn.yclv.util.HttpUtil;

public class BaiheCrawer {
	
	static String memUrlPre = "http://search.baihe.com/mystruts/userInfo.action?jsoncallback=jQuery183009323774384471162_1498272743197&userIds=";
	
	static String tab = "\t";
	
	public static void main(String[] args) {
//		crawlerSingleMember();
		crawlerBatchMember();
		
	}

	private static void crawlerBatchMember() {
		String type = ":64,";
		StringBuffer memIdsSb = new StringBuffer();
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i <= 1000000; i++) {
			if(i % 100 != 0){
				memIdsSb.append(i + type);
			}else{
				memIdsSb.append(i + ":64");
				String batchMemJsonRes = HttpUtil.buildGet(memUrlPre + memIdsSb.toString());
				batchMemJsonRes = replaceJsonResult(batchMemJsonRes);
				memIdsSb = new StringBuffer();
				MemberResult memberResult = new Gson().fromJson(batchMemJsonRes, MemberResult.class);
				List<Member> mems = memberResult.getResults();
				parseMemberResult(sb, mems);
				if(i % 1000 == 0){
					FileUtil.writeTxt(sb.toString(), "E:\\java\\crawlerData\\baihe\\member\\batch_format" + i + ".txt");
					sb = new StringBuffer();
				}
				System.out.println(batchMemJsonRes);
			}
			
		}
	}

	private static void parseMemberResult(StringBuffer sb, List<Member> mems) {
		for (Member m : mems) {
			sb.append(m.getAge() + tab + m.getArea() + tab
						+ m.getCreditStatus() + tab + m.getEducation() + tab
						+ m.getFamilyDesc() + tab + m.getGroupId() + tab
						+ m.getHight() + tab + m.getIconurl() + tab
						+ m.getIdentityClass() + tab + m.getIdentityName() + tab
						+ m.getIdentityOS() + tab + m.getIdentitySign() + tab
						+ m.getIdentitySpm() + tab + m.getIdentityURL() + tab
						+ m.getIsCreditedById5() + tab + m.getIsGender() + tab
						+ m.getIsMarrage() + tab + m.getIsOnline() + tab
						+ m.getJobtype() + tab + m.getLovetype() + tab
						+ m.getNickname() + tab + m.getPhotoNum() + tab
						+ m.getPic() + tab + m.getTagType() + tab
						+ m.getUid() + tab + m.getUserType() + "\r"
			);
		}
	}

	public static void crawlerSingleMember() {
		String memUrlSuff = ":64";
		StringBuffer sb = new StringBuffer();
		for (int i = 3801; i <= 1000000; i++) {
			String memJsonRes = HttpUtil.buildGet(memUrlPre + i + memUrlSuff);
			memJsonRes = replaceJsonResult(memJsonRes);
			sb.append(memJsonRes + "\r");
			if(i % 100 == 0){
				FileUtil.writeTxt(sb.toString(), "E:\\java\\crawlerData\\baihe\\member\\member" + i + ".txt");
				sb = new StringBuffer();
			}
		}
		System.out.println(sb.toString());
	}

	private static String replaceJsonResult(String memJsonRes) {
		memJsonRes = memJsonRes.replace("jQuery183009323774384471162_1498272743197(", "");
		memJsonRes = memJsonRes.substring(0, memJsonRes.length() - 1);
		return memJsonRes;
	}
}
