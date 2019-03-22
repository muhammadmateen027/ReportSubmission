/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sunway.rssdateloader;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.joget.apps.form.model.Element;
import org.joget.apps.form.model.FormData;
import org.joget.plugin.base.PluginWebSupport;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sunway.rssdateloader.databases.QueryHandler;
import org.sunway.rssdateloader.utilities.Utils;

/**
 *
 * @pc omen
 * @author Mateen
 */
public class PluginMainClass extends Element implements PluginWebSupport, QueryHandlerInterface {

    final String pluginName = "RSS - DateLoader";
    final String version = "2.0.0";
    PrintWriter out;
    JSONArray jSONArray;
    QueryHandler qh = null;
    List<String> phList = null;

    String company;
    String subject;
    String period_to;
    String month;
    

    @Override
    public String renderTemplate(FormData formData, Map dataModel) {
        return "";
    }

    public String getName() {
        return pluginName;
    }

    public String getVersion() {
        return version;
    }

    public String getDescription() {
        return pluginName;
    }

    public String getLabel() {
        return pluginName;
    }

    public String getClassName() {
        return this.getClass().getName();
    }

    public String getPropertyOptions() {
        return "";
    }

    public void webService(HttpServletRequest request, HttpServletResponse response) throws IOException {
        company = request.getParameter("company");
        subject = request.getParameter("subject");
        period_to = request.getParameter("period_to");
        out = response.getWriter();
        jSONArray = new JSONArray();

        Utils.showMsg("company = " + company);
        Utils.showMsg("subject = " + subject);
        Utils.showMsg("period_to = " + period_to);

        if (!company.equalsIgnoreCase("") && !subject.equalsIgnoreCase("") && !period_to.equalsIgnoreCase("")) {
            qh = new QueryHandler(this);

            qh.getPublicHolidays();

        } else {
            out.print(jSONArray);
        }

    }
    
    public void onHolidayCallBack(ResultSet rSet) {
        phList = new ArrayList<String>();
        if (rSet != null && phList != null) {
            try {
                while (rSet.next()) {
                    String unsortDate = rSet.getString("c_holiday_date");
                    String sortedDate = Utils.arrangedDate(unsortDate);
                    phList.add(sortedDate);
                }
            } catch (SQLException ex) {
                Logger.getLogger(PluginMainClass.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if (phList.size() != 0 && qh != null) {
            getKPIProfileData();
        }
    }

    private void getKPIProfileData() {
        String[] dateArray = period_to.split("-");
        month = Utils.getMonthIdFromDate(period_to);

        try {
            qh.getKPIData(company, subject, dateArray[2], month);
        } catch (JSONException ex) {
            Logger.getLogger(PluginMainClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onSuccess(ResultSet rSet) {
        try {
            while (rSet.next()) {
                Utils.showMsg("Before getting object");
                jSONArray.put(getArrayFromObject(rSet, month, period_to));
            }

        } catch (SQLException ex) {
            Logger.getLogger(PluginMainClass.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(PluginMainClass.class.getName()).log(Level.SEVERE, null, ex);
        }

        out.print(jSONArray);
    }

    public void onFailure() {
        Utils.showMsg("On Failure");
        out.print(jSONArray);
    }

    private JSONObject getArrayFromObject(ResultSet rSet, String wdMonth, String periodToDate) throws JSONException, SQLException {
        JSONObject jSONObject = new JSONObject();
        String kpiDate = "";
        String workDays = rSet.getString(wdMonth);

        float numbers = Float.parseFloat(workDays);
        int noOfDays = (int) numbers;
        String kpi_target = rSet.getString("c_kpi_target");

        if (!kpi_target.equalsIgnoreCase("") || kpi_target != null) {
            Utils.showMsg("kpi_target:  " + kpi_target);
            if (kpi_target.equalsIgnoreCase("internal")) {

                kpiDate = Utils.getDateFromWD(Utils.arrangedDate(periodToDate), noOfDays, phList);

                Utils.showMsg("Getting kpiDate in internal:   " + kpiDate);

                jSONObject.put(Utils.internalDate, Utils.arrangedDate(kpiDate));
                jSONObject.put(Utils.internalWD, String.valueOf(noOfDays));
            } else if (kpi_target.equalsIgnoreCase("external")) {
                kpiDate = Utils.getDateFromWD(Utils.arrangedDate(periodToDate), noOfDays, phList);
                Utils.showMsg("Getting kpiDate in external:   " + kpiDate);

                jSONObject.put(Utils.externalDate, Utils.arrangedDate(kpiDate));
                jSONObject.put(Utils.externalWD, String.valueOf(noOfDays));
            }

            jSONObject.put(Utils.environment, rSet.getString("c_environment_id"));
        }

        return jSONObject;
    }

    
}
