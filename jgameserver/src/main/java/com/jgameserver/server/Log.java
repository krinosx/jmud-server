package com.jgameserver.server;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Log
 */
public class Log {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");

    public static boolean debugEnabled = false;

    public static void info(String msg, Class<?> origin){
        writeLog(msg, "INFO", origin.getSimpleName());
    }

    public static void error(String msg, Class<?> origin){
        writeLog(msg, "ERROR", origin.getSimpleName());
    }

    public static void debug(String msg, Class<?> origin){
        if( debugEnabled ) {
            writeLog(msg, "DEBUG", origin.getSimpleName());
        }
    }

    private static void writeLog(String log, String type, String classe) {

        System.out.printf("[ %s ] %s - %s : %s%n", simpleDateFormat.format(new Date()), type, classe, log );

    }


}
