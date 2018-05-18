package com.mst.pdf.processor;

public class SentenceTextRequest {

	private String text;
	private String source, practice, study;
	private DiscreteData discreteData;
	private boolean convertMeasurements, convertLargest;
	private boolean needResult;
	// private boolean isProcessingtypeSentenceDiscovery;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	/*
	 * public boolean getIsProcessingtypeSentenceDiscovery() { return
	 * isProcessingtypeSentenceDiscovery; } public void
	 * setIsProcessingtypeSentenceDiscovery(boolean
	 * isProcessingtypeSentenceDiscovery) { this.isProcessingtypeSentenceDiscovery =
	 * isProcessingtypeSentenceDiscovery; }
	 */
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getPractice() {
		return practice;
	}

	public void setPractice(String practice) {
		this.practice = practice;
	}

	public String getStudy() {
		return study;
	}

	public void setStudy(String study) {
		this.study = study;
	}

	public DiscreteData getDiscreteData() {
		return discreteData;
	}

	public void setDiscreteData(DiscreteData discreteData) {
		this.discreteData = discreteData;
	}

	public boolean isConvertMeasurements() {
		return convertMeasurements;
	}

	public void setConvertMeasurements(boolean convertMeasurements) {
		this.convertMeasurements = convertMeasurements;
	}

	public boolean isConvertLargest() {
		return convertLargest;
	}

	public void setConvertLargest(boolean convertLargest) {
		this.convertLargest = convertLargest;
	}

	public boolean isNeedResult() {
		return needResult;
	}

	public void setNeedResult(boolean needResult) {
		this.needResult = needResult;
	}

	@Override
	public String toString() {
		return "SentenceTextRequest [text=" + text + ", source=" + source + ", practice=" + practice + ", study="
				+ study + ", discreteData=" + discreteData + ", convertMeasurements=" + convertMeasurements
				+ ", convertLargest=" + convertLargest + ", needResult=" + needResult + "]";
	}

}
