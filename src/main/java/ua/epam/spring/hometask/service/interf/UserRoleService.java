package ua.epam.spring.hometask.service.interf;

import ua.epam.spring.hometask.domain.UserRole;

import java.util.List;

/**
 * Created by Oleksii_Kovetskyi on 4/19/2018.
 */
public interface UserRoleService extends AbstractDomainObjectService<UserRole>{

    /**
     * Finding user role by user id
     *
     * @param userId
     *            id of the user
     * @return found user or <code>null</code>
     */
    List<UserRole> getByUserId(Long userId);
}
