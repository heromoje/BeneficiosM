package com.bcp.bo.discounts.animations;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout.LayoutParams;

public class Flip3dLayouts {
	
	public static final int DIRECTION_NEXT = 1;
	public static final int DIRECTION_BACK = -1;
	
	private static final int ANIMATION_DURATION = 500;
	private static final float ANIMATION_3D_DEPTH = 0.0f;

	private ViewGroup vParent;
	
	private static final int STATE_END = -1;
	private static final int STATE_START = 0;
	private static final int STATE_TO_OPEN_MIDDLE = 2;
	private static final int STATE_TO_CLOSE_MIDDLE = 3;
	
	private int state = STATE_START;
	
	private ImageView ivTopToShow = null;
	private ImageView ivBottomToShow = null;
	private ImageView ivTopToHide = null;
	private ImageView ivBottomToHide = null;
	
	private static final int IMAGEVIEW_TOP_TO_SHOW_ID = 101;
	private static final int IMAGEVIEW_BOTTOM_TO_SHOW_ID = 102;
	private static final int IMAGEVIEW_TOP_TO_HIDE_ID = 103;
	private static final int IMAGEVIEW_BOTTOM_TO_HIDE_ID = 104;
	
	private int viewWidth = 0;
	private int viewHeight = 0;
	
	/** RelativeLayout with all the images to do the animations **/
	private RelativeLayout rlImages = null;
	
	//views animated
	private View vFrom = null;
	private View vTo = null;
	
	//interface to call at end of animation
	private Flip3dLayoutsListener flip3dLayoutsListener = null;
	
	
	public Flip3dLayouts(ViewGroup vParent){
		this.vParent = vParent;
		
	}
	
	private Drawable[] generateDrawable(View view){
		
		//get top drawable
		Bitmap bTop = Bitmap.createBitmap(view.getWidth(), view.getHeight()/2, Config.ARGB_8888); 
		Canvas cTop = new Canvas(bTop);
		view.draw(cTop);
		Drawable dTop = new BitmapDrawable(vParent.getResources(), bTop);
		
		//get bottom drawable
		Bitmap bBottom = Bitmap.createBitmap(view.getWidth(), view.getHeight()/2, Config.ARGB_8888); 
		Canvas cBottom = new Canvas(bBottom);
		cBottom.translate(0, -view.getHeight()/2);
		view.draw(cBottom);
		Drawable dBottom = new BitmapDrawable(vParent.getResources(), bBottom);
		
		//return drawables
		return new Drawable[]{dTop, dBottom};
	}
	
	/**
	 * End the animation setting end state and removing all views created for animations
	 */
	private void endAnimation(){
		
		//remove the images
		vParent.removeView(rlImages);
		rlImages = null;
		
		//set visibility views
		vFrom.setVisibility(View.INVISIBLE);
		vTo.setVisibility(View.VISIBLE);
		
		//free views
		vFrom = null;
		vTo = null;
		
		//call to listener
		if(flip3dLayoutsListener!=null){
			flip3dLayoutsListener.onEndFlipAnimation();
			flip3dLayoutsListener = null;
		}
	}
	
	/**
	 * Generate the images to play the animations
	 * @param vFrom - View to hide
	 * @param vTo - View to show
	 */
	private void generateImages(View vFrom, View vTo){
		
		Drawable[] dsTo = generateDrawable(vTo);
		Drawable[] dsFrom = generateDrawable(vFrom);
		
		rlImages = new RelativeLayout(vParent.getContext());
		
		//image views to show
		ivTopToShow = new ImageView(vParent.getContext());
		ivTopToShow.setId(IMAGEVIEW_TOP_TO_SHOW_ID);
		ivTopToShow.setImageDrawable(dsTo[0]);
		rlImages.addView(ivTopToShow);
		RelativeLayout.LayoutParams rlpTopToShow = (RelativeLayout.LayoutParams) ivTopToShow.getLayoutParams();
		rlpTopToShow.addRule(RelativeLayout.CENTER_HORIZONTAL);
		
		ivBottomToShow = new ImageView(vParent.getContext());
		ivBottomToShow.setId(IMAGEVIEW_BOTTOM_TO_SHOW_ID);
		ivBottomToShow.setImageDrawable(dsTo[1]);
		rlImages.addView(ivBottomToShow);
		RelativeLayout.LayoutParams rlpBottomToShow = (RelativeLayout.LayoutParams) ivBottomToShow.getLayoutParams();
		rlpBottomToShow.addRule(RelativeLayout.BELOW, IMAGEVIEW_TOP_TO_SHOW_ID);
		rlpBottomToShow.addRule(RelativeLayout.CENTER_HORIZONTAL);
		
		//image views to hide
		ivTopToHide = new ImageView(vParent.getContext());
		ivTopToHide.setId(IMAGEVIEW_TOP_TO_HIDE_ID);
		ivTopToHide.setImageDrawable(dsFrom[0]);
		rlImages.addView(ivTopToHide);
		RelativeLayout.LayoutParams rlpTopToHide = (RelativeLayout.LayoutParams) ivTopToHide.getLayoutParams();
		rlpTopToHide.addRule(RelativeLayout.CENTER_HORIZONTAL);
		
		ivBottomToHide = new ImageView(vParent.getContext());
		ivBottomToHide.setId(IMAGEVIEW_BOTTOM_TO_HIDE_ID);
		ivBottomToHide.setImageDrawable(dsFrom[1]);
		rlImages.addView(ivBottomToHide);
		RelativeLayout.LayoutParams rlpBottomToHide = (RelativeLayout.LayoutParams) ivBottomToHide.getLayoutParams();
		rlpBottomToHide.addRule(RelativeLayout.BELOW, IMAGEVIEW_TOP_TO_HIDE_ID);
		rlpBottomToHide.addRule(RelativeLayout.CENTER_HORIZONTAL);
		
		vParent.addView(rlImages);
		RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams)rlImages.getLayoutParams();
		rlp.width = LayoutParams.MATCH_PARENT;
		rlp.height = LayoutParams.WRAP_CONTENT;
		rlp.addRule(RelativeLayout.CENTER_VERTICAL);
	}
	
	/**
	 * Make a flip from child indexFrom to child indexTo of parent received in constructor
	 * @param indexFrom - int index of child to hide
	 * @param indexTo - int index of chilt to show
	 * @param flip3dLayoutsListener - Flip3dLayoutsListener listener to receive end event
	 */
	public void flip(int indexFrom, int indexTo, int direction, Flip3dLayoutsListener flip3dLayoutsLitener){
		
		//get the views
		View viewFrom = vParent.getChildAt(indexFrom);
		View viewTo = vParent.getChildAt(indexTo);
		
		flip(viewFrom, viewTo, direction, flip3dLayoutsLitener);
	}
	
	/**
	 * Make a flip from child indexFrom to child indexTo of parent received in constructor
	 * @param indexFrom - View to hide
	 * @param indexTo - View to show
	 * @param flip3dLayoutsListener - Flip3dLayoutsListener listener to receive end event
	 */
	public void flip(View viewFrom, View viewTo, int direction, Flip3dLayoutsListener flip3dLayoutsLitener){
		
		//check valid state
		if(state!=STATE_START){
			return;
		}
		
		this.vFrom = viewFrom;
		this.vTo = viewTo;
		this.flip3dLayoutsListener = flip3dLayoutsLitener;
				
		//calculate the width and height to use
		
		viewWidth = vFrom.getWidth();
		viewHeight = vFrom.getHeight();
		
		//get images
		generateImages(vFrom, vTo);
		
		if(direction==DIRECTION_NEXT){
			Log.d("FLIP", "OPENING");
			flipNextStart();
			
		}else{
						
			Log.d("FLIP", "CLOSING");
			flipBackStart();
		}
		
	}
	
	
	private void flipNextStart(){
		
		//change state
		state = STATE_TO_OPEN_MIDDLE;
		
		Log.d("FLIP", "flipTopToHide");
		//calculate vertex
		int centerX = (int) (viewWidth / 2.0f);
    	int centerY = (int) (viewHeight / 2.0f);
		
    	//make animation
		Flip3dAnimation anim = new Flip3dAnimation(0, -90, centerX, centerY, ANIMATION_3D_DEPTH, true);
		anim.setInterpolator(new AccelerateInterpolator());
		runAnimation(anim, ivTopToHide);
    	
		
	}
	
	private void flipNextEnd(){
		Log.d("FLIP", "flipBottomToShow");
		//calculate vertex
		int centerX = (int) (viewWidth / 2.0f);
    	int centerY = (int) (0);
		
		Flip3dAnimation anim = new Flip3dAnimation(90, 0, centerX, centerY, ANIMATION_3D_DEPTH, true);
		anim.setInterpolator(new DecelerateInterpolator());
		runAnimation(anim, ivBottomToShow);
    	
    	//change visibility of views
    	ivTopToHide.setVisibility(View.INVISIBLE);
    	ivBottomToShow.bringToFront();
		
	}
	
	private void flipBackStart(){
		
		//change state
    	state = STATE_TO_CLOSE_MIDDLE;
		
		Log.d("FLIP", "flipBottomToHide");
		//calculate vertex
		int centerX = (int) (viewWidth / 2.0f);
    	int centerY = (int) (0);
    	
		Flip3dAnimation anim = new Flip3dAnimation(0, 90, centerX, centerY, ANIMATION_3D_DEPTH, true);
		anim.setInterpolator(new AccelerateInterpolator());
		runAnimation(anim, ivBottomToHide);
    	
	}
	
	private void flipBackEnd(){
		Log.d("FLIP", "flipTopToShow");
		//calculate vertex
		int centerX = (int) (viewWidth / 2.0f);
    	int centerY = (int) (viewHeight / 2.0f);
		
		Flip3dAnimation anim = new Flip3dAnimation(-90, 0, centerX, centerY, ANIMATION_3D_DEPTH, true);
		anim.setInterpolator(new DecelerateInterpolator());
		runAnimation(anim, ivTopToShow);
    			
		ivBottomToHide.setVisibility(View.INVISIBLE);
		ivTopToShow.bringToFront();
	}
	
	private void runAnimation(Flip3dAnimation flip3dAnimation, View viewToAnime){
		
		//save view animating
		viewAnimating = viewToAnime;
		
		//generate the animation
		flip3dAnimation.setDuration(ANIMATION_DURATION/2);
		viewAnimating.setAnimation(flip3dAnimation);
    	flip3dAnimation.setAnimationListener(listener);
    	flip3dAnimation.startNow();
	}
	
	private View viewAnimating = null;
	
	private AnimationListener listener = new AnimationListener(){

		@Override
		public void onAnimationEnd(Animation animation) {
			
			if(viewAnimating!=null){
				viewAnimating.clearAnimation();
				viewAnimating = null;
			}
			
			//change the state
			if(state==STATE_TO_OPEN_MIDDLE){
				state = STATE_END;
				flipNextEnd();
			
			}else if(state==STATE_TO_CLOSE_MIDDLE){
				state = STATE_END;
				flipBackEnd();
			
			}else if(state==STATE_END){
				endAnimation();
			}
			
		}

		@Override
		public void onAnimationRepeat(Animation arg0) {
			
		}

		@Override
		public void onAnimationStart(Animation arg0) {
			
		}
		
	};
	
	public interface Flip3dLayoutsListener{
		public void onEndFlipAnimation();
	}
		
}
