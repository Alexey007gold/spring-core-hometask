package ua.epam.spring.hometask.service.impl;

import ua.epam.spring.hometask.dao.DomainObjectDAO;
import ua.epam.spring.hometask.domain.DomainObject;
import ua.epam.spring.hometask.service.interf.AbstractDomainObjectService;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * Created by Oleksii_Kovetskyi on 4/4/2018.
 */
public abstract class AbstractDomainObjectServiceImpl<T extends DomainObject, D extends DomainObjectDAO<T>> implements AbstractDomainObjectService<T> {

    protected D domainObjectDAO;

    public AbstractDomainObjectServiceImpl(D domainObjectDAO) {
        this.domainObjectDAO = domainObjectDAO;
    }

    @Override
    public T save(@Nonnull T object) {
        domainObjectDAO.save(object);
        return object;
    }

    @Override
    public void remove(@Nonnull T object) {
        domainObjectDAO.remove(object);
    }

    @Override
    public T getById(@Nonnull Long id) {
        return domainObjectDAO.getById(id);
    }

    @Nonnull
    @Override
    public Collection<T> getAll() {
        return domainObjectDAO.getAll();
    }
}
