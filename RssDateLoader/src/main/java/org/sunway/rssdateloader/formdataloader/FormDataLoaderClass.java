/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sunway.rssdateloader.formdataloader;

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
public class FormDataLoaderClass extends Element implements PluginWebSupport, QueryHandlerInterface {

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
    //changes for revise target --> rssreviselogic
    String revinternal_wd = "";
    String revexternal_wd = "";//-->ends

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
        //changes for revise target --> rssreviselogic
        revinternal_wd = request.getParameter("revIntWd");
        revexternal_wd = request.getParameter("revExtWd");
        //-->ends
        out = response.getWriter();
        jSONArray = new JSONArray();

        Utils.showMsg("company = " + company);
        Utils.showMsg("subject = " + subject);
        Utils.showMsg("period_to = " + period_to);
        //changes for revise target --> rssreviselogic
        Utils.showMsg("revinternal_wd = " + revinternal_wd);
        Utils.showMsg("revexternal_wd = " + revexternal_wd);
        //-->ends
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
                Logger.getLogger(FormDataLoaderClass.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (!phList.isEmpty() && qh != null) {
            getKPIProfileData();
        }
    }

    private void getKPIProfileData() {
        String[] dateArray = period_to.split("-");
        month = Utils.getMonthIdFromDate(period_to);

        try {

            qh.getKPIData(company, subject, dateArray[2], month);
        } catch (JSONException ex) {
            Logger.getLogger(FormDataLoaderClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onSuccess(ResultSet rSet) {
        try {
            while (rSet.next()) {
                Utils.showMsg("Before getting object");
                jSONArray.put(getArrayFromObject(rSet, month, period_to));
            }

        } catch (SQLException ex) {
            Logger.getLogger(FormDataLoaderClass.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(FormDataLoaderClass.class.getName()).log(Level.SEVERE, null, ex);
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
        String revIntDate = "";
        String revExtDate = "";
        String workDays = rSet.getString(wdMonth);
        float numbers = Float.parseFloat(workDays);
        float revIntWd;
        float revExtWd;
        int revInt_wd = 0;
        int revExt_wd = 0;
        //changes for revise target --> rssreviselogic
        if ((revexternal_wd != null && !revexternal_wd.isEmpty()) && (revexternal_wd != null && !revexternal_wd.isEmpty())) {
            Utils.showMsg("calculate wd");
            revIntWd = Float.parseFloat(revinternal_wd);
            revExtWd = Float.parseFloat(revexternal_wd);
            revInt_wd = (int) revIntWd;
            revExt_wd = (int) revExtWd;
        }
        //-->ends
        int noOfDays = (int) numbers;
        String kpi_target = rSet.getString("c_kpi_target");

        if (!kpi_target.equalsIgnoreCase("") || kpi_target != null) {
            Utils.showMsg("kpi_target:  " + kpi_target);
            if (kpi_target.equalsIgnoreCase("internal")) {
                kpiDate = Utils.getDateFromWD(Utils.arrangedDate(periodToDate), noOfDays, phList);
                //changes for revise target --> rssreviselogic
                if ((revinternal_wd != null && !revinternal_wd.isEmpty()) && revInt_wd != 0) {
                    Utils.showMsg("revIntDate:  " + revinternal_wd);
                    revIntDate = Utils.getDateFromWD(Utils.arrangedDate(periodToDate), revInt_wd, phList);
                    jSONObject.put(Utils.revInternalDate, Utils.arrangedDate(revIntDate));
                } //-->ends

                Utils.showMsg("Getting kpiDate in internal:   " + kpiDate);

                jSONObject.put(Utils.internalDate, Utils.arrangedDate(kpiDate));
                jSONObject.put(Utils.internalWD, String.valueOf(noOfDays));
            } else if (kpi_target.equalsIgnoreCase("external")) {
                kpiDate = Utils.getDateFromWD(Utils.arrangedDate(periodToDate), noOfDays, phList);
                //changes for revise target --> rssreviselogic
                if ((revexternal_wd != null && !revexternal_wd.isEmpty()) && revExt_wd != 0) {
                    Utils.showMsg("revExtDate:  " + revexternal_wd);
                    revExtDate = Utils.getDateFromWD(Utils.arrangedDate(periodToDate), revExt_wd, phList);
                    jSONObject.put(Utils.revExternalDate, Utils.arrangedDate(revExtDate));
                } //-->ends
                Utils.showMsg("Getting kpiDate in external:   " + kpiDate);

                jSONObject.put(Utils.externalDate, Utils.arrangedDate(kpiDate));
                jSONObject.put(Utils.externalWD, String.valueOf(noOfDays));
            }

            jSONObject.put(Utils.environment, rSet.getString("c_environment_id"));
        }

        return jSONObject;
    }

}
