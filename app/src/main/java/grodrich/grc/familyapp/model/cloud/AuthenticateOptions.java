package grodrich.grc.familyapp.model.cloud;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by gabri on 25/08/16.
 */
public class AuthenticateOptions {
    private static FirebaseAuth.AuthStateListener mAuthListener;
    private static FirebaseAuth mAuth;

    public static void initAuthenticate(){
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.i("LOGIN", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.i("LOGIN", "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    public static void finishAuthenticate(){
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public static Task<AuthResult> createUser(String email, String password){
        return mAuth.createUserWithEmailAndPassword(email, password);
    }

    public static Task<AuthResult> loginUser(String email, String password){
        return mAuth.signInWithEmailAndPassword(email, password);
    }

    public static FirebaseUser getCurrentUser(){
        return mAuth.getCurrentUser();
    }
}
