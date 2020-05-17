package zd.cn.novipvideo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Icon;
import android.transition.Transition;
import android.widget.ImageView;



import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import java.security.MessageDigest;

import zd.cn.novipvideo.NoVipApplication;
import zd.cn.novipvideo.R;

public class GlideUtils {

    private static Context context = NoVipApplication.getApplicationContex();


    public static void GlideLoadImg(String url,ImageView imageView){
        Glide.with(context).load(url).asBitmap().into(imageView);
    }

//    public static void GlieDefaultImg(String url,ImageView imageView){
//        RequestOptions options = new RequestOptions();
//        options.placeholder(R.drawable.default_img).error(R.drawable.default_img);
//        options.centerCrop();
//        if(!url.isEmpty()){
//            Glide.with(context).asBitmap().apply(options).load(url).into(imageView);
//        }else {
//            Glide.with(context).asBitmap().load(R.drawable.default_img).into(imageView);
//        }
//    }
//
//
//    public static void GlideRaudis(String url, ImageView imageView){
//              Glide.with(context).asBitmap().load(url).apply(RequestOptions.bitmapTransform(getMation())).into(imageView);
//    }
//
//    //加在圆形图片
//    public static void GlideCircle(String url,ImageView imageView){
//        Glide.with(context).asBitmap().load(url).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(imageView);
//    }
//
//
//    public static void GlideRaudisAll(String url, ImageView imageView){
//        Glide.with(context).asBitmap().load(url).apply(RequestOptions.bitmapTransform(getMationAll())).into(imageView);
//    }
//
//    public static MultiTransformation<Bitmap> getMationAll(){
//        RoundedCornersTransformation transformation = new RoundedCornersTransformation(25,0, RoundedCornersTransformation.CornerType.ALL);
//        MultiTransformation<Bitmap> mation = new MultiTransformation<>(new CenterCrop(),transformation);
//        return mation;
//    }
//
//    public static MultiTransformation<Bitmap> getMation(){
//        RoundedCornersTransformation transformation = new RoundedCornersTransformation(25,0, RoundedCornersTransformation.CornerType.TOP_LEFT);
//        RoundedCornersTransformation transformation1 = new RoundedCornersTransformation(25,0, RoundedCornersTransformation.CornerType.TOP_RIGHT);
//
//        MultiTransformation<Bitmap> mation = new MultiTransformation<>(transformation,transformation1);
//        return mation;
//    }

}
