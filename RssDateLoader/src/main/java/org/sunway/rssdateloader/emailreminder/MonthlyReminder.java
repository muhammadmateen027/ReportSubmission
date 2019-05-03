/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sunway.rssdateloader.emailreminder;

import java.sql.ResultSet;
import java.sql.SQLException;
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
public class MonthlyReminder extends DefaultApplicationPlugin implements QueryHandlerInterface {

    final String pluginName = "RSS - Monthly Reminder";

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
        qh.getMonthlyData();
        return null;
    }

    @Override
    public void onSuccess(ResultSet rSet) {
        String manager = "";
        String managerName = "";
        String tl = "";
        String tlName = "";
        try {
            while (rSet.next()) {
                manager = rSet.getString("c_manager_id");
                tl = rSet.getString("c_team_leader");
                
                managerName = rSet.getString("c_manager_name");
                tlName = rSet.getString("c_tlName");
                
                String to = manager+";"+tl;
                String emailNames = managerName+","+tlName;
                sendEmail(to, emailNames);
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
        String cc = "yvonnewyw@sunway.com.my";
        String link ="<a href=https://cloudappsdev.sunway.com.my/jw/web/userview/fssrss/rss_home_page/_/delay_completed_list target=_blank> Click here. </a>";
        String subject = "Delay Completed - FSSC Report Submission";
        String message = "Dear "+ emailNames+", <br>"
            +"Kindly click the following link to view the delay completed => "+ link;
        
        Utils.composeEmail("", to, cc, subject, message);
    }

}
