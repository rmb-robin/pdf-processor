package com.mst.pdf.processor;

import java.time.LocalDate;

public class DiscreteData {

	private String readingLocation;
	private String patientMRN;
	private LocalDate patientDob;
	private String sex;
	private String patientAccount;
	private String patientEncounter;
	private String vrReportId;
	private String accessionNumber;
	private String examDescription;
	private String modality;
	private String resultStatus;
	private String reportFinalizedBy;
	private String reportFinalizedById;
	private LocalDate reportFinalizedDate;
	private int patientAge;

	private LocalDate processingDate;

	private String organizationId;

	public String getReadingLocation() {
		return readingLocation;
	}

	public void setReadingLocation(String readingLocation) {
		this.readingLocation = readingLocation;
	}

	public String getPatientMRN() {
		return patientMRN;
	}

	public void setPatientMRN(String patientMRN) {
		this.patientMRN = patientMRN;
	}

	public LocalDate getPatientDob() {
		return patientDob;
	}

	public void setPatientDob(LocalDate patientDob) {
		this.patientDob = patientDob;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPatientAccount() {
		return patientAccount;
	}

	public void setPatientAccount(String patientAccount) {
		this.patientAccount = patientAccount;
	}

	public String getPatientEncounter() {
		return patientEncounter;
	}

	public void setPatientEncounter(String patientEncounter) {
		this.patientEncounter = patientEncounter;
	}

	public String getVrReportId() {
		return vrReportId;
	}

	public void setVrReportId(String vrReportId) {
		this.vrReportId = vrReportId;
	}

	public String getAccessionNumber() {
		return accessionNumber;
	}

	public void setAccessionNumber(String accessionNumber) {
		this.accessionNumber = accessionNumber;
	}

	public String getExamDescription() {
		return examDescription;
	}

	public void setExamDescription(String examDescription) {
		this.examDescription = examDescription;
	}

	public String getModality() {
		return modality;
	}

	public void setModality(String modality) {
		this.modality = modality;
	}

	public String getResultStatus() {
		return resultStatus;
	}

	public void setResultStatus(String resultStatus) {
		this.resultStatus = resultStatus;
	}

	public String getReportFinalizedBy() {
		return reportFinalizedBy;
	}

	public void setReportFinalizedBy(String reportFinalizedBy) {
		this.reportFinalizedBy = reportFinalizedBy;
	}

	public String getReportFinalizedById() {
		return reportFinalizedById;
	}

	public void setReportFinalizedById(String reportFinalizedById) {
		this.reportFinalizedById = reportFinalizedById;
	}

	public LocalDate getReportFinalizedDate() {
		return reportFinalizedDate;
	}

	public void setReportFinalizedDate(LocalDate reportFinalizedDate) {
		this.reportFinalizedDate = reportFinalizedDate;
	}

	public int getPatientAge() {
		return patientAge;
	}

	public void setPatientAge(int patientAge) {
		this.patientAge = patientAge;
	}

	public LocalDate getProcessingDate() {
		return processingDate;
	}

	public void setProcessingDate(LocalDate processingDate) {
		this.processingDate = processingDate;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	@Override
	public String toString() {
		return "DiscreteData [readingLocation=" + readingLocation + ", patientMRN=" + patientMRN + ", patientDob="
				+ patientDob + ", sex=" + sex + ", patientAccount=" + patientAccount + ", patientEncounter="
				+ patientEncounter + ", vrReportId=" + vrReportId + ", accessionNumber=" + accessionNumber
				+ ", examDescription=" + examDescription + ", modality=" + modality + ", resultStatus=" + resultStatus
				+ ", reportFinalizedBy=" + reportFinalizedBy + ", reportFinalizedById=" + reportFinalizedById
				+ ", reportFinalizedDate=" + reportFinalizedDate + ", patientAge=" + patientAge + ", processingDate="
				+ processingDate + ", organizationId=" + organizationId + "]";
	}

}