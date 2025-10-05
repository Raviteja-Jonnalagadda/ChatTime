package com.chat.error;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@PropertySource("classpath:error.properties")
@ConfigurationProperties(prefix = "ctap.error")
public class ErrorBean {
    private String E001;
    private String E002;
    private String E003;
    private String E004;
    private String E005;
    private String E006;
    
	public String getE001() {
		return E001;
	}
	public void setE001(String e001) {
		E001 = e001;
	} 
	public String getE002() {
		return E002;
	}
	public void setE002(String e002) {
		E002 = e002;
	}
	public String getE003() {
		return E003;
	}
	public void setE003(String e003) {
		System.out.println(e003);
		E003 = e003;
	}
	public String getE004() {
		return E004;
	}
	public void setE004(String e004) {
		E004 = e004;
	}
	public String getE005() {
		return E005;
	}
	public void setE005(String e005) {
		E005 = e005;
	}
	public String getE006() {
		return E006;
	}
	public void setE006(String e006) {
		E006 = e006;
	}
}
