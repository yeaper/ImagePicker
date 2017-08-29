package com.yyp.imagepicker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.yyp.imagepicker.R;
import com.yyp.imagepicker.model.PhotoDirectory;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片文件夹适配器
 */
public class PopupDirectoryListAdapter extends BaseAdapter {

    private List<PhotoDirectory> directories = new ArrayList<>();
    private RequestManager glide;

    public PopupDirectoryListAdapter(RequestManager glide, List<PhotoDirectory> directories) {
        this.directories = directories;
        this.glide = glide;
    }


    @Override
    public int getCount() {
        return directories.size();
    }


    @Override
    public PhotoDirectory getItem(int position) {
        return directories.get(position);
    }


    @Override
    public long getItemId(int position) {
        return directories.get(position).hashCode();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(parent.getContext());
            convertView = mLayoutInflater.inflate(R.layout.picker_item_directory, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.bindData(directories.get(position));

        return convertView;
    }

    private class ViewHolder {

        public ImageView ivCover;
        public TextView tvName;
        public TextView tvCount;

        public ViewHolder(View rootView) {
            ivCover = rootView.findViewById(R.id.iv_dir_cover);
            tvName = rootView.findViewById(R.id.tv_dir_name);
            tvCount = rootView.findViewById(R.id.tv_dir_count);
        }

        /**
         * 绑定图片文件夹数据
         *
         * @param directory
         */
        public void bindData(PhotoDirectory directory) {
            glide.load(directory.getCoverPath())
                    .dontAnimate()
                    .thumbnail(0.1f)
                    .into(ivCover);
            tvName.setText(directory.getName());
            tvCount.setText(tvCount.getContext().getString(R.string.picker_image_count, directory.getPhotos().size()));
        }
    }

}
