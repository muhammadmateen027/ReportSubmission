/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sunway.rssdateloader.loadDataKPI;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.joget.apps.form.model.Element;
import org.joget.apps.form.model.FormData;
import org.joget.plugin.base.PluginWebSupport;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sunway.rssdateloader.databases.QueryHandler;
import org.sunway.rssdateloader.formdataloader.FormDataLoaderClass;
import org.sunway.rssdateloader.formdataloader.QueryHandlerInterface;
import org.sunway.rssdateloader.utilities.Utils;

/**
 *
 * @author kirthikan
 */
public class LoadProfileDataClass extends Element implements PluginWebSupport, QueryHandlerInterface{
    final String pluginName = "RSS - ManagerValue";
    final String version = "1.0.0";
    JSONArray jSONArray;
    QueryHandler qh = null;
    List<String> mgrIdList=null;
    List<String> mgrNameList=null;
    List<String> tlList=null;
    List<String> buFinanceList=null;

    PrintWriter out;
    String company;
    String subject;

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


    public void webService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        qh = new QueryHandler(this);
        company = request.getParameter("company");
        subject=request.getParameter("subject");
        Utils.showMsg("company = " + company);
        Utils.showMsg("subject = " + subject);
        out = response.getWriter();
        jSONArray = new JSONArray();
        if (!company.equalsIgnoreCase("") && !subject.equalsIgnoreCase("")) 
        {   
           Utils.showMsg("start");
           qh.getManagerData(company,subject);
           Utils.showMsg("done");

        } else {
            out.print(jSONArray);
        }

    }  
    
     public void onSuccess(ResultSet rSet) {
        JSONObject jSONObject = new JSONObject();
        mgrIdList = new ArrayList<String>();
        mgrNameList=new ArrayList<String>();
        tlList=new ArrayList<String>();
        buFinanceList=new ArrayList<String>();
         try {
              while (rSet.next()) {
                Utils.showMsg("Before getting object");
                mgrIdList.add(rSet.getString("c_manager_id"));
                Utils.showMsg("mgrList : "+mgrIdList);
                mgrNameList.add(rSet.getString("c_manager_names"));
                Utils.showMsg("mgrNameList : "+mgrNameList);
                tlList.add(rSet.getString("c_teamLead"));
                buFinanceList.add(rSet.getString("c_buFinance"));
            }
                jSONObject.put(Utils.managerId, mgrIdList);
                jSONObject.put(Utils.managerName, mgrNameList);
                jSONObject.put(Utils.teamLead, tlList);
                jSONObject.put(Utils.buFinance, buFinanceList);

                jSONArray.put(jSONObject);
        } catch (SQLException ex) {
            Logger.getLogger(FormDataLoaderClass.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(LoadProfileDataClass.class.getName()).log(Level.SEVERE, null, ex);
        }

        out.print(jSONArray);
    }

    public void onFailure() {
        Utils.showMsg("On Failure");
        out.print(jSONArray);
    }

    public void onHolidayCallBack(ResultSet rSet) {
            Utils.showMsg("onHolidayCallBack");

    }

 }
