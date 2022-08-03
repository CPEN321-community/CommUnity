package com.example.community.ui.home;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.community.databinding.FragmentHomeBinding;
import com.example.community.databinding.TimeSunMoonElementBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.messaging.FirebaseMessaging;

public class HomeFragment extends Fragment {

    private static final String TAG = "HOME_FRAGMENT";
    private static final String[] tabNames = {"Offers", "Requests"};
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            String token = task.getResult();
            Log.d(TAG, "onCreateView: " + token);
        });
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTabViewPager();
        initSunMoonWatcher();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initTabViewPager() {
        final PostTabAdapter adapter = new PostTabAdapter(getChildFragmentManager(), getLifecycle());
        ViewPager2 tabViewPager = binding.postTabs.tabViewPager;
        tabViewPager.canScrollHorizontally(0);
        tabViewPager.setUserInputEnabled(false);
        TabLayout tabLayout = binding.postTabs.tabLayout;
        tabViewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, tabViewPager,
                (tab, position) -> tab.setText(tabNames[position])
        ).attach();

    }

    private void initSunMoonWatcher() {
        TimeSunMoonElementBinding sunMoonElementBinding = binding.sunMoonBg;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        new SunMoonWatcher(sunMoonElementBinding, width, height);
    }
}