package grodrich.grc.familyapp.model;

/**
 * Created by gabri on 08/02/2017.
 */

public class Notification {
    private String text;
    private String userId;
    private String userEmail;
    private boolean read;

    public Notification(){}

    public Notification(String text, String userId, String userEmail){
        this.text = text;
        this.userId = userId;
        this.userEmail = userEmail;
        this.read = false;
    }

    public Notification(String text, String userId,  String userEmail, boolean read) {
        this.text = text;
        this.userId = userId;
        this.read = read;
        this.userEmail = userEmail;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
