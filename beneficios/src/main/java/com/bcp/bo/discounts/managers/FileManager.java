package com.bcp.bo.discounts.managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.bcp.bo.discounts.BuildConfig;

/**
 * Add support to make file operations.
 */
public final class FileManager {
	
	/**
	 * List all files on internal storage path.
	 * @param context App context.
	 * @param path The path to list.
	 * @return Array with only the name of the files. Null for nothing.
	 */
	public String[] listInternalStoragePrivateFile(final Context context, final String path) {
		
		// PRECONDITIONS
		if (context == null) {
			throw new IllegalArgumentException("context is null");
		} else if (path == null) {
			throw new IllegalArgumentException("path is null");
		}
		
	    File dir = new File(context.getFilesDir(), path);
	    if(dir.isDirectory()) {
	    	return dir.list();
	    }		
		
		return null;
	}
	
	/**
	 * List all files on internal storage path.
	 * @param context App context.
	 * @param path The path to list.
	 * @return Array with the absolute path of the files. Null for nothing.
	 */
	public String[] listAbsolutePathInternalStoragePrivateFile(final Context context, final String path) {
		
		// PRECONDITIONS
		if (context == null) {
			throw new IllegalArgumentException("context is null");
		} else if (path == null) {
			throw new IllegalArgumentException("path is null");
		}
		
		String[] list = listInternalStoragePrivateFile(context, path);
		
		if(list != null) {
			final File dir = new File(context.getFilesDir(), path);
			
			for (int i = 0; i < list.length; i++) {
				list[i] = dir.getAbsolutePath() + File.separator + list[i];
			}
			
		}
		
		return list;
	}
	
	/**
	 * Create internal storage file. Always private to the app.
	 * @param context App context.
	 * @param inputStream The input stream to write. 
	 * @param path The path where save the info creating the directories if needed.
	 * @param name The name of the file.
	 * @throws IOException Error writing the file.
	 */
	public void createInternalStoragePrivateFile(final Context context, final InputStream inputStream, final String path, final String name) 
			throws IOException {
		
		// PRECONDITIONS
		if (context == null) {
			throw new IllegalArgumentException("context is null");
		} else if (path == null) {
			throw new IllegalArgumentException("path is null");
		} else if (inputStream == null) {
			throw new IllegalArgumentException("inputStream is null");
		} else if (name == null) {
			throw new IllegalArgumentException("name is null");
		}	
		
		
		
	    // Create a path where we will place our private file on internal
	    // storage.

	    File dir = new File(context.getFilesDir(), path);
	    // Create the dir if needed.
	    dir.mkdirs();
	    
	    File file = new File(dir, name);	    
	    OutputStream os = null;
	    try {
	        os = new FileOutputStream(file);
	        byte[] data = new byte[inputStream.available()]; // Only works with little files
	        inputStream.read(data);
	        os.write(data);
	    } finally {
	        inputStream.close();
	        if(os != null) {
	        	os.close();
	        }
	    }

	    // @LOG DEBUG
//	    if (BuildConfig.DEBUG) {
//	    	Log.d(FileManager.class.getSimpleName(), "Created file on: " + file.getAbsolutePath());
//	    }
	    
	}
	
	/**
	 * Create internal storage file. Always private to the app.
	 * @param context App context.
	 * @param base64Data The base 64 encoded string to save. 
	 * @param path The path where save the info creating the directories if needed.
	 * @param name The name of the file.
	 * @throws IOException Error writing the file.
	 */
	public void createInternalStoragePrivateFile(final Context context,
			final String base64Data, final String path, final String name) throws IOException {

		// PRECONDITIONS
		if (context == null) {
			throw new IllegalArgumentException("context is null");
		} else if (path == null) {
			throw new IllegalArgumentException("path is null");
		} else if (base64Data == null) {
			throw new IllegalArgumentException("base64Data is null");
		} else if (name == null) {
			throw new IllegalArgumentException("name is null");
		}	
		
		
	    File dir = new File(context.getFilesDir(), path);
	    // Create the dir if needed.
	    dir.mkdirs();
	    
	    File file = new File(dir, name);
	    OutputStream os = null;
	    try {
	        os = new FileOutputStream(file);
	        os.write(Base64.decode(base64Data.getBytes(), Base64.DEFAULT));
	    } finally {
	        if(os != null) {
	        	os.close();
	        }
	    }
		
	    // @LOG DEBUG
//	    if (BuildConfig.DEBUG) {
//	    	Log.d(FileManager.class.getSimpleName(), "Created file on: " + file.getAbsolutePath());
//	    }
	    
	}
	
	/**
	 * Delete internal file.
	 * @param context App context. 
	 * @param path The path where delete the info.
	 * @param name The name of the file.
	 * @return True if the file was deleted, false otherwise.
	 */
	public boolean deleteInternalStoragePrivateFile(final Context context, final String path, final String name) {
		
		// PRECONDITIONS
		if (context == null) {
			throw new IllegalArgumentException("context is null");
		} else if (path == null) {
			throw new IllegalArgumentException("path is null");
		} else if (name == null) {
			throw new IllegalArgumentException("name is null");
		}	
		
		
	    // Get path for the file on external storage.
		boolean exit = false;
		
		File file = new File(context.getFilesDir(), name);
	    if (file != null) {
	    	exit = file.delete();
	    }
		
	    // @LOG DEBUG
//	    if (BuildConfig.DEBUG) {
//	    	Log.d(FileManager.class.getSimpleName(), "Delete file '" + file.getAbsolutePath() + "': " + exit);
//	    }
	    
		return exit;
		
	}

	/**
	 * Check if exist internal storage file.
	 * @param context App context. 
	 * @param path The path where check the info.
	 * @param name The name of the file.
	 * @return True if the file was deleted, false otherwise.
	 */
	public boolean hasInternalStoragePrivateFile(final Context context, final String path, final String name) {
		
		// PRECONDITIONS
		if (context == null) {
			throw new IllegalArgumentException("context is null");
		} else if (path == null) {
			throw new IllegalArgumentException("path is null");
		} else if (name == null) {
			throw new IllegalArgumentException("name is null");
		}	
		
		
	    // Get path for the file on external storage.
		boolean exists = false;
		
		File file = new File(context.getFilesDir(), name);	    
	    if (file != null) {
	        exists = file.exists();
	    }
		    
		
	    return exists;
	}
	
	/**
	 * Get the base internal absolute path.
	 * @param context App context.
	 * @return The path.
	 */
	public String getInternalStorageBasePath(final Context context) {
		return (context.getFilesDir()).getAbsolutePath() + File.separator;
	}
	
	/**
	 * Check if external storage is available.
	 * @return True if is available, false otherwise.
	 */
	public boolean checkExternalStorageState() {	
		boolean mounted = false;	
		final String state = Environment.getExternalStorageState();
		
		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    // We can read and write the media
			mounted = true;
		} else if (BuildConfig.DEBUG) {

		}
		
		return mounted;
	}
	
	/**
	 * List all files on external storage path.
	 * @param context App context.
	 * @param path The path to list.
	 * @return Array with only the name of the files. Null for nothing.
	 */
	public String[] listExternalStoragePrivateFile(final Context context, final String path) {
		
		// PRECONDITIONS
		if (context == null) {
			throw new IllegalArgumentException("context is null");
		} else if (path == null) {
			throw new IllegalArgumentException("path is null");
		}
		
		
		if(checkExternalStorageState()) {
		    File dir = new File(context.getExternalFilesDir(null), path);
		    if(dir.isDirectory()) {
		    	return dir.list();
		    }
		}
		
		return null;
		
	}
	
	/**
	 * List all files on external storage path.
	 * @param context App context.
	 * @param path The path to list.
	 * @return Array with the absolute path of the files. Null for nothing.
	 */
	public String[] listAbsolutePathExternalStoragePrivateFile(final Context context, final String path) {
		
		// PRECONDITIONS
		if (context == null) {
			throw new IllegalArgumentException("context is null");
		} else if (path == null) {
			throw new IllegalArgumentException("path is null");
		}
		
		String[] list = listExternalStoragePrivateFile(context, path);
		
		if(list != null) {
			final File dir = new File(context.getExternalFilesDir(null), path);
			
			for (int i = 0; i < list.length; i++) {
				list[i] = dir.getAbsolutePath() + File.separator + list[i];
			}
			
		}
		
		return list;
		
	}
	
	/**
	 * Create internal storage file. Always private to the app.
	 * @param context App context.
	 * @param inputStream The input stream to write. 
	 * @param path The path where save the info creating the directories if needed.
	 * @param name The name of the file.
	 * @return True if the file was saved, false if external storage is not available.
	 * @throws IOException Error writing the file.
	 */
	public boolean createExternalStoragePrivateFile(final Context context, 
			final InputStream inputStream, final String path, final String name) 
			throws IOException {
		
		// PRECONDITIONS
		if (context == null) {
			throw new IllegalArgumentException("context is null");
		} else if (path == null) {
			throw new IllegalArgumentException("path is null");
		} else if (inputStream == null) {
			throw new IllegalArgumentException("inputStream is null");
		} else if (name == null) {
			throw new IllegalArgumentException("name is null");
		}	
		
		
		
	    // Create a path where we will place our private file on external
	    // storage.
		boolean exit = false;
		
		if(checkExternalStorageState()) {
		    File dir = new File(context.getExternalFilesDir(null), path);
		    // Create the dir if needed.
		    dir.mkdirs();
		    
		    File file = new File(dir, name);
		    OutputStream os = null;
		    try {
		        os = new FileOutputStream(file);
		        byte[] data = new byte[inputStream.available()]; // Only works with little files
		        inputStream.read(data);
		        os.write(data);
		        exit = true;	
		    } finally {
		        inputStream.close();
		        if(os != null) {
		        	os.close();
		        }
		    }
		    
		    // @LOG DEBUG
//		    if (BuildConfig.DEBUG) {
//		    	Log.d(FileManager.class.getSimpleName(), "Created file on: " + file.getAbsolutePath());
//		    }
		    
		}
		
		return exit;
	}

	/**
	 * Create external storage file. Always private to the app.
	 * @param context App context.
	 * @param base64Data The base 64 encoded string to save. 
	 * @param path The path where save the info creating the directories if needed.
	 * @param name The name of the file.
	 * @return True if the file was saved, false if external is not available.
	 * @throws IOException Error writing the file.
	 */
	public boolean createExternalStoragePrivateFile(final Context context, final String base64Data, 
			final String path, final String name) throws IOException {

		// PRECONDITIONS
		if (context == null) {
			throw new IllegalArgumentException("context is null");
		} else if (path == null) {
			throw new IllegalArgumentException("path is null");
		} else if (base64Data == null) {
			throw new IllegalArgumentException("base64Data is null");
		} else if (name == null) {
			throw new IllegalArgumentException("name is null");
		}	
		
		
		boolean exit = false;
		
		if(checkExternalStorageState()) {
		    File dir = new File(context.getExternalFilesDir(null), path);
		    // Create the dir if needed.
		    dir.mkdirs();
		    
		    File file = new File(dir, name);
		    OutputStream os = null;
		    try {
		        os = new FileOutputStream(file);
		        os.write(Base64.decode(base64Data.getBytes(), Base64.DEFAULT));
		        exit = true;	
		    } finally {
		        if(os != null) {
		        	os.close();
		        }
		    }
		    
		    // @LOG DEBUG
//		    if (BuildConfig.DEBUG) {
//		    	Log.d(FileManager.class.getSimpleName(), "Created file on: " + file.getAbsolutePath());
//		    }
		    
		}
		
		return exit;
		
	}
	
	/**
	 * Delete external file.
	 * @param context App context. 
	 * @param path The path where delete the info.
	 * @param name The name of the file.
	 * @return True if the file was deleted, false otherwise.
	 */
	public boolean deleteExternalStoragePrivateFile(final Context context, final String path, final String name) {
		
		// PRECONDITIONS
		if (context == null) {
			throw new IllegalArgumentException("context is null");
		} else if (path == null) {
			throw new IllegalArgumentException("path is null");
		} else if (name == null) {
			throw new IllegalArgumentException("name is null");
		}	
		
		
	    // Get path for the file on external storage.
		boolean exit = false;
		
		if(checkExternalStorageState()) {
		    File dir = new File(context.getExternalFilesDir(null), path);
		    File file = new File(dir, name);
		    
		    if (file != null) {
		        exit = file.delete();
		    }
		    
		    // @LOG DEBUG
//		    if (BuildConfig.DEBUG) {
//		    	Log.d(FileManager.class.getSimpleName(), "Delete file '" + file.getAbsolutePath() + "': " + exit);
//		    }
		    
		}
	    
		return exit;
		
	}

	/**
	 * Check if exist external storage file.
	 * @param context App context. 
	 * @param path The path where check the info.
	 * @param name The name of the file.
	 * @return True if the file was deleted, false otherwise.
	 */
	public boolean hasExternalStoragePrivateFile(final Context context, final String path, final String name) {
		
		// PRECONDITIONS
		if (context == null) {
			throw new IllegalArgumentException("context is null");
		} else if (path == null) {
			throw new IllegalArgumentException("path is null");
		} else if (name == null) {
			throw new IllegalArgumentException("name is null");
		}	
		
		
	    // Get path for the file on external storage.
		boolean exists = false;
		
		if(checkExternalStorageState()) {
		    File dir = new File(context.getExternalFilesDir(null), path);
		    File file = new File(dir, name);
		    
		    if (file != null) {
		        exists = file.exists();
		    }
		    
		}
		
	    return exists;
	}
	
	/**
	 * Get the base external absolute path.
	 * @param context App context.
	 * @return The path, null if external storage is not available.
	 */
	public String getExternalStorageBasePath(final Context context) {
		
		String exit = null;
		
		if(checkExternalStorageState()) {
			exit = (context.getExternalFilesDir(null)).getAbsolutePath() + File.separator;
		} 
		
		return exit;
		
	}
	
	/**
	 * Get a file as input stream.
	 * @param absolutePath The absolute path of the file.
	 * @return The file input stream or null if it is not found or has an error.
	 */
	public static InputStream getFile(String absolutePath) {
		try {
			return new FileInputStream(new File(absolutePath));
		} catch (FileNotFoundException e) {
			return null;
		}	
	}
	
	/**
	 * Check if a file exist.
	 * @param absolutePath The absolute path of the file.
	 * @return True if the file exist, false otherwise.
	 */
	public static boolean hasFile(String absolutePath) {
		
	    // Get path for the file on external storage.
		boolean exists = false;
		
	    File file = new File(absolutePath);
	    
	    if (file != null) {
	        exists = file.exists();
	    }
		    
		
	    return exists;	
	}
	
	/**
	 * Save bitmap image to file with JPEG format and with no quality lost.
	 * @param image The bitmap image to save.
	 * @param format Format of the image.
	 * @param absolutePath The absolute path of the file.
	 * @return True if was saved, false otherwise.
	 */
	public static boolean saveBitmap(Bitmap image, Bitmap.CompressFormat format, String absolutePath) {

		FileOutputStream out = null;
		boolean exit = false;
		try {
			out = new FileOutputStream(absolutePath);
		    image.compress(format, 100, out); 
		    
		    exit = true;
		} catch (FileNotFoundException e) {
			Log.e(FileManager.class.getSimpleName(), e.toString());
		} finally {
			if(out != null) {
			    try {
					out.flush();
					out.close();
					
					// @LOG DEBUG
//				    if (BuildConfig.DEBUG) {
//				    	Log.d(FileManager.class.getSimpleName(), "Save bitmap: " + absolutePath);
//				    }
				    
				} catch (IOException e) { 
					Log.e(FileManager.class.getSimpleName(), e.toString());
				}
			}
		}
		
		return exit;
	
	}
	
	/**
	 * Delete a list of absolute paths.
	 * @param paths The absolute paths of each file.
	 * @return The success true of each file in the same order as the list.
	 */
	public static List<Boolean> deleteFiles(final List<String> paths) {
		ArrayList<Boolean> list = new ArrayList<Boolean>(paths.size());
		
		File f;
		for (int i = 0; i < paths.size(); i++) {
			
		    // A File object to represent the filename
		    f = new File(paths.get(i));
		    
		    // Make sure the file or directory exists and isn't write protected
		    if (f.exists() && f.canWrite() && f.isFile()) {
			    // Attempt to delete it
			    list.add(f.delete());
		    } else {
		    	list.add(false);
		    }

		}
	    
		return list;
	}

	public String createExternalStoragePrivateFile(
			Context context, byte[] data,
			String path, String name) {
				
		// PRECONDITIONS
		if (context == null) {
			throw new IllegalArgumentException("context is null");
		} else if (path == null) {
			throw new IllegalArgumentException("path is null");
		} else if (data == null) {
			throw new IllegalArgumentException("data is null");
		} else if (name == null) {
			throw new IllegalArgumentException("name is null");
		}	
		
		String absolutePath = "";
		
		if(checkExternalStorageState()) {
		    File dir = new File(context.getExternalFilesDir(null), path);
		    // Create the dir if needed.
		    dir.mkdirs();
		    
		    File file = new File(dir, name);
		    OutputStream os = null;

	        try {
				os = new FileOutputStream(file);
		        os.write(data);
		        os.flush();
		        os.close();		
		        
		        absolutePath = file.getAbsolutePath();
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			}

		}
		
		return absolutePath;
		
	}

	public String createInternalStoragePrivateFile(
			Context context, byte[] data,
			String path, String name) {
		
		// PRECONDITIONS
		if (context == null) {
			throw new IllegalArgumentException("context is null");
		} else if (path == null) {
			throw new IllegalArgumentException("path is null");
		} else if (data == null) {
			throw new IllegalArgumentException("data is null");
		} else if (name == null) {
			throw new IllegalArgumentException("name is null");
		}

		String absolutePath = "";
		
	    // Create a path where we will place our private file on internal
	    // storage.	
	    File dir = new File(context.getFilesDir(), path);
	    // Create the dir if needed.
	    dir.mkdirs();
	    
	    File file = new File(dir, name);	    
	    OutputStream os = null;
	    try {
	        os = new FileOutputStream(file);
	        os.write(data);
	        os.flush();
	        os.close();
	        
	        absolutePath = dir.getAbsolutePath();
	        
	        //check absolute path has added the name of the file
	        if(absolutePath.length()<=name.length() || !absolutePath.substring(absolutePath.length()-name.length()).equalsIgnoreCase(name)){
				absolutePath += "/" + name;
			}
	        
	    } catch (FileNotFoundException e) {
		} catch (IOException e) {
		} 	    	    
	    
	    return absolutePath;
	    
	}
	
}
