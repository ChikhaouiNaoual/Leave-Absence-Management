package sample;

public class AllUsers {
    private String ID;
    private String User_Name;
    private String Name;
    private String Email;
    private String Country;
    private String Gender;
    private String Department;

    public AllUsers(String ID, String user_Name, String name, String email, String country, String gender, String department) {
        this.ID = ID;
        User_Name = user_Name;
        Name = name;
        Email = email;
        Country = country;
        Gender = gender;
        Department = department;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }
}
