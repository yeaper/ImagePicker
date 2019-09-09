package com.yyp.image.picker.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.yyp.image.picker.bean.Photo;
import com.yyp.image.picker.ui.SelectImageActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * 图片选择器连接类
 *
 * 连接到 SelectImageActivity，传递参数
 */
public class PhotoPicker {

    public final static int DEFAULT_MAX_COUNT = 9; //默认最多可选择的图片
    public final static int DEFAULT_COLUMN_NUMBER = 3; //默认展示的图片列数

    public final static String KEY_SELECTED_PHOTOS = "SELECTED_PHOTOS"; //选择结果intent回传的key
    public final static String EXTRA_MAX_COUNT = "MAX_COUNT"; //最多个数的参数key
    public final static String EXTRA_GRID_COLUMN = "COLUMN"; //展示图片列数的参数key

    private PhotoPicker(){}

    public static PhotoPickerBuilder builder() {
        return new PhotoPickerBuilder();
    }

    /**
     * 图片选择构建器
     */
    public static class PhotoPickerBuilder {

        private Bundle mPickerOptionsBundle;
        private Intent mPickerIntent;

        public PhotoPickerBuilder() {
            mPickerOptionsBundle = new Bundle();
            mPickerIntent = new Intent();
        }

        /**
         * 调起图片选择界面
         *
         * @param activity 接收结果的界面
         * @param requestCode 自定义请求码
         */
        public void start(@NonNull Activity activity, int requestCode) {
            activity.startActivityForResult(getIntent(activity), requestCode);
        }

        /**
         * 获取intent
         *
         * @param context 上下文
         * @return
         */
        public Intent getIntent(@NonNull Context context) {
            mPickerIntent.setClass(context, SelectImageActivity.class);
            mPickerIntent.putExtras(mPickerOptionsBundle);
            return mPickerIntent;
        }

        /**
         * 设置最多可选择的图片个数
         *
         * @param photoCount 图片个数
         * @return
         */
        public PhotoPickerBuilder setPhotoCount(int photoCount) {
            mPickerOptionsBundle.putInt(EXTRA_MAX_COUNT, photoCount);
            return this;
        }

        /**
         * 设置展示图片的列数
         *
         * @param columnCount 列数
         * @return
         */
        public PhotoPickerBuilder setGridColumnCount(int columnCount) {
            mPickerOptionsBundle.putInt(EXTRA_GRID_COLUMN, columnCount);
            return this;
        }
    }

    /**
     * 获取返回的数据集合
     *
     * @return
     */
    public static List<Photo> getResultList(Intent data){
        List<Photo> list = new ArrayList<>();
        if(data != null){
            list = (List<Photo>) data.getSerializableExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            if(list == null){
                list = new ArrayList<>();
            }
        }

        return list;
    }
}
