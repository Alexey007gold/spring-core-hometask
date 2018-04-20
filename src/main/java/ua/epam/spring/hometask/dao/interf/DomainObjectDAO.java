package ua.epam.spring.hometask.dao.interf;

import ua.epam.spring.hometask.domain.DomainObject;

import java.util.List;

/**
 * Created by Oleksii_Kovetskyi on 4/20/2018.
 */
public interface DomainObjectDAO<T extends DomainObject> {
    void save(T object);

    List<T> getAll();

    void remove(T object);

    T getById(Long id);

    List<T> getBy(String[] columnNames, Object... values);

    T getOneBy(String[] columnNames, Object... values);
}
