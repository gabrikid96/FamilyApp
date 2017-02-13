package grodrich.grc.familyapp.controller;

import android.widget.ImageView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Hashtable;

import grodrich.grc.familyapp.model.Family;
import grodrich.grc.familyapp.model.FamilyIdGenerator;
import grodrich.grc.familyapp.model.Notification;
import grodrich.grc.familyapp.model.User;
import grodrich.grc.familyapp.model.cloud.AuthenticateOptions;
import grodrich.grc.familyapp.model.cloud.DatabaseOptions;
import grodrich.grc.familyapp.model.cloud.StorageOptions;

/**
 * Created by gabri on 26/08/16.
 */
public class Controller {

    private static Controller ourInstance;
    private User actualUser;


    private Controller (){
        initAuthenticateOptions();
        loadDatabaseList();
    }

    private void loadDatabaseList() {
        DatabaseOptions.loadUsers();
        DatabaseOptions.loadFamilies();
    }

    public static Controller getInstance(){
        if (ourInstance == null){
            ourInstance = new Controller();
        }
        return ourInstance;
    }

    public boolean isDataLoaded(){
        boolean isUsersLoaded = DatabaseOptions.getUsers() != null;
        boolean isFamilyLoaded = DatabaseOptions.getFamilies() != null;
        return isUsersLoaded && isFamilyLoaded;
    }


    public void updateUser(){
        DatabaseOptions.getUserById(getCurrentUser().getUid());
    }

    public User getActualUser() {
        return actualUser;
    }

    public void setActualUser(User user){
        this.actualUser = user;
    }


    public Task<AuthResult> loginUser(String mEmail, String mPassword){
       return AuthenticateOptions.loginUser(mEmail, mPassword);
    }

    public UploadTask saveFamilyImage(ImageView imageView, String id){
        return StorageOptions.saveFamilyImage(imageView, id);
    }

    public UploadTask saveUserImage(ImageView imageView, String email){
        return StorageOptions.saveUserImage(imageView, email);
    }

    public Family createFamily(String familyName, String password, ArrayList<User> members){
        String unicId = generateId();
        return new Family(familyName,members,password, unicId);
    }

    private String generateId() {
        String id = FamilyIdGenerator.getUnicId();
        while (DatabaseOptions.getFamilies().get(id) != null){
            id = FamilyIdGenerator.getUnicId();
        }
        return id;
    }

    public Task<AuthResult> attemptCreateUser(String mEmail, String mPassword){
        return AuthenticateOptions.createUser(mEmail, mPassword);
    }


    public User createUser(String name, String age, String email,String id){
        return new User(name,Integer.parseInt(age),email,id);
    }

    public FirebaseUser getCurrentUser(){
        return AuthenticateOptions.getCurrentUser();
    }

    public void initAuthenticateOptions(){
        AuthenticateOptions.initAuthenticate();
    }

    public void finishAuthenticateOptions(){
        AuthenticateOptions.finishAuthenticate();
    }


    public void registerNewUser(User user) {
        DatabaseOptions.createNewUser(user);
        DatabaseOptions.addNotification(null);
    }

    public void addNotification(Notification notification){
        DatabaseOptions.addNotification(notification);
    }

    public void associateFamily(Family family) {
        if (family != null) {
            for (User user : family.getMembers()){
                DatabaseOptions.changeUserInformation(user.getId(),"familyId",family.getFamilyId());
            }
        }else{
            actualUser.setFamilyId("");
        }
    }

    public void registerNewFamily(Family family) {
        DatabaseOptions.createNewFamily(family);
    }

    public Task<byte[]> getFamilyImage(String id){
        return StorageOptions.downloadFamilyImage(id);
    }
    public Task<byte[]> getUserImage(String email){
        return StorageOptions.downloadUserImage(email);
    }

    public Hashtable<String,User> getUsers(){
        return DatabaseOptions.getUsers();
    }

    public Hashtable<String,Family> getFamilies(){
        return DatabaseOptions.getFamilies();
    }

    public void removeFamily(Family family) {
        DatabaseOptions.deleteFamily(family.getFamilyId());

        StorageOptions.deleteFamilyImage(family.getFamilyId());

        for (User user : family.getMembers()){
            DatabaseOptions.changeUserInformation(user.getId(),"familyId","");
        }
    }

    public DatabaseReference getNotificacionReference(){
        return DatabaseOptions.getNotificacionReference();
    }
}
