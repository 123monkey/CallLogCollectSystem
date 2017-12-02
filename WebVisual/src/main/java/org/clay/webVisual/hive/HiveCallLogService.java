package org.clay.webVisual.hive;

import org.apache.hadoop.hbase.client.Result;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.clay.webVisual.domain.CallLog;
import org.clay.webVisual.domain.CallLogStat;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Service("hiveCallLogService")
public class HiveCallLogService {

    //hiveserver2连接串
    private static String url = "jdbc:hive2://mini1:10000/mydb" ;
    //驱动程序类
    private static String driverClass = "org.apache.hive.jdbc.HiveDriver" ;

    static{
        try {
            Class.forName(driverClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 查询最近的通话记录,使用hive进行mr查询.
     */
    public CallLog findLatestCallLog(String phoneNum){
        try {
            Connection conn = DriverManager.getConnection(url);
            System.out.println(conn);
            Statement st = conn.createStatement();
            String sql = "select * from ext_calllogs_in_hbase where id like '%"+ phoneNum+"%' order by callTime desc limit 1" ;
            ResultSet rs = st.executeQuery(sql);
            CallLog log = null ;
            if(rs.next()){
                log = new CallLog();
                log.setCaller(rs.getString("caller"));
                log.setCallee(rs.getString("callee"));
                log.setCallTime(rs.getString("callTime"));
                log.setCallDuration(rs.getString("callDuration"));
            }
            rs.close();
            return log ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null ;
    }

    /**
     * 查询指定人员指定年份中各个月份的通话次数
     */
    public List<CallLogStat> statCallLogsCount(String caller, String year){
        SparkSession sess = SparkSession.builder().appName("SparkHive").enableHiveSupport().master("spark://s201:7077").getOrCreate();
        String sql = "select count(*) ,substr(calltime,1,6) from ext_calllogs_in_hbase " +
                "where caller = '" + caller + "' and substr(calltime,1,4) == '" + year
                + "' group by substr(calltime,1,6) order by substr(calltime,1,6) asc";
        Dataset<Row> df = sess.sql(sql);
        List<Row> rows = df.collectAsList();
        List<CallLogStat> list = new ArrayList<CallLogStat>();
        for(Row row : rows){
            list.add(new CallLogStat(row.getString(2),row.getInt(1)));
        }
        return list;
    }

    /**
     * 查询指定人员指定年份中各个月份的通话次数
     */
    public List<CallLogStat> statCallLogsCount2(String caller, String year){
        List<CallLogStat> list = new ArrayList<CallLogStat>() ;
        try {
            Connection conn = DriverManager.getConnection(url);
            Statement st = conn.createStatement();
            String sql = "select count(*) ,substr(calltime,1,6) from mydb.ext_calllogs_in_hbase " +
                    "where caller = '" + caller+"' and substr(calltime,1,4) == '" + year
                    + "' group by substr(calltime,1,6) order by substr(calltime,1,6) ";
            ResultSet rs = st.executeQuery(sql);
            CallLog log = null;
            while (rs.next()) {
                CallLogStat logSt = new CallLogStat();
                logSt.setCount(rs.getInt(0));
                logSt.setYearMonth(rs.getString(1));
                list.add(logSt);
            }
            rs.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
