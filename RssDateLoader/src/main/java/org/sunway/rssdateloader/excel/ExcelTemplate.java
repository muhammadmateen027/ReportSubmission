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
    List<String> mgrList = null;
    List<String> picList = null;
    List<String> tlList=null;
    List<String> companyList = null;

//    Model dummy=new Model();
//    List<Model> dummyList=new ArrayList<Model>();
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
        Utils.showMsg("0");
        ServletOutputStream outStream;
        String closingDate = request.getParameter("closingDate");
        closingDate = "04-2019";
        List<Model> excelEnvRows = createEnvSheet(closingDate);
        List<Model> excelMgrRows = createManagerSheet(closingDate);
        List<Model> excelPICRows = createPICSheet(closingDate);
        List<Model> excelTLRows=createTLSheet(closingDate);
        
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("By Environment");
        XSSFSheet sheet1 = workbook.createSheet("By Manager");
        XSSFSheet sheet2 = workbook.createSheet("By PIC");
        XSSFSheet sheet3 = workbook.createSheet("By TeamLeader");

        // To create Heading
//        String[] headings = {"KPI Status", "KPI Tasks", "Environment", "Count"};
        String[] headings = {"KPI Status", "KPI Tasks", "TestEnv", "Construction","Dekon","Emerging","Hotel","Medical","PDD","PI"};
        String[] headings1 = {"KPI Status", "KPI Tasks", "TRAIN USER1 ,FAIZ RASHIDI"};
        String[] headings2 = {"PIC Name", "Industry", "Company", "GL Manager", "KPI Tasks", "Preparer Exceed", "Preparer Meet", "Preparer Delay"};
        String[] headings3 = {"TL Name", "Industry", "Company", "GL Manager", "KPI Tasks", "TL Exceed", "TL Meet", "TL Delay"};

        Row headingRow = sheet.createRow(0);
        Row headingRow1 = sheet1.createRow(0);
        Row headingRow2 = sheet2.createRow(0);
        Row headingRow3 = sheet3.createRow(0);
        
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
        int columnCount2 = 0;
        for (String col : headings2) {
            Cell cell = headingRow2.createCell(columnCount2);
            cell.setCellValue(col);
            setFontStyle(cell, workbook);
            sheet2.setColumnWidth(columnCount2, 9000);
            columnCount2++;
        }
        int columnCount3 = 0;
        for (String col : headings3) {
            Cell cell = headingRow3.createCell(columnCount3);
            cell.setCellValue(col);
            setFontStyle(cell, workbook);
            sheet3.setColumnWidth(columnCount3, 9000);
            columnCount3++;
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
            if (excelRow.getEnv().equalsIgnoreCase("TestEnv")) {
                Cell cell2 = row.createCell(2);
                cell2.setCellValue(excelRow.getTestEnv());
            } else if (excelRow.getEnv().equalsIgnoreCase("Construction")) {
                Cell cell3 = row.createCell(3);
                cell3.setCellValue(excelRow.getConstruction());
            }else if (excelRow.getEnv().equalsIgnoreCase("Dekon")) {
                Cell cell4 = row.createCell(4);
                cell4.setCellValue(excelRow.getDekon());
            }else if (excelRow.getEnv().equalsIgnoreCase("Emerging")) {
                Cell cell5 = row.createCell(5);
                cell5.setCellValue(excelRow.getEmerging());
            }else if (excelRow.getEnv().equalsIgnoreCase("Hotel")) {
                Cell cell6 = row.createCell(6);
                cell6.setCellValue(excelRow.getHotel());
            }else if (excelRow.getEnv().equalsIgnoreCase("Medical")) {
                Cell cell7 = row.createCell(7);
                cell7.setCellValue(excelRow.getMedical());
            }else if (excelRow.getEnv().equalsIgnoreCase("PDD")) {
                Cell cell8 = row.createCell(8);
                cell8.setCellValue(excelRow.getPDD());
            }else if (excelRow.getEnv().equalsIgnoreCase("PI")) {
                Cell cell9 = row.createCell(9);
                cell9.setCellValue(excelRow.getPI());
            }
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
            if (excelRow.getManager().equalsIgnoreCase("TRAIN USER1 ,FAIZ RASHIDI")) {
                Cell cell2 = row.createCell(2);
                cell2.setCellValue(excelRow.getCount());
            } else if (excelRow.getEnv().equalsIgnoreCase("PI")) {
                Cell cell3 = row.createCell(3);
                cell3.setCellValue(excelRow.getCount());
            }
            Utils.showMsg("checking---------->" + cell0.toString());
            rowCount1++;
        }
        int rowCount2 = 1;
        for (Model excelRow : excelPICRows) {
            Row row = sheet2.createRow(rowCount2);
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(excelRow.getPicName());
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(excelRow.getEnv());
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(excelRow.getCompany());
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(excelRow.getManager());
            Cell cell4 = row.createCell(4);
            cell4.setCellValue(excelRow.getSubject());

            if (excelRow.getKpiStatus().equalsIgnoreCase("Preparer Exceed")) {
                Cell cell5 = row.createCell(5);
                cell5.setCellValue(excelRow.getCount());
            } else if (excelRow.getKpiStatus().equalsIgnoreCase("Preparer Meet")) {
                Cell cell6 = row.createCell(6);
                cell6.setCellValue(excelRow.getCount());
            } else if (excelRow.getKpiStatus().equalsIgnoreCase("Preparer Delay")) {
                Cell cell7 = row.createCell(7);
                cell7.setCellValue(excelRow.getCount());
            }
            Utils.showMsg("checking---------->" + cell0.toString());
            rowCount2++;
        }
        int rowCount3 = 1;
        for (Model excelRow : excelTLRows) {
            Row row = sheet3.createRow(rowCount3);
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(excelRow.getTlName());
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(excelRow.getEnv());
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(excelRow.getCompany());
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(excelRow.getManager());
            Cell cell4 = row.createCell(4);
            cell4.setCellValue(excelRow.getSubject());

            if (excelRow.getKpiStatus().equalsIgnoreCase("TL Exceed")) {
                Cell cell5 = row.createCell(5);
                cell5.setCellValue(excelRow.getCount());
            } else if (excelRow.getKpiStatus().equalsIgnoreCase("TL Meet")) {
                Cell cell6 = row.createCell(6);
                cell6.setCellValue(excelRow.getCount());
            } else if (excelRow.getKpiStatus().equalsIgnoreCase("TL Delay")) {
                Cell cell7 = row.createCell(7);
                cell7.setCellValue(excelRow.getCount());
            }
            Utils.showMsg("checking---------->" + cell0.toString());
            rowCount3++;
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
                            for(int s=0;s<excelList.size();s++)
                            {
                                if((excelList.get(s).getSubject().equalsIgnoreCase(subList.get(i))) 
                                        && (excelList.get(s).getKpiStatus().equalsIgnoreCase(intStatus.get(k)))){
                                    Utils.showMsg("inside same record subject");
                                    if(envList.get(j).equalsIgnoreCase("TestEnv")){
                                        excelList.get(s).setTestEnv(intCount);
                                    }else if(envList.get(j).equalsIgnoreCase("Construction")){
                                        excelList.get(s).setConstruction(intCount);
                                    }else if(envList.get(j).equalsIgnoreCase("Dekon")){
                                        excelList.get(s).setDekon(intCount);
                                    }else if(envList.get(j).equalsIgnoreCase("Emerging")){
                                        excelList.get(s).setEmerging(intCount);
                                    }else if(envList.get(j).equalsIgnoreCase("Hotel")){
                                        excelList.get(s).setHotel(intCount);
                                    }else if(envList.get(j).equalsIgnoreCase("Medical")){
                                        excelList.get(s).setMedical(intCount);
                                    }else if(envList.get(j).equalsIgnoreCase("PDD")){
                                        excelList.get(s).setPDD(intCount);
                                    }else if(envList.get(j).equalsIgnoreCase("PI")){
                                        excelList.get(s).setPI(intCount);
                                    }
                                }else{
                                    Utils.showMsg("inside different record subject");
                                    if(envList.get(j).equalsIgnoreCase("TestEnv")){
                                        mod.setTestEnv(intCount);
                                    }else if(envList.get(j).equalsIgnoreCase("Construction")){
                                        mod.setConstruction(intCount);
                                    }else if(envList.get(j).equalsIgnoreCase("Dekon")){
                                        mod.setDekon(intCount);
                                    }else if(envList.get(j).equalsIgnoreCase("Emerging")){
                                        mod.setEmerging(intCount);
                                    }else if(envList.get(j).equalsIgnoreCase("Hotel")){
                                        mod.setHotel(intCount);
                                    }else if(envList.get(j).equalsIgnoreCase("Medical")){
                                        mod.setMedical(intCount);
                                    }else if(envList.get(j).equalsIgnoreCase("PDD")){
                                        mod.setPDD(intCount);
                                    }else if(envList.get(j).equalsIgnoreCase("PI")){
                                        mod.setPI(intCount);
                                    }
                                    mod.setKpiStatus(intStatus.get(k));
                                    mod.setSubject(subList.get(i));
                                    mod.setEnv(envList.get(j));

                                }
                                    
                            }
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
                                                    + " External: " + extStatus.get(m) + " : count: " + String.valueOf(extCount));
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
                                                    + " External: " + extStatus.get(m) + " : count: " + String.valueOf(extCount));
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

    private List<Model> createPICSheet(String closingDate) {
        QueryHandler qh = new QueryHandler(this);
        String query = "";

        List<String> intStatus = getStatus("internal");
        List<Model> excelList = new ArrayList<Model>();

        if (!closingDate.equalsIgnoreCase("")) {
            query = "select distinct c_sub_id as col from app_fd_rss_request_detail Where c_close_mnth =  ?";
            subList = qh.getInfo(query, closingDate);
            Utils.showMsg("a");
            query = "select distinct c_env as col from app_fd_rss_request_detail Where c_close_mnth = ? ";
            envList = qh.getInfo(query, closingDate);
            query = "select distinct c_company_id as col from app_fd_rss_request_detail Where c_close_mnth = ? ";
            companyList = qh.getInfo(query, closingDate);
            query = "select distinct c_pic_name as col from app_fd_rss_request_detail Where c_close_mnth = ? ";
            picList = qh.getInfo(query, closingDate);
            query = "select distinct c_manager_name as col from app_fd_rss_request_detail Where c_close_mnth = ? ";
            mgrList = qh.getInfo(query, closingDate);

            Utils.showMsg("b");
            List<Model> actualData = qh.getKPITasksByMonth(closingDate);
            Utils.showMsg("c");
            if (!actualData.isEmpty()) {
                for (int i = 0; i < subList.size(); i++) {
                    Utils.showMsg("1");
                    for (int j = 0; j < envList.size(); j++) {
                        Utils.showMsg("2");
                        for (int k = 0; k < mgrList.size(); k++) {
                            for (int l = 0; l < companyList.size(); l++) {
                                for (int m = 0; m < picList.size(); m++) {
                                    Utils.showMsg("before status check");
                                    for (int n = 0; n < intStatus.size(); n++) {
                                        Utils.showMsg("3");
                                        int intCount = 0;
                                        Model mod = new Model();
                                        for (int p = 0; p < actualData.size(); p++) {
                                            Utils.showMsg("4");
                                            if (actualData.get(p).getEnv().equalsIgnoreCase(envList.get(j))) {
                                                Utils.showMsg("5");
                                                if (actualData.get(p).getSubject().equalsIgnoreCase(subList.get(i))) {
                                                    if (actualData.get(p).getManager().equalsIgnoreCase(mgrList.get(k))) {
                                                        if (actualData.get(p).getPicName().equalsIgnoreCase(picList.get(m))) {
                                                            if (actualData.get(p).getCompany().equalsIgnoreCase(companyList.get(l))) {
                                                                Utils.showMsg("6");
                                                                Utils.showMsg(actualData.get(p).getIntKpiStatus());
                                                                Utils.showMsg(intStatus.get(n));
                                                                if (actualData.get(p).getIntKpiStatus().equalsIgnoreCase(intStatus.get(n))) {
                                                                    Utils.showMsg("7");
                                                                    intCount++;
                                                                    Utils.showMsg("Subj: " + actualData.get(p).getSubject()
                                                                            + "Env: " + actualData.get(p).getEnv()
                                                                            + " Internal: " + intStatus.get(n) + " : count: " + String.valueOf(intCount));
                                                                }
                                                            }
                                                        }
                                                    }
                                                }//
                                            }
                                        }
                                        mod.setKpiStatus(intStatus.get(n));
                                        mod.setSubject(subList.get(i));
                                        mod.setEnv(envList.get(j));
                                        mod.setCount(intCount);
                                        mod.setManager(mgrList.get(k));
                                        mod.setPicName(picList.get(m));
                                        mod.setCompany(companyList.get(l));
                                        excelList.add(mod);

                                        Utils.showMsg("Internal Count: " + String.valueOf(intCount));
                                    }
//                        Utils.showMsg("Int Count: " + String.valueOf(intCount));
//                        Utils.showMsg("Ext Count: " + String.valueOf(extCount));
                                }
                            }
                        }
                    }//sub

                }
            } else {
                Utils.showMsg("Helllooo ");
            }
        }
        return excelList;
    }
    private List<Model> createTLSheet(String closingDate) {
        QueryHandler qh = new QueryHandler(this);
        String query = "";

        List<String> extStatus = getStatus("external");
        List<Model> excelList = new ArrayList<Model>();

        if (!closingDate.equalsIgnoreCase("")) {
            query = "select distinct c_sub_id as col from app_fd_rss_request_detail Where c_close_mnth =  ?";
            subList = qh.getInfo(query, closingDate);
            Utils.showMsg("a");
            query = "select distinct c_env as col from app_fd_rss_request_detail Where c_close_mnth = ? ";
            envList = qh.getInfo(query, closingDate);
            query = "select distinct c_company_id as col from app_fd_rss_request_detail Where c_close_mnth = ? ";
            companyList = qh.getInfo(query, closingDate);
            query = "select distinct c_tlName as col from app_fd_rss_request_detail Where c_close_mnth = ? ";
            tlList = qh.getInfo(query, closingDate);
            query = "select distinct c_manager_name as col from app_fd_rss_request_detail Where c_close_mnth = ? ";
            mgrList = qh.getInfo(query, closingDate);

            Utils.showMsg("b");
            List<Model> actualData = qh.getKPITasksByMonth(closingDate);
            Utils.showMsg("c");
            if (!actualData.isEmpty()) {
                for (int i = 0; i < subList.size(); i++) {
                    Utils.showMsg("1");
                    for (int j = 0; j < envList.size(); j++) {
                        Utils.showMsg("2");
                        for (int k = 0; k < mgrList.size(); k++) {
                            for (int l = 0; l < companyList.size(); l++) {
                                for (int m = 0; m < tlList.size(); m++) {
                                    Utils.showMsg("before status check");
                                    for (int n = 0; n < extStatus.size(); n++) {
                                        Utils.showMsg("3");
                                        int extCount = 0;
                                        Model mod = new Model();
                                        for (int p = 0; p < actualData.size(); p++) {
                                            Utils.showMsg("4");
                                            if (actualData.get(p).getEnv().equalsIgnoreCase(envList.get(j))) {
                                                Utils.showMsg("5");
                                                if (actualData.get(p).getSubject().equalsIgnoreCase(subList.get(i))) {
                                                    if (actualData.get(p).getManager().equalsIgnoreCase(mgrList.get(k))) {
                                                        if (actualData.get(p).getTlName().equalsIgnoreCase(tlList.get(m))) {
                                                            if (actualData.get(p).getCompany().equalsIgnoreCase(companyList.get(l))) {
                                                                Utils.showMsg("6");
                                                                Utils.showMsg(actualData.get(p).getExtKpiStatus());
                                                                Utils.showMsg(extStatus.get(n));
                                                                if (actualData.get(p).getExtKpiStatus().equalsIgnoreCase(extStatus.get(n))) {
                                                                    Utils.showMsg("7");
                                                                    extCount++;
                                                                    Utils.showMsg("Subj: " + actualData.get(p).getSubject()
                                                                            + "Env: " + actualData.get(p).getEnv()
                                                                            + " Internal: " + extStatus.get(n) + " : count: " + String.valueOf(extCount));
                                                                }
                                                            }
                                                        }
                                                    }
                                                }//
                                            }
                                        }
                                        mod.setKpiStatus(extStatus.get(n));
                                        mod.setSubject(subList.get(i));
                                        mod.setEnv(envList.get(j));
                                        mod.setCount(extCount);
                                        mod.setManager(mgrList.get(k));
                                        mod.setTlName(tlList.get(m));
                                        mod.setCompany(companyList.get(l));
                                        excelList.add(mod);

                                        Utils.showMsg("Internal Count: " + String.valueOf(extCount));
                                    }
//                        Utils.showMsg("Int Count: " + String.valueOf(intCount));
//                        Utils.showMsg("Ext Count: " + String.valueOf(extCount));
                                }
                            }
                        }
                    }//sub

                }
            } else {
                Utils.showMsg("Helllooo ");
            }
        }
        return excelList;
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

    private void setFontStyle(Cell cell, XSSFWorkbook workbook) {
        // create font
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setFontName("Arial");
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setBold(true);
        font.setItalic(false);

        // Create cell style 
        CellStyle style = workbook.createCellStyle();;
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        // Setting font to style
        style.setFont(font);

        // Setting cell style
        cell.setCellStyle(style);
    }

}
