package com.yyp.image.picker.bean;

import java.io.Serializable;

/**
 * 图片类
 */
public class Photo implements Serializable {

  private static final long serialVersionUID = 5609493768405518179L;

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

    @Override
    public int hashCode() {
        return getId();
    }
}
