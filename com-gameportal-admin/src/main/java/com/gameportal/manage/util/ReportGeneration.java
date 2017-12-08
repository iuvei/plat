package com.gameportal.manage.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service("reportGeneration")
public class ReportGeneration {
	private Configuration configuration = null;

	public ReportGeneration() {
		super();
		// TODO Auto-generated constructor stub
		configuration = new Configuration();
		configuration.setDefaultEncoding("UTF-8");
	}

	public String setReportGeneration(String templateCatalog,
			String templateName, String generationCatalog,
			String generationName, Map<String, Object> dataMap) {
		configuration.setClassForTemplateLoading(this.getClass(),
				templateCatalog); // FTL文件所存在的位置
		Template t = null;
		try {
			t = configuration.getTemplate(templateName); // 文件名
			t.setEncoding("UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		File outFile = new File(generationCatalog + "/" + generationName
				+ ".xls"); // 生成文件的路径
		Writer out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outFile), "UTF-8"));
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			t.process(dataMap, out);
			out.flush();
			out.close();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return generationName + ".xls";
	}
}
