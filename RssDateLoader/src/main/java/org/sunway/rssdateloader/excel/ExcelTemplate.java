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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
    List<String> tlList = null;
    List<String> companyList = null;
    String[] managerList = {};

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

        // Utils.showMsg("excel creation");
        String filename = "RSS-" + new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date()) + ".xlsx";
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        ServletOutputStream outStream;

        //TODO: 
        String mainQuery = getQuery(request);
        String closingDate = request.getParameter("closingDate");
        String subjects = request.getParameter("subject");
        if (subjects.equalsIgnoreCase("none")) {
            subjects = "All";
        }

//        if (!closingDate.equalsIgnoreCase("") 
//                && closingDate.equalsIgnoreCase("hell")) {
//            
//        }        
        List<Model> excelEnvRows = createEnvSheet(closingDate, mainQuery);
        List<Model> excelMgrRows = createManagerSheet(closingDate, mainQuery);
        List<Model> excelPICRows = createPICSheet(closingDate, mainQuery);
        List<Model> excelTLRows = createTLSheet(closingDate, mainQuery);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("By Environment");
        XSSFSheet sheet1 = workbook.createSheet("By Manager");
        XSSFSheet sheet2 = workbook.createSheet("By PIC");
        XSSFSheet sheet3 = workbook.createSheet("By TeamLeader");

        // To create Heading
//        String[] headings = {"KPI Status", "KPI Tasks", "Environment", "Count"};
        String[] headings = {"KPI Status", "KPI Tasks", "TestEnv", "Construction", "Dekon", "Emerging", "Hotel", "Medical", "PDD", "PI"};
        String[] headings1 = {"Manager(s)", "KPI Tasks", "Preparer Exceed", "Preparer Delay", "Preparer Meet", "TL Exceed", "TL Delay", "TL Meet"};
        String[] headings2 = {"PIC Name", "Industry", "Company", "GL Manager", "KPI Tasks", "Preparer Exceed", "Preparer Delay", "Preparer Meet"};
        String[] headings3 = {"TL Name", "Industry", "Company", "GL Manager", "KPI Tasks", "TL Exceed", "TL Delay", "TL Meet"};

        createHeader(sheet, closingDate, subjects);
        createHeader(sheet1, closingDate, subjects);
        createHeader(sheet2, closingDate, subjects);
        createHeader(sheet3, closingDate, subjects);

        Row headingRow = sheet.createRow(4);
        Row headingRow1 = sheet1.createRow(4);
        Row headingRow2 = sheet2.createRow(4);
        Row headingRow3 = sheet3.createRow(4);

        int columnCount = 0;
        for (String col : headings) {
            Cell cell = headingRow.createCell(columnCount);
            cell.setCellValue(col);
            setFontStyle(cell, workbook);
            sheet.setColumnWidth(columnCount, 5000);
            columnCount++;
        }
        int columnCount1 = 0;
        for (String col : headings1) {
            Cell cell = headingRow1.createCell(columnCount1);
            cell.setCellValue(col);
            setFontStyle(cell, workbook);
            sheet1.setColumnWidth(columnCount1, 6000);
            columnCount1++;
        }
        int columnCount2 = 0;
        for (String col : headings2) {
            Cell cell = headingRow2.createCell(columnCount2);
            cell.setCellValue(col);
            setFontStyle(cell, workbook);
            sheet2.setColumnWidth(columnCount2, 6000);
            columnCount2++;
        }
        int columnCount3 = 0;
        for (String col : headings3) {
            Cell cell = headingRow3.createCell(columnCount3);
            cell.setCellValue(col);
            setFontStyle(cell, workbook);
            sheet3.setColumnWidth(columnCount3, 6000);
            columnCount3++;
        }
        // to create data in sheet rows
        int rowCount = 5;
        for (Model excelRow : excelEnvRows) {
            Row row = sheet.createRow(rowCount);
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(excelRow.getKpiStatus());
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(excelRow.getSubject());
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(excelRow.getTestEnv());
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(excelRow.getConstruction());
            Cell cell4 = row.createCell(4);
            cell4.setCellValue(excelRow.getDekon());
            Cell cell5 = row.createCell(5);
            cell5.setCellValue(excelRow.getEmerging());
            Cell cell6 = row.createCell(6);
            cell6.setCellValue(excelRow.getHotel());
            Cell cell7 = row.createCell(7);
            cell7.setCellValue(excelRow.getMedical());
            Cell cell8 = row.createCell(8);
            cell8.setCellValue(excelRow.getPDD());
            Cell cell9 = row.createCell(9);
            cell9.setCellValue(excelRow.getPI());
            rowCount++;
        }

        int rowCount1 = 5;
        for (Model excelRow : excelMgrRows) {
            Row row = sheet1.createRow(rowCount1);
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(excelRow.getManager());
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(excelRow.getSubject());
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(excelRow.getPreExceed());
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(excelRow.getPreDelay());
            Cell cell4 = row.createCell(4);
            cell4.setCellValue(excelRow.getPreMeet());
            Cell cell5 = row.createCell(5);
            cell5.setCellValue(excelRow.getTlExceed());
            Cell cell6 = row.createCell(6);
            cell6.setCellValue(excelRow.getTlDelay());
            Cell cell7 = row.createCell(7);
            cell7.setCellValue(excelRow.getTlMeet());
            rowCount1++;
        }
        int rowCount2 = 5;
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
            Cell cell5 = row.createCell(5);
            cell5.setCellValue(excelRow.getPreExceed());
            Cell cell6 = row.createCell(6);
            cell6.setCellValue(excelRow.getPreDelay());
            Cell cell7 = row.createCell(7);
            cell7.setCellValue(excelRow.getPreMeet());
            rowCount2++;
        }
        int rowCount3 = 5;
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
            Cell cell5 = row.createCell(5);
            cell5.setCellValue(excelRow.getTlExceed());
            Cell cell6 = row.createCell(6);
            cell6.setCellValue(excelRow.getTlDelay());
            Cell cell7 = row.createCell(7);
            cell7.setCellValue(excelRow.getTlMeet());
            rowCount3++;
        }

        try {
            outStream = response.getOutputStream();
            workbook.write(outStream);
            // Utils.showMsg("workbook ceation");
            outStream.close();
        } catch (FileNotFoundException ex) {
            // Utils.showMsg(ex.getMessage());
        } catch (IOException ex) {
            // Utils.showMsg(ex.getMessage());
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

    private void createHeader(XSSFSheet sheet, String closingDate, String subjects) {
        Row dateRowRow = sheet.createRow(1);
        Cell clDateH = dateRowRow.createCell(0);
        clDateH.setCellValue("Closing Date");

        Cell clDate = dateRowRow.createCell(1);
        clDate.setCellValue(closingDate);

        Row subRow = sheet.createRow(2);
        Cell subH = subRow.createCell(0);
        subH.setCellValue("Subject");

        Cell sub = subRow.createCell(1);
        sub.setCellValue(subjects);
    }

    private String getQuery(HttpServletRequest request) {
        String query = "";
        String closingDate = request.getParameter("closingDate");
        String subject = request.getParameter("subject");
        String teamLeader = request.getParameter("teamLeader");
        String glManager = request.getParameter("glManager");
        String preparer = request.getParameter("preparer");
        String period_from = request.getParameter("period_from");
        String period_to = request.getParameter("period_to");

        String[] array = null;

        // Utils.showMsg("===>>> Here date is: " + closingDate + " : Subject : " + subject);
        // Utils.showMsg("===>>> teamLeader: " + teamLeader + " : glManager : " + glManager);
        // Utils.showMsg("===>>> preparer: " + preparer);
        // Utils.showMsg("===>>> period_from: " + period_from + " : period_to : " + period_to);
        query = "select distinct id, c_env, c_manager_name,c_company_id, c_pic_name, c_tlName, c_int_kpi_status, c_ext_kpi_status, c_sub_id, c_close_mnth, c_manager_name \n"
                + " from  app_fd_rss_request_detail Where c_close_mnth = '" + closingDate + "' \n";

        if (!subject.equalsIgnoreCase("none")) {
            query += " AND c_sub_id ='" + subject + "' ";
        }

        if (!teamLeader.equalsIgnoreCase("none")) {
            query += " AND c_team_leader like '%" + teamLeader + "%' ";
        }

        if (!glManager.equalsIgnoreCase("none")) {
            query += " AND c_manager_id  like '%" + glManager + "%' ";
        }

        if (!preparer.equalsIgnoreCase("none")) {
            query += " AND c_username ='" + preparer + "' ";
        }

        if (!period_from.equalsIgnoreCase("none")) {
            array = period_from.split("\\-");
            period_from = array[2] + "-" + array[1] + "-" + array[0];

            query += " AND c_period_from ='" + period_from + "' ";
        }

        if (!period_to.equalsIgnoreCase("none")) {
            array = period_to.split("\\-");
            period_to = array[2] + "-" + array[1] + "-" + array[0];

            query += " AND c_period_to ='" + period_to + "' ";
        }

        return query;
    }

    private List<Model> createEnvSheet(String closingDate, String query) {
        QueryHandler qh = new QueryHandler(this);
        HashMap<String, Model> kpiCount = new HashMap<String, Model>();
        String mQuery = "";
        List<String> intStatus = getStatus("internal");
        List<String> extStatus = getStatus("external");
        List<Model> excelList = new ArrayList<Model>();

        if (!closingDate.equalsIgnoreCase("")) {
            mQuery = "select distinct c_sub_id as col from app_fd_rss_request_detail Where c_close_mnth =  ?";
            subList = qh.getInfo(mQuery, closingDate);
            mQuery = "select distinct c_env as col from app_fd_rss_request_detail Where c_close_mnth = ? ";
            envList = qh.getInfo(mQuery, closingDate);

            List<Model> actualData = qh.getKPITasksByMonth(query);
            if (!actualData.isEmpty()) {
                for (int i = 0; i < subList.size(); i++) {
                    for (int j = 0; j < envList.size(); j++) {
                        for (int k = 0; k < intStatus.size(); k++) {
                            Model mod = new Model();
                            int intCount = 0;
                            for (int l = 0; l < actualData.size(); l++) {
                                if (actualData.get(l).getEnv().equalsIgnoreCase(envList.get(j))) {
                                    if (actualData.get(l).getSubject().equalsIgnoreCase(subList.get(i))) {
                                        if (actualData.get(l).getIntKpiStatus().equalsIgnoreCase(intStatus.get(k))) {
                                            intCount++;

                                        }
                                    }

                                }
                            }
                            mod.setKpiStatus(intStatus.get(k));
                            mod.setSubject(subList.get(i));
                            mod.setEnv(envList.get(j));
                            if (kpiCount.containsKey(intStatus.get(k) + subList.get(i))) {
                                mod = (Model) kpiCount.get(intStatus.get(k) + subList.get(i));

                                if (envList.get(j).equalsIgnoreCase("TestEnv")) {
                                    mod.setTestEnv(intCount);
                                } else if (envList.get(j).equalsIgnoreCase("Construction")) {
                                    mod.setConstruction(intCount);
                                } else if (envList.get(j).equalsIgnoreCase("Dekon")) {
                                    mod.setDekon(intCount);
                                } else if (envList.get(j).equalsIgnoreCase("Emerging")) {
                                    mod.setEmerging(intCount);
                                } else if (envList.get(j).equalsIgnoreCase("Hotel")) {
                                    mod.setHotel(intCount);
                                } else if (envList.get(j).equalsIgnoreCase("Medical")) {
                                    mod.setMedical(intCount);
                                } else if (envList.get(j).equalsIgnoreCase("PDD")) {
                                    mod.setPDD(intCount);
                                } else if (envList.get(j).equalsIgnoreCase("PI")) {
                                    mod.setPI(intCount);
                                }
                            } else {
                                if (envList.get(j).equalsIgnoreCase("TestEnv")) {
                                    mod.setTestEnv(intCount);
                                } else if (envList.get(j).equalsIgnoreCase("Construction")) {
                                    mod.setConstruction(intCount);
                                } else if (envList.get(j).equalsIgnoreCase("Dekon")) {
                                    mod.setDekon(intCount);
                                } else if (envList.get(j).equalsIgnoreCase("Emerging")) {
                                    mod.setEmerging(intCount);
                                } else if (envList.get(j).equalsIgnoreCase("Hotel")) {
                                    mod.setHotel(intCount);
                                } else if (envList.get(j).equalsIgnoreCase("Medical")) {
                                    mod.setMedical(intCount);
                                } else if (envList.get(j).equalsIgnoreCase("PDD")) {
                                    mod.setPDD(intCount);
                                } else if (envList.get(j).equalsIgnoreCase("PI")) {
                                    mod.setPI(intCount);
                                }
                                if (intCount != 0) {
                                    kpiCount.put(intStatus.get(k) + subList.get(i), mod);
                                }
                            }
                        }

                        for (int m = 0; m < extStatus.size(); m++) {
                            Model mod = new Model();
                            int extCount = 0;
                            for (int l = 0; l < actualData.size(); l++) {
                                if (actualData.get(l).getEnv().equalsIgnoreCase(envList.get(j))) {
                                    if (actualData.get(l).getSubject().equalsIgnoreCase(subList.get(i))) {
                                        if (actualData.get(l).getExtKpiStatus().equalsIgnoreCase(extStatus.get(m))) {
                                            extCount++;
                                        }
                                    }

                                }
                            }

                            mod.setKpiStatus(extStatus.get(m));
                            mod.setSubject(subList.get(i));
                            mod.setEnv(envList.get(j));
                            if (kpiCount.containsKey(extStatus.get(m) + subList.get(i))) {
                                mod = (Model) kpiCount.get(extStatus.get(m) + subList.get(i));
                                if (envList.get(j).equalsIgnoreCase("TestEnv")) {
                                    mod.setTestEnv(extCount);
                                } else if (envList.get(j).equalsIgnoreCase("Construction")) {
                                    mod.setConstruction(extCount);
                                } else if (envList.get(j).equalsIgnoreCase("Dekon")) {
                                    mod.setDekon(extCount);
                                } else if (envList.get(j).equalsIgnoreCase("Emerging")) {
                                    mod.setEmerging(extCount);
                                } else if (envList.get(j).equalsIgnoreCase("Hotel")) {
                                    mod.setHotel(extCount);
                                } else if (envList.get(j).equalsIgnoreCase("Medical")) {
                                    mod.setMedical(extCount);
                                } else if (envList.get(j).equalsIgnoreCase("PDD")) {
                                    mod.setPDD(extCount);
                                } else if (envList.get(j).equalsIgnoreCase("PI")) {
                                    mod.setPI(extCount);
                                }
                            } else {
                                if (envList.get(j).equalsIgnoreCase("TestEnv")) {
                                    mod.setTestEnv(extCount);
                                } else if (envList.get(j).equalsIgnoreCase("Construction")) {
                                    mod.setConstruction(extCount);
                                } else if (envList.get(j).equalsIgnoreCase("Dekon")) {
                                    mod.setDekon(extCount);
                                } else if (envList.get(j).equalsIgnoreCase("Emerging")) {
                                    mod.setEmerging(extCount);
                                } else if (envList.get(j).equalsIgnoreCase("Hotel")) {
                                    mod.setHotel(extCount);
                                } else if (envList.get(j).equalsIgnoreCase("Medical")) {
                                    mod.setMedical(extCount);
                                } else if (envList.get(j).equalsIgnoreCase("PDD")) {
                                    mod.setPDD(extCount);
                                } else if (envList.get(j).equalsIgnoreCase("PI")) {
                                    mod.setPI(extCount);
                                }
                                if (extCount != 0) {
                                    kpiCount.put(extStatus.get(m) + subList.get(i), mod);
                                }
                            }

                        }
                    }

                }
            } else {
                // Utils.showMsg("Helllooo ");
            }
        }

        for (Model url : kpiCount.values()) {
            excelList.add(url);
        }
        Comparator<Model> compareById = (Model o1, Model o2) -> o1.getKpiStatus().compareTo(o2.getKpiStatus());
        Collections.sort(excelList, compareById);

//        compareById = (Model o1, Model o2) -> o1.getSubject().compareTo( o2.getSubject());
//        Collections.sort(excelList, compareById);
        return excelList;
    }

//    //filter by manager
    private List<Model> createManagerSheet(String closingDate, String query) {
        QueryHandler qh = new QueryHandler(this);
        HashMap<String, Model> kpiCount = new HashMap<String, Model>();
        String mQuery = "";
        Utils.showMsg("------------->Inside manager");
        List<String> intStatus = getStatus("internal");
        List<String> extStatus = getStatus("external");
        List<Model> excelList = new ArrayList<Model>();

        if (!closingDate.equalsIgnoreCase("")) {
            mQuery = "select distinct c_sub_id as col from app_fd_rss_request_detail Where c_close_mnth =  ?";
            subList = qh.getInfo(mQuery, closingDate);
            Utils.showMsg("a");
            mQuery = "select distinct c_manager_name as col from app_fd_rss_request_detail Where c_close_mnth = ? ";
            mgrList = qh.getInfo(mQuery, closingDate);
            Utils.showMsg("b");
            List<Model> actualData = qh.getKPITasksByMonth(query);
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
                            if (kpiCount.containsKey(subList.get(i) + mgrList.get(j))) {
                                mod = (Model) kpiCount.get(subList.get(i) + mgrList.get(j));

                                if (intStatus.get(k).equalsIgnoreCase("Preparer Exceed")) {
                                    mod.setPreExceed(intCount);
                                } else if (intStatus.get(k).equalsIgnoreCase("Preparer Delay")) {
                                    mod.setPreDelay(intCount);
                                } else if (intStatus.get(k).equalsIgnoreCase("Preparer Meet")) {
                                    mod.setPreMeet(intCount);
                                }
                            } else {
                                if (intStatus.get(k).equalsIgnoreCase("Preparer Exceed")) {
                                    mod.setPreExceed(intCount);
                                } else if (intStatus.get(k).equalsIgnoreCase("Preparer Delay")) {
                                    mod.setPreDelay(intCount);
                                } else if (intStatus.get(k).equalsIgnoreCase("Preparer Meet")) {
                                    mod.setPreMeet(intCount);
                                }
                                if (intCount != 0) {
                                    kpiCount.put(subList.get(i) + mgrList.get(j), mod);
                                }
                            }
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
                            if (kpiCount.containsKey(subList.get(i) + mgrList.get(j))) {
                                mod = (Model) kpiCount.get(subList.get(i) + mgrList.get(j));

                                if (extStatus.get(m).equalsIgnoreCase("TL Exceed")) {
                                    mod.setTlExceed(extCount);
                                } else if (extStatus.get(m).equalsIgnoreCase("TL Delay")) {
                                    mod.setTlDelay(extCount);
                                } else if (extStatus.get(m).equalsIgnoreCase("TL Meet")) {
                                    mod.setTlMeet(extCount);
                                }
                            } else {
                                if (extStatus.get(m).equalsIgnoreCase("TL Exceed")) {
                                    mod.setTlExceed(extCount);
                                } else if (extStatus.get(m).equalsIgnoreCase("TL Delay")) {
                                    mod.setTlDelay(extCount);
                                } else if (extStatus.get(m).equalsIgnoreCase("TL Meet")) {
                                    mod.setTlMeet(extCount);
                                }
                                if (extCount != 0) {
                                    kpiCount.put(subList.get(i) + mgrList.get(j), mod);
                                }
                            }
                        }
                    }

                }
            } else {
                Utils.showMsg("Helllooo ");
            }
        }
        for (Model url : kpiCount.values()) {
            excelList.add(url);
        }
        Comparator<Model> compareById = (Model o1, Model o2) -> o1.getSubject().compareTo(o2.getSubject());
        Collections.sort(excelList, compareById);
        return excelList;
    }

    private List<Model> createPICSheet(String closingDate, String query) {
        QueryHandler qh = new QueryHandler(this);
        String mquery = "";
        HashMap<String, Model> kpiCount = new HashMap<String, Model>();
        List<String> intStatus = getStatus("internal");
        List<Model> excelList = new ArrayList<Model>();

        if (!closingDate.equalsIgnoreCase("")) {
            mquery = "select distinct c_sub_id as col from app_fd_rss_request_detail Where c_close_mnth =  ?";
            subList = qh.getInfo(mquery, closingDate);
            // Utils.showMsg("a");
            mquery = "select distinct c_env as col from app_fd_rss_request_detail Where c_close_mnth = ? ";
            envList = qh.getInfo(mquery, closingDate);
            mquery = "select distinct c_company_id as col from app_fd_rss_request_detail Where c_close_mnth = ? ";
            companyList = qh.getInfo(mquery, closingDate);
            mquery = "select distinct c_pic_name as col from app_fd_rss_request_detail Where c_close_mnth = ? ";
            picList = qh.getInfo(mquery, closingDate);
            mquery = "select distinct c_manager_name as col from app_fd_rss_request_detail Where c_close_mnth = ? ";
            mgrList = qh.getInfo(mquery, closingDate);

            // Utils.showMsg("b");
            List<Model> actualData = qh.getKPITasksByMonth(query);
            // Utils.showMsg("c");
            if (!actualData.isEmpty()) {
                for (int i = 0; i < subList.size(); i++) {
                    // Utils.showMsg("1");
                    for (int j = 0; j < envList.size(); j++) {
                        // Utils.showMsg("2");
                        for (int k = 0; k < mgrList.size(); k++) {
                            for (int l = 0; l < companyList.size(); l++) {
                                for (int m = 0; m < picList.size(); m++) {
                                    // Utils.showMsg("before status check");
                                    for (int n = 0; n < intStatus.size(); n++) {
                                        // Utils.showMsg("3");
                                        int intCount = 0;
                                        Model mod = new Model();
                                        for (int p = 0; p < actualData.size(); p++) {
                                            // Utils.showMsg("4");
                                            if (actualData.get(p).getEnv().equalsIgnoreCase(envList.get(j))) {
                                                // Utils.showMsg("5");
                                                if (actualData.get(p).getSubject().equalsIgnoreCase(subList.get(i))) {
                                                    if (actualData.get(p).getManager().equalsIgnoreCase(mgrList.get(k))) {
                                                        if (actualData.get(p).getPicName().equalsIgnoreCase(picList.get(m))) {
                                                            if (actualData.get(p).getCompany().equalsIgnoreCase(companyList.get(l))) {
                                                                // Utils.showMsg("6");
                                                                // Utils.showMsg(actualData.get(p).getIntKpiStatus());
                                                                // Utils.showMsg(intStatus.get(n));
                                                                if (actualData.get(p).getIntKpiStatus().equalsIgnoreCase(intStatus.get(n))) {
                                                                    // Utils.showMsg("7");
                                                                    intCount++;
                                                                    // Utils.showMsg("Subj: " + actualData.get(p).getSubject()
//                                                                            + "Env: " + actualData.get(p).getEnv()
//                                                                            + " Internal: " + intStatus.get(n) + " : count: " + String.valueOf(intCount));
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        mod.setKpiStatus(intStatus.get(n));
                                        mod.setSubject(subList.get(i));
                                        mod.setEnv(envList.get(j));
                                        mod.setManager(mgrList.get(k));
                                        mod.setPicName(picList.get(m));
                                        mod.setCompany(companyList.get(l));

                                        if (kpiCount.containsKey(subList.get(i) + envList.get(j) + mgrList.get(k) + picList.get(m) + companyList.get(l))) {
                                            mod = (Model) kpiCount.get(subList.get(i) + envList.get(j) + mgrList.get(k) + picList.get(m) + companyList.get(l));

                                            if (intStatus.get(n).equalsIgnoreCase("Preparer Exceed")) {
                                                mod.setPreExceed(intCount);
                                            } else if (intStatus.get(n).equalsIgnoreCase("Preparer Delay")) {
                                                mod.setPreDelay(intCount);
                                            } else if (intStatus.get(n).equalsIgnoreCase("Preparer Meet")) {
                                                mod.setPreMeet(intCount);
                                            }
                                        } else {
                                            if (intStatus.get(n).equalsIgnoreCase("Preparer Exceed")) {
                                                mod.setPreExceed(intCount);
                                            } else if (intStatus.get(n).equalsIgnoreCase("Preparer Delay")) {
                                                mod.setPreDelay(intCount);
                                            } else if (intStatus.get(n).equalsIgnoreCase("Preparer Meet")) {
                                                mod.setPreMeet(intCount);
                                            }
                                            if (intCount != 0) {
                                                kpiCount.put(subList.get(i) + envList.get(j) + mgrList.get(k) + picList.get(m) + companyList.get(l), mod);
                                            }
                                        }

                                        // Utils.showMsg("Internal Count: " + String.valueOf(intCount));
                                    }
                                }
                            }
                        }
                    }

                }
            } else {
                // Utils.showMsg("Helllooo ");
            }
        }
        for (Model url : kpiCount.values()) {
            excelList.add(url);
        }
        Comparator<Model> compareById = (Model o1, Model o2) -> o1.getSubject().compareTo(o2.getSubject());
        Collections.sort(excelList, compareById);
        return excelList;
    }

    private List<Model> createTLSheet(String closingDate, String query) {
        QueryHandler qh = new QueryHandler(this);
        String mquery = "";
        HashMap<String, Model> kpiCount = new HashMap<String, Model>();
        List<String> extStatus = getStatus("external");
        List<Model> excelList = new ArrayList<Model>();

        if (!closingDate.equalsIgnoreCase("")) {
            mquery = "select distinct c_sub_id as col from app_fd_rss_request_detail Where c_close_mnth =  ?";
            subList = qh.getInfo(mquery, closingDate);
            // Utils.showMsg("a");
            mquery = "select distinct c_env as col from app_fd_rss_request_detail Where c_close_mnth = ? ";
            envList = qh.getInfo(mquery, closingDate);
            mquery = "select distinct c_company_id as col from app_fd_rss_request_detail Where c_close_mnth = ? ";
            companyList = qh.getInfo(mquery, closingDate);
            mquery = "select distinct c_tlName as col from app_fd_rss_request_detail Where c_close_mnth = ? ";
            tlList = qh.getInfo(mquery, closingDate);
            mquery = "select distinct c_manager_name as col from app_fd_rss_request_detail Where c_close_mnth = ? ";
            mgrList = qh.getInfo(mquery, closingDate);

            // Utils.showMsg("b");
            List<Model> actualData = qh.getKPITasksByMonth(query);
            // Utils.showMsg("c");
            if (!actualData.isEmpty()) {
                for (int i = 0; i < subList.size(); i++) {
                    // Utils.showMsg("1");
                    for (int j = 0; j < envList.size(); j++) {
                        // Utils.showMsg("2");
                        for (int k = 0; k < mgrList.size(); k++) {
                            for (int l = 0; l < companyList.size(); l++) {
                                for (int m = 0; m < tlList.size(); m++) {
                                    // Utils.showMsg("before status check");
                                    for (int n = 0; n < extStatus.size(); n++) {
                                        // Utils.showMsg("3");
                                        int extCount = 0;
                                        Model mod = new Model();
                                        for (int p = 0; p < actualData.size(); p++) {
                                            // Utils.showMsg("4");
                                            if (actualData.get(p).getEnv().equalsIgnoreCase(envList.get(j))) {
                                                // Utils.showMsg("5");
                                                if (actualData.get(p).getSubject().equalsIgnoreCase(subList.get(i))) {
                                                    if (actualData.get(p).getManager().equalsIgnoreCase(mgrList.get(k))) {
                                                        if (actualData.get(p).getTlName().equalsIgnoreCase(tlList.get(m))) {
                                                            if (actualData.get(p).getCompany().equalsIgnoreCase(companyList.get(l))) {
                                                                // Utils.showMsg("6");
                                                                // Utils.showMsg(actualData.get(p).getExtKpiStatus());
                                                                // Utils.showMsg(extStatus.get(n));
                                                                if (actualData.get(p).getExtKpiStatus().equalsIgnoreCase(extStatus.get(n))) {
                                                                    // Utils.showMsg("7");
                                                                    extCount++;
                                                                    // Utils.showMsg("Subj: " + actualData.get(p).getSubject()
//                                                                            + "Env: " + actualData.get(p).getEnv()
//                                                                            + " Internal: " + extStatus.get(n) + " : count: " + String.valueOf(extCount));
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
                                        mod.setManager(mgrList.get(k));
                                        mod.setTlName(tlList.get(m));
                                        mod.setCompany(companyList.get(l));

                                        if (kpiCount.containsKey(subList.get(i) + envList.get(j) + mgrList.get(k) + tlList.get(m) + companyList.get(l))) {
                                            mod = (Model) kpiCount.get(subList.get(i) + envList.get(j) + mgrList.get(k) + tlList.get(m) + companyList.get(l));

                                            if (extStatus.get(n).equalsIgnoreCase("TL Exceed")) {
                                                mod.setTlExceed(extCount);
                                            } else if (extStatus.get(n).equalsIgnoreCase("TL Delay")) {
                                                mod.setTlDelay(extCount);
                                            } else if (extStatus.get(n).equalsIgnoreCase("TL Meet")) {
                                                mod.setTlMeet(extCount);
                                            }
                                        } else {
                                            if (extStatus.get(n).equalsIgnoreCase("TL Exceed")) {
                                                mod.setTlExceed(extCount);
                                            } else if (extStatus.get(n).equalsIgnoreCase("TL Delay")) {
                                                mod.setTlDelay(extCount);
                                            } else if (extStatus.get(n).equalsIgnoreCase("TL Meet")) {
                                                mod.setTlMeet(extCount);
                                            }
                                            if (extCount != 0) {
                                                kpiCount.put(subList.get(i) + envList.get(j) + mgrList.get(k) + tlList.get(m) + companyList.get(l), mod);
                                            }
                                        }

                                        // Utils.showMsg("External Count: " + String.valueOf(extCount));
                                    }
                                }
                            }
                        }
                    }

                }
            } else {
                // Utils.showMsg("Helllooo ");
            }
        }
        for (Model url : kpiCount.values()) {
            excelList.add(url);
        }
        Comparator<Model> compareById = (Model o1, Model o2) -> o1.getKpiStatus().compareTo(o2.getKpiStatus());
        Collections.sort(excelList, compareById);
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
