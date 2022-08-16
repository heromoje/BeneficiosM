package com.bcp.bo.discounts.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ImageTicket extends ImageView {
	Drawable background;
	public ImageTicket(Context context) {
		super(context);
		background=getDrawable();
		setWillNotDraw(false);
		//background = context.getResources().getDrawable(R.drawable.ic_logo_cyzone);
	    //setBackgroundResource(R.drawable.ic_logo_cyzone);
	    //setBackground(background);
	}
	public ImageTicket(Context context, AttributeSet attrs) {
	    super(context, attrs);
	    background=getDrawable();
	    setWillNotDraw(false);
	    //background = context.getResources().getDrawable(R.drawable.ic_logo_cyzone);
	    //setBackgroundResource(R.drawable.ic_logo_cyzone);
	}
	public ImageTicket(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
	    background=getDrawable();
	    setWillNotDraw(false);
	    //background = context.getResources().getDrawable(R.drawable.ic_logo_cyzone);
	    //setBackgroundResource(R.drawable.ic_logo_cyzone);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
		int ancho=MeasureSpec.getSize(widthMeasureSpec);
	    setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
  	}
	@Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*Bitmap original=BitmapFactory.decodeResource(getResources(),R.drawable.dsc_detail_img);
        Bitmap mask = BitmapFactory.decodeResource(getResources(),R.drawable.dsc_detail_ticket_top_mask);
        
        
        if(	original.getWidth()<mask.getWidth() || 
        		original.getHeight()<mask.getHeight()){
        	original = ImageUtils.resizeDrawable(mask.getWidth(), mask.getHeight(), original, getResources());
		}
		
		int width = mask.getIntrinsicWidth();
		int height = mask.getIntrinsicHeight();
		
		//Bitmap to generate
		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);

        //You can change original image here and draw anything you want to be masked on it.

        Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Config.ARGB_8888);
        Canvas tempCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        tempCanvas.drawBitmap(original, 0, 0, null);
        tempCanvas.drawBitmap(mask, 0, 0, paint);
        paint.setXfermode(null);*/

        //Draw result after performing masking
        /*L.d("------------ START IMAGE ---------");
		
		//check the origin is equal or bigger than alpha
        int colorMask=Color.argb(255, 58, 233, 28);
        Drawable dOrigin=getResources().getDrawable(R.drawable.dsc_detail_img);
        Drawable dMask=getResources().getDrawable(R.drawable.dsc_detail_ticket_top_mask);
		if(	dOrigin.getIntrinsicWidth()<dMask.getIntrinsicWidth() || 
			dOrigin.getIntrinsicHeight()<dMask.getIntrinsicHeight()){
			
			dOrigin = ImageUtils.resizeDrawable(dMask.getIntrinsicWidth(), dMask.getIntrinsicHeight(), dOrigin, getResources());
		}
		
		int width = dMask.getIntrinsicWidth();
		int height = dMask.getIntrinsicHeight();
		
		//Bitmap to generate
		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		
		if(Build.VERSION.SDK_INT<14){
			canvas = new Canvas(bitmap);
			
			//paint image
			dOrigin.setBounds(0, 0, width, height);
			dOrigin.draw(canvas);
			
			//paint mask
			dMask.setBounds(0, 0, width, height);
			dMask.draw(canvas);
			
			//apply transparency to mask color
			Paint p = new Paint();
			p.setColor(colorMask);
			int removeColor = p.getColor();
			p.setAlpha(0);
			p.setXfermode(new AvoidXfermode(removeColor, 0, AvoidXfermode.Mode.TARGET));
			canvas.drawPaint(p);
			canvas = null;
			
		}else{
		
			//get bitmaps
			Bitmap bOrigin = ((BitmapDrawable) dOrigin).getBitmap();
			Bitmap bMask = ((BitmapDrawable) dMask).getBitmap();
					
			int[] pixels = new int[width*height];
			
			//mix color and alpha
			for(int y=0; y<height; y++){
				for(int x=0; x<width; x++){
		        	int pMask = bMask.getPixel(x, y);
		        	if(pMask==colorMask){
		        		pixels[(y*width)+x] = Color.TRANSPARENT;
		        	}else{
		        		pixels[(y*width)+x] = bOrigin.getPixel(x, y);
		        	}
		        }
			}
			
			//apply pixels to bitmap
			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
			
//			bOrigin.recycle();
//			bMask.recycle();
			pixels = null;
			bOrigin = null;
			bMask = null;
		}*/
        //canvas.drawBitmap(result, 0, 0, new Paint());
        //canvas.drawOval(rect, mPaint);
    }
}