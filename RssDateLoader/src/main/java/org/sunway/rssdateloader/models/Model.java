/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sunway.rssdateloader.models;

/**
 *
 * @pc omen
 * @author Mateen
 */
public class Model {
    String id="";
    String actionTime="";
    
    String env = "";
    String intKpiStatus = "";

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getIntKpiStatus() {
        return intKpiStatus;
    }

    public void setIntKpiStatus(String intKpiStatus) {
        this.intKpiStatus = intKpiStatus;
    }

    public String getExtKpiStatus() {
        return extKpiStatus;
    }

    public void setExtKpiStatus(String extKpiStatus) {
        this.extKpiStatus = extKpiStatus;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
    String extKpiStatus = "";
    String subject = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActionTime() {
        return actionTime;
    }

    public void setActionTime(String actionTime) {
        this.actionTime = actionTime;
    }
}
