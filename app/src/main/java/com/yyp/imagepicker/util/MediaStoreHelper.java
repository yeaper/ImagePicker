package com.yyp.imagepicker.util;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.yyp.imagepicker.R;
import com.yyp.imagepicker.model.PhotoDirectory;

import java.util.ArrayList;
import java.util.List;

import static android.provider.MediaStore.Audio.Playlists.Members._ID;
import static android.provider.MediaStore.Audio.PlaylistsColumns.DATE_ADDED;
import static android.provider.MediaStore.MediaColumns.SIZE;
import static android.provider.MediaStore.Video.Thumbnails.DATA;
import static android.provider.MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME;
import static android.provider.MediaStore.Video.VideoColumns.BUCKET_ID;

/**
 * 加载图片
 */
public class MediaStoreHelper {

    public final static int INDEX_ALL_PHOTOS = 0;


    public static void getPhotoDirs(FragmentActivity activity, Bundle args, PhotosResultCallback resultCallback) {
        activity.getSupportLoaderManager()
                .initLoader(0, args, new PhotoDirLoaderCallbacks(activity, resultCallback));
    }

    private static class PhotoDirLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

        private Context context;
        private PhotosResultCallback resultCallback;

        public PhotoDirLoaderCallbacks(Context context, PhotosResultCallback resultCallback) {
            this.context = context;
            this.resultCallback = resultCallback;
        }

        /**
         * 创建一个图片文件加载器
         * @param id
         * @param args
         * @return
         */
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new PhotoDirectoryLoader(context);
        }

        /**
         * 加载完成后，把数据放入集合
         * @param loader
         * @param data
         */
        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            if (data == null) return;
            List<PhotoDirectory> directories = new ArrayList<>(); // 文件夹集合
            PhotoDirectory photoDirectoryAll = new PhotoDirectory(); // 所有图片集合
            photoDirectoryAll.setName(context.getString(R.string.all_images));
            photoDirectoryAll.setId("ALL");
            // 遍历数据
            while (data.moveToNext()) {
                // 单个图片信息
                int imageId = data.getInt(data.getColumnIndexOrThrow(_ID));
                String bucketId = data.getString(data.getColumnIndexOrThrow(BUCKET_ID));
                String name = data.getString(data.getColumnIndexOrThrow(BUCKET_DISPLAY_NAME));
                String path = data.getString(data.getColumnIndexOrThrow(DATA));
                long size = data.getInt(data.getColumnIndexOrThrow(SIZE));

                if (size < 1) continue;
                // 新建一个文件夹类
                PhotoDirectory photoDirectory = new PhotoDirectory();
                photoDirectory.setId(bucketId);
                photoDirectory.setName(name);
                // 将图片添加到对应的文件夹中
                if (!directories.contains(photoDirectory)) {
                    photoDirectory.setCoverPath(path);
                    photoDirectory.addPhoto(imageId, path);
                    photoDirectory.setDateAdded(data.getLong(data.getColumnIndexOrThrow(DATE_ADDED)));
                    directories.add(photoDirectory);
                } else {
                    directories.get(directories.indexOf(photoDirectory)).addPhoto(imageId, path);
                }
                // 所有图片都添加到 所有图片这个集合中
                photoDirectoryAll.addPhoto(imageId, path);
            }
            if (photoDirectoryAll.getPhotoPaths().size() > 0) {
                // 设置该文件夹的显示图为图片集合的第一张
                photoDirectoryAll.setCoverPath(photoDirectoryAll.getPhotoPaths().get(0));
            }
            directories.add(INDEX_ALL_PHOTOS, photoDirectoryAll);
            if (resultCallback != null) {
                resultCallback.onResultCallback(directories);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }

    public interface PhotosResultCallback {
        void onResultCallback(List<PhotoDirectory> directories);
    }

}