package org.clay.webVisual.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.clay.webVisual.domain.Person;
import org.clay.webVisual.service.PersonService;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class TestPersonService {
    public static Map<String, String> callers = new HashMap<String, String>();

    static {
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
    }

    @Test
    public void test1(){
        ApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml");
        PersonService ps = (PersonService)ac.getBean("personService");
        for(Map.Entry<String,String> e : callers.entrySet()){
            Person p = new Person();
            p.setName(e.getValue());
            p.setPhone(e.getKey());
            ps.insert(p);
        }
    }

    @Test
    public void testFindAll(){
        ApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml");
        PersonService ps = (PersonService)ac.getBean("personService");
        List<Person> list = ps.selectAll();
        String json = JSONArray.toJSONString(list);
        json = JSON.toJSONString(list) ;
        System.out.println(json);
    }

    @Test
    public void testFindNameByPhone(){
        ApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml");
        PersonService ps = (PersonService)ac.getBean("personService");
        System.out.println(ps.selectNameByPhone("18605954456"));
    }
}
