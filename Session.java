package sample;

public class Session {
    public static String loggedInUsername;

    public static void setUsername(String username){
        loggedInUsername = username;
    }
    public static String getUsername(){
        return loggedInUsername;
    }
}
