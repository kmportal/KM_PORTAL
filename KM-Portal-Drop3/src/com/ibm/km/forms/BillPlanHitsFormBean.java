/**
 * 
 */
package com.ibm.km.forms;

import java.util.Map;

import org.apache.struts.action.ActionForm;

/**
 * @author Nehil Parashar
 *
 */
public class BillPlanHitsFormBean extends ActionForm 
{
	private Map<String, Integer> billPlanHits;

	public void setBillPlanHits(Map<String, Integer> billPlanHits) {
		this.billPlanHits = billPlanHits;
	}

	public Map<String, Integer> getBillPlanHits() {
		return billPlanHits;
	}
	
}
