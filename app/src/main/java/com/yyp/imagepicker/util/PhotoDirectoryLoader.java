package com.yyp.imagepicker.util;

import android.content.Context;
import android.provider.MediaStore.Images.Media;
import android.support.v4.content.CursorLoader;

import static android.provider.MediaStore.MediaColumns.MIME_TYPE;

public class PhotoDirectoryLoader extends CursorLoader {

    private final String[] IMAGE_PROJECTION = {
            Media._ID,
            Media.DATA,
            Media.BUCKET_ID,
            Media.BUCKET_DISPLAY_NAME,
            Media.DATE_ADDED,
            Media.SIZE
    };

    public PhotoDirectoryLoader(Context context) {
        super(context);

        setProjection(IMAGE_PROJECTION);
        setUri(Media.EXTERNAL_CONTENT_URI);
        setSortOrder(Media.DATE_ADDED + " DESC");

        // 查询语句："mime_type=image/jpeg or mime_type=image/png or mime_type=image/jpg"
        setSelection(
                MIME_TYPE + "=? or " + MIME_TYPE + "=? or " + MIME_TYPE + "=? ");
        String[] selectionArgs;
        selectionArgs = new String[]{"image/jpeg", "image/png", "image/jpg"};
        setSelectionArgs(selectionArgs);
    }
}
