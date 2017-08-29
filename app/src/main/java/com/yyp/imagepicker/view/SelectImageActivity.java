package com.yyp.imagepicker.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.yyp.imagepicker.R;
import com.yyp.imagepicker.interfaces.OnItemCheckListener;
import com.yyp.imagepicker.model.Photo;
import com.yyp.imagepicker.view.fragment.PhotoPickerFragment;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;
import static com.yyp.imagepicker.util.PhotoPicker.DEFAULT_COLUMN_NUMBER;
import static com.yyp.imagepicker.util.PhotoPicker.DEFAULT_MAX_COUNT;
import static com.yyp.imagepicker.util.PhotoPicker.EXTRA_GRID_COLUMN;
import static com.yyp.imagepicker.util.PhotoPicker.EXTRA_MAX_COUNT;
import static com.yyp.imagepicker.util.PhotoPicker.KEY_SELECTED_PHOTOS;

public class SelectImageActivity extends AppCompatActivity {

    private PhotoPickerFragment pickerFragment;
    private MenuItem menuDoneItem;

    private int maxCount = DEFAULT_MAX_COUNT;
    private boolean menuIsInflated = false;
    private int columnNumber = DEFAULT_COLUMN_NUMBER;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picker_activity_photo_picker);

        setActionBar();
        // 获取 intent 传进来的参数
        maxCount = getIntent().getIntExtra(EXTRA_MAX_COUNT, DEFAULT_MAX_COUNT);
        columnNumber = getIntent().getIntExtra(EXTRA_GRID_COLUMN, DEFAULT_COLUMN_NUMBER);

        // 创建一个图片选择 Fragment 并显示
        pickerFragment = (PhotoPickerFragment) getSupportFragmentManager().findFragmentByTag("tag");
        if (pickerFragment == null) {
            pickerFragment = PhotoPickerFragment
                    .newInstance(columnNumber, maxCount);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, pickerFragment, "tag")
                    .commit();
            getSupportFragmentManager().executePendingTransactions();
        }
        // 监听图片选择，更新完成按钮的内容
        pickerFragment.getPhotoGridAdapter().setOnItemCheckListener(new OnItemCheckListener() {
            @Override public boolean onItemCheck(int position, Photo photo, final int selectedItemCount) {

                menuDoneItem.setEnabled(selectedItemCount > 0);

                /* 最多只能选择一张时，第二次选择另一个，则清除前一个选中的
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
                    Toast.makeText(getActivity(), getString(R.string.__picker_over_max_count_tips, maxCount),
                            LENGTH_LONG).show();
                    return false;
                }
                menuDoneItem.setTitle(getString(R.string.__picker_done_with_count, selectedItemCount, maxCount));
                return true;
            }
        });

    }

    /**
     * 设置 Actionbar 相关
     */
    public void setActionBar(){
        setTitle("选取照片");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
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
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }

        // 监听是否按下完成选取键，传递选中的图片路径集合
        if (item.getItemId() == R.id.done) {
            Intent intent = new Intent();
            ArrayList<String> selectedPhotos = pickerFragment.getPhotoGridAdapter().getSelectedPhotoPaths();
            intent.putStringArrayListExtra(KEY_SELECTED_PHOTOS, selectedPhotos);
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
