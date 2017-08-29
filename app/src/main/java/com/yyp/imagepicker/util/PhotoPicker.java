package com.yyp.imagepicker.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.yyp.imagepicker.view.SelectImageActivity;

/**
 * 连接到 SelectImageActivity，传递参数
 */
public class PhotoPicker {

  public static final int REQUEST_CODE = 233;

  public final static int DEFAULT_MAX_COUNT = 9;
  public final static int DEFAULT_COLUMN_NUMBER = 3;

  public final static String KEY_SELECTED_PHOTOS = "SELECTED_PHOTOS";

  public final static String EXTRA_MAX_COUNT = "MAX_COUNT";
  public final static String EXTRA_GRID_COLUMN = "column";

  public static PhotoPickerBuilder builder() {
    return new PhotoPickerBuilder();
  }

  public static class PhotoPickerBuilder {
    private Bundle mPickerOptionsBundle;
    private Intent mPickerIntent;

    public PhotoPickerBuilder() {
      mPickerOptionsBundle = new Bundle();
      mPickerIntent = new Intent();
    }

    /**
     * Send the Intent from an Activity with a custom request code
     *
     * @param activity    Activity to receive result
     * @param requestCode requestCode for result
     */
    public void start(@NonNull Activity activity, int requestCode) {
      activity.startActivityForResult(getIntent(activity), requestCode);
    }

    public Intent getIntent(@NonNull Context context) {
      mPickerIntent.setClass(context, SelectImageActivity.class);
      mPickerIntent.putExtras(mPickerOptionsBundle);
      return mPickerIntent;
    }

    /**
     * Send the crop Intent from an Activity
     *
     * @param activity Activity to receive result
     */
    public void start(@NonNull Activity activity) {
      start(activity, REQUEST_CODE);
    }

    public PhotoPickerBuilder setPhotoCount(int photoCount) {
      mPickerOptionsBundle.putInt(EXTRA_MAX_COUNT, photoCount);
      return this;
    }

    public PhotoPickerBuilder setGridColumnCount(int columnCount) {
      mPickerOptionsBundle.putInt(EXTRA_GRID_COLUMN, columnCount);
      return this;
    }
  }
}
