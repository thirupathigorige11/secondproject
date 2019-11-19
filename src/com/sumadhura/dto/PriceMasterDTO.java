package com.sumadhura.dto;

import java.util.List;

public class PriceMasterDTO {
	private String price_id;
	private String child_product_id;
	private String child_product_name;
	private String measurement_name;
	private String amount_per_unit_before_taxes;
	private String amount_per_unit_after_taxes;
	private String available_quantity;
	private String basic_amount;
	private String total_amount;
	private String amount_after_tax;
	private String created_date;
	private String site_id;
	private String site_name;
	private String month_name;
	private String month_number;
	private String invoice_number;
	private String dc_number;
	private String vendor_id;
	private String vendor_name;
	private String indent_entry_id;
	private String dc_entry_id;
	private String indent_recive_date;
	private String dc_recive_date;
	private String indentType;
	private List<PriceMasterDTO> subProductsList;
	private List<PriceMasterDTO> childProductsList;

	@Override
	public int hashCode() {
	try {
		if (basic_amount == null && total_amount == null) {
			return child_product_id.hashCode()+child_product_name.hashCode()+site_id.hashCode();
		} else
			return Integer.valueOf((int) Math.round(Double.valueOf(amount_per_unit_after_taxes)))
					+ Integer.valueOf((int) Math.round(Double.valueOf(amount_per_unit_before_taxes)));
		
	} catch (Exception e) {
		System.out.println("Got Exception : "+child_product_id+" "+child_product_name+" "+amount_per_unit_after_taxes+" "+amount_per_unit_before_taxes+" "+price_id);
	//	e.printStackTrace();
	}
	return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof PriceMasterDTO))
			return false;
		if (obj == this)
			return true;

		PriceMasterDTO chldProd = (PriceMasterDTO) obj;
		if (basic_amount == null && total_amount == null) {
			return this.child_product_name.equals(chldProd.child_product_name);
		} else {

		//boolean baseAMT = this.basic_amount.equals(chldProd.basic_amount);
		boolean createdDate = this.created_date.equals(chldProd.created_date);
		//	boolean totalAmt = this.total_amount.equals(chldProd.total_amount);
		boolean amtAfterTax = this.amount_per_unit_after_taxes.equals(chldProd.amount_per_unit_after_taxes);
		boolean amtBeforeTax =	this.amount_per_unit_before_taxes.equals(chldProd.amount_per_unit_before_taxes);
		boolean isSiteIdSame =	this.site_id.equals(chldProd.site_id);
		boolean vendor_id=this.vendor_id.equals(chldProd.vendor_id);
		return amtAfterTax && createdDate && amtBeforeTax&&isSiteIdSame&&vendor_id;
		}
	}

	public PriceMasterDTO() {

	}

	public PriceMasterDTO(String price_id, String child_product_id, String name, String measurement_name,
			String amount_per_unit_before_taxes, String amount_per_unit_after_taxes, String available_quantity,
			String basic_amount, String total_amount, String amount_after_tax, String createddate, String site_id,
			String monthName,String siteName,String month_number,String invoice_number,	 String dc_number,	 String vendor_id,	 String vendor_name,
			String indent_entry_id,String dc_entry_id,String indent_recive_date, String dc_recive_date) {
		super();
		this.price_id = price_id;
		this.child_product_id = child_product_id;
		this.child_product_name = name;
		this.measurement_name = measurement_name;
		this.amount_per_unit_before_taxes = amount_per_unit_before_taxes;
		this.amount_per_unit_after_taxes = amount_per_unit_after_taxes;
		this.available_quantity = available_quantity;
		this.basic_amount = basic_amount;
		this.total_amount = total_amount;
		this.amount_after_tax = amount_after_tax;
		this.created_date = createddate;
		this.site_id = site_id;
		this.month_name = monthName;
		this.site_name=siteName;
		this.month_number=month_number;
		this.invoice_number=invoice_number;
		this.dc_number=dc_number;
		this.vendor_id=vendor_id;
		this.vendor_name=vendor_name;
		this.indent_entry_id=indent_entry_id;
		this.dc_entry_id=dc_entry_id;
		this.indent_recive_date=indent_recive_date;
	    this.dc_recive_date= dc_recive_date;
	}
	 
	public PriceMasterDTO(String price_id, String child_product_id, String name, String measurement_name,
			String amount_per_unit_before_taxes, String amount_per_unit_after_taxes, String site_id,
			String siteName,String invoice_number,	 String dc_number,	 String vendor_id,	 String vendor_name,
			String indent_entry_id,String dc_entry_id,String indent_recive_date, String dc_recive_date) {
		super();
		this.price_id = price_id;
		this.child_product_id = child_product_id;
		this.child_product_name = name;
		this.measurement_name = measurement_name;
		this.amount_per_unit_before_taxes = amount_per_unit_before_taxes;
		this.amount_per_unit_after_taxes = amount_per_unit_after_taxes;
		this.site_id = site_id;
		this.site_name=siteName;
		this.invoice_number=invoice_number;
		this.dc_number=dc_number;
		this.vendor_id=vendor_id;
		this.vendor_name=vendor_name;
		this.indent_entry_id=indent_entry_id;
		this.dc_entry_id=dc_entry_id;
		this.indent_recive_date=indent_recive_date;
	    this.dc_recive_date= dc_recive_date;
	}
	
	public String getIndentType() {
		return indentType;
	}

	public void setIndentType(String indentType) {
		this.indentType = indentType;
	}

	public String getInvoice_number() {
		return invoice_number;
	}

	public void setInvoice_number(String invoice_number) {
		this.invoice_number = invoice_number;
	}

	public String getDc_number() {
		return dc_number;
	}

	public void setDc_number(String dc_number) {
		this.dc_number = dc_number;
	}

	public String getVendor_id() {
		return vendor_id;
	}

	public void setVendor_id(String vendor_id) {
		this.vendor_id = vendor_id;
	}

	public String getVendor_name() {
		return vendor_name;
	}

	public void setVendor_name(String vendor_name) {
		this.vendor_name = vendor_name;
	}
	
	public String getIndent_entry_id() {
		return indent_entry_id;
	}

	public void setIndent_entry_id(String indent_entry_id) {
		this.indent_entry_id = indent_entry_id;
	}

	public String getDc_entry_id() {
		return dc_entry_id;
	}

	public void setDc_entry_id(String dc_entry_id) {
		this.dc_entry_id = dc_entry_id;
	}

	
	public String getIndent_recive_date() {
		return indent_recive_date;
	}

	public void setIndent_recive_date(String indent_recive_date) {
		this.indent_recive_date = indent_recive_date;
	}

	public String getDc_recive_date() {
		return dc_recive_date;
	}

	public void setDc_recive_date(String dc_recive_date) {
		this.dc_recive_date = dc_recive_date;
	}

	public String getSite_name() {
		return site_name;
	}

	public void setSite_name(String site_name) {
		this.site_name = site_name;
	}

	public void setSubProductsList(List<PriceMasterDTO> subProductsList) {
		this.subProductsList = subProductsList;
	}

	public List<PriceMasterDTO> getSubProductsList() {
		return subProductsList;
	}

	public void setChildProductsList(List<PriceMasterDTO> childProductsList) {
		this.childProductsList = childProductsList;
	}

	public List<PriceMasterDTO> getChildProductsList() {
		return childProductsList;
	}

	public String getPrice_id() {
		return price_id;
	}

	public void setPrice_id(String price_id) {
		this.price_id = price_id;
	}

	public String getChild_product_id() {
		return child_product_id;
	}

	public void setChild_product_id(String child_product_id) {
		this.child_product_id = child_product_id;
	}

	public String getChild_product_name() {
		return child_product_name;
	}

	public void setChild_product_name(String child_product_name) {
		this.child_product_name = child_product_name;
	}

	public String getMeasurement_name() {
		return measurement_name;
	}

	public void setMeasurement_name(String measurement_name) {
		this.measurement_name = measurement_name;
	}

	public String getAmount_per_unit_before_taxes() {
		return amount_per_unit_before_taxes;
	}

	public void setAmount_per_unit_before_taxes(String amount_per_unit_before_taxes) {
		this.amount_per_unit_before_taxes = amount_per_unit_before_taxes;
	}

	public String getAmount_per_unit_after_taxes() {
		return amount_per_unit_after_taxes;
	}

	public void setAmount_per_unit_after_taxes(String amount_per_unit_after_taxes) {
		this.amount_per_unit_after_taxes = amount_per_unit_after_taxes;
	}

	public String getAvailable_quantity() {
		return available_quantity;
	}

	public void setAvailable_quantity(String available_quantity) {
		this.available_quantity = available_quantity;
	}

	public String getBasic_amount() {
		return basic_amount;
	}

	public void setBasic_amount(String basic_amount) {
		this.basic_amount = basic_amount;
	}

	public String getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(String total_amount) {
		this.total_amount = total_amount;
	}

	public String getAmount_after_tax() {
		return amount_after_tax;
	}

	public void setAmount_after_tax(String amount_after_tax) {
		this.amount_after_tax = amount_after_tax;
	}

	public String getCreated_date() {
		return created_date;
	}

	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}

	public String getSite_id() {
		return site_id;
	}

	public void setSite_id(String site_id) {
		this.site_id = site_id;
	}

	
	public String getMonth_number() {
		return month_number;
	}

	public void setMonth_number(String month_number) {
		this.month_number = month_number;
	}

	public String getMonth_name() {
		return month_name;
	}

	public void setMonth_name(String month_name) {
		this.month_name = month_name;
	}

	@Override
	public String toString() {
		return "PriceMasterDTO [price_id=" + price_id + ", child_product_id=" + child_product_id
				+ ", child_product_name=" + child_product_name + ", measurement_name=" + measurement_name
				+ ", amount_per_unit_before_taxes=" + amount_per_unit_before_taxes + ", amount_per_unit_after_taxes="
				+ amount_per_unit_after_taxes + ", available_quantity=" + available_quantity + ", basic_amount="
				+ basic_amount + ", total_amount=" + total_amount + ", amount_after_tax=" + amount_after_tax
				+ ", created_date=" + created_date + ", site_id=" + site_id + ", month_name=" + month_name + "]";
	}

}
