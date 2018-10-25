# multiAnimationImageView

项目要求做个登陆背景渐变的效果。
开始用属性动画来做的。后来发现大才小用了
使用两个imageview组件组成的一个viewgroup自定义组件，使用帧动画就可以解决。  
####使用方式：  
```
<com.minxing.client.widget.MultiImageGradientsLayout
android:id="@+id/multiImageGradients"
android:layout_width="match_parent"
android:layout_height="match_parent" />

```  
然后在展示的activity/fragment页面调用  
```
ArrayList<Bitmap> bitmaps = new ArrayList<>();
for (int i = 0; i < drawAbles.length; i++) {
Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawAbles[i]);
bitmaps.add(bitmap);
}
multiImageGradients.setBitmaps(bitmaps);
multiImageGradients.startAnim();
```
