/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sunway.rssdateloader.schedular;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joget.apps.app.service.AppUtil;
import org.joget.plugin.base.DefaultApplicationPlugin;
import org.joget.workflow.model.service.WorkflowManager;
import org.sunway.rssdateloader.databases.QueryHandler;
import org.sunway.rssdateloader.formdataloader.QueryHandlerInterface;
import org.sunway.rssdateloader.models.Model;
import org.sunway.rssdateloader.utilities.Utils;

/**
 *
 * @pc omen
 * @author Mateen
 */
public class WithdrawSchedular extends DefaultApplicationPlugin implements QueryHandlerInterface {

    final String pluginName = "RSS - Stop Process Schedular";
    String actionTime = "";

    public String getName() {
        return pluginName;
    }

    public String getVersion() {
        return "1.0.0";
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
    public Object execute(Map props) {
        QueryHandler qh = new QueryHandler(this);
        List<Model> listRec = qh.getPendingRecords();
        String uId = UUID.randomUUID().toString();
        
        Utils.showMsg("==> Inside Schedular");

        for (int i = 0; i < listRec.size(); i++) {
            Utils.showMsg("1");
            try {
                boolean isValid = getDateValidation(listRec.get(0).getActionTime());
                Utils.showMsg("2" + String.valueOf(isValid));

                if (isValid) {
                    Utils.showMsg("3");
                    qh.updateRecordStatusById(listRec.get(0).getId());
                    stopProcess(qh, listRec.get(0).getId());
                    
                    qh.updateHistoryLog(uId, listRec.get(0).getId(), "System Action", "Auto Closed", "");
                } else{
                    Utils.showMsg("4");
                }
            } catch (ParseException ex) {
                Logger.getLogger(WithdrawSchedular.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return null;
    }

    private void stopProcess(QueryHandler qh, String id) {
        String processId = qh.getProcessIdFromFormId(id);

        if (!processId.equalsIgnoreCase("")) {
            id = processId;
        }

        WorkflowManager wm = (WorkflowManager) AppUtil.getApplicationContext().getBean("workflowManager");
        wm.processAbort(id);
    }

    private boolean getDateValidation(String mDate) throws ParseException {
        boolean isValid = false;
        DateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = formater.parse(formater.format(new Date()));
        Date validDate = formater.parse(mDate);

        Calendar c = Calendar.getInstance();
        c.setTime(validDate);
        c.add(Calendar.DAY_OF_MONTH, 7);

        String formattedDate = formater.format(c.getTime());

        System.out.println("New date: " + formattedDate);
        System.out.println("Current date: " + currentDate);

        validDate = formater.parse(formater.format(c.getTime()));
        if (currentDate.equals(validDate) || currentDate.after(validDate)) {
            isValid = true;
        }
        return isValid;
    }

    @Override
    public void onSuccess(ResultSet rSet) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onFailure() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onHolidayCallBack(ResultSet rSet) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
