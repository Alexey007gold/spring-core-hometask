package ua.epam.spring.hometask.service;

import ua.epam.spring.hometask.domain.User;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Yuriy_Tkach
 */
public interface UserService extends AbstractDomainObjectService<User> {

    /**
     * Finding user by email
     * 
     * @param email
     *            Email of the user
     * @return found user or <code>null</code>
     */
    public @Nullable User getUserByEmail(@Nonnull String email);

    /**
     * Finding user by firstName
     *
     * @param firstName
     *            Email of the user
     * @return found user or <code>null</code>
     */
    public @Nullable List<User> getUsersByFirstName(@Nonnull String firstName);

    /**
     * Finding user by lastName
     *
     * @param lastName
     *            Email of the user
     * @return found user or <code>null</code>
     */
    public @Nullable List<User> getUsersByLastName(@Nonnull String lastName);

    /**
     * Finding user by firstName and lastName
     *
     * @param firstName
     *            Email of the user
     * @param lastName
     *            Email of the user
     * @return found user or <code>null</code>
     */
    public @Nullable
    List<User> getUsersByFirstAndLastName(@Nonnull String firstName, @Nonnull String lastName);

}
