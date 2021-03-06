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
import org.sunway.rssdateloader.emailcomposers.EmailClass;
import org.sunway.rssdateloader.utilities.Utils;

/**
 *
 * @author kirthikan
 */
public class StoreBinderMain extends WorkflowFormBinder implements OnCallBack {

    final String pliuginName = "RSS - CustomStoreBinder";
    private boolean setSubmit;
    private String stat = "";
    private String formId = "";
    private String passingfmId = "";

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
        setSubmit = true;

        if (rowSet != null && !rowSet.isEmpty()) {
            this.formId = super.getFormId(); // Take form Id to detect and perform action on Specific form
            if (formId.equalsIgnoreCase("reviseTargetForm")) {
                String id = FormUtil.getElementParameterName(element);
                RequestForm form = new RequestForm(formData, rowSet);
                form.reviseTargetAction(id,formId);

            } else if (formId.equalsIgnoreCase("managerViewForm")) {
                FormStatusClass formStatusClass = new FormStatusClass(formData, rowSet);
                formStatusClass.managerFormAction();
            }//check for TL action
            else if (formId.equalsIgnoreCase("tl_view_form")) {
                FormStatusClass formStatusClass = new FormStatusClass(formData, rowSet);
                formStatusClass.tlFormAction();
            } //Check for the final form status
            else if (formId.equalsIgnoreCase("buFinanceViewForm")) {
                FormStatusClass formStatusClass = new FormStatusClass(formData, rowSet);
                formStatusClass.BUFormAction();
            }
            else if (formId.equalsIgnoreCase("revise_target_form")) {
                RequestForm form = new RequestForm(formData, rowSet);
                form.createRequestForm();
            }

        }
        if (setSubmit) {
            Utils.showMsg("SetSubmit = true");
            fm = super.store(element, rowSet, formData);
        } else {
            Utils.showMsg("SetSubmit = False");
        }

        
        
        return fm;
    }

    public void onSuccess() {
    }

    public void onFailure() {

        Utils.showMsg("In Failure Section");
        this.setSubmit = false;
    }

    public void sendEmail(String status) {
        Utils.showMsg("send Email");
        this.stat = status;
    }

    @Override
    public void sendEmailOnFirstForm(String status, String formId) {
        this.stat = status;
        this.passingfmId = formId;
    }
}
