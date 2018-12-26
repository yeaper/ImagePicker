package com.yyp.image.picker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.yyp.image.picker.R;
import com.yyp.image.picker.interfaces.OnItemCheckListener;
import com.yyp.image.picker.model.Photo;
import com.yyp.image.picker.ui.fragment.PhotoPickerFragment;
import com.yyp.image.picker.util.PhotoPicker;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择图片界面
 */
public class SelectImageActivity extends AppCompatActivity {

    private PhotoPickerFragment pickerFragment;
    private MenuItem menuDoneItem;

    private int maxCount = PhotoPicker.DEFAULT_MAX_COUNT;
    private boolean menuIsInflated = false;
    private int columnNumber = PhotoPicker.DEFAULT_COLUMN_NUMBER;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getIntent() != null){
            // 获取 intent 传进来的参数
            maxCount = getIntent().getIntExtra(PhotoPicker.EXTRA_MAX_COUNT, PhotoPicker.DEFAULT_MAX_COUNT);
            columnNumber = getIntent().getIntExtra(PhotoPicker.EXTRA_GRID_COLUMN, PhotoPicker.DEFAULT_COLUMN_NUMBER);
        }else{
            Toast.makeText(this, "参数错误！", Toast.LENGTH_SHORT).show();
            finish();
        }

        setContentView(R.layout.picker_activity_photo_picker);
        setActionBar();
        showSelectFragment();
    }

    /**
     * 设置 Actionbar 相关
     */
    private void setActionBar(){
        setTitle("选取照片");
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); //显示返回按键
        }
    }

    /**
     * 显示图片选择的Fragment
     */
    private void showSelectFragment(){
        // 创建一个图片选择 Fragment 并显示
        FragmentManager fragmentManager = getSupportFragmentManager();
        pickerFragment = (PhotoPickerFragment) fragmentManager.findFragmentByTag("tag");
        if (pickerFragment == null) {
            pickerFragment = PhotoPickerFragment.newInstance(columnNumber, maxCount);
            fragmentManager.beginTransaction()
                    .add(R.id.container, pickerFragment, "tag")
                    .commit();
            fragmentManager.executePendingTransactions();
        }else{
            fragmentManager.beginTransaction()
                    .show(pickerFragment)
                    .commit();
        }

        setListener();
    }

    private void setListener() {
        // 监听图片选择，更新完成按钮的内容
        pickerFragment.getPhotoGridAdapter().setOnItemCheckListener(new OnItemCheckListener() {
            @Override
            public boolean onItemCheck(int position, Photo photo, final int selectedItemCount) {

                menuDoneItem.setEnabled(selectedItemCount > 0); //完成按钮可点击

                /*
                    最多只能选择一张时，第二次选择另一个，则清除前一个选中的
                    返回是否开关选择框
                */
                if (maxCount <= 1) {
                    List<String> photos = pickerFragment.getPhotoGridAdapter().getSelectedPhotos();
                    if (!photos.contains(photo.getPath())) {
                        photos.clear();
                        pickerFragment.getPhotoGridAdapter().notifyDataSetChanged();
                    }
                    return true;
                }
                // 超过选取的最大个数，进行提示
                if (selectedItemCount > maxCount) {
                    Toast.makeText(getActivity(), getString(R.string.picker_over_max_count_tips, maxCount),
                            Toast.LENGTH_LONG).show();
                    return false;
                }
                // 设置完成按钮的内容
                menuDoneItem.setTitle(getString(R.string.picker_done_with_count, selectedItemCount, maxCount));
                return true;
            }
        });
    }


    /**
     * 初始化、配置菜单
     * @param menu
     * @return
     */
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        if (!menuIsInflated) {
            getMenuInflater().inflate(R.menu.picker_menu_picker, menu);
            menuDoneItem = menu.findItem(R.id.done);
            // 刚进来，默认不可点击
            menuDoneItem.setEnabled(false);
            // 菜单初始化完成标志
            menuIsInflated = true;
            return true;
        }
        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { //监听返回键
            super.onBackPressed();
            return true;
        }

        // 监听是否按下完成选取键，传递选中的图片路径集合
        if (item.getItemId() == R.id.done) {
            Intent intent = new Intent();
            ArrayList<String> selectedPhotos = pickerFragment.getSelectedPhotoPaths();
            intent.putStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS, selectedPhotos);
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public SelectImageActivity getActivity() {
        return this;
    }
}
