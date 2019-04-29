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

/**
 *
 * @author kirthikan
 */
public class ExcelCreater extends Element implements PluginWebSupport, QueryHandlerInterface {

    final String pluginName = "RSS - ExcelTemplate";
    final String version = "1.0";
    JSONArray jSONArray;
    List<String> subList = null;
    List<String> envList = null;
    List<String> mgrList = null;
    List<String> picList = null;
    List<String> tlList = null;
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

        // Utils.showMsg("excel creation");
        String filename = "RSS-" + new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date()) + ".xlsx";
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        // Utils.showMsg("0");
        ServletOutputStream outStream;
        String closingDate = request.getParameter("closingDate");
        closingDate = "04-2019";
        List<Model> excelEnvRows = createEnvSheet(closingDate);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("By Environment");
        XSSFSheet sheet1 = workbook.createSheet("By Manager");
        XSSFSheet sheet2 = workbook.createSheet("By PIC");
        XSSFSheet sheet3 = workbook.createSheet("By TeamLeader");

        // To create Heading
//        String[] headings = {"KPI Status", "KPI Tasks", "Environment", "Count"};
        String[] headings = {"KPI Status", "KPI Tasks", "TestEnv", "Construction", "Dekon", "Emerging", "Hotel", "Medical", "PDD", "PI"};
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
        // Utils.showMsg("column count done");
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
                cell3.setCellValue(excelRow.getCount());
            } else if (excelRow.getEnv().equalsIgnoreCase("Dekon")) {
                Cell cell4 = row.createCell(4);
                cell4.setCellValue(excelRow.getCount());
            } else if (excelRow.getEnv().equalsIgnoreCase("Emerging")) {
                Cell cell5 = row.createCell(5);
                cell5.setCellValue(excelRow.getCount());
            } else if (excelRow.getEnv().equalsIgnoreCase("Hotel")) {
                Cell cell6 = row.createCell(6);
                cell6.setCellValue(excelRow.getCount());
            } else if (excelRow.getEnv().equalsIgnoreCase("Medical")) {
                Cell cell7 = row.createCell(7);
                cell7.setCellValue(excelRow.getCount());
            } else if (excelRow.getEnv().equalsIgnoreCase("PDD")) {
                Cell cell8 = row.createCell(8);
                cell8.setCellValue(excelRow.getCount());
            } else if (excelRow.getEnv().equalsIgnoreCase("PI")) {
                Cell cell9 = row.createCell(9);
                cell9.setCellValue(excelRow.getPI());
            }
            rowCount++;

        }
        // Utils.showMsg("row count done");

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

    private List<Model> createEnvSheet(String closingDate) {
        QueryHandler qh = new QueryHandler(this);
        String query = "";

        List<String> intStatus = getStatus("internal");
        List<String> extStatus = getStatus("external");
        List<Model> excelList = new ArrayList<Model>();

        if (!closingDate.equalsIgnoreCase("")) {
            query = "select distinct c_sub_id as col from app_fd_rss_request_detail Where c_close_mnth =  ?";
            subList = qh.getInfo(query, closingDate);

            // Utils.showMsg("a");
            query = "select distinct c_env as col from app_fd_rss_request_detail Where c_close_mnth = ? ";
            envList = qh.getInfo(query, closingDate);

            for (int i = 0; i < intStatus.size(); i++) {
                Model model = new Model();
                for (int j = 0; j < subList.size(); j++) {
                    for (int k = 0; j < envList.size(); k++) {
                        
                    }
                }
            }

            // Utils.showMsg("b");
            List<Model> actualData = qh.getKPITasksByMonth(closingDate);
            // Utils.showMsg("c");

        }
        Utils.showMsg("Excel Size: " + String.valueOf(excelList.size()));
        return excelList;
    }

    private Model getResults(List<Model> excelList, Model mod, int counts) {

        if (excelList.size() == 0) {
            if (mod.getEnv().equalsIgnoreCase("TestEnv")) {
                mod.setTestEnv(counts);
            }
            if (mod.getEnv().equalsIgnoreCase("PI")) {
                mod.setPI(counts);
            }
        } else {

            for (int i = 0; i < excelList.size(); i++) {
                Utils.showMsg("1");
                if (excelList.get(i).getKpiStatus().equalsIgnoreCase(mod.getKpiStatus())) {
                    if (excelList.get(i).getSubject().equalsIgnoreCase(mod.getSubject())) {
                        Utils.showMsg("Here in If: " + counts);
                        if (mod.getEnv().equalsIgnoreCase("TestEnv")) {
                            excelList.get(i).setTestEnv(counts);
                        } else if (mod.getEnv().equalsIgnoreCase("PI")) {
                            excelList.get(i).setPI(counts);
                        }

                        Utils.showMsg("If: New Settled: " + excelList.get(i).getTestEnv() + " : " + excelList.get(i).getPI());
                    } else {
                        Utils.showMsg("Here in Else: " + counts);
                        if (mod.getEnv().equalsIgnoreCase("TestEnv")) {
                            mod.setTestEnv(counts);
                        } else if (mod.getEnv().equalsIgnoreCase("PI")) {
                            mod.setPI(counts);
                        }

                        Utils.showMsg("Else: New Settled: " + mod.getTestEnv() + " : " + mod.getPI());
                    }
                }

            }
        }
        Utils.showMsg("Status: " + mod.getKpiStatus()
                + ", Subj: " + mod.getSubject()
                + ", Env: " + mod.getEnv()
                + ", Count : " + String.valueOf(mod.getCount()));

        return mod;
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


//select  c_int_kpi_status, c_env,c_sub_id, c_close_mnth,count(*) as total
//	from app_fd_rss_request_detail 
//    where c_close_mnth = '04-2019'
//    group by c_int_kpi_status, c_env,c_sub_id, c_close_mnth;