package grodrich.grc.familyapp.view.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import grodrich.grc.familyapp.controller.Controller;
import grodrich.grc.familyapp.view.activities.NavigationActivity;

/**
 * Created by gabri on 2/09/16.
 */
public abstract class OptionsFragment extends Fragment {
    protected Controller ctrl;
    protected View rootView;
    public OptionsFragment(){
        ctrl = Controller.getInstance();
    }

    protected abstract void getViewsByXML();
    protected abstract void listeners();

    protected View findViewById(int id){
        return rootView.findViewById(id);
    }

    public boolean hasConection(){
        ConnectivityManager conMgr = (ConnectivityManager) getContext()
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

    public boolean isValidEmail(String email){
        return email.contains("@");
    }


}
