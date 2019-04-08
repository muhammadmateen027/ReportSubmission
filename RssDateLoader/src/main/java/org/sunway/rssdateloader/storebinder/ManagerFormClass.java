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
public class ManagerFormClass implements QueryHandlerInterface{

    private FormRowSet rowSet;
    private QueryHandler qh;

    public ManagerFormClass(FormRowSet rowSet) {
        this.rowSet = rowSet;
        this.qh = new QueryHandler(this);
    }

    public void formAction() {

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
        
        row.setProperty("revise_status", userAction);  
        qh.updateHistoryLog(uId, id, current_manager, userAction+" KPI revision request", manager_remarks);

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
