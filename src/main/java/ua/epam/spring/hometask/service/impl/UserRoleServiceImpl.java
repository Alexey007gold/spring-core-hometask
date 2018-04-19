package ua.epam.spring.hometask.service.impl;

import org.springframework.stereotype.Service;
import ua.epam.spring.hometask.dao.UserRoleDAO;
import ua.epam.spring.hometask.domain.UserRole;
import ua.epam.spring.hometask.service.interf.UserRoleService;

import java.util.List;

/**
 * Created by Oleksii_Kovetskyi on 4/19/2018.
 */
@Service
public class UserRoleServiceImpl extends AbstractDomainObjectServiceImpl<UserRole, UserRoleDAO> implements UserRoleService {

    public UserRoleServiceImpl(UserRoleDAO domainObjectDAO) {
        super(domainObjectDAO);
    }

    @Override
    public List<UserRole> getByUserId(Long userId) {
        return domainObjectDAO.getBy(new String[] {"user_id"}, userId);
    }
}
