package com.bcp.bo.discounts.general;

import android.content.Context;
import android.content.res.Resources;
//import android.graphics.AvoidXfermode;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageView;

import com.bcp.bo.discounts.R;

@SuppressWarnings("deprecation")
public class ImageUtils {
	
	private static final int DRAWABLE_REDUCTION = 1;
	
	//--------------------- PROJECT FUNCTIONS ----------------------
	
	/**
	 * Generate a Poi with the category inside
	 * @param context - Context where to get the resources
	 * @param dCategory - Drawable of the category
	 * @return Bitmap - generated (new instance)
	 */
	public static Bitmap generatePoiBitmapWithCategory(Context context, Drawable dCategory){
		L.d("------------ POIS ---------");
		//margins to apply for mixing images
		final float[] marginsRect = new float[]{0.15f, 0.15f, 0.65f, 0.55f};
		
		//get image poi
		Drawable dPoi = context.getResources().getDrawable(R.drawable.dsc_map_poi);
		
		//get width and height of poi and calculate the rect where to add
		int widthPoi = dPoi.getIntrinsicWidth();
		int heightPoi = dPoi.getIntrinsicHeight();
		Rect rect = new Rect((int)(widthPoi*marginsRect[0]), (int)(heightPoi*marginsRect[1]), 
				(int)(widthPoi*marginsRect[2]), (int)(heightPoi*marginsRect[3]));
		
		//generate the drawable mixing images
		return ImageUtils.generateBitmapMixingImages(context, dPoi, dCategory, rect);
	}
	
	/**
	 * Generate a Poi with the category inside
	 * @param context - Context where to get the resources
	 * @param dCategory - Drawable of the category
	 * @return Drawable - generated (new instance)
	 */
	public static Drawable generatePoiImageWithCategory(Context context, Drawable dCategory){
		
		//get image poi
		Drawable dPoi = context.getResources().getDrawable(R.drawable.dsc_map_poi);
		
		//get width and height of poi and calculate the rect where to add
		int widthPoi = dPoi.getIntrinsicWidth();
		int heightPoi = dPoi.getIntrinsicHeight();
		Rect rect = new Rect((int)(widthPoi*0.1), (int)(heightPoi*0.1), (int)(widthPoi*0.7), (int)(heightPoi*0.6));
		
		//generate the drawable mixing images
		return ImageUtils.generateDrawableMixingImages(context, dPoi, dCategory, rect);
	}
	
	//------------------ GENERAL IMAGE FUNCTIONS -----------------
	/**
	 * Generate a drawable mixing two images
	 * @param context - Context where to get resources pointer
	 * @param dBase - Drawable to show at the bottom
	 * @param dTop - Drawable to show at the top
	 * @param rectAdd - Rect where to add the drawable top
	 * @return Bitmap - generated (new instance)
	 */
	public static Bitmap generateBitmapMixingImages(Context context, Drawable dBase, Drawable dTop, Rect rectAdd){
		
		if(dBase==null || dTop==null || dTop.getIntrinsicHeight()==0 || dTop.getIntrinsicWidth()==0 || 
				dBase.getIntrinsicWidth()==0 || dBase.getIntrinsicHeight()==0){
			return null;
		}
		
		//get scaled width and height
		float aspectImage = dTop.getIntrinsicWidth() / dTop.getIntrinsicHeight(); 
		float aspectSize = rectAdd.width() / rectAdd.height();
				
		//adjust the rect with the scale comparison
		if((aspectImage>1 && aspectSize>1) || (aspectImage<1 && aspectImage>1)){
			
			//take max width
			int height = (rectAdd.width() * dTop.getIntrinsicHeight()) / dTop.getIntrinsicWidth();
			
			//adjust height in rect
			rectAdd.top = rectAdd.centerY()-(height/2);
			rectAdd.bottom = rectAdd.top + height;
			
		}else{
			
			//take max height
			int width = (rectAdd.height() * dTop.getIntrinsicWidth()) / dTop.getIntrinsicHeight();
			
			//adjust width in rect
			rectAdd.left = rectAdd.centerX()-(width/2);
			rectAdd.right = rectAdd.left + width;
			
		}
		
		//now we have the good rect we can generate the drawable
		//get the canvas over the bitmap
		Bitmap bitmap = Bitmap.createBitmap(dBase.getIntrinsicWidth(), dBase.getIntrinsicHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		
		//draw the drawable bottom
		dBase.setBounds(0, 0, dBase.getIntrinsicWidth(), dBase.getIntrinsicHeight());
		dBase.draw(canvas);
		
		//draw the drawable top
		dTop.setBounds(rectAdd);
		dTop.draw(canvas);
		
		canvas = null;
		
		return bitmap;
	}
	
	/**
	 * Generate a drawable mixing two images
	 * @param context - Context where to get resources pointer
	 * @param dBase - Drawable to show at the bottom
	 * @param dTop - Drawable to show at the top
	 * @param rectAdd - Rect where to add the drawable top
	 * @return Drawable - generated (new instance)
	 */
	public static Drawable generateDrawableMixingImages(Context context, Drawable dBase, Drawable dTop, Rect rectAdd){
		
		//get the drawable
		Bitmap bitmap = generateBitmapMixingImages(context, dBase, dTop, rectAdd);
		Drawable dReturn = new BitmapDrawable(context.getResources(), bitmap);
		
		//free bitmap
		bitmap = null;
		Runtime.getRuntime().gc();
		
		//generate the drawable to return
		return dReturn;
		
	}
		
	
	/**
	 * Resize a drawable to the maximun side keeping the aspect
	 * @param d - Drawable to resize
	 * @param maxWidth - int maximun width possible
	 * @param maxHeight - int maximun height possible
	 * @return Drawable - Copy of drawable resized
	 */
	public static Drawable resizeImage(Drawable d, Resources res, int maxWidth, int maxHeight){
		
		//get scaled width and height
		float aspectImage = (float)d.getIntrinsicWidth() / (float)d.getIntrinsicHeight(); 
		float aspectSize = (float)maxWidth / (float)maxHeight;
		
		int width = maxWidth;
		int height = maxHeight;
		
		//check what we have to adjust
		if((aspectImage>1 && aspectSize>1) || (aspectImage<1 && aspectImage>1)){
			
			//take max width
			height = (maxWidth * d.getIntrinsicHeight()) / d.getIntrinsicWidth();
			
		}else{
			
			//take max height
			width = (maxHeight * d.getIntrinsicWidth()) / d.getIntrinsicHeight();
			
		}
		
		//return the resized drawable
		return resizeDrawable(width, height, d, res);
	}
	
	/**
	 * Resize a image with the scale received
	 * @param d - Drawable yo reescale
	 * @param scale - float scale from 0.0 to 1.0
	 * @return Drawable - generated
	 */
	public static Drawable resizeImage(Drawable d, Resources res, float scale){
		
		//get scaled width and height
		int width = (int) (d.getIntrinsicWidth() * scale);
		int height = (int) (d.getIntrinsicHeight() * scale);
		
		//return the drawable generated
		return resizeDrawable(width, height, (BitmapDrawable) d, res);
	}
	
	
	/**
	 * Redimensiona un drawwable
	 * @param newWidth
	 * @param newHeight
	 * @param bdImage
	 * @return Image
	 */
	public static BitmapDrawable resizeDrawable(float newWidth, float newHeight, Drawable dImage, Resources res) {

		Bitmap bitmapOrig = ((BitmapDrawable)dImage).getBitmap();
		float scaleWidth = ((float) newWidth) / bitmapOrig.getWidth();
		float scaleHeight = ((float) newHeight) / bitmapOrig.getHeight();
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrig, 0, 0, bitmapOrig.getWidth(), bitmapOrig.getHeight(), matrix, true);
		BitmapDrawable bitmapDrawableResized = new BitmapDrawable(res, resizedBitmap);
		
		//free the bitmaps
		resizedBitmap = null;
		bitmapOrig = null;
		Runtime.getRuntime().gc();
		
		//return the drawable resized
		return bitmapDrawableResized;
	}
	
	/**
	 * Resize the image received in the path to that resolution cutting parts of it
	 * If it is not enough size keep the aspect ratio
	 * @param Context - context of the application
	 * @param sPath - path of the image
	 * @param width - Width to set
	 * @param height - Height to set
	 * @return Drawable with the image cut or null if error
	 */
	public static Drawable resizeImage(Context context, String sPath, int widthCanvas, int heightCanvas){
		
		//check the path
		if(sPath==null || widthCanvas<=0 || heightCanvas<=0){
			return null;
		}
		
		//get the drawable as a bitmap
		Drawable dOrig = Drawable.createFromPath(sPath);
		
		//check if the drawable is ok
		if(dOrig==null){
			return null;
		}
		
		//get the bitmap to work with the image
		Bitmap bOrig = ((BitmapDrawable)dOrig).getBitmap();
		
		//calculate the width and the height of the image
		int widthImage = dOrig.getMinimumWidth();
		int heightImage = dOrig.getMinimumHeight();
		
		//calculate difference between images
		double difW = (double)widthCanvas / (double) widthImage;
		double difH = (double)heightCanvas / (double) heightImage;
		
		//calculate relation aspect of the image
		double relWImage = (double)heightCanvas / (double) widthCanvas;
		double relHImage = (double)widthCanvas / (double) heightCanvas;
		
		//width and height of the image
		int widthDest = widthImage;
		int heightDest = heightImage;
		
		//check if the image is bigger
		if(difW>difH){
			widthDest = widthImage;
			heightDest = (int) (widthImage * relWImage);
		}else{
			widthDest = (int) (heightImage * relHImage);
			heightDest = heightImage;
		}
		
		//with the width of destiny we calculate the x and y
		int xDest = 0;
		int yDest = 0;
		if(widthDest<widthImage){
			xDest = (widthImage-widthDest)/2;
		}
		if(heightDest<heightImage){
			yDest = (heightImage-heightDest)/2;
		}
		
		//get the canvas over the bitmap
		Bitmap bitmap = Bitmap.createBitmap(widthCanvas, heightCanvas, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		
		//draw the bitmap to cut
		canvas.drawBitmap(bOrig, new Rect(xDest, yDest, xDest+widthDest, yDest+heightDest), new Rect(0,0,widthCanvas,heightCanvas), new Paint(Paint.ANTI_ALIAS_FLAG));
		
		//generate the drawable to return
		return new BitmapDrawable(context.getResources(), bitmap);
	}
	
	/**
	 * Redimensiona un drawable al tamano mas grande por lado
	 * @param ctx
	 * @param d
	 * @return Image
	 */
	public static BitmapDrawable resizeToHighest(Resources res, BitmapDrawable d){
		
		//obtenemos el tamano de la pantalla
		int widthScreen = res.getDisplayMetrics().widthPixels;
		int heightScreen = res.getDisplayMetrics().heightPixels;

		//obtenemos el tamano de la imagen
		int widthImage = d.getIntrinsicWidth();
		int heightImage = d.getIntrinsicHeight();
		
		//comprobamos que tenga un tamano normal
		if(widthImage==0 || heightImage==0){
			return d;
		}
		
		//variables que definen el tamano final
		int width = 0;
		int height = 0;
		
		//actuamos en funcion del lado mas grande
		if(widthImage>heightImage){
			width = Math.min(widthImage, widthScreen - 20);
			height = (width * heightImage) / widthImage;
		}else{
			height = Math.min(heightImage, heightScreen/2);
			width = (height * widthImage) / heightImage;
		}
		
		//redimensionamos
		d = resizeDrawable(width, height, d, res);
		
		return d;
	}
	/**
	 * Resize a image with the scale received
	 * @param d - Drawable yo reescale
	 * @param scale - float scale from 0.0 to 1.0
	 * @return Drawable - generated
	 */
	public static Bitmap cutBitmap(Bitmap bitmap,int width,int height,float scale){
		Bitmap result=Bitmap.createBitmap(width, height, Config.ARGB_8888);
		
		return result;
	}
	
	/**
	 * Generate a new drawable painting the drawable origin with the alpha of dAlpha and its size
	 * @param dOrigin - Drawable origin where to get the image
	 * @param dAlpha - Drawable with alpha to apply
	 * @return Drawable - new instance of drawable
	 */
	public static Drawable generateDrawableAlpha(Resources resources, Drawable dOrigin, Drawable dAlpha){
		
		//check the origin is equal or bigger than alpha
		if(	dOrigin.getIntrinsicWidth()<dAlpha.getIntrinsicWidth() || 
			dOrigin.getIntrinsicHeight()<dAlpha.getIntrinsicHeight()){
			
			dOrigin = resizeDrawable(dAlpha.getIntrinsicWidth(), dAlpha.getIntrinsicHeight(), dOrigin, resources);
		}
		
		//get bitmaps
		Bitmap bOrigin = ((BitmapDrawable) dOrigin).getBitmap();
		Bitmap bAlpha = ((BitmapDrawable) dAlpha).getBitmap();
				
		//Bitmap to generate
		Bitmap bitmap = Bitmap.createBitmap(bAlpha.getWidth(), bAlpha.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		
		//mix color and alpha
		for(int x = (bitmap.getWidth()-bAlpha.getWidth())/2; x<bitmap.getWidth() && x<bAlpha.getWidth(); x++){
	        for(int y = (bitmap.getHeight()-bAlpha.getHeight())/2; y<bitmap.getHeight() && y<bAlpha.getHeight(); y++){
	        	
	        	int a = Color.alpha(bAlpha.getPixel(x, y));
	        	int pixel = bOrigin.getPixel(x, y);
	            int r = Color.red(pixel);
	            int g = Color.green(pixel);
	            int b = Color.blue(pixel);
	        	
	        	paint.setColor( Color.argb(a, r, g, b));
	        	
	        	//mix
	        	canvas.drawPoint(x, y, paint);
	        }
		}
		
		//create the drawable to return
		Drawable dReturn = new BitmapDrawable(resources, bitmap);
		
		//try to free memory
		bOrigin = null;
		bAlpha = null;
		canvas = null;
		paint = null;
		bitmap = null;
		Runtime.getRuntime().gc();
		
		return dReturn;
	}
	
	/**
	 * Generate a new drawable painting the drawable origin with the alpha of dAlpha and its size
	 * @param dOrigin - Drawable origin where to get the image
	 * @param dMask - Drawable with mask to apply
	 * @param colorMask - int color of mask to delete
	 * @return Drawable - new instance of drawable
	 */
	public static Bitmap generateDrawableMask(Resources resources, Drawable dOrigin, Drawable dMask, int colorMask){
		L.d("------------ START IMAGE ---------");
		
		//check the origin is equal or bigger than alpha
		if(	dOrigin.getIntrinsicWidth()<dMask.getIntrinsicWidth() || 
			dOrigin.getIntrinsicHeight()<dMask.getIntrinsicHeight()){
			
			dOrigin = resizeDrawable(dMask.getIntrinsicWidth(), dMask.getIntrinsicHeight(), dOrigin, resources);
		}
		
		int width = dMask.getIntrinsicWidth();
		int height = dMask.getIntrinsicHeight();
		
		//Bitmap to generate
		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		
		if(Build.VERSION.SDK_INT<14){
			Canvas canvas = new Canvas(bitmap);
			
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
			//TODO: uncomment this line
			//p.setXfermode(new AvoidXfermode(removeColor, 0, AvoidXfermode.Mode.TARGET));
			canvas.drawPaint(p);
			canvas = null;
			
		}else{
//			//get bitmaps
//			Bitmap bOrigin = ((BitmapDrawable) dOrigin).getBitmap();
//			Bitmap bMask = ((BitmapDrawable) dMask).getBitmap();
//
//			int[] pixels = new int[width*height];
//
//			//mix color and alpha
//			for(int y=0; y<height; y++){
//				for(int x=0; x<width; x++){
//		        	int pMask = bMask.getPixel(x, y);
//		        	if(pMask==colorMask){
//		        		pixels[(y*width)+x] = Color.TRANSPARENT;
//		        	}else{
//		        		pixels[(y*width)+x] = bOrigin.getPixel(x, y);
//		        	}
//		        }
//			}
//
//			//apply pixels to bitmap
//			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
//
////			bOrigin.recycle();
////			bMask.recycle();
//			pixels = null;
//			bOrigin = null;
//			bMask = null;

			Canvas canvas = new Canvas(bitmap);

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
			//TODO: uncomment this line
			//p.setXfermode(new AvoidXfermode(removeColor, 0, AvoidXfermode.Mode.TARGET));
			canvas.drawPaint(p);
			canvas = null;

		}
		
		return bitmap;
		//create the drawable to return
		/*Drawable dReturn = new BitmapDrawable(resources, bitmap);
		
		//free memory
		bitmap = null;
		Runtime.getRuntime().gc();
		
		L.d("------------ END IMAGE ---------");
		
		return dReturn;*/
	}
	
	/**
	 * Create a drawable reduced
	 * @param sPath - String path of file
	 * @return Drawable - read
	 */
	@SuppressWarnings("unused")
	public static Drawable createDrawableFromPath(Resources res, String sPath){
		
		if(DRAWABLE_REDUCTION>1){
			BitmapFactory.Options bfo = new BitmapFactory.Options();
			bfo.inSampleSize = DRAWABLE_REDUCTION;
			bfo.inScaled = true;
			return new BitmapDrawable(res, BitmapFactory.decodeFile(sPath, bfo));
		}else{
			return Drawable.createFromPath(sPath);
		}
		
		
		
		
	}
}
