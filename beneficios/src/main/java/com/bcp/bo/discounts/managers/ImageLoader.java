package com.bcp.bo.discounts.managers;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.bcp.bo.discounts.general.ImageUtils;
import com.bcp.bo.discounts.views.CustomImageView;

public class ImageLoader {

	public static void loadImage(final CustomImageView civ, final String sPath, final Integer tag, final ImageView ivHide){
		
		if(civ==null || sPath==null){
			return;
		}
		
		new Thread(){ public void run(){
			
			//get drawable
			final Drawable drawable = ImageUtils.createDrawableFromPath(civ.getResources(), sPath);
			
			if(civ!=null && civ.getTag().equals(tag)){
				civ.post(new Runnable(){ public void run(){
					civ.setDrawable(drawable);
					civ.setVisibility(View.VISIBLE);
					if(ivHide!=null){
						ivHide.setVisibility(View.INVISIBLE);
					}				
				}});
			}
			
		}}.start();
		
	}
	
}
