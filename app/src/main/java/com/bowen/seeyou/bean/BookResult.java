package com.bowen.seeyou.bean;

import java.io.Serializable;

/**
 * Created by 肖稳华 on 2017/6/10.
 */

public class BookResult implements Serializable {

	/**
	 * returnCode : 500
	 * returnData : {"main":{"alipayCost":0,"carTeamId":60,"companyId":1,"dayNum":5,"id":3835797,"lineCode":"326","lineId":41651,"lineName":"深大北门（1755-13）-水田社区","lineNo":"P414-2","mainNo":"2017060003835797","mileage":33.35,"needTime":20,"offStationId":7577,"onStationId":7383,"opType":2,"orderTime":"2017-06-10 14:24","originalPrice":25,"payNo":"362317893","payType":3,"refundNum":0,"startTime":"1802","status":0,"timeKey":20170610,"tradePrice":25,"userId":179792,"userName":"17051052812","vehTime":"1755"}}
	 * returnInfo : 操作成功
	 */

	private String         returnCode;
	/**
	 * main : {"alipayCost":0,"carTeamId":60,"companyId":1,"dayNum":5,"id":3835797,"lineCode":"326","lineId":41651,"lineName":"深大北门（1755-13）-水田社区","lineNo":"P414-2","mainNo":"2017060003835797","mileage":33.35,"needTime":20,"offStationId":7577,"onStationId":7383,"opType":2,"orderTime":"2017-06-10 14:24","originalPrice":25,"payNo":"362317893","payType":3,"refundNum":0,"startTime":"1802","status":0,"timeKey":20170610,"tradePrice":25,"userId":179792,"userName":"17051052812","vehTime":"1755"}
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

		/**
		 * alipayCost : 0
		 * carTeamId : 60
		 * companyId : 1
		 * dayNum : 5
		 * id : 3835797
		 * lineCode : 326
		 * lineId : 41651
		 * lineName : 深大北门（1755-13）-水田社区
		 * lineNo : P414-2
		 * mainNo : 2017060003835797
		 * mileage : 33.35
		 * needTime : 20
		 * offStationId : 7577
		 * onStationId : 7383
		 * opType : 2
		 * orderTime : 2017-06-10 14:24
		 * originalPrice : 25
		 * payNo : 362317893
		 * payType : 3
		 * refundNum : 0
		 * startTime : 1802
		 * status : 0
		 * timeKey : 20170610
		 * tradePrice : 25
		 * userId : 179792
		 * userName : 17051052812
		 * vehTime : 1755
		 */

		private MainBean main;

		public MainBean getMain() {
			return main;
		}

		public void setMain(MainBean main) {
			this.main = main;
		}

		public static class MainBean {

			private int    alipayCost;
			private int    carTeamId;
			private int    companyId;
			private int    dayNum;
			private int    id;
			private String lineCode;
			private int    lineId;
			private String lineName;
			private String lineNo;
			private String mainNo;
			private double mileage;
			private int    needTime;
			private int    offStationId;
			private int    onStationId;
			private int    opType;
			private String orderTime;
			private int    originalPrice;
			private String payNo;
			private int    payType;
			private int    refundNum;
			private String startTime;
			private int    status;
			private int    timeKey;
			private int    tradePrice;
			private int    userId;
			private String userName;
			private String vehTime;

			public int getAlipayCost() {
				return alipayCost;
			}

			public void setAlipayCost(int alipayCost) {
				this.alipayCost = alipayCost;
			}

			public int getCarTeamId() {
				return carTeamId;
			}

			public void setCarTeamId(int carTeamId) {
				this.carTeamId = carTeamId;
			}

			public int getCompanyId() {
				return companyId;
			}

			public void setCompanyId(int companyId) {
				this.companyId = companyId;
			}

			public int getDayNum() {
				return dayNum;
			}

			public void setDayNum(int dayNum) {
				this.dayNum = dayNum;
			}

			public int getId() {
				return id;
			}

			public void setId(int id) {
				this.id = id;
			}

			public String getLineCode() {
				return lineCode;
			}

			public void setLineCode(String lineCode) {
				this.lineCode = lineCode;
			}

			public int getLineId() {
				return lineId;
			}

			public void setLineId(int lineId) {
				this.lineId = lineId;
			}

			public String getLineName() {
				return lineName;
			}

			public void setLineName(String lineName) {
				this.lineName = lineName;
			}

			public String getLineNo() {
				return lineNo;
			}

			public void setLineNo(String lineNo) {
				this.lineNo = lineNo;
			}

			public String getMainNo() {
				return mainNo;
			}

			public void setMainNo(String mainNo) {
				this.mainNo = mainNo;
			}

			public double getMileage() {
				return mileage;
			}

			public void setMileage(double mileage) {
				this.mileage = mileage;
			}

			public int getNeedTime() {
				return needTime;
			}

			public void setNeedTime(int needTime) {
				this.needTime = needTime;
			}

			public int getOffStationId() {
				return offStationId;
			}

			public void setOffStationId(int offStationId) {
				this.offStationId = offStationId;
			}

			public int getOnStationId() {
				return onStationId;
			}

			public void setOnStationId(int onStationId) {
				this.onStationId = onStationId;
			}

			public int getOpType() {
				return opType;
			}

			public void setOpType(int opType) {
				this.opType = opType;
			}

			public String getOrderTime() {
				return orderTime;
			}

			public void setOrderTime(String orderTime) {
				this.orderTime = orderTime;
			}

			public int getOriginalPrice() {
				return originalPrice;
			}

			public void setOriginalPrice(int originalPrice) {
				this.originalPrice = originalPrice;
			}

			public String getPayNo() {
				return payNo;
			}

			public void setPayNo(String payNo) {
				this.payNo = payNo;
			}

			public int getPayType() {
				return payType;
			}

			public void setPayType(int payType) {
				this.payType = payType;
			}

			public int getRefundNum() {
				return refundNum;
			}

			public void setRefundNum(int refundNum) {
				this.refundNum = refundNum;
			}

			public String getStartTime() {
				return startTime;
			}

			public void setStartTime(String startTime) {
				this.startTime = startTime;
			}

			public int getStatus() {
				return status;
			}

			public void setStatus(int status) {
				this.status = status;
			}

			public int getTimeKey() {
				return timeKey;
			}

			public void setTimeKey(int timeKey) {
				this.timeKey = timeKey;
			}

			public int getTradePrice() {
				return tradePrice;
			}

			public void setTradePrice(int tradePrice) {
				this.tradePrice = tradePrice;
			}

			public int getUserId() {
				return userId;
			}

			public void setUserId(int userId) {
				this.userId = userId;
			}

			public String getUserName() {
				return userName;
			}

			public void setUserName(String userName) {
				this.userName = userName;
			}

			public String getVehTime() {
				return vehTime;
			}

			public void setVehTime(String vehTime) {
				this.vehTime = vehTime;
			}
		}
	}
}
