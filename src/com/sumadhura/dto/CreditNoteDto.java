package com.sumadhura.dto;

public class CreditNoteDto {
	private int creditSeqNo ;
	private String creditNoteNumber;
	private String indentEntryId;
	private String invoiceNumber;
	private String dcNumber;
	private String creditTotalAmount;
	private int creditNoteDtlsSeqId ;
	private String dcEntryId;
	private String creditFor;
	
	
	
	
	public String getCreditFor() {
		return creditFor;
	}
	public void setCreditFor(String creditFor) {
		this.creditFor = creditFor;
	}
	public String getDcEntryId() {
		return dcEntryId;
	}
	public void setDcEntryId(String dcEntryId) {
		this.dcEntryId = dcEntryId;
	}
	public String getDcNumber() {
		return dcNumber;
	}
	public void setDcNumber(String dcNumber) {
		this.dcNumber = dcNumber;
	}
	public int getCreditNoteDtlsSeqId() {
		return creditNoteDtlsSeqId;
	}
	public void setCreditNoteDtlsSeqId(int creditNoteDtlsSeqId) {
		this.creditNoteDtlsSeqId = creditNoteDtlsSeqId;
	}
	public String getIndentEntryId() {
		return indentEntryId;
	}
	public void setIndentEntryId(String indentEntryId) {
		this.indentEntryId = indentEntryId;
	}
	public int getCreditSeqNo() {
		return creditSeqNo;
	}
	public void setCreditSeqNo(int creditSeqNo) {
		this.creditSeqNo = creditSeqNo;
	}
	public String getCreditNoteNumber() {
		return creditNoteNumber;
	}
	public void setCreditNoteNumber(String creditNoteNumber) {
		this.creditNoteNumber = creditNoteNumber;
	}
	
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public String getCreditTotalAmount() {
		return creditTotalAmount;
	}
	public void setCreditTotalAmount(String creditTotalAmount) {
		this.creditTotalAmount = creditTotalAmount;
	}
}
