package com.yyp.imagepicker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yyp.imagepicker.util.PhotoPicker;

public class MainActivity extends Activity {

     TextView select_image_nine;
    TextView select_image_one;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }

    public void initView(){
        select_image_nine = findViewById(R.id.select_image_nine);
        select_image_one = findViewById(R.id.select_image_one);

        select_image_nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoPicker.builder()
                        .setPhotoCount(9)
                        .setGridColumnCount(3)
                        .start(MainActivity.this);
            }
        });
        select_image_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setGridColumnCount(3)
                        .start(MainActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            // 拿到选取的图片集合
        }
    }
}
