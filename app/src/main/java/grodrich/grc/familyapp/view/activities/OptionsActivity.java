package grodrich.grc.familyapp.view.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import grodrich.grc.familyapp.R;
import grodrich.grc.familyapp.controller.Controller;

/**
 * Created by gabri on 26/08/16.
 */
public abstract class OptionsActivity extends AppCompatActivity {

    protected Controller ctrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctrl = Controller.getInstance();
    }

    protected abstract void getViewsByXML();
    protected abstract void listeners();

    public boolean isValidPassword(String password){
        return password.length() > 4;
    }

    public boolean isValidEmail(String email){
        return email.contains("@");
    }

    public boolean hasConection(){
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        if (i == null)
            return false;
        if (!i.isConnected())
            return false;
        if (!i.isAvailable())
            return false;
        return true;
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

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.transition_right_in, R.anim.transition_right_out);
    }

    public void launchIntent(android.content.Context packageContext,java.lang.Class<?> cls){
        Intent intent = new Intent(packageContext, cls);
        startActivity(intent);
        overridePendingTransition(R.anim.transition_left_in, R.anim.transition_left_out);

    }

    public void launchIntentForResult(android.content.Context packageContext,java.lang.Class<?> cls, int requestCode){
        startActivityForResult(new Intent(packageContext,cls),requestCode);
    }

    public void launchIntentForResult(android.content.Context packageContext,java.lang.Class<?> cls, int requestCode, Bundle options){
        startActivity(new Intent(packageContext,cls));
    }

    protected void saveLoginInformation(String email, String password){
        SharedPreferences sp = getSharedPreferences(getString(R.string.login_information), 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(getString(R.string.prompt_email), email);
        editor.putString(getString(R.string.prompt_password), password);
        editor.commit();
    }

}
