package com.bcp.bo.discounts.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import com.bcp.bo.discounts.R;
import com.bcp.bo.discounts.general.ImageUtils;
import com.bcp.bo.discounts.views.CustomImageView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

public class ImageDownloader{
	private final static String ID_BITMAP="ID";
	public final static String MASK_SUFIX=".smask";
	private Activity activity;
	private MemoryCache memoryCache=new MemoryCache();
	private FileCache fileCache;
	public ImageDownloader(Activity activity){
		this.activity=activity;
		fileCache=new FileCache(activity);
	}
	/*public void GetIcono(ImageView imageView,ProgressBar progressBar_Estado,Producto producto){
        Bitmap bitmap=memoryCache.get(SIZEICON+producto.Imagen);
        if(bitmap!=null){
        	progressBar_Estado.setVisibility(View.INVISIBLE);
        	imageView.setImageBitmap(bitmap);
        	Log.d("LOADER","ICONO OK");
        }
        else{
        	AsyncTaskLoadIcono task=new AsyncTaskLoadIcono(producto,imageView,progressBar_Estado);
        	task.execute(producto);
        	Log.d("LOADER","ICONO NULL:"+producto.Imagen);
        }
	}*/
	public Bitmap getBitmap(String imagenNombre){
		return memoryCache.get(imagenNombre+MASK_SUFIX);
	}
	public Drawable getDrawable(String imagenNombre){
		Bitmap bitmap=memoryCache.get(imagenNombre+MASK_SUFIX);
		if(bitmap!=null){
			return new BitmapDrawable(bitmap);
		}
		return null;
	}
	public Bitmap LoadBitmap(int id){
		Bitmap bitmap=memoryCache.get(ID_BITMAP+id);
		if(bitmap!=null){
			Log.d("ImageManager","IN CACHE");
			return bitmap;
		}
		else{
			Log.d("ImageManager","NO IN CACHE");
			bitmap=BitmapFactory.decodeResource(activity.getResources(),id);
			memoryCache.put(ID_BITMAP+id,bitmap);
			return bitmap;
		}
	}
	public void LoadImageList(CustomImageView customImageView,String imagenNombre){
		if(imagenNombre.equals("")){
			return;
		}
		customImageView.setTag(imagenNombre);
		Bitmap bitmap=memoryCache.get(imagenNombre);
		if(bitmap!=null){
			customImageView.setDrawable(new BitmapDrawable(bitmap));//setImageBitmap(bitmap);
		}
		else{
			new AsyncTaskCargarListImagen(imagenNombre,customImageView).execute();
		}
	}
	public void LoadImageTicket(CustomImageView customImageView,String imagenNombre){
		if(imagenNombre.equals("")){
			return;
		}
		customImageView.setTag(imagenNombre);
		Bitmap bitmap=memoryCache.get(imagenNombre+MASK_SUFIX);
		if(bitmap!=null){
			customImageView.setDrawable(new BitmapDrawable(bitmap));//setImageBitmap(bitmap);
		}
		else{
			new AsyncTaskCargarTicketImagen(imagenNombre,customImageView).execute();
		}
	}
	/*public void CargarTicketImagen(ImageView imageView,String imagenNombre){
		if(imageView!=null){
			//imageView.setImageResource(R.drawable.ic_action_download);
		}
		imageView.setTag(imagenNombre);
		//customImageView.setScaleType(ScaleType.CENTER);
		Bitmap bitmap=memoryCache.get(imagenNombre+MASK_SUFIX);
		if(bitmap!=null){
			imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));;//setImageBitmap(bitmap);
			//imageView.setScaleType(ScaleType.FIT_XY);
		}
		else{
			new AsyncTaskCargarImageTicket(imagenNombre,imageView).execute();
		}
	}*/
	/*public void Get(ImageView imageView,ProgressBar progressBar_Estado,String imageUrl,long size){
		final int dimen=(int)(size*SCALE);
		imageView.setTag(imageUrl);
        Bitmap bitmap=memoryCache.get(imageUrl+String.valueOf(dimen));
        Log.d("IMAGEN INICIAR",imageUrl+String.valueOf(dimen));
        if(imageView!=null)imageView.setImageResource(R.drawable.ic_action_download);
        if(bitmap!=null){
        	if(progressBar_Estado!=null)progressBar_Estado.setVisibility(View.INVISIBLE);
        	imageView.setImageBitmap(bitmap);
        	Log.d("LOADER","ICONO OK");
        }
        else{
        	AsyncTaskLoad task=new AsyncTaskLoad(imageView,progressBar_Estado,dimen);
        	task.execute(imageUrl);
        	Log.d("LOADER","ICONO NULL:"+imageUrl);
        }
	}*/
	/*public void GetIcono(ImageView imageView,ProgressBar progressBar_Estado,String imageUrl){
		String path=imageUrl;
		imageView.setTag(path);
        Bitmap bitmap=memoryCache.get(path);
        if(imageView!=null)imageView.setImageResource(R.drawable.ic_action_download);
        if(bitmap!=null){
        	if(progressBar_Estado!=null)progressBar_Estado.setVisibility(View.INVISIBLE);
        	imageView.setImageBitmap(bitmap);
        	//Log.d("LOADER","ICONO OK");
        }
        else{
        	AsyncTaskLoadIcono task=new AsyncTaskLoadIcono(imageView,progressBar_Estado);
        	task.execute(path);
        	//Log.d("LOADER","ICONO NULL:"+path);
        }
	}*/
	/*public void GetPhoto(ImageView imageView,String imageUrl){
		String path=imageUrl;
		imageView.setTag(path);
        Bitmap bitmap=memoryCache.get(path);
        if(bitmap!=null){
        	imageView.setImageBitmap(bitmap);
        	//Log.d("LOADER","ICONO OK");
        }
        else{
        	AsyncTaskLoadPhoto task=new AsyncTaskLoadPhoto(imageView);
        	task.execute(path);
        	//Log.d("LOADER","ICONO NULL:"+path);
        }
	}*/
	/*public void GetImagen(ImageView imageView,String url){
        Bitmap bitmap=memoryCache.get(url);
        if(bitmap!=null){
        	imageView.setImageBitmap(bitmap);
        	Log.d("LOADER","IMAGEN OK");
        }
        else{
        	AsyncTaskLoadPhoto task=new AsyncTaskLoadPhoto(url,imageView);
        	task.execute(url);
        	Log.d("LOADER","IMAGEN NULL:"+url);
        }
	}*/
	private Bitmap decodeFile(File f){
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
            int scale=1;
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }
	private class Resultado{
    	public Bitmap bitmap;
    	public String path;
    	public Resultado(Bitmap bitmap,String path){
    		this.bitmap=bitmap;
    		this.path=path;
    	}
    }
	//LOADER
	private class AsyncTaskCargarListImagen extends AsyncTask<Void,Void,Resultado>{
		private String imagenNombre;
		private final WeakReference<CustomImageView> imageViewReference;

		public AsyncTaskCargarListImagen(String imagenNombre,CustomImageView customImageView){
			this.imagenNombre=imagenNombre;
			imageViewReference = new WeakReference<CustomImageView>(customImageView);
		}
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }
        protected Resultado doInBackground(Void... params){
			File file=fileCache.getFile(imagenNombre);
    		Bitmap bitmap = decodeFile(file);
            if(bitmap!=null){
            	return new Resultado(bitmap,imagenNombre);
            }
            try {
                bitmap=null;
                URL imageUrl=new URL(imagenNombre);
                HttpURLConnection conn=(HttpURLConnection)imageUrl.openConnection();
                conn.setConnectTimeout(30000);
                conn.setReadTimeout(30000);
                conn.setInstanceFollowRedirects(true);
                InputStream is=conn.getInputStream();
                OutputStream os=new FileOutputStream(file);
                CopyStream(is,os);
                os.close();
                bitmap=decodeFile(file);
                return new Resultado(bitmap,imagenNombre);
            } catch (Throwable ex){
               ex.printStackTrace();
               if(ex instanceof OutOfMemoryError)
                   memoryCache.clear();
               return new Resultado(bitmap,imagenNombre);
            }
        }
        protected void onPostExecute(Resultado resultado){
        	if(!isCancelled() & resultado.bitmap!=null){
        		//Bitmap bitmapMask=ImageUtils.generateDrawableMask(activity.getResources(),new BitmapDrawable(resultado.bitmap), activity.getResources().getDrawable(R.drawable.dsc_detail_ticket_top_mask), Color.argb(255, 58, 233, 28));
    			memoryCache.put(resultado.path,resultado.bitmap);
    			//memoryCache.put(resultado.path+MASK_SUFIX,bitmapMask);
        		CustomImageView imageView=imageViewReference.get();
    			if (imageView!=null){//IMAGEVIEW-ENCONTRADO
    				if(imageView.getTag()!=null){
    					if(imageView.getTag().toString()==resultado.path){
	    					imageView.setDrawable(new BitmapDrawable(resultado.bitmap));
	    					
	    					//imageView.setScaleType(ScaleType.FIT_XY);
    					}
					}
    			}
        	}
        	else{
        		CustomImageView imageView=imageViewReference.get();
    			if (imageView!=null){//IMAGEVIEW-ENCONTRADO
    				if(imageView.getTag()!=null){
    					if(imageView.getTag().toString()==resultado.path){
	    					//imageView.setImageResource(R.drawable.ic_action_error);
	    					//imageView.setScaleType(ScaleType.CENTER);
    					}
					}
    			}
        	}
        }
    }
	private class AsyncTaskCargarTicketImagen extends AsyncTask<Void,Void,Resultado>{
		private String imagenNombre;
		private final WeakReference<CustomImageView> imageViewReference;

		public AsyncTaskCargarTicketImagen(String imagenNombre,CustomImageView customImageView){
			this.imagenNombre=imagenNombre;
			imageViewReference = new WeakReference<CustomImageView>(customImageView);
		}
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }
        protected Resultado doInBackground(Void... params){
			File file=fileCache.getFile(imagenNombre);
    		Bitmap bitmap = decodeFile(file);
            if(bitmap!=null){
            	return new Resultado(bitmap,imagenNombre);
            }
            try {
                bitmap=null;
                URL imageUrl=new URL(imagenNombre);
                HttpURLConnection conn=(HttpURLConnection)imageUrl.openConnection();
                conn.setConnectTimeout(30000);
                conn.setReadTimeout(30000);
                conn.setInstanceFollowRedirects(true);
                InputStream is=conn.getInputStream();
                OutputStream os=new FileOutputStream(file);
                CopyStream(is,os);
                os.close();
                bitmap=decodeFile(file);
                return new Resultado(bitmap,imagenNombre);
            } catch (Throwable ex){
               ex.printStackTrace();
               if(ex instanceof OutOfMemoryError)
                   memoryCache.clear();
               return new Resultado(bitmap,imagenNombre);
            }
        }
        protected void onPostExecute(Resultado resultado){
        	if(!isCancelled() & resultado.bitmap!=null){
    			Bitmap bitmapMask=ImageUtils.generateDrawableMask(activity.getResources(),new BitmapDrawable(resultado.bitmap), activity.getResources().getDrawable(R.drawable.dsc_detail_ticket_top_mask), Color.argb(255, 58, 233, 28));
    			//memoryCache.put(resultado.path,resultado.bitmap);
    			memoryCache.put(resultado.path+MASK_SUFIX,bitmapMask);
        		CustomImageView imageView=imageViewReference.get();
    			if (imageView!=null){//IMAGEVIEW-ENCONTRADO
    				if(imageView.getTag()!=null){
    					if(imageView.getTag().toString()==resultado.path){
	    					imageView.setDrawable(new BitmapDrawable(bitmapMask));
	    					
	    					//imageView.setScaleType(ScaleType.FIT_XY);
    					}
					}
    			}
        	}
        	else{
        		CustomImageView imageView=imageViewReference.get();
    			if (imageView!=null){//IMAGEVIEW-ENCONTRADO
    				if(imageView.getTag()!=null){
    					if(imageView.getTag().toString()==resultado.path){
	    					//imageView.setImageResource(R.drawable.ic_action_error);
	    					//imageView.setScaleType(ScaleType.CENTER);
    					}
					}
    			}
        	}
        }
    }
	public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
}
