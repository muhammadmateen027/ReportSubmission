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
public class ExcelByYear extends Element implements PluginWebSupport, QueryHandlerInterface {

    final String pluginName = "RSS - ByYear ExcelTemplate";
    final String version = "1.0";
    JSONArray jSONArray;
    List<String> subList = null;
    List<String> envList = null;
    List<String> mgrList = null;
    List<String> picList = null;
    List<String> tlList = null;
    List<String> companyList = null;
    String[] managerList = {};

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
        ServletOutputStream outStream;

        //TODO: 
        Utils.showMsg("==>> "+"Hellooo");
        String mainQuery = getQuery(request);
        Utils.showMsg("==>> "+mainQuery);
        
        String from = request.getParameter("from");
        String to = request.getParameter("to");
        String year = request.getParameter("year");
        
        
        String startMonth = from + "-" +year;
        String endMonth = to + "-" +year;
        
        Utils.showMsg("==>> "+startMonth + " ; "+ endMonth);
        
        String subjects = request.getParameter("subject");
        if (subjects.equalsIgnoreCase("none")) {
            subjects = "All";
        }

        List<Model> excelEnvRows = createEnvSheet(startMonth, endMonth, mainQuery);
        List<Model> excelMgrRows = createManagerSheet(startMonth, endMonth, mainQuery);
        List<Model> excelPICRows = createPICSheet(startMonth, endMonth, mainQuery);
        List<Model> excelTLRows = createTLSheet(startMonth, endMonth, mainQuery);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("By Environment");
        XSSFSheet sheet1 = workbook.createSheet("By Manager");
        XSSFSheet sheet2 = workbook.createSheet("By PIC");
        XSSFSheet sheet3 = workbook.createSheet("By TeamLeader");

        // To create Heading
        String[] headings = {"KPI Status", "KPI Tasks", "TestEnv", "Construction", "Dekon", "Emerging", "Hotel", "Medical", "PDD", "PI","REIT"};
        String[] headings1 = {"Manager(s)", "KPI Tasks", "Preparer Exceed", "Preparer Delay", "Preparer Meet", "TL Exceed", "TL Delay", "TL Meet"};
        String[] headings2 = {"PIC Name", "Industry", "Company", "GL Manager", "KPI Tasks", "Preparer Exceed", "Preparer Delay", "Preparer Meet"};
        String[] headings3 = {"TL Name", "Industry", "Company", "GL Manager", "KPI Tasks", "TL Exceed", "TL Delay", "TL Meet"};

        createHeader(sheet, startMonth, endMonth, subjects);
        createHeader(sheet1, startMonth, endMonth, subjects);
        createHeader(sheet2, startMonth, endMonth, subjects);
        createHeader(sheet3, startMonth, endMonth, subjects);

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
        int excelSize = excelEnvRows.size();
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
            Cell cell10 = row.createCell(10);
            cell10.setCellValue(excelRow.getREIT());
            rowCount++;
            if (rowCount == excelSize + 4) {
                Row rowTotal = sheet.createRow(rowCount + 2);
                Cell grand = rowTotal.createCell(1);
                grand.setCellValue("Grand total: ");
                setFontStyle(grand, workbook);
                sheet.setColumnWidth(1, 6000);
                rowTotal.createCell(2).setCellFormula("SUM(C6:C" + (rowCount + 1) + ")");
                rowTotal.createCell(3).setCellFormula("SUM(D6:D" + (rowCount + 1) + ")");
                rowTotal.createCell(4).setCellFormula("SUM(E6:E" + (rowCount + 1) + ")");
                rowTotal.createCell(5).setCellFormula("SUM(F6:F" + (rowCount + 1) + ")");
                rowTotal.createCell(6).setCellFormula("SUM(G6:G" + (rowCount + 1) + ")");
                rowTotal.createCell(7).setCellFormula("SUM(H6:H" + (rowCount + 1) + ")");
                rowTotal.createCell(8).setCellFormula("SUM(I6:I" + (rowCount + 1) + ")");
                rowTotal.createCell(9).setCellFormula("SUM(J6:J" + (rowCount + 1) + ")");
                rowTotal.createCell(10).setCellFormula("SUM(K6:K" + (rowCount + 1) + ")");

            } else {
                Utils.showMsg("==>> Here: " + String.valueOf(rowCount) + " : Total: " + String.valueOf(excelSize));
            }
        }

        int rowCount1 = 5;
        int excelSize1 = excelMgrRows.size();
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
            if (rowCount1 == excelSize1 + 4) {
                Row rowTotal = sheet1.createRow(rowCount1 + 2);
                Cell grand = rowTotal.createCell(1);
                grand.setCellValue("Grand total: ");
                setFontStyle(grand, workbook);
                sheet1.setColumnWidth(1, 6000);
                rowTotal.createCell(2).setCellFormula("SUM(C6:C" + (rowCount1 + 1) + ")");
                rowTotal.createCell(3).setCellFormula("SUM(D6:D" + (rowCount1 + 1) + ")");
                rowTotal.createCell(4).setCellFormula("SUM(E6:E" + (rowCount1 + 1) + ")");
                rowTotal.createCell(5).setCellFormula("SUM(F6:F" + (rowCount1 + 1) + ")");
                rowTotal.createCell(6).setCellFormula("SUM(G6:G" + (rowCount1 + 1) + ")");
                rowTotal.createCell(7).setCellFormula("SUM(H6:H" + (rowCount1 + 1) + ")");
            } else {
                Utils.showMsg("==>> Here: " + String.valueOf(rowCount1) + " : Total: " + String.valueOf(excelSize));
            }
        }
        int rowCount2 = 5;
        int excelSize2 = excelPICRows.size();
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
            if (rowCount2 == excelSize2 + 4) {
                Row rowTotal = sheet2.createRow(rowCount2 + 2);
                Cell grand = rowTotal.createCell(4);
                grand.setCellValue("Grand total: ");
                setFontStyle(grand, workbook);
                sheet2.setColumnWidth(4, 6000);
                rowTotal.createCell(5).setCellFormula("SUM(F6:F" + (rowCount2 + 1) + ")");
                rowTotal.createCell(6).setCellFormula("SUM(G6:G" + (rowCount2 + 1) + ")");
                rowTotal.createCell(7).setCellFormula("SUM(H6:H" + (rowCount2 + 1) + ")");
            } else {
                Utils.showMsg("==>> Here: " + String.valueOf(rowCount2) + " : Total: " + String.valueOf(excelSize));
            }
        }
        int rowCount3 = 5;
        int excelSize3 = excelTLRows.size();
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
            if (rowCount3 == excelSize3 + 4) {
                Row rowTotal = sheet3.createRow(rowCount3 + 2);
                Cell grand = rowTotal.createCell(4);
                grand.setCellValue("Grand total: ");
                setFontStyle(grand, workbook);
                sheet3.setColumnWidth(4, 6000);
                rowTotal.createCell(5).setCellFormula("SUM(F6:F" + (rowCount3 + 1) + ")");
                rowTotal.createCell(6).setCellFormula("SUM(G6:G" + (rowCount3 + 1) + ")");
                rowTotal.createCell(7).setCellFormula("SUM(H6:H" + (rowCount3 + 1) + ")");
            } else {
                Utils.showMsg("==>> Here: " + String.valueOf(rowCount3) + " : Total: " + String.valueOf(excelSize));
            }
        }

        try {
            outStream = response.getOutputStream();
            workbook.write(outStream);
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

    private void createHeader(XSSFSheet sheet, String startMonth, String endMonth, String subjects) {
        Row dateRowRow = sheet.createRow(1);
        Cell clDateH = dateRowRow.createCell(0);
        clDateH.setCellValue("Closing Date");
        Cell clDate = dateRowRow.createCell(1);
        clDate.setCellValue(startMonth + " to "+ endMonth);

        Row subRow = sheet.createRow(2);
        Cell subH = subRow.createCell(0);
        subH.setCellValue("Subject");
        Cell sub = subRow.createCell(1);
        sub.setCellValue(subjects);
    }

    private String getQuery(HttpServletRequest request) {
        String query = "";
        
        String from = request.getParameter("from");
        String to = request.getParameter("to");
        String year = request.getParameter("year");
        
        Utils.showMsg("==>> "+year);
        
        String subject = request.getParameter("subject");
        String teamLeader = request.getParameter("teamLeader");
        String glManager = request.getParameter("glManager");
        String preparer = request.getParameter("preparer");

        String[] array = null;
        
        String fromDate = year+"-"+from;
        String toDate = year+"-"+to;
        
        query = "select distinct id, c_env, c_manager_name,c_company_id, c_pic_name, c_tlName, c_int_kpi_status, c_ext_kpi_status, c_sub_id, c_close_mnth,c_close_month_report, c_manager_name \n"
                + " from  app_fd_rss_request_detail Where c_close_month_report BETWEEN '" + fromDate + "' AND '" + toDate  +"' \n";

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

        return query;
    }

    private List<Model> createEnvSheet(String startMonth, String endMonth, String query) {
        QueryHandler qh = new QueryHandler(this);
        HashMap<String, Model> kpiCount = new HashMap<String, Model>();
        String mQuery = "";
        List<String> intStatus = getStatus("internal");
        List<String> extStatus = getStatus("external");
        List<Model> excelList = new ArrayList<Model>();

        if (!startMonth.equalsIgnoreCase("")) {
            mQuery = "select distinct c_sub_id as col from app_fd_rss_request_detail Where c_close_mnth BETWEEN '"+startMonth+"' AND ?";
            subList = qh.getInfo(mQuery, endMonth);
            mQuery = "select distinct c_env as col from app_fd_rss_request_detail Where c_close_mnth BETWEEN '"+startMonth+"' AND ?";
            envList = qh.getInfo(mQuery, endMonth);

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
                                }else if (envList.get(j).equalsIgnoreCase("REIT")) {
                                    mod.setREIT(intCount);
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
                                }else if (envList.get(j).equalsIgnoreCase("REIT")) {
                                    mod.setREIT(intCount);
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
                                }else if (envList.get(j).equalsIgnoreCase("REIT")) {
                                    mod.setREIT(extCount);
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
                                }else if (envList.get(j).equalsIgnoreCase("REIT")) {
                                    mod.setREIT(extCount);
                                }
                                if (extCount != 0) {
                                    kpiCount.put(extStatus.get(m) + subList.get(i), mod);
                                }
                            }

                        }
                    }

                }
            } else {
                Utils.showMsg("else loop ");
            }
        }

        for (Model url : kpiCount.values()) {
            excelList.add(url);
        }
        Comparator<Model> compareById = (Model o1, Model o2) -> o1.getSubject().compareTo(o2.getSubject());
        Collections.sort(excelList, compareById);
        return excelList;
    }

//    //filter by manager
    private List<Model> createManagerSheet(String startMonth, String endMonth, String query) {
        QueryHandler qh = new QueryHandler(this);
        HashMap<String, Model> kpiCount = new HashMap<String, Model>();
        String mQuery = "";
        List<String> intStatus = getStatus("internal");
        List<String> extStatus = getStatus("external");
        List<Model> excelList = new ArrayList<Model>();

        if (!startMonth.equalsIgnoreCase("")) {
            mQuery = "select distinct c_sub_id as col from app_fd_rss_request_detail Where c_close_mnth BETWEEN '"+startMonth+"' AND ?";
            subList = qh.getInfo(mQuery, endMonth);
            mQuery = "select distinct c_manager_name as col from app_fd_rss_request_detail Where c_close_mnth BETWEEN '"+startMonth+"' AND ?";
            mgrList = qh.getInfo(mQuery, endMonth);
            List<Model> actualData = qh.getKPITasksByMonth(query);
            if (!actualData.isEmpty()) {
                for (int i = 0; i < subList.size(); i++) {
                    for (int j = 0; j < mgrList.size(); j++) {
                        for (int k = 0; k < intStatus.size(); k++) {
                            int intCount = 0;
                            Model mod = new Model();
                            for (int l = 0; l < actualData.size(); l++) {
                                if (actualData.get(l).getManager().equalsIgnoreCase(mgrList.get(j))) {
                                    if (actualData.get(l).getSubject().equalsIgnoreCase(subList.get(i))) {
                                        if (actualData.get(l).getIntKpiStatus().equalsIgnoreCase(intStatus.get(k))) {
                                            intCount++;
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
                            int extCount = 0;
                            Model mod = new Model();
                            for (int l = 0; l < actualData.size(); l++) {
                                if (actualData.get(l).getManager().equalsIgnoreCase(mgrList.get(j))) {
                                    if (actualData.get(l).getSubject().equalsIgnoreCase(subList.get(i))) {
                                        if (actualData.get(l).getExtKpiStatus().equalsIgnoreCase(extStatus.get(m))) {
                                            extCount++;
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
                Utils.showMsg("else loop ");
            }
        }
        for (Model url : kpiCount.values()) {
            excelList.add(url);
        }
        Comparator<Model> compareById = (Model o1, Model o2) -> o1.getSubject().compareTo(o2.getSubject());
        Collections.sort(excelList, compareById);
        return excelList;
    }

    private List<Model> createPICSheet(String startMonth, String endMonth, String query) {
        QueryHandler qh = new QueryHandler(this);
        String mquery = "";
        HashMap<String, Model> kpiCount = new HashMap<String, Model>();
        List<String> intStatus = getStatus("internal");
        List<Model> excelList = new ArrayList<Model>();

        if (!startMonth.equalsIgnoreCase("")) {
            mquery = "select distinct c_sub_id as col from app_fd_rss_request_detail Where c_close_mnth BETWEEN '"+startMonth+"' AND ?";
            subList = qh.getInfo(mquery, endMonth);
            mquery = "select distinct c_env as col from app_fd_rss_request_detail Where c_close_mnth BETWEEN '"+startMonth+"' AND ? ";
            envList = qh.getInfo(mquery, endMonth);
            mquery = "select distinct c_company_id as col from app_fd_rss_request_detail Where c_close_mnth BETWEEN '"+startMonth+"' AND ? ";
            companyList = qh.getInfo(mquery, endMonth);
            mquery = "select distinct c_pic_name as col from app_fd_rss_request_detail Where c_close_mnth BETWEEN '"+startMonth+"' AND ? ";
            picList = qh.getInfo(mquery, endMonth);
            mquery = "select distinct c_manager_name as col from app_fd_rss_request_detail Where c_close_mnth BETWEEN '"+startMonth+"' AND ? ";
            mgrList = qh.getInfo(mquery, endMonth);

            List<Model> actualData = qh.getKPITasksByMonth(query);
            if (!actualData.isEmpty()) {
                for (int i = 0; i < subList.size(); i++) {
                    for (int j = 0; j < envList.size(); j++) {
                        for (int k = 0; k < mgrList.size(); k++) {
                            for (int l = 0; l < companyList.size(); l++) {
                                for (int m = 0; m < picList.size(); m++) {
                                    for (int n = 0; n < intStatus.size(); n++) {
                                        int intCount = 0;
                                        Model mod = new Model();
                                        for (int p = 0; p < actualData.size(); p++) {
                                            if (actualData.get(p).getEnv().equalsIgnoreCase(envList.get(j))) {
                                                if (actualData.get(p).getSubject().equalsIgnoreCase(subList.get(i))) {
                                                    if (actualData.get(p).getManager().equalsIgnoreCase(mgrList.get(k))) {
                                                        if (actualData.get(p).getPicName().equalsIgnoreCase(picList.get(m))) {
                                                            if (actualData.get(p).getCompany().equalsIgnoreCase(companyList.get(l))) {
                                                                if (actualData.get(p).getIntKpiStatus().equalsIgnoreCase(intStatus.get(n))) {
                                                                    intCount++;
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
                                    }
                                }
                            }
                        }
                    }

                }
            } else {
                Utils.showMsg("else loop ");
            }
        }
        for (Model url : kpiCount.values()) {
            excelList.add(url);
        }
        Comparator<Model> compareById = (Model o1, Model o2) -> o1.getSubject().compareTo(o2.getSubject());
        Collections.sort(excelList, compareById);
        return excelList;
    }

    private List<Model> createTLSheet(String startMonth, String endMonth, String query) {
        QueryHandler qh = new QueryHandler(this);
        String mquery = "";
        HashMap<String, Model> kpiCount = new HashMap<String, Model>();
        List<String> extStatus = getStatus("external");
        List<Model> excelList = new ArrayList<Model>();

        if (!startMonth.equalsIgnoreCase("")) {
            mquery = "select distinct c_sub_id as col from app_fd_rss_request_detail Where c_close_mnth BETWEEN '"+startMonth+"' AND  ?";
            subList = qh.getInfo(mquery, endMonth);
            mquery = "select distinct c_env as col from app_fd_rss_request_detail Where c_close_mnth BETWEEN '"+startMonth+"' AND ? ";
            envList = qh.getInfo(mquery, endMonth);
            mquery = "select distinct c_company_id as col from app_fd_rss_request_detail Where c_close_mnth BETWEEN '"+startMonth+"' AND ? ";
            companyList = qh.getInfo(mquery, endMonth);
            mquery = "select distinct c_tlName as col from app_fd_rss_request_detail Where c_close_mnth BETWEEN '"+startMonth+"' AND ? ";
            tlList = qh.getInfo(mquery, endMonth);
            mquery = "select distinct c_manager_name as col from app_fd_rss_request_detail Where c_close_mnth BETWEEN '"+startMonth+"' AND ? ";
            mgrList = qh.getInfo(mquery, endMonth);

            List<Model> actualData = qh.getKPITasksByMonth(query);
            if (!actualData.isEmpty()) {
                for (int i = 0; i < subList.size(); i++) {
                    for (int j = 0; j < envList.size(); j++) {
                        for (int k = 0; k < mgrList.size(); k++) {
                            for (int l = 0; l < companyList.size(); l++) {
                                for (int m = 0; m < tlList.size(); m++) {
                                    for (int n = 0; n < extStatus.size(); n++) {
                                        int extCount = 0;
                                        Model mod = new Model();
                                        for (int p = 0; p < actualData.size(); p++) {
                                            if (actualData.get(p).getEnv().equalsIgnoreCase(envList.get(j))) {
                                                if (actualData.get(p).getSubject().equalsIgnoreCase(subList.get(i))) {
                                                    if (actualData.get(p).getManager().equalsIgnoreCase(mgrList.get(k))) {
                                                        if (actualData.get(p).getTlName().equalsIgnoreCase(tlList.get(m))) {
                                                            if (actualData.get(p).getCompany().equalsIgnoreCase(companyList.get(l))) {
                                                                if (actualData.get(p).getExtKpiStatus().equalsIgnoreCase(extStatus.get(n))) {
                                                                    extCount++;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
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
                                    }
                                }
                            }
                        }
                    }

                }
            } else {
                Utils.showMsg("else loop ");
            }
        }
        for (Model url : kpiCount.values()) {
            excelList.add(url);
        }
        Comparator<Model> compareById = (Model o1, Model o2) -> o1.getSubject().compareTo(o2.getSubject());
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
