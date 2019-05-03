package org.sunway.rssdateloader;

import org.sunway.rssdateloader.formdataloader.FormDataLoaderClass;
import java.util.ArrayList;
import java.util.Collection;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.sunway.rssdateloader.emailreminder.DailyReminder;
import org.sunway.rssdateloader.emailreminder.MonthlyReminder;
import org.sunway.rssdateloader.excel.ExcelTemplate;
import org.sunway.rssdateloader.loadDataKPI.LoadProfileDataClass;
import org.sunway.rssdateloader.schedular.WithdrawSchedular;
import org.sunway.rssdateloader.storebinder.MasterSetupClass;
import org.sunway.rssdateloader.storebinder.StoreBinderMain;

public class Activator implements BundleActivator {

    protected Collection<ServiceRegistration> registrationList;

    public void start(BundleContext context) {
        registrationList = new ArrayList<ServiceRegistration>();

        //Register plugin here
        registrationList.add(context.registerService(FormDataLoaderClass.class.getName(), new FormDataLoaderClass(), null));
        registrationList.add(context.registerService(StoreBinderMain.class.getName(), new StoreBinderMain(), null));
	registrationList.add(context.registerService(ExcelTemplate.class.getName(), new ExcelTemplate(), null));
        registrationList.add(context.registerService(LoadProfileDataClass.class.getName(), new LoadProfileDataClass(), null));
        registrationList.add(context.registerService(MasterSetupClass.class.getName(), new MasterSetupClass(), null));
        registrationList.add(context.registerService(MonthlyReminder.class.getName(), new MonthlyReminder(), null));
        registrationList.add(context.registerService(DailyReminder.class.getName(), new DailyReminder(), null));
        registrationList.add(context.registerService(WithdrawSchedular.class.getName(), new WithdrawSchedular(), null));
        

    }

    public void stop(BundleContext context) {
        for (ServiceRegistration registration : registrationList) {
            registration.unregister();
        }
    }
}