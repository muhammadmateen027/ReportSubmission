/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sunway.rssdateloader;

import java.sql.ResultSet;

/**
 *
 * @pc omen
 * @author Mateen
 */
public interface QueryHandlerInterface {

    public void onSuccess(ResultSet rSet);

    public void onFailure();
    
    public void onHolidayCallBack(ResultSet rSet);
}
