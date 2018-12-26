# ImagePicker


ImagePicker是一个仿微信的图片选择器，支持自定义选择图片个数，图片展示列数，底部可切换显示对应的图片文件夹


运行效果如下图：

<img src="http://bmob-cdn-20165.b0.upaiyun.com/2018/12/26/490af765407f866e80759bbdbb92c616.png" width = "270" height = "560"/>

1. 添加依赖

- gradle配置

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}


dependencies {
	  implementation 'com.github.yeaper:ImagePicker:Tag'
}
```

- Maven配置

```
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>


<dependency>
    <groupId>com.github.yeaper</groupId>
    <artifactId>ImagePicker</artifactId>
    <version>Tag</version
</dependency>

```

2. 使用

点击时，启动图片选择页面

```java
PhotoPicker.builder()
      .setPhotoCount(9) //最多选择个数
      .setGridColumnCount(3) //图片显示列数
      .start(MainActivity.this, REQUEST_CODE_SELECT_PHOTO);
```

然后，在 onActivityResult 中接收选择的图片路径集合

```java
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
```

后面考虑加入更多的可配置化选项，比如标题、界面style、按钮文字等等
