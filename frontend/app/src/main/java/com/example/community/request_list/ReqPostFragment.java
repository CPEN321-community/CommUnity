package com.example.community.request_list;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.VolleyError;
import com.example.community.VolleyCallBack;
import com.example.community.databinding.FragmentPostListBinding;

public class ReqPostFragment extends Fragment {

    private static final String TAG = "REQ_POST_FRAGMENT";
    private FragmentPostListBinding binding_req;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        ReqPostViewModel reqPostViewModel = new ViewModelProvider(this).get(ReqPostViewModel.class);
        binding_req = FragmentPostListBinding.inflate(inflater, container, false);

        View root = binding_req.getRoot();
        final RecyclerView listView = binding_req.offerPostList;
        listView.setLayoutManager(new LinearLayoutManager(requireContext()));
        SwipeRefreshLayout refresher = binding_req.pullToRefresh;
        refresher.setOnRefreshListener(() -> {
            Log.d(TAG, "onCreateView: Refreshing");
            reqPostViewModel.fetchReqPosts(new VolleyCallBack() {
                @Override
                public void onError(VolleyError error) {
                }

                @Override
                public void onSuccess(boolean b) {
                }

                @Override
                public void onSuccess() {
                    refresher.setRefreshing(false);
                }

                @Override
                public void onError() {
                    refresher.setRefreshing(false);
                }
            });
        });
        reqPostViewModel.getList().observe(getViewLifecycleOwner(), reqList -> {
            ReqPostAdapter adapter;
            adapter = new ReqPostAdapter(requireContext(),
                    reqList);
            listView.setAdapter(adapter);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding_req = null;
    }
}


