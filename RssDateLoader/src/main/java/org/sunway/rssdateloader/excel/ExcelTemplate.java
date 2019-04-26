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
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
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
<<<<<<< HEAD
    List<String> mgrList=null;
    Model dummy=new Model();
    List<Model> dummyList=new ArrayList<Model>();
=======
>>>>>>> c02858cec5e36b146374ae2774fba030e5ba7366

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
<<<<<<< HEAD
        Utils.showMsg("0");
        ServletOutputStream outStream;
        String closingDate = request.getParameter("closingDate");
        closingDate = "04-2019";
        List<Model> excelEnvRows= createEnvSheet(closingDate);
        List<Model> excelMgrRows=createManagerSheet(closingDate);
        
        
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("By Environment");
        XSSFSheet sheet1 = workbook.createSheet("By Manager");
=======
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
>>>>>>> c02858cec5e36b146374ae2774fba030e5ba7366

        // To create Heading
//        String[] headings = {"KPI Status", "KPI Tasks", "Environment", "Count"};
        String[] headings = {"KPI Status", "KPI Tasks", "TestEnv","PI"};
        String[] headings1={"KPI Status","KPI Tasks", "TRAIN USER1 ,FAIZ RASHIDI"};
        Row headingRow = sheet.createRow(0);
        Row headingRow1=sheet1.createRow(0);
        int columnCount = 0;
        for (String col : headings) {
            Cell cell = headingRow.createCell(columnCount);
            cell.setCellValue(col);
            setFontStyle(cell, workbook);
            sheet.setColumnWidth(columnCount, 9000);
            columnCount++;
        }
        int columnCount1 = 0;
        for (String col : headings1) {
            Cell cell = headingRow1.createCell(columnCount1);
            cell.setCellValue(col);
            setFontStyle(cell, workbook);
            sheet1.setColumnWidth(columnCount1, 9000);
            columnCount1++;
        }
Utils.showMsg("column count done");
        // to create data in sheet rows
        int rowCount = 1;
        for (Model excelRow : excelEnvRows) {
            Row row = sheet.createRow(rowCount);
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(excelRow.getKpiStatus());
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(excelRow.getSubject());  
            if(excelRow.getEnv().equalsIgnoreCase("TestEnv")){
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(excelRow.getCount());
            }else if(excelRow.getEnv().equalsIgnoreCase("PI")){
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(excelRow.getCount());}
            rowCount++;
//            Cell cell2 = row.getCell(2);
//            if (cell2 == null)
//            cell2 = row.createCell(2);
//            Cell cell3 = row.getCell(3);
//            if(cell3 == null)
//            cell3 = row.createCell(3);
//            if(excelRow.getEnv().equalsIgnoreCase("TestEnv")){
//            cell2.setCellValue(excelRow.getCount());
//            }else if(excelRow.getEnv().equalsIgnoreCase("PI")){
//            cell3.setCellValue(excelRow.getCount());
//            }
//            Utils.showMsg("checking---------->"+cell0.toString());
//            for(int r=0;r<excelEnvRows.size();r++){
//                if((excelEnvRows.get(r).getKpiStatus().equalsIgnoreCase(excelRow.getKpiStatus())
//                        &&excelEnvRows.get(r).getSubject().equalsIgnoreCase(excelRow.getSubject())
//                        && !(excelEnvRows.get(r).getEnv().equalsIgnoreCase(excelRow.getEnv())))){
//                        Utils.showMsg(excelRow.getKpiStatus()+"------------>"+excelRow.getSubject()+"------->"+excelRow.getEnv());
//                        rowCount=rowCount-6;
//                }else{
//                        rowCount++;
//                        break;
//                }
//
//            }
                
         }
        Utils.showMsg("row count done");
        
        int rowCount1 = 1;
        for (Model excelRow : excelMgrRows) {
            Row row = sheet1.createRow(rowCount1);
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(excelRow.getKpiStatus());
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(excelRow.getSubject());
            if(excelRow.getManager().equalsIgnoreCase("TRAIN USER1 ,FAIZ RASHIDI")){
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(excelRow.getCount());
            }else if(excelRow.getEnv().equalsIgnoreCase("PI")){
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(excelRow.getCount());
            }
            Utils.showMsg("checking---------->"+cell0.toString());
            rowCount1++;
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
    

<<<<<<< HEAD
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

    private List<Model> createEnvSheet(String closingDate) {
        QueryHandler qh = new QueryHandler(this);
        String query = "";

        List<String> intStatus = getStatus("internal");
        List<String> extStatus = getStatus("external");
        List<Model> excelList = new ArrayList<Model>();


        if (!closingDate.equalsIgnoreCase("")) {
            query = "select distinct c_sub_id as col from app_fd_rss_request_detail Where c_close_mnth =  ?";
            subList = qh.getInfo(query, closingDate);
            Utils.showMsg("a");
            query = "select distinct c_env as col from app_fd_rss_request_detail Where c_close_mnth = ? ";
            envList = qh.getInfo(query, closingDate);
            Utils.showMsg("b");
            List<Model> actualData = qh.getKPITasksByMonth(closingDate);
            Utils.showMsg("c");
            if (!actualData.isEmpty()) {
                for (int i = 0; i < subList.size(); i++) {
                    Utils.showMsg("1");
                    for (int j = 0; j < envList.size(); j++) {
                        Utils.showMsg("2");
                        for (int k = 0; k < intStatus.size(); k++) {
                            Utils.showMsg("3");
                            int intCount = 0;
                            Model mod = new Model();
                            for (int l = 0; l < actualData.size(); l++) {
                                Utils.showMsg("4");
                                if (actualData.get(l).getEnv().equalsIgnoreCase(envList.get(j))) {
                                    Utils.showMsg("5");
                                    if (actualData.get(l).getSubject().equalsIgnoreCase(subList.get(i))) {
                                        Utils.showMsg("6");
                                        Utils.showMsg(actualData.get(l).getIntKpiStatus());
                                        Utils.showMsg(intStatus.get(k));
                                        if (actualData.get(l).getIntKpiStatus().equalsIgnoreCase(intStatus.get(k))) {
                                            Utils.showMsg("7");
                                            intCount++;
                                            Utils.showMsg("Subj: " + actualData.get(l).getSubject()
                                                    + "Env: " + actualData.get(l).getEnv()
                                                    + " Internal: " + intStatus.get(k) + " : count: " + String.valueOf(intCount));
                                        }
                                    }

                                }
                            }
                            mod.setKpiStatus(intStatus.get(k));
                            mod.setSubject(subList.get(i));
                            mod.setEnv(envList.get(j));
                            mod.setCount(intCount);
                            excelList.add(mod);

                            Utils.showMsg("Internal Count: " + String.valueOf(intCount));
                        }
                        for (int m = 0; m < extStatus.size(); m++) {
                            Utils.showMsg("33");
                            int extCount = 0;
                            Model mod = new Model();
                            for (int l = 0; l < actualData.size(); l++) {
                                Utils.showMsg("44");
                                if (actualData.get(l).getEnv().equalsIgnoreCase(envList.get(j))) {
                                    Utils.showMsg("55");
                                    if (actualData.get(l).getSubject().equalsIgnoreCase(subList.get(i))) {
                                        Utils.showMsg("66");
                                        Utils.showMsg(actualData.get(l).getExtKpiStatus());
                                        Utils.showMsg(extStatus.get(m));
                                        if (actualData.get(l).getExtKpiStatus().equalsIgnoreCase(extStatus.get(m))) {
                                            Utils.showMsg("77");
                                            extCount++;
                                            Utils.showMsg("Subj: " + actualData.get(l).getSubject()
                                                    + "Env: " + actualData.get(l).getEnv()
                                                    + " Internal: " + extStatus.get(m) + " : count: " + String.valueOf(extCount));
                                        }
                                    }

                                }
                            }
                            mod.setKpiStatus(extStatus.get(m));
                            mod.setSubject(subList.get(i));
                            mod.setEnv(envList.get(j));
                            mod.setCount(extCount);
                            excelList.add(mod);

                            Utils.showMsg("External Count: " + String.valueOf(extCount));

                        }
//                        Utils.showMsg("Int Count: " + String.valueOf(intCount));
//                        Utils.showMsg("Ext Count: " + String.valueOf(extCount));
                    }

                }
            } else {
                Utils.showMsg("Helllooo ");
            }
        }
        return excelList;
    }
    
    //filter by manager
    private List<Model> createManagerSheet(String closingDate) {
        QueryHandler qh = new QueryHandler(this);
        String query = "";
Utils.showMsg("------------->Inside manager");
        List<String> intStatus = getStatus("internal");
        List<String> extStatus = getStatus("external");
        List<Model> excelList = new ArrayList<Model>();


        if (!closingDate.equalsIgnoreCase("")) {
            query = "select distinct c_sub_id as col from app_fd_rss_request_detail Where c_close_mnth =  ?";
            subList = qh.getInfo(query, closingDate);
            Utils.showMsg("a");
            query = "select distinct c_manager_name as col from app_fd_rss_request_detail Where c_close_mnth = ? ";
            mgrList = qh.getInfo(query, closingDate);
            Utils.showMsg("b");
            List<Model> actualData = qh.getKPITasksByMonth(closingDate);
            Utils.showMsg("c");
            if (!actualData.isEmpty()) {
                for (int i = 0; i < subList.size(); i++) {
                    Utils.showMsg("1");
                    for (int j = 0; j < mgrList.size(); j++) {
                        Utils.showMsg("2");
                        for (int k = 0; k < intStatus.size(); k++) {
                            Utils.showMsg("3");
                            int intCount = 0;
                            Model mod = new Model();
                            for (int l = 0; l < actualData.size(); l++) {
                                Utils.showMsg("4");
                                if (actualData.get(l).getManager().equalsIgnoreCase(mgrList.get(j))) {
                                    Utils.showMsg("5");
                                    if (actualData.get(l).getSubject().equalsIgnoreCase(subList.get(i))) {
                                        Utils.showMsg("6");
                                        Utils.showMsg(actualData.get(l).getIntKpiStatus());
                                        Utils.showMsg(intStatus.get(k));
                                        if (actualData.get(l).getIntKpiStatus().equalsIgnoreCase(intStatus.get(k))) {
                                            Utils.showMsg("7");
                                            intCount++;
                                            Utils.showMsg("Subj: " + actualData.get(l).getSubject()
                                                    + "Env: " + actualData.get(l).getManager()
                                                    + " Internal: " + intStatus.get(k) + " : count: " + String.valueOf(intCount));
                                        }
                                    }

                                }
                            }
                            mod.setKpiStatus(intStatus.get(k));
                            mod.setSubject(subList.get(i));
                            mod.setManager(mgrList.get(j));
                            mod.setCount(intCount);
                            excelList.add(mod);

                            Utils.showMsg("Internal Count: " + String.valueOf(intCount));
                        }
                        for (int m = 0; m < extStatus.size(); m++) {
                            Utils.showMsg("33");
                            int extCount = 0;
                            Model mod = new Model();
                            for (int l = 0; l < actualData.size(); l++) {
                                Utils.showMsg("44");
                                if (actualData.get(l).getManager().equalsIgnoreCase(mgrList.get(j))) {
                                    Utils.showMsg("55");
                                    if (actualData.get(l).getSubject().equalsIgnoreCase(subList.get(i))) {
                                        Utils.showMsg("66");
                                        Utils.showMsg(actualData.get(l).getExtKpiStatus());
                                        Utils.showMsg(extStatus.get(m));
                                        if (actualData.get(l).getExtKpiStatus().equalsIgnoreCase(extStatus.get(m))) {
                                            Utils.showMsg("77");
                                            extCount++;
                                            Utils.showMsg("Subj: " + actualData.get(l).getSubject()
                                                    + "Env: " + actualData.get(l).getManager()
                                                    + " Internal: " + extStatus.get(m) + " : count: " + String.valueOf(extCount));
                                        }
                                    }

                                }
                            }
                            mod.setKpiStatus(extStatus.get(m));
                            mod.setSubject(subList.get(i));
                            mod.setManager(mgrList.get(j));
                            mod.setCount(extCount);
                            excelList.add(mod);

                            Utils.showMsg("External Count: " + String.valueOf(extCount));

                        }
//                        Utils.showMsg("Int Count: " + String.valueOf(intCount));
//                        Utils.showMsg("Ext Count: " + String.valueOf(extCount));
                    }

                }
            } else {
                Utils.showMsg("Helllooo ");
            }
        }
        return excelList;
    }

=======
>>>>>>> c02858cec5e36b146374ae2774fba030e5ba7366
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
<<<<<<< HEAD
    private void setFontStyle(Cell cell, XSSFWorkbook workbook){
        // create font
        XSSFFont font= workbook.createFont();
        font.setFontHeightInPoints((short)10);
        font.setFontName("Arial");
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setBold(true);
        font.setItalic(false);
                                    
         // Create cell style 
        CellStyle style=workbook.createCellStyle();;
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        // Setting font to style
        style.setFont(font);
        
        // Setting cell style
        cell.setCellStyle(style);
    }
=======

>>>>>>> c02858cec5e36b146374ae2774fba030e5ba7366
}


