package com.sumadhura.transdao;

import java.util.List;

import com.sumadhura.bean.TransportChargesBean;

public interface TransportChargesDetailsDao {

	List<TransportChargesBean> getTransportChargesList(String invoiceNumber, String siteId);

}
