/**
 * 
 */
package com.ibm.km.dto;

/**
 * @author Nehil Parashar
 *
 */
public class KmBillPlanDto 
{
	int documentId;
	String billPlan;
	String topic;
	String header;
	String content;
	String path;
	String fromDate;
	String toDate;
	String circleName;
	
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getCircleName() {
		return circleName;
	}
	public void setCircleName(String circleName) {
		this.circleName = circleName;
	}
	public int getDocumentId() {
		return documentId;
	}
	public void setDocumentId(int documentId) {
		this.documentId = documentId;
	}
	public String getBillPlan() {
		return billPlan;
	}
	public void setBillPlan(String billPlan) {
		this.billPlan = billPlan;
	}
}
