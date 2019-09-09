package com.yyp.image.picker.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.yyp.image.picker.R;
import com.yyp.image.picker.adapter.PhotoGridAdapter;
import com.yyp.image.picker.adapter.PopupDirectoryListAdapter;
import com.yyp.image.picker.bean.Photo;
import com.yyp.image.picker.bean.PhotoDirectory;
import com.yyp.image.picker.util.AndroidLifecycleUtils;
import com.yyp.image.picker.util.ImageCaptureManager;
import com.yyp.image.picker.util.MediaStoreHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.yyp.image.picker.util.MediaStoreHelper.INDEX_ALL_PHOTOS;
import static com.yyp.image.picker.util.PhotoPicker.DEFAULT_COLUMN_NUMBER;

/**
 * 图片选择Fragment
 */
public class PhotoPickerFragment extends Fragment {

    private RecyclerView mRvPhoto;

    private ImageCaptureManager captureManager;
    private PhotoGridAdapter photoGridAdapter;

    private PopupDirectoryListAdapter listAdapter;
    private List<PhotoDirectory> directories; //所有图片文件夹

    private int SCROLL_THRESHOLD = 30;
    private int column;
    //目录弹出框的一次最多显示的目录数目
    public static int COUNT_MAX = 4;
    private final static String EXTRA_COLUMN = "column";
    private final static String EXTRA_COUNT = "count";
    private ListPopupWindow listPopupWindow;
    private RequestManager mGlideRequestManager;

    Button btSwitchDirectory;

    /**
     * 获取 PhotoPickerFragment实例 带参数
     *
     * @param column
     * @param maxCount
     * @return
     */
    public static PhotoPickerFragment newInstance(int column, int maxCount) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_COLUMN, column);
        args.putInt(EXTRA_COUNT, maxCount);
        PhotoPickerFragment fragment = new PhotoPickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getParam();
        init();
    }

    private void getParam(){
        if (getArguments() != null) {
            column = getArguments().getInt(EXTRA_COLUMN, DEFAULT_COLUMN_NUMBER);
        }else{
            column = DEFAULT_COLUMN_NUMBER;
        }
    }

    /**
     * 初始化
     */
    public void init(){
        mGlideRequestManager = Glide.with(this);
        directories = new ArrayList<>();
        photoGridAdapter = new PhotoGridAdapter(directories);
        photoGridAdapter.setShowCamera(true);
        listAdapter = new PopupDirectoryListAdapter(mGlideRequestManager, directories);
        captureManager = new ImageCaptureManager(getActivity());

        // 获取图片
        MediaStoreHelper.getPhotoDirs(getActivity(), null,
                new MediaStoreHelper.PhotosResultCallback() {
                    @Override
                    public void onResultCallback(List<PhotoDirectory> dirs) {
                        directories.clear();
                        directories.addAll(dirs);
                        photoGridAdapter.notifyDataSetChanged();
                        listAdapter.notifyDataSetChanged();
                        adjustHeight();
                    }
                });
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_select_image, container, false);
        mRvPhoto = rootView.findViewById(R.id.rv_select_image);
        btSwitchDirectory = rootView.findViewById(R.id.select_image_folder);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initPhotoList();
        initDirPopupWindow();
        listen();
    }

    /**
     * 初始化图片列表控件
     */
    private void initPhotoList(){
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(column, OrientationHelper.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mRvPhoto.setLayoutManager(layoutManager);
        mRvPhoto.setAdapter(photoGridAdapter);
    }

    /**
     * 初始化文件夹弹窗
     */
    private void initDirPopupWindow(){
        // 实例化文件夹列表弹窗
        listPopupWindow = new ListPopupWindow(getActivity());
        listPopupWindow.setWidth(ListPopupWindow.MATCH_PARENT);
        listPopupWindow.setAnchorView(btSwitchDirectory);
        listPopupWindow.setAdapter(listAdapter);
        listPopupWindow.setModal(true);
        listPopupWindow.setDropDownGravity(Gravity.BOTTOM);

        // 点击图片文件夹列表，显示该文件夹底下的所有图片
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listPopupWindow.dismiss();

                PhotoDirectory directory = directories.get(position);
                btSwitchDirectory.setText(directory.getName());

                photoGridAdapter.setCurrentDirectoryIndex(position);
                photoGridAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 监听
     */
    private void listen(){
        photoGridAdapter.setOnCameraClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //点击拍照
                openCamera();
            }
        });

        btSwitchDirectory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //显示文件夹弹窗
                if (listPopupWindow.isShowing()) {
                    listPopupWindow.dismiss();
                } else if (!getActivity().isFinishing()) {
                    adjustHeight();
                    listPopupWindow.show();
                }
            }
        });


        // 滑动过程中不请求加载图片，停止后才加载
        mRvPhoto.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Math.abs(dy) > SCROLL_THRESHOLD) {
                    mGlideRequestManager.pauseRequests();
                } else {
                    resumeRequestsIfNotDestroyed();
                }
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    resumeRequestsIfNotDestroyed();
                }
            }
        });
    }
    /**
     * 打开照相机
     */
    private void openCamera() {
        try {
            Intent intent = captureManager.dispatchTakePictureIntent();
            startActivityForResult(intent, ImageCaptureManager.REQUEST_TAKE_PHOTO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 扫描到照片返回集合结果
        if (requestCode == ImageCaptureManager.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            if (captureManager == null) {
                FragmentActivity activity = getActivity();
                captureManager = new ImageCaptureManager(activity);
            }

            captureManager.galleryAddPic();
            if (directories.size() > 0) {
                String path = captureManager.getCurrentPhotoPath();
                PhotoDirectory directory = directories.get(INDEX_ALL_PHOTOS);
                directory.getPhotos().add(INDEX_ALL_PHOTOS, new Photo(path.hashCode(), path));
                directory.setCoverPath(path);
                photoGridAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 获取图片列表适配器
     *
     * @return
     */
    public PhotoGridAdapter getPhotoGridAdapter() {
        return photoGridAdapter;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        captureManager.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        captureManager.onRestoreInstanceState(savedInstanceState);
        super.onViewStateRestored(savedInstanceState);
    }

    /**
     * 获取选中的图片列表
     *
     * @return
     */
    public List<Photo> getSelectedPhotos() {
        return photoGridAdapter.getSelectedPhotos();
    }

    /**
     * 调整文件夹弹出容器的高度
     */
    public void adjustHeight() {
        if (listAdapter == null) return;
        int count = listAdapter.getCount();
        count = count < COUNT_MAX ? count : COUNT_MAX;
        if (listPopupWindow != null) {
            listPopupWindow.setHeight(count * getResources().getDimensionPixelOffset(R.dimen.__picker_item_directory_height));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (directories == null) {
            return;
        }

        for (PhotoDirectory directory : directories) {
            directory.getPhotoPaths().clear();
            directory.getPhotos().clear();
            directory.setPhotos(null);
        }
        directories.clear();
        directories = null;
    }

    private void resumeRequestsIfNotDestroyed() {
        if (!AndroidLifecycleUtils.canLoadImage(this)) {
            return;
        }

        mGlideRequestManager.resumeRequests();
    }
}
