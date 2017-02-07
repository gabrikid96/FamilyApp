package grodrich.grc.familyapp.view.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;

import grodrich.grc.familyapp.R;
import grodrich.grc.familyapp.model.Family;
import grodrich.grc.familyapp.model.User;
import grodrich.grc.familyapp.view.activities.OptionsActivity;

/**
 * Created by lidia on 4/09/16.
 */
public class FamilyCreationFragment extends  OptionsFragment{
    private static final int IMAGE_SELECTOR = 1;
    private ImageView familyImage;
    private AutoCompleteTextView inputFamilyName;
    private EditText inputPassword;
    private Button btnCreateFamily;
    private AutoCompleteTextView memberEmail;
    private ProgressBar creationProgressBar;
    private Button btnAddMember;
    private ListView membersList;

    private ArrayList<String> emailMembers;
    private ArrayAdapter<String> stringAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.family_creation_fragment, container, false);
        this.rootView = rootView;
        emailMembers = new ArrayList<>();
        getViewsByXML();
        listeners();
        return rootView;
    }

    @Override
    protected void getViewsByXML() {
        familyImage = (ImageView) findViewById(R.id.familyImage);
        inputFamilyName = (AutoCompleteTextView) findViewById(R.id.input_family_name);
        inputPassword = (EditText) findViewById(R.id.family_input_password);
        btnCreateFamily = (Button) findViewById(R.id.btn_create_family);
        creationProgressBar = (ProgressBar) findViewById(R.id.family_progress_bar);
        memberEmail = (AutoCompleteTextView) findViewById(R.id.memberEmail);
        btnAddMember = (Button) findViewById(R.id.btnAddMember);
        membersList = (ListView) findViewById(R.id.membersList);
        stringAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,emailMembers);
        membersList.setAdapter(stringAdapter);
    }

    @Override
    protected void listeners() {
        btnCreateFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                familyCreationAttempt();
            }
        });
        familyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_image();
            }
        });
        btnAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = memberEmail.getText().toString();
                if (isValidEmail(email)){
                    emailMembers.add(email);
                    membersList.setVisibility(View.VISIBLE);
                    stringAdapter.notifyDataSetChanged();
                    memberEmail.setText("");
                }else{
                    memberEmail.setError(getString(R.string.error_invalid_email));
                }
            }
        });
    }

    private void select_image() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, getString(R.string.select_picture));
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, IMAGE_SELECTOR);
    }

    private void familyCreationAttempt() {
        String familyName = inputFamilyName.getText().toString();
        String password = inputPassword.getText().toString();
        boolean correct = true;

        if (familyName.isEmpty()){
            inputFamilyName.setError(getString(R.string.error_field_required));
            correct = false;
        }

        if (password.length() > 0 && password.length() < 4){
            inputPassword.setError(getString(R.string.error_invalid_password));
            correct = false;
        }

        if (password.isEmpty()){
            password = "";
        }

        if (correct){
            new CreationFamilyTask(familyName,password).execute((Void)null);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_SELECTOR && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                familyImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class CreationFamilyTask extends AsyncTask<Void, Void, UploadTask> {
        String familyName;
        String password;
        public CreationFamilyTask(String familyName, String password) {
            this.familyName = familyName;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            if (!hasConection()) {
                cancel(true);
            }else{
                creationProgressBar.setVisibility(View.VISIBLE);
            }
        }

        public void createFamily(){
            ArrayList<User> members = new ArrayList<User>();
            members.add(ctrl.getActualUser());

            for (String emailMember : emailMembers){ //getting users
                members.add(ctrl.getUsers().get(emailMember));
            }

            Family family = ctrl.createFamily(familyName,password,members);

            ctrl.associateFamily(family);
            ctrl.registerNewFamily(family);
        }

        @Override
        protected UploadTask doInBackground(Void... params) {
            createFamily();
            UploadTask task = ctrl.saveFamilyImage(familyImage);
            while (!task.isComplete());
            return task;
        }

        @Override
        protected void onPostExecute(final UploadTask task) {
            if (task.isSuccessful()){
                Snackbar.make(inputPassword, "Familia creada", Snackbar.LENGTH_SHORT).show();
                //TODO : user notification
            }else{
                Snackbar.make(inputPassword, "Familia no creada", Snackbar.LENGTH_SHORT).show();
            }
            creationProgressBar.setVisibility(View.GONE);

        }

        @Override
        protected void onCancelled() {
            creationProgressBar.setVisibility(View.GONE);
        }
    }


}
