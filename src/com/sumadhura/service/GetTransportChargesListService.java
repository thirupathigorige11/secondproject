package com.sumadhura.service;

import java.util.List;

import com.sumadhura.bean.TransportChargesBean;


public interface GetTransportChargesListService {

	List<TransportChargesBean> getTransportChargesList(String invoiceNumber, String siteId);

}
