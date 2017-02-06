package grodrich.grc.familyapp.model;

import java.util.ArrayList;

/**
 * Created by gabri on 3/09/16.
 */
public class Family {

    private String familyName;
    private int numMembers;
    private ArrayList<User> members;
    private String password;
    private String familyId;

    public Family() {
    }

    public Family(String familyName, ArrayList<User> members, String password, String familyId) {
        this.familyName = familyName;
        this.numMembers = members.size();
        this.members = members;
        this.password = password;
        this.familyId = familyId;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public int getNumMembers() {
        return numMembers;
    }

    public void setNumMembers(int numMembers) {
        this.numMembers = numMembers;
    }

    public ArrayList<User> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<User> members) {
        this.members = members;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }
}
