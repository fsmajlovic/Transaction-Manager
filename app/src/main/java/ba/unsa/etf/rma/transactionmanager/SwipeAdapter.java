package ba.unsa.etf.rma.transactionmanager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SwipeAdapter extends FragmentPagerAdapter  {
    private List<Fragment> fragmentList;

    public SwipeAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        fragmentList = new ArrayList<Fragment>();
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }


    @Override
    public int getCount() {
        return fragmentList.size();
    }


    public void addFragment(Fragment fragment){
        fragmentList.add(fragment);
    }

}
