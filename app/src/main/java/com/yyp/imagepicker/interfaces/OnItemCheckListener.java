package com.yyp.imagepicker.interfaces;

import com.yyp.imagepicker.model.Photo;

public interface OnItemCheckListener {

  /***
   * 选中回调
   * @param position 所选图片的位置
   * @param path     所选的图片
   * @param selectedItemCount  已选数量
   * @return enable check
   */
  boolean onItemCheck(int position, Photo path, int selectedItemCount);

}
