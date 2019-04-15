/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sunway.rssdateloader.storebinder;

import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.lib.WorkflowFormBinder;
import org.joget.apps.form.model.Element;
import org.joget.apps.form.model.FormData;
import org.joget.apps.form.model.FormRow;
import org.joget.apps.form.model.FormRowSet;
import org.joget.apps.form.service.FormUtil;
import org.sunway.rssdateloader.databases.QueryHandler;
import org.sunway.rssdateloader.utilities.Utils;

/**
 *
 * @author kirthikan
 */
public class StoreBinderMain extends WorkflowFormBinder implements OnCallBack {

    final String pliuginName = "RSS - CustomStoreBinder";
    private boolean setSubmit = true;

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
        FormRowSet fm = null;
        if (rowSet != null && !rowSet.isEmpty()) {
            String formId = super.getFormId(); // Take form Id to detect and perform action on Specific form
            if (formId.equalsIgnoreCase("revise_target_form") || formId.equalsIgnoreCase("user_update_form")||formId.equalsIgnoreCase("draftForm")) {
                String id = FormUtil.getElementParameterName(element);
                RequestForm form = new RequestForm(formData, rowSet, this);
                form.performAction(id);
            }
//            else if (formId.equalsIgnoreCase("user_submit_form")) {
//              String id = FormUtil.getElementParameterName(element);
//                RequestForm form = new RequestForm(formData, rowSet, this);
//                form.SubmitToTL(id);
//           }//check for Manager action
            else if (formId.equalsIgnoreCase("managerViewForm")) {
                FormStatusClass formStatusClass = new FormStatusClass(rowSet);
                formStatusClass.managerFormAction();
            }//check for TL action
            else if (formId.equalsIgnoreCase("tl_view_form")) {
                FormStatusClass formStatusClass = new FormStatusClass(rowSet);
                formStatusClass.tlFormAction();
            }
           //Check for the final form status
           else if (formId.equalsIgnoreCase("buFinanceViewForm")) {
              FormStatusClass formStatusClass = new FormStatusClass(rowSet);
                formStatusClass.BUFormAction();
           }
                       
        }
        if (setSubmit) {
            fm = super.store(element, rowSet, formData);
        }
        return fm;
    }

    public void onSuccess() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void onFailure() {
        setSubmit = false;
    }
}
