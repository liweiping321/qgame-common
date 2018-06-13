package com.mokylin.htoswap;

import java.lang.instrument.Instrumentation;

/**
 * Created by liweiping on 2018/6/8.
 */
public class JavaDynAgent {
    private static Instrumentation instrumentation;
    private static Object lockObject = new Object();

    public JavaDynAgent() {
    }

    public static void agentmain(String args, Instrumentation inst) {
        synchronized(lockObject) {
            if(instrumentation == null) {
                instrumentation = inst;
            }
        }
    }

    public static Instrumentation getInstrumentation() {
        return instrumentation;
    }
}
