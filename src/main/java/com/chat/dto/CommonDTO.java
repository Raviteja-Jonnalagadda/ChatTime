package com.chat.dto;

import java.time.LocalDateTime;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CommonDTO {

	private String loginstatus; 
	private String pagetype;
	private String userid;
	private LocalDateTime logintime;
	
	public String getLoginstatus() {
		return loginstatus;
	}
	public void setLoginstatus(String loginstatus) {
		this.loginstatus = loginstatus;
	}
	public String getPagetype() {
		return pagetype;
	}
	public void setPagetype(String pagetype) {
		this.pagetype = pagetype;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public LocalDateTime getLogintime() {
		return logintime;
	}
	public void setLogintime(LocalDateTime logintime) {
		this.logintime = logintime;
	}
	
}
