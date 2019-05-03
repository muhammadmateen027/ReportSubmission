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
    String intKpiStatus = "";
    String extKpiStatus="";
    String subject="";
    String env = "";
    String kpiStatus="";
    String manager="";
    int count=0;    
    String company="";
    String picName="";
    String tlName="";
    int TestEnv=0;    
    int Construction=0;    
    int Dekon=0;    
    int Emerging=0;    
    int Hotel=0;    
    int Medical=0;    
    int PDD=0;    
    int PI=0; 
    int preExceed=0;
    int preDelay=0;
    int preMeet=0;
    int tlExceed=0;
    int tlDelay=0;
    int tlMeet=0;

    public Model(int testEnv, int pi){
        this.TestEnv = testEnv;
        this.PI = pi;
    }
    
    public Model(){}
    
    public int getPreExceed() {
        return preExceed;
    }

    public void setPreExceed(int preExceed) {
        this.preExceed = preExceed;
    }

    public int getPreDelay() {
        return preDelay;
    }

    public void setPreDelay(int preDelay) {
        this.preDelay = preDelay;
    }
    public int getPreMeet() {
        return preMeet;
    }

    public void setPreMeet(int preMeet) {
        this.preMeet = preMeet;
    }
    public int getTlExceed() {
        return tlExceed;
    }

    public void setTlExceed(int tlExceed) {
        this.tlExceed = tlExceed;
    }

    public int getTlDelay() {
        return tlDelay;
    }

    public void setTlDelay(int tlDelay) {
        this.tlDelay = tlDelay;
    }

    public int getTlMeet() {
        return tlMeet;
    }

    public void setTlMeet(int tlMeet) {
        this.tlMeet = tlMeet;
    }
    public int getTestEnv() {
        return TestEnv;
    }

    public void setTestEnv(int TestEnv) {
        this.TestEnv = TestEnv;
    }

    public int getConstruction() {
        return Construction;
    }

    public void setConstruction(int Construction) {
        this.Construction = Construction;
    }

    public int getDekon() {
        return Dekon;
    }

    public void setDekon(int Dekon) {
        this.Dekon = Dekon;
    }

    public int getEmerging() {
        return Emerging;
    }

    public void setEmerging(int Emerging) {
        this.Emerging = Emerging;
    }

    public int getHotel() {
        return Hotel;
    }

    public void setHotel(int Hotel) {
        this.Hotel = Hotel;
    }

    public int getMedical() {
        return Medical;
    }

    public void setMedical(int Medical) {
        this.Medical = Medical;
    }

    public int getPDD() {
        return PDD;
    }

    public void setPDD(int PDD) {
        this.PDD = PDD;
    }

    public int getPI() {
        return PI;
    }

    public void setPI(int PI) {
        this.PI = PI;
    }


    public String getTlName() {
        return tlName;
    }

    public void setTlName(String tlName) {
        this.tlName = tlName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }


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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getKpiStatus() {
        return kpiStatus;
    }

    public void setKpiStatus(String kpiStatus) {
        this.kpiStatus = kpiStatus;
    }
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
