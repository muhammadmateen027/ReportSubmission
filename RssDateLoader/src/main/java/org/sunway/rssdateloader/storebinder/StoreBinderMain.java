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
            if (formId.equalsIgnoreCase("revise_target_form")
                    || formId.equalsIgnoreCase("draftForm")
                    || formId.equalsIgnoreCase("user_submit_form")) {
                String id = FormUtil.getElementParameterName(element);
                RequestForm form = new RequestForm(formData, rowSet, this);
                form.performAction(id, formId);
            } else if (formId.equalsIgnoreCase("user_update_form")) {
                String id = FormUtil.getElementParameterName(element);
                RequestForm form = new RequestForm(formData, rowSet, this);
                form.updateFormAction(id,formId);

            }else if (formId.equalsIgnoreCase("update_form")) {
                String id = FormUtil.getElementParameterName(element);
                RequestForm form = new RequestForm(formData, rowSet, this);
                form.userUpdateNewForm(id,formId);

            } 
            else if (formId.equalsIgnoreCase("user_UpdateManager")) {
                String id = FormUtil.getElementParameterName(element);
                RequestForm form = new RequestForm(formData, rowSet, this);
                form.updateManagerFormAction(id,formId);

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

        }
        if (setSubmit) {
            Utils.showMsg("SetSubmit = true");
            fm = super.store(element, rowSet, formData);
        }

        EmailClass emc = new EmailClass(formData, rowSet);
//        if (!stat.equalsIgnoreCase("") && 
//                (formId.equalsIgnoreCase("revise_target_form")
//                || formId.equalsIgnoreCase("user_submit_form")
//                || formId.equalsIgnoreCase("draftForm"))) {
//            Utils.showMsg("After form submission status: " + stat);
//            emc.mainReqEmailComposer(stat);
//        } 
         if (!stat.equalsIgnoreCase("") && 
                (formId.equalsIgnoreCase("revise_target_form")
                 || formId.equalsIgnoreCase("user_submit_form")
                 ||formId.equalsIgnoreCase("user_update_form")
                 ||formId.equalsIgnoreCase("update_form")
                 ||formId.equalsIgnoreCase("user_UpdateManager"))) {
            emc.mainReqEmailComposer(stat);
            
        }
        Utils.showMsg("SetSubmit = False");
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
