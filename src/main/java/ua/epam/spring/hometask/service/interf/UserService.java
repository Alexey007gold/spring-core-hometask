package ua.epam.spring.hometask.service.interf;

import ua.epam.spring.hometask.domain.User;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
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

    /**
     * Finding user by login
     * @param login login of the user
     * @return found user or <code>null</code>
     */
    public @Nullable User getUserByLogin(String login);
    /**
     * Finding userId by login
     * @param login login of the user
     * @return found userId or <code>null</code>
     */
    public @Nullable Long getUserIdByLogin(String login);

    /**
     * Checks whetheer a user is registered
     * @param user a user to check
     * @return true/false
     */
    public boolean isUserRegistered(User user);

    /**
     * Parses users from inputStream and saves them
     * @param inputStream inputStream with users data
     * @throws IOException when an IO exception occurs
     */
    public List<User> parseUsersFromInputStream(InputStream inputStream) throws IOException;
}
