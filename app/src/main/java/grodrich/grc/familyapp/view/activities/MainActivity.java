package grodrich.grc.familyapp.view.activities;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import grodrich.grc.familyapp.R;

/**
 * Created by gabri on 26/08/16.
 */
public class MainActivity extends OptionsActivity {

    /*MAIN LAYOUTS*/
    private LinearLayout titleLayout;
    private LinearLayout loginLayout;

    /*TITLE LAYOUT*/
    private TextView txtTitle;
    private TextView txtAppName;
    private ProgressBar optionsProgress;

    /*LOGIN LAYOUT*/
    private AutoCompleteTextView inputEmail;
    private EditText inputPassword;
    private Button btnLogin;
    private TextView txtNoAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        getViewsByXML();
        listeners();
        loadLoginInformation();
    }



    @Override
    protected void getViewsByXML() {
        titleLayout = (LinearLayout) findViewById(R.id.titleLayout);
        loginLayout = (LinearLayout) findViewById(R.id.login_layout);

        /*TITLE LAYOUT*/
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtAppName = (TextView) findViewById(R.id.txtAppName);
        optionsProgress = (ProgressBar) findViewById(R.id.optionsProgress);

        /*LOGIN LAYOUT*/
        inputEmail = (AutoCompleteTextView) findViewById(R.id.input_email);
        inputPassword = (EditText) findViewById(R.id.input_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        txtNoAccount = (TextView) findViewById(R.id.txtNoAccount);
        //titleLayout.requestFocus();
    }

    @Override
    protected void listeners() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAttempt();
            }
        });
        txtNoAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchIntent(MainActivity.this,SignInActivity.class);
            }
        });
    }

    public void loginAttempt(){
        boolean isCorrect = true;
        inputEmail.setError(null);
        inputPassword.setError(null);

        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

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
            new UserLoginTask(email,password).execute((Void)null);
        }
    }

    private void saveLoginInformation(String email, String password){
        SharedPreferences sp = getSharedPreferences(getString(R.string.login_information), 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(getString(R.string.prompt_email), email);
        editor.putString(getString(R.string.prompt_password), password);
        editor.commit();
    }

    private String[] getLoginInformation(){
        SharedPreferences sp = getSharedPreferences(getString(R.string.login_information), 0);

        String email = sp.getString(getString(R.string.prompt_email), null);
        String password = sp.getString(getString(R.string.prompt_password), null);

        if (email != null && password != null){
            String [] ret = {email,password};
            return ret;
        }else{
            return null;
        }
    }

    private void loadLoginInformation() {
        String[] emailAndPassword = getLoginInformation();

        if (emailAndPassword != null){
            String email = emailAndPassword[0];
            String password = emailAndPassword[1];
            new UserLoginTask(email,password).execute((Void)null);
        }else {
            animationDelay(2000);
            optionsProgress.setVisibility(View.GONE);
        }

    }

    private void animationDelay(final long milis){
        Thread timerThread = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(milis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loginAnimation();
                        }
                    });

                }
            }
        });
        timerThread.start();
    }

    private void loginAnimation(){
        if (loginLayout.getVisibility() != View.VISIBLE) {
            titleLayout.animate().y(0).setDuration(1500);
            loginLayout.setVisibility(View.VISIBLE);
            loginLayout.setAlpha(0.0f);
            loginLayout.animate().alpha(1.0f).setDuration(2000);
        }
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        public UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected void onPreExecute() {
            if (!hasConection()) {
                cancel(true);
            }else{
                optionsProgress.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Task<AuthResult> task = ctrl.loginUser(mEmail,mPassword);
            while (!task.isComplete());
            return task.isSuccessful();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success){
                //TODO :Next Activity
                saveLoginInformation(mEmail,mPassword);
                Snackbar.make(inputPassword, "Login correct", Snackbar.LENGTH_SHORT).show();
                MainActivity.this.launchIntent(MainActivity.this,NavigationActivity.class);
            }else{
                animationDelay(200);
                if (getLoginInformation() != null) {
                    inputEmail.setText(getLoginInformation()[0]);
                    inputPassword.requestFocus();
                }
                Snackbar.make(inputPassword, "The password is not correct", Snackbar.LENGTH_SHORT).show();
            }
            optionsProgress.setVisibility(View.GONE);

        }

        @Override
        protected void onCancelled() {
            optionsProgress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        ctrl.initAuthenticateOptions();
    }

    @Override
    public void onStop() {
        super.onStop();
        ctrl.finishAuthenticateOptions();
    }
}
