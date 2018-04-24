package ua.epam.spring.hometask.service.impl;

import org.springframework.stereotype.Service;
import ua.epam.spring.hometask.dao.interf.UserAccountDAO;
import ua.epam.spring.hometask.domain.UserAccount;
import ua.epam.spring.hometask.service.interf.UserAccountService;

/**
 * Created by Oleksii_Kovetskyi on 4/24/2018.
 */
@Service
public class UserAccountServiceImpl extends AbstractDomainObjectServiceImpl<UserAccount, UserAccountDAO> implements UserAccountService {

    public UserAccountServiceImpl(UserAccountDAO domainObjectDAO) {
        super(domainObjectDAO);
    }

    @Override
    public UserAccount getByUserId(Long userId) {
        return domainObjectDAO.getOneBy(new String[] {"user_id"}, userId);
    }
}
