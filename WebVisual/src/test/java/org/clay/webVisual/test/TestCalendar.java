package org.clay.webVisual.test;

import org.clay.webVisual.domain.CallLogRange;
import org.junit.Test;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/4/13.
 */
public class TestCalendar {
    @Test
    public void test1() throws Exception {
        SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdfYM = new SimpleDateFormat("yyyyMM");
        DecimalFormat df00 = new DecimalFormat("00") ;

        //
        List<CallLogRange> list = new ArrayList<CallLogRange>();
        //字符串时间
        String startStr = "20140203" ;
        String startPrefix = startStr.substring(0,6);


        String endStr = "20160304" ;
        String endPrefix = endStr.substring(0,6);
        int endDay = Integer.parseInt(endStr.substring(6, 8));
        //结束点
        String endPoint = endPrefix + df00.format(endDay + 1) ;

        //日历对象
        Calendar c = Calendar.getInstance() ;

        //同年月
        if(startPrefix.equals(endPrefix)){
            CallLogRange range = new CallLogRange();
            range.setStartPoint(startStr);          //设置起始点

            range.setEndPoint(endPoint);            //设置结束点
            list.add(range) ;
        }
        else{
            //1.起始月
            CallLogRange range = new CallLogRange();
            range.setStartPoint(startStr);

            //设置日历的时间对象
            c.setTime(sdfYMD.parse(startStr));
            c.add(Calendar.MONTH,1);
            range.setEndPoint(sdfYM.format(c.getTime()));
            list.add(range) ;

            //是否是最后一月
            while(true){
                //到了结束月份
                if(endStr.startsWith(sdfYM.format(c.getTime()))){
                    range = new CallLogRange();
                    range.setStartPoint(sdfYM.format(c.getTime()));
                    range.setEndPoint(endPoint);
                    list.add(range) ;
                    break ;
                }
                else{
                    range = new CallLogRange();
                    //起始时间
                    range.setStartPoint(sdfYM.format(c.getTime()));

                    //增加月份
                    c.add(Calendar.MONTH,1);
                    range.setEndPoint(sdfYM.format(c.getTime()));
                    list.add(range);
                }
            }

            System.out.println("");
        }
    }
}
