package grodrich.grc.familyapp.view.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import grodrich.grc.familyapp.R;
import grodrich.grc.familyapp.view.fragments.OptionsFragment;

public class HomeFragment extends OptionsFragment {
    public HomeFragment(){
    }

    @Override
    protected void getViewsByXML() {

    }

    @Override
    protected void listeners() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        return rootView;
    }

}