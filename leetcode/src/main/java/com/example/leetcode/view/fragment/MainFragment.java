package com.example.leetcode.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.leetcode.BR;
import com.example.leetcode.R;
import com.example.leetcode.RVBindingAdapter;
import com.example.leetcode.SuperBindingViewHolder;
import com.example.leetcode.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.fragment.NavHostFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    private static final String TAG =  "MainFragment-zhaoy";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private MainViewModel mState;

    private ViewDataBinding mBinding;
    private RVBindingAdapter mAdapter;

    public MainFragment() {
    }

    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: ");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mState = new ViewModelProvider(this).get(MainViewModel.class);
        mState.list.observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                mAdapter.setDataList(strings);
            }
        });

        Log.d(TAG, "onCreate: ");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        mBinding.setLifecycleOwner(this);
        mAdapter = new RVBindingAdapter(getActivity(), BR.text) {
            @Override
            public void setItemClick(SuperBindingViewHolder holder, int position) {
                holder.getBinding().setVariable(BR.presenter, new Presenter(position));
            }
        };
        mBinding.setVariable(BR.adapter, mAdapter);

        Log.d(TAG, "onCreateView: ");

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<String> data = new ArrayList<>();
        data.add(getActivity().getString(R.string.binary_search));
        data.add(getActivity().getString(R.string.dp));
        data.add(getActivity().getString(R.string.pattern));
        data.add(getActivity().getString(R.string.sort));
        data.add(getActivity().getString(R.string.stack));
        data.add(getActivity().getString(R.string.tree));

        mState.list.setValue(data);

        Log.d(TAG, "onViewCreated: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBinding.unbind();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: ");
    }

    public class Presenter {
        int position;

        public Presenter(int position) {
            this.position = position;
        }

        public void openFragment(){
            String tab = (String) mAdapter.getDataList().get(position);
            Log.d("TAG", "openFragment: " + tab);
            NavController controller = NavHostFragment.findNavController(MainFragment.this);
            switch (tab) {
                case "查找":
                    controller.navigate(R.id.action_mainFragment_to_searchFragment);
                    break;
                case "动态规划":
                    controller.navigate(R.id.action_mainFragment_to_dpFragment);
                    break;
                case "设计模式":
                    controller.navigate(R.id.action_mainFragment_to_patternFragment);
                    break;
                case "排序":
                    controller.navigate(R.id.action_mainFragment_to_sortFragment);
                    break;
                case "栈问题":
                    controller.navigate(R.id.action_mainFragment_to_stackFragment);
                    break;
                case "字符串问题":
                    controller.navigate(R.id.action_mainFragment_to_stringFragment);
                    break;
                case "Tree问题":
                    controller.navigate(R.id.action_mainFragment_to_treeFragment);
                    break;
            }
        }
    }
}