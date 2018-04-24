package ua.epam.spring.hometask.service.interf;

import ua.epam.spring.hometask.domain.UserAccount;

/**
 * Created by Oleksii_Kovetskyi on 4/24/2018.
 */
public interface UserAccountService extends AbstractDomainObjectService<UserAccount>{

    /**
     * Finding user account by user id
     *
     * @param userId
     *            id of the user
     * @return found user or <code>null</code>
     */
    UserAccount getByUserId(Long userId);
}
