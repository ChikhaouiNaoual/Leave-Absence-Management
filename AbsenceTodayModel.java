package sample;

public class AbsenceTodayModel {
    private String name;
    private String monday, tuesday, wednesday, thursday, friday, saturday;
    private String today;

    public AbsenceTodayModel(String name, String monday, String tuesday, String wednesday, String thursday,
                             String friday, String saturday, String today) {
        this.name = name;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.today = today;
    }

    // Getters ...


    public String getName() {
        return name;
    }

    public String getMonday() {
        return monday;
    }

    public String getTuesday() {
        return tuesday;
    }

    public String getWednesday() {
        return wednesday;
    }

    public String getThursday() {
        return thursday;
    }

    public String getFriday() {
        return friday;
    }

    public String getSaturday() {
        return saturday;
    }


    public String getToday() {
        return today;
    }
}
