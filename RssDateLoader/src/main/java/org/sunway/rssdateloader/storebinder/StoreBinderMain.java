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

/**
 *
 * @author kirthikan
 */
public class StoreBinderMain extends WorkflowFormBinder {

    public String getName() {
        return "ESCA - CustomStoreBinder";
    }

    public String getVersion() {
        return "1.0";
    }

    public String getDescription() {
        return "ESCA - CustomStoreBinder";
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
        return null;
    }
}
