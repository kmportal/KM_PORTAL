package com.ibm.km.actions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.ibm.km.dto.KmViewDthAegingDto;
import com.ibm.km.dto.OfferDetailsDTO;
import com.ibm.km.forms.KmViewDthAegingFormBean;
import com.ibm.km.services.KmViewDthAegingService;
import com.ibm.km.services.impl.KmViewDthAegingServiceImpl;

public class KmViewDthAegingAction extends DispatchAction{

	public ActionForward initDth(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		//System.out.println("Inside KmViewDthAegingAction.initDth!!!!");
		
		KmViewDthAegingFormBean formBean = (KmViewDthAegingFormBean)form;
		String today;
		GregorianCalendar gc = new GregorianCalendar();
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		Date yest = gc.getTime();
		today  = sdf.format(yest);
		//today = today.substring(0,10);
		//System.out.println("today="+today);

		formBean.setAegingFromSuspDate(today);
		
		//Set bucket freezing date//
		Calendar calendar = Calendar.getInstance();   
        calendar.setTime(new Date());  
        Date currentDate = calendar.getTime();
        calendar.set(Calendar.DAY_OF_MONTH, 1);   
        Date bucketFreezingDate = calendar.getTime();
        String d = sdf.format(bucketFreezingDate);
        bucketFreezingDate = sdf.parse(d);
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd-yyyy");   
        formBean.setBucketFreezingDate(sdf1.format(bucketFreezingDate));
        //////////////////////////
		
        //Set aegingFromSuspDate//
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
	    Date aegingFromSuspDate = dateFormat.parse(formBean.getAegingFromSuspDate()); 
        
	    //System.out.println(bucketFreezingDate+","+aegingFromSuspDate);
	    
	    //Set toDate, fromDate based on preselected Date
        formBean.setToDate(((aegingFromSuspDate.getTime()-currentDate.getTime())/(1000*60*60*24))+"");
		formBean.setFromDate(((aegingFromSuspDate.getTime()-bucketFreezingDate.getTime())/(1000*60*60*24))+"");

        formBean.setOfferList(new ArrayList());
		return mapping.findForward("success");
	}
	public ActionForward getBucketDetails(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
		
		KmViewDthAegingFormBean formBean = (KmViewDthAegingFormBean)form;
		ArrayList<OfferDetailsDTO> offerList = new ArrayList<OfferDetailsDTO>();
		//get entered date as date//
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
	    Date aegingFromSuspDate = dateFormat.parse(formBean.getAegingFromSuspDate());
	    //System.out.println("formBean.getAegingFromSuspDate()=="+formBean.getAegingFromSuspDate());
		
	    //get 1st Day of the month as date//
	    Calendar calendar = Calendar.getInstance();   
        calendar.setTime(new Date());  
        calendar.set(Calendar.DAY_OF_MONTH, 1);   
        Date bucketFreezingDate = calendar.getTime();
        String d = dateFormat.format(bucketFreezingDate);
        bucketFreezingDate = dateFormat.parse(d);
        
        //set fromdate//
        //using calendar
        Calendar calBucketFreezingDate = Calendar.getInstance();
        calBucketFreezingDate.setTime(bucketFreezingDate);
        Calendar calAegingFromSuspDate = Calendar.getInstance();
        calAegingFromSuspDate.setTime(aegingFromSuspDate);
        long fromDays = daysBetween(calAegingFromSuspDate,calBucketFreezingDate);
        //formBean.setFromDate(fromDays+"");
        ////////
        //not using calendar
        int fromDate = (int)((aegingFromSuspDate.getTime()-bucketFreezingDate.getTime())/(1000*60*60*24));
        if(fromDate <0)
        	fromDate = fromDate*(-1);

        formBean.setFromDate(fromDate+"");
        /////////////////////
        
        
        //get Current date//
        Calendar calendar1 = Calendar.getInstance();
        Date currentDate = calendar1.getTime();
        d = dateFormat.format(currentDate);
        currentDate = dateFormat.parse(d);

        
        //set todate//
        //using calendar
        Calendar calCurrentDate = Calendar.getInstance();
        calCurrentDate.setTime(currentDate);
        Calendar calAegingFromSuspDate1 = Calendar.getInstance();
        calAegingFromSuspDate1.setTime(aegingFromSuspDate);
        long toDays = daysBetween(calAegingFromSuspDate1, calCurrentDate);
        //formBean.setToDate(toDays+"");
        ///////////
        //not using calendar
        int toDate = (int)(((aegingFromSuspDate.getTime())-(currentDate.getTime()))/(1000*60*60*24));
        if(toDate <0)
        	 toDate = toDate*(-1);
        
        formBean.setToDate(toDate+"");
		////System.out.println("getAegingfromsuspDate =="+formBean.getAegingFromSuspDate());
		//////////////////////////
        KmViewDthAegingService service = new KmViewDthAegingServiceImpl();
        ArrayList<KmViewDthAegingDto> bucketDetails = service.getBucketId(formBean.getToDate());
        if(bucketDetails != null && bucketDetails.size() != 0){
        	offerList = service.getOfferDetails(bucketDetails.get(0).getBucketId());
        	formBean.setBucketForCustomer(bucketDetails.get(0).getFromDay()+"-"+bucketDetails.get(0).getToDay());
        }
        formBean.setOfferList(offerList);
		return mapping.findForward("success");
	}
	public static long daysBetween(Calendar startDate, Calendar endDate) {  
		  Calendar date = (Calendar) startDate.clone();  
		  long daysBetween = 0;  
		  while (date.before(endDate)) {  
		    date.add(Calendar.DAY_OF_MONTH, 1);  
		    daysBetween++;  
		  }  
		  return daysBetween;  
		}  
}
