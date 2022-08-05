package com.example.community.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.community.classes.SearchHelper;
import com.example.community.classes.TagHelper;
import com.example.community.databinding.FragmentHomeBinding;
import com.example.community.databinding.TimeSunMoonElementBinding;
import com.example.community.offer_list.NewOfferForm;
import com.example.community.request_list.NewRequestForm;
import com.example.community.ui.TagAdapter;
import com.example.community.ui.chat.ChatActivity;
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

        RecyclerView tagList = binding.includeTagsHome.tagsList;
        tagList.setVisibility(View.INVISIBLE);
        tagList.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        TagAdapter adapter = new TagAdapter(requireContext(), TagHelper.reset());
        tagList.setAdapter(adapter);

        TagHelper.getTagData().observe(getViewLifecycleOwner(), ts -> {
            adapter.setItems(ts);
            adapter.notifyDataSetChanged();
            if (TagHelper.getClickedTags().size() > 0) {
                binding.toolbar.searchToolbar.mSearchSrcTextView.setEnabled(false);
                binding.toolbar.searchToolbar.mSearchSrcTextView.setText("");
            } else {
                binding.toolbar.searchToolbar.mSearchSrcTextView.setEnabled(true);
            }
            SearchHelper.search(requireContext());
        });

        binding.toolbar.chatButton.setOnClickListener((view) -> {
            Intent chatIntent = new Intent(requireActivity(), ChatActivity.class);
            startActivity(chatIntent);
        });

        binding.addPostButton.setOnClickListener(v -> {
            TagHelper.reset();
            Intent newOfferIntent = new Intent(requireContext(), NewOfferForm.class);
            requireActivity().startActivity(newOfferIntent);
        });

        binding.toolbar.searchToolbar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                SearchHelper.search(requireContext());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                SearchHelper.setQuery(s);
                return false;
            }
        });

        binding.toolbar.searchToolbar.mSearchSrcTextView.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus || TagHelper.getClickedTags().size() > 0) {
                tagList.setVisibility(View.VISIBLE);
            } else {
                tagList.setVisibility(View.INVISIBLE);
            }
        });
        binding.toolbar.searchToolbar.setOnCloseListener(() -> {
            TagHelper.reset();
            tagList.setVisibility(View.INVISIBLE);
            return false;
        });

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
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tabLayout.getSelectedTabPosition();
                if (position == 0) {
                    binding.addPostButton.setOnClickListener(v -> {
                        TagHelper.reset();
                        Intent newOfferIntent = new Intent(requireContext(), NewOfferForm.class);
                        requireActivity().startActivity(newOfferIntent);
                    });
                } else {
                    binding.addPostButton.setOnClickListener(v -> {
                        TagHelper.reset();
                        Intent newReqIntent = new Intent(requireContext(), NewRequestForm.class);
                        requireActivity().startActivity(newReqIntent);
                    });
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Handled inside tabselected
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Handled inside tabselected
            }
        });

    }

    private void initSunMoonWatcher() {
        TimeSunMoonElementBinding sunMoonElementBinding = binding.sunMoonBg;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        new SunMoonWatcher(sunMoonElementBinding, width, height);
    }
}