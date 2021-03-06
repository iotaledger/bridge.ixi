package org.iota.ict.ixi;

import org.iota.ict.Ict;
import org.iota.ict.utils.properties.Properties;

/**
 * This class is just for testing bridge.ixi, so you don't have to run Ict manually.
 * */
public class Main {

    public static void main(String[] args) throws Exception {

        Properties properties = new Properties();
        Ict ict = new Ict(properties.toFinal());
        ict.getModuleHolder().loadVirtualModule(Bridge.class, "Bridge.ixi");
        ict.getModuleHolder().startAllModules();

    }

}