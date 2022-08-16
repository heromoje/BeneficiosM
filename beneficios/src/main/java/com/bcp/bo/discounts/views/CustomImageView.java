package com.bcp.bo.discounts.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class CustomImageView extends ImageView {

//	private Drawable drawable = null;

	private Rect rect = new Rect();
	
	public CustomImageView(Context context, AttributeSet attrs) {
		super(context, attrs);

		
	}
	
	public void setDrawable(Drawable d){
		freeMem();
//		this.drawable = d;
//		this.invalidate();
	}
	
//	public Drawable getDrawable(){
//		return drawable;
//	}
	
	@Override
	protected void onLayout (boolean changed, int left, int top, int right, int bottom){
		super.onLayout(changed, left, top, right, bottom);
	
		if(changed){
			rect.right = right-left;
			rect.bottom = bottom-top;
		}
	}
	
	@Override
	protected void onDraw (Canvas canvas){
		super.onDraw(canvas);
		
//		if(drawable!=null){
//			drawable.setBounds(rect);
//			drawable.draw(canvas);
//		}
	}
	
	public void freeMem(){
//		if(drawable!=null){
//			drawable.setCallback(null);
//			drawable = null;
//			System.gc();
//		}
	}

}
