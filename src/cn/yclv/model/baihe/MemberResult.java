package cn.yclv.model.baihe;

import java.util.List;

public class MemberResult {
	private Long groupId;
	private String isGender;
	private Boolean isLogin;
	private List<Member> results;
	private String status;
	private Long viewUserId;

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getIsGender() {
		return isGender;
	}

	public void setIsGender(String isGender) {
		this.isGender = isGender;
	}

	public Boolean getIsLogin() {
		return isLogin;
	}

	public void setIsLogin(Boolean isLogin) {
		this.isLogin = isLogin;
	}

	public List<Member> getResults() {
		return results;
	}

	public void setResults(List<Member> results) {
		this.results = results;
	}

	public Long getViewUserId() {
		return viewUserId;
	}

	public void setViewUserId(Long viewUserId) {
		this.viewUserId = viewUserId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
