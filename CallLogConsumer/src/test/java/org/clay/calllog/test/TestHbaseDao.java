package org.clay.calllog.test;

import org.clay.calllog.consumer.HbaseDao;
import org.junit.Test;

/**
 *
 */
public class TestHbaseDao {
    @Test
    public void test1(){
        HbaseDao dao = new HbaseDao();
        dao.put("15032293356,18620192711,2017/03/13 14:02:01,297");
    }
}
