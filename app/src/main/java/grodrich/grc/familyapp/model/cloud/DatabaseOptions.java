package grodrich.grc.familyapp.model.cloud;

import com.google.firebase.database.FirebaseDatabase;

import grodrich.grc.familyapp.model.User;

/**
 * Created by gabri on 27/08/16.
 */
public class DatabaseOptions {
    private static FirebaseDatabase firebaseDatabase = FirebaseDatabase.
                                                            getInstance();
    public static final String USER_REFERENCE = "users";

    public static void createNewUser(User user){
        firebaseDatabase.getReference().
        child(USER_REFERENCE).child(user.getId()).setValue(user);
    }

}
