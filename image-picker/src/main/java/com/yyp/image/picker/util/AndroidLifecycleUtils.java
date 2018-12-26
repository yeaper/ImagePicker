package com.yyp.image.picker.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * 安卓生命周期工具
 */
public class AndroidLifecycleUtils {

    /**
     * 判断是否加载图片
     *
     * @param fragment fragment
     * @return
     */
    public static boolean canLoadImage(Fragment fragment) {
        if (fragment == null) {
            return true;
        }

        FragmentActivity activity = fragment.getActivity();

        return canLoadImage(activity);
    }

    /**
     * 判断是否加载图片
     *
     * @param context 上下文
     * @return
     */
    public static boolean canLoadImage(Context context) {
        if (context == null) {
            return true;
        }

        if (!(context instanceof Activity)) {
            return true;
        }

        Activity activity = (Activity) context;
        return canLoadImage(activity);
    }

    /**
     * 判断是否可以加载图片
     *
     * @param activity Activity
     * @return
     */
    public static boolean canLoadImage(Activity activity) {
        if (activity == null) {
            return true;
        }

        boolean destroyed = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 &&
                activity.isDestroyed();

        return !(destroyed || activity.isFinishing());

    }
}
