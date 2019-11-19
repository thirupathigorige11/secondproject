package com.sumadhura.transdao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import jxl.format.Format;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sumadhura.bean.GetInvoiceDetailsBean;
import com.sumadhura.bean.TransportChargesBean;
import com.sumadhura.bean.ViewIndentIssueDetailsBean;
import com.sumadhura.dto.IndentReceiveDto;
import com.sumadhura.dto.TransportChargesDto;
import com.sumadhura.service.EmailFunction;
import com.sumadhura.util.DBConnection;
import com.sumadhura.util.DateUtil;
import com.sumadhura.util.NumberToWord;
import com.sumadhura.util.UIProperties;
import com.sun.istack.internal.logging.Logger;

import java.util.Currency;
import java.util.Locale;



@Repository("InwardGetInvoiceDetailsDaoClass")


public class InwardGetInvoiceDetailsDaoImpl extends UIProperties implements InwardGetInvoiceDetailsDao{
	static Logger log = Logger.getLogger(InwardGetInvoiceDetailsDaoImpl.class);

	@Autowired(required = true)
	private JdbcTemplate jdbcTemplate;

	private Object totalamount;

	@Autowired
	IndentReceiveDao ird;
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	@Qualifier("purchaseDeptIndentrocessDao")
	PurchaseDepartmentIndentProcessDao objPurchaseDepartmentIndentProcessDao;

	public String getVendorDetails(String invoiceNumber, String siteId,String strVendorId,String invoiceDateParam, String selectIndentType) {
		List<Map<String, Object>> productList = null;
		List<Map<String, Object>> transportorList = null;

		List<GetInvoiceDetailsBean> GetGrnDetailsInward = new ArrayList<GetInvoiceDetailsBean>();
		GetInvoiceDetailsBean objGetInvoiceDetailsInward=null;
		String tblOneData="";
		String state="";
		String invoiceid="";
		String gstinNumber="";
		String grn_no="";
		String invoiceDate="";
		String vendorName="";
		String poNo="";
		String vendorAddress="";
		String poDate="";
		String eWayBillNo="";
		String vehileNo="";
		String transporterName="";
		String receviedDate="";
		double totalamount=0.0;
		String note="";
		String dcNo="";
		String doubleSumOfOtherCharges="";
		String indententryid="";
		String strEmpName="";
		String steReceiveDate="";
		boolean isThisInvoiceConvertedFromDC = false;
		JdbcTemplate template = null;
		String sql = "";
		String query="";
		try {

			    template = new JdbcTemplate(DBConnection.getDbConnection());
				if(selectIndentType==null){
					selectIndentType="IN";
				}else if(selectIndentType.length()==0){
					selectIndentType="IN";
				}
			if (StringUtils.isNotBlank(invoiceNumber) ) {
				//sql = "SELECT distinct IE.STATE,IE.TOTAL_AMOUNT,IE.NOTE, IE.RECEVED_DATE,IED.TRANSPORTERNAME, IED.VEHICLENO,IED.EWAYBILLNO, IED.PO_NUMBER,IED.PODATE,VD.VENDOR_NAME,VD.ADDRESS,VD.GSIN_NUMBER,IE.INVOICE_ID, IE.INVOICE_DATE FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, VENDOR_DETAILS VD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND IE.VENDOR_ID=VD.VENDOR_ID AND IE.INVOICE_ID=? AND IE.SITE_ID=? AND IE.INDENT_TYPE='IN'";
				sql = "SELECT distinct IE.STATE,IE.TOTAL_AMOUNT,IE.NOTE, IE.INDENT_ENTRY_ID,IE.RECEIVED_OR_ISSUED_DATE,IE.GRN_NO,CONCAT(TD.FIRST_NAME,' '||TD.LAST_NAME)  as TRANSPORTERNAME, IE.VEHICLENO,IE.EWAYBILLNO, IE.PO_ID,IE.PODATE,VD.VENDOR_NAME,VD.ADDRESS,VD.GSIN_NUMBER,IE.INVOICE_ID, IE.INVOICE_DATE,SED.EMP_NAME,t3.DC_ENTRY_ID   "
						+ "FROM INDENT_ENTRY IE " 
						+ "LEFT OUTER JOIN TRANSPORTOR_DETAILS TD ON TD.TRANSPORTOR_ID=IE.TRANSPORTERNAME "
						+ "LEFT OUTER JOIN ("
						+ "SELECT t2.*  "
						+ "FROM (SELECT t1.*, (ROW_NUMBER() OVER(PARTITION BY INDENT_ENTRY_ID ORDER BY DC_ENTRY_ID asc)) As Rank " 
						+ "FROM DC_ENTRY t1) t2 "
						+ "WHERE t2.Rank=1"
						+ ") t3 "
						+ "ON IE.INDENT_ENTRY_ID = t3.INDENT_ENTRY_ID,  "
						+ "INDENT_ENTRY_DETAILS IED, VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND IE.VENDOR_ID=VD.VENDOR_ID AND SED.EMP_ID = IE.USER_ID AND IE.INDENT_ENTRY_ID=? AND IE.SITE_ID=? AND VD.VENDOR_ID = ? AND IE.INDENT_TYPE='"+selectIndentType+"' "
						+ "AND trunc(IE.INVOICE_DATE,'yy') = trunc(TO_DATE( ? ,'dd-MM-yy'),'yy')";
				productList = template.queryForList(sql, new Object[] {invoiceNumber, siteId,strVendorId,invoiceDateParam});
				System.out.println("in dao class data"+productList);
			} 
			if (null != productList && productList.size() > 0) {

				//int i = 1;
				int sno=0;
				for (Map<?, ?> GetInvoiceDetailsInwardBean : productList) {
					sno++;
					if(sno==1){
					objGetInvoiceDetailsInward = new GetInvoiceDetailsBean();
					state=GetInvoiceDetailsInwardBean.get("STATE") == null ? "" : GetInvoiceDetailsInwardBean.get("STATE").toString();
					invoiceid=GetInvoiceDetailsInwardBean.get("INVOICE_ID") == null ? "" : GetInvoiceDetailsInwardBean.get("INVOICE_ID").toString();

					invoiceDate=GetInvoiceDetailsInwardBean.get("INVOICE_DATE") == null ? "": GetInvoiceDetailsInwardBean.get("INVOICE_DATE").toString();
					vendorName =GetInvoiceDetailsInwardBean.get("VENDOR_NAME") == null ? "" : GetInvoiceDetailsInwardBean.get("VENDOR_NAME").toString();
					gstinNumber=GetInvoiceDetailsInwardBean.get("GSIN_NUMBER") == null ? "" : GetInvoiceDetailsInwardBean.get("GSIN_NUMBER").toString();
					grn_no=GetInvoiceDetailsInwardBean.get("GRN_NO") == null ? "-" : GetInvoiceDetailsInwardBean.get("GRN_NO").toString();
					
					poNo=GetInvoiceDetailsInwardBean.get("PO_ID") == null ? "" : GetInvoiceDetailsInwardBean.get("PO_ID").toString();
					vendorAddress=GetInvoiceDetailsInwardBean.get("ADDRESS") == null ? "" : GetInvoiceDetailsInwardBean.get("ADDRESS").toString();
					poDate=GetInvoiceDetailsInwardBean.get("PODATE") == null ? "" : GetInvoiceDetailsInwardBean.get("PODATE").toString();
					eWayBillNo=GetInvoiceDetailsInwardBean.get("EWAYBILLNO") == null ? "" : GetInvoiceDetailsInwardBean.get("EWAYBILLNO").toString();
					vehileNo=GetInvoiceDetailsInwardBean.get("VEHICLENO") == null ? "" : GetInvoiceDetailsInwardBean.get("VEHICLENO").toString();
					transporterName=GetInvoiceDetailsInwardBean.get("TRANSPORTERNAME") == null ? "" : GetInvoiceDetailsInwardBean.get("TRANSPORTERNAME").toString();
					receviedDate=GetInvoiceDetailsInwardBean.get("RECEIVED_OR_ISSUED_DATE") == null ? "" : GetInvoiceDetailsInwardBean.get("RECEIVED_OR_ISSUED_DATE").toString();
					note=GetInvoiceDetailsInwardBean.get("NOTE") == null ? "" : GetInvoiceDetailsInwardBean.get("NOTE").toString();
					indententryid=GetInvoiceDetailsInwardBean.get("INDENT_ENTRY_ID") == null ? "" : GetInvoiceDetailsInwardBean.get("INDENT_ENTRY_ID").toString();
					strEmpName = GetInvoiceDetailsInwardBean.get("EMP_NAME") == null ? "" : GetInvoiceDetailsInwardBean.get("EMP_NAME").toString();
					steReceiveDate = GetInvoiceDetailsInwardBean.get("RECEIVED_OR_ISSUED_DATE") == null ? "" : GetInvoiceDetailsInwardBean.get("RECEIVED_OR_ISSUED_DATE").toString();
					if(GetInvoiceDetailsInwardBean.get("DC_ENTRY_ID")!=null){
						isThisInvoiceConvertedFromDC = true;
					}
					}
					totalamount+=Double.parseDouble((GetInvoiceDetailsInwardBean.get("TOTAL_AMOUNT") == null ? "" : GetInvoiceDetailsInwardBean.get("TOTAL_AMOUNT").toString()));
					if(transporterName.equals("") || transporterName.equals(" ")){
						query="SELECT TRANSPORTERNAME FROM INDENT_ENTRY IE WHERE IE.INDENT_ENTRY_ID=? AND IE.SITE_ID=? AND IE.VENDOR_ID=? AND trunc(IE.INVOICE_DATE,'yy') = trunc(TO_DATE(?,'dd-MM-yy'),'yy')";
						transportorList = template.queryForList(query, new Object[] {invoiceNumber, siteId,strVendorId,invoiceDateParam});
						
						for (Map<?, ?> getData : transportorList) {
							transporterName=getData.get("TRANSPORTERNAME") == null ? "" : getData.get("TRANSPORTERNAME").toString();
						}
					}
					System.out.println("amount"+totalamount);	
					try{

						SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
						Date date = dt.parse(receviedDate); 
						Date date1 = dt.parse(invoiceDate); 

						SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy");
						receviedDate=dt1.format(date);
						invoiceDate=dt1.format(date1);
						if(!poDate.equals("") && poDate != null){
							Date date2 = dt.parse(poDate); 
							poDate=dt1.format(date2);
						}
					}catch(Exception e){
						e.printStackTrace();
					}

					//GetGrnDetailsInward.add(objGetInvoiceDetailsInward);

					double totalAmt=Double.valueOf(totalamount);
					totalAmt =Double.parseDouble(new DecimalFormat("##.##").format(totalAmt));
					int val = (int) Math.ceil(totalAmt);
					double roundoff=Math.ceil(totalAmt)-totalAmt;
					double grandtotal=Math.ceil(totalAmt);
					
					
					NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
				      String strtotal = numberFormat.format(totalAmt);
				      
					//String strtotal=String.valueOf(totalAmt);
					String strroundoff=String.format("%.2f",roundoff);
					String strgrandtotal=numberFormat.format(grandtotal);
					//StringBuffer tbldata=new tbldata();

					if(isThisInvoiceConvertedFromDC){
						tblOneData= gstinNumber+"@@@@"+invoiceid+"@@"+invoiceDate+"@@"+"Sumadhura"+"@@"+""+strtotal+"@@"+new NumberToWord().convertNumberToWords(val)+" Rupees Only."+"@@"+vendorAddress+"@@"+grn_no+"@@"+poNo+"@@"+dcNo+"@@"+vendorName+"@@"+poDate+"@@"+eWayBillNo+"@@"+vehileNo+"@@"+transporterName+"@@"+doubleSumOfOtherCharges+"@@"+invoiceDate+"@@"+strroundoff+"@@"+strgrandtotal+"@@"+strEmpName+"@@"+steReceiveDate;
					}
					else{
						tblOneData= gstinNumber+"@@@@"+invoiceid+"@@"+invoiceDate+"@@"+"Sumadhura"+"@@"+""+strtotal+"@@"+new NumberToWord().convertNumberToWords(val)+" Rupees Only."+"@@"+vendorAddress+"@@"+grn_no+"@@"+poNo+"@@"+dcNo+"@@"+vendorName+"@@"+poDate+"@@"+eWayBillNo+"@@"+vehileNo+"@@"+transporterName+"@@"+doubleSumOfOtherCharges+"@@"+receviedDate+"@@"+strroundoff+"@@"+strgrandtotal+"@@"+strEmpName+"@@"+steReceiveDate;
					}
					
					System.out.println(tblOneData);
				}


			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}



		return  tblOneData;
	}

	public String getProductDetails(String invoiceNumber, String siteId,String strVendorId,String invoiceDate) {
		List<Map<String, Object>> GetInvoiceDetailsList = null;

		//List<GetInvoiceDetailsBean> GetInvoiceDetailsInward = new ArrayList<GetInvoiceDetailsBean>();
		GetInvoiceDetailsBean objGetInvoiceDetailsInward=null;JdbcTemplate template = null;
		String sql = "";	
		String tblTwoData="";
		try{

			String product="";
			String subProduct="";

			String childProduct="";
			String price="";
			String basicAmnt="";
			String unitsOfMeasurement="";
			String tax="";
			String hsnCd="";
			String taxAmnt="";
			String amntAfterTax="";


			String quantity="";
			String state="";
			double amt=0.0;
			double CGSTAMT=0.0;
			double SGSTAMT=0.0;
			double percent=0.0;
			String CGST="";
			String SGST="";
			String IGST="";

			double IGSTAMT=0.0;
			String note="";
			int count=0;
			String strCGSTAMT = "";
			String strSGSTAMT = "";
			String strIGSTAMT = "";
			template = new JdbcTemplate(DBConnection.getDbConnection());
			DecimalFormat df = new DecimalFormat("#.00");

			if (StringUtils.isNotBlank(invoiceNumber) ) {

				sql+="select IED.PRODUCT_NAME,IE.STATE,IED.SUB_PRODUCT_NAME,IED.CHILD_PRODUCT_NAME,IED.MEASUR_MNT_NAME,IED.RECEVED_QTY,SPL.TAX,SPL.TAX_AMOUNT,SPL.AMOUNT_AFTER_TAX,SPL.BASIC_AMOUNT,SPL.AMOUNT_PER_UNIT_BEFORE_TAXES,SPL.HSN_CODE from INDENT_ENTRY_DETAILS IED,SUMADHURA_PRICE_LIST SPL,INDENT_ENTRY IE where SPL.INDENT_ENTRY_DETAILS_ID = IED.INDENT_ENTRY_DETAILS_ID and IED.INDENT_ENTRY_ID = IE.INDENT_ENTRY_ID and IE.INVOICE_ID = ? and VENDOR_ID = ? AND SPL.SITE_ID= ?  "
						+ "AND trunc(IE.INVOICE_DATE,'yy') = trunc(TO_DATE( ?, 'dd-MM-yy'),'yy')";

				GetInvoiceDetailsList = template.queryForList(sql, new Object[] {invoiceNumber, strVendorId,siteId,invoiceDate});
				//System.out.println("second product data in service"+GetInvoiceDetailsList);
			} 
			if (null != GetInvoiceDetailsList && GetInvoiceDetailsList.size() > 0) {
				for (Map<?, ?> GetInvoiceDetailsInwardBean : GetInvoiceDetailsList) {

					objGetInvoiceDetailsInward = new GetInvoiceDetailsBean();
					product=GetInvoiceDetailsInwardBean.get("PRODUCT_NAME") == null ? "" : GetInvoiceDetailsInwardBean.get("PRODUCT_NAME").toString();
					subProduct=GetInvoiceDetailsInwardBean.get("SUB_PRODUCT_NAME") == null ? "": GetInvoiceDetailsInwardBean.get("SUB_PRODUCT_NAME").toString();
					childProduct=GetInvoiceDetailsInwardBean.get("CHILD_PRODUCT_NAME") == null ? "" : GetInvoiceDetailsInwardBean.get("CHILD_PRODUCT_NAME").toString();		
					unitsOfMeasurement=GetInvoiceDetailsInwardBean.get("MEASUR_MNT_NAME") == null ? "" : GetInvoiceDetailsInwardBean.get("MEASUR_MNT_NAME").toString();	
					hsnCd=GetInvoiceDetailsInwardBean.get("HSN_CODE") == null ? "-" : GetInvoiceDetailsInwardBean.get("HSN_CODE").toString();	
					quantity=GetInvoiceDetailsInwardBean.get("RECEVED_QTY") == null ? "" : GetInvoiceDetailsInwardBean.get("RECEVED_QTY").toString();
					price=GetInvoiceDetailsInwardBean.get("AMOUNT_PER_UNIT_BEFORE_TAXES") == null ? "" : GetInvoiceDetailsInwardBean.get("AMOUNT_PER_UNIT_BEFORE_TAXES").toString();
					String basicAmnt1=GetInvoiceDetailsInwardBean.get("BASIC_AMOUNT") == null ? "" : GetInvoiceDetailsInwardBean.get("BASIC_AMOUNT").toString();
					tax = GetInvoiceDetailsInwardBean.get("TAX") == null ? "0" : GetInvoiceDetailsInwardBean.get("TAX").toString();
					taxAmnt = GetInvoiceDetailsInwardBean.get("TAX_AMOUNT") == null ? "0" : GetInvoiceDetailsInwardBean.get("TAX_AMOUNT").toString();

					  String strtotal=GetInvoiceDetailsInwardBean.get("AMOUNT_AFTER_TAX") == null ? "0" : GetInvoiceDetailsInwardBean.get("AMOUNT_AFTER_TAX").toString();
					
					  System.out.println("the total amount"+strtotal);
					  System.out.println("the total basicamount"+basicAmnt1);
				//	 String strtot=df.format(Double.valueOf(strtotal)).toString();
					
					// String baiscamt=df.format(Double.valueOf(basicAmnt1)).toString();
					  
					  
					  
					  //String strtot=(Double.valueOf(strtotal)).toString();
					  //String baiscamt=(Double.valueOf(basicAmnt1)).toString();
					  double basicamt=Double.valueOf(basicAmnt1);
					  double no=Double.valueOf(strtotal);
					  basicamt=Double.parseDouble(new DecimalFormat("##.##").format((basicamt)));
					  no=Double.parseDouble(new DecimalFormat("##.##").format((no)));
					  quantity=String.format("%.2f",Double.valueOf(quantity));
					  price=String.format("%.2f",Double.valueOf(price));
					  NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
					  amntAfterTax =numberFormat.format(no);
					  basicAmnt=numberFormat.format(basicamt);
					
					
					
					state=GetInvoiceDetailsInwardBean.get("STATE") == null ? "" : GetInvoiceDetailsInwardBean.get("STATE").toString();
					request.setAttribute("state",state);

					if (state.equals("1") || state.equals("Local")) {
						/*						taxPercentage = taxPercentage.substring(0,taxPercentage.length()-1);*/
						if (tax.equals("0")) {
							CGST = "0.00";
							SGST = "0.00";
							 strCGSTAMT="0.00";
							 strSGSTAMT="0.00";
						} else {
							percent = Double.parseDouble(tax)/2;
							amt = Double.parseDouble(taxAmnt)/2;
							CGSTAMT = amt;
							SGSTAMT = amt;
							CGST = String.valueOf(percent);
							SGST = String.valueOf(percent);
							strCGSTAMT =String.format("%.2f",CGSTAMT);
							 strSGSTAMT =String.format("%.2f",SGSTAMT);
						}
					} else {
						percent = Double.parseDouble(tax);
						amt = Double.parseDouble(taxAmnt);
						IGST = String.valueOf(percent);
						IGSTAMT = amt;
						strIGSTAMT=String.format("%.2f",IGSTAMT);
					}

					count = count+1;

					if (state.equals("1") || state.equals("Local")) {

						tblTwoData+=count+"@@"+childProduct+"@@"+hsnCd+"@@"+note+"@@"+unitsOfMeasurement+"@@"+quantity+"@@"+quantity+"@@"+price+"@@"+basicAmnt+"@@"+CGST+"@@"+strCGSTAMT+"@@"+SGST+"@@"+strSGSTAMT+"@@"+""+"@@"+""+"@@"+amntAfterTax+"@@"+note+"@@"+"-"+"&&";
					} else {
						tblTwoData+=count+"@@"+childProduct+"@@"+hsnCd+"@@"+note+"@@"+unitsOfMeasurement+"@@"+quantity+"@@"+quantity+"@@"+price+"@@"+basicAmnt+"@@"+""+"@@"+""+"@@"+""+"@@"+""+"@@"+IGST+"@@"+strIGSTAMT+"@@"+amntAfterTax+"@@"+note+"@@"+"-"+"&&";
						//tblTwoData.append(count+"@@"+hsnCd+" "+note+"@@"+measurementName+"@@"+"-"+"@@"+quantity+"@@"+quantity+"@@"+prc+"@@"+"@@"+basicAmnt+"@@"+"@@"+basicAmnt+"@@"+"-"+"&&");
					}

				}

			}	

		}catch (Exception e) {
			e.printStackTrace();
		}


		return tblTwoData;
	}	

	@Override
	public List<GetInvoiceDetailsBean> getIssueLists(String indentEntryId, String siteId,String vendorName,String invoiceDate) {

		List<Map<String, Object>> productList = null;

		List<GetInvoiceDetailsBean> GetInvoiceDetailsInward = new ArrayList<GetInvoiceDetailsBean>();
		GetInvoiceDetailsBean objGetInvoiceDetailsInward = null;
		//JdbcTemplate jdbcTemplate = null;
		String sql = "";

		try {

	//		jdbcTemplate = new JdbcTemplate(DBConnection.getDbConnection());

			if (StringUtils.isNotBlank(indentEntryId)) {
				// sql = "SELECT distinct IE.STATE,IE.TOTAL_AMOUNT,IE.NOTE,
				// IE.RECEVED_DATE,IED.TRANSPORTERNAME,
				// IED.VEHICLENO,IED.EWAYBILLNO,
				// IED.PO_NUMBER,IED.PODATE,VD.VENDOR_NAME,VD.ADDRESS,VD.GSIN_NUMBER,IE.INVOICE_ID,
				// IE.INVOICE_DATE FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS
				// IED, VENDOR_DETAILS VD WHERE IE.INDENT_ENTRY_ID =
				// IED.INDENT_ENTRY_ID AND IE.VENDOR_ID=VD.VENDOR_ID AND
				// IE.INVOICE_ID=? AND IE.SITE_ID=? AND IE.INDENT_TYPE='IN'";
				sql = "SELECT distinct IE.STATE,IE.TOTAL_AMOUNT,IE.NOTE,IE.VENDOR_ID, IE.INDENT_ENTRY_ID,IE.RECEIVED_OR_ISSUED_DATE,CONCAT(TD.FIRST_NAME,' '||TD.LAST_NAME)  as TRANSPORTERNAME, " +
					" IE.VEHICLENO,IE.EWAYBILLNO, IE.PO_ID,IE.PODATE,VD.VENDOR_NAME,VD.ADDRESS,VD.GSIN_NUMBER,IE.INVOICE_ID, IE.INVOICE_DATE " +
					" FROM INDENT_ENTRY_DETAILS IED, VENDOR_DETAILS VD,INDENT_ENTRY IE LEFT OUTER JOIN TRANSPORTOR_DETAILS TD ON TD.TRANSPORTOR_ID=IE.TRANSPORTERNAME WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID " +
					" AND IE.VENDOR_ID=VD.VENDOR_ID AND IE.INDENT_ENTRY_ID=?  and lower(VD.VENDOR_NAME)=lower(?) AND IE.SITE_ID=? AND IE.INDENT_TYPE='IN' " +
					" AND trunc(IE.INVOICE_DATE,'yy') = trunc(TO_DATE(? ,'dd-MM-yy'),'yy')";
				productList = jdbcTemplate.queryForList(sql, new Object[] { indentEntryId,vendorName, siteId,invoiceDate });
			}
			if (null != productList && productList.size() > 0) {

				// int i = 1;
				for (Map<?, ?> GetInvoiceDetailsInwardBean : productList) {

					objGetInvoiceDetailsInward = new GetInvoiceDetailsBean();
					objGetInvoiceDetailsInward.setState(GetInvoiceDetailsInwardBean.get("STATE") == null ? ""
							: GetInvoiceDetailsInwardBean.get("STATE").toString());
					objGetInvoiceDetailsInward.setInvoiceNumber(GetInvoiceDetailsInwardBean.get("INVOICE_ID") == null
							? "" : GetInvoiceDetailsInwardBean.get("INVOICE_ID").toString());

					objGetInvoiceDetailsInward
							.setInvoiceDate(toStrDate(GetInvoiceDetailsInwardBean.get("INVOICE_DATE") == null ? ""
									: GetInvoiceDetailsInwardBean.get("INVOICE_DATE").toString()));
					objGetInvoiceDetailsInward.setVendorName(GetInvoiceDetailsInwardBean.get("VENDOR_NAME") == null ? ""
							: GetInvoiceDetailsInwardBean.get("VENDOR_NAME").toString());
					objGetInvoiceDetailsInward.setGsinNo(GetInvoiceDetailsInwardBean.get("GSIN_NUMBER") == null ? ""
							: GetInvoiceDetailsInwardBean.get("GSIN_NUMBER").toString());
					objGetInvoiceDetailsInward.setPoNo(GetInvoiceDetailsInwardBean.get("PO_ID") == null ? ""
							: GetInvoiceDetailsInwardBean.get("PO_ID").toString());
					objGetInvoiceDetailsInward.setVendorAdress(GetInvoiceDetailsInwardBean.get("ADDRESS") == null ? ""
							: GetInvoiceDetailsInwardBean.get("ADDRESS").toString());
					objGetInvoiceDetailsInward.setPoDate(toStrDate(GetInvoiceDetailsInwardBean.get("PODATE") == null
							? "" : GetInvoiceDetailsInwardBean.get("PODATE").toString()));
					objGetInvoiceDetailsInward.seteWayBillNo(GetInvoiceDetailsInwardBean.get("EWAYBILLNO") == null ? ""
							: GetInvoiceDetailsInwardBean.get("EWAYBILLNO").toString());
					objGetInvoiceDetailsInward.setVehileNo(GetInvoiceDetailsInwardBean.get("VEHICLENO") == null ? ""
							: GetInvoiceDetailsInwardBean.get("VEHICLENO").toString());
					objGetInvoiceDetailsInward
							.setTransporterName(GetInvoiceDetailsInwardBean.get("TRANSPORTERNAME") == null ? ""
									: GetInvoiceDetailsInwardBean.get("TRANSPORTERNAME").toString());
					objGetInvoiceDetailsInward.setReceivedDate(
							toStrDate(GetInvoiceDetailsInwardBean.get("RECEIVED_OR_ISSUED_DATE") == null ? ""
									: GetInvoiceDetailsInwardBean.get("RECEIVED_OR_ISSUED_DATE").toString()));
					objGetInvoiceDetailsInward.setTotalAmount(GetInvoiceDetailsInwardBean.get("TOTAL_AMOUNT") == null
							? "" : GetInvoiceDetailsInwardBean.get("TOTAL_AMOUNT").toString());
					objGetInvoiceDetailsInward.setNote(GetInvoiceDetailsInwardBean.get("NOTE") == null ? ""
							: GetInvoiceDetailsInwardBean.get("NOTE").toString());
					objGetInvoiceDetailsInward
							.setIndentEntryId(GetInvoiceDetailsInwardBean.get("INDENT_ENTRY_ID") == null ? ""
									: GetInvoiceDetailsInwardBean.get("INDENT_ENTRY_ID").toString());
					objGetInvoiceDetailsInward
					.setVendorId(GetInvoiceDetailsInwardBean.get("VENDOR_ID") == null ? ""
							: GetInvoiceDetailsInwardBean.get("VENDOR_ID").toString());

					GetInvoiceDetailsInward.add(objGetInvoiceDetailsInward);
					// i++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return GetInvoiceDetailsInward;
	}

	@Override
	public List<GetInvoiceDetailsBean> getInvoiceDetails(String invoiceNumber, String siteId,String vendorId,String invoiceDate, String indentType) {

		List<Map<String, Object>> productList = null;
		List<Map<String, Object>> transportorList = null;

		List<GetInvoiceDetailsBean> GetInvoiceDetailsInward = new ArrayList<GetInvoiceDetailsBean>();
		GetInvoiceDetailsBean objGetInvoiceDetailsInward=null;
		JdbcTemplate template = null;
		String sql = "";
		String  transporterName="";
		String query="";
		if(indentType==null){
			indentType="IN";
		}else 	if(indentType.length()==0){
			indentType="IN";
		}
		try {
			template = new JdbcTemplate(DBConnection.getDbConnection());

			if (StringUtils.isNotBlank(invoiceNumber) ) {
				//sql = "SELECT distinct IE.STATE,IE.TOTAL_AMOUNT,IE.NOTE, IE.RECEVED_DATE,IED.TRANSPORTERNAME, IED.VEHICLENO,IED.EWAYBILLNO, IED.PO_NUMBER,IED.PODATE,VD.VENDOR_NAME,VD.ADDRESS,VD.GSIN_NUMBER,IE.INVOICE_ID, IE.INVOICE_DATE FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, VENDOR_DETAILS VD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND IE.VENDOR_ID=VD.VENDOR_ID AND IE.INVOICE_ID=? AND IE.SITE_ID=? AND IE.INDENT_TYPE='IN'";
				sql = "SELECT  NVL((SELECT TAXINVOICE_STATUS FROM ACC_TAXINVOICE_DETAILS ATD WHERE IE.INDENT_ENTRY_ID=ATD.INDENT_ENTRY_ID),'No')  AS TAXINVOICE_STATUS," 
					+" IE.STATE,IE.TOTAL_AMOUNT,IE.NOTE, IE.INDENT_ENTRY_ID ,IE.RECEIVED_OR_ISSUED_DATE,CONCAT(TD.FIRST_NAME,' '||TD.LAST_NAME)  as TRANSPORTERNAME, IE.VEHICLENO,IE.EWAYBILLNO, " 
					+" IE.PO_ID,IE.PODATE,VD.VENDOR_NAME,VD.ADDRESS,VD.GSIN_NUMBER,IE.INVOICE_ID, IE.INVOICE_DATE,IE.SITE_ID,IE.REQUESTER_ID,IE.REQUESTER_NAME FROM " 
					+" INDENT_ENTRY_DETAILS IED, VENDOR_DETAILS VD,INDENT_ENTRY IE LEFT OUTER JOIN TRANSPORTOR_DETAILS TD ON TD.TRANSPORTOR_ID=IE.TRANSPORTERNAME WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND IE.VENDOR_ID=VD.VENDOR_ID AND IE.INVOICE_ID=? AND IE.SITE_ID=? " 
					+" AND IE.INDENT_TYPE='"+indentType+"' AND IE.VENDOR_ID = ? "+ " AND trunc(IE.INVOICE_DATE,'yy') = trunc(TO_DATE( ? ,'dd-MM-yy'),'yy')";
				productList = template.queryForList(sql, new Object[] {invoiceNumber, siteId,vendorId,invoiceDate});
			} 
			if (null != productList && productList.size() > 0) {
				//int i = 1;
				for (Map<?, ?> GetInvoiceDetailsInwardBean : productList) {

					
					String istaxInvoiceSubmitted	=GetInvoiceDetailsInwardBean.get("TAXINVOICE_STATUS") == null ? "" : GetInvoiceDetailsInwardBean.get("TAXINVOICE_STATUS").toString();
					if(!istaxInvoiceSubmitted.equals("No")){//SUBMITTED,PENDING
						istaxInvoiceSubmitted="Yes";
					}
					objGetInvoiceDetailsInward = new GetInvoiceDetailsBean();
					objGetInvoiceDetailsInward.setIstaxInvoiceSubmitted(istaxInvoiceSubmitted);
					objGetInvoiceDetailsInward.setState(GetInvoiceDetailsInwardBean.get("STATE") == null ? "-" : GetInvoiceDetailsInwardBean.get("STATE").toString());
					objGetInvoiceDetailsInward.setInvoiceNumber(GetInvoiceDetailsInwardBean.get("INVOICE_ID") == null ? "" : GetInvoiceDetailsInwardBean.get("INVOICE_ID").toString());

					objGetInvoiceDetailsInward.setInvoiceDate(toStrDate(GetInvoiceDetailsInwardBean.get("INVOICE_DATE") == null ? "": GetInvoiceDetailsInwardBean.get("INVOICE_DATE").toString()));
					objGetInvoiceDetailsInward.setVendorName(GetInvoiceDetailsInwardBean.get("VENDOR_NAME") == null ? "-" : GetInvoiceDetailsInwardBean.get("VENDOR_NAME").toString());
					objGetInvoiceDetailsInward.setGsinNo(GetInvoiceDetailsInwardBean.get("GSIN_NUMBER") == null ? "" : GetInvoiceDetailsInwardBean.get("GSIN_NUMBER").toString());
					objGetInvoiceDetailsInward.setPoNo(GetInvoiceDetailsInwardBean.get("PO_ID") == null ? "-" : GetInvoiceDetailsInwardBean.get("PO_ID").toString());
					objGetInvoiceDetailsInward.setVendorAdress(GetInvoiceDetailsInwardBean.get("ADDRESS") == null ? "-" : GetInvoiceDetailsInwardBean.get("ADDRESS").toString());
					objGetInvoiceDetailsInward.setPoDate(toStrDate(GetInvoiceDetailsInwardBean.get("PODATE") == null ? "-" : GetInvoiceDetailsInwardBean.get("PODATE").toString()));
					objGetInvoiceDetailsInward.seteWayBillNo(GetInvoiceDetailsInwardBean.get("EWAYBILLNO") == null ? "-" : GetInvoiceDetailsInwardBean.get("EWAYBILLNO").toString());
					objGetInvoiceDetailsInward.setVehileNo(GetInvoiceDetailsInwardBean.get("VEHICLENO") == null ? "-" : GetInvoiceDetailsInwardBean.get("VEHICLENO").toString());
					
					transporterName=GetInvoiceDetailsInwardBean.get("TRANSPORTERNAME") == null ? "-" : GetInvoiceDetailsInwardBean.get("TRANSPORTERNAME").toString();
					objGetInvoiceDetailsInward.setReceivedDate(toStrDate(GetInvoiceDetailsInwardBean.get("RECEIVED_OR_ISSUED_DATE") == null ? "-" : GetInvoiceDetailsInwardBean.get("RECEIVED_OR_ISSUED_DATE").toString()));
					objGetInvoiceDetailsInward.setTotalAmount(GetInvoiceDetailsInwardBean.get("TOTAL_AMOUNT") == null ? "-" : GetInvoiceDetailsInwardBean.get("TOTAL_AMOUNT").toString());
					objGetInvoiceDetailsInward.setNote(GetInvoiceDetailsInwardBean.get("NOTE") == null ? "-" : GetInvoiceDetailsInwardBean.get("NOTE").toString());
					objGetInvoiceDetailsInward.setIndentEntryId(GetInvoiceDetailsInwardBean.get("INDENT_ENTRY_ID") == null ? "-" : GetInvoiceDetailsInwardBean.get("INDENT_ENTRY_ID").toString());
					objGetInvoiceDetailsInward.setSiteId(GetInvoiceDetailsInwardBean.get("SITE_ID") == null ? "-" : GetInvoiceDetailsInwardBean.get("SITE_ID").toString());
					
					objGetInvoiceDetailsInward.setEmployeeName(GetInvoiceDetailsInwardBean.get("REQUESTER_NAME") == null ? "-" : GetInvoiceDetailsInwardBean.get("REQUESTER_NAME").toString());
					objGetInvoiceDetailsInward.setEmployeeId(GetInvoiceDetailsInwardBean.get("REQUESTER_ID") == null ? "-" : GetInvoiceDetailsInwardBean.get("REQUESTER_ID").toString());
					objGetInvoiceDetailsInward.setCreditTotalAmount(GetInvoiceDetailsInwardBean.get("CREDIT_TOTAL_AMOUNT") == null ? "0" : GetInvoiceDetailsInwardBean.get("CREDIT_TOTAL_AMOUNT").toString());
					objGetInvoiceDetailsInward.setCreditNoteType(GetInvoiceDetailsInwardBean.get("TYPE") == null ? "" : GetInvoiceDetailsInwardBean.get("TYPE").toString());
					
					if(transporterName.equals("") || transporterName.equals(" ")){
						query="SELECT TRANSPORTERNAME FROM INDENT_ENTRY IE WHERE IE.INDENT_ENTRY_ID=? AND IE.SITE_ID=? AND IE.VENDOR_ID=? AND trunc(IE.INVOICE_DATE,'yy') = trunc(TO_DATE(?,'dd-MM-yy'),'yy')";
						transportorList = template.queryForList(query, new Object[] {invoiceNumber, siteId,vendorId,invoiceDate});
						
						for (Map<?, ?> getData : transportorList) {
							transporterName=getData.get("TRANSPORTERNAME") == null ? "" : getData.get("TRANSPORTERNAME").toString();
						}
					}
					objGetInvoiceDetailsInward.setTransporterName(transporterName);
					GetInvoiceDetailsInward.add(objGetInvoiceDetailsInward);
					//i++;
					break;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return GetInvoiceDetailsInward;
	}
	private String toStrDate(String invoicedate) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		Date parsedDate;
		try{ parsedDate = dateFormat.parse(invoicedate);} catch(Exception e){return "N/A";}
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String dateWithZeroTime = formatter.format(parsedDate);
		return dateWithZeroTime;
	}

	@Override
	public List<GetInvoiceDetailsBean> getGetInvoiceDetailsLists(String indentEntryId, String siteId,String vendorName) {
		List<Map<String, Object>> GetInvoiceDetailsList = null;

		List<GetInvoiceDetailsBean> GetInvoiceDetailsInward = new ArrayList<GetInvoiceDetailsBean>();
		GetInvoiceDetailsBean objGetInvoiceDetailsInward=null;
		JdbcTemplate template = null;
		String sql = "";

		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());
			if (StringUtils.isNotBlank(indentEntryId) ) {
				sql ="SELECT IED.INDENT_ENTRY_DETAILS_ID,IE.INDENT_ENTRY_ID,IED.PRODUCT_ID,IED.PRODUCT_NAME,IED.SUB_PRODUCT_ID,IED.SUB_PRODUCT_NAME,IED.CHILD_PRODUCT_ID,	IED.CHILD_PRODUCT_NAME,IED.MEASUR_MNT_ID,IED.MEASUR_MNT_NAME,IED.RECEVED_QTY,SPL.AMOUNT_PER_UNIT_BEFORE_TAXES,SPL.BASIC_AMOUNT,SPL.TAX,	SPL.HSN_CODE,SPL.TAX_AMOUNT,SPL.AMOUNT_AFTER_TAX,SPL.OTHER_CHARGES,SPL.TAX_ON_OTHER_TRANSPORT_CHG,SPL.OTHER_CHARGES_AFTER_TAX,	SPL.TOTAL_AMOUNT,IED.EXPIRY_DATE FROM  INDENT_ENTRY_DETAILS IED,INDENT_ENTRY IE,SUMADHURA_PRICE_LIST SPL , VENDOR_DETAILS VD	WHERE  IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID and  IED.INDENT_ENTRY_DETAILS_ID = SPL.INDENT_ENTRY_DETAILS_ID AND IE.VENDOR_ID=VD.VENDOR_ID	AND IE.INDENT_ENTRY_ID=? AND lower(VD.VENDOR_NAME)=lower(?)  AND IE.SITE_ID=?  AND IE.INDENT_TYPE='IN' ";
				GetInvoiceDetailsList = template.queryForList(sql, new Object[] {indentEntryId,vendorName,siteId});
			} 
			if (null != GetInvoiceDetailsList && GetInvoiceDetailsList.size() > 0) {
				int i = 1;
				double finalamountdiv=0.0;
				for (Map<?, ?> GetInvoiceDetailsInwardBean : GetInvoiceDetailsList) {

					objGetInvoiceDetailsInward = new GetInvoiceDetailsBean();
					objGetInvoiceDetailsInward.setIndentEntryDetailsId(GetInvoiceDetailsInwardBean.get("INDENT_ENTRY_DETAILS_ID") == null ? "" : GetInvoiceDetailsInwardBean.get("INDENT_ENTRY_DETAILS_ID").toString());
					objGetInvoiceDetailsInward.setProductId(GetInvoiceDetailsInwardBean.get("PRODUCT_NAME") == null ? "" : GetInvoiceDetailsInwardBean.get("PRODUCT_NAME").toString());
					objGetInvoiceDetailsInward.setSubProductId(GetInvoiceDetailsInwardBean.get("SUB_PRODUCT_NAME") == null ? "": GetInvoiceDetailsInwardBean.get("SUB_PRODUCT_NAME").toString());
					objGetInvoiceDetailsInward.setChildProductId(GetInvoiceDetailsInwardBean.get("CHILD_PRODUCT_NAME") == null ? "" : GetInvoiceDetailsInwardBean.get("CHILD_PRODUCT_NAME").toString());
					objGetInvoiceDetailsInward.setChildProductId(GetInvoiceDetailsInwardBean.get("MEASUR_MNT_NAME") == null ? "" : GetInvoiceDetailsInwardBean.get("MEASUR_MNT_NAME").toString());
					objGetInvoiceDetailsInward.setProduct(GetInvoiceDetailsInwardBean.get("PRODUCT_NAME") == null ? "" : GetInvoiceDetailsInwardBean.get("PRODUCT_NAME").toString());
					objGetInvoiceDetailsInward.setSubProduct(GetInvoiceDetailsInwardBean.get("SUB_PRODUCT_NAME") == null ? "": GetInvoiceDetailsInwardBean.get("SUB_PRODUCT_NAME").toString());
					objGetInvoiceDetailsInward.setChildProduct(GetInvoiceDetailsInwardBean.get("CHILD_PRODUCT_NAME") == null ? "" : GetInvoiceDetailsInwardBean.get("CHILD_PRODUCT_NAME").toString());
					objGetInvoiceDetailsInward.setUnitsOfMeasurement(GetInvoiceDetailsInwardBean.get("MEASUR_MNT_NAME") == null ? "" : GetInvoiceDetailsInwardBean.get("MEASUR_MNT_NAME").toString());
					objGetInvoiceDetailsInward.setQuantity(GetInvoiceDetailsInwardBean.get("RECEVED_QTY") == null ? "" : GetInvoiceDetailsInwardBean.get("RECEVED_QTY").toString());
					objGetInvoiceDetailsInward.setHsnCode(GetInvoiceDetailsInwardBean.get("HSN_CODE") == null ? "" : GetInvoiceDetailsInwardBean.get("HSN_CODE").toString());
					objGetInvoiceDetailsInward.setPrice(GetInvoiceDetailsInwardBean.get("AMOUNT_PER_UNIT_BEFORE_TAXES") == null ? "" : GetInvoiceDetailsInwardBean.get("AMOUNT_PER_UNIT_BEFORE_TAXES").toString());
					objGetInvoiceDetailsInward.setTotalAmount(GetInvoiceDetailsInwardBean.get("TOTAL_AMOUNT") == null ? "" : GetInvoiceDetailsInwardBean.get("TOTAL_AMOUNT").toString());
					objGetInvoiceDetailsInward.setBasicAmount(GetInvoiceDetailsInwardBean.get("BASIC_AMOUNT") == null ? "" : GetInvoiceDetailsInwardBean.get("BASIC_AMOUNT").toString());
					objGetInvoiceDetailsInward.setTax(GetInvoiceDetailsInwardBean.get("TAX") == null ? "" : GetInvoiceDetailsInwardBean.get("TAX").toString());
					objGetInvoiceDetailsInward.setTaxAmount(GetInvoiceDetailsInwardBean.get("TAX_AMOUNT") == null ? "" : GetInvoiceDetailsInwardBean.get("TAX_AMOUNT").toString());
					objGetInvoiceDetailsInward.setAmountAfterTax(GetInvoiceDetailsInwardBean.get("AMOUNT_AFTER_TAX") == null ? "" : GetInvoiceDetailsInwardBean.get("AMOUNT_AFTER_TAX").toString());
					objGetInvoiceDetailsInward.setOtherOrTransportCharges(GetInvoiceDetailsInwardBean.get("OTHER_CHARGES") == null ? "" : GetInvoiceDetailsInwardBean.get("OTHER_CHARGES").toString());

					objGetInvoiceDetailsInward.setFinalAmountDiv(GetInvoiceDetailsInwardBean.get("TOTAL_AMOUNT") == null ? "" : GetInvoiceDetailsInwardBean.get("TOTAL_AMOUNT").toString());
					String s=objGetInvoiceDetailsInward.getFinalAmountDiv();
					if(s!="")
					{
						finalamountdiv = finalamountdiv+Double.parseDouble(s);
						String strFinalAmt=String.format("%.2f",finalamountdiv);
						objGetInvoiceDetailsInward.setFinalAmountDiv(strFinalAmt);
					}


					objGetInvoiceDetailsInward.setTaxOnOtherOrTransportCharges(GetInvoiceDetailsInwardBean.get("TAX_ON_OTHER_TRANSPORT_CHG") == null ? "" : GetInvoiceDetailsInwardBean.get("TAX_ON_OTHER_TRANSPORT_CHG").toString());
					objGetInvoiceDetailsInward.setOtherOrTransportChargesAfterTax(GetInvoiceDetailsInwardBean.get("OTHER_CHARGES_AFTER_TAX") == null ? "" : GetInvoiceDetailsInwardBean.get("OTHER_CHARGES_AFTER_TAX").toString());
					objGetInvoiceDetailsInward.setExpireDate(GetInvoiceDetailsInwardBean.get("EXPIRY_DATE") == null ? "" : GetInvoiceDetailsInwardBean.get("EXPIRY_DATE").toString());
					objGetInvoiceDetailsInward.setStrSerialNo(i);

					GetInvoiceDetailsInward.add(objGetInvoiceDetailsInward);

					i++;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return GetInvoiceDetailsInward;
	}
	@Override
	public List<GetInvoiceDetailsBean> getGetInvoiceDetailsLists2(String invoiceNumber, String siteId,String vendorId,String invoiceDate, String indentType) {
		//System.out.println("InwardGetInvoiceDetailsDaoImpl.getGetInvoiceDetailsLists2() vendor id is "+vendorId );
		List<Map<String, Object>> GetInvoiceDetailsList = null;

		List<GetInvoiceDetailsBean> GetInvoiceDetailsInward = new ArrayList<GetInvoiceDetailsBean>();
		GetInvoiceDetailsBean objGetInvoiceDetailsInward=null;
		JdbcTemplate template = null;
		String sql = "";
		com.ibm.icu.text.NumberFormat format = com.ibm.icu.text.NumberFormat.getCurrencyInstance(new Locale("en", "in"));
		double totalAmount=0.0;
		double quantity=0.0;
		double price=0.0;
		double basicAmount=0.0;
		double taxAmount=0.0;
		double amountAfterTax=0.0;
		double otherCharges=0.0;
		try {
			if(indentType==null){
				indentType="IN";
			}else if(indentType.length()==0){
				indentType="IN";
			}
			
			template = new JdbcTemplate(DBConnection.getDbConnection());

			if (StringUtils.isNotBlank(invoiceNumber) ) {
				sql = " SELECT IED.INDENT_ENTRY_DETAILS_ID, IED.PRODUCT_ID,IED.PRODUCT_NAME,IED.SUB_PRODUCT_ID,IED.SUB_PRODUCT_NAME,IED.CHILD_PRODUCT_ID,"+
				"IED.REMARKS,IED.CHILD_PRODUCT_NAME,IED.MEASUR_MNT_ID,IED.MEASUR_MNT_NAME,IED.RECEVED_QTY,SPL.AMOUNT_PER_UNIT_BEFORE_TAXES,SPL.BASIC_AMOUNT,SPL.TAX,"+
				"SPL.HSN_CODE,SPL.TAX_AMOUNT,SPL.AMOUNT_AFTER_TAX,SPL.OTHER_CHARGES,SPL.TAX_ON_OTHER_TRANSPORT_CHG,SPL.OTHER_CHARGES_AFTER_TAX,"+
				"SPL.TOTAL_AMOUNT,IED.EXPIRY_DATE FROM  INDENT_ENTRY_DETAILS IED,INDENT_ENTRY IE,SUMADHURA_PRICE_LIST SPL "+
				"WHERE  IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID and  IED.INDENT_ENTRY_DETAILS_ID = SPL.INDENT_ENTRY_DETAILS_ID "+
				"AND IE.INVOICE_ID=? AND IE.SITE_ID=? AND IE.INDENT_TYPE='"+indentType+"' AND IE.VENDOR_ID = ? "
				+ " AND trunc(IE.INVOICE_DATE,'yy') = trunc(TO_DATE( ? ,'dd-MM-yy'),'yy')";
				GetInvoiceDetailsList = template.queryForList(sql, new Object[] {invoiceNumber, siteId,vendorId,invoiceDate });
			} 
			if (null != GetInvoiceDetailsList && GetInvoiceDetailsList.size() > 0) {
				int i = 1;
				double finalamountdiv=0.0;
				for (Map<?, ?> GetInvoiceDetailsInwardBean : GetInvoiceDetailsList) {

					objGetInvoiceDetailsInward = new GetInvoiceDetailsBean();
					objGetInvoiceDetailsInward.setIndentEntryDetailsId(GetInvoiceDetailsInwardBean.get("INDENT_ENTRY_DETAILS_ID") == null ? "" : GetInvoiceDetailsInwardBean.get("INDENT_ENTRY_DETAILS_ID").toString());
					objGetInvoiceDetailsInward.setProductId(GetInvoiceDetailsInwardBean.get("PRODUCT_NAME") == null ? "" : GetInvoiceDetailsInwardBean.get("PRODUCT_NAME").toString());
					objGetInvoiceDetailsInward.setSubProductId(GetInvoiceDetailsInwardBean.get("SUB_PRODUCT_NAME") == null ? "": GetInvoiceDetailsInwardBean.get("SUB_PRODUCT_NAME").toString());
					objGetInvoiceDetailsInward.setChildProductId(GetInvoiceDetailsInwardBean.get("CHILD_PRODUCT_NAME") == null ? "" : GetInvoiceDetailsInwardBean.get("CHILD_PRODUCT_NAME").toString());
					objGetInvoiceDetailsInward.setChildProductId(GetInvoiceDetailsInwardBean.get("MEASUR_MNT_NAME") == null ? "" : GetInvoiceDetailsInwardBean.get("MEASUR_MNT_NAME").toString());
					objGetInvoiceDetailsInward.setProduct(GetInvoiceDetailsInwardBean.get("PRODUCT_NAME") == null ? "" : GetInvoiceDetailsInwardBean.get("PRODUCT_NAME").toString());
					objGetInvoiceDetailsInward.setSubProduct(GetInvoiceDetailsInwardBean.get("SUB_PRODUCT_NAME") == null ? "": GetInvoiceDetailsInwardBean.get("SUB_PRODUCT_NAME").toString());
					objGetInvoiceDetailsInward.setChildProduct(GetInvoiceDetailsInwardBean.get("CHILD_PRODUCT_NAME") == null ? "" : GetInvoiceDetailsInwardBean.get("CHILD_PRODUCT_NAME").toString());
					objGetInvoiceDetailsInward.setUnitsOfMeasurement(GetInvoiceDetailsInwardBean.get("MEASUR_MNT_NAME") == null ? "" : GetInvoiceDetailsInwardBean.get("MEASUR_MNT_NAME").toString());
					objGetInvoiceDetailsInward.setRemarks(GetInvoiceDetailsInwardBean.get("REMARKS") == null ? "-" : GetInvoiceDetailsInwardBean.get("REMARKS").toString());
					quantity=Double.valueOf(GetInvoiceDetailsInwardBean.get("RECEVED_QTY") == null ? "0" : GetInvoiceDetailsInwardBean.get("RECEVED_QTY").toString());
					objGetInvoiceDetailsInward.setHsnCode(GetInvoiceDetailsInwardBean.get("HSN_CODE") == null ? "-" : GetInvoiceDetailsInwardBean.get("HSN_CODE").toString());
					price=Double.valueOf(GetInvoiceDetailsInwardBean.get("AMOUNT_PER_UNIT_BEFORE_TAXES") == null ? "0" : GetInvoiceDetailsInwardBean.get("AMOUNT_PER_UNIT_BEFORE_TAXES").toString());
					totalAmount=Double.valueOf(GetInvoiceDetailsInwardBean.get("TOTAL_AMOUNT") == null ? "0" : GetInvoiceDetailsInwardBean.get("TOTAL_AMOUNT").toString());
					basicAmount=Double.valueOf(GetInvoiceDetailsInwardBean.get("BASIC_AMOUNT") == null ? "0" : GetInvoiceDetailsInwardBean.get("BASIC_AMOUNT").toString());
					objGetInvoiceDetailsInward.setTax(GetInvoiceDetailsInwardBean.get("TAX") == null ? "" : GetInvoiceDetailsInwardBean.get("TAX").toString());
					taxAmount=Double.valueOf(GetInvoiceDetailsInwardBean.get("TAX_AMOUNT") == null ? "0" : GetInvoiceDetailsInwardBean.get("TAX_AMOUNT").toString());
					amountAfterTax=Double.valueOf(GetInvoiceDetailsInwardBean.get("AMOUNT_AFTER_TAX") == null ? "0" : GetInvoiceDetailsInwardBean.get("AMOUNT_AFTER_TAX").toString());
					otherCharges=Double.valueOf(GetInvoiceDetailsInwardBean.get("OTHER_CHARGES") == null ? "0" : GetInvoiceDetailsInwardBean.get("OTHER_CHARGES").toString());

					//objGetInvoiceDetailsInward.setFinalAmountDiv(GetInvoiceDetailsInwardBean.get("TOTAL_AMOUNT") == null ? "" : GetInvoiceDetailsInwardBean.get("TOTAL_AMOUNT").toString());
					//String s=objGetInvoiceDetailsInward.getFinalAmountDiv();
					totalAmount=Double.parseDouble(new DecimalFormat("##.##").format((totalAmount)));
					// this is used to calculate final amount in jsp so set in below bean taken this separate why beacuase total amount set with comma
					objGetInvoiceDetailsInward.setFinalAmountDiv(String.format("%.2f",totalAmount));
					// to remove Rs it will come from default Conversion time Currency so use Replace here
					objGetInvoiceDetailsInward.setTotalAmount(format.format(totalAmount).replaceAll("Rs",""));
					// after decimals two value tester need two values so written String format due to this below commented
					
					/*quantity=Double.parseDouble(new DecimalFormat("##.##").format((quantity)));
					price=Double.parseDouble(new DecimalFormat("##.##").format((price)));
					String str=	String.format("%.2f", basicAmount);
					basicAmount=Double.parseDouble(new DecimalFormat("##.##").format((basicAmount)));
					taxAmount=Double.parseDouble(new DecimalFormat("##.##").format((taxAmount)));
					amountAfterTax=Double.parseDouble(new DecimalFormat("##.##").format((amountAfterTax)));
					otherCharges=Double.parseDouble(new DecimalFormat("##.##").format((otherCharges)));*/
					
					objGetInvoiceDetailsInward.setQuantity(String.format("%.2f", quantity));
					objGetInvoiceDetailsInward.setPrice(String.format("%.2f", price));
					objGetInvoiceDetailsInward.setBasicAmount(String.format("%.2f", basicAmount));
					objGetInvoiceDetailsInward.setTaxAmount(String.format("%.2f", taxAmount));
					objGetInvoiceDetailsInward.setAmountAfterTax(String.format("%.2f", amountAfterTax));
					objGetInvoiceDetailsInward.setOtherOrTransportCharges(String.format("%.2f", otherCharges));
					
					/*if(s!="")
					{
						finalamountdiv = finalamountdiv+Double.parseDouble(s);
						
						
						//finalamountdiv=format.format(finalamountdiv);
						
						System.out.println(format.format(finalamountdiv));
						String tempFinalAmount=format.format(finalamountdiv);

						objGetInvoiceDetailsInward.setFinalAmountDiv(String.valueOf(finalamountdiv));
					}

*/
					objGetInvoiceDetailsInward.setTaxOnOtherOrTransportCharges(GetInvoiceDetailsInwardBean.get("TAX_ON_OTHER_TRANSPORT_CHG") == null ? "" : GetInvoiceDetailsInwardBean.get("TAX_ON_OTHER_TRANSPORT_CHG").toString());
					objGetInvoiceDetailsInward.setOtherOrTransportChargesAfterTax(GetInvoiceDetailsInwardBean.get("OTHER_CHARGES_AFTER_TAX") == null ? "" : GetInvoiceDetailsInwardBean.get("OTHER_CHARGES_AFTER_TAX").toString());
					objGetInvoiceDetailsInward.setExpireDate(GetInvoiceDetailsInwardBean.get("EXPIRY_DATE") == null ? "N/A" : GetInvoiceDetailsInwardBean.get("EXPIRY_DATE").toString());
					objGetInvoiceDetailsInward.setStrSerialNo(i);

					GetInvoiceDetailsInward.add(objGetInvoiceDetailsInward);

					i++;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return GetInvoiceDetailsInward;
	}
	@Override
	public List<GetInvoiceDetailsBean> getTransportChargesList(String invoiceNumber, String siteId,String indentEntryId) {
		List<Map<String, Object>> productList = null;

		List<GetInvoiceDetailsBean> GetTransportChargesListDetails = new ArrayList<GetInvoiceDetailsBean>();
		GetInvoiceDetailsBean objGetTransportChargesDetails=null;
		JdbcTemplate template = null;
		String sql = "";
		String chargeName="";
		int sno=0;
		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());

			if (StringUtils.isNotBlank(invoiceNumber) ) {
				sql = "select STOCD.ID,STOCD.TRANSPORT_ID,STOCM.CHARGE_NAME,STOCD.TRANSPORT_GST_PERCENTAGE,IG.TAX_PERCENTAGE,STOCD.TRANSPORT_GST_AMOUNT,"
					+" STOCD.TOTAL_AMOUNT_AFTER_GST_TAX,STOCD.TRANSPORT_AMOUNT from SUMADHURA_TRNS_OTHR_CHRGS_DTLS  STOCD, "
					+" SUMADHURA_TRNS_OTHR_CHRGS_MST STOCM,INDENT_GST IG,INDENT_ENTRY IE where  STOCD.TRANSPORT_ID = STOCM.CHARGE_ID and" 
					+" IG.TAX_ID = STOCD.TRANSPORT_GST_PERCENTAGE and IE.INDENT_ENTRY_ID = STOCD.INDENT_ENTRY_ID and STOCD.INDENT_ENTRY_ID=? and IE.INVOICE_ID =? and IE.SITE_ID =? ";

				productList = template.queryForList(sql, new Object[] {indentEntryId,invoiceNumber,siteId});
			} 
			if (null != productList && productList.size() > 0) {
				for (Map<?, ?> GetTransportChargesDetails : productList) {
					chargeName=GetTransportChargesDetails.get("CHARGE_NAME") == null ? "" : GetTransportChargesDetails.get("CHARGE_NAME").toString();
					//if(!chargeName.equalsIgnoreCase("None")){
					sno++;
					
					objGetTransportChargesDetails = new GetInvoiceDetailsBean();
					objGetTransportChargesDetails.setStrSerialNo(sno);
					objGetTransportChargesDetails.setTransportId(GetTransportChargesDetails.get("TRANSPORT_ID") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_ID").toString());
					objGetTransportChargesDetails.setConveyance(chargeName);
					objGetTransportChargesDetails.setConveyanceAmount(GetTransportChargesDetails.get("TRANSPORT_AMOUNT") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_AMOUNT").toString());
					objGetTransportChargesDetails.setGstTaxId(GetTransportChargesDetails.get("TRANSPORT_GST_PERCENTAGE") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_GST_PERCENTAGE").toString());
					objGetTransportChargesDetails.setGSTTax(GetTransportChargesDetails.get("TAX_PERCENTAGE") == null ? "" : GetTransportChargesDetails.get("TAX_PERCENTAGE").toString());
					objGetTransportChargesDetails.setGSTAmount(GetTransportChargesDetails.get("TRANSPORT_GST_AMOUNT") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_GST_AMOUNT").toString());
					objGetTransportChargesDetails.setAmountAfterTax(GetTransportChargesDetails.get("TOTAL_AMOUNT_AFTER_GST_TAX") == null ? "" : GetTransportChargesDetails.get("TOTAL_AMOUNT_AFTER_GST_TAX").toString());
					objGetTransportChargesDetails.setiD(GetTransportChargesDetails.get("ID") == null ? "" : GetTransportChargesDetails.get("ID").toString());
					objGetTransportChargesDetails.setTransportInvoice1(GetTransportChargesDetails.get("TRANSPORT_INVOICE_ID") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_INVOICE_ID").toString());
					//objGetTransportChargesDetails.setIndentEntryInvoiceId(GetTransportChargesDetails.get("INDENT_ENTRY_INVOICE_ID") == null ? "" : GetTransportChargesDetails.get("INDENT_ENTRY_INVOICE_ID").toString());
					//objGetTransportChargesDetails.setDateAndTime(GetTransportChargesDetails.get("DATE_AND_TIME") == null ? "": GetTransportChargesDetails.get("DATE_AND_TIME").toString());
					
					
					GetTransportChargesListDetails.add(objGetTransportChargesDetails);
					//}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return GetTransportChargesListDetails;
	}
	@Override
	public List<GetInvoiceDetailsBean> getTransportChargesList2(String invoiceNumber, String siteId,String vendorId,String invoiceDate, String indentType) {
		List<Map<String, Object>> productList = null;

		List<GetInvoiceDetailsBean> GetTransportChargesListDetails = new ArrayList<GetInvoiceDetailsBean>();
		GetInvoiceDetailsBean objGetTransportChargesDetails=null;
		JdbcTemplate template = null;
		String sql = "";

		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());

			if (StringUtils.isNotBlank(invoiceNumber) ) {
				sql = "SELECT * FROM SUMADHURA_TRNS_OTHR_CHRGS_DTLS STOCD,INDENT_ENTRY IE WHERE STOCD.SITE_ID=? AND STOCD.INDENT_ENTRY_INVOICE_ID=? AND IE.INDENT_ENTRY_ID = STOCD.INDENT_ENTRY_ID AND IE.VENDOR_ID = ?"+ " AND trunc(IE.INVOICE_DATE,'yy') = trunc(TO_DATE( ? ,'dd-MM-yy'),'yy')";
				productList = template.queryForList(sql, new Object[] {invoiceNumber, siteId,vendorId,invoiceDate});
			} 
			if (null != productList && productList.size() > 0) {
				for (Map<?, ?> GetTransportChargesDetails : productList) {

					objGetTransportChargesDetails = new GetInvoiceDetailsBean();

					objGetTransportChargesDetails.setTransportId(GetTransportChargesDetails.get("TRANSPORT_ID") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_ID").toString());
					objGetTransportChargesDetails.setConveyance1(GetTransportChargesDetails.get("TRANSPORT_GST_PERCENTAGE") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_GST_PERCENTAGE").toString());
					objGetTransportChargesDetails.setGSTAmount1(GetTransportChargesDetails.get("TRANSPORT_GST_AMOUNT") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_GST_AMOUNT").toString());
					objGetTransportChargesDetails.setAmountAfterTax1(GetTransportChargesDetails.get("TOTAL_AMOUNT_AFTER_GST_TAX") == null ? "" : GetTransportChargesDetails.get("TOTAL_AMOUNT_AFTER_GST_TAX").toString());
					objGetTransportChargesDetails.setTransportInvoice1(GetTransportChargesDetails.get("TRANSPORT_INVOICE_ID") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_INVOICE_ID").toString());
					//objGetTransportChargesDetails.setIndentEntryInvoiceId(GetTransportChargesDetails.get("INDENT_ENTRY_INVOICE_ID") == null ? "" : GetTransportChargesDetails.get("INDENT_ENTRY_INVOICE_ID").toString());
					//objGetTransportChargesDetails.setDateAndTime(GetTransportChargesDetails.get("DATE_AND_TIME") == null ? "": GetTransportChargesDetails.get("DATE_AND_TIME").toString());
					objGetTransportChargesDetails.setConveyanceAmount1(GetTransportChargesDetails.get("TRANSPORT_AMOUNT") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_AMOUNT").toString());


					GetTransportChargesListDetails.add(objGetTransportChargesDetails);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return GetTransportChargesListDetails;
	}

	//pavan code added
	public void updateInvoiceIndentEntryDetails(GetInvoiceDetailsBean irdto) {


		//ravi written code
		
		String query1="update INDENT_ENTRY_DETAILS set  RECEVED_QTY = ?,HSN_CODE=? where  INDENT_ENTRY_DETAILS_ID = ? "; 
		jdbcTemplate.update(query1, new Object[] {irdto.getQuantity(),irdto.getHsnCode(),irdto.getIndentEntryDetailsId()});
		// price details moved to price list becase of that writing 2 time
		
		

		String query = "update SUMADHURA_PRICE_LIST set  OTHER_CHARGES = ? ,TAX_ON_OTHER_TRANSPORT_CHG =? ,OTHER_CHARGES_AFTER_TAX = ? ,TOTAL_AMOUNT = ?,HSN_CODE=? where  INDENT_ENTRY_DETAILS_ID = ?";
		jdbcTemplate.update(query, new Object[] {irdto.getOtherOrTransportCharges(), irdto.getTaxOnOtherOrTransportCharges(),
				irdto.getOtherOrTransportChargesAfterTax(),irdto.getTotalAmount(),irdto.getHsnCode(),irdto.getIndentEntryDetailsId()});


		Double TotalAmount = Double.valueOf((irdto.getTotalAmount() == null ? "0" : irdto.getTotalAmount().toString()));
		Double Quantity = Double.valueOf((irdto.getQuantity() == null ? "0" : irdto.getQuantity().toString()));
		Double pricePerUnit = 0.0;

		pricePerUnit = TotalAmount/Quantity;


		query = " update SUMADHURA_PRICE_LIST set AMOUNT_PER_UNIT_AFTER_TAXES = ?, UPDATED_DATE = sysdate where INVOICE_NUMBER = ? and INDENT_ENTRY_DETAILS_ID = ?";
		jdbcTemplate.update(query, new Object[] {pricePerUnit, irdto.getInvoiceNumber(),irdto.getIndentEntryDetailsId()});


	}

	//pavan code added
	public void updateInvoiceIndentEntry(GetInvoiceDetailsBean irdto,String vendorId) {


		String query = "update INDENT_ENTRY set TOTAL_AMOUNT = ?, NOTE=? where INVOICE_ID = ? and VENDOR_ID=? ";
		jdbcTemplate.update(query, new Object[] {irdto.getTotalAmount(),irdto.getNote(), irdto.getInvoiceNumber(),vendorId});



	}


	//pavan code added
	public void updateInvoiceOtherCharges(GetInvoiceDetailsBean irdto,String strSiteId,String strindentEntryId) {



		String query = "insert into SUMADHURA_TRNS_OTHR_CHRGS_DTLS(ID,TRANSPORT_ID,TRANSPORT_AMOUNT,TRANSPORT_GST_PERCENTAGE," +
		"TOTAL_AMOUNT_AFTER_GST_TAX,TRANSPORT_INVOICE_ID,INDENT_ENTRY_INVOICE_ID," +
		"DATE_AND_TIME,SITE_ID,INDENT_ENTRY_ID,TRANSPORT_GST_AMOUNT) " +
		" values(TRANS_SEQ_ID.nextval,?,?,?,?,?,?,sysdate,?,?,?)";
		jdbcTemplate.update(query, new Object[] { irdto.getConveyance1(),
				irdto.getConveyanceAmount1(),irdto.getGSTTax1(),
				irdto.getAmountAfterTax1(),irdto.getTransportInvoice1(),irdto.getInvoiceNumber(),strSiteId,strindentEntryId,
				irdto.getGSTAmount1()});

	}




	@Override
    public String getTransportChargesListForGRN(String invoiceNumber,String strSiteId,String state,String strVendorId,String invoiceDate,String indentEntryId) {
          List<Map<String, Object>> productList = null;

          List<GetInvoiceDetailsBean> GetTransportChargesListDetails = new ArrayList<GetInvoiceDetailsBean>();
          GetInvoiceDetailsBean objGetTransportChargesDetails=null;
          JdbcTemplate template = null;
          String sql = "";
          String tblCharges = "";
          String tblChargeName = "";
          String strConveyanceAmountChrg = "";
          double chargeCGST = 0.0;
          double chargeCGSTAMT = 0.0;
          double chargeSGST = 0.0;
          double chargeSGSTAMT = 0.0;
          String strAmountAfterTaxChrg =   "";
          double chargeIGST = 0.0;
          double chargeIGSTAMT = 0.0;
          String strGSTPercentage = "";
          String strGSTAmount = "";
          double conveCharges=0.0;
          try {

                template = new JdbcTemplate(DBConnection.getDbConnection());

                if (StringUtils.isNotBlank(invoiceNumber) ) {
                      if(strVendorId.equals("")){
                            sql += "select TRANSPORT_GST_AMOUNT,TAX_PERCENTAGE, "+
                            "TRANSPORT_GST_AMOUNT,TRANSPORT_AMOUNT,CHARGE_NAME,TOTAL_AMOUNT_AFTER_GST_TAX from SUMADHURA_TRNS_OTHR_CHRGS_DTLS STCD, "+
                            "SUMADHURA_TRNS_OTHR_CHRGS_MST STCM , INDENT_GST IG where STCM.CHARGE_ID = STCD.TRANSPORT_ID and IG.TAX_ID = STCD.TRANSPORT_GST_PERCENTAGE and INDENT_ENTRY_INVOICE_ID = ? and SITE_ID = ? and INDENT_ENTRY_ID=? ";
                            productList = template.queryForList(sql, new Object[] {invoiceNumber, strSiteId,indentEntryId});
                      }
                      else{
                            sql = "select IG.TAX_PERCENTAGE, "
                                  + "STCD.TRANSPORT_GST_AMOUNT,STCD.TRANSPORT_AMOUNT,STCM.CHARGE_NAME,STCD.TOTAL_AMOUNT_AFTER_GST_TAX "
                                  + "  from SUMADHURA_TRNS_OTHR_CHRGS_DTLS STCD,  INDENT_ENTRY IE ,"
                                  + "   SUMADHURA_TRNS_OTHR_CHRGS_MST STCM , INDENT_GST IG where STCM.CHARGE_ID = STCD.TRANSPORT_ID and IG.TAX_ID = STCD.TRANSPORT_GST_PERCENTAGE and STCD.INDENT_ENTRY_INVOICE_ID = ? and STCD.SITE_ID = ? " 
                                  + " and IE.VENDOR_ID = ? and IE.INDENT_ENTRY_ID = STCD.INDENT_ENTRY_ID "
                                  + " AND trunc(IE.INVOICE_DATE,'yy') = trunc(TO_DATE( ?, 'dd-MM-yy'),'yy')";
                            productList = template.queryForList(sql, new Object[] {invoiceNumber, strSiteId, strVendorId, invoiceDate});
                      }

                } 
                if (null != productList && productList.size() > 0) {

                      Map<String,String> transportChargesSortMap = new HashMap<String,String>();
                      String strTypeOfTransportAndPercentageKey = "";

                      double gstAmount = 0.0;
                      double gstAmountTax = 0.0;
                      int i = 1;
                      Set <String> transportCharges = new HashSet <String>();
                      //Set transportCharges = HashSet(); 
                      for (Map<?, ?> GetTransportChargesDetails : productList) {

                            tblChargeName = GetTransportChargesDetails.get("CHARGE_NAME") == null ? "" : GetTransportChargesDetails.get("CHARGE_NAME").toString();
                            strGSTPercentage = GetTransportChargesDetails.get("TAX_PERCENTAGE") == null ? "" : GetTransportChargesDetails.get("TAX_PERCENTAGE").toString();
                            strGSTAmount = GetTransportChargesDetails.get("TRANSPORT_GST_AMOUNT") == null ? "0" : GetTransportChargesDetails.get("TRANSPORT_GST_AMOUNT").toString();
                            strAmountAfterTaxChrg = GetTransportChargesDetails.get("TOTAL_AMOUNT_AFTER_GST_TAX") == null ? "" : GetTransportChargesDetails.get("TOTAL_AMOUNT_AFTER_GST_TAX").toString();
                            strConveyanceAmountChrg = GetTransportChargesDetails.get("TRANSPORT_AMOUNT") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_AMOUNT").toString();
                            
                            strTypeOfTransportAndPercentageKey = tblChargeName+"&&&"+strGSTPercentage;
                            transportCharges.add(strTypeOfTransportAndPercentageKey); //removing dublicate keys

                            if(!transportChargesSortMap.containsKey(strTypeOfTransportAndPercentageKey)){   // if key not present in map.(First time)

                            	  transportChargesSortMap.put(strTypeOfTransportAndPercentageKey, "");
                                  transportChargesSortMap.put(strTypeOfTransportAndPercentageKey+"&&&ChargeName", tblChargeName);
                                  transportChargesSortMap.put(strTypeOfTransportAndPercentageKey+"&&&ChargePercentage", strGSTPercentage);
                                  transportChargesSortMap.put(strTypeOfTransportAndPercentageKey+"&&&GSTAmount", strGSTAmount);
                                  transportChargesSortMap.put(strTypeOfTransportAndPercentageKey+"&&&AmountAfterTaxChrg", strAmountAfterTaxChrg);
                                  transportChargesSortMap.put(strTypeOfTransportAndPercentageKey+"&&&strConveyanceAmountChrg", strConveyanceAmountChrg);
                            
                            }else{
                            	//from second time key will available here



                                  double DoublePrevGSTAmount = Double.valueOf(transportChargesSortMap.get(strTypeOfTransportAndPercentageKey+"&&&GSTAmount"));
                                  gstAmount = Double.valueOf(strGSTAmount)+DoublePrevGSTAmount;
                                  transportChargesSortMap.put(strTypeOfTransportAndPercentageKey+"&&&GSTAmount", String.valueOf(gstAmount));

                                  double DoublePrevGSTAmountAfterTAx = Double.valueOf(transportChargesSortMap.get(strTypeOfTransportAndPercentageKey+"&&&AmountAfterTaxChrg"));
                                  gstAmountTax = Double.valueOf(strAmountAfterTaxChrg)+DoublePrevGSTAmountAfterTAx;
                                  transportChargesSortMap.put(strTypeOfTransportAndPercentageKey+"&&&AmountAfterTaxChrg", String.valueOf(gstAmountTax));
                                 
                                  double DoublePrevConvAmountChar = Double.valueOf(transportChargesSortMap.get(strTypeOfTransportAndPercentageKey+"&&&strConveyanceAmountChrg"));
                                  conveCharges = Double.valueOf(strConveyanceAmountChrg)+DoublePrevConvAmountChar;
                                  transportChargesSortMap.put(strTypeOfTransportAndPercentageKey+"&&&strConveyanceAmountChrg", String.valueOf(conveCharges));



                            }
                      }

                            java.util.Iterator<String> iterator = transportCharges.iterator(); 

                            // based on set(it contain unique key) iterating the map
                            while (iterator.hasNext()) {
                                  String strKey = (String) iterator.next();
                                  tblChargeName = transportChargesSortMap.get(strKey+"&&&ChargeName");
                                  strAmountAfterTaxChrg = transportChargesSortMap.get(strKey+"&&&AmountAfterTaxChrg");
                                  strConveyanceAmountChrg = transportChargesSortMap.get(strKey+"&&&strConveyanceAmountChrg");
                                  strConveyanceAmountChrg=String.format("%.2f",Double.valueOf(strConveyanceAmountChrg));
                                  strAmountAfterTaxChrg=String.format("%.2f",Double.valueOf(strAmountAfterTaxChrg));
                                  
                                  strGSTPercentage = transportChargesSortMap.get(strKey+"&&&ChargePercentage");
                                  strGSTAmount = transportChargesSortMap.get(strKey+"&&&GSTAmount");
                                  strGSTAmount=String.format("%.2f",Double.valueOf(strGSTAmount));
                                  
                                  if(strGSTPercentage.contains("%")){
                                      strGSTPercentage = strGSTPercentage.replace("%", "");
                                }
                                  
                                  if (state.equals("1") || state.equals("Local")) {
                                        
                                        
                                        
                                        chargeCGST = Double.valueOf(strGSTPercentage)/2;
                                        chargeSGST = Double.valueOf(strGSTPercentage)/2;
                                        chargeSGSTAMT =  Double.valueOf(transportChargesSortMap.get(strKey+"&&&GSTAmount"))/2;
                                        chargeCGSTAMT = Double.valueOf(transportChargesSortMap.get(strKey+"&&&GSTAmount"))/2; 
                                        chargeCGST=Double.parseDouble(new DecimalFormat("##.##").format((chargeCGST)));
                                        chargeSGST=Double.parseDouble(new DecimalFormat("##.##").format((chargeSGST)));
                                        chargeSGSTAMT=Double.parseDouble(new DecimalFormat("##.##").format((chargeSGSTAMT)));
                                        chargeCGSTAMT=Double.parseDouble(new DecimalFormat("##.##").format((chargeCGSTAMT)));
                                        
                                        tblCharges+= tblChargeName+"@@"+strConveyanceAmountChrg+"@@"+chargeCGST+"@@"+chargeCGSTAMT+"@@"+chargeSGST+"@@"+chargeSGSTAMT+"@@"+""+"@@"+""+"@@"+strAmountAfterTaxChrg+"@@"+"-"+"&&";
                                  }else{
                                        tblCharges+= tblChargeName+"@@"+strConveyanceAmountChrg+"@@"+""+"@@"+""+"@@"+""+"@@"+""+"@@"+strGSTPercentage+"@@"+strGSTAmount+"@@"+strAmountAfterTaxChrg+"@@"+"-"+"&&";
                                  }
                            }                             

                      //}





                      //old code

                      /*for (Map<?, ?> GetTransportChargesDetails : productList) {

                            objGetTransportChargesDetails = new GetInvoiceDetailsBean();

                            tblChargeName = GetTransportChargesDetails.get("CHARGE_NAME") == null ? "" : GetTransportChargesDetails.get("CHARGE_NAME").toString();
                            strGSTPercentage = GetTransportChargesDetails.get("TAX_PERCENTAGE") == null ? "" : GetTransportChargesDetails.get("TAX_PERCENTAGE").toString();
                            strGSTAmount = GetTransportChargesDetails.get("TRANSPORT_GST_AMOUNT") == null ? "0" : GetTransportChargesDetails.get("TRANSPORT_GST_AMOUNT").toString();
                            strAmountAfterTaxChrg = GetTransportChargesDetails.get("TOTAL_AMOUNT_AFTER_GST_TAX") == null ? "" : GetTransportChargesDetails.get("TOTAL_AMOUNT_AFTER_GST_TAX").toString();
                            objGetTransportChargesDetails.setTransportInvoice1(GetTransportChargesDetails.get("TRANSPORT_INVOICE_ID") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_INVOICE_ID").toString());
                            //objGetTransportChargesDetails.setIndentEntryInvoiceId(GetTransportChargesDetails.get("INDENT_ENTRY_INVOICE_ID") == null ? "" : GetTransportChargesDetails.get("INDENT_ENTRY_INVOICE_ID").toString());
                            //objGetTransportChargesDetails.setDateAndTime(GetTransportChargesDetails.get("DATE_AND_TIME") == null ? "": GetTransportChargesDetails.get("DATE_AND_TIME").toString());
                            strConveyanceAmountChrg = GetTransportChargesDetails.get("TRANSPORT_AMOUNT") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_AMOUNT").toString();

                            if(strGSTPercentage.contains("%")){
                                  strGSTPercentage = strGSTPercentage.replace("%", "");
                            }


                            if (state.equals("1") || state.equals("Local")) {

                                  chargeCGST = Double.valueOf(strGSTPercentage)/2; 
                                  chargeSGST = Double.valueOf(strGSTPercentage)/2;
                                  chargeCGSTAMT = Double.valueOf(strGSTAmount)/2;
                                  chargeSGSTAMT =  Double.valueOf(strGSTAmount)/2;

                                  tblCharges+= tblChargeName+"@@"+strConveyanceAmountChrg+"@@"+chargeCGST+"@@"+chargeCGSTAMT+"@@"+chargeSGST+"@@"+chargeSGSTAMT+"@@"+""+"@@"+""+"@@"+strAmountAfterTaxChrg+"@@"+"-"+"&&";
                            } else {
                                  tblCharges+= tblChargeName+"@@"+strConveyanceAmountChrg+"@@"+""+"@@"+""+"@@"+""+"@@"+""+"@@"+strGSTPercentage+"@@"+strGSTAmount+"@@"+strAmountAfterTaxChrg+"@@"+"-"+"&&";
                            }
                      }*/
                }
          }
          catch (Exception e) {
                e.printStackTrace();
          }
          return tblCharges;
    }

	@Override
	public String getVendorNameAndIndentId(String invoiceNumber, String siteId, String vendorName) {
		List<Map<String, Object>> productList = null;
		String vendorNameAndIndentId = "";
		String query = "SELECT IE.VENDOR_ID,IE.INDENT_ENTRY_ID,VD.VENDOR_NAME FROM INDENT_ENTRY IE,VENDOR_DETAILS VD where IE.INVOICE_ID = ? AND VD.VENDOR_NAME = ? AND IE.SITE_ID = ? AND IE.VENDOR_ID = VD.VENDOR_ID";
		productList = jdbcTemplate.queryForList(query, new Object[] { invoiceNumber, vendorName, siteId });
		if (null != productList && productList.size() > 0) {
			for (Map<?, ?> GetTransportChargesDetails : productList) {

				vendorNameAndIndentId+=GetTransportChargesDetails.get("VENDOR_NAME") == null ? "" : GetTransportChargesDetails.get("VENDOR_NAME").toString();
				vendorNameAndIndentId+="@@";
				vendorNameAndIndentId+=GetTransportChargesDetails.get("INDENT_ENTRY_ID") == null ? "" : GetTransportChargesDetails.get("INDENT_ENTRY_ID").toString();
			}
		}
		return vendorNameAndIndentId;
	}

	@Override
	public List<ViewIndentIssueDetailsBean> getGrnViewDetails(String fromDate, String toDate, String siteId) {

		String query = "";
		String strDCFormQuery = "";
		String strDCNumber = "";
		JdbcTemplate template = null;
		List<Map<String, Object>> dbIndentDts = null;
		List<ViewIndentIssueDetailsBean> list = new ArrayList<ViewIndentIssueDetailsBean>();
		ViewIndentIssueDetailsBean indentObj = null; 

		try {
			//if part is for view indent receive details,else part is for view indent issue details
			template = new JdbcTemplate(DBConnection.getDbConnection());

			if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
				query = "SELECT  IE.INVOICE_ID,IE.SITE_ID,IE.INDENT_ENTRY_ID, min(IE.RECEIVED_OR_ISSUED_DATE) as RECEVED_DATE,min(IE.INVOICE_DATE) as INVOICE_DATE, min(VD.VENDOR_NAME) as VENDOR_NAME,min(VD.VENDOR_ID) as VENDOR_ID FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED,  VENDOR_DETAILS VD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID  AND IE.INDENT_TYPE='IN' AND IE.SITE_ID='"+siteId+"'  AND IE.VENDOR_ID=VD.VENDOR_ID AND TRUNC(IE.RECEIVED_OR_ISSUED_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy') group by IE.INVOICE_ID,IE.SITE_ID,VD.VENDOR_ID,IE.INDENT_ENTRY_ID,TRUNC( IE.INVOICE_DATE,'yy')";
				//query = "SELECT LD.USERNAME, IE.REQUESTER_NAME, IE.REQUESTER_ID, IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.ISSUED_QTY FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND IE.INDENT_TYPE='OUT' AND IE.SITE_ID='"+siteId+"' AND LD.UNAME=IE.USER_ID AND IE.ENTRY_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
			} else if (StringUtils.isNotBlank(fromDate)) {
				query = "SELECT  IE.INVOICE_ID,IE.SITE_ID,IE.INDENT_ENTRY_ID, min(IE.RECEIVED_OR_ISSUED_DATE) as RECEVED_DATE,min(IE.INVOICE_DATE) as INVOICE_DATE, min(VD.VENDOR_NAME) as VENDOR_NAME,min(VD.VENDOR_ID) as VENDOR_ID FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, VENDOR_DETAILS VD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID  AND IE.INDENT_TYPE='IN' AND IE.SITE_ID='"+siteId+"' AND IE.VENDOR_ID=VD.VENDOR_ID AND TRUNC(IE.RECEIVED_OR_ISSUED_DATE) >=TO_DATE('"+fromDate+"', 'dd-MM-yy') group by IE.INVOICE_ID,IE.SITE_ID,VD.VENDOR_ID,IE.INDENT_ENTRY_ID,TRUNC( IE.INVOICE_DATE,'yy')";
			} else if(StringUtils.isNotBlank(toDate)) {
				query = "SELECT  IE.INVOICE_ID,IE.SITE_ID,IE.INDENT_ENTRY_ID, min(IE.RECEIVED_OR_ISSUED_DATE) as RECEVED_DATE,min(IE.INVOICE_DATE) as INVOICE_DATE, min(VD.VENDOR_NAME) as VENDOR_NAME,min(VD.VENDOR_ID) as VENDOR_ID FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, VENDOR_DETAILS VD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID  AND IE.INDENT_TYPE='IN' AND IE.SITE_ID='"+siteId+"' AND IE.VENDOR_ID=VD.VENDOR_ID AND TRUNC(IE.RECEIVED_OR_ISSUED_DATE) <=TO_DATE('"+toDate+"', 'dd-MM-yy') group by IE.INVOICE_ID,IE.SITE_ID,VD.VENDOR_ID,IE.INDENT_ENTRY_ID,TRUNC( IE.INVOICE_DATE,'yy')";
			}

			/*LOGIN_DUMMY LD, removed from above queries*/




			dbIndentDts = template.queryForList(query, new Object[]{});

			for(Map<String, Object> prods : dbIndentDts) {
				indentObj = new ViewIndentIssueDetailsBean();

				indentObj.setRequesterId(prods.get("INVOICE_ID")==null ? "" : prods.get("INVOICE_ID").toString());
				//indentObj.setStrInvoiceDate(prods.get("INVOICE_DATE")==null ? "" : prods.get("INVOICE_DATE").toString());
				indentObj.setVendorName(prods.get("VENDOR_NAME")==null ? "" : prods.get("VENDOR_NAME").toString());
				indentObj.setVendorId(prods.get("VENDOR_ID")==null ? "" : prods.get("VENDOR_ID").toString());
				indentObj.setIndentEntryId(prods.get("INDENT_ENTRY_ID")==null ? "" : prods.get("INDENT_ENTRY_ID").toString());
				indentObj.setSiteId(prods.get("SITE_ID")==null ? "" : prods.get("SITE_ID").toString());
				


				String date=prods.get("INVOICE_DATE")==null ? "" : prods.get("INVOICE_DATE").toString();
				//System.out.println("before: "+date);
				if (StringUtils.isNotBlank(date)) {
					date = DateUtil.dateConversion(date);
				} else {
					date = "";
				}
				indentObj.setReceivedDate(date);
				indentObj.setStrInvoiceDate(date);
				list.add(indentObj);
			}





		} catch (Exception ex) {
			ex.printStackTrace();
			//log.debug("Exception = "+ex.getMessage());
			//.info("Exception Occured Inside getViewGrnDetails() in IndentIssueDao class --"+ex.getMessage());
		} finally {
			query = "";
			indentObj = null; 
			template = null;
			dbIndentDts = null;
		}
		return list;
	}

	@Override
	public String getGrnNumberForGRN(String indentEntry_Id, String site_id,String vendorId) {
		String query = "SELECT GRN_NO FROM INDENT_ENTRY where INDENT_ENTRY_ID = '"+indentEntry_Id+"' AND SITE_ID = '"+site_id+"' and VENDOR_ID='"+vendorId+"'";
		String result = jdbcTemplate.queryForObject(query,String.class);  
		return result;
	}

	@Override
	public String getVendorNameAndIndentEntryId(String invoiceNumber,String vendorName1,String invoiceDate) {

		List<Map<String, Object>> productList = null;
		JdbcTemplate template = null;
		String sql = "";
		String response="";

		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());

			if (StringUtils.isNotBlank(invoiceNumber) ) {
				//sql = "SELECT distinct IE.STATE,IE.TOTAL_AMOUNT,IE.NOTE, IE.RECEVED_DATE,IED.TRANSPORTERNAME, IED.VEHICLENO,IED.EWAYBILLNO, IED.PO_NUMBER,IED.PODATE,VD.VENDOR_NAME,VD.ADDRESS,VD.GSIN_NUMBER,IE.INVOICE_ID, IE.INVOICE_DATE FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, VENDOR_DETAILS VD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND IE.VENDOR_ID=VD.VENDOR_ID AND IE.INVOICE_ID=? AND IE.SITE_ID=? AND IE.INDENT_TYPE='IN'";
				sql = "select IE.INDENT_ENTRY_ID,VD.VENDOR_NAME from INDENT_ENTRY IE,VENDOR_DETAILS VD "
					+ " WHERE IE.INVOICE_ID=? AND IE.VENDOR_ID=VD.VENDOR_ID and lower(VD.VENDOR_NAME)=lower(?) AND trunc(IE.INVOICE_DATE) = trunc(TO_DATE(? ,'dd-MM-yy'))";
				productList = template.queryForList(sql, new Object[] {invoiceNumber,vendorName1,invoiceDate});
			} 
			if (null != productList && productList.size() > 0) {

				//int i = 1;
				for (Map<?, ?> GetInvoiceDetailsInwardBean : productList) {
				String indent_Entry_Id=GetInvoiceDetailsInwardBean.get("INDENT_ENTRY_ID") == null ? "" : GetInvoiceDetailsInwardBean.get("INDENT_ENTRY_ID").toString();	
				String vendor_Name=GetInvoiceDetailsInwardBean.get("VENDOR_NAME") == null ? "" : GetInvoiceDetailsInwardBean.get("VENDOR_NAME").toString();		
				response=indent_Entry_Id+"|"+vendor_Name;
				}
				}
		
	}catch(Exception e){
		e.printStackTrace();
	}
		
		return response;

}
		@Override
	public List<String> loadAllVendorNames(String vendorName) {
		System.out.println(vendorName + "in DAO");
		String selectAllVendorsName = "SELECT VENDOR_NAME FROM VENDOR_DETAILS WHERE  STATUS = 'A' and lower(VENDOR_NAME) like lower('"
				+ vendorName + "%') and rownum <10 ORDER BY VENDOR_NAME ASC";
		List<String> childProdData = jdbcTemplate.query(selectAllVendorsName, new ResultSetExtractor<List<String>>() {

			@Override
			public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<String> list = new ArrayList<String>();
				while (rs.next()) {
					list.add(rs.getString(1));
				}
				return list;
			}
		});

		return childProdData;
	}
	
/*	=============================================================upadte invoice=====================================================*/	
		
		public int updateIndentIndentAvailability(GetInvoiceDetailsBean irdto,double strQuantity) {

			List<Map<String, Object>> dbProductDetailsList = null;
			int result = 0;
			String availability_Id="";
			double receive_Form_Quantity=0.0;
			String quantity="";
			double updateQuantity=0.0;
			double tempQuantity=0.0;
			
			String query="SELECT INDENT_AVAILABILITY_ID FROM SUMADHURA_PRICE_LIST where INDENT_ENTRY_DETAILS_ID ='"+irdto.getIndentEntryDetailsId()+"'";
			
			 availability_Id = jdbcTemplate.queryForObject(query,String.class);  
			 
			 String query1="SELECT PRODUT_QTY FROM INDENT_AVAILABILITY WHERE  INDENT_AVAILABILITY_ID ='"+availability_Id+"' ";
				
			 quantity = jdbcTemplate.queryForObject(query1,String.class);
			
			
			 receive_Form_Quantity=Double.parseDouble(irdto.getQuantity());
			 tempQuantity=receive_Form_Quantity-strQuantity;
			 updateQuantity=Double.parseDouble(quantity)+tempQuantity;
			 if(tempQuantity>0){
				 
					String updateIndentAvalibilityQry = "UPDATE INDENT_AVAILABILITY SET PRODUT_QTY ='"+updateQuantity+"' WHERE INDENT_AVAILABILITY_ID ='"+availability_Id+"'";
					jdbcTemplate.update(updateIndentAvalibilityQry, new Object[] {});
				 
			 }else if(tempQuantity<0){
				 
				 String updateIndentAvalibilityQry = "UPDATE INDENT_AVAILABILITY SET PRODUT_QTY ='"+updateQuantity+"' WHERE INDENT_AVAILABILITY_ID ='"+availability_Id+"'";
					jdbcTemplate.update(updateIndentAvalibilityQry, new Object[] {});

				 	 }
			 
			
			
			return result;
		}
	
		
		public int updatePoentryDetailsQuantity(GetInvoiceDetailsBean irdto,String poNo,String strQuantity) {

			List<Map<String, Object>> dbProductDetailsList = null;
			int result = 0;
			String availability_Id="";
			double receive_Form_Quantity=0.0;
			String po_Entry_Id="";
			double Quantity=0.0;
			double tempQuantity=0.0;
			String child_Product_id="";
			String po_Entry_Details_Id="";
			String indent_Creation_Id="";
			String marketing_dept_id = validateParams.getProperty("MARKETING_DEPT_ID") == null ? "" : validateParams.getProperty("MARKETING_DEPT_ID").toString();
			String indent_Creation_Details_Id="";
			String query="SELECT CHILD_PRODUCT_ID FROM INDENT_ENTRY_DETAILS where INDENT_ENTRY_DETAILS_ID ='"+irdto.getIndentEntryDetailsId()+"'";
			
			child_Product_id = jdbcTemplate.queryForObject(query,String.class);  
			 
			 String query1="SELECT SPE.PO_ENTRY_ID,SPED.PO_QTY,SPED.PO_ENTRY_DETAILS_ID,SPE.INDENT_NO,SPED.INDENT_CREATION_DTLS_ID FROM SUMADHURA_PO_ENTRY SPE,SUMADHURA_PO_ENTRY_DETAILS SPED WHERE  SPE.PO_NUMBER ='"+poNo+"'  AND SPED.CHILD_PRODUCT_ID='"+child_Product_id+"'"
			 		+ "AND SPE.PO_ENTRY_ID=SPED.PO_ENTRY_ID";
				
			 dbProductDetailsList = jdbcTemplate.queryForList(query1, new Object[] {	});
			 if(dbProductDetailsList.size()>0){
			
			 if (null != dbProductDetailsList && dbProductDetailsList.size() > 0) {
					for (Map<String, Object> prods : dbProductDetailsList) {
						Quantity = Double.parseDouble(prods.get("PO_QTY") == null ? "0" : prods.get("PO_QTY").toString());
						//+po_Entry_Id = (prods.get("PO_ENTRY_ID") == null ? "" : prods.get("PO_ENTRY_ID").toString());
						 po_Entry_Details_Id=(prods.get("PO_ENTRY_DETAILS_ID") == null ? "" : prods.get("PO_ENTRY_DETAILS_ID").toString());
						 indent_Creation_Id=(prods.get("INDENT_NO") == null ? "" : prods.get("INDENT_NO").toString());
						 indent_Creation_Details_Id=(prods.get("INDENT_CREATION_DTLS_ID") == null ? "" : prods.get("INDENT_CREATION_DTLS_ID").toString());
						 
						/* String sql="SELECT INDENT_CREATION_DETAILS_ID FROM SUMADHURA_INDENT_CREATION_DTLS where INDENT_CREATION_ID ='"+indent_Creation_Id+"' and CHILD_PRODUCT_ID='"+child_Product_id+"'";
						 indent_Creation_Details_Id= jdbcTemplate.queryForObject(sql,String.class);*/
					}
				}
				
			
			 }
			
			 receive_Form_Quantity=Double.parseDouble(irdto.getQuantity());
			 double strtempQuantity=receive_Form_Quantity-Double.parseDouble(strQuantity);
			 
			 if(strtempQuantity!=0 && !irdto.getSiteId().equals(marketing_dept_id)){
				 
				 String IndentAvalibilityQry = "UPDATE SUMADHURA_INDENT_CREATION_DTLS SET RECEIVE_QUANTITY =RECEIVE_QUANTITY+? WHERE INDENT_CREATION_DETAILS_ID ='"+indent_Creation_Details_Id+"'";
					jdbcTemplate.update(IndentAvalibilityQry, new Object[] {strtempQuantity});
					
					String query2 = "update  SUM_PURCHASE_DEPT_INDENT_PROSS set ALLOCATED_QUANTITY = ALLOCATED_QUANTITY+? "
							+ ", PENDING_QUANTIY = PENDING_QUANTIY-? , PO_INTIATED_QUANTITY = PO_INTIATED_QUANTITY-? "
						+ " where INDENT_CREATION_DETAILS_ID = ? ";
					jdbcTemplate.update(query2, new Object[] {strtempQuantity,strtempQuantity,strtempQuantity,indent_Creation_Details_Id});
				 tempQuantity=(Quantity)+strtempQuantity; 
			 }
			 
			 if(dbProductDetailsList.size()>0 && !irdto.getSiteId().equals(marketing_dept_id)){
			 ird.setIndentInactiveAfterChecking(indent_Creation_Id);
			 }
			 /*if(tempQuantity>0){
				 
					String updateIndentAvalibilityQry = "UPDATE SUMADHURA_PO_ENTRY_DETAILS SET PO_QTY ='"+tempQuantity+"' WHERE PO_ENTRY_DETAILS_ID ='"+po_Entry_Details_Id+"'";
					jdbcTemplate.update(updateIndentAvalibilityQry, new Object[] {});
				 
			 } */if(tempQuantity<0){
				 
				 String updateIndentAvalibilityQry = "UPDATE SUMADHURA_PO_ENTRY_DETAILS SET PO_QTY ='"+tempQuantity+"' WHERE PO_ENTRY_DETAILS_ID ='"+po_Entry_Details_Id+"'";
					jdbcTemplate.update(updateIndentAvalibilityQry, new Object[] {});

					String status = "UPDATE SUMADHURA_PO_ENTRY SET PO_STATUS ='A' WHERE PO_NUMBER ='"+poNo+"'";
					jdbcTemplate.update(status, new Object[] {});
					if(!irdto.getSiteId().equals(marketing_dept_id)){
					String statusActive = "update SUMADHURA_INDENT_CREATION set STATUS = 'A',PENDIND_DEPT_ID = 'VND'  where INDENT_CREATION_ID = ? ";
					jdbcTemplate.update(statusActive, new Object[] {indent_Creation_Id});
					} 
				 	 }
			 
			
			
			return result;
		}
		
		
		
		public int inActivePOentrytbl(String indent_Entry_Id,String poNumber) {

			List<Map<String, Object>> dbProductDetailsList = null;
			List<Map<String, Object>> potblProductDetailsList = null;
			int result = 0;
			//String availability_Id="";
			//double receive_Form_Quantity=0.0;
			String po_Entry_Id="";
			double indent_Quantity=0.0;
			double tempQuantity=0.0;
			String indent_child_Product_id="";
			//String po_Entry_Details_Id="";
			//String po_Child_Prod_id="";
			String po_Quantity="";
			String no_Of_Records="";
			int no_Of_Times=0;
			
			int count1 = jdbcTemplate.queryForInt("select count(*) from SUMADHURA_PO_ENTRY where PO_NUMBER ='"+poNumber+"'");
			//int count= jdbcTemplate.queryForInt(count,String.class); 
			if(count1>0){
			String sql = "SELECT PO_ENTRY_ID FROM SUMADHURA_PO_ENTRY where PO_NUMBER ='"+poNumber+"'";
			 po_Entry_Id = jdbcTemplate.queryForObject(sql,String.class);  
			
			
			String query="SELECT CHILD_PRODUCT_ID,RECEVED_QTY FROM INDENT_ENTRY_DETAILS where INDENT_ENTRY_ID ='"+indent_Entry_Id+"'";
			 dbProductDetailsList = jdbcTemplate.queryForList(query, new Object[] {	});
			 
			 /*String count="SELECT count(*) FROM INDENT_ENTRY_DETAILS where INDENT_ENTRY_ID ='"+indent_Entry_Id+"'";
			 no_Of_Records = jdbcTemplate.queryForObject(count,String.class); */
			 String count="SELECT count(*) FROM SUMADHURA_PO_ENTRY_DETAILS where PO_ENTRY_ID ='"+po_Entry_Id+"'";
			 no_Of_Records = jdbcTemplate.queryForObject(count,String.class); 
			
				if (null != dbProductDetailsList && dbProductDetailsList.size() > 0) {
					for (Map<String, Object> prods : dbProductDetailsList) {
						indent_child_Product_id = (prods.get("CHILD_PRODUCT_ID") == null ? "0" : prods.get("CHILD_PRODUCT_ID").toString());
						
						indent_Quantity=Double.parseDouble(prods.get("RECEVED_QTY") == null ? "" : prods.get("RECEVED_QTY").toString());
					
						//if we have same child product two times
					String query1="SELECT sum(PO_QTY) FROM SUMADHURA_PO_ENTRY_DETAILS where PO_ENTRY_ID ='"+po_Entry_Id+"' and CHILD_PRODUCT_ID='"+indent_child_Product_id+"'";
					po_Quantity = jdbcTemplate.queryForObject(query1,String.class); 
					if((Double.parseDouble(po_Quantity))>indent_Quantity){
						
						break;
					}else{
						no_Of_Times++;
					}
					
					}
					
					if(no_Of_Times==Integer.parseInt(no_Of_Records)){
						
							String query2 = "update SUMADHURA_PO_ENTRY set PO_STATUS ='I' where PO_NUMBER='"+poNumber+"'";
							jdbcTemplate.update(query2, new Object[] {});
						

					}
				}
			}
				
			/* String query1="SELECT CHILD_PRODUCT_ID,PO_QTY FROM SUMADHURA_PO_ENTRY_DETAILS where PO_ENTRY_ID ='"+po_Entry_Id+"'";
			 potblProductDetailsList = jdbcTemplate.queryForList(query1, new Object[] {	});
			 if (null != potblProductDetailsList && potblProductDetailsList.size() > 0) {
					for (Map<String, Object> prods : potblProductDetailsList) {
						po_Child_Prod_id = (prods.get("CHILD_PRODUCT_ID") == null ? "0" : prods.get("CHILD_PRODUCT_ID").toString());
						
						po_Quantity=(prods.get("PO_QTY") == null ? "" : prods.get("PO_QTY").toString());
					}
				}*/
		
		return result;
		}
		
		// ravi written to update edit tranport details in updateInvoice
		
	
		public int updateInvoiceOtherChargesEdited(GetInvoiceDetailsBean objGetInvoiceDetailsBean,String id){

			String query =  "update SUMADHURA_TRNS_OTHR_CHRGS_DTLS set TRANSPORT_ID = ? ,TRANSPORT_AMOUNT = ? ,"
				+ " TRANSPORT_GST_PERCENTAGE = ? ,TRANSPORT_GST_AMOUNT = ? ,TOTAL_AMOUNT_AFTER_GST_TAX = ? "
				+ " where ID = ? ";

			int result = jdbcTemplate.update(query, new Object[] {
					
					objGetInvoiceDetailsBean.getConveyance1(),
					objGetInvoiceDetailsBean.getConveyanceAmount1(),
					objGetInvoiceDetailsBean.getGSTTax1(),
					objGetInvoiceDetailsBean.getGSTAmount1(),
					objGetInvoiceDetailsBean.getAmountAfterTax1(),id
			});
				return result;
		}
	
		public int updateInvoiceOtherChargesDelete(String id) {
			String query =  "DELETE FROM SUMADHURA_TRNS_OTHR_CHRGS_DTLS WHERE ID = ? ";
			return jdbcTemplate.update(query, new Object[] {id});
		}
	
		/***********************************************start payment done or not check here start*************************************************/
		/**************************************check payment tables payment initiated or not for cancel po purpose start*************************************/
	public boolean checkPaymentDoneOrNotOnInvoice(String invoiceNumber,String site_id,String vendorId,String receviedDate,String siteName,int portNumber,String indentEntry_Id) {
			
			boolean returnStatus=false;
			Set<String> emailList=new HashSet<String>();
			double reqAmount=0.0;
			double invoiceAmount=0.0;
			String VendorName="";
			String currentEmpMail="";
			try{
			
				String query = "select EMP_EMAIL from SUMADHURA_EMPLOYEE_DETAILS where EMP_ID in( "
						+" SELECT ASPRD.EMP_ID FROM ACC_SITE_APPR_REJECT_DTLS ASPRD "
						+" where ASPRD.PAYMENT_DETAILS_ID in (select PAYMENT_DETAILS_ID from ACC_PAYMENT_DTLS "
						+" where STATUS='A' and PAYMENT_ID in (select PAYMENT_ID from ACC_PAYMENT "
						+" where INVOICE_NUMBER =? and SITE_ID =? and (PAYMENT_REQ_UPTO!='0') and VENDOR_ID =? and INDENT_ENTRY_ID=?))) "
						
						+" union "
						+" select EMP_EMAIL from SUMADHURA_EMPLOYEE_DETAILS where EMP_ID in( "
						+" SELECT APARD.EMP_ID from ACC_PMT_APPR_REJECT_DTLS APARD,ACC_TEMP_PAYMENT_TRANSACTIONS ATPT "
						+" WHERE APARD.TEMP_PAYMENT_TRANSACTIONS_ID=ATPT.TEMP_PAYMENT_TRANSACTIONS_ID "
						+" AND ATPT.PAYMENT_DETAILS_ID in (select PAYMENT_DETAILS_ID from ACC_PAYMENT_DTLS "
						+" where STATUS='A' and PAYMENT_ID in (select PAYMENT_ID from ACC_PAYMENT "
						+" where INVOICE_NUMBER =? and SITE_ID =? and (PAYMENT_REQ_UPTO!='0') and VENDOR_ID =? and INDENT_ENTRY_ID=?)))";
						
				List<Map<String, Object>> map = jdbcTemplate.queryForList(query,new Object[]{ invoiceNumber, site_id, vendorId,indentEntry_Id,invoiceNumber, site_id, vendorId,indentEntry_Id});
				if(map.size()>0){
					 for(Map<String, Object> data : map) {
						 String empEmail=data.get("EMP_EMAIL")==null ? "" :   data.get("EMP_EMAIL").toString();
						 if(StringUtils.isNotBlank(empEmail)){
								if(empEmail.contains(",")){
									String[] emailArr= empEmail.split(",");
									for(String Email:emailArr){
										emailList.add(Email);
									}
								}
								else{emailList.add(empEmail);}
							}
						 //email.add(empEmail);
					 }
					 //getting the requested amount so written this one
					 
					 
					 String empId="select APD.REQUEST_PENDING_EMP_ID,APD.REQUEST_PENDING_DEPT_ID,"
								+" ATPT.REQUEST_PENDING_EMP_ID as ACC_PENDING_EMP_ID,APD.STATUS "  
								+" from VENDOR_DETAILS VD,ACC_PAYMENT AP,SITE S,ACC_PAYMENT_DTLS APD "
						 		+" LEFT OUTER JOIN ACC_TEMP_PAYMENT_TRANSACTIONS ATPT on APD.PAYMENT_DETAILS_ID = ATPT.PAYMENT_DETAILS_ID " 
								+" LEFT OUTER JOIN ACC_ACCOUNTS_DEPT_PMT_PROSS AADPP on AADPP.PAYMENT_DETAILS_ID=APD.PAYMENT_DETAILS_ID "
								+" LEFT OUTER JOIN SUMADHURA_EMPLOYEE_DETAILS SED on SED.EMP_ID = APD.REQUEST_PENDING_EMP_ID "
								+" where AP.PAYMENT_ID = APD.PAYMENT_ID and AP.VENDOR_ID = VD.VENDOR_ID and AP.SITE_ID = S.SITE_ID " 
								+" and (ATPT.REQUEST_PENDING_EMP_ID NOT IN ('VND') OR ATPT.REQUEST_PENDING_EMP_ID IS NULL) "
								+" and AP.INVOICE_NUMBER = ? and APD.STATUS='A'";
					 List<Map<String, Object>> emp_Id = jdbcTemplate.queryForList(empId,new Object[]{invoiceNumber});	
					 for(Map<String, Object> data : emp_Id) {
						 String pending_Emp_Id=data.get("REQUEST_PENDING_EMP_ID")==null ? "" :   data.get("REQUEST_PENDING_EMP_ID").toString();
						 String pending_acc_Id=data.get("REQUEST_PENDING_DEPT_ID")==null ? "" :   data.get("REQUEST_PENDING_DEPT_ID").toString();
						 String pending_acc2_Id=data.get("ACC_PENDING_EMP_ID")==null ? "" :   data.get("ACC_PENDING_EMP_ID").toString();
						 if(!pending_acc2_Id.equals("")){
							 currentEmpMail="select EMP_EMAIL from SUMADHURA_EMPLOYEE_DETAILS where DEPT_ID='"+pending_acc2_Id+"'";
						 }else if(!pending_acc_Id.equals("")){
							 currentEmpMail="select EMP_EMAIL from SUMADHURA_EMPLOYEE_DETAILS where DEPT_ID='"+pending_acc_Id+"'";
						 }else{
							 currentEmpMail="select EMP_EMAIL from SUMADHURA_EMPLOYEE_DETAILS where EMP_ID='"+pending_Emp_Id+"'";
						 }
					 }
					 List<Map<String, Object>> currentMails = jdbcTemplate.queryForList(currentEmpMail,new Object[]{});
					 for(Map<String, Object> data : currentMails) {
						 String empmailId=data.get("EMP_EMAIL")==null ? "" :   data.get("EMP_EMAIL").toString();
						 if(StringUtils.isNotBlank(empmailId)){
						 if(empmailId.contains(",")){
								String[] emailArr= empmailId.split(",");
								for(String Email:emailArr){
									emailList.add(Email);
								}
							}
						 else{
						 emailList.add(empmailId);
						 }
						 }
					 }
					 
					 returnStatus=true;
					 String sql="SELECT PAYMENT_REQ_UPTO,PAYMENT_DONE_UPTO,INVOICE_AMOUNT FROM ACC_PAYMENT WHERE INVOICE_NUMBER=? AND VENDOR_ID=?";
					 List<Map<String, Object>> Amount = jdbcTemplate.queryForList(sql,new Object[]{invoiceNumber,vendorId});
					 for(Map<String, Object> data : Amount){
						 reqAmount=Double.valueOf(data.get("PAYMENT_REQ_UPTO")==null ? "0" :   data.get("PAYMENT_REQ_UPTO").toString());
						 //reqAmount_Done=Double.valueOf(data.get("PAYMENT_DONE_UPTO")==null ? "0" :   data.get("PAYMENT_DONE_UPTO").toString());
						 invoiceAmount=Double.valueOf(data.get("INVOICE_AMOUNT")==null ? "0" :   data.get("INVOICE_AMOUNT").toString());
						
					Map<String, String> vendordata =(Map<String, String>)objPurchaseDepartmentIndentProcessDao.getVendorNameEmail(vendorId);
						for(Map.Entry<String, String> retVal : vendordata.entrySet()) {
							VendorName=retVal.getKey();
						}
					
					 Object ApproveData[]={invoiceNumber,receviedDate,invoiceAmount,reqAmount};
					 List<String> list=new ArrayList<String>(emailList);
					 String ccTo [] = new String[list.size()];
					 list.toArray(ccTo);
					 EmailFunction objEmailFunction = new EmailFunction();
					 objEmailFunction.sendMailForAccountsDeptinApprovalTimeAtInvoiceUpdate(ApproveData,ccTo,VendorName,siteName,portNumber); // mail send to previous employess 

				 }
				}
			//request.setAttribute("emailList",emailList); // this is used in send mail to time permanent
			}
			catch(EmptyResultDataAccessException e){
				e.printStackTrace();
			}
			return returnStatus;

		}
	
	/*===================================================== update payment acc table start=================================================*/
	public int updateAccPayment(String invoiceNumber,String site_id,String vendorId,String ttlAmntForIncentEntryTbl)
	{
	String Query ="update ACC_PAYMENT set INVOICE_AMOUNT =? where INVOICE_NUMBER =? AND SITE_ID=? AND VENDOR_ID=?";
	int result = jdbcTemplate.update(Query, new Object[] {ttlAmntForIncentEntryTbl,invoiceNumber,site_id,vendorId});
	return result;
	}	
	
	@Override
	public List<GetInvoiceDetailsBean> getTaxInvoiceSubmissionDetails(String indentEntryId, String invoiceNumber, String siteId) {
		log.info("INDENT_ENTRY_ID "+indentEntryId+" - INVOICE_NO "+invoiceNumber+" - SITE ID "+siteId);
		StringBuffer query=new StringBuffer("SELECT   (SELECT SED.EMP_NAME FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID =ATARD.TAXINVOICE_APP_EMP_ID) as EMP_NAME ")
					.append(" ,TO_CHAR(ATARD.CREATION_DATE,'dd-MM-yyyy hh24:mi:ss') as CREATION_DATE,ATARD.OPERATION_TYPE,nvl(ATARD.REMARKS,'-') REMARKS ")
					.append(" FROM ACC_TAXINVOICE_DETAILS ATD,ACC_TAXINVOICE_APP_REJ_DETAILS ATARD WHERE ATD.INDENT_ENTRY_ID=ATARD.INDENT_ENTRY_ID AND ")
					.append(" ATD.INDENT_ENTRY_ID=? AND ATD.INVOICE_NO=? AND ATD.SITE_ID=? order by TAXINVOICE_CRETAION_APPROVAL_DTLS_ID");
			Object[] queryParams={indentEntryId,invoiceNumber,siteId};
			
	 		return  jdbcTemplate.query(query.toString(), queryParams,new ResultSetExtractor<List<GetInvoiceDetailsBean>>(){
			 
			 
			@Override
			public List<GetInvoiceDetailsBean> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<GetInvoiceDetailsBean> list=new ArrayList<GetInvoiceDetailsBean>();
				int serialNo=1;
				String empName="";
				String operationType="";
				String receivedDate="";
				String remarks="";
					while (rs.next()) {
						GetInvoiceDetailsBean bean=new GetInvoiceDetailsBean();
						empName=rs.getString("EMP_NAME")==null?"":rs.getString("EMP_NAME");
						operationType=rs.getString("OPERATION_TYPE")==null?"":rs.getString("OPERATION_TYPE");
						receivedDate=rs.getString("CREATION_DATE")==null?"":rs.getString("CREATION_DATE");
						remarks=rs.getString("REMARKS")==null?"":rs.getString("REMARKS");
						
						if(operationType.equals("C")){operationType="Submitted";}
						else if(operationType.equals("R")){operationType="Rejected";}
						else if(operationType.equals("A")){operationType="Received";}
						
						bean.setStrSerialNo(serialNo);
						bean.setEmployeeName(empName);
						bean.setOperationType(operationType);
						bean.setReceivedDate(receivedDate);
						bean.setRemarks(remarks);
						serialNo++;
						list.add(bean);
					}
				return list;
			}
			 
		 });
			
			
	}
	
	/*================================================= update in acc table and transaction table start===========================================*/
	/*************************************************advance amount added to the in update or revised po time strat**************************************/
	public boolean  updateInvoiceAccPaymenttbl(HttpServletRequest request,String invoiceNumber,String site_id,String vendorId,String ttlAmntForIncentEntryTbl,String indentEntry_Id) {
		double done_Upto=0.0;
		double request_Upto=0.0;
		double paidAmount=0.0;
		String updateDate="";
		String paymentDetailsId="";
		String paymentId="";
		String req_Pending_Emp="";
		//String strpaymentDetailsId="";
		List<String> emailList=new ArrayList<String>();
		boolean amountIncreased=false;
		double transActionPaidAmt=0.0;
		double totalTransactionAmt=0.0;
		//List<Double> amount=new ArrayList<Double>();
		//List<String> transactonId=new ArrayList<String>();
		String sql="select PAYMENT_DONE_UPTO from ACC_PAYMENT where INVOICE_NUMBER=? AND SITE_ID=? AND VENDOR_ID=? AND INDENT_ENTRY_ID=?";
		List<Map<String, Object>> mapData = jdbcTemplate.queryForList(sql,new Object[]{invoiceNumber,site_id,vendorId,indentEntry_Id});
		if(mapData.size()>0){
			//amountIncreased=true;
			for(Map<String, Object> prods : mapData) {
				paidAmount=Double.valueOf(prods.get("PAYMENT_DONE_UPTO")==null ? "0" :   prods.get("PAYMENT_DONE_UPTO").toString());
			}if(paidAmount==0 || paidAmount==0.0){
				amountIncreased=true;
			}
			if(paidAmount<=Double.valueOf(ttlAmntForIncentEntryTbl)){
				
			String query = "select AC.UPDATED_DATE,AC.PAYMENT_ID,AC.INVOICE_AMOUNT,AC.PAYMENT_REQ_UPTO,AC.PAYMENT_DONE_UPTO," 
				+" APD.PAYMENT_DETAILS_ID,APT.PAYMENT_TRANSACTIONS_ID,APD.REQUEST_PENDING_EMP_ID,APT.PAID_AMOUNT"
				+" from ACC_PAYMENT AC "
				+" left outer join ACC_PAYMENT_DTLS APD "
				+" left outer join ACC_PAYMENT_TRANSACTIONS APT on APT.PAYMENT_DETAILS_ID=APD.PAYMENT_DETAILS_ID on APD.PAYMENT_ID=AC.PAYMENT_ID"
				+" where AC.INVOICE_NUMBER =? and AC.SITE_ID =? and AC.VENDOR_ID =? ORDER BY  APT.PAYMENT_TRANSACTIONS_ID DESC";
			List<Map<String, Object>> map = jdbcTemplate.queryForList(query,new Object[]{invoiceNumber,site_id,vendorId});

			if(map!=null && map.size()>0){
				for(Map<String, Object> prods : map) {
					request_Upto=Double.valueOf(prods.get("PAYMENT_REQ_UPTO")==null ? "0" :   prods.get("PAYMENT_REQ_UPTO").toString());
					done_Upto=Double.valueOf(prods.get("PAYMENT_DONE_UPTO")==null ? "0" :   prods.get("PAYMENT_DONE_UPTO").toString());
					updateDate=prods.get("UPDATED_DATE")==null ? "-" :   prods.get("UPDATED_DATE").toString();
					paymentDetailsId=prods.get("PAYMENT_DETAILS_ID")==null ? "0" :   prods.get("PAYMENT_DETAILS_ID").toString();
					int transactionId=Integer.parseInt(prods.get("PAYMENT_TRANSACTIONS_ID")==null ? "0" :   prods.get("PAYMENT_TRANSACTIONS_ID").toString());
					paymentId=prods.get("PAYMENT_ID")==null ? "0" :   prods.get("PAYMENT_ID").toString();
					req_Pending_Emp=prods.get("REQUEST_PENDING_EMP_ID")==null ? "-" :   prods.get("REQUEST_PENDING_EMP_ID").toString();
					transActionPaidAmt=Double.valueOf(prods.get("PAID_AMOUNT")==null ? "0" :   prods.get("PAID_AMOUNT").toString());
					totalTransactionAmt+=transActionPaidAmt;
					if(transactionId!=0 && req_Pending_Emp.equals("-")){
						
							
							String query1="update ACC_PAYMENT_TRANSACTIONS set REMARKS='System Updated Invoice Amount Converted ' where PAYMENT_DETAILS_ID ='"+paymentDetailsId+"'";
							 jdbcTemplate.update(query1, new Object[] {});
							 
							 String acc_History="update ACC_INVOICE_HISTORY set INVOICE_AMOUNT=? where PAYMENT_TRANSACTIONS_ID =?";
							 jdbcTemplate.update(acc_History, new Object[] {ttlAmntForIncentEntryTbl,transactionId});
							 
							//strpaymentDetailsId+=paymentDetailsId+",";
							//amount.add(transActionPaidAmt);transactonId.add(String.valueOf(transactionId));
						
					}
				}
				/*if (!paymentDetailsId.equals("0") && strpaymentDetailsId.endsWith(",")) {
					strpaymentDetailsId = strpaymentDetailsId.substring(0, strpaymentDetailsId.length() - 1);
					}
				if(strpaymentDetailsId!=null && !strpaymentDetailsId.equals("")){
					emailList=getEmailsForPayment(strpaymentDetailsId);// mails send to payment deptment 
				}*/
			}
			amountIncreased=true;
			String Query ="update ACC_PAYMENT set INVOICE_AMOUNT=? where INVOICE_NUMBER =? AND SITE_ID=? AND VENDOR_ID=? AND INDENT_ENTRY_ID=?";
			 jdbcTemplate.update(Query, new Object[] {ttlAmntForIncentEntryTbl,invoiceNumber,site_id,vendorId,indentEntry_Id});
			 
			String invoiceAmt="update INDENT_ENTRY set TOTAL_AMOUNT =? where INVOICE_ID =? AND SITE_ID=? AND VENDOR_ID=? AND INDENT_ENTRY_ID=?"; 
			jdbcTemplate.update(invoiceAmt, new Object[] {ttlAmntForIncentEntryTbl,invoiceNumber,site_id,vendorId,indentEntry_Id});
			
			//totalTransactionAmt=totalTransactionAmt-Double.valueOf(ttlAmntForIncentEntryTbl);
			// to update paid amount in acc dept tables
			/*if(paidAmtModified){
				for(int i=0;i<amount.size();i++){
					double tempAmt=amount.get(i);
					double differenceAmt=tempAmt-totalTransactionAmt;
					if(differenceAmt<=0){
						String paidAmt="update ACC_PAYMENT_TRANSACTIONS set PAID_AMOUNT=? where PAYMENT_TRANSACTIONS_ID =?";
						jdbcTemplate.update(paidAmt, new Object[] {"0",transactonId.get(i)});
						
						String acc_History="update ACC_INVOICE_HISTORY set INVOICE_AMOUNT=?,PAID_AMOUNT=? where PAYMENT_TRANSACTIONS_ID =?";
						 jdbcTemplate.update(acc_History, new Object[] {ttlAmntForIncentEntryTbl,"0",transactonId.get(i)});
					}else{
						String paidAmt="update ACC_PAYMENT_TRANSACTIONS set PAID_AMOUNT=? where PAYMENT_TRANSACTIONS_ID =?";
						jdbcTemplate.update(paidAmt, new Object[] {differenceAmt,transactonId.get(i)});
						
						String acc_History="update ACC_INVOICE_HISTORY set INVOICE_AMOUNT=?,PAID_AMOUNT=? where PAYMENT_TRANSACTIONS_ID =?";
						 jdbcTemplate.update(acc_History, new Object[] {ttlAmntForIncentEntryTbl,differenceAmt,transactonId.get(i)});
						break;
					}
					differenceAmt=Math.abs(differenceAmt);
				}
				
				String paidAmt="update ACC_PAYMENT set PAYMENT_DONE_UPTO=? where SITE_ID=? AND VENDOR_ID=? AND INDENT_ENTRY_ID=?";
				int result1 = jdbcTemplate.update(paidAmt, new Object[] {ttlAmntForIncentEntryTbl,site_id,vendorId,indentEntry_Id});
			}*/
			}//else{ // invoice amount increase then it will execute
			
			request.setAttribute("emailList",emailList); // this is used in send mail permanent PO GENERATED TIME 

		//}	
		}else{
			amountIncreased=true;
		}


	return amountIncreased;
	}
	
	public void getGrnViewDetails1(Model model,String indentEntryId, HttpServletRequest request) {

		String query = "";
		JdbcTemplate template = null;
		List<Map<String, Object>> dbIndentDts = null;
		List<ViewIndentIssueDetailsBean> list = new ArrayList<ViewIndentIssueDetailsBean>();
		ViewIndentIssueDetailsBean indentObj = null; 

		try {
			template = new JdbcTemplate(DBConnection.getDbConnection());

				query = "SELECT  IE.INVOICE_ID,IE.SITE_ID,IE.INDENT_ENTRY_ID, IE.RECEIVED_OR_ISSUED_DATE as RECEVED_DATE,IE.INVOICE_DATE as INVOICE_DATE,IE.VENDOR_ID as VENDOR_ID FROM INDENT_ENTRY IE WHERE IE.INDENT_ENTRY_ID = ? ";
			
				dbIndentDts = template.queryForList(query, new Object[]{indentEntryId});

			for(Map<String, Object> prods : dbIndentDts) {
				indentObj = new ViewIndentIssueDetailsBean();

				model.addAttribute("invoiceNumber", prods.get("INVOICE_ID")==null ? "" : prods.get("INVOICE_ID").toString());
				model.addAttribute("vendorId", prods.get("VENDOR_ID")==null ? "" : prods.get("VENDOR_ID").toString());
				model.addAttribute("indentEntryId", prods.get("INDENT_ENTRY_ID")==null ? "" : prods.get("INDENT_ENTRY_ID").toString());
				model.addAttribute("siteId", prods.get("SITE_ID")==null ? "" : prods.get("SITE_ID").toString());
				String poEntryId = request.getParameter("poEntryId");
				model.addAttribute("poEntryId",poEntryId);
				
				String urlName = request.getParameter("urlName");
				if(StringUtils.isBlank(urlName)){
				urlName= "getGrnAndCreditNoteGrnViewDetails.spring?indentEntryId="+indentEntryId+"&poEntryId="+poEntryId;
				}model.addAttribute("urlName",urlName);
				String date=prods.get("INVOICE_DATE")==null ? "" : prods.get("INVOICE_DATE").toString();
				if (StringUtils.isNotBlank(date)) {
					date = DateUtil.dateConversion(date);
				} else {
					date = "";
				}
				model.addAttribute("invoiceDate", date);
				break;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			query = "";
			indentObj = null; 
			template = null;
			dbIndentDts = null;
		}
		
	}
	
	
}
