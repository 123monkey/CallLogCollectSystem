package org.clay.callloggen;

import java.io.FileWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 *
 */
public class GenDataAppMain {
    static Random r = new Random();

    public static List<String> phoneNumbers = new ArrayList<String>();
    public static Map<String,String> callers = new HashMap<String, String>();
    static{

        callers.put("18888886888", "李灵黛");
        callers.put("17626613141", "冷文卿");
        callers.put("18939909990", "阴露萍");
        callers.put("13107880788", "柳兰歌");
        callers.put("15633122933", "秦水支");
        callers.put("13805517527", "李念儿");
        callers.put("18258885660", "文彩依");
        callers.put("15358239568", "柳婵诗");
        callers.put("13985578976", "顾莫言");
        callers.put("18723454334", "任水寒");
        callers.put("18091730004", "丁玲珑");
        callers.put("17099149876", "水笙");
        callers.put("18605954456", "景茵梦");
        callers.put("17620150375", "容柒雁");
        callers.put("17629089459", "林墨瞳");
        callers.put("13808621816", "华诗");
        callers.put("13985578975", "千湄");
        callers.put("15151881767", "洛离");
        phoneNumbers.addAll(callers.keySet());
    }



    public static void main(String[] args) throws Exception {
        genCallLog();
    }

    public static void genCallLog() throws Exception {
//        FileWriter fw = new FileWriter("/home/centos/calllog/calllog.log",true);
        FileWriter fw = new FileWriter(PropertiesUtil.getString("log.file"),true);
        while(true){
            //取主叫
            String caller = phoneNumbers.get(r.nextInt(callers.size()));
            //主叫名字
            String callerName = callers.get(caller);

            //被叫号
            String callee = null;
            String calleeName = null;
            while (true) {
                callee = phoneNumbers.get(r.nextInt(callers.size()));
                if (!caller.equals(callee)) {
                    break;
                }
            }
            calleeName = callers.get(callee);
            //通话时长
            int duration = r.nextInt(PropertiesUtil.getInt("call.duration.max")) + 1;
            DecimalFormat df = new DecimalFormat();
            df.applyPattern(PropertiesUtil.getString("call.duration.format"));
            String durStr = df.format(duration);

            //通话时间
            int year = PropertiesUtil.getInt("call.year");
            //月份(0~11)
            int month = r.nextInt(12);
            //天,范围(1~31)
            int day = r.nextInt(29) + 1;
            int hour = r.nextInt(24);
            int min = r.nextInt(60);
            int sec = r.nextInt(60);

            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, day);
            c.set(Calendar.HOUR_OF_DAY, hour);
            c.set(Calendar.MINUTE, min);
            c.set(Calendar.SECOND, sec);
            Date date = c.getTime();


            //如果时间超过今天就重新qushijian取时间.
            Date now = new Date();
            if (date.compareTo(now) > 0) {
                continue ;
            }
            SimpleDateFormat dfs = new SimpleDateFormat();
            dfs.applyPattern(PropertiesUtil.getString("call.time.format"));
            //通话时间
            String dateStr = dfs.format(date);

            //String log = caller + "," + callerName + "," + callee + "," + calleeName + "," + dateStr + "," + durStr  ;
            String log = caller + "," + callee + "," + dateStr + "," + durStr;
            System.out.println(log);
            fw.write(log + "\r\n");
            fw.flush();
            Thread.sleep(PropertiesUtil.getInt("gen.data.interval.ms"));
        }
    }
 }
