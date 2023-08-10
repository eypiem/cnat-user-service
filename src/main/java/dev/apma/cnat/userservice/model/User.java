package dev.apma.cnat.userservice.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * @see
     * <a href="https://owasp.org/www-community/OWASP_Validation_Regex_Repository">OWASP Validation Regex Repository</a>
     */
    @Email(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$", message =
            "Email is not valid")
    @Column(nullable = false, unique = true, length = 254)
    private String email;

    @Size(min = 8, max = 64, message = "Password should be between 8 and 64 characters")
    @Column(nullable = false, length = 64)
    private String password;

    @Size(min = 2, max = 50, message = "First name should be between 2 and 50 characters")
    @Column(name = "first_name", nullable = false, length = 20)
    private String firstName;

    @Size(min = 2, max = 50, message = "Last name should be between 2 and 50 characters")
    @Column(name = "last_name", nullable = false, length = 20)
    private String lastName;

    @Column(nullable = false)
    private boolean enabled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "User{"
               + "id="
               + id
               + ", email='"
               + email
               + '\''
               + ", password='"
               + password
               + '\''
               + ", firstName='"
               + firstName
               + '\''
               + ", lastName='"
               + lastName
               + '\''
               + ", enabled="
               + enabled
               + '}';
    }
}
