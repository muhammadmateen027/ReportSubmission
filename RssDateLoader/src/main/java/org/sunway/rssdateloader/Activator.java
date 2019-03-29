package org.sunway.rssdateloader;

import org.sunway.rssdateloader.formdataloader.FormDataLoaderClass;
import java.util.ArrayList;
import java.util.Collection;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.sunway.rssdateloader.excel.ExcelTemplate;
import org.sunway.rssdateloader.storebinder.StoreBinderMain;

public class Activator implements BundleActivator {

    protected Collection<ServiceRegistration> registrationList;

    public void start(BundleContext context) {
        registrationList = new ArrayList<ServiceRegistration>();

        //Register plugin here
        registrationList.add(context.registerService(FormDataLoaderClass.class.getName(), new FormDataLoaderClass(), null));
        registrationList.add(context.registerService(StoreBinderMain.class.getName(), new StoreBinderMain(), null));
	registrationList.add(context.registerService(ExcelTemplate.class.getName(), new ExcelTemplate(), null));
    }

    public void stop(BundleContext context) {
        for (ServiceRegistration registration : registrationList) {
            registration.unregister();
        }
    }
}