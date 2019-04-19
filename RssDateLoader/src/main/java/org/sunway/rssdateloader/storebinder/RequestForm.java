/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sunway.rssdateloader.storebinder;

import java.sql.ResultSet;
import java.util.UUID;
import org.joget.apps.form.model.FormData;
import org.joget.apps.form.model.FormRow;
import org.joget.apps.form.model.FormRowSet;
import org.sunway.rssdateloader.databases.QueryHandler;
import org.sunway.rssdateloader.formdataloader.QueryHandlerInterface;
import org.sunway.rssdateloader.utilities.Utils;

/**
 *
 * @pc omen
 * @author Mateen
 */
public class RequestForm implements QueryHandlerInterface {

    private FormRowSet rowSet;
    private QueryHandler qh;
    private FormData formData;
    public OnCallBack onCallBack;

    public RequestForm(FormData formData, FormRowSet rowSet, OnCallBack aClass) {
        this.rowSet = rowSet;
        this.formData = formData;
        this.qh = new QueryHandler(this);
        this.onCallBack = aClass;
    }

    public void performAction(String elemId, String formId) {
        FormRow row = rowSet.get(0);
        String buttonAction = row.getProperty("button_box");
        String status=row.getProperty("status");
        if (buttonAction.equalsIgnoreCase("Submit")) {
            Utils.showMsg("===>>  Submitted to TL");
            if (status.equalsIgnoreCase("New"))
            row.setProperty("status", "Completed");
            onCallBack.sendEmailOnFirstForm("Completed", formId);
        } 
    }
    public void reviseTargetAction(String elemId,String formId) {
        FormRow row = rowSet.get(0);
        String internal_wd = row.getProperty("int_wd");
        String internal_date = row.getProperty("int_date");
        String external_wd = row.getProperty("ext_wd");
        String external_date = row.getProperty("ext_date");

        String rev_remarks = row.getProperty("rev_remarks");

        String revise_status = row.getProperty("revise_status");
        String status = row.getProperty("status");
        String buttonAction = row.getProperty("button_box");
        String is_revised = row.getProperty("is_revised");

        Utils.showMsg("external_date: " + external_date);

        row.setProperty("f_int_wd", internal_wd);
        row.setProperty("f_int_date", internal_date);

        row.setProperty("f_ext_wd", external_wd);
        row.setProperty("f_ext_date", external_date);

        Utils.showMsg("=> Revised Status: " + revise_status + " ; isRevised: " + is_revised + " ; status: " + status);

        if (revise_status.equalsIgnoreCase("Pending approval")
                && buttonAction.equalsIgnoreCase("Submit")
                && is_revised.equalsIgnoreCase("Yes")) {

            if (!rev_remarks.equalsIgnoreCase("")) {
                String rev_int_wd = row.getProperty("rev_internal_wd");
                String rev_int_date = row.getProperty("rev_int_date");
                String rev_ext_wd = row.getProperty("rev_external_wd");
                String rev_ext_date = row.getProperty("rev_ext_date");

                row.setProperty("f_int_wd", rev_int_wd);
                row.setProperty("f_int_date", rev_int_date);

                row.setProperty("f_ext_wd", rev_ext_wd);
                row.setProperty("f_ext_date", rev_ext_date);
                if (status.equalsIgnoreCase("New")) {
                    row.setProperty("status", "Draft");
                }
                if (formId.equalsIgnoreCase("reviseTargetForm"))
                    onCallBack.sendEmailOnFirstForm("Draft", formId);
            } else {
                Utils.showError(formData, elemId, "Revision remarks is compulsory.");
                onCallBack.onFailure();
            }

            Utils.showMsg("===>>  1");

        }else {
            Utils.showMsg("===>>  else loop of 1");
        }
    }
//    public void SubmitToTL(String elemId){
//        FormRow row = rowSet.get(0);
//        String buttonAction = row.getProperty("button_box");
//        if(buttonAction.equalsIgnoreCase("Submit"))
//        {
//           row.setProperty("is_revised", "No");
//           row.setProperty("status", "Completed"); 
//        }
//
//    }

    public void updateFormAction(String elemId,String formId) {
        FormRow row = rowSet.get(0);
        String internal_wd = row.getProperty("int_wd");
        String internal_date = row.getProperty("int_date");
        String external_wd = row.getProperty("ext_wd");
        String external_date = row.getProperty("ext_date");

        String rev_remarks = row.getProperty("rev_remarks");

        String revise_status = row.getProperty("revise_status");
        String status = row.getProperty("status");
        String buttonAction = row.getProperty("button_box");
        String is_revised = row.getProperty("is_revised");

        Utils.showMsg("external_date: " + external_date);

        row.setProperty("f_int_wd", internal_wd);
        row.setProperty("f_int_date", internal_date);

        row.setProperty("f_ext_wd", external_wd);
        row.setProperty("f_ext_date", external_date);

        Utils.showMsg("=> Revised Status: " + revise_status + " ; isRevised: " + is_revised + " ; status: " + status);

        if (buttonAction.equalsIgnoreCase("Resubmit")
                && is_revised.equalsIgnoreCase("No")
                && (status.equalsIgnoreCase("TLRejected")
                    || status.equalsIgnoreCase("BURejected"))) {

            if (!rev_remarks.equalsIgnoreCase("")) {
                String rev_int_wd = row.getProperty("rev_internal_wd");
                String rev_int_date = row.getProperty("rev_int_date");
                String rev_ext_wd = row.getProperty("rev_external_wd");
                String rev_ext_date = row.getProperty("rev_ext_date");

                row.setProperty("f_int_wd", rev_int_wd);
                row.setProperty("f_int_date", rev_int_date);

                row.setProperty("f_ext_wd", rev_ext_wd);
                row.setProperty("f_ext_date", rev_ext_date);

                row.setProperty("is_revised", "Yes");
                row.setProperty("revise_status", "Pending approval");
                if (status.equalsIgnoreCase("New")) {
                    row.setProperty("status", "Draft");
                }
                if (formId.equalsIgnoreCase("user_update_form"))
                    onCallBack.sendEmailOnFirstForm("Draft", formId);
            } else {
                Utils.showError(formData, elemId, "Revision remarks is compulsory.");
                onCallBack.onFailure();
            }

            Utils.showMsg("===>>  1");

        } else if ((status.equalsIgnoreCase("TLRejected")
                    || status.equalsIgnoreCase("BURejected")) 
                && buttonAction.equalsIgnoreCase("Resubmit") 
                && is_revised.equalsIgnoreCase("Yes") 
                && revise_status.equalsIgnoreCase("Pending approval")) {
            Utils.showMsg("===>>  4");
            row.setProperty("status", "Completed");
            if (formId.equalsIgnoreCase("user_update_form"))
                    onCallBack.sendEmailOnFirstForm("Completed", formId);
        } else {
            Utils.showMsg("===>>  5");
        }
    }

    public void userUpdateNewForm(String elemId,String formId) {
        FormRow row = rowSet.get(0);
        String internal_wd = row.getProperty("int_wd");
        String internal_date = row.getProperty("int_date");
        String external_wd = row.getProperty("ext_wd");
        String external_date = row.getProperty("ext_date");

        String rev_remarks = row.getProperty("rev_remarks");

        String revise_status = row.getProperty("revise_status");
        String status = row.getProperty("status");
        String buttonAction = row.getProperty("button_box");
        String is_revised = row.getProperty("is_revised");

        Utils.showMsg("external_date: " + external_date);

        row.setProperty("f_int_wd", internal_wd);
        row.setProperty("f_int_date", internal_date);

        row.setProperty("f_ext_wd", external_wd);
        row.setProperty("f_ext_date", external_date);

        Utils.showMsg("=> Revised Status: " + revise_status + " ; isRevised: " + is_revised + " ; status: "
                + status + " ; raju: " + is_revised.equalsIgnoreCase("Yes"));

        if (buttonAction.toString().equalsIgnoreCase("Resubmit")
                && is_revised.toString().equalsIgnoreCase("Yes")
                && (status.toString().equalsIgnoreCase("TLRejected")
                || status.toString().equalsIgnoreCase("BURejected"))
                && (!revise_status.toString().equalsIgnoreCase("Approved")
                || !revise_status.toString().equalsIgnoreCase("Rejected"))) {

            if (!rev_remarks.equalsIgnoreCase("")) {
                String rev_int_wd = row.getProperty("rev_internal_wd");
                String rev_int_date = row.getProperty("rev_int_date");
                String rev_ext_wd = row.getProperty("rev_external_wd");
                String rev_ext_date = row.getProperty("rev_ext_date");

                row.setProperty("f_int_wd", rev_int_wd);
                row.setProperty("f_int_date", rev_int_date);

                row.setProperty("f_ext_wd", rev_ext_wd);
                row.setProperty("f_ext_date", rev_ext_date);

                if (status.equalsIgnoreCase("New")) {
                    row.setProperty("status", "Draft");
                }
                if (formId.equalsIgnoreCase("update_form"))
                    onCallBack.sendEmailOnFirstForm("Draft", formId);
            } else {
                Utils.showError(formData, elemId, "Revision remarks is compulsory.");
                onCallBack.onFailure();
            }

            Utils.showMsg("===>>  1");

        } else if ((status.toString().equalsIgnoreCase("TLRejected")
                || status.toString().equalsIgnoreCase("BURejected"))
                && buttonAction.equalsIgnoreCase("Resubmit")
                && is_revised.equalsIgnoreCase("No")
                && (revise_status.toString().equalsIgnoreCase("Approved")
                || revise_status.toString().equalsIgnoreCase("Rejected"))) {
            Utils.showMsg("===>>  4");
            row.setProperty("status", "Completed");
            if (formId.equalsIgnoreCase("update_form"))
                    onCallBack.sendEmailOnFirstForm("Completed", formId);
        } else {
            Utils.showMsg("===>>  5");
        }

    }

    public void updateManagerFormAction(String elemId,String formId) {
        FormRow row = rowSet.get(0);
        String internal_wd = row.getProperty("int_wd");
        String internal_date = row.getProperty("int_date");
        String external_wd = row.getProperty("ext_wd");
        String external_date = row.getProperty("ext_date");

        String rev_remarks = row.getProperty("rev_remarks");

        String revise_status = row.getProperty("revise_status");
        String status = row.getProperty("status");
        String buttonAction = row.getProperty("button_box");
        String is_revised = row.getProperty("is_revised");

        Utils.showMsg("external_date: " + external_date);

        row.setProperty("f_int_wd", internal_wd);
        row.setProperty("f_int_date", internal_date);

        row.setProperty("f_ext_wd", external_wd);
        row.setProperty("f_ext_date", external_date);

        if (buttonAction.equalsIgnoreCase("Resubmit")
                && revise_status.equalsIgnoreCase("Pending Approval")) {

            if (!rev_remarks.equalsIgnoreCase("")) {
                String rev_int_wd = row.getProperty("rev_internal_wd");
                String rev_int_date = row.getProperty("rev_int_date");
                String rev_ext_wd = row.getProperty("rev_external_wd");
                String rev_ext_date = row.getProperty("rev_ext_date");

                row.setProperty("f_int_wd", rev_int_wd);
                row.setProperty("f_int_date", rev_int_date);

                row.setProperty("f_ext_wd", rev_ext_wd);
                row.setProperty("f_ext_date", rev_ext_date);

                if (status.equalsIgnoreCase("New")) {
                    row.setProperty("status", "Draft");
                }
                if (formId.equalsIgnoreCase("user_UpdateManager"))
                    onCallBack.sendEmailOnFirstForm("Draft", formId);
            } else {
                Utils.showError(formData, elemId, "Revision remarks is compulsory.");
                onCallBack.onFailure();
            }

            Utils.showMsg("===>>  In If condition");
        } else if (buttonAction.equalsIgnoreCase("Resubmit")
                && !revise_status.equalsIgnoreCase("Pending Approval")) {
            Utils.showMsg("===>>  In different Way");
            row.setProperty("status", "Completed");
            if (formId.equalsIgnoreCase("user_UpdateManager"))
                    onCallBack.sendEmailOnFirstForm("Completed", formId);
        } else {
            Utils.showMsg("===>>  Else Triggered");
            Utils.showMsg("status: " + status + " & Button: " + buttonAction);
        }
    }

    public void onSuccess(ResultSet rSet) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void onFailure() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void onHolidayCallBack(ResultSet rSet) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
