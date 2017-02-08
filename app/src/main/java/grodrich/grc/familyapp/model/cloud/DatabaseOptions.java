package grodrich.grc.familyapp.model.cloud;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Hashtable;

import grodrich.grc.familyapp.controller.Controller;
import grodrich.grc.familyapp.model.Family;
import grodrich.grc.familyapp.model.Notification;
import grodrich.grc.familyapp.model.User;

/**
 * Created by gabri on 27/08/16.
 */
public class DatabaseOptions {
    private static FirebaseDatabase firebaseDatabase = FirebaseDatabase.
                                                            getInstance();
    public static final String USER_REFERENCE = "users";
    public static final String FAMILY_REFERENCE = "families";
    public static final String NOTIFICATION_REFERENCE = "notifications";

    private static Hashtable<String,User> users;
    private static Hashtable<String,Family>families;

    /**
     * Save user information in Database in /users/.
     * @param user user to save.
     */
    public static void createNewUser(User user){
        firebaseDatabase.getReference().
        child(USER_REFERENCE).child(user.getId()).setValue(user);
    }

    /**
     * Save family information in Database in /family/.
     * @param family family to save.
     */
    public static void createNewFamily(Family family){
        firebaseDatabase.getReference().
        child(FAMILY_REFERENCE).child(family.getFamilyId()).setValue(family);
        //Save users ids
    }

    /**
     * Save a notification information of an user.
     * @param notification notification information
     */
    public static void createNewNotification(Notification notification){
        if (notification == null){
            firebaseDatabase.getReference().child(NOTIFICATION_REFERENCE).
                    child(Controller.getInstance().getCurrentUser().getUid()).setValue(notification);
        }else{
            firebaseDatabase.getReference().child(NOTIFICATION_REFERENCE).
                    child(notification.getUserId()).setValue(notification);
        }

    }

    /**
     * Delete family information by id.
     * @param familyId family id.
     */
    public static void deleteFamily(String familyId){
        firebaseDatabase.getReference().child(FAMILY_REFERENCE).child(familyId).removeValue();
    }

    /**
     * Change field value of an user.
     * @param userId user id.
     * @param field field to be change.
     * @param newValue value to set.
     */
    public static void changeUserInformation(String userId,String field,String newValue){
        firebaseDatabase.getReference().child(USER_REFERENCE).child(userId).child(field).setValue(newValue);
    }

    /**
     * Start load users.
     * If an user has been created or deleted, this method update the hash table.
     */
    public static void loadUsers(){
        firebaseDatabase.getReference().child(USER_REFERENCE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                final Hashtable<String,User> userList = new Hashtable<>();
                Log.i("LOAD_DATA", "Numero de usuarios: " + snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    userList.put(user.getEmail(),user);
                }
                users = userList;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("LOAD_DATA", "Ha fallado la carga de los usuarios");
            }
        });
    }

    /**
     * Start load families.
     * If a family has been created or deleted, this method update the hash table.
     */
    public static void loadFamilies(){
        firebaseDatabase.getReference().child(FAMILY_REFERENCE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.i("LOAD_DATA", "Numero de families: " + snapshot.getChildrenCount());
                Hashtable<String,Family> familyList = new Hashtable<>();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Family family = postSnapshot.getValue(Family.class);
                    familyList.put(family.getFamilyId(),family);
                }
                families = familyList;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("LOAD_DATA", "Ha fallado la carga de los usuarios");
            }
        });
    }

    public static void getUserById(String id){
        firebaseDatabase.getReference().child(USER_REFERENCE).child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Controller.getInstance().setActualUser(user);
                Log.i("USER","User actual updated");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Get hash table of users
     * @return
     */
    public static Hashtable<String,User> getUsers() {
        return users;
    }

    /**
     * Get hash table of families
     * @return
     */
    public static Hashtable<String,Family> getFamilies() {return families;}
    /**
     *
     */
    public static DatabaseReference getNotificacionReference(){
        return firebaseDatabase.getReference().child(NOTIFICATION_REFERENCE);
    }
}
