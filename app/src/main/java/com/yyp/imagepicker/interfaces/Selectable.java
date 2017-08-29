package com.yyp.imagepicker.interfaces;

import com.yyp.imagepicker.model.Photo;

public interface Selectable {

  /**
   * 判断照片是否选中
   *
   * @param photo 选中的图片
   * @return true 或 false
   */
  boolean isSelected(Photo photo);

  /**
   * 开关图片的选中状态
   *
   * @param photo
   */
  void toggleSelection(Photo photo);

  /**
   * 获取选中的图片数量
   */
  int getSelectedItemCount();

}
