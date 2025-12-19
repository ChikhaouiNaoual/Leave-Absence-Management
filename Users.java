package sample;

public class Users {

    private int Employee_ID;
    private String Full_Name;
    private String Department;
    private String Role;
    private String Status;

    public Users(int employee_ID, String full_Name, String department, String role, String status) {
        this.Employee_ID = employee_ID;
        this.Full_Name = full_Name;
        this.Department = department;
        this.Role = role;
        this.Status = status;


    }

    public int getEmployee_ID() {
        return Employee_ID;
    }

    public void setEmployee_ID(int employee_ID) {
        Employee_ID = employee_ID;
    }

    public String getFull_Name() {
        return Full_Name;
    }

    public void setFull_Name(String full_Name) {
        Full_Name = full_Name;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}


