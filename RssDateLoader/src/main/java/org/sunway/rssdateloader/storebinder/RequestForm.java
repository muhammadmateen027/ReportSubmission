/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sunway.rssdateloader.storebinder;

import java.sql.ResultSet;
import org.joget.apps.form.model.FormRow;
import org.joget.apps.form.model.FormRowSet;
import org.sunway.rssdateloader.databases.QueryHandler;
import org.sunway.rssdateloader.formdataloader.QueryHandlerInterface;

/**
 *
 * @pc omen
 * @author Mateen
 */
public class RequestForm implements QueryHandlerInterface {

    private FormRowSet rowSet;
    private QueryHandler qh;

    public RequestForm(FormRowSet rowSet) {
        this.rowSet = rowSet;
        this.qh = new QueryHandler(this);
    }

    public void performAction() {
        FormRow row = rowSet.get(0);
        String internal_wd = row.getProperty("internal_wd");
        String internal_date = row.getProperty("internal_date");
        
        String external_wd = row.getProperty("external_wd");
        String external_date = row.getProperty("external_date");
        
        row.setProperty("final_int_wd", internal_wd);
        row.setProperty("final_int_date", internal_date);
        
        row.setProperty("final_ext_wd", external_wd);
        row.setProperty("final_ext_date", external_date);
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