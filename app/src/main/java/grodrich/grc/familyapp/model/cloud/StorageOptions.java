package grodrich.grc.familyapp.model.cloud;


import android.graphics.Bitmap;
import android.widget.ImageView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import grodrich.grc.familyapp.controller.Controller;

/**
 * Created by lidia on 4/09/16.
 */
public class StorageOptions {

    private static FirebaseStorage firebaseStorage = FirebaseStorage.
            getInstance();
    public static final String USERS_IMAGES_REFERENCE = "user_images/";
    public static final String FAMILY_IMAGES_REFERENCE = "family_images/";
    public static final int USER = 0;
    public static final int FAMILY = 1;

    public static UploadTask saveFamilyImage(ImageView imageView){
        StorageReference familyRef = firebaseStorage.getReference().child(getImageReference(FAMILY));

        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        return familyRef.putBytes(data);
    }

    public static UploadTask saveUserImage(ImageView imageView){
        StorageReference familyRef = firebaseStorage.getReference().child(getImageReference(USER));

        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        return familyRef.putBytes(data);
    }
    private static String getImageReference(int referenceType){
        switch (referenceType){
            case FAMILY:
                return FAMILY_IMAGES_REFERENCE.concat(Controller.getInstance().getActualUser().getFamilyId());
            case USER:
                return USERS_IMAGES_REFERENCE.concat(Controller.getInstance().getActualUser().getId());
            default:
                return null;
        }

    }


    public static Task<byte[]> downloadFamilyImage(){
        StorageReference familyImageRefence = firebaseStorage.getReference().child(getImageReference(FAMILY));
        final long ONE_MEGABYTE = 1024 * 1024;
        return familyImageRefence.getBytes(ONE_MEGABYTE);
    }

    public static Task<byte[]> downloadUserImage(){
        StorageReference userImageRefence = firebaseStorage.getReference().child(getImageReference(USER));
        final long ONE_MEGABYTE = 1024 * 1024;
        return userImageRefence.getBytes(ONE_MEGABYTE);
    }

    public static void deleteFamilyImage(String familyId) {
        firebaseStorage.getReference().child(FAMILY_IMAGES_REFERENCE.concat(familyId)).delete();
    }
}
