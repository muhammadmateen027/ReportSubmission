/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sunway.rssdateloader.storebinder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joget.apps.form.model.Element;
import org.joget.apps.form.model.FormData;
import org.joget.apps.form.model.FormRow;
import org.joget.apps.form.model.FormRowSet;
import org.sunway.rssdateloader.databases.QueryHandler;
import org.sunway.rssdateloader.formdataloader.FormDataLoaderClass;
import org.sunway.rssdateloader.formdataloader.QueryHandlerInterface;
import org.sunway.rssdateloader.utilities.Utils;

/**
 *
 * @pc omen
 * @author Mateen
 */
public class CompanyEnvironmentClass implements QueryHandlerInterface {

    FormRowSet rowSet;
    FormData formData;
    String elementId;
    
    public OnCallBack onCallBack;

    public CompanyEnvironmentClass(FormRowSet rowSet, FormData formData, String elementId, OnCallBack onCallBack) {
        this.rowSet = rowSet;
        this.formData = formData;
        this.elementId = elementId;
        this.onCallBack = onCallBack;
    }

    public void companyEnvironmentExistance() {
        FormRow row = rowSet.get(0);
        String comapny_id = row.getProperty("comapny_id");
        String env_id = row.getProperty("env_id");

        if (!comapny_id.equalsIgnoreCase("") && !env_id.equalsIgnoreCase("")) {
            QueryHandler qh = new QueryHandler(this);
            qh.getEnvCompExistance(comapny_id);
        }

    }

    public void onSuccess(ResultSet rSet) {
        String isExists = "";
        if (rSet != null) {
            try {
                while (rSet.next()) {
                    isExists = rSet.getString("existance").toString();
                }
            } catch (SQLException ex) {
                Logger.getLogger(FormDataLoaderClass.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        Utils.showMsg(isExists);
        if (!isExists.equalsIgnoreCase("") && !isExists.equalsIgnoreCase("0")) {
            Utils.showError(formData, elementId, "Company has already Environment.");
            onCallBack.onFailure();
        } else onCallBack.onSuccess();
    }

    public void onFailure() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void onHolidayCallBack(ResultSet rSet) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
