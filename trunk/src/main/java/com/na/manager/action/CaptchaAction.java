package com.na.manager.action;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.na.manager.common.annotation.Auth;
import com.na.manager.enums.RedisKeyEnum;

/**
 * Created by sunny on 2017/6/21 0021.
 */
@Controller
@Auth(isPublic = true)
public class CaptchaAction {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private static final Font mFont = new Font("Times New Roman", Font.BOLD, 28);
    private static final String allcode[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/codeimg")
    public void codeimg(String username,HttpServletResponse response){
        try {
            response.setContentType("image/gif");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);

            Random random = new Random();
            StringBuffer filePath = new StringBuffer("/static/img/captcha");
            int startIndex = 0;
            startIndex= 40;
            filePath.append("_pc").append(random.nextInt(5)).append(".jpg");

            BufferedImage image = ImageIO.read(this.getClass().getResourceAsStream(filePath.toString()));
            Graphics gra = image.getGraphics();
            gra.setColor(Color.black); // 设置字体色
            gra.setFont(mFont);

            // 取随机产生的认证码(4位)
            String sRand = "";
            for (int i = 0; i < 4; i++) {
                String rand = allcode[random.nextInt(allcode.length)];
                sRand += rand;
                // 将认证码显示到图象中
                gra.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));// 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
                gra.drawString(rand, 16 * i + startIndex+ random.nextInt(5), 20 + random.nextInt(10));
            }
            redisTemplate.boundValueOps(RedisKeyEnum.CAPTCHA_ATTR.get(username)).set(sRand,RedisKeyEnum.CAPTCHA_ATTR.getTtl(), TimeUnit.SECONDS);
            try {
                ImageIO.write(image, "JPEG", response.getOutputStream());//将内存中的图片通过流动形式输出到客户端
            } catch (Exception e) {
                log.error("输出验证码失败!", e);
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
    }

    private Color getRandColor(int fc, int bc) {// 给定范围获得随机颜色
        Random random = new Random();
        if (fc > 255)fc = 255;
        if (bc > 255)bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
}
