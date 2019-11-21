package entity;

import java.time.LocalDate;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    private boolean isUserActive;
    private LocalDate birthDay;
    private String password;
    private String login;
    private String email;

    public User(int id, String firstName, String lastName, boolean isUserActive, LocalDate birthDay, String password, String login, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isUserActive = isUserActive;
        this.birthDay = birthDay;
        this.password = password;
        this.login = login;
        this.email = email;
    }

    public User(int id, String firstName, String lastName, boolean isUserActive, LocalDate birthDay) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isUserActive = isUserActive;
        this.birthDay = birthDay;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean getIsUserActive() {
        return isUserActive;
    }

    public LocalDate getBirthDay() {
        return birthDay;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", isUserActive=" + isUserActive +
                ", birthDay=" + birthDay +
                ", password='" + password + '\'' +
                ", login='" + login + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
