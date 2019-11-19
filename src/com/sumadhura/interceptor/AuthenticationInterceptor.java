package com.sumadhura.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	throws Exception {

		final HttpSession session = request.getSession(false);
		String uri = request.getRequestURI();
		if (uri.endsWith("login.spring")) {
			return true;

		}
		else if(uri.endsWith("vendorloginsubmit.spring")) {
			return true;

		}else if(uri.endsWith("SaveEnquiryForm.spring")) {
			return true;

		}else if(uri.endsWith("approveIndentCreationFromMail.spring")) {
			return true;

		}else if(uri.endsWith("rejectIndentCreationFromMail.spring")) {
			return true;

		}else if(uri.endsWith("showPODetailsToVendor.spring")) {
			return true;

		}
		else if(uri.endsWith("forGetPassword.spring")) {
			return true;

		}
		else if(uri.endsWith("changeToNewPassword.spring")) {
			return true;

		}
		else if(uri.endsWith("sendMailWithoutLogin.spring")) { 
			return true;

		}else if(uri.endsWith("getDetailsforPoApproval.spring")) {
			return true;

		}else if(uri.endsWith("SavePoApproveDetails.spring")) {
			return true;

		}else if(uri.endsWith("RejectPoDetails.spring")) {
			return true;


		}else if(uri.endsWith("getQuatationDetails.spring")) {
			return true;

		}else if(uri.endsWith("getComparisionStatement.spring")) {
			return true;

		}else if(uri.endsWith("cancelPO.spring")) {
			return true;

		}else if(uri.endsWith("RejectMailTempPO.spring")) {
			return true;
			
		}else if(uri.endsWith("IndentRejectFromMail.spring")) {
			return true;
			
		}else if(uri.endsWith("downloadPdf.spring")) {
			return true;
			
		}
		
		else if(uri.endsWith("getRandomNo.spring")) {
			return true;
			
		}
		else if(uri.endsWith("getAllSitesList.spring")) {
			return true;

		}
		else if(uri.endsWith("TotalReceiveIssueAmount.spring")) {
			return true;

		}else if(uri.endsWith("showMarketingPODetailsToVendor.spring")) {
			return true;

		}else if(uri.endsWith("getDetailsforMarketingPoApproval.spring")) {
			return true;

		}else if(uri.endsWith("SaveMarketingPoApproveDetails.spring")) {
			return true;

		}else if(uri.endsWith("CancelMailMarketingTempPO.spring")) {
			return true;

		}else if(uri.endsWith("cancelMarketingPoDetails.spring")) {
			return true;

		}else if(uri.endsWith("saveApprovalCancelPermanentPoFromMail.spring")) {
			return true;

		}else if(uri.endsWith("CancelPerminentPODetailsRejectFromMail.spring")) {
			return true;

	}else if(uri.endsWith("showPerminentPODetailsToCancel.spring")) {
			return true;

		}else if(uri.endsWith("saveCancelPerminentPODetails.spring")) {
			return true;

		}else if(uri.endsWith("CancelPerminentPODetailsReject.spring")) {
			return true;
		}else if(uri.endsWith("getPoDetailsList.spring")) {
			return true;

		}else if(uri.endsWith("getInvoiceDetails.spring")) {
			return true;

		}else if(uri.endsWith("UpdatedTempPoApproval.spring")) {
			return true;

		}else if(uri.endsWith("loginRestServices.spring")) {
			return true;

		} else if (uri.endsWith("approveWorkOrderFromMail.spring")) {
			return true;
		} else if (uri.endsWith("rejectOrModifyWorkOrderFromMail.spring")) {
			return true;
		} else if (uri.endsWith("getMyCompletedWorkOrder.spring")) {
			return true;
		} else if (uri.endsWith("printWorkOrderDetail.spring")) {
			return true;
		}else if(uri.endsWith("MarketingModifyTempPO.spring")) {
			return true;

		}


		else if((session == null || session.isNew()) || (null==session.getAttribute("UserId"))){
			String context = request.getContextPath();
			response.sendRedirect("/Sumadhura/");
			return false;
		}else{

			return true;
		}  
	}
}