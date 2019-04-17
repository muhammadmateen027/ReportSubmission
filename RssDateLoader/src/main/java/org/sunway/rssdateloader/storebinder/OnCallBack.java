/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sunway.rssdateloader.storebinder;

/**
 *
 * @pc omen
 * @author Mateen
 */
public interface OnCallBack {
    public void onSuccess();
    public void onFailure();
    
    public void sendEmail(String status);
    public void sendEmailOnFirstForm(String status, String formId);
}
