package com.yyp.image.picker.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yyp.image.picker.R;
import com.yyp.image.picker.interfaces.OnItemCheckListener;
import com.yyp.image.picker.interfaces.OnPhotoClickListener;
import com.yyp.image.picker.model.Photo;
import com.yyp.image.picker.model.PhotoDirectory;
import com.yyp.image.picker.util.AndroidLifecycleUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片适配器
 */
public class PhotoGridAdapter extends SelectableAdapter<PhotoGridAdapter.PhotoViewHolder> {

    private OnItemCheckListener onItemCheckListener = null;
    private OnPhotoClickListener onPhotoClickListener = null;
    private View.OnClickListener onCameraClickListener = null;

    public final static int ITEM_TYPE_CAMERA = 100;
    public final static int ITEM_TYPE_PHOTO = 101;

    private boolean hasCamera = true; //是否显示拍照项 默认显示
    private boolean previewEnable = true; //是否开启图片预览

    public PhotoGridAdapter(List<PhotoDirectory> photoDirectories) {
        this.photoDirectories = photoDirectories;
        this.selectedPhotos = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        return (showCamera() && position == 0) ? ITEM_TYPE_CAMERA : ITEM_TYPE_PHOTO;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.picker_item_photo, parent, false);
        final PhotoViewHolder holder = new PhotoViewHolder(itemView);
        // 如果是相机，则隐藏选择框
        if (viewType == ITEM_TYPE_CAMERA) {
            holder.vSelected.setVisibility(View.GONE);
            holder.ivPhoto.setScaleType(ImageView.ScaleType.CENTER);

            holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onCameraClickListener != null) {
                        onCameraClickListener.onClick(view);
                    }
                }
            });
        }
        return holder;
    }


    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, int position) {

        if (getItemViewType(position) == ITEM_TYPE_PHOTO) {
            // 已选中的图片集中，获取对应图片
            List<Photo> photos = getCurrentPhotos();
            final Photo photo;
            if (showCamera()) {
                photo = photos.get(position - 1);
            } else {
                photo = photos.get(position);
            }

            // 判断是否可以加载图片
            boolean canLoadImage = AndroidLifecycleUtils.canLoadImage(holder.ivPhoto.getContext());
            if (canLoadImage) {
                Glide.with(holder.itemView.getContext())
                        .load(new File(photo.getPath()))
                        .thumbnail(0.5f)
                        .into(holder.ivPhoto);
            }

            final boolean isChecked = isSelected(photo);
            // 设置为选中状态
            holder.vSelected.setSelected(isChecked);
            holder.ivPhoto.setSelected(isChecked);

            holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onPhotoClickListener != null) {
                        int pos = holder.getAdapterPosition();
                        if (previewEnable) {
                            onPhotoClickListener.onClick(view, pos, showCamera());
                        } else {
                            holder.vSelected.performClick();
                        }
                    }
                }
            });
            holder.vSelected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = holder.getAdapterPosition();
                    boolean isEnable = true;

                    // 根据返回的值，判断是否开关选择框
                    if (onItemCheckListener != null) {
                        isEnable = onItemCheckListener.onItemCheck(pos, photo,
                                getSelectedPhotos().size() + (isSelected(photo) ? -1 : 1));
                    }
                    if (isEnable) {
                        // 开关选择状态
                        toggleSelection(photo);
                        notifyItemChanged(pos);
                    }
                }
            });

        } else {
            holder.ivPhoto.setImageResource(R.drawable.picker_camera);
        }
    }


    @Override
    public int getItemCount() {
        int photosCount = photoDirectories.size() == 0 ? 0 : getCurrentPhotos().size();
        if (showCamera()) {
            return photosCount + 1;
        }
        return photosCount;
    }

    /**
     * 带选择框的 photo
     */
    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;
        private View vSelected;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.iv_photo);
            vSelected = itemView.findViewById(R.id.v_selected);
        }
    }

    /**
     * 图片选择框选中事件
     *
     * @param onItemCheckListener
     */
    public void setOnItemCheckListener(OnItemCheckListener onItemCheckListener) {
        this.onItemCheckListener = onItemCheckListener;
    }

    /**
     * 图片点击事件
     * @param onPhotoClickListener
     */
    public void setOnPhotoClickListener(OnPhotoClickListener onPhotoClickListener) {
        this.onPhotoClickListener = onPhotoClickListener;
    }

    /**
     * 照相机点击事件
     *
     * @param onCameraClickListener
     */
    public void setOnCameraClickListener(View.OnClickListener onCameraClickListener) {
        this.onCameraClickListener = onCameraClickListener;
    }

    /**
     * 获取选中的图片集合
     *
     * @return
     */
    public ArrayList<String> getSelectedPhotoPaths() {
        ArrayList<String> selectedPhotoPaths = new ArrayList<>(getSelectedItemCount());

        selectedPhotoPaths.addAll(selectedPhotos);

        return selectedPhotoPaths;
    }

    /**
     * 设置是否显示拍照项
     *
     * @param hasCamera
     */
    public void setShowCamera(boolean hasCamera) {
        this.hasCamera = hasCamera;
    }

    /**
     * 判断是否显示了照相机
     *
     * @return
     */
    public boolean showCamera() {
        return hasCamera;
    }

}
