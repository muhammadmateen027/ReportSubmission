/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sunway.rssdateloader.emailcomposers;

import java.sql.ResultSet;
import java.util.UUID;
import org.joget.apps.form.model.FormData;
import org.joget.apps.form.model.FormRow;
import org.joget.apps.form.model.FormRowSet;
import org.sunway.rssdateloader.databases.QueryHandler;
import org.sunway.rssdateloader.formdataloader.QueryHandlerInterface;
import org.sunway.rssdateloader.storebinder.OnCallBack;
import org.sunway.rssdateloader.utilities.Utils;

/**
 *
 * @pc omen
 * @author Mateen
 */
public class EmailClass implements QueryHandlerInterface {

    private FormRowSet rowSet;
    private QueryHandler qh;
    private FormData formData;

    public EmailClass(FormData formData, FormRowSet rowSet) {
        this.rowSet = rowSet;
        this.formData = formData;
        this.qh = new QueryHandler(this);
    }

    public void mainReqEmailComposer(String status) {
        Utils.showMsg("Inside Email Composer");
        FormRow row = rowSet.get(0);
        String uId = UUID.randomUUID().toString();
        String team_leader = row.getProperty("team_leader");
        String manager_id = row.getProperty("manager_id");
        String closing_month = row.getProperty("closing_month");
        String subject = row.getProperty("sub_id");
        String company = row.getProperty("company_id");
        String period_from = row.getProperty("period_from");
        String period_to = row.getProperty("period_to");
        String userName = row.getProperty("user_name");
        String refNo = row.getProperty("refNo");
        String remarks = row.getProperty("remarks");
        String userId = row.getProperty("username");
        String current_manager_name = row.getProperty("current_manager");

        String id = qh.getUniqueId(refNo);
//        String server = Utils.getEnvVar("fssrss", "rss_server");
        String server = "https://cloudappsdev.sunway.com.my";
        String link = "";

        if (!id.equalsIgnoreCase("")) {
            

            Utils.showMsg("Id: " + id);
            String emailSubject = "";
            String to = "";
            String cc = "";
            String message = "";

            if (status.equalsIgnoreCase("Completed")) {
                qh.updateHistoryLog(uId, id, userName, status, remarks);
                to = team_leader;
                link = Utils.getlink(server, "tl_approval", id, refNo);
                emailSubject = "FSSC Report Submission (" + subject + " From " + period_from + " To " + period_to + ") - "
                        + company + " has been completed by " + userName + ". Your approval is required.";
            } else if (status.equalsIgnoreCase("Draft")) {
                qh.updateHistoryLog(uId, id, userName, status, remarks);
                link = Utils.getlink(server, "manager_approval", id, refNo);
                to = manager_id;
                cc = team_leader;
                emailSubject = "Revised KPI timeline for " + closing_month + " " + subject + " " + company + " is pending for your approval.";
            } else if (status.equalsIgnoreCase("Rejected")) {
                
                to = qh.getUserEmail(userId);
                cc = team_leader;
                link = Utils.getlink(server, "myRequests", id, refNo);
                emailSubject = "Revised KPI timeline for " + closing_month + " " + subject + " " + company + " has been rejected by "+ current_manager_name;
            } else if (status.equalsIgnoreCase("Approved")) {
                to = qh.getUserEmail(userId);
                cc = team_leader;
                link = Utils.getlink(server, "myRequests", id, refNo);
                emailSubject = "Revised KPI timeline for " + closing_month + " " + subject + " " + company + " has been approved by "+ current_manager_name;
            }

            message = "Dear Sir / Madam, \n\n"
                    + "Please click the reference number to open the document :" + link
                    + Utils.emailFooter();

            if (to.equalsIgnoreCase("") || to == null) {
                Utils.showMsg("No email");
            } else Utils.composeEmail("", to, cc, emailSubject, message);

            Utils.showMsg("Email Sent");
        }

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
