package grodrich.grc.familyapp.view.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import grodrich.grc.familyapp.R;
import grodrich.grc.familyapp.model.Family;
import grodrich.grc.familyapp.model.cloud.DatabaseOptions;
import grodrich.grc.familyapp.view.views.CircularImageView;

/**
 * Created by gabri on 3/09/16.
 */
public class MyFamilyFragment extends OptionsFragment{
    private TextView txtFamilyName;
    private ImageView familyImage;
    private Family family;
    private ListView listMembers;
    private ProgressBar loadImageProgressBar;
    private Button btnRemoveFamily;
    private CircularImageView userImage;

    public MyFamilyFragment(){
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.my_family_fragment, container, false);
        this.rootView = rootView;
        getViewsByXML();
        listeners();
        return rootView;
    }

    @Override
    protected void getViewsByXML() {
        txtFamilyName = (TextView) findViewById(R.id.txt_name_family);
        familyImage = (ImageView) findViewById(R.id.family_image);
        family = getFamilyById(ctrl.getActualUser().getFamilyId());
        loadImageProgressBar = (ProgressBar) findViewById(R.id.image_progress_bar);
        userImage = (CircularImageView) findViewById(R.id.circularImage);
        btnRemoveFamily = (Button) findViewById(R.id.btnRemoveFamily);
        loadFamilyOptions();
    }

    public void loadFamilyOptions(){
        txtFamilyName.setText(family.getFamilyName());
        loadFamilyImage();
        loadMembers();
    }

    private void loadMembers() {
        ctrl.getUserImage().addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                userImage.setImageBitmap(bitmap);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                loadMembers();
            }
        });
    }

    private void loadFamilyImage() {
        loadImageProgressBar.setVisibility(View.VISIBLE);
        ctrl.getFamilyImage().addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                familyImage.setImageBitmap(bitmap);
                
                loadImageProgressBar.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                loadFamilyImage();
            }
        });
    }

    public Family getFamilyById(String id){
        for(Family family : DatabaseOptions.getFamilies()){
            if (family.getFamilyId().equals(id)){
                return family;
            }
        }
        return null;
    }

    @Override
    protected void listeners() {
        btnRemoveFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ctrl.removeFamily(family);
                try {
                    MyFamilyFragment.this.finalize();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
    }
}
