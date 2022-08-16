package com.bcp.bo.discounts.views;

import android.content.res.Resources;
//import android.graphics.AvoidXfermode;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.os.Build;

import com.bcp.bo.discounts.general.ImageUtils;
import com.bcp.bo.discounts.general.L;
import com.squareup.picasso.Transformation;

/**
 * Created by S57973 on 8/2/2016.
 */
public class TicketImageTransform implements Transformation {
    private Resources _resources;
    private Drawable _mask;

    public TicketImageTransform(Resources resources, int drawableId) {
        _resources = resources;
        _mask = _resources.getDrawable(drawableId);
//        _mask = mask;
    }

    private Bitmap Mask(Drawable dOrigin, Drawable dMask, int colorMask) {
        L.d("------------ START IMAGE ---------");

        //check the origin is equal or bigger than alpha
        if (dOrigin.getIntrinsicWidth() < dMask.getIntrinsicWidth() ||
                dOrigin.getIntrinsicHeight() < dMask.getIntrinsicHeight()) {

            dOrigin = ImageUtils.resizeDrawable(dMask.getIntrinsicWidth(), dMask.getIntrinsicHeight(), dOrigin, _resources);
        }

        int width = dMask.getIntrinsicWidth();
        int height = dMask.getIntrinsicHeight();

        //Bitmap to generate
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        if (Build.VERSION.SDK_INT < 14) {
            Canvas canvas = new Canvas(bitmap);
            dOrigin.setBounds(0, 0, width, height);

            //paint image
//            dOrigin.draw(canvas);

            //paint mask
            dMask.setBounds(0, 0, width, height);
            dMask.draw(canvas);

            //apply transparency to mask color
            Paint p = new Paint();
            p.setColor(colorMask);
            int removeColor = p.getColor();
            p.setAlpha(0);

            p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
//            p.setXfermode(new AvoidXfermode(removeColor, 0, AvoidXfermode.Mode.TARGET));
            canvas.drawPaint(p);
            canvas = null;

        } else {

            Canvas canvas = new Canvas(bitmap);

            //paint image
            dOrigin.setBounds(0, 0, width, height);
//            dOrigin.draw(canvas);

            //paint mask
            dMask.setBounds(0, 0, width, height);
            dMask.draw(canvas);

            //apply transparency to mask color
            Paint p = new Paint();
            p.setColor(colorMask);
            int removeColor = p.getColor();
            p.setAlpha(0);
            p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
//            p.setXfermode(new Xfermode(removeColor, 0, Xfermode.Mode.TARGET));
            canvas.drawPaint(p);
            canvas = null;
        }

        return bitmap;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        int width = _mask.getIntrinsicWidth();
        int height = _mask.getIntrinsicHeight();

        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        if (source.getWidth() < _mask.getIntrinsicWidth() ||
                source.getHeight() < _mask.getIntrinsicHeight()) {

            source = Bitmap.createScaledBitmap(source, _mask.getIntrinsicWidth(), _mask.getIntrinsicHeight(), true);
        }

//        Drawable mask = Utils.getMaskDrawable(mContext, mMaskId);

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        canvas.drawBitmap(source, 0, 0, paint);
        _mask.draw(canvas);
        paint.setColor(Color.argb(255, 58, 233, 28));
        int removeColor = paint.getColor();
        paint.setAlpha(0);
        //TODO: Uncomment this line
        //paint.setXfermode(new AvoidXfermode(removeColor, 0, AvoidXfermode.Mode.TARGET));
//      paint.setXfermode(new Xfermode(removeColor, 0, AvoidXfermode.Mode.TARGET));
        //paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
        _mask.setBounds(0, 0, width, height);
        _mask.draw(canvas);
        canvas.drawPaint(paint);

        source.recycle();

        return result;
//        return source;
//        return this.Mask(new BitmapDrawable(source),_mask, Color.argb(255, 58, 233, 28));
    }

    @Override
    public String key() {
        return "circle";
    }
}