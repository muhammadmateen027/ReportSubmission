/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sunway.rssdateloader.emailreminder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joget.apps.app.service.AppUtil;
import org.joget.plugin.base.DefaultApplicationPlugin;
import org.sunway.rssdateloader.databases.QueryHandler;
import org.sunway.rssdateloader.formdataloader.QueryHandlerInterface;
import org.sunway.rssdateloader.utilities.Utils;

/**
 *
 * @pc omen
 * @author Mateen
 */
public class DailyReminder extends DefaultApplicationPlugin implements QueryHandlerInterface {

    final String pluginName = "RSS - Daily Reminder";

    public String getName() {
        return pluginName;
    }

    public String getVersion() {
        return "1.0";
    }

    public String getDescription() {
        return pluginName;
    }

    public String getLabel() {
        return getName();
    }

    public String getClassName() {
        return getClass().getName();
    }

    public String getPropertyOptions() {
        return AppUtil.readPluginResource(getClass().getName(), "/properties/emailConfig.json", null, true, "message/emailConfig");
    }

    @Override
    public Object execute(Map map) {
        QueryHandler qh = new QueryHandler(this);
        qh.getManagerPendingApproval();
        
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	Date date = new Date();
        
        String query = "";
        
        query = "Select c_manager_id from app_fd_rss_request_detail Where"
                + " c_f_int_date = DATE_FORMAT(CURDATE(), \"%d-%m-%Y\") AND c_status != ? ";
        List<String> managers = qh.getMonitoringEmail(query);
        
        String link = "<a href=https://cloudappsdev.sunway.com.my/jw/web/userview/fssrss/rss_home_page/_/internal_kpi_by_date target=_blank> Click here. </a>";
        
        String Subject = "KPI Monitoring Email for target Internal Date [" + dateFormat.format(date)+"]";
        
        String message = "Dear Sir/Madam, <br> Please click here to view the pending completion Target Internal Kpi Date =>" + link;
        if(managers.size() > 0) {
            for (int i=0; i<managers.size(); i++) {
                Utils.composeEmail("", managers.get(i), "", Subject, message);
            }
        }
        
        query = "Select c_manager_id from app_fd_rss_request_detail Where"
                + " c_f_ext_date = DATE_FORMAT(CURDATE(), \"%d-%m-%Y\") AND c_status != ? ";
        managers = qh.getMonitoringEmail(query);
        link = "<a href=https://cloudappsdev.sunway.com.my/jw/web/userview/fssrss/rss_home_page/_/external_kpi_by_date target=_blank> Click here. </a>";
        Subject = "KPI Monitoring Email for target External Date [" + dateFormat.format(date)+"]";
        message = "Dear Sir/Madam, <br> Please click here to view the pending completion Target External Kpi Date =>" + link;
        if(managers.size() > 0) {
            for (int i=0; i<managers.size(); i++) {
                Utils.composeEmail("", managers.get(i), "", Subject, message);
            }
        }
        return null;
    }

    @Override
    public void onSuccess(ResultSet rSet) {
        String manager = "";
        String managerName = "";
        try {
            while (rSet.next()) {
                manager = rSet.getString("c_manager_id");
                
                managerName = rSet.getString("c_manager_name");
                
                sendEmail(manager, managerName);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MonthlyReminder.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void onFailure() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onHolidayCallBack(ResultSet rSet) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void sendEmail(String to, String emailNames) {
        String link = "https://cloudappsdev.sunway.com.my/jw/web/userview/fssrss/rss_home_page/_/revised_pending_list";
        String subject = "Pending Revise Timeline Approval for FSSC Report - Your action is required";
        String message = "Dear "+ emailNames+", <br>"
            +"Kindly click the following link to open the document list pending for your action => "+ link;
        
        Utils.composeEmail("", to, "", subject, message);
    }

}
