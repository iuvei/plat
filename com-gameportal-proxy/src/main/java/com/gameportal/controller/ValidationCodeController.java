package com.gameportal.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gameportal.comms.WebConst;
import com.google.code.kaptcha.Producer;


@Controller
@RequestMapping(value="/validation")
public class ValidationCodeController extends BaseAction{

	@Resource(name = "captchaProducer")
    private Producer captchaProducer = null;
	
	@RequestMapping(value = "/pcrimg")
	public void crimg(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		HttpSession session = request.getSession();
		setResponseHeaders(response);
		String capText = captchaProducer.createText();  
        // store the text in the session  
        session.setAttribute(WebConst.SESSION_SECURITY_CODE, capText);  
          
        // create the image with the text  
        BufferedImage bi = captchaProducer.createImage(capText);  
        ServletOutputStream out = response.getOutputStream();  
          
        ImageIO.write(bi, "png", out);  
        try {  
            out.flush();  
        } finally {  
            out.close();  
        }  
	}
	
	protected void setResponseHeaders(HttpServletResponse response) {
		response.setContentType("image/png");
		response.setHeader("Cache-Control", "no-cache, no-store");
		response.setHeader("Pragma", "no-cache");
		long time = System.currentTimeMillis();
		response.setDateHeader("Last-Modified", time);
		response.setDateHeader("Date", time);
		response.setDateHeader("Expires", time);
	}
}
