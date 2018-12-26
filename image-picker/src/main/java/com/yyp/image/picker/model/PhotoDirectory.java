package com.yyp.image.picker.model;


import com.yyp.image.picker.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片文件夹类
 */
public class PhotoDirectory {

    private String id; // 文件夹ID
    private String coverPath; // 封面图路径
    private String name; // 文件夹名字
    private long dateAdded; // 日期
    private List<Photo> photos = new ArrayList<>(); // 图片集合

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    /**
     * 清除空文件
     *
     * @param photos
     */
    public void setPhotos(List<Photo> photos) {
        if (photos == null) return;
        for (int i = 0, j = 0, num = photos.size(); i < num; i++) {
            Photo p = photos.get(j);
            if (p == null || !FileUtils.fileIsExists(p.getPath())) {
                photos.remove(j);
            } else {
                j++;
            }
        }
        this.photos = photos;
    }

    /**
     * 获取图片集合的所有路径
     * @return
     */
    public List<String> getPhotoPaths() {
        List<String> paths = new ArrayList<>(photos.size());
        for (Photo photo : photos) {
            paths.add(photo.getPath());
        }
        return paths;
    }

    /**
     * 为集合添加图片
     * @param id
     * @param path
     */
    public void addPhoto(int id, String path) {
        if (FileUtils.fileIsExists(path)) {
            photos.add(new Photo(id, path));
        }
    }

}
