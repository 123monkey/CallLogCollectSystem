package org.clay.webVisual.test;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class TestFastJson {
    @Test
    public void test1(){
        int[] arr = {1,2,3,4,5} ;
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        System.out.println(JSON.toJSONString(list));
    }
}
