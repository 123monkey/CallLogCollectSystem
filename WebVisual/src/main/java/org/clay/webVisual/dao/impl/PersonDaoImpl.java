package org.clay.webVisual.dao.impl;

import org.apache.ibatis.session.RowBounds;
import org.clay.webVisual.dao.BaseDao;
import org.clay.webVisual.domain.Person;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 */
@Repository("personDao")
public class PersonDaoImpl extends SqlSessionDaoSupport implements BaseDao<Person> {
    public void insert(Person person) {
        getSqlSession().insert("persons.insert",person) ;
    }

    public void update(Person person) {

    }

    public void delete(Integer id) {

    }

    public Person selectOne(Integer id) {
        return null;
    }

    public List<Person> selectAll() {
        return getSqlSession().selectList("persons.selectAll");
    }

    public List<Person> selectPage(int offset, int len) {
        return null;
    }

    public int selectCount() {
        return 0;
    }

    //按照号码查询用户
    public String selectNameByPhone(String phone){
        return getSqlSession().selectOne("persons.selectNameByPhone",phone);
    }
}
