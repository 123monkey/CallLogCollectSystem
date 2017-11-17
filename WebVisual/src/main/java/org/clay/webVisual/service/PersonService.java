package org.clay.webVisual.service;

import org.clay.webVisual.domain.Person;

import java.util.List;

/**
 *
 */
public interface PersonService extends BaseService<Person> {
    public String selectNameByPhone(String phone);
}
