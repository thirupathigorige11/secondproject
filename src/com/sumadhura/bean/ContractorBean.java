package com.sumadhura.bean;

import java.io.Serializable;
import java.util.Arrays;

import org.springframework.web.multipart.MultipartFile;

public class ContractorBean implements Cloneable, Serializable {

	private static final long serialVersionUID = -2784966707972663384L;
	private String contractor_id;
	private String contractor_name;
	private String first_name;
	private String last_name;
	private String address;
	private String pan_number;
	private String mobile_number;
	private String landline_number;
	private String alternate_mobile_number;
	private String bank_acc_number;
	private String bank_name;
	private String state_name;
	private String city_name;
	private String branch_name;
	private String account_type;
	private String ifsc_code;
	private String email;
	private String GSTIN;
	private MultipartFile[] file;

	public ContractorBean() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	public MultipartFile[] getFile() {
		return file;
	}

	public void setFiles(MultipartFile[] files) {
		this.file = files;
	}

	public String getState_name() {
		return state_name;
	}

	public void setState_name(String state_name) {
		this.state_name = state_name;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public String getBranch_name() {
		return branch_name;
	}

	public void setBranch_name(String branch_name) {
		this.branch_name = branch_name;
	}

	public String getAccount_type() {
		return account_type;
	}

	public void setAccount_type(String account_type) {
		this.account_type = account_type;
	}

	public String getLandline_number() {
		return landline_number;
	}

	public void setLandline_number(String landline_number) {
		this.landline_number = landline_number;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGSTIN() {
		return GSTIN;
	}

	public void setGSTIN(String gSTIN) {
		GSTIN = gSTIN;
	}

	public String getAlternate_mobile_number() {
		return alternate_mobile_number;
	}

	public void setAlternate_mobile_number(String alternate_mobile_number) {
		this.alternate_mobile_number = alternate_mobile_number;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getContractor_id() {
		return contractor_id;
	}

	public void setContractor_id(String contractor_id) {
		this.contractor_id = contractor_id;
	}

	public String getContractor_name() {
		return contractor_name;
	}

	public void setContractor_name(String contractor_name) {
		this.contractor_name = contractor_name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPan_number() {
		return pan_number;
	}

	public void setPan_number(String pan_number) {
		this.pan_number = pan_number;
	}

	public String getMobile_number() {
		return mobile_number;
	}

	public void setMobile_number(String mobile_number) {
		this.mobile_number = mobile_number;
	}

	public String getBank_acc_number() {
		return bank_acc_number;
	}

	public void setBank_acc_number(String bank_acc_number) {
		this.bank_acc_number = bank_acc_number;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getIfsc_code() {
		return ifsc_code;
	}

	public void setIfsc_code(String ifsc_code) {
		this.ifsc_code = ifsc_code;
	}


}
