package com.example.community.request_list;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.community.classes.SearchHelper;
import com.example.community.databinding.FragmentPostListBinding;

import java.util.ArrayList;

public class ReqPostFragment extends Fragment {

    private static final String TAG = "REQ_POST_FRAGMENT";
    private FragmentPostListBinding binding_req;

    @RequiresApi(api = Build.VERSION_CODES.R)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding_req = FragmentPostListBinding.inflate(inflater, container, false);

        View root = binding_req.getRoot();
        final RecyclerView listView = binding_req.postList;
        LinearLayoutManager manager = new LinearLayoutManager(requireContext());
        listView.setLayoutManager(manager);
        SwipeRefreshLayout refresher = binding_req.pullToRefresh;
        refresher.setOnRefreshListener(() -> {
            Log.d(TAG, "onCreateView: Refreshing");
            SearchHelper.search(requireContext());
            Observer<Boolean> observer = new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean loading) {
                    if (!loading) {
                        SearchHelper.getLoadingData().removeObserver(this);
                        refresher.setRefreshing(false);
                    }
                }
            };
            SearchHelper.getLoadingData().observe(getViewLifecycleOwner(), observer);
        });

        ReqPostAdapter adapter = new ReqPostAdapter(requireContext(),
                new ArrayList<>());
        listView.setAdapter(adapter);

        SearchHelper.getRequestLiveData().observe(getViewLifecycleOwner(), reqList -> {
            adapter.setItems(reqList);
            adapter.notifyDataSetChanged();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding_req = null;
    }
}


