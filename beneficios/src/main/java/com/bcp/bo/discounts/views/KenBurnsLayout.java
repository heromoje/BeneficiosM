package com.bcp.bo.discounts.views;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

public class KenBurnsLayout extends FrameLayout {
	
	/// Time default for animations
	private static final int ANIMATION_DURATION_DEFAULT = 8000;
	private static final int ANIMATION_DURATION_APPEAR_DEFAULT = 2000;
	
	//maximum percent of image to increase
	private static final float IMAGE_SIZE_PERCENT_MOVEMENT_MAXIMUM_DEFAULT = 0.10f;
	private static final float IMAGE_SIZE_PERCENT_SCALE_MAXIMUM_DEFAULT = 0.20f;
	
	/** Value to decrease the image **/
	private static final int IMAGE_DECREASE_SIZE = 2; //1:Normal, 2:Half image 4:/4 size 
	
	
	//Images views used to make animations
	private View viewRunning = null; 
			
	/** List of images to show **/
	private List<String> listImagesPath = null;
	private List<Integer> listImagesRId = null;
	
	/** Index of the image to play - This is changed in private:getNextDrawable() **/
	private int imageIndexToRun = -1;
	
	//size of layout
	private int layoutWidth = 0;
	private int layoutHeight = 0;
	
	//duration of animation
	private int animationDuration = ANIMATION_DURATION_DEFAULT;
	private int animationDurationAppear  = ANIMATION_DURATION_APPEAR_DEFAULT;
	
	//maximum percent of image to increase
	private float imageSizePercentMovementMaximum = IMAGE_SIZE_PERCENT_MOVEMENT_MAXIMUM_DEFAULT;
	private float imageSizePercentScaleMaximum = IMAGE_SIZE_PERCENT_SCALE_MAXIMUM_DEFAULT;
	

	private static final int STATE_STOP = 0;
	private static final int STATE_PLAY = 1;
	private static final int STATE_PLAY_WAIT = 2;
	private static final int STATE_PAUSE = 3;
	
	/** State of animations **/
	private int state = STATE_STOP;
	
	
	//-------------- CONSTRUCTION

	public KenBurnsLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initViews();
		initValues();
		init();
	}

	public KenBurnsLayout(Context context) {
		super(context);
		
		initViews();
		initValues();
		init();
	}
	
	/**
	 * Initialize the views
	 */
	private void initViews(){
				
	}
	
	/**
	 * Initialize the values
	 */
	private void initValues(){
		listImagesPath = new ArrayList<String>();
		listImagesRId = new ArrayList<Integer>();
	}
	
	/**
	 * Initialize the component
	 */
	private void init(){
	
		
	}
	
	//------------ PREPARATION ------------
	
	/**
	 * Add images to the list
	 * @param listImages - List<String> of path images
	 */
	public void addImages(List<String> listImagesPath){
		for(String s : listImagesPath){
			addImage(s);
		}
	}
	
	/**
	 * Add images to the list
	 * @param listImages - List<Integer> of id images of resources
	 */
	public void addImages(int[] listImagesRId){
		for(Integer i : listImagesRId){
			addImage(i);
		}
	}
	
	/**
	 * Add an image to the list of images to show
	 * @param s - String path of image
	 */
	public void addImage(String s){
		this.listImagesPath.add(s);
		this.listImagesRId.add(0);
	}
	
	/**
	 * Add an image to the list of images to show
	 * @param rId - int id of view of resources
	 */
	public void addImage(int rId){
		this.listImagesPath.add("");
		this.listImagesRId.add(rId);
	}
	
	//------------- ACTIONS ----------------
	
	/**
	 * Start to play animations
	 */
	public void play(){
		
		//check state of component 
		if(layoutWidth==0 || layoutHeight==0){
			
			//no size so we wait to play
			state = STATE_PLAY_WAIT;
			return;
		}else{
			state = STATE_PLAY;
		}
				
		//play next animation
		playNextAnimation();
	}
	
	/**
	 * Play next animation calling to the timer for the next animation
	 */
	private void playNextAnimation(){
		
		//check state pause and error
		if(state==STATE_STOP){
			return;
		}else if(state==STATE_PAUSE){
			return;
		}
				
		//prepare animation
		AnimationSet as = updateAnimationToRun();
		if(as==null){
			return;
		}
		
		//show view at the top
		bringChildToFront(viewRunning);
		viewRunning.setAnimation(as);
		
		//start animation
		as.start();
		
		//remove the first child because it is not being used
		if(super.getChildCount()>2){
			super.removeViewAt(0);
		}
		
		//add the view to show
		addView(viewRunning);
				
		//prepare timer to play next animation
		postDelayed(playNextAnimationRunnable, animationDuration-animationDurationAppear);
		
	}
	
	/**
	 * Called when the animation has ended and has to show next animation
	 */
	private Runnable playNextAnimationRunnable = new Runnable(){
		
		@Override
		public void run() {
			playNextAnimation();
		}
		
	};
	
	/**
	 * Stop the animations and when play will continue from stopped animation
	 */
	public void pause(){
		state = STATE_PAUSE;
	}
	
	/**
	 * Stop the animations and next time will start from the beginning
	 */
	public void stop(){
		state = STATE_STOP;
	}
	 
	/**
	 * Set the duraton of an animation
	 * @param duration - int Milliseconds
	 */
	public void setAnimationDuration(int animationDuration){
		this.animationDuration = animationDuration;
	}
	
	//------------ SUPER METHODS ----------
	
	@Override
	protected void onLayout (boolean changed, int left, int top, int right, int bottom){
		super.onLayout(changed, left, top, right, bottom);
		
		//set the size of layout
		layoutWidth = right-left;
		layoutHeight = bottom-top;
		
		//if we are waiting to play we play
		if(state==STATE_PLAY_WAIT){
			play();
		}
	}
	
	//------------ CALCULATIONS ----------------
	
	/**
	 * Generate a random point origin
	 * @param width - int width of image
	 * @param height - int height of image
	 * @return Pair<Point,Point> - point origin and point destiny where to start an animation
	 */
	private Pair<Point, Point> generateRandomPoints(Point size){
		
		//random size to move
		int widthMove = (int) (Math.random()*(size.x*imageSizePercentMovementMaximum));
		int heightMove = (int) (Math.random()*(size.y*imageSizePercentMovementMaximum));
		
		//diference of size between image and screen
		int difX = size.x-layoutWidth;
		int difY = size.y-layoutHeight;
		
		//check we have enough difference for movement, if not make size bigger
		if(difX<widthMove){
			size.y *= ((float)(layoutWidth + widthMove)) / (float)size.x;
			size.x = layoutWidth + widthMove;
			difY = size.y-layoutHeight;
			difX = size.x-layoutWidth;
		}
		if(difY<heightMove){
			size.x *= ((float)(layoutHeight + heightMove)) / (float)size.y;
			size.y = layoutHeight + heightMove;
			difX = size.x-layoutWidth;
			difY = size.y-layoutHeight;
		}
		
		int randMoveX = (int) (getIntRandom(2)*widthMove);
		int randMoveY = (int) (getIntRandom(2)*heightMove);
		
		//get random points for origin
		int xOrig = (int) ((Math.random()*(difX-widthMove)) + randMoveX);
		int yOrig = (int) ((Math.random()*(difY-heightMove)) + randMoveY);
		
		//get random direction and calculate destiny
		int xDest = xOrig + widthMove;
		if(getIntRandom(2)==1){
			xDest = xOrig - widthMove;
		}
		int yDest = yOrig + heightMove;
		if(getIntRandom(2)==1){
			yDest = yOrig - heightMove;
		}
		
		//check the point destiny is inside the size of the image
		if(xDest<0){
			xDest = xOrig + widthMove;
		}else if(xDest>size.x-layoutWidth){
			xDest = xOrig - widthMove;
		}
		
		if(yDest<0){
			yDest = yOrig + heightMove;
		}else if(yDest>size.y-layoutHeight){
			yDest = yOrig - heightMove;
		}
		
		//generate the points
		Point pointOrigin = new Point(-xOrig, -yOrig);
		Point pointDestiny = new Point(-xDest, -yDest);
		
		//if hor and ver are center there is no movement so by default we select TOP LEFT
		return new Pair<Point, Point>(pointOrigin, pointDestiny);
	}
	
	/**
	 * Get the drawable for the next animation
	 * @return Drawable - image to show in next animation / CAN BE NULL if no images to play
	 */
	private Drawable getNextDrawable(){
		
		//if no images directly respond null
		if(listImagesPath.size()==0){
			return null;
		}
		
		//get the next index
		imageIndexToRun++;
		
		//check if index is out of array to start from the beginning
		if(imageIndexToRun>=listImagesPath.size()){
			imageIndexToRun = 0;
		}
		
		//get path an id sved in that position
		String sPath = listImagesPath.get(imageIndexToRun);
		int rId = listImagesRId.get(imageIndexToRun);
		
		//get the drawable
		if(sPath.length()>0){
			
			//get image from path
//			return Drawable.createFromPath(sPath);
			
			//options
			Options opts = new BitmapFactory.Options();
			opts.inScaled = true;
			opts.inSampleSize = IMAGE_DECREASE_SIZE;
			
			//inputstream
			InputStream is = null;
			try{
				is = new FileInputStream(sPath);
			}catch(FileNotFoundException  fnfe){
				return null;
			}
			
			Drawable d = null;
			try{
				d = Drawable.createFromResourceStream(getResources(), null, is, null, opts);
			}catch(Exception e){
				//this is necessary because some devices launch nullPointerException when try to get a resource
				try{
					d = Drawable.createFromStream(is, null);
				}catch(Exception e2){
				}
			}
			return d;
			
		}else if(rId!=0){
			
			//get image from resource
			return getContext().getResources().getDrawable(rId);
		}else{
			
			//the image is incorrect, return error
			return null;
		}
		
	}
	
	/**
	 * Resize a drawable to the maximun side keeping the aspect
	 * @param d - Drawable to resize
	 * @param mWidth - int maximun or minimun width possible
	 * @param mHeight - int maximun or minimun height possible
	 * @param maxValues - boolean true if values are max and false if values are taking as min
	 * @return Point - size of image for that values
	 */
	public static Point sizeImage(Drawable d, int mWidth, int mHeight, boolean maxValues){
		
		//get scaled width and height
		float aspectImage = (float)d.getIntrinsicWidth() / (float)d.getIntrinsicHeight(); 
		float aspectSize = (float)mWidth / (float)mHeight;
		
		int width = mWidth;
		int height = mHeight;
		
		//check what we have to adjust
		if((aspectImage<aspectSize && maxValues) || (aspectImage<aspectSize && !maxValues)){
			
			//take max width
			height = (mWidth * d.getIntrinsicHeight()) / d.getIntrinsicWidth();
			
		}else{
			
			//take max height
			width = (mHeight * d.getIntrinsicWidth()) / d.getIntrinsicHeight();
			
		}
		
		//return the resized drawable
		return new Point(width, height);
	}
	
	//------------ ANIMATIONS -----------------
	
	/**
	 * Update the animationToRun variable that will run when time ends
	 * @return AnimationSet - AnimationSet or null if no animations
	 */
	private AnimationSet updateAnimationToRun(){
		
		//create a view that we will add
		viewRunning = new View(getContext());
		FrameLayout.LayoutParams flp1 = new FrameLayout.LayoutParams(0, 0);
		viewRunning.setLayoutParams(flp1);
		
		//generate the animation set
		AnimationSet aSet = new AnimationSet(false);
		
		//assign the drawable to the view
		Drawable d = getNextDrawable();
		if(d==null){
			return null;
		}
		
		//get the size of the drawable for this screen size
		Point size = KenBurnsLayout.sizeImage(d, layoutWidth, layoutHeight, false);
		
		//generate animation, these modify the size giving us the initial size
		if(getIntRandom(2)==0){
			aSet.addAnimation(generateTranslateAnimation(size));
		}else{
			aSet.addAnimation(generateScaleAnimation(size));
		}
		if(super.getChildCount()>0){
			aSet.addAnimation(generateAppearAlphaAnimation());
		}
		
		//set the size that could be modified by generators of animations
		setDrawableToView(viewRunning, d, size.x, size.y);
		
		//return animations
		return aSet;
	}
	
	/**
	 * Set the drawable as background of the view
	 * Set the size of the view taking the layout size to apply scale if need
	 * @param view - View to modify
	 * @param d - Drawable to apply to the view
	 */
	@SuppressWarnings("deprecation")
	private void setDrawableToView(View view, Drawable d, int width, int height){
		
		//set the size
		FrameLayout.LayoutParams flp = (FrameLayout.LayoutParams) view.getLayoutParams();
		flp.width = width;
		flp.height = height;
		view.setLayoutParams(flp);
		
		//add drawable to view
		view.setBackgroundDrawable(d);
	}
	
	/**
	 * Generate an alpha animation to appear
	 * @return AlphaAnimation - animation generated to show the view
	 */
	private AlphaAnimation generateAppearAlphaAnimation(){
		
		//generate the alpha animation
		AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
		aa.setDuration(animationDurationAppear);
		
		//return the animation generated
		return aa;
	}
	
	/**
	 * Generate a translate animation in a view with the drawable received
	 * LayoutParams of the view are changed to coincide with size of drawable
	 * @param size - Point size of the image to show, this can be modify to make the image at right side
	 * @return TranslateAnimation - animation with translation
	 */
	private TranslateAnimation generateTranslateAnimation(Point size){
		
		//define the origin point in image
		Pair<Point,Point> points = generateRandomPoints(size);
		Point pointOrigin = points.first;
		Point pointDestiny = points.second;
		
		//generate the translate animation
		TranslateAnimation ta = new TranslateAnimation(pointOrigin.x, pointDestiny.x, pointOrigin.y, pointDestiny.y);
		ta.setDuration(animationDuration);
		ta.setFillAfter(true);
		ta.setFillBefore(true);
		
		//return the animation
		return ta;
	}
	
	/**
	 * Generate a scale animation, this will need a translate animation as well
	 * @param size - Point size of image at beginning that will be change if need
	 * @return Animation - Animation Set with scale and translation animation
	 */
	private AnimationSet generateScaleAnimation(Point size){
		
		//movement
		float scaleMove = (float) (Math.random()*imageSizePercentScaleMaximum);
		
		//origin where to start
		float scaleOrig = 1.0f;
		float scaleDest = 1.0f;
		
		//aleat decide direction of scale
		if(getIntRandom(2)==0){
			scaleDest -= scaleMove;
		}else{
			scaleDest += scaleMove;
		}
		
		//check for scaleDest if is enough with actual size, if not we increase the scale
		if(scaleDest<1.0f){
			float scaleIncrease = 1.0f / (float)scaleDest;
			size.x *= scaleIncrease;
			size.y *= scaleIncrease;
		}
		
		AnimationSet as = new AnimationSet(true);
		
		//generate the scale animation
		ScaleAnimation sa = new ScaleAnimation(scaleOrig, scaleDest, scaleOrig, scaleDest);
		sa.setDuration(animationDuration);
		as.addAnimation(sa);
		
		//positions for animation
		int x0 = (int) ((layoutWidth-(size.x*scaleOrig))/2);
		int y0 = (int) ((layoutHeight-(size.y*scaleOrig))/2);
		int xF = (int) ((layoutWidth-(size.x*scaleDest))/2);
		int yF = (int) ((layoutHeight-(size.y*scaleDest))/2); 
		
		//create translate animation
		TranslateAnimation ta = new TranslateAnimation(x0, xF, y0, yF);
		ta.setDuration(animationDuration);
		as.addAnimation(ta);
		
		//return the animation
		return as;
	}
	
	/**
	 * Get an integer random from 0 to number max
	 * @param max - int max number possible (not included)
	 * @return int - random number
	 */
	private int getIntRandom(int max){
		return ( (int)(Math.random()*1000) ) % max;
	}
	
}
