package ua.epam.spring.hometask.domain;

/**
 * Created by Oleksii_Kovetskyi on 4/19/2018.
 */
public class UserRole extends DomainObject {

    private Long userId;
    private String role;

    public UserRole() {
    }

    public UserRole(Long userId, String role) {
        this.userId = userId;
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
