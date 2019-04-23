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

    public RequestForm(FormData formData, FormRowSet rowSet) {
        this.rowSet = rowSet;
        this.formData = formData;
        this.qh = new QueryHandler(this);
    }

    public void reviseTargetAction(String elemId, String formId) {
        FormRow row = rowSet.get(0);
        String rev_remarks = row.getProperty("rev_remarks");
        String revise_status = row.getProperty("revise_status");

        String buttonAction = row.getProperty("button_box");
        
        String id = row.getProperty("id");
        String current_tl = row.getProperty("current_tl");
        
        String uId = UUID.randomUUID().toString();

        if (buttonAction.equalsIgnoreCase("Submit to Manager")) {

            String rev_int_wd = row.getProperty("rev_internal_wd");
            String rev_int_date = row.getProperty("rev_int_date");
            String rev_ext_wd = row.getProperty("rev_external_wd");
            String rev_ext_date = row.getProperty("rev_ext_date");

            row.setProperty("f_int_wd", rev_int_wd);
            row.setProperty("f_int_date", rev_int_date);

            row.setProperty("f_ext_wd", rev_ext_wd);
            row.setProperty("f_ext_date", rev_ext_date);
            
            qh.updateHistoryLog(uId, id, current_tl, revise_status, rev_remarks);
        } else {
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
