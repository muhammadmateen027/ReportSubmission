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
public class KPIModel {
    String internalWD;
    String externalWD;
    String internalDate;
    String externalDate;
    String environment;

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getInternalWD() {
        return internalWD;
    }

    public void setInternalWD(String internalWD) {
        this.internalWD = internalWD;
    }

    public String getExternalWD() {
        return externalWD;
    }

    public void setExternalWD(String externalWD) {
        this.externalWD = externalWD;
    }

    public String getInternalDate() {
        return internalDate;
    }

    public void setInternalDate(String internalDate) {
        this.internalDate = internalDate;
    }

    public String getExternalDate() {
        return externalDate;
    }

    public void setExternalDate(String externalDate) {
        this.externalDate = externalDate;
    }
    
    
}
