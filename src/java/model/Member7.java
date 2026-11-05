package model;

import java.util.Date;

public class Member7 implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String username;
    private String password;
    private FullName7 fullName;
    private Date dateOfBirth;
    private String phone;
    private String email;
    private Address7 address;

    public Member7() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public FullName7 getFullName() {
        return fullName;
    }

    public void setFullName(FullName7 fullName) {
        this.fullName = fullName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth == null ? null : new Date(dateOfBirth.getTime());
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth == null ? null : new Date(dateOfBirth.getTime());
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address7 getAddress() {
        return address;
    }

    public void setAddress(Address7 address) {
        this.address = address;
    }
}
