package com.yyp.image.picker.interfaces;

import android.view.View;

/**
 * 图片点击监听接口
 */
public interface OnPhotoClickListener {

  /**
   * 图片点击回调
   * @param v 点击的视图
   * @param position 点击的位置
   * @param showCamera 是否显示拍照
   */
  void onClick(View v, int position, boolean showCamera);
}
