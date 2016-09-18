package com.zyx.unitpro;

import com.orhanobut.logger.Logger;
import com.zyx.unitpro.test.tool.RoboBase;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by zhangyouxuan on 2016/9/18.
 */
public class LoggerTest extends RoboBase{

    @BeforeClass
    public static void setClass(){
        Logger.init("LoggerTest");
    }

    @Test
    public void testLogger(){
        Logger.i("test logger type format.");
    }
}
