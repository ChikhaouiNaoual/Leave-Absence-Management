package sample;

import javafx.collections.ObservableList;

public class MyLeaveTable {
    private Integer id ;
    private String UserName;
    private String request_type;
    private String leave_type;
    private String from_date;
    private String to_date ;
    private String reason;
    private String status;

    public MyLeaveTable( int id ,String UserName, String request_type, String leave_type, String from_date, String to_date, String reason , String status ) {
        this.id = id;
        this.UserName = UserName;
        this.request_type = request_type;
        this.leave_type = leave_type;
        this.from_date = from_date;
        this.to_date = to_date;
        this.reason = reason;
        this.status = status;
    }

    public static void setItems(ObservableList<MyLeaveTable> leavesList) {

    }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }

    public String getLeave_type() {
        return leave_type;
    }

    public void setLeave_type(String leave_type) {
        this.leave_type = leave_type;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



}
