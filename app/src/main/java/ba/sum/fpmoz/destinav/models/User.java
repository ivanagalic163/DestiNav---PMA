package ba.sum.fpmoz.destinav.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String firstName;
    public String lastName;
    public String email;
    public String password;
    public String role;


    public User(String firstName, String lastName, String email, String password,String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;

    }
}