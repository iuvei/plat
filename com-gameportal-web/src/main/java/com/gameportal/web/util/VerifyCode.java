package com.gameportal.web.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

public class VerifyCode {

	private static int width = 85;// 验证码宽度
	private static int height = 40;// 验证码高度
	private static int codeCount = 4;// 验证码个数
	private static int lineCount = 1;// 混淆线个数

	private static char[] codeSequence = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	public static String getImageCode(HttpServletResponse response) throws IOException {
		// 定义随机数类
		Random r = new Random();
		// 定义存储验证码的类
		StringBuilder builderCode = new StringBuilder();
		// 定义画布
		BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// 得到画笔
		Graphics g = buffImg.getGraphics();
		// 1.设置颜色,画边框
		g.setColor(Color.black);
		g.drawRect(0, 0, width, height);
		// 2.设置颜色,填充内部
		g.setColor(Color.white);
		g.fillRect(1, 1, width - 2, height - 2);
		// 3.设置干扰线
		g.setColor(Color.gray);
		for (int i = 0; i < lineCount; i++) {
			g.drawLine(r.nextInt(width), r.nextInt(width), r.nextInt(width), r.nextInt(width));
		}
		// 4.设置验证码
		g.setColor(Color.blue);
		// 4.1设置验证码字体
		g.setFont(new Font("宋体", Font.BOLD | Font.ITALIC, 24));
		for (int i = 0; i < codeCount; i++) {
			char c = codeSequence[r.nextInt(codeSequence.length)];
			builderCode.append(c);
			float x = i * 1.0F * width / 4;
			g.drawString(c + "", (int) x, height - 12);
		}
		// 5.输出到屏幕
		ServletOutputStream sos = response.getOutputStream();
		ImageIO.write(buffImg, "png", sos);
		sos.close();
		return builderCode.toString();
	}
}
