package sample;
import javafx.beans.property.SimpleStringProperty;


public class Employee {
    private String id;
    private String first_name;
    private String last_name;
    private String email;
    private String country;
    private String city;
    private String address;
    private String gender;
    private String department;
    private String birthday;
    private String mobile;
    private String password;

    // Constructor

    public Employee(String employeeID, String firstName, String lastName, String email, String country,
                    String city, String address, String gender, String department, String birthday,
                    String mobileNumber, String password) {
        this.id = employeeID;
        this.first_name = firstName;
        this.last_name = lastName;
        this.email = email;
        this.country = country;
        this.city = city;
        this.address = address;
        this.gender = gender;
        this.department = department;
        this.birthday = birthday;
        this.mobile = mobileNumber;
        this.password = password;
    }


    // Getters and Setters
    public String getEmployeeID() {
        return id;
    }

    public void setEmployee_id(String employeeID) {
        this.id = employeeID;
    }

    public String getFirstName() {
        return first_name;
    }

    public void setFirst_name(String firstName) {
        this.first_name = firstName;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String lastName) {
        this.last_name = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getMobileNumber() {
        return mobile;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobile = mobileNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

