package ua.epam.spring.hometask.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.interf.UserRoleService;
import ua.epam.spring.hometask.service.interf.UserService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Oleksii_Kovetskyi on 4/19/2018.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User userByLogin = userService.getUserByLogin(login);
        if (userByLogin == null)
            throw new UsernameNotFoundException("User with login: " + login + " not found!");

        List<GrantedAuthority> authorities = userRoleService.getByUserId(userByLogin.getId()).stream()
                .map(ur -> new SimpleGrantedAuthority(ur.getRole()))
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(userByLogin.getLogin(),
                userByLogin.getPassword(), authorities);
    }
}
