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

/**
 *
 * @pc omen
 * @author Mateen
 */
public class FormStatusClass implements QueryHandlerInterface{

    private FormRowSet rowSet;
    private QueryHandler qh;

    public FormStatusClass(FormRowSet rowSet) {
        this.rowSet = rowSet;
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
        
        if (mgrAction.equalsIgnoreCase("Approved")) {
            userAction = "Approved";
        } else if (mgrAction.equalsIgnoreCase("Reject")) {
            userAction = "Rejected";
        }
        
        
        row.setProperty("is_revised", "No");
        row.setProperty("revise_status", userAction);  
        qh.updateHistoryLog(uId, id, current_manager, userAction+" KPI revision request", manager_remarks);

    }
    
    public void tlFormAction() {
        FormRow row = rowSet.get(0);
        String uId = UUID.randomUUID().toString();
        String tlAction = row.getProperty("actionButton");
        String id = row.getProperty("id");
        String current_tl = row.getProperty("current_tl");
        String tl_remarks = row.getProperty("tl_remarks");
        String userAction = "";
        String historyStatus="";
        if (tlAction.equalsIgnoreCase("Approved")) {
            userAction = "TLApproved";
            historyStatus="Request approved by TL and is pending by BU Finanac's approval";
        } else if (tlAction.equalsIgnoreCase("Reject")) {
            userAction = "TLRejected";
            historyStatus="Request rejected by TL and is pending for completion";
        }
        row.setProperty("is_revised", "No");
        row.setProperty("status", userAction);  
        qh.updateHistoryLog(uId, id, current_tl, historyStatus, tl_remarks);

    }
    
    public void BUFormAction() {

        FormRow row = rowSet.get(0);
        String uId = UUID.randomUUID().toString();
        String buAction = row.getProperty("actionButton");
        String id = row.getProperty("id");
        String current_bu = row.getProperty("current_bu");
        String bu_remarks = row.getProperty("bu_remarks");
        String userAction = "";
        String historyStatus="";
        if (buAction.equalsIgnoreCase("Approved")) {
            userAction = "Closed";
            row.setProperty("is_revised", "yes");
            historyStatus="Request approved by BU Finanace and is fully closed";
        } else if (buAction.equalsIgnoreCase("Reject")) {
            userAction = "BURejected";
            row.setProperty("is_revised", "No");
            historyStatus="Request rejected by BU and is pending for completion";
        }
        
        row.setProperty("status", userAction);  
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
