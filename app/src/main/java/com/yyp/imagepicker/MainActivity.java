package com.yyp.imagepicker;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yyp.image.picker.util.PhotoPicker;

import java.util.List;


/**
 * 主界面
 */
public class MainActivity extends Activity {

    TextView select_image_nine;
    TextView select_image_one;

    private static final int REQUEST_CODE_SELECT_PHOTO = 0x11;

    //需要的权限
    private final String[] permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPermissions();

        initView();

    }

    /**
     * 动态请求权限
     */
    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String p : permissions) {
                if (checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(permissions, 100);
                }
            }
        }
    }

    public void initView(){
        select_image_nine = findViewById(R.id.select_image_nine);
        select_image_one = findViewById(R.id.select_image_one);

        select_image_nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoPicker.builder()
                        .setPhotoCount(9) //最多选择个数
                        .setGridColumnCount(3) //图片显示列数
                        .start(MainActivity.this, REQUEST_CODE_SELECT_PHOTO);
            }
        });
        select_image_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoPicker.builder()
                        .setPhotoCount(1) //最多选择个数
                        .setGridColumnCount(3)
                        .start(MainActivity.this, REQUEST_CODE_SELECT_PHOTO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE_SELECT_PHOTO){
            // 拿到选取的图片集合
            if(data != null){
                List<String> imageList = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                Toast.makeText(this, imageList.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
