package ua.epam.spring.hometask.service;

import ua.epam.spring.hometask.domain.DomainObject;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Oleksii_Kovetskyi on 4/4/2018.
 */
public abstract class AbstractDomainObjectServiceImpl<T extends DomainObject> implements AbstractDomainObjectService<T> {

    protected Map<Long, T> domainObjectMap;
    private long lastId;

    public AbstractDomainObjectServiceImpl() {
        domainObjectMap = new HashMap<>();
    }

    @Override
    public T save(@Nonnull T object) {
        if (object.getId() == null) {
            object.setId(++lastId);
        }
        domainObjectMap.put(lastId, object);
        return object;
    }

    @Override
    public void remove(@Nonnull T object) {
        domainObjectMap.remove(object.getId());
    }

    @Override
    public T getById(@Nonnull Long id) {
        return domainObjectMap.get(id);
    }

    @Nonnull
    @Override
    public Collection<T> getAll() {
        return new ArrayList<>(domainObjectMap.values());
    }
}
