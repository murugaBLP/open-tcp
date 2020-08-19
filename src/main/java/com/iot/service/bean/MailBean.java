package com.iot.service.bean;

public class MailBean {

	private Integer status;
	private String tagName;
	private Long occurredTime;
	private Long mailSentTime;
	private Long mailInterval;
	private String imeiNo;
	
	
	public MailBean(Integer status, String tagName, Long occurredTime, Long mailSentTime,
			Long mailInterval, String imeiNo) {
		this.status = status;
		this.tagName = tagName;
		this.occurredTime = occurredTime;
		this.mailSentTime = mailSentTime;
		this.mailInterval = mailInterval;
		this.imeiNo = imeiNo;
	}
	
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Long getOccurredTime() {
		return occurredTime;
	}
	public void setOccurredTime(Long occurredTime) {
		this.occurredTime = occurredTime;
	}
	public Long getMailSentTime() {
		return mailSentTime;
	}
	public void setMailSentTime(Long mailSentTime) {
		this.mailSentTime = mailSentTime;
	}
	public Long getMailInterval() {
		return mailInterval;
	}
	public void setMailInterval(Long mailInterval) {
		this.mailInterval = mailInterval;
	}
	public String getImeiNo() {
		return imeiNo;
	}
	public void setImeiNo(String imeiNo) {
		this.imeiNo = imeiNo;
	}
	public String toString() {
		return "status : "+status+", occurredTime : "+occurredTime+", mailSentTime : "+mailSentTime
				+", mailInterval : "+mailInterval +", imeiNo : "+imeiNo;
	}

	
}
