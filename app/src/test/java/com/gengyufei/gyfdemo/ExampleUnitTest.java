package com.gengyufei.gyfdemo;

import com.gengyufei.gyfdemo.utils.Util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {

        String str = "3F717D1A05A84A26938FE77E95A6CA0F/coupon/activity21497950539801";

        System.out.println(Util.getMD5String(str));
        System.out.println(Util.md5Hex(str));

        assertEquals(Util.getMD5String(str), Util.md5Hex(str));
        assertEquals(4, 2 + 2);
    }
}