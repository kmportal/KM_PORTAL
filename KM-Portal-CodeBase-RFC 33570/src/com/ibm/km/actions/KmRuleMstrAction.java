package com.ibm.km.actions;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import javax.servlet.http.*;

public class KmRuleMstrAction extends Action {


    public ActionForward execute(
            ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws Exception {
        ActionErrors errors = new ActionErrors();
        ActionForward forward = new ActionForward();
        HttpSession session = request.getSession();
        //TODO Actual Code
        return null;
    }
}
