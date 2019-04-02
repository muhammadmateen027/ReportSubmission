/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sunway.rssdateloader.databases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.sql.DataSource;
import org.joget.apps.app.service.AppUtil;
import org.joget.commons.util.LogUtil;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.sunway.rssdateloader.formdataloader.QueryHandlerInterface;
import org.sunway.rssdateloader.utilities.Utils;

/**
 *
 * @pc omen
 * @author Mateen
 */
public class QueryHandler {

    public QueryHandlerInterface queryHandlerInterface;

    public QueryHandler(QueryHandlerInterface aClass) {
        this.queryHandlerInterface = aClass;
    }

    public void getPublicHolidays() {
        String query = "Select c_holiday_date from app_fd_cs_public_holiday order by c_holiday_date asc ";
        Connection con = getDatabaseConnection();

        try {
            PreparedStatement statement = con.prepareStatement(query);
            ResultSet rSet = statement.executeQuery();
            queryHandlerInterface.onHolidayCallBack(rSet);

        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (con != null) {
                closeDatabaseConnection(con);
            }
        }
    }

    public void getEnvCompExistance(String company) {
        String query = "SELECT EXISTS(SELECT 1 FROM app_fd_rss_cmpyEnv_table where c_comapny_id = ?) as existance ";
        Connection con = getDatabaseConnection();

        try {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, company);
            ResultSet rSet = statement.executeQuery();
            queryHandlerInterface.onSuccess(rSet);

        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (con != null) {
                closeDatabaseConnection(con);
            }
        }
    }

    public JSONArray getKPIData(String comp, String sub, String year, String wdMonth) throws JSONException {
        JSONArray jArr = new JSONArray();
        String query = "Select distinct id, c_environment_id, c_f_manager_name, c_f_manager_id, c_kpi_target, " + wdMonth
                + " from app_fd_rss_cmp_kpi_profile WHERE c_company_id = ? AND c_subject_id = ? AND c_year_id = ? ";
        Connection con = getDatabaseConnection();

        try {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, comp);
            statement.setString(2, sub);
            statement.setString(3, year);
            ResultSet rSet = statement.executeQuery();
            if (rSet != null) {
                queryHandlerInterface.onSuccess(rSet);
            } else {
                queryHandlerInterface.onFailure();
            }
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (con != null) {
                closeDatabaseConnection(con);
            }
        }
        return jArr;
    }

    private Connection getDatabaseConnection() {
        Connection con = null;
        DataSource ds = (DataSource) AppUtil.getApplicationContext().getBean("setupDataSource");
        try {
            con = ds.getConnection();
        } catch (SQLException ex) {
            LogUtil.error(this.getClass().getName(), ex, ex.getMessage());
        }
        return con;
    }

    private void closeDatabaseConnection(Connection con) {
        try {
            con.close();
        } catch (SQLException ex) {
            LogUtil.error(this.getClass().getName(), ex, ex.getMessage());
        }
    }

    public void getManagerData(String comp, String sub) {
        Utils.showMsg("query start");
        String query = "Select distinct c_manager_id, c_manager_id, c_manager_names, c_teamLead,c_buFinance from app_fd_rss_kpiProfileSetup Where c_cmpy = ? AND c_sub = ? ";
        Connection con = getDatabaseConnection();

        try {
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, comp);
            statement.setString(2, sub);
            ResultSet rSet = statement.executeQuery();
            if (rSet != null) {
                queryHandlerInterface.onSuccess(rSet);
            } else {
                queryHandlerInterface.onFailure();
            }
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (con != null) {
                closeDatabaseConnection(con);
            }
        }
    }
}
