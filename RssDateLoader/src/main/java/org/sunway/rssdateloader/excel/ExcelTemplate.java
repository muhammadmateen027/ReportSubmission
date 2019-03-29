/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sunway.rssdateloader.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.Element;
import org.joget.apps.form.model.FormData;
import org.joget.commons.util.LogUtil;
import org.joget.plugin.base.PluginWebSupport;
import org.json.JSONArray;
import org.springframework.context.ApplicationContext;
import org.sunway.rssdateloader.utilities.Utils;

/**
 *
 * @author kirthikan
 */
public class ExcelTemplate extends Element implements PluginWebSupport {

    final String pluginName = "RSS - ExcelTemplate";
    final String version = "1.0";
    JSONArray jSONArray;

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
        String filename = "DCC-" + new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date()) + ".xlsx";
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        ServletOutputStream outStream;

//        Collections.sort(excelRows); 
// UserDetailModel required to implement        
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

}
