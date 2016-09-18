package com.zyx.unitpro;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.zyx.unitpro.test.tool.Purpose;
import com.zyx.unitpro.test.tool.RoboBase;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.robolectric.Robolectric;

import java.text.DecimalFormat;

import static org.junit.Assert.assertEquals;

/**
 * Created by zhangyouxuan on 2016/9/1.
 */

public class TimeCostTest extends RoboBase{

    private Context mContext;
    long begin = 0;

    static CostTimeRecoder matchPattern = new CostTimeRecoder();
    static CostTimeRecoder tryCatch = new CostTimeRecoder();

    @BeforeClass
    public static void beforreClass(){
    }

    @AfterClass
    public static void afterClass(){
        double pre = 0.01;
        double ns2ms = 1000000 *pre;
        DecimalFormat df = new DecimalFormat("#.00000");
        String split = "       |           ";
        String space = "        ";
        Log.i("    Solution    ","mIllegalLongData/ns   |  mIllegalSharedData/ns  |   mCommonData/ns ");
        Log.i("    tryCatch    ",space+tryCatch.mIllegalLongData+split+ tryCatch.mIllegalSharedData+split+tryCatch.mCommonData);
        Log.i("  matchPattern  ",space+matchPattern.mIllegalLongData+" "+split+ matchPattern.mIllegalSharedData+" "+split+matchPattern.mCommonData);
        Log.i(" t - m（100*ns）",space+(tryCatch.mIllegalLongData-matchPattern.mIllegalLongData)+split+ (tryCatch.mIllegalSharedData-matchPattern.mIllegalSharedData)+split+(tryCatch.mCommonData-matchPattern.mCommonData));
        Log.i(" t - m（100*ms）",space+df.format((tryCatch.mIllegalLongData-matchPattern.mIllegalLongData)*pre/ns2ms)+split+ df.format((tryCatch.mIllegalSharedData-matchPattern.mIllegalSharedData)*pre/ns2ms)+split+df.format((tryCatch.mCommonData-matchPattern.mCommonData)*pre/ns2ms));
    }

    @Before
    public void setUp(){
        Activity activity = Robolectric.setupActivity(MainActivity.class);
        mContext = activity.getApplicationContext();
    }

    @Test
    @Purpose(desc = "使用Try Catch将非法数据强制转化为long型，转换不成功则置为默认值")
    public void testTryCatch_IllegalLongData(){
        long l = 0L;
        String a = "aaaa";
        begin = System.nanoTime();
        for(int i=0;i<100;i++){
            try{
                l = Long.valueOf(a);
            }catch (Exception e){
                l = 100L;
            }
        }
        tryCatch.mIllegalLongData  = System.nanoTime()-begin;
        assertEquals(100L,l);
    }

    @Test
    @Purpose(desc = "使用正则表达式先判断数据是否为纯数字，再根据结果选择强制转换或置为默认值")
    public void testMatchPattern_IllegalLongData(){
        long l = 0L;
        String a = "aaaa";
        begin = System.nanoTime();
        for(int i=0;i<100;i++){
            l = a.matches("[0-9]+")?Long.valueOf(a):100L;
        }
        matchPattern.mIllegalLongData = System.nanoTime()-begin;
        assertEquals(100L,l);
    }

    @Test
    @Purpose(desc = "使用Try Catch将SharedPreferences中的某字段强制转化为long型，转换不成功，置为默认值")
    public void testTryCatch_IllegalSharedPreData(){
        long l = 0L;
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("test",Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("value","no long");
        editor.commit();
        begin = System.nanoTime();
        for(int i=0;i<100;i++){
            try {
                l = sharedPreferences.getLong("value",200);
            }catch(Exception e){
                l = 200;
            }
        }
        tryCatch.mIllegalSharedData = System.nanoTime()-begin;
        assertEquals(200,l);
    }

    @Test
    @Purpose(desc = "使用正则表达式先判断SharedPreferences中的某字段是否为纯数字，再根据结果选择强制转换或置为默认值")
    public void testMatchPattern_IllegalSharedPreData(){
        long l = 0L;
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("test", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("value", "no long");
        editor.commit();
        begin = System.nanoTime();
        for(int i=0;i<100;i++){
            String temp = sharedPreferences.getString("value", "200");
            l = temp.matches("[0-9]+") ? Long.parseLong(temp) : 200;
        }
        matchPattern.mIllegalSharedData = System.nanoTime()-begin;
        assertEquals(200, l);
    }

    @Test
    @Purpose(desc = "使用Try Catch将纯数字String强制转化为long型，若转换不成功置为默认值")
    public void testTryCatch_CommonData(){
        long l = 0L;
        String a = "100L";
        begin = System.nanoTime();
        for(int i=0;i<100;i++){
            try {
                l = Long.valueOf(a);
            }catch (Exception e){
                l = 100L;
            }
        }
        tryCatch.mCommonData = System.nanoTime()-begin;
        assertEquals(100L,l);
    }

    @Test
    @Purpose(desc = "使用正则表达式先判断纯数字String是否为纯数字，再根据结果选择强制转换或置为默认值")
    public void testMatchPattern_CommonData(){
        long l = 0L;
        String a = "100L";
        begin = System.nanoTime();
        for(int i=0;i<100;i++){
            l = a.matches("[0-9]+")?Long.valueOf(a):100L;
        }
        matchPattern.mCommonData = System.nanoTime()-begin;
        assertEquals(100L,l);
    }


    static class CostTimeRecoder {
        long mCommonData = 0;
        long mIllegalLongData = 0;
        long mIllegalSharedData = 0;
    }

}
