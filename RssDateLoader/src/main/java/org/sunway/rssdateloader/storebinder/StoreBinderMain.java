/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sunway.rssdateloader.storebinder;

import org.joget.apps.form.lib.WorkflowFormBinder;
import org.joget.apps.form.model.Element;
import org.joget.apps.form.model.FormData;
import org.joget.apps.form.model.FormRowSet;
import org.sunway.rssdateloader.databases.QueryHandler;

/**
 *
 * @author kirthikan
 */
public class StoreBinderMain extends WorkflowFormBinder {

    final String pliuginName = "RSS - CustomStoreBinder";
    public String getName() {
        return pliuginName;
    }

    public String getVersion() {
        return "1.0";
    }

    public String getDescription() {
        return pliuginName;
    }

    public String getLabel() {
        return getName();
    }

    public String getClassName() {
        return getClass().getName();
    }

    public String getPropertyOptions() {
        return "";
    }

    @Override
    public FormRowSet store(Element element, FormRowSet rowSet, FormData formData) {
        
        String formId = super.getFormId(); // Take form Id to detect and perform action on Specific form
        if (formId.equalsIgnoreCase("request_detail_form")) {
            RequestForm form = new RequestForm(rowSet);
            form.performAction();
        }
        FormRowSet fm = super.store(element, rowSet, formData);
        return fm;
    }
}
