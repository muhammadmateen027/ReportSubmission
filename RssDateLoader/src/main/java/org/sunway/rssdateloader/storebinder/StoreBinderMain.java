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
            if (formId.equalsIgnoreCase("revise_target_form")) {
                String id = FormUtil.getElementParameterName(element);
                RequestForm form = new RequestForm(formData, rowSet, this);
                form.performAction(id);
            }//check for manager action
            else if (formId.equalsIgnoreCase("managerViewForm")) {
                ManagerFormClass managerFormClass = new ManagerFormClass(rowSet);
                managerFormClass.formAction();
            }//check for BUFinance Approval
//            else if (formId.equalsIgnoreCase("tlViewForm")) {
//                FormRow row = rowSet.get(0);
//                String tlAction = row.getProperty("actionButton");
//                if (tlAction.equalsIgnoreCase("Approved")) {
//                    row.setProperty("status", "Pending Approval for BuFinance");
//                } else if (tlAction.equalsIgnoreCase("Reject")) {
//                    row.setProperty("status", "TeamLeader Rejected");
//                }
//
//            }//Check for the final form status
//            else if (formId.equalsIgnoreCase("buFinanceViewForm")) {
//                FormRow row = rowSet.get(0);
//                String buFinanceAction = row.getProperty("actionButton");
//                if (buFinanceAction.equalsIgnoreCase("Approved")) {
//                    row.setProperty("status", "Form Approved");
//                } else if (buFinanceAction.equalsIgnoreCase("Reject")) {
//                    row.setProperty("status", "BUFinance Rejected");
//                }
//
//            } else if (formId.equalsIgnoreCase("user_change_form")) {
//                FormRow row = rowSet.get(0);
//                String isRevised = row.getProperty("is_revised");
//                String buttonAction = row.getProperty("button_box");
//                if (isRevised.equalsIgnoreCase("Yes") && buttonAction.equalsIgnoreCase("Submit")) {
//                    String rev_int_wd = row.getProperty("rev_internal_wd");
//                    String rev_int_date = row.getProperty("rev_int_date");
//                    String rev_ext_wd = row.getProperty("rev_external_wd");
//                    String rev_ext_date = row.getProperty("rev_ext_date");
//                    row.setProperty("f_int_wd", rev_int_wd);
//                    row.setProperty("f_int_date", rev_int_date);
//                    row.setProperty("f_ext_wd", rev_ext_wd);
//                    row.setProperty("f_ext_date", rev_ext_date);
//                    row.setProperty("status", "New");
//                    row.setProperty("revise_status", "Pending Approval for Manager");
//                } else if (isRevised.equalsIgnoreCase("No") && buttonAction.equalsIgnoreCase("Submit")) {
//                    row.setProperty("status", "Pending Approval for TL");
//                }
//            }
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
