package com.yyp.image.picker.adapter;

import android.support.v7.widget.RecyclerView;

import com.yyp.image.picker.interfaces.Selectable;
import com.yyp.image.picker.bean.Photo;
import com.yyp.image.picker.bean.PhotoDirectory;

import java.util.ArrayList;
import java.util.List;

/**
 * 参考 photoPicker
 * @param <VH>
 */
public abstract class SelectableAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> implements Selectable {

    // 图片文件夹集合
    protected List<PhotoDirectory> photoDirectories;
    // 已选中图片集合
    protected List<Photo> selectedPhotos;

    private int currentDirectoryIndex = 0;

    public SelectableAdapter() {
        photoDirectories = new ArrayList<>();
        selectedPhotos = new ArrayList<>();
    }

    @Override
    public boolean isSelected(Photo photo) {
        return getSelectedPhotos().contains(photo);
    }

    @Override
    public void toggleSelection(Photo photo) {
        if (selectedPhotos.contains(photo)) {
            selectedPhotos.remove(photo);
        } else {
            selectedPhotos.add(photo);
        }
    }

    /**
     * Count the selected items
     *
     * @return Selected items count
     */
    @Override
    public int getSelectedItemCount() {
        return selectedPhotos.size();
    }

    public void setCurrentDirectoryIndex(int currentDirectoryIndex) {
        this.currentDirectoryIndex = currentDirectoryIndex;
    }

    /**
     * 获取当前文件夹下的图片集合
     *
     * @return
     */
    public List<Photo> getCurrentPhotos() {
        return photoDirectories.get(currentDirectoryIndex).getPhotos();
    }

    /**
     * 获取当前图片集合的图片路经集合
     *
     * @return
     */
    public List<String> getCurrentPhotoPaths() {
        List<String> currentPhotoPaths = new ArrayList<>(getCurrentPhotos().size());
        for (Photo photo : getCurrentPhotos()) {
            currentPhotoPaths.add(photo.getPath());
        }
        return currentPhotoPaths;
    }

    /**
     * 获取选中的图片集合
     *
     * @return
     */
    public List<Photo> getSelectedPhotos() {
        return selectedPhotos;
    }

}