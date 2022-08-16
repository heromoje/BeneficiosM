package com.bcp.bo.discounts.views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.bcp.bo.discounts.R;
import com.bcp.bo.discounts.general.StyleApp;

public class CustomSwitchView extends View {
	
	/***** DEFAULT VALUES TO SET ******/
	
	private static final int BACKGROUND_DRAWABLE_ID = R.drawable.lgn_switch_bg;
	private static final int SWITCH_DRAWABLE_ID = R.drawable.lgn_switch_btn;
	private static final int ON_STRING_ID = R.string.ON;
	private static final int OFF_STRING_ID = R.string.OFF;
	private static final int TEXT_HEIGHT_DIMEN_ID = R.dimen.switchTextHeight;
	private static final int TEXT_ON_COLOR_ID = R.color.switchColorTextOn;
	private static final int TEXT_OFF_COLOR_ID = R.color.switchColorTextOff;
	private static final int TEXT_ON_SHADOW_COLOR_ID = R.color.switchColorShadowTextOn;
	private static final int TEXT_OFF_SHADOW_COLOR_ID = R.color.switchColorShadowTextOff;
	private static final float TEXT_SHADOW_VERTICAL = 0.0f;
	private static final float TEXT_SHADOW_HORIZONTAL = 0.0f;
	private static final String TEXT_TYPEFACE = "formataRegular";
	
	/**********************************/
	
	
	//TIME MS ANIMATION
	private static final int ANIMATION_TIME = 20;
	private static final int ANIMATION_NUM = 5;
	
	private static final int MOVEMENT_MIN = 5; 
	
	//animation
	private int animationMovement = 0;

	//size
	private int widthView = 0;
	private int heightView = 0;
	
	//to paint
	private Paint paint;
	
	//rects
	private Rect rectBg = new Rect();
	private Rect rectSwitcher = new Rect();
	
	//drawables
	private Drawable dBg = null;
	private Drawable dSwitcher = null;
	
	//texts
	private String sOn = null;
	private String sOff = null;
	
	//typeface text
	private String typefaceName = null;
	private Typeface typeface = null;
	
	//size texts
	private int heightText = 0;
	
	//color texts
	private int colorTextOn = Color.TRANSPARENT;
	private int colorTextOff = Color.TRANSPARENT;
	private int colorShadowTextOn = Color.TRANSPARENT;
	private int colorShadowTextOff = Color.TRANSPARENT;
	
	//shadow texts
	private float shadowVerticalText = 0.0f;
	private float shadowHorizontalText = 0.0f;
	
	//typeface of font
	
	//value
	private boolean state = false;
	
	//listener
	private CustomSwitchListener customSwitchListener = null;
	
	public CustomSwitchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		//create paint
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		//get data from attributes
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.customSwitch);
		dBg = ta.getDrawable(R.styleable.customSwitch_bgImage);
		dSwitcher = ta.getDrawable(R.styleable.customSwitch_switcherImage);
		sOn = ta.getString(R.styleable.customSwitch_textOn);
		sOff = ta.getString(R.styleable.customSwitch_textOff);
		heightText = ta.getDimensionPixelSize(R.styleable.customSwitch_textHeight, context.getResources().getDimensionPixelSize(TEXT_HEIGHT_DIMEN_ID));
		colorTextOff = ta.getColor(R.styleable.customSwitch_colorTextOff, context.getResources().getColor(TEXT_OFF_COLOR_ID));
		colorTextOn = ta.getColor(R.styleable.customSwitch_colorTextOn, context.getResources().getColor(TEXT_ON_COLOR_ID));
		colorShadowTextOff = ta.getColor(R.styleable.customSwitch_colorTextOffShadow, context.getResources().getColor(TEXT_OFF_SHADOW_COLOR_ID));
		colorShadowTextOn = ta.getColor(R.styleable.customSwitch_colorTextOnShadow, context.getResources().getColor(TEXT_ON_SHADOW_COLOR_ID));
		shadowVerticalText = ta.getFloat(R.styleable.customSwitch_shadowVerticalText, TEXT_SHADOW_VERTICAL);
		shadowHorizontalText = ta.getFloat(R.styleable.customSwitch_shadowHorizontalText, TEXT_SHADOW_HORIZONTAL);
		typefaceName = ta.getString(R.styleable.customSwitch_textFontName);
		ta.recycle();
		
		//initialize default values if not found in attributes
		initDefaultValues(context);
		
		//set the size of the text to the paint
		paint.setTextSize(heightText);
					
	}
	
	/**
	 * Default values
	 * @param context
	 */
	private void initDefaultValues(Context context){
		
		//resources
		Resources resources = context.getResources();
		
		//images default
		if(dBg==null){
			dBg = resources.getDrawable(BACKGROUND_DRAWABLE_ID);
		}
		if(dSwitcher==null){
			dSwitcher = resources.getDrawable(SWITCH_DRAWABLE_ID);
		}
				
		//text default
		if(sOn==null){
			sOn = resources.getString(ON_STRING_ID);
		}
		if(sOff==null){
			sOff = resources.getString(OFF_STRING_ID);
		}
		
		//color text default
		if(colorTextOn==Color.TRANSPARENT){
			colorTextOn = resources.getColor(TEXT_ON_COLOR_ID);
		}
		if(colorTextOff==Color.TRANSPARENT){
			colorTextOff = resources.getColor(TEXT_OFF_COLOR_ID);
		}
		
		//font
		if(typefaceName==null){
			typefaceName = TEXT_TYPEFACE;
		}
		typeface = StyleApp.getPreLoadTypeface(getContext(), typefaceName);
	}
	
	
	@Override
    protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		//check animation
		if(animationDirection!=0){
			moveSwitcher(animationMovement*animationDirection);
			
			//check if it is the end of animation
			if(rectSwitcher.left==0 || rectSwitcher.right==widthView){
				animationDirection = 0;
				
			//it's not the end of animation, so we call to paint again
			}else{
				//make animation
				postInvalidateDelayed(ANIMATION_TIME);
			}
		}
		
		//paint view
		if(dBg!=null){
			dBg.draw(canvas);
		}else{
			paint.setColor(Color.BLUE);
			canvas.drawRect(rectBg, paint);
		}
		
		//set paint for texts
		paint.setStyle(Style.FILL);
		paint.setTextAlign(Align.CENTER);
		paint.setTypeface(typeface);
		
		//position y of the text
		int posY = (int) (((heightView-heightText)/2)-paint.ascent());
		
		//paint text over background
		if(sOn!=null && sOn.length()>0){
			
			int posX = widthView/4;
			
			//shadow
			if(shadowVerticalText!=0 || shadowHorizontalText!=0){
				paint.setColor(colorShadowTextOn);
				canvas.drawText(sOn, posX+shadowHorizontalText, posY+shadowVerticalText, paint);
			}
			
			//text
			paint.setColor(colorTextOn);
			canvas.drawText(sOn, posX, posY, paint);
		}
		
		if(sOff!=null && sOff.length()>0){
			
			int posX = (widthView*3)/4;
			
			//shadow
			if(shadowVerticalText!=0 || shadowHorizontalText!=0){
				paint.setColor(colorShadowTextOff);
				canvas.drawText(sOff, posX+shadowHorizontalText, posY+shadowVerticalText, paint);
			}
			
			//text
			paint.setColor(colorTextOff);
			canvas.drawText(sOff, posX, posY, paint);
		}
		
		//paint switcher
		if(dSwitcher!=null){
			dSwitcher.setBounds(rectSwitcher);
			dSwitcher.draw(canvas);
		}else{
			paint.setColor(Color.RED);
			canvas.drawRect(rectSwitcher, paint);
		}
	}
	
	@Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		widthView = dBg.getIntrinsicWidth();//MeasureSpec.getSize(widthMeasureSpec);
		heightView = dBg.getIntrinsicHeight();//MeasureSpec.getSize(heightMeasureSpec);
		
		//save rect bg
		rectBg.right = widthView;
		rectBg.bottom = heightView;
		
		if(dBg!=null){
			dBg.setBounds(rectBg);
		}
		
		//save rect switcher
		if(!state){
			rectSwitcher.left = 0;
			rectSwitcher.right = rectSwitcher.left + dSwitcher.getIntrinsicWidth();//widthView/2;
		}else{
			rectSwitcher.right = widthView;
			rectSwitcher.left = widthView - dSwitcher.getIntrinsicWidth();
		}
		rectSwitcher.bottom = heightView;
		
		//calculate animation direction
		animationMovement = widthView / ANIMATION_NUM;
		
//		this.setMeasuredDimension(MeasureSpec.makeMeasureSpec(widthView, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(heightView, MeasureSpec.EXACTLY));
		this.setMeasuredDimension(widthView, heightView);
	}

	//last and initial touch position
	private int lastX = 0;
	private int iniX = 0;
	private int direction = 0;
	
	private int animationDirection = 0;
	
	@Override
    public boolean onTouchEvent(MotionEvent ev) {
		
		//position
		int posX = (int) ev.getX();
		
        //DOWN
        if(ev.getAction()==MotionEvent.ACTION_DOWN){
        	
        	//save initial position
        	if(posX<widthView/2){
        		direction = -1;
        	}else{
        		direction = 1;
        	}
        	iniX = posX;
        	lastX = posX;
        	animationDirection = 0;
        	
        //MOVE
        }else if(ev.getAction()==MotionEvent.ACTION_MOVE){
        	
        	//calculate difference with the last one
        	moveSwitcher(posX - lastX);
        	
        	 
        //UP, CANCEL, OUT
        }else{
        	
			//save the direction of the animation
			if(Math.abs(posX-iniX)<MOVEMENT_MIN){
				if(!state){
					animationDirection = 1;
				}else{
					animationDirection = -1;
				}
			}else{
				animationDirection = direction!=0? direction : -1;
			}
			
			//change state with value of animationDirection and call to listener
			if(animationDirection==1){
				state = true;
				callCustomSwitchListener();
				
			}else if(animationDirection==-1){
				state = false;
				callCustomSwitchListener();
			}
        	
        }
        
        //calculate direction
        if(posX>lastX){
        	direction = 1;
        }else if(posX<lastX){
        	direction = -1;
        }
        
        //save last position
        lastX = posX;
        
        //invalidate
        invalidate();
        
        //return event consumed
        return true;
	}
	
	/**
	 * Move the switcher in x axis
	 * @param x - int axis value
	 */
	private void moveSwitcher(int x){
		
		//check if it is outside
		int leftSwitcher = rectSwitcher.left + x;
    	if(leftSwitcher<0){
    		leftSwitcher = 0;
    	}else if(leftSwitcher + rectSwitcher.width() > widthView){
    		leftSwitcher = widthView - rectSwitcher.width();
    	}
    	
    	//set the new location of the switcher
    	rectSwitcher.right = leftSwitcher + rectSwitcher.width();
    	rectSwitcher.left = leftSwitcher;
	}
	
	/**
	 * Set the state without flag animated
	 * @param state
	 */
	public void setState(boolean state){
		setState(state, true);
	}
	
	/**
	 * Activate or deactivate the switch
	 * @param state - boolean with true for activated and false for deactivated
	 */
	public void setState(boolean state, boolean animated){
		
		//check state is different to actual to make the change
		if(!this.state && state){
			
			//move the switcher
			if(animated){
				animationDirection = -1;
			}else{
				moveSwitcher(-rectSwitcher.left);
			}
			
			invalidate();
			
		}else if(this.state && !state){
			
			//move the switcher
			if(animated){
				animationDirection = 1;
			}else{
				moveSwitcher(widthView-rectSwitcher.width()-rectSwitcher.left);
			}
			
			invalidate();
			
		}
		
		//set the new state
		this.state = state;
		
	}
	
	/**
	 * Indicate if switch is activated
	 * @return boolean with state value
	 */
	public boolean isOn(){
		return state;
	}
	
	/**
	 * Set the listener for this view
	 * @param customSwitchListener - CustomSwitchListener to call when swtch change
	 */
	public void setCustomSwitchListener(CustomSwitchListener customSwitchListener) {
		this.customSwitchListener = customSwitchListener;
	}
	
	/**
	 * Call to the listener if not null
	 */
	private void callCustomSwitchListener(){
		if(customSwitchListener!=null){
			customSwitchListener.switchChanged(this, state);
		}
	}
	
	/**
	 * Interface for switch listener
	 * @author javiersanchezruiz
	 *
	 */
	public interface CustomSwitchListener{
		public void switchChanged(CustomSwitchView csv, boolean isOn);
	}	
}
