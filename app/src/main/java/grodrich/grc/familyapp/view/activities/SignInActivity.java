package grodrich.grc.familyapp.view.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import grodrich.grc.familyapp.R;
import grodrich.grc.familyapp.model.User;

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
        btnSignIn = (Button) findViewById(R.id.btn_sign_in);
        signInProgressBar = (ProgressBar) findViewById(R.id.sign_in_progress_bar);
    }

    @Override
    protected void listeners() {
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInAttempt();
            }
        });
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

    private boolean isValidName(String name) {
        return name.length() > 1;
    }

    private class UserSignInTask extends AsyncTask<Void,Void,Task<AuthResult>> {
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
        protected Task<AuthResult> doInBackground(Void... params) {
            Task<AuthResult> task = ctrl.attemptCreateUser(mEmail, mPassword);
            while (!task.isComplete());
            return task;
        }

        @Override
        protected void onPostExecute(final Task<AuthResult> task) {
            signInProgressBar.setVisibility(View.GONE);
            if (task.isSuccessful()){
                Snackbar.make(inputPassword, "User has been registered", Snackbar.LENGTH_SHORT).show();
                User user = ctrl.createUser(mName,mAge,mEmail,ctrl.getCurrentUser().getUid());
                ctrl.registerNewUser(user);
            }else{
                String exception = task.getException().getMessage();
                Snackbar.make(inputPassword,exception,Snackbar.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {

        }
    }
}
