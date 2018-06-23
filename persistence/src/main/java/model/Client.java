package model;




import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import model.enums_utils.Role;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by ilya on 18.08.2016.
 * Client entity
 */
@NamedQueries({
        @NamedQuery(name = "Client.getAll" , query = "select u from Client u ")

})
@Entity
@Table(name = "users")
@Access( AccessType.FIELD)
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name ="name")
    private String name;

    @Column(name ="surname")
    private String surname;

//    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @Column(name ="dateOfBirth")
    private Date birth;

    @Column(name ="email",unique = true)
    private String email;

    @Column(name ="password")
    @JsonIgnore
    private String password;

    @Transient
    @JsonIgnore
    private String passwordconfirm;

    @Column(name = "registered")
//    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="dd-MM-yyyy",timezone = "Europe/Moscow")
    private Date registered;

    public void setRegistered(Date registered) {
        this.registered = registered;
    }

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @Embedded
    private Address address = new Address();

    public boolean isAdmin(){
        return roles.contains(Role.ROLE_ADMIN);
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Address getAddress(){return address;}

    public int getId(){return id;}

    public Set<Role> getRoles() {
        return roles;
    }

    public Date getRegistered() {
        return registered;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Date getBirth() {
        return birth;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPasswordconfirm() {
        return passwordconfirm;
    }

    public void setPasswordconfirm(String passwordconfirm) {
        this.passwordconfirm = passwordconfirm;
    }

    @Override
    public String toString() {
        return "Client{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", birth=" + birth +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Client that = (Client) o;
        if (id == 0 || that.id == 0) {
            return false;
        }
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (id == 0) ? 0 : id;
    }
}
