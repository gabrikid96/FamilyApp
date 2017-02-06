package grodrich.grc.familyapp.view.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import grodrich.grc.familyapp.R;
import grodrich.grc.familyapp.model.User;
import grodrich.grc.familyapp.view.views.CircularImageView;

/**
 * Created by gabri on 26/08/16.
 */
public class SignInActivity extends OptionsActivity {

    //Sign In View
    private AutoCompleteTextView inputName;
    private AutoCompleteTextView inputAge;
    private AutoCompleteTextView inputEmail;
    private EditText inputPassword;
    private Button btnSignIn;
    private ProgressBar signInProgressBar;
    private CircularImageView userImage;
    private static final int IMAGE_SELECTOR = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getViewsByXML();
        listeners();
    }

    @Override
    protected void getViewsByXML() {
        inputName = (AutoCompleteTextView) findViewById(R.id.input_name);
        inputAge = (AutoCompleteTextView) findViewById(R.id.input_age);
        inputEmail = (AutoCompleteTextView) findViewById(R.id.sign_in_input_email);
        inputPassword = (EditText) findViewById(R.id.sign_in_input_password);
        btnSignIn = (Button) findViewById(R.id.btn_create_family);
        signInProgressBar = (ProgressBar) findViewById(R.id.sign_in_progress_bar);
        userImage = (CircularImageView) findViewById(R.id.userImage);
    }

    @Override
    protected void listeners() {
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInAttempt();
            }
        });
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select_image();
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_SELECTOR && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getBaseContext().getContentResolver(), uri);
                userImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void signInAttempt() {
        boolean isCorrect = true;
        inputEmail.setError(null);
        inputPassword.setError(null);

        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        String name = inputName.getText().toString();
        String age = inputAge.getText().toString();

        if (name.isEmpty() || !isValidName(name)){
            inputName.setError(getString(R.string.error_invalid_name));
            isCorrect = false;
        }

        if (age.isEmpty()){
            inputAge.setError(getString(R.string.error_field_required));
            isCorrect = false;
        }

        if (password.isEmpty() || !isValidPassword(password)){
            inputPassword.setError(getString(R.string.error_invalid_password));
            isCorrect = false;
        }

        if (email.isEmpty()){
            inputEmail.setError(getString(R.string.error_field_required));
            isCorrect = false;
        }else if (!isValidEmail(email)){
            inputEmail.setError(getString(R.string.error_invalid_email));
            isCorrect = false;
        }

        if (isCorrect){
            new UserSignInTask().execute((Void)null);
        }


    }

    private void goodSignIn(String mEmail, String mPassword ){
        saveLoginInformation(mEmail,mPassword);
        SignInActivity.this.launchIntent(SignInActivity.this, NavigationActivity.class);
        finish();
    }

    private boolean isValidName(String name) {
        return name.length() > 1;
    }

    private class UserSignInTask extends AsyncTask<Void,Void,Task> {
        private final String mEmail;
        private final String mPassword;
        private final String mName;
        private final String mAge;

        public UserSignInTask() {
            mEmail = inputEmail.getText().toString();
            mPassword = inputPassword.getText().toString();
            mAge = inputAge.getText().toString();
            mName = inputName.getText().toString();
        }

        @Override
        protected void onPreExecute() {
            if (hasConection()) {
                signInProgressBar.setVisibility(View.VISIBLE);
            }else{
                cancel(true);
            }
        }

        @Override
        protected Task doInBackground(Void... params) {
            Task<AuthResult> task = ctrl.attemptCreateUser(mEmail, mPassword);
            while (!task.isComplete());

            if (task.isSuccessful()){
                User user = ctrl.createUser(mName,mAge,mEmail,ctrl.getCurrentUser().getUid());
                ctrl.registerNewUser(user);
                ctrl.setActualUser(user);

                UploadTask taskImage = ctrl.saveUserImage(userImage);
                while (!taskImage.isComplete());
                return taskImage;
            }
            return task;
        }

        @Override
        protected void onPostExecute(final Task task) {
            if (task.isSuccessful()){
                Snackbar.make(inputPassword, "User has been registered", Snackbar.LENGTH_SHORT).show();
                goodSignIn(mEmail,mPassword);
            }else{
                String exception = task.getException().getMessage();
                Snackbar.make(inputPassword,exception,Snackbar.LENGTH_SHORT).show();
            }
            signInProgressBar.setVisibility(View.GONE);
        }

        @Override
        protected void onCancelled() {

        }
    }
}
