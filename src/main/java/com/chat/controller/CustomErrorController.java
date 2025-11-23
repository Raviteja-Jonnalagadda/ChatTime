package com.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chat.dto.CommonDTO;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

	@Autowired
	public CommonDTO cd;
	
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute("javax.servlet.error.status_code");
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
            	System.err.println("[CustomErrorController] [handleError] 404 Error Occured Please check the Page you are trying to view [ "+request.getRequestURI()+" ] .");
                return "error/404"; 
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
            	System.err.println("[CustomErrorController] [handleError] 403 Error Occured Please check the Page permitions you are trying to view");
                return "error/403"; 
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            	System.err.println("[CustomErrorController] [handleError] 500 Error Occured Please check the logs that it may be a java error or a JSP error");
                return "error/500"; 
            }
        }
    	System.err.println("[CustomErrorController] [handleError] Unknown Error code ocured please check the logs that it may be a java error or a JSP error");
        return "error/error"; 
    }

    @GetMapping("/UnIdentifiedLogin")
    public String unidelogin() {
    	System.err.println("[CustomErrorController] [unidelogin] UnIdentified Login Occured Check the status and try again");
    	return "error/unIdentifiedLogin";
    }
    
    @GetMapping("/Invalid_Redirection")
    public String InvalidRedir() {
    	System.err.println("[CustomErrorController] [unidelogin] Invalid Redirection Login Occured Check the log and try again");
    	if(!reqcheker()) {
    		return "error/unIdentifiedLogin";
    	}
    	else {
        	return "error/Invalid_Redirection";
    	}    	
    }
    
    public boolean reqcheker() {
		String ckval = cd.getLoginstatus();
		System.out.println("[CustomErrorController] [handleError] check value : " + ckval);
		if(ckval.trim().isBlank()||ckval == null) {
			return false;
		}
		else {
			return true;
		}
    	
    }
}