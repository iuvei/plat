package com.gameportal.web.user.model;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 电销人员数据列表
 * @author Administrator
 *
 */
@XStreamAlias("electricpinList")
public class ElectricpinList {

	@XStreamImplicit(itemFieldName = "electricpin")
	private List<Electricpin> electricpin = new ArrayList<Electricpin>();

	public List<Electricpin> getElectricpin() {
		return electricpin;
	}

	public void setElectricpin(List<Electricpin> electricpin) {
		this.electricpin = electricpin;
	}

	@Override
	public String toString() {
		return "ElectricpinList [electricpin=" + electricpin + "]";
	}
	
}
