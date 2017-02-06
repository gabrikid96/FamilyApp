package grodrich.grc.familyapp.controller;

import android.widget.ImageView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import grodrich.grc.familyapp.model.Family;
import grodrich.grc.familyapp.model.FamilyIdGenerator;
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

    public void setActualUser(){
        String uid = getCurrentUser().getUid();
        User user = findUserById(uid);
        actualUser = user;
    }

    public void setActualUser(User user){
        actualUser = user;
    }

    public User getActualUser() {
        return actualUser;
    }

    public User findUserById(String uid){
        for (User user : DatabaseOptions.getUsers()){
            if (user.getId().equals(uid)){
                return user;
            }
        }
        return null;
    }


    public Task<AuthResult> loginUser(String mEmail, String mPassword){
       return AuthenticateOptions.loginUser(mEmail, mPassword);
    }

    public UploadTask saveFamilyImage(ImageView imageView){
        return StorageOptions.saveFamilyImage(imageView);
    }

    public UploadTask saveUserImage(ImageView imageView){
        return StorageOptions.saveUserImage(imageView);
    }

    public Family createFamily(String familyName, String password, ArrayList<User> members){
        String unicId = generateId();
        return new Family(familyName,members,password, unicId);
    }

    private String generateId() {
        String id = FamilyIdGenerator.getUnicId();
        for(Family family : DatabaseOptions.getFamilies()){
            if(id.equals(family.getFamilyId())){
                id = FamilyIdGenerator.getUnicId();
            }
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
    }

    public void associateFamily(Family family) {
        if (family != null) {
            actualUser.setFamilyId(family.getFamilyId());
        }else{
            actualUser.setFamilyId("");
        }
        registerNewUser(actualUser);

    }

    public void registerNewFamily(Family family) {
        DatabaseOptions.createNewFamily(family);
    }

    public Task<byte[]> getFamilyImage(){
        return StorageOptions.downloadFamilyImage();
    }
    public Task<byte[]> getUserImage(){
        return StorageOptions.downloadUserImage();
    }

    public void removeFamily(Family family) {
        getActualUser().setFamilyId("");
        DatabaseOptions.deleteFamily(family.getFamilyId());

        StorageOptions.deleteFamilyImage(family.getFamilyId());

        for (User user : family.getMembers()){
            DatabaseOptions.changeUserInformation(user.getId(),"familyId","");
        }
        loadDatabaseList();
    }
}
