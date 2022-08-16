package com.bcp.bo.discounts.views;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class CustomSliderLayout extends FrameLayout{
	
	//variables for listener
	public static final int ACTION_OPEN = 1;
	public static final int ACTION_CLOSE = -1;
	public static final int MOMENT_START = 0;
	public static final int MOMENT_END = 1;	
	
	/** Animation time in milliseconds **/
	private static final int ANIMATION_DURATION = 300;
	private static final int CLICK_PIXELS_ACTIVATION = 10;
	
	/** Flag to show animated or not **/
	private static final boolean ANIMATED_DEFAULT = true;
	private static final boolean ANIMATED_TOUCH_DEFAULT = true;

	//views
	private View vContent = null;
			
	/** Number of pixels accepted as click and no move **/
	private int clickPixels = 0;
	
	//pull
	private ImageView ivPull = null;
	private int pullHeight = 0;
	
	//layout size
	private int layoutWidth = 0;
	private int layoutHeight = 0;
	
	//content size
	private int contentHeight = 0;
	private int contentWidth = 0;
		
	//flag to know if it is touching pull
	private boolean pullActive = false;
	
	//flag to know if real views or not
	private boolean showRealContent = false;
	
	//touch variables
	private int startY = 0;
	private int touchIniY = 0;
	private int touchLastY = 0;
	private int paintY = 0;
	private int direction = 0;
	
	/** Listener to call when actions **/
	private CustomSliderListener listener;
	
	/** Flag to show animation or not **/
	private boolean animatedTouch = ANIMATED_TOUCH_DEFAULT;
	private boolean animated = ANIMATED_DEFAULT;
	
	public CustomSliderLayout(Context context) {
		super(context);
		initAll();
	}
	
	public CustomSliderLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAll();
	}
	
	public CustomSliderLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initAll();
	}
	
	private void initAll(){
		initViews();
		initValues();
		initListeners();
		init();
	}
	

	private void initViews() {
		
		
	}
	
	/**
	 * Set the view used to show as pull, this will be shown in center
	 * @param width - int size specific or MATCH_PARENT, WARP_CONTENT
	 * @param rId - int with id of drawable to show
	 */
	@SuppressWarnings("deprecation")
	public void setPullView(int rId, int width){
		
		Drawable dPull = getContext().getResources().getDrawable(rId);
		pullHeight = dPull.getIntrinsicHeight();
		
		//create the image view of the pull
		ivPull = new ImageView(getContext());
		ivPull.setBackgroundDrawable(dPull);
		FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(width, LayoutParams.WRAP_CONTENT);
		ivPull.setLayoutParams(flp);
		addView(ivPull);
		
	}

	/**
	 * Set the content view to show in the slider
	 * @param view - View to show (layout)
	 * @param width - int size specific or MATCH_PARENT, WARP_CONTENT
	 * @param height - int size specific or MATCH_PARENT, WARP_CONTENT
	 */
	public void setContentView(View view, int width, int height){
		
		//check if there is a content view
		if(this.vContent!=null){
			removeView(this.vContent);
		}
		
		//set the view to move
		this.vContent = view;
		
		//set the params
		FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(width, height);
		vContent.setLayoutParams(flp);
		
		//add the layout
		addView(vContent);
		
		//show pull up
		if(ivPull!=null){
			bringChildToFront(ivPull);
		}
				
	}
	
	/**
	 * Remove the content view
	 */
	public void removeContentView(){
		//check if there is a content view
		if(this.vContent!=null){
			removeView(this.vContent);
			this.vContent = null;
		}
		
	}
	
	/**
	 * Active or deactive animations
	 * @param animated - boolean true for activate
	 */
	public void setAnimated(boolean animated){
		this.animated = animated;
	}
	
	/**
	 * Active or deactive touch animations
	 * @param animated - boolean true for activate
	 */
	public void setAnimatedTouch(boolean animatedTouch){
		this.animatedTouch = animatedTouch;
	}
	
	/**
	 * Get the content view
	 * @return ViewGroup - view used as content
	 */
	public View getContentView(){
		return vContent;
	}
	
	/**
	 * Set the listner to call when an action happens
	 * @param listener - CustomSliderListener to call
	 */
	public void setCustomSliderListener(CustomSliderListener listener){
		this.listener = listener;
	}
	
	/**
	 * Initialize the values
	 */
	private void initValues() {
		
		//default values
		if(clickPixels==0){
			clickPixels = CLICK_PIXELS_ACTIVATION;
		}
	}

	private void initListeners() {

	}

	private void init() {
		//need if want to be called draw directly
		this.setBackgroundColor(Color.TRANSPARENT);
	}
	
	//-------------------------- PAINT ----------------------------
	
	@SuppressLint("WrongCall")
	@Override
	public void draw(Canvas canvas){

		if(showRealContent){
			super.draw(canvas);
		}else{
			drawContent(canvas);
			drawPull(canvas);
		}
		
	}
	
	private void drawContent(Canvas canvas){
		if(vContent!=null){
			
			//translate canvas
			canvas.save();
			canvas.translate(0, paintY - contentHeight);
			
			//paint content
			vContent.draw(canvas);
			
			//restore canvas
			canvas.restore();
		}
	}
	
	private void drawPull(Canvas canvas){
		//paint pull
		if(ivPull!=null){
			
			//translate canvas
			canvas.save();
			canvas.translate(0, paintY);
						
			//paint content
			ivPull.draw(canvas);
			
			//restore canvas
			canvas.restore();
		}
	}
	
	
	@Override
	public void onMeasure(int measuredWidth, int measuredHeight){
		super.onMeasure(measuredWidth, measuredHeight);
		
		//set the size
		setMeasuredDimension(MeasureSpec.getSize(measuredWidth), MeasureSpec.getSize(measuredHeight));
		
		if((layoutWidth!=0 && layoutHeight!=0) && vContent!=null){
						
			//check the size of content
			ViewGroup.LayoutParams llp = (ViewGroup.LayoutParams)vContent.getLayoutParams();
			int widthLayout = llp.width;
			int heightLayout = llp.height;
			if(widthLayout==LayoutParams.WRAP_CONTENT){
				widthLayout = MeasureSpec.getSize(measuredWidth);
			}else if(widthLayout==LayoutParams.MATCH_PARENT){
				widthLayout = MeasureSpec.makeMeasureSpec(layoutWidth, MeasureSpec.EXACTLY);
			}
			if(heightLayout==LayoutParams.WRAP_CONTENT){
				heightLayout = MeasureSpec.getSize(measuredHeight);
			}else if(heightLayout==LayoutParams.MATCH_PARENT){
				heightLayout = MeasureSpec.makeMeasureSpec(layoutHeight-pullHeight, MeasureSpec.EXACTLY);
			}
			
			//call to measure of son
			vContent.measure(widthLayout, heightLayout);
			
			//save the size
			contentWidth = vContent.getMeasuredWidth();
			contentHeight = vContent.getMeasuredHeight();
			
			//set the size of content
			vContent.layout(0, 0, contentWidth, contentHeight);
												
		}
		
		
	}
	
	@Override
	protected void onLayout (boolean changed, int left, int top, int right, int bottom){
		super.onLayout(changed, left, top, right, bottom);
		
		//set the size of layout
		layoutWidth = right-left;
		layoutHeight = bottom-top;
		
		//set the x position of pull
		if(ivPull!=null){
			FrameLayout.LayoutParams flp = (FrameLayout.LayoutParams)ivPull.getLayoutParams();
			flp.leftMargin = 0;
			ivPull.setLayoutParams(flp);
		}
	}

	//-------------------------- ACTIONS ---------------------------
	
	/**
	 * Force the open of slider
	 * @param animated - boolean true to do it with animation, false to do it directly
	 */
	public void forceOpen(boolean animated){
		
		if(animated){
			openSliderAnimated();
		}else{
			endOpen();
		}
		
	}
	
	/**
	 * Force the close of slider
	 * @param animated - boolean true to do it with animation, false to do it directly
	 */
	public void forceClose(boolean animated){
		
		if(animated){
			closeSliderAnimated();
		}else{
			endClose();
		}
		
	}
	
	
	//-------------------------- TOUCH ----------------------------
	
	@Override
	public boolean onInterceptTouchEvent (MotionEvent ev){
		if(showRealContent){
			return false;
		}else{
			return true;
		}
	}
	
	@Override
	public boolean onTouchEvent (MotionEvent motionEvent){
		int y = (int)motionEvent.getY();
		
		//check touching pull
		if(!pullActive){
			if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
				if(y<=paintY+pullHeight && y>=paintY){
					pullActive = true;
					showRealContent = false;
					if(vContent!=null){
						vContent.setVisibility(View.VISIBLE);
					}
					
				}else{
					return false;
				}
			}else{
				return false;
			}
		}
		
		//DOWN
		if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
			
			//call to listener if we have
			if(listener!=null){
				if(paintY==0){
					listener.onSliderAction(ACTION_OPEN, MOMENT_START);
				}else{
					listener.onSliderAction(ACTION_CLOSE, MOMENT_END);
				}
			}
			
			//init values touch
			startY = paintY;
			touchIniY = y;
			touchLastY = touchIniY;
			direction = 0;
			
		//MOVE
		}else if(motionEvent.getAction()==MotionEvent.ACTION_MOVE){
			
			//calculate real position, it is different if we started from bottom or from top
			if(animatedTouch){
				paintY = (y-touchIniY)+startY;
				if(paintY<0){
					paintY = 0;
				}else if(paintY>contentHeight){
					paintY = contentHeight;
				}
			}
			
		//UP - CANCEL - OUT
		}else{
			
			showRealContent = true;
			
			//difference of movement from start
			int difference = Math.abs(y-touchIniY);
			if(difference<clickPixels){
				if(startY==0){
					direction = 1;
				}else{
					direction = -1;
				}
			}
											
			//direction DOWN
			if(direction==1){
				
				if(vContent!=null){
					openSliderAnimated();
				}
				
				paintY = contentHeight;
				
			//direction UP
			}else{
				
				if(vContent!=null){
					closeSliderAnimated();
				}
				
				paintY = 0;
			}
			
			
			pullActive = false;
		}
		
		
		
		//get the direction saving the last position
		if(y>touchLastY){
			direction=1;
		}else if(y<touchLastY){
			direction=-1;
		}
		touchLastY = y;
		
		//invalidate
		invalidate();
		
		//catch all events
		return true;
	}
	
	/**
	 * Open the slider with an animation
	 */
	private void openSliderAnimated(){
		
		//check if has to be animated
		if(!animated){
			endOpen();
			return;
		}
		
		//animator to play
		AnimationSet as = new AnimationSet(true);
//		as.setInterpolator(new LinearInterpolator());
		if(vContent!=null){
			
			//animate the content
			TranslateAnimation ta = new TranslateAnimation(0, 0, paintY-contentHeight, 0);
			ta.setDuration(ANIMATION_DURATION);
			vContent.setAnimation(ta);
			as.addAnimation(ta);
		}
		
		if(ivPull!=null){
			
			//set the params to show the pull
			FrameLayout.LayoutParams flpPull = (FrameLayout.LayoutParams)ivPull.getLayoutParams();
			flpPull.gravity = Gravity.TOP;
			ivPull.setLayoutParams(flpPull);
			
			//animate
			TranslateAnimation ta = new TranslateAnimation(0, 0, paintY, contentHeight);
			ta.setDuration(ANIMATION_DURATION);
			ta.setAnimationListener(animationOpenListener);
			ivPull.setAnimation(ta);
			as.addAnimation(ta);
			ivPull.invalidate();
		}
		
		//play animations
		as.start();
		
		
	}
	
	/**
	 * Animation called when slider is opened
	 */
	AnimationListener animationOpenListener = new AnimationListener(){

		@Override
		public void onAnimationEnd(Animation arg0) {
			
			//clear the animation before setting the values after animation
			ivPull.clearAnimation();	
			
			//set open state
			endOpen();
			
			//call to listener if we have
			if(listener!=null){
				listener.onSliderAction(ACTION_OPEN, MOMENT_END);
			}
		}

		@Override
		public void onAnimationRepeat(Animation arg0) {
			
		}

		@Override
		public void onAnimationStart(Animation arg0) {
			
			
		}
		
	};
	
	/**
	 * Set the final state for open
	 */
	private void endOpen(){
		//set the params to show the pull
		FrameLayout.LayoutParams flpPull = (FrameLayout.LayoutParams)ivPull.getLayoutParams();
		flpPull.gravity = Gravity.BOTTOM;
		ivPull.setLayoutParams(flpPull);
		
		//hide pull
		invalidate();
	}
	
	/**
	 * Close the slider with animation
	 */
	private void closeSliderAnimated(){
		
		//check if has to be animated
		if(!animated){
			
			//set the params to show the pull
			FrameLayout.LayoutParams flp = (FrameLayout.LayoutParams)ivPull.getLayoutParams();
			flp.gravity = Gravity.TOP;
			ivPull.setLayoutParams(flp);
			
			endClose();
			return;
		}
		
		//animator to play
		AnimationSet as = new AnimationSet(true);
		as.setInterpolator(new LinearInterpolator());
		
		if(vContent!=null){
			
			//animate the content
			TranslateAnimation ta = new TranslateAnimation(0, 0, paintY-contentHeight, -contentHeight);
			ta.setDuration(ANIMATION_DURATION);
			vContent.setAnimation(ta);
			as.addAnimation(ta);
		}
		
		if(ivPull!=null){
			
			//set the params to show the pull
			FrameLayout.LayoutParams flp = (FrameLayout.LayoutParams)ivPull.getLayoutParams();
			flp.gravity = Gravity.TOP;
			ivPull.setLayoutParams(flp);
			
			//animate
			TranslateAnimation ta = new TranslateAnimation(0, 0, paintY, 0);
			ta.setDuration(ANIMATION_DURATION);
			ta.setAnimationListener(animationCloseListener);
			ivPull.setAnimation(ta);
			as.addAnimation(ta);
			ivPull.invalidate();
		}
		
		//play animations
		as.start();
	}
	
	/**
	 * Listener called when slider is closed running an animation
	 */
	AnimationListener animationCloseListener = new AnimationListener(){

		@Override
		public void onAnimationEnd(Animation arg0) {
			
			//clear the animation before setting the values after animation
			ivPull.clearAnimation();
			
			//set close state
			endClose();
			
			//call to listener if we have
			if(listener!=null){
				listener.onSliderAction(ACTION_CLOSE, MOMENT_END);
			}
		}

		@Override
		public void onAnimationRepeat(Animation arg0) {
			
		}

		@Override
		public void onAnimationStart(Animation arg0) {
			
		}
		
	};
	
	/**
	 * Set the final state for close
	 */
	private void endClose(){
		
		//add the view
		if(vContent!=null){
			vContent.setVisibility(View.GONE);
		}
		
		//hide pull
		invalidate();
	}
	
	public interface CustomSliderListener{
		public void onSliderAction(int action, int moment);
	}
	
}


