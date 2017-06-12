package com.bowen.seeyou.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 肖稳华 on 2017/6/12.
 */

public class SearchResult implements Serializable {

	/**
	 * returnCode : 500
	 * returnData : [{"id":470103,"isEnd":1,"isFirst":1,"isLabel":1,"lineId":41650,"lineNo":"P414-1","mileage":31.22,"needTime":50,"offGeogId":5,"offStationId":512033,"offStationName":"大冲①","onGeogId":7,"onStationId":2146,"onStationName":"水田社区","openType":1,"perNum":0,"price":5,"startTime":"0725","status":5,"tradePrice":5,"vehTime":"0725"},{"id":332922,"isEnd":1,"isFirst":1,"isLabel":1,"lineId":41651,"lineNo":"P414-2","mileage":33.35,"needTime":50,"offGeogId":7,"offStationId":2299,"offStationName":"水田社区","onGeogId":5,"onStationId":7267,"onStationName":"深大北门①","openType":1,"perNum":0,"price":5,"startTime":"1755","status":5,"tradePrice":5,"vehTime":"1755"}]
	 * returnInfo : 获取成功
	 * returnSize : 2
	 */

	private String returnCode;
	private String returnInfo;
	private int    returnSize;
	/**
	 * id : 470103
	 * isEnd : 1
	 * isFirst : 1
	 * isLabel : 1
	 * lineId : 41650
	 * lineNo : P414-1
	 * mileage : 31.22
	 * needTime : 50
	 * offGeogId : 5
	 * offStationId : 512033
	 * offStationName : 大冲①
	 * onGeogId : 7
	 * onStationId : 2146
	 * onStationName : 水田社区
	 * openType : 1
	 * perNum : 0
	 * price : 5
	 * startTime : 0725
	 * status : 5
	 * tradePrice : 5
	 * vehTime : 0725
	 */

	private List<ReturnDataBean> returnData;

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnInfo() {
		return returnInfo;
	}

	public void setReturnInfo(String returnInfo) {
		this.returnInfo = returnInfo;
	}

	public int getReturnSize() {
		return returnSize;
	}

	public void setReturnSize(int returnSize) {
		this.returnSize = returnSize;
	}

	public List<ReturnDataBean> getReturnData() {
		return returnData;
	}

	public void setReturnData(List<ReturnDataBean> returnData) {
		this.returnData = returnData;
	}

	public static class ReturnDataBean {

		private int    id;
		private int    isEnd;
		private int    isFirst;
		private int    isLabel;
		private int    lineId;
		private String lineNo;
		private double mileage;
		private int    needTime;
		private int    offGeogId;
		private int    offStationId;
		private String offStationName;
		private int    onGeogId;
		private int    onStationId;
		private String onStationName;
		private int    openType;
		private int    perNum;
		private int    price;
		private String startTime;
		private int    status;
		private int    tradePrice;
		private String vehTime;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getIsEnd() {
			return isEnd;
		}

		public void setIsEnd(int isEnd) {
			this.isEnd = isEnd;
		}

		public int getIsFirst() {
			return isFirst;
		}

		public void setIsFirst(int isFirst) {
			this.isFirst = isFirst;
		}

		public int getIsLabel() {
			return isLabel;
		}

		public void setIsLabel(int isLabel) {
			this.isLabel = isLabel;
		}

		public int getLineId() {
			return lineId;
		}

		public void setLineId(int lineId) {
			this.lineId = lineId;
		}

		public String getLineNo() {
			return lineNo;
		}

		public void setLineNo(String lineNo) {
			this.lineNo = lineNo;
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

		public int getOffGeogId() {
			return offGeogId;
		}

		public void setOffGeogId(int offGeogId) {
			this.offGeogId = offGeogId;
		}

		public int getOffStationId() {
			return offStationId;
		}

		public void setOffStationId(int offStationId) {
			this.offStationId = offStationId;
		}

		public String getOffStationName() {
			return offStationName;
		}

		public void setOffStationName(String offStationName) {
			this.offStationName = offStationName;
		}

		public int getOnGeogId() {
			return onGeogId;
		}

		public void setOnGeogId(int onGeogId) {
			this.onGeogId = onGeogId;
		}

		public int getOnStationId() {
			return onStationId;
		}

		public void setOnStationId(int onStationId) {
			this.onStationId = onStationId;
		}

		public String getOnStationName() {
			return onStationName;
		}

		public void setOnStationName(String onStationName) {
			this.onStationName = onStationName;
		}

		public int getOpenType() {
			return openType;
		}

		public void setOpenType(int openType) {
			this.openType = openType;
		}

		public int getPerNum() {
			return perNum;
		}

		public void setPerNum(int perNum) {
			this.perNum = perNum;
		}

		public int getPrice() {
			return price;
		}

		public void setPrice(int price) {
			this.price = price;
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

		public int getTradePrice() {
			return tradePrice;
		}

		public void setTradePrice(int tradePrice) {
			this.tradePrice = tradePrice;
		}

		public String getVehTime() {
			return vehTime;
		}

		public void setVehTime(String vehTime) {
			this.vehTime = vehTime;
		}

		@Override
		public String toString() {
			return "ReturnDataBean{" +
				   "id=" + id +
				   ", isEnd=" + isEnd +
				   ", isFirst=" + isFirst +
				   ", isLabel=" + isLabel +
				   ", lineId=" + lineId +
				   ", lineNo='" + lineNo + '\'' +
				   ", mileage=" + mileage +
				   ", needTime=" + needTime +
				   ", offGeogId=" + offGeogId +
				   ", offStationId=" + offStationId +
				   ", offStationName='" + offStationName + '\'' +
				   ", onGeogId=" + onGeogId +
				   ", onStationId=" + onStationId +
				   ", onStationName='" + onStationName + '\'' +
				   ", openType=" + openType +
				   ", perNum=" + perNum +
				   ", price=" + price +
				   ", startTime='" + startTime + '\'' +
				   ", status=" + status +
				   ", tradePrice=" + tradePrice +
				   ", vehTime='" + vehTime + '\'' +
				   '}';
		}
	}
}
