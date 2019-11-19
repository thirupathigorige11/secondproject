package com.sumadhura.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import com.sumadhura.bean.TransportChargesBean;
import com.sumadhura.transdao.TransportChargesDetailsDao;
@Service("getTransportChargesListServiceClass")
public class GetTransportChargesListServiceImpl implements GetTransportChargesListService {
	
	@Autowired
	private TransportChargesDetailsDao transportChargesDetailsClass;
	
	
	@Autowired
	PlatformTransactionManager transactionManager;

	@Override
	public List<TransportChargesBean> getTransportChargesList(String invoiceNumber, String siteId) {
		List<TransportChargesBean> transportChargesList = null;

		try {

		
			transportChargesList = transportChargesDetailsClass.getTransportChargesList(invoiceNumber,  siteId);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return transportChargesList;
	}
	}


