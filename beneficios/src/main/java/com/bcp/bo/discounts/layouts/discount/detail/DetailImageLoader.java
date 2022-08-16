package com.bcp.bo.discounts.layouts.discount.detail;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.bcp.bo.discounts.base.BaseLayoutActivity;
import com.bcp.bo.discounts.general.Constants;
import com.bcp.bo.discounts.general.ImageUtils;
import com.bcp.bo.discounts.managers.FileManager;
import com.bcp.bo.discounts.views.CustomImageView;

/**
 * Load asynchronously an image on a image view and set a mask on it.
 * It needs the alpha drawable.
 */
public class DetailImageLoader extends AsyncTask<Drawable, Void, Void> {

	public static final String DEFAULT_MASK = "temp.png.mask";
	public static final String MASK_SUFIX = ".mask";
	
	private Drawable dMixed;
	private DetailImageLoaderInterface layout;
	private Context context;
	private String path;

	/**
	 * Images absolute base path.
	 */
	private StringBuilder imageBasePath;
	
	public DetailImageLoader(String path, BaseLayoutActivity baseActivity, final DetailImageLoaderInterface layout) {
		this.layout = layout;
		this.context = baseActivity;
		this.path = path;
		
		FileManager fileManager = new FileManager();
		
		imageBasePath = new StringBuilder();
		
		if(fileManager.checkExternalStorageState()) {
			imageBasePath.append(fileManager.getExternalStorageBasePath(context))
			.append(Constants.DISCOUNT_IMAGES_PATH).append(File.separator);
		} else {
			imageBasePath.append(fileManager.getInternalStorageBasePath(context))
			.append(Constants.DISCOUNT_IMAGES_PATH).append(File.separator);
		}
	}
	
	@Override
	protected Void doInBackground(Drawable... params) {
		
		Drawable dTop;
		String name;
		boolean exist;
		
		if("".compareTo(path) != 0) {
			
			name = path + MASK_SUFIX;					
			exist = FileManager.hasFile(name);

			if (exist) {
				dMixed = ImageUtils.createDrawableFromPath(context.getResources(), name);
			} else {
				dTop = BitmapDrawable.createFromPath(path);
				//saveMask(dTop, params[0], name);
			}
			
		}
				
		return null;	
	}
	
	private void saveMasks(Drawable dTop, Drawable mask, String name) {
		
		dMixed = null;ImageUtils.generateDrawableMask(context.getResources(), dTop, mask, Color.argb(255, 58, 233, 28));
		Bitmap bMixed = ((BitmapDrawable) dMixed).getBitmap();	
		FileManager.saveBitmap(bMixed, Bitmap.CompressFormat.PNG, name);
		bMixed = null;
//		dMixed = null;
	}

	@Override
	protected void onPostExecute(Void result) {
		layout.setBackgroundDrawableIvInfo(dMixed);
		
		dMixed = null;
		layout = null;
		context = null;
		
		super.onPostExecute(result);
	}
	
	public interface DetailImageLoaderInterface{
		public void setBackgroundDrawableIvInfo(Drawable dMixed);
		public CustomImageView findCustomImageView();
	}
	
}


