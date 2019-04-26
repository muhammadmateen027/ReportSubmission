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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.sql.DataSource;
import org.joget.apps.app.service.AppUtil;
import org.joget.commons.util.LogUtil;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.sunway.rssdateloader.formdataloader.QueryHandlerInterface;
import org.sunway.rssdateloader.models.Model;
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

    public void updateHistoryLog(String uId, String parentId, String userName, String logStatus, String logRemarks) {
        String query = "INSERT INTO app_audit_rss_historyLog (id, appDefId, rowId, loggedBy, logStatus, logRemarks) VALUES (?, ?, ?, ?, ?, ?)";
        Connection con = getDatabaseConnection();
        PreparedStatement stmtInsert;
        try {
            stmtInsert = con.prepareStatement(query);

            stmtInsert.setString(1, uId);
            stmtInsert.setString(2, "fssrss");
            stmtInsert.setString(3, parentId);
            stmtInsert.setString(4, userName);
            stmtInsert.setString(5, logStatus);
            stmtInsert.setString(6, logRemarks);
            stmtInsert.executeUpdate();
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
        String query = "Select distinct id, c_environment_id, c_f_manager_name, c_f_manager_id, c_f_pic_name, c_f_pic_id, c_kpi_target, " + wdMonth
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
        String query = "Select distinct c_manager_id, c_manager_id, c_manager_name, c_pic_id, c_pic_name from app_fd_rss_kpiProfileSetup Where c_company_id = ? AND c_sub_id = ? ";
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

    public String getUserEmail(String username) {
        String userEmail = "";
        String query = "select email from dir_user where username = ?";
        Connection con = getDatabaseConnection();
        try {
            PreparedStatement stmt2 = con.prepareStatement(query);
            stmt2.setString(1, username);
            ResultSet rSet = stmt2.executeQuery();
            while (rSet.next()) {
                userEmail = rSet.getString("email");
            }
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (con != null) {
                closeDatabaseConnection(con);
            }
        }
        return userEmail;
    }

    public String getUniqueId(String refNo) {
        String id = "";
        String query = "select distinct id from app_fd_rss_request_detail where c_refNo =  ?";
        Connection con = getDatabaseConnection();
        try {
            PreparedStatement stmt2 = con.prepareStatement(query);
            stmt2.setString(1, refNo);
            ResultSet rSet = stmt2.executeQuery();
            while (rSet.next()) {
                id = rSet.getString("id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (con != null) {
                closeDatabaseConnection(con);
            }
        }
        return id;
    }

    public String getProcessIdFromFormId(String formId) {
        String processId = "";
        String query = "Select distinct processId from wf_process_link where originProcessId = ? ";
        Connection con = getDatabaseConnection();
        PreparedStatement stmtInsert;
        try {
            stmtInsert = con.prepareStatement(query);

            stmtInsert.setString(1, formId);
            ResultSet rSet = stmtInsert.executeQuery();
            if (rSet != null) {
                while (rSet.next()) {
                    processId = rSet.getString("processId");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (con != null) {
                closeDatabaseConnection(con);
            }
        }
        return processId;
    }

    public void updateRecordStatusById(String id) {
        String query = "UPDATE app_fd_rss_request_detail SET c_status = ? WHERE id = ? ";
        Connection con = getDatabaseConnection();
        PreparedStatement stmtInsert;
        try {
            stmtInsert = con.prepareStatement(query);

            stmtInsert.setString(1, "Closed");
            stmtInsert.setString(2, id);
            stmtInsert.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (con != null) {
                closeDatabaseConnection(con);
            }
        }
    }

    public List<Model> getPendingRecords() {
        List<Model> recList = new ArrayList<Model>();
        String query = "select distinct req.id, req.c_tl_action_time from app_fd_rss_request_detail as req,\n"
                + "	app_fd_rss_subjects as sub \n"
                + "	Where req.c_status = ? \n"
                + "	AND req.c_sub_id = sub.c_sub_id \n"
                + "     AND sub.c_auto_close = ? ";
        Connection con = getDatabaseConnection();
        PreparedStatement stmtInsert;
        try {
            stmtInsert = con.prepareStatement(query);

            stmtInsert.setString(1, "TLApproved");
            stmtInsert.setString(2, "Yes");
            ResultSet rSet = stmtInsert.executeQuery();
            if (rSet != null) {
                while (rSet.next()) {
                    Model model = new Model();
                    model.setId(rSet.getString("id"));
                    String[] parts = rSet.getString("c_tl_action_time").split(" ");
                    model.setActionTime(parts[0]);
                    recList.add(model);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (con != null) {
                closeDatabaseConnection(con);
            }
        }
        return recList;
    }

    private void updateHistoryLog(String uId, String parentId, String fullName) {
        long id = 0;
        String query = "INSERT INTO app_audit_slts_history (id, appDefId, rowId, loggedBy, logStatus) VALUES (?, ?, ?, ?, ?)";
        Connection con = getDatabaseConnection();
        PreparedStatement stmtInsert;
        try {
            stmtInsert = con.prepareStatement(query);

            stmtInsert.setString(1, uId);
            stmtInsert.setString(2, "lsts");
            stmtInsert.setString(3, parentId);
            stmtInsert.setString(4, fullName);
            stmtInsert.setString(5, "Withdrawn");
            stmtInsert.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (con != null) {
                closeDatabaseConnection(con);
            }
        }
    }

    public List<Model> getKPITasksByMonth(String closeMonth) {
        List<Model> list = new ArrayList<Model>();
        String query = "select distinct id, c_env, c_int_kpi_status, c_ext_kpi_status, c_sub_id,c_manager_name, c_close_mnth from "
                + "app_fd_rss_request_detail Where c_close_mnth =  ? ";

        Connection con = getDatabaseConnection();
        PreparedStatement stmtInsert;
        try {
            stmtInsert = con.prepareStatement(query);
            stmtInsert.setString(1, closeMonth);
            ResultSet rSet = stmtInsert.executeQuery();

            if (rSet != null) {
                while (rSet.next()) {
                    Model model = new Model();
                    model.setId(rSet.getString("id"));
                    model.setEnv(rSet.getString("c_env"));
                    model.setSubject(rSet.getString("c_sub_id"));
                    model.setManager(rSet.getString("c_manager_name"));
                    if (!rSet.getString("c_int_kpi_status").equalsIgnoreCase("")
                            || rSet.getString("c_int_kpi_status") != null)
                        model.setIntKpiStatus(rSet.getString("c_int_kpi_status"));
                    else  model.setIntKpiStatus("");
                    if (!rSet.getString("c_ext_kpi_status").equalsIgnoreCase("")
                            || rSet.getString("c_ext_kpi_status") != null) 
                        model.setExtKpiStatus(rSet.getString("c_ext_kpi_status"));
                    else model.setExtKpiStatus("");
                    list.add(model);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (con != null) {
                closeDatabaseConnection(con);
            }
        }
        return list;
    }
    
    public List<String> getInfo(String query, String month) {    
        List<String> list = new ArrayList<String>();
        Connection con = getDatabaseConnection();
        try {
            PreparedStatement stmt2 = con.prepareStatement(query);
            stmt2.setString(1, month);
            ResultSet rSet = stmt2.executeQuery();
            while (rSet.next()) {
                list.add(rSet.getString("col"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (con != null) {
                closeDatabaseConnection(con);
            }
        }
        return list;
    }
}
