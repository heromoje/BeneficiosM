package com.bcp.bo.discounts.general;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class StyleApp implements ViewGroup.OnHierarchyChangeListener{
	
	//fonts
	private static Typeface formataRegular;
	private static Typeface formataItalic;
	private static Typeface formataMedium;
	private static Typeface formataMediumItalic;
	
	//---------------- READ TAG -------------------
	
	/**
	 * Apply font to a view
	 * @param view - View to apply the style
	 */
	private static void applyTypeface(View view){
		
		//check instaceof tag
		String sTag = null;
		if(view.getTag() instanceof String){
			sTag = (String)view.getTag();
		}else{
			return;
		}
		
		//check there is style
		if(sTag.length()==0){
			return;
		}
			
		//font, color, size and style is all we can get
		Typeface typeface = getTypefaceFromWord(sTag);
		
		//if has style we apply it first
		if(typeface!=null){
			//aaply the style received
			if(view instanceof Button){
				applyTypefaceData((Button)view, typeface);
			}else if(view instanceof TextView){
				applyTypefaceData((TextView)view, typeface);
			}
		}
	}
	
	/**
	 * Get a typeface preloaded in the class, if it is not load them
	 * @param context - Context of app
	 * @param word - String name of font
	 * @return Typeface - Font with that name
	 */
	public static Typeface getPreLoadTypeface(Context context, String word){
		
		//update fonts
		StyleApp.updateFonts(context);
		
		//return the typeface
		return StyleApp.getTypefaceFromWord(word);
	}
	
	
	/**
	 * Get a font from a word
	 * @param word - String with name of the font
	 * @return Typeface equivalent or null if not found
	 */
	private static Typeface getTypefaceFromWord(String word){
		if(word.equalsIgnoreCase("formataRegular")){
			return formataRegular;
		}else if(word.equalsIgnoreCase("formataItalic")){
			return formataItalic;
		}else if(word.equalsIgnoreCase("formataMedium")){
			return formataMedium;
		}else if(word.equalsIgnoreCase("formataMediumItalic")){
			return formataMediumItalic;
		}else{
			return null;
		}
	}
	
	
	/**
	 * Apply the style to the text view
	 * @param tv - TextView to apply the style
	 * @param typeface - Typeface to set
	 */
	private static void applyTypefaceData(TextView tv, Typeface typeface){
		
		//font
		if(typeface!=null){
			tv.setTypeface(typeface);
		}
		
	}
	
	/**
	 * Apply the style to the text view
	 * @param b - Button to apply the style
	 * @param typeface - Typeface to set
	 */
	private static void applyTypefaceData(Button b, Typeface typeface){
		
		//font
		if(typeface!=null){
			b.setTypeface(typeface);
		}
		
	}
	
	
	
	//---------------- PROCESSING VIEWS -------------------

	/**
	 * Set style of a tree of views
	 * @param topView - View on the top of the tree
	 * @param assetManager - AssetManager with information about assets
	 * @param hierachyChangeListener - OnHierachyChangeListener listener to add to the view
	 */
	public static void setStyle(View topView) {
		
		if(topView==null){
			return;
		}
		
		//apply style
		StyleApp.setStyle(topView, null);
	}
	
	/**
	 * Set style of a tree of views
	 * @param topView - View on the top of the tree
	 * @param assetManager - AssetManager with information about assets
	 */
	public static void setStyle(View topView, ViewGroup.OnHierarchyChangeListener hierarchyChangeListener) {
		
		//update fonts
		updateFonts(topView.getContext());

		//process views
		processView(topView, hierarchyChangeListener);
		
	}
	
	/**
	 * Update the fonts if we would lose the reference to one of them
	 * @param context
	 */
	private static void updateFonts(Context context){
		//get the fonts if it is first time
		if (formataRegular == null || formataItalic == null || formataMedium == null || formataMediumItalic == null) {
			
			//get the assets manager
			AssetManager assetManager = context.getAssets();
			
			//get the fonts
			formataRegular = Typeface.createFromAsset(assetManager, "fonts/Flexo-Regular.ttf");
			formataItalic = Typeface.createFromAsset(assetManager, "fonts/Flexo-It.ttf");
			formataMedium = Typeface.createFromAsset(assetManager, "fonts/Flexo-Bold.ttf");
			formataMediumItalic = Typeface.createFromAsset(assetManager, "fonts/Flexo-BoldIt.ttf");
			
		}
	}
	
	/**
	 * Run over all tree of views processing the views
	 * @param v - View to process
	 */
	private static void processView(View v, ViewGroup.OnHierarchyChangeListener hierarchyChangeListener) {

		//for each child we process the view
		if(v instanceof ViewGroup){
			ViewGroup viewGroup = (ViewGroup) v;
			viewGroup.setOnHierarchyChangeListener(hierarchyChangeListener);
			int numChild = viewGroup.getChildCount();
			for(int i=0; i<numChild; i++){
				processView(viewGroup.getChildAt(i), hierarchyChangeListener);
			}
			
		//set style for each type
		}else{
			if(v instanceof View){
				applyTypeface(v);
			}
		}
	}

	//---------- ViewGroup.OnHierarchyChangeListener ----------
	
	@Override
	public void onChildViewAdded (View parent, View child) {
		//set the style to the child added
		StyleApp.setStyle(child, this);
	}

	@Override
	public void onChildViewRemoved(View parent, View child) {
		
	}

}
