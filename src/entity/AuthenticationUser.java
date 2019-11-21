package entity;

public class AuthenticationUser {

    public enum ROLE{
        ADMIN,LIBRARIAN;
    }

    private int id;
    private ROLE role;
    private String firstName;
    private String lastName;
    private String password;
    private String login;



    public AuthenticationUser(int id, ROLE role, String firstName, String lastName, String password, String login) {
        this.id = id;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.login = login;
    }

    public AuthenticationUser(ROLE role, String firstName, String lastName, String password, String login) {
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.login = login;
    }

    @Override
    public String toString() {
        return "AuthenticationUser{" +
                "id=" + id +
                ", role='" + role + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", login='" + login + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public ROLE getRole() {
        return role;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getLogin() {
        return login;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
