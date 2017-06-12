package com.bowen.seeyou.bean;

import java.io.Serializable;

/**
 * Created by 肖稳华 on 2017/6/12.
 */

public class TicketNumberResult implements Serializable {

	/**
	 * returnCode : 500
	 * returnData : {"prices":"-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1","tickets":"-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1"}
	 * returnInfo : 获取成功
	 */

	private String         returnCode;
	/**
	 * prices : -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1
	 * tickets : -1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1
	 */

	private ReturnDataBean returnData;
	private String returnInfo;

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public ReturnDataBean getReturnData() {
		return returnData;
	}

	public void setReturnData(ReturnDataBean returnData) {
		this.returnData = returnData;
	}

	public String getReturnInfo() {
		return returnInfo;
	}

	public void setReturnInfo(String returnInfo) {
		this.returnInfo = returnInfo;
	}

	public static class ReturnDataBean {

		private String prices;
		private String tickets;

		public String getPrices() {
			return prices;
		}

		public void setPrices(String prices) {
			this.prices = prices;
		}

		public String getTickets() {
			return tickets;
		}

		public void setTickets(String tickets) {
			this.tickets = tickets;
		}

		@Override
		public String toString() {
			return "ReturnDataBean{" +
				   "prices='" + prices + '\'' +
				   ", tickets='" + tickets + '\'' +
				   '}';
		}
	}

	@Override
	public String toString() {
		return "TicketNumberResult{" +
			   "returnCode='" + returnCode + '\'' +
			   ", returnData=" + returnData +
			   ", returnInfo='" + returnInfo + '\'' +
			   '}';
	}
}
