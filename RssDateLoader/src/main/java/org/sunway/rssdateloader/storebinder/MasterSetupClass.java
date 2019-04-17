/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sunway.rssdateloader.storebinder;

import org.joget.apps.form.lib.WorkflowFormBinder;
import org.joget.apps.form.model.Element;
import org.joget.apps.form.model.FormData;
import org.joget.apps.form.model.FormRowSet;
import org.joget.apps.form.service.FormUtil;
import org.sunway.rssdateloader.utilities.Utils;

/**
 *
 * @pc omen
 * @author Mateen
 */
public class MasterSetupClass extends WorkflowFormBinder implements OnCallBack {
    
    boolean callSuper;
    Element element = null;
    FormRowSet rowSet = null;
    FormData formData = null;

    final String pliuginName = "RSS -Master- CustomStoreBinder";

    public String getName() {
        return pliuginName;
    }

    public String getVersion() {
        return "1.0";
    }

    public String getDescription() {
        return pliuginName;
    }

    public String getLabel() {
        return getName();
    }

    public String getClassName() {
        return getClass().getName();
    }

    public String getPropertyOptions() {
        return "";
    }

    @Override
    public FormRowSet store(Element element, FormRowSet rowSet, FormData formData) {
        
        
        callSuper = true;
        FormRowSet fd = null;
        
        if (rowSet != null && !rowSet.isEmpty()) {
            String elementId = FormUtil.getElementParameterName(element);
            String formId = super.getFormId(); // Take form Id to detect and perform action on Specific form
            if (formId.equalsIgnoreCase("cmp_env")) {
                CompanyEnvironmentClass cec = new CompanyEnvironmentClass(rowSet, formData, elementId, this);
                cec.companyEnvironmentExistance();
            }
        }
        Utils.showMsg("Store Function before callSuper");
        if (callSuper) fd =  super.store(element, rowSet, formData);
        Utils.showMsg("Store Function After callSuper");
        
        return fd;
    }

    public void onSuccess() {
        callSuper = true;
    }

    public void onFailure() {
        Utils.showMsg("onFailure Before");
        callSuper = false;
        Utils.showMsg("onFailure After");
    }

    public void sendEmail(String status) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sendEmailOnFirstForm(String status, String formId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
