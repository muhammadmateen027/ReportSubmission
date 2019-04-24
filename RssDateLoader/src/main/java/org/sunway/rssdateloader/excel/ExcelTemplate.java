/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sunway.rssdateloader.excel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joget.apps.form.model.Element;
import org.joget.apps.form.model.FormData;
import org.joget.plugin.base.PluginWebSupport;
import org.json.JSONArray;
import org.sunway.rssdateloader.databases.QueryHandler;
import org.sunway.rssdateloader.formdataloader.QueryHandlerInterface;
import org.sunway.rssdateloader.models.Model;
import org.sunway.rssdateloader.utilities.Utils;
import sun.rmi.server.Util;

/**
 *
 * @author kirthikan
 */
public class ExcelTemplate extends Element implements PluginWebSupport, QueryHandlerInterface {

    final String pluginName = "RSS - ExcelTemplate";
    final String version = "1.0";
    JSONArray jSONArray;
    List<String> subList = null;
    List<String> envList = null;

    //PrintWriter out;
    @Override
    public String renderTemplate(FormData fd, Map map) {
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

    public void webService(HttpServletRequest request, HttpServletResponse response) throws IOException, FileNotFoundException {

        Utils.showMsg("excel creation");
        String filename = "RSS-" + new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date()) + ".xlsx";
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        QueryHandler qh = new QueryHandler(this);
        String query = "";

        Utils.showMsg("0");
        String closingDate = request.getParameter("closingDate");
        closingDate = "04-2019";

        List<String> intStatus = getStatus("internal");
        List<String> extStatus = getStatus("external");

        if (!closingDate.equalsIgnoreCase("")) {
            query = "select distinct c_sub_id as col from app_fd_rss_request_detail Where c_close_mnth =  ?";
            subList = qh.getInfo(query, closingDate);
            Utils.showMsg("a");
            query = "select distinct c_env as col from app_fd_rss_request_detail Where c_close_mnth = ? ";
            envList = qh.getInfo(query, closingDate);
            Utils.showMsg("b");
            List<Model> actualData = qh.getKPITasksByMonth(closingDate);
            Utils.showMsg("c");
            if (actualData.size() != 0) {
                for (int i = 0; i < subList.size(); i++) {
                    Utils.showMsg("1");
                    for (int j = 0; j < envList.size(); j++) {
                        int intCount = 0;
                        int extCount = 0;
                        Utils.showMsg("2");
                        for (int k = 0; k < actualData.size(); k++) {
                            Utils.showMsg("3");

                            for (int l = 0; l < intStatus.size(); l++) {
                                Utils.showMsg("4");
                                if (actualData.get(k).getEnv().equalsIgnoreCase(envList.get(j))) {
                                    Utils.showMsg("5");
                                    if (actualData.get(k).getSubject().equalsIgnoreCase(subList.get(i))) {
                                        Utils.showMsg("6");
                                        if (actualData.get(k).getIntKpiStatus().equalsIgnoreCase(intStatus.get(l).toString())) {
                                            Utils.showMsg("7");
                                            intCount++;
                                            createList(actualData.get(k), 1);
                                            Utils.showMsg("Subj: " + actualData.get(k).getSubject()
                                                    + "Env: " + actualData.get(k).getEnv()
                                                    + " Internal: " + intStatus.get(l) + " : count: " + String.valueOf(intCount));
                                        }
                                    }

                                }
                            }

//                            for (int m = 0; m < extStatus.length; m++) {
//                                Utils.showMsg("8");
//                                if (actualData.get(k).getEnv().equalsIgnoreCase(envList.get(j))
//                                        && actualData.get(k).getSubject().equalsIgnoreCase(subList.get(i))
//                                        && actualData.get(k).getIntKpiStatus().equalsIgnoreCase(intStatus[m])) {
//                                    extCount++;
//
//                                    Utils.showMsg("External: " + intStatus[m] + " : count: " + String.valueOf(extCount));
//                                }
//                            }
                            Utils.showMsg("Internal Count: " + String.valueOf(intCount));
                            Utils.showMsg("External Count: " + String.valueOf(extCount));
                        }

                        Utils.showMsg("Int Count: " + String.valueOf(intCount));
                        Utils.showMsg("Ext Count: " + String.valueOf(extCount));
                    }

                }
            } else {
                Utils.showMsg("Helllooo ");
            }

//            for(int i=0; i < intenalStatus.length ; i++) {
//                
//            }
//            for(int i=0; i < subJectList.size(); i++)
//            if (data.size() != 0) {
//                for (int i=0; i<data.size(); i++) {
//                    if (data.get(i).getIntKpiStatus().equalsIgnoreCase("TLExceed"))
//                }
//            }
        }

    }
    
    private void createList(Model model, int count) {
        if (subList != null && envList != null) {
            
        }
    }

    private List<String> getStatus(String type) {
        List<String> list = new ArrayList<String>();
        if (type.equalsIgnoreCase("internal")) {
            list.add("Preparer Exceed");
            list.add("Preparer Meet");
            list.add("Preparer Delay");
        } else {
            list.add("TL Exceed");
            list.add("TL Meet");
            list.add("TL Delay");
        }
        return list;
    }

    private void createWorkBook(HttpServletResponse response, Model actualData) {
        
        ServletOutputStream outStream;
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Users List");
        Row headingRow = sheet.createRow(0);

        Map< String, Object[]> empinfo;
        empinfo = new TreeMap< String, Object[]>();
        
        empinfo.put("1", new Object[]{
            "EMP ID", "EMP NAME", "DESIGNATION"});

        empinfo.put("2", new Object[]{
            "tp01", "Gopal", "Technical Manager"});

        empinfo.put("3", new Object[]{
            "tp02", "Manisha", "Proof Reader"});

        empinfo.put("4", new Object[]{
            "tp03", "Masthan", "Technical Writer"});

        empinfo.put("5", new Object[]{
            "tp04", "Satish", "Technical Writer"});

        empinfo.put("6", new Object[]{
            "tp05", "Krishna", "Technical Writer"});
        Set< String> keyid = empinfo.keySet();
        int rowid = 0;

        for (String key : keyid) {
            headingRow = sheet.createRow(rowid++);
            Object[] objectArr = empinfo.get(key);
            int cellid = 0;

            for (Object obj : objectArr) {
                Cell cell = headingRow.createCell(cellid++);
                cell.setCellValue((String) obj);
            }
        }

        try {
            outStream = response.getOutputStream();
            workbook.write(outStream);
            Utils.showMsg("workbook ceation");
            outStream.close();
        } catch (FileNotFoundException ex) {
            Utils.showMsg(ex.getMessage());
        } catch (IOException ex) {
            Utils.showMsg(ex.getMessage());
        }
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
