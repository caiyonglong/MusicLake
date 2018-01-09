//package com.cyl.musiclake.ui.localmusic.fragment;
//
//import android.os.Build;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v7.app.ActionBar;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.Toolbar;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.cyl.musiclake.R;
//import com.cyl.musiclake.data.model.FolderInfo;
//import com.cyl.musiclake.ui.localmusic.contract.FoldersContract;
//import com.cyl.musiclake.ui.localmusic.presenter.FoldersPresenter;
//
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
///**
// * Created by D22434 on 2018/1/8.
// */
//
//public class FoldersFragment extends Fragment implements FoldersContract.View {
//
//    FoldersPresenter mPresenter;
//    @BindView(R.id.recyclerview)
//    FastScrollRecyclerView recyclerView;
//    @BindView(R.id.view_empty)
//    View emptyView;
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
//    private FolderAdapter mAdapter;
//
//    @Override
//    public void onCreate(final Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mPresenter = new FoldersPresenter(getActivity());
//        mPresenter.attachView(this);
//
//        mAdapter = new FolderAdapter((AppCompatActivity) getActivity(), null);
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.fragment_list_layout, container, false);
//        ButterKnife.bind(this, rootView);
//        return rootView;
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        if (Build.VERSION.SDK_INT < 21 && view.findViewById(R.id.status_bar) != null) {
//            view.findViewById(R.id.status_bar).setVisibility(View.GONE);
//            if (Build.VERSION.SDK_INT >= 19) {
//                int statusBarHeight = DensityUtil.getStatusBarHeight(getContext());
//                view.findViewById(R.id.toolbar).setPadding(0, statusBarHeight, 0, 0);
//            }
//        }
//
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//
//        final ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
//        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
//        ab.setDisplayHomeAsUpEnabled(true);
//        ab.setTitle(R.string.folders);
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setAdapter(mAdapter);
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST, false));
//
//        mPresenter.subscribe();
//        subscribeMetaChangedEvent();
//    }
//
//    @Override
//    public void onActivityCreated(final Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        setHasOptionsMenu(true);
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        mPresenter.unsubscribe();
//        RxBus.getInstance().unSubscribe(this);
//    }
//
//    @Override
//    public void showEmptyView() {
//        recyclerView.setVisibility(View.GONE);
//        emptyView.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void showFolders(List<FolderInfo> folderInfos) {
//        recyclerView.setVisibility(View.VISIBLE);
//        emptyView.setVisibility(View.GONE);
//        mAdapter.setFolderList(folderInfos);
//    }
//
//    @Override
//    public void showLoading() {
//
//    }
//
//    @Override
//    public void hideLoading() {
//
//    }
//}
