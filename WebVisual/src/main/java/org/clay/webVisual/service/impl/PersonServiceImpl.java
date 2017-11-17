package org.clay.webVisual.service.impl;

import org.clay.webVisual.dao.BaseDao;
import org.clay.webVisual.dao.impl.PersonDaoImpl;
import org.clay.webVisual.domain.Person;
import org.clay.webVisual.service.PersonService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 */
@Service("personService")
public class PersonServiceImpl extends BaseServiceImpl<Person> implements PersonService {

    @Resource(name="personDao")
    public void setDao(BaseDao<Person> dao) {
        super.setDao(dao);
    }

    public String selectNameByPhone(String phone){
        return ((PersonDaoImpl)getDao()).selectNameByPhone(phone) ;
    }
}