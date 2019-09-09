package com.yyp.image.picker.util;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.yyp.image.picker.R;
import com.yyp.image.picker.bean.PhotoDirectory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.provider.MediaStore.Audio.Playlists.Members._ID;
import static android.provider.MediaStore.Audio.PlaylistsColumns.DATE_ADDED;
import static android.provider.MediaStore.MediaColumns.SIZE;
import static android.provider.MediaStore.Video.Thumbnails.DATA;
import static android.provider.MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME;
import static android.provider.MediaStore.Video.VideoColumns.BUCKET_ID;

/**
 * 多媒体储存助手，用于加载本地图片
 */
public class MediaStoreHelper {

    public final static int INDEX_ALL_PHOTOS = 0;

    /**
     * 获取所有的图片文件夹
     *
     * @param activity       上下文
     * @param args
     * @param resultCallback 结果回调
     */
    public static void getPhotoDirs(FragmentActivity activity, Bundle args, PhotosResultCallback resultCallback) {
        LoaderManager.getInstance(activity)
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
         *
         * @param id
         * @param args
         * @return
         */
        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(
                    context,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{
                            MediaStore.Images.Media._ID,
                            MediaStore.Images.Media.DATA,
                            MediaStore.Images.Media.BUCKET_ID,
                            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                            MediaStore.Images.Media.DATE_ADDED,
                            MediaStore.Images.Media.SIZE},
                    " mime_type=? or mime_type=? or mime_type=? ",
                    new String[]{"image/jpeg", "image/png", "image/jpg"},
                    MediaStore.Images.Media.DATE_ADDED + " DESC"
            );
        }

        /**
         * 加载完成后，把数据放入集合
         *
         * @param loader
         * @param data
         */
        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
            if (data == null) return;

            Map<String, PhotoDirectory> directories = new HashMap<>(); // 文件夹集合
            // 所有图片的文件夹
            PhotoDirectory photoDirectoryAll = new PhotoDirectory();
            photoDirectoryAll.setName(context.getString(R.string.all_images));
            photoDirectoryAll.setId("ALL");
            directories.put("ALL", photoDirectoryAll);

            // 遍历数据
            if (data.getCount() > 0) {
                data.moveToFirst(); //必须移动到第一个，每次重新进入界面，都会走这里
                do {
                    // 单个图片信息
                    int imageId = data.getInt(data.getColumnIndexOrThrow(_ID));
                    String bucketId = data.getString(data.getColumnIndexOrThrow(BUCKET_ID));
                    String name = data.getString(data.getColumnIndexOrThrow(BUCKET_DISPLAY_NAME));
                    String path = data.getString(data.getColumnIndexOrThrow(DATA));
                    long dateAdded = data.getLong(data.getColumnIndexOrThrow(DATE_ADDED));
                    long size = data.getInt(data.getColumnIndexOrThrow(SIZE));

                    if (size < 1) continue;

                    // 将图片添加到对应的文件夹中
                    if (!directories.containsKey(bucketId)) { //扫描到新的文件夹需要重新创建
                        PhotoDirectory photoDirectory = new PhotoDirectory();
                        photoDirectory.setId(bucketId);
                        photoDirectory.setName(name);
                        photoDirectory.setCoverPath(path);
                        photoDirectory.setDateAdded(dateAdded);
                        photoDirectory.addPhoto(imageId, path); //文件夹中添加图片
                        directories.put(bucketId, photoDirectory);
                    } else {
                        PhotoDirectory photoDirectory = directories.get(bucketId);
                        if (photoDirectory != null) {
                            photoDirectory.addPhoto(imageId, path); //将同一文件夹下的图片放在一起
                        }
                    }
                    // 所有图片都添加到"所有图片"这个集合中
                    photoDirectoryAll.addPhoto(imageId, path);
                } while (data.moveToNext());


                if (photoDirectoryAll.getPhotoPaths().size() > 0) {
                    // 设置该文件夹的显示图为图片集合的第一张
                    photoDirectoryAll.setCoverPath(photoDirectoryAll.getPhotoPaths().get(0));
                }

                if (resultCallback != null) { //将Map集合转为List集合回调出去
                    List<PhotoDirectory> photoDirectoryList = new ArrayList<>();
                    for (String key : directories.keySet()) {
                        photoDirectoryList.add(directories.get(key));
                    }
                    resultCallback.onResultCallback(photoDirectoryList);
                }
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        }
    }

    /**
     * 扫描到的所有图片回调
     */
    public interface PhotosResultCallback {
        /**
         * 扫描结果回调
         *
         * @param directories 图片文件夹
         */
        void onResultCallback(List<PhotoDirectory> directories);
    }

}