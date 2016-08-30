package grodrich.grc.familyapp.controller;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import grodrich.grc.familyapp.model.User;
import grodrich.grc.familyapp.model.cloud.AuthenticateOptions;
import grodrich.grc.familyapp.model.cloud.DatabaseOptions;

/**
 * Created by gabri on 26/08/16.
 */
public class Controller {

    private static Controller ourInstance;

    private Controller (){
        initAuthenticateOptions();
    }

    public static Controller getInstance(){
        return ourInstance == null ? new Controller() : ourInstance;
    }

    public Task<AuthResult> loginUser(String mEmail, String mPassword){
       return AuthenticateOptions.loginUser(mEmail,mPassword);
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
}
