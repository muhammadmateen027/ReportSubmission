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
import org.sunway.rssdateloader.emailcomposers.EmailClass;
import org.sunway.rssdateloader.formdataloader.QueryHandlerInterface;

/**
 *
 * @pc omen
 * @author Mateen
 */
public class FormStatusClass implements QueryHandlerInterface {

    private FormRowSet rowSet;
    private QueryHandler qh;
    private FormData formData;

    public FormStatusClass(FormData formData, FormRowSet rowSet) {
        this.rowSet = rowSet;
        this.formData = formData;
        this.qh = new QueryHandler(this);
    }

    public void managerFormAction() {

        FormRow row = rowSet.get(0);
        String uId = UUID.randomUUID().toString();
        String mgrAction = row.getProperty("actionButton");
        String id = row.getProperty("id");
        String current_manager = row.getProperty("current_manager");
        String manager_remarks = row.getProperty("manager_remarks");
        String userAction = "";

        EmailClass ec = new EmailClass(formData, rowSet);
        if (mgrAction.equalsIgnoreCase("Approve")) {
            userAction = "Approved";
            
        } else if (mgrAction.equalsIgnoreCase("Reject")) {
            userAction = "Rejected";
            row.setProperty("rev_internal_wd", "");
            row.setProperty("rev_external_wd", "");
            row.setProperty("rev_int_date", "");
            row.setProperty("rev_ext_date", "");            
        }
        row.setProperty("is_revised", "No");
        row.setProperty("revise_status", userAction);
        ec.mainReqEmailComposer(userAction);
        qh.updateHistoryLog(uId, id, current_manager, userAction + " KPI revision request", manager_remarks);

    }

    public void tlFormAction() {
        FormRow row = rowSet.get(0);
        String uId = UUID.randomUUID().toString();
        String tlAction = row.getProperty("actionButton");
        String id = row.getProperty("id");
        String current_tl = row.getProperty("current_tl");
        String tl_remarks = row.getProperty("tl_remarks");
        String historyStatus = "";        
        if (tlAction.equalsIgnoreCase("Approve")) {
            historyStatus = "Request approved by TL and is pending by BU Finanac's approval";
        } else if (tlAction.equalsIgnoreCase("Reject")) {
            historyStatus = "Request rejected by TL and is pending for completion";
        }
        qh.updateHistoryLog(uId, id, current_tl, historyStatus, tl_remarks);
    }

    public void BUFormAction() {

        FormRow row = rowSet.get(0);
        String uId = UUID.randomUUID().toString();
        String buAction = row.getProperty("buActionButtons");
        String id = row.getProperty("id");
        String current_bu = row.getProperty("current_bu");
        String bu_remarks = row.getProperty("bu_remarks");
        String historyStatus = "";
        
        if (buAction.equalsIgnoreCase("Approve")) {
            historyStatus = "Request approved by BU Finanace and is fully closed";
        } else if (buAction.equalsIgnoreCase("Reject")) {
            historyStatus = "Request rejected by BU and is pending for completion";
        }

        qh.updateHistoryLog(uId, id, current_bu, historyStatus, bu_remarks);

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
