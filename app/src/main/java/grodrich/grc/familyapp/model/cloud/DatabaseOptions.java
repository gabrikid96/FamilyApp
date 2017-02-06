package grodrich.grc.familyapp.model.cloud;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import grodrich.grc.familyapp.controller.Controller;
import grodrich.grc.familyapp.model.Family;
import grodrich.grc.familyapp.model.User;

/**
 * Created by gabri on 27/08/16.
 */
public class DatabaseOptions {
    private static FirebaseDatabase firebaseDatabase = FirebaseDatabase.
                                                            getInstance();
    public static final String USER_REFERENCE = "users";
    public static final String FAMILY_REFERENCE = "families";

    private static ArrayList<User> users;
    private static ArrayList<Family> families;

    public static void createNewUser(User user){
        firebaseDatabase.getReference().
        child(USER_REFERENCE).child(user.getId()).setValue(user);
    }

    public static void createNewFamily(Family family){
        firebaseDatabase.getReference().
        child(FAMILY_REFERENCE).child(family.getFamilyId()).setValue(family);
        //Save users ids
    }

    public static void deleteFamily(String familyId){
        firebaseDatabase.getReference().child(FAMILY_REFERENCE).child(familyId).removeValue();
    }

    public static void changeUserInformation(String userId,String field,String newValue){
        firebaseDatabase.getReference().child(USER_REFERENCE).child(userId).child(field).setValue(newValue);
    }

    public static void loadUsers(){
        firebaseDatabase.getReference().child(USER_REFERENCE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                final ArrayList<User> userList = new ArrayList<>();
                Log.i("LOAD_DATA", "Numero de usuarios: " + snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    userList.add(user);
                }
                users = userList;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("LOAD_DATA", "Ha fallado la carga de los usuarios");
            }
        });
    }

    public static void loadFamilies(){
        firebaseDatabase.getReference().child(FAMILY_REFERENCE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.i("LOAD_DATA", "Numero de families: " + snapshot.getChildrenCount());
                ArrayList<Family> familyList = new ArrayList<Family>();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Family family = postSnapshot.getValue(Family.class);
                    familyList.add(family);
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

    public static ArrayList<User> getUsers() {
        return users;
    }

    public static ArrayList<Family> getFamilies() {
        return families;
    }
}
