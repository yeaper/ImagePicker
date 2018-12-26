package com.yyp.image.picker.model;

/**
 * 图片类
 */
public class Photo {

  private int id; // 图片id
  private String path; // 图片路径

  public Photo(int id, String path) {
    this.id = id;
    this.path = path;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
