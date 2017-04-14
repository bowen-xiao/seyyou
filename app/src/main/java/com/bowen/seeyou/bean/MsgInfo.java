package com.bowen.seeyou.bean;

/**
 * Created by 肖稳华 on 2017/4/12.
 */

public class MsgInfo {
	//消息的内容
	private String msg;
	//消息的类型
	private MsgType msgType;
	//消息的状态，是否发送成功
	private int msgStatus;

	public MsgInfo(String msg, MsgType msgType, int msgStatus) {
		this.msg = msg;
		this.msgType = msgType;
		this.msgStatus = msgStatus;
	}

	@Override
	public String toString() {
		return "MsgInfo{" +
			   "msg='" + msg + '\'' +
			   ", msgType=" + msgType +
			   ", msgStatus=" + msgStatus +
			   '}';
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public MsgType getMsgType() {
		return msgType;
	}

	public void setMsgType(MsgType msgType) {
		this.msgType = msgType;
	}

	public int getMsgStatus() {
		return msgStatus;
	}

	public void setMsgStatus(int msgStatus) {
		this.msgStatus = msgStatus;
	}
}
