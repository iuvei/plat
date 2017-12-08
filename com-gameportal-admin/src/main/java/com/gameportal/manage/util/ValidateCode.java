package com.gameportal.manage.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ValidateCode {

	public static Color getRandColor(int fc, int bc, Random random) {// 给定范围获得随机颜色
		if (fc > 255) {
			fc = 255;
		}
		if (bc > 255) {
			bc = 255;
		}
		int differ = bc - fc;
		int r = fc + random.nextInt(differ);
		int g = fc + random.nextInt(differ);
		int b = fc + random.nextInt(differ);
		return new Color(r, g, b);
	}

	// 提取request里面的字符串参数；
	public static String getString(HttpServletRequest request, String str) {
		str = request.getParameter(str);
		return str == null ? "" : str.trim();
	}

	// 提取request header里面的字符串参数；
	public static String getHeader(HttpServletRequest request, String str) {
		str = request.getHeader(str);
		return str == null ? "" : str.trim();
	}

	// 生成图片验证码
	public static String getImg(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String sRand = "";
			String[] arr = new String[] { "5", "8", "3", "4", "5", "6", "7",
					"8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "D", "J",
					"K", "L", "M", "N", "A", "P", "D", "R", "S", "T", "B", "F",
					"W", "X", "Y", "H" };
			// 在内存中创建图象
			int width = 71, height = 20;
			BufferedImage image = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			// 获取图形上下文
			Graphics g = image.getGraphics();
			// 生成随机类
			Random random = new Random();
			// 设定背景色
			g.setColor(getRandColor(240, 250, random));
			g.fillRect(0, 0, width, height);
			// 设定字体
			g.setFont(new Font("Times New Roman", Font.BOLD, 18));
			// 画边框
			g.setColor(new Color(255, 255, 255));
			g.drawRect(0, 0, width - 1, height - 1);
			// 随机产生55条干扰线，使图象中的认证码不易被其它程序探测到
			g.setColor(getRandColor(190, 200, random));
			for (int i = 0; i < 55; i++) {
				int x = random.nextInt(width);
				int y = random.nextInt(height);
				int xl = random.nextInt(20);
				int yl = random.nextInt(20);
				g.drawLine(x, y, x + xl, y + yl);
			}
			// 取随机产生的认证码(4位数字)
			for (int i = 0; i < 4; i++) {
				int adapt = random.nextInt(1);
				String rand = "";
				if ((i + adapt) % 2 == 0) {
					rand = arr[random.nextInt(9)];
				} else {
					rand = arr[random.nextInt(35)];
				}
				sRand += rand;
				// 将认证码显示到图象中
				g.setColor(new Color(20 + random.nextInt(110), 20 + random
						.nextInt(110), 20 + random.nextInt(110)));
				// 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
				g.drawString(rand, 13 * i + 6, 16);
			}
			g.dispose();
			java.io.OutputStream os = response.getOutputStream();
			ImageIO.write(image, "JPEG", os);
			os.flush();
			os.close();
			// 输出图象到页面sRand
			return sRand;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
