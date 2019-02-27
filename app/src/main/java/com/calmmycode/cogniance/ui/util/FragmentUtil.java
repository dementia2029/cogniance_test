package com.calmmycode.cogniance.ui.util;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.calmmycode.cogniance.model.exceptions.FragmentNotFoundException;
import com.calmmycode.cogniance.ui.fragment.SearchFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import retrofit2.internal.EverythingIsNonNull;

public class FragmentUtil {
    private static Fragment getCurrentVisibleFragment(FragmentManager fm) {
        for (Fragment fragment : fm.getFragments()) {
            if (fragment.isVisible()) {
                return fragment;
            }
        }
        return null;
    }

    public static boolean performFragmentPopBackStack(FragmentManager fm){
        // check if fragment has child fragments. just for sure.
        for (Fragment fragment : fm.getFragments()) {
            if(fragment.isVisible()){
                if (fragment.getChildFragmentManager().getBackStackEntryCount() > 0) {
                    fragment.getChildFragmentManager().popBackStack();
                    return true;
                }
            }
        }
        if(fm.getBackStackEntryCount() > 0){
            fm.popBackStack();
            return true;
        }
        return false;
    }

    @EverythingIsNonNull
    public static void loadNewFragment(String fragmentId, FragmentManager fm, List<String> fragmentIds, ViewGroup container){
        loadNewFragment(fragmentId, fm, fragmentIds, container, null);
    }

    public static void loadNewFragment(@NonNull String fragmentId, @NonNull FragmentManager fm, @NonNull List<String> fragmentIds, @NonNull ViewGroup container, Bundle args){
        if(!fragmentIds.contains(fragmentId)){
            throw new FragmentNotFoundException();
        }
        Fragment currentFragment = getCurrentVisibleFragment(fm);
        if(currentFragment != null &&currentFragment.getTag() != null && currentFragment.getTag().equals(fragmentId)){
            return;
        }
        Fragment newFragment = fm.findFragmentByTag(fragmentId);
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if(currentFragment != null){
            fragmentTransaction.hide(currentFragment);
        }
        if(newFragment == null){
            try {
                newFragment = (Fragment) Class.forName(fragmentId).newInstance();
                newFragment.setArguments(args);
                fragmentTransaction.add(container.getId(), newFragment, fragmentId);
            } catch (IllegalAccessException | InstantiationException | ClassNotFoundException | ClassCastException e) {
                e.printStackTrace();
            }
        }else {
            fragmentTransaction.attach(newFragment);
        }
        if(newFragment instanceof SearchFragment ){
            fragmentTransaction.disallowAddToBackStack();
        } else {
            fragmentTransaction.addToBackStack("");
        }
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.commit();
    }
}
