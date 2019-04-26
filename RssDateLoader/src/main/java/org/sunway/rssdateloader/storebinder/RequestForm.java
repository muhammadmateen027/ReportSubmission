/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sunway.rssdateloader.storebinder;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joget.apps.form.model.FormData;
import org.joget.apps.form.model.FormRow;
import org.joget.apps.form.model.FormRowSet;
import org.sunway.rssdateloader.databases.QueryHandler;
import org.sunway.rssdateloader.emailcomposers.EmailClass;
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

        EmailClass ec = new EmailClass(formData, rowSet);

        if (buttonAction.equalsIgnoreCase("Submit to Manager")) {

            String rev_int_wd = row.getProperty("rev_internal_wd");
            String rev_int_date = row.getProperty("rev_int_date");
            String rev_ext_wd = row.getProperty("rev_external_wd");
            String rev_ext_date = row.getProperty("rev_ext_date");

            row.setProperty("f_int_wd", rev_int_wd);
            row.setProperty("f_int_date", rev_int_date);
            row.setProperty("f_ext_wd", rev_ext_wd);
            row.setProperty("f_ext_date", rev_ext_date);

            ec.mainReqEmailComposer(revise_status);
            qh.updateHistoryLog(uId, id, current_tl, revise_status, rev_remarks);
        } else {
            Utils.showMsg("===>>  else loop of 1");
        }
    }

    public void createRequestForm() {
        FormRow row = rowSet.get(0);
        String int_date = row.getProperty("int_date");
        String kpiStatus = "";
        Utils.showMsg("1");
        try {
            kpiStatus = getKpiStatus(int_date);
            Utils.showMsg("2 : " +kpiStatus);
            row.setProperty("int_kpi_status", kpiStatus);
            Utils.showMsg("3");
        } finally {
            if (kpiStatus.equalsIgnoreCase("")) {
                Utils.showMsg("4");
                kpiStatus = getKpiStatus(int_date);
                Utils.showMsg("5");
                row.setProperty("int_kpi_status", kpiStatus);
                Utils.showMsg("6");
            }
        }
    }

    private String getKpiStatus(String internalDate) {
        String kpiStatus = "";
        try {
            DateFormat formater = new SimpleDateFormat("dd-MM-yyyy");
            Date currentDate = formater.parse(formater.format(new Date()));
            Date validDate = formater.parse(internalDate);

            if (currentDate.equals(validDate)) {
                kpiStatus = "Preparer Meet";
            } else if (currentDate.compareTo(validDate) < 0) {
                kpiStatus = "Preparer Exceed";
            } else if (currentDate.compareTo(validDate) > 0) {
                kpiStatus = "Preparer Delay";
            }
        } catch (ParseException ex) {
            Logger.getLogger(RequestForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        Utils.showMsg("Internal KPI Status: "+kpiStatus);
        return kpiStatus;
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
