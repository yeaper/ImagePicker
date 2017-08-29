/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.yyp.imagepicker.interfaces;

import android.view.View;

public interface OnPhotoClickListener {

  /**
   * 图片点击回调
   * @param v 点击的视图
   * @param position 点击的位置
   * @param showCamera 是否显示拍照
   */
  void onClick(View v, int position, boolean showCamera);
}
