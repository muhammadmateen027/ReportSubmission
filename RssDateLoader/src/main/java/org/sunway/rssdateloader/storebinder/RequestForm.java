/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sunway.rssdateloader.storebinder;

import java.sql.ResultSet;
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

    public void performAction(String elemId) {
        FormRow row = rowSet.get(0);
        String internal_wd = row.getProperty("int_wd");
        String internal_date = row.getProperty("int_date");
        String external_wd = row.getProperty("ext_wd");
        String external_date = row.getProperty("ext_date");

        String rev_remarks = row.getProperty("rev_remarks");

        String isRevised = row.getProperty("is_revised");
        String buttonAction = row.getProperty("button_box");

        Utils.showMsg("external_date: " + external_date);

        row.setProperty("f_int_wd", internal_wd);
        row.setProperty("f_int_date", internal_date);

        row.setProperty("f_ext_wd", external_wd);
        row.setProperty("f_ext_date", external_date);

        if (isRevised.equalsIgnoreCase("Yes") && buttonAction.equalsIgnoreCase("Submit")) {

            if (!rev_remarks.equalsIgnoreCase("")) {
                String rev_int_wd = row.getProperty("rev_internal_wd");
                String rev_int_date = row.getProperty("rev_int_date");
                String rev_ext_wd = row.getProperty("rev_external_wd");
                String rev_ext_date = row.getProperty("rev_ext_date");

                row.setProperty("f_int_wd", rev_int_wd);
                row.setProperty("f_int_date", rev_int_date);

                row.setProperty("f_ext_wd", rev_ext_wd);
                row.setProperty("f_ext_date", rev_ext_date);

                row.setProperty("revise_status", "Pending Approval for Manager");
            }else {
                Utils.showError(formData, elemId, "Revision remarks is compulsory.");
                onCallBack.onFailure();
            }

        } else if (isRevised.equalsIgnoreCase("No") && buttonAction.equalsIgnoreCase("Submit")) {
            row.setProperty("status", "Pending Approval for TL");
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
