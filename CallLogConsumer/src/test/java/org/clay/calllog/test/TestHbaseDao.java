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
        dao.put("18888886888,17626613141,2017/03/13 14:02:01,297");
    }
}
