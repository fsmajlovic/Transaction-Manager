package ba.unsa.etf.rma.transactionmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class HomeFragment extends Fragment {
    private ViewPager viewPager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = (ViewPager)fragmentView.findViewById(R.id.viewPager);
        setupViewPager(viewPager);
        return fragmentView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        SwipeAdapter swipeAdapter = new SwipeAdapter(getChildFragmentManager(), 1);
        swipeAdapter.addFragment(new TransactionListFragment());
        swipeAdapter.addFragment(new BudgetFragment());
        swipeAdapter.addFragment(new GraphsFragment());
        viewPager.setAdapter(swipeAdapter);
        viewPager.addOnPageChangeListener(new CircularViewPagerHandler(viewPager));

    }

}
