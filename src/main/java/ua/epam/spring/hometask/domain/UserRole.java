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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserRole userRole = (UserRole) o;

        if (getId() != null && (userRole.getId() == null)) return false;
        if (!getId().equals(userRole.getId())) return false;
        if (!getUserId().equals(userRole.getUserId())) return false;
        return getRole().equals(userRole.getRole());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result += getUserId().hashCode();
        result = 31 * result + getRole().hashCode();
        return result;
    }
}
