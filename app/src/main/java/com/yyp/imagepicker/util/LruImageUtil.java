package com.yyp.imagepicker.util;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * 单例类 图片缓存
 * Created by yyp on 2017/8/29.
 */
public class LruImageUtil {

    private static LruImageUtil instance;

    private int cacheSize = (int) Runtime.getRuntime().maxMemory() / (1024 * 8); // 拿出1/8的缓存容量，单位K

    private LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(cacheSize){
        @Override
        protected int sizeOf(String key, Bitmap value) {
            // 计算一张图片的大小
            return value.getByteCount() / 1024;
        }
    };

    private LruImageUtil(){

    }

    public static LruImageUtil getInstance(){
        if(instance == null){
            instance = new LruImageUtil();
        }
        return instance;
    }

    public void putBitmap(String key, Bitmap bitmap){
        cache.put(key, bitmap);
    }

    public Bitmap getBitmap(String key){
        return cache.get(key);
    }
}
