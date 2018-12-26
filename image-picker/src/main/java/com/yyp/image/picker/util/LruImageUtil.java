package com.yyp.image.picker.util;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * 单例类 图片缓存
 *
 * 使用静态内部类
 * Created by yyp on 2017/8/29.
 */
public class LruImageUtil {

    private int cacheSize = (int) Runtime.getRuntime().maxMemory() / (1024 * 8); // 拿出1/8的缓存容量，单位K

    private LruImageUtil(){

    }

    public static LruImageUtil getInstance(){
        return LruImageSingleton.instance;
    }

    /**
     * 静态内部类
     */
    private static class LruImageSingleton{
        private static final LruImageUtil instance = new LruImageUtil();
    }

    /**
     * 配置要使用的缓存大小
     */
    private LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(cacheSize){
        @Override
        protected int sizeOf(String key, Bitmap value) {
            // 计算一张图片的大小
            return value.getByteCount() / 1024;
        }
    };

    /**
     * 缓存图片
     *
     * @param key key值
     * @param bitmap 缓存的图片
     */
    public void putBitmap(String key, Bitmap bitmap){
        cache.put(key, bitmap);
    }

    /**
     * 获取缓存的图片
     *
     * @param key key值
     * @return 需要的图片
     */
    public Bitmap getBitmap(String key){
        return cache.get(key);
    }
}
