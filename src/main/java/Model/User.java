package Model;

import java.math.BigDecimal;
import java.math.BigInteger;

public class User {
    private int userId;
    private int roleId;
    private String firstName;
    private String lastName;
    private BigDecimal phoneNumber;
    private String username;

    public User() {

    }

    public User(int userId, String firstName, String lastName, BigDecimal phoneNumber, String username) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.username = username;
    }

    public User(int roleId, String firstName, String lastName, BigDecimal phoneNumber, String username, String randomSalt, String password) {

        this.roleId = roleId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.password = password;
        this.randomSalt = randomSalt;
    }

    public String getRandomSalt() {
        return randomSalt;
    }

    public void setRandomSalt(String randomSalt) {
        this.randomSalt = randomSalt;
    }

    private String randomSalt;
    private String password;
    private int addressId;

    public User(int userId, int roleId, String username) {
        this.userId = userId;
        this.roleId = roleId;
        this.username = username;
    }

    public User(int userId, int roleId, String username, String password) {
        this.userId = userId;
        this.roleId = roleId;
        this.username = username;
        this.password = password;
    }

    public User(int userId, int roleId, String firstName, String lastName, BigDecimal phoneNumber, String username, String randomSalt, String password, int addressId) {
        this.userId = userId;
        this.roleId = roleId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.randomSalt = randomSalt;
        this.password = password;
        this.addressId = addressId;
    }

    // Full Constructor
    public User(int userId, int roleId, String firstName, String lastName, BigDecimal phoneNumber, String username, String password, int addressId) {
        this.userId = userId;
        this.roleId = roleId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.password = password;
        this.addressId = addressId;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
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

    public BigDecimal getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(BigDecimal phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", roleId=" + roleId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", username='" + username + '\'' +
                ", addressId=" + addressId +
                '}';
    }
}
