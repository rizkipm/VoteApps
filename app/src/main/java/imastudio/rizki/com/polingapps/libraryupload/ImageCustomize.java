package imastudio.rizki.com.polingapps.libraryupload;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import imastudio.rizki.com.polingapps.helper.AzrielHelper;


public class ImageCustomize {
	
	public static Bitmap resizeBitmap(File f, int height) {
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);
            
            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=height;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }
            
            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.outHeight = height;
            
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }

    

	public static Bitmap decodeFile(File f , int size) {
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);
            
            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=size;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true) {
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }
            
            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }

    public static File resizeImage(File f , int size) {
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=size;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true) {
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }

            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o2);


            return f;


        } catch (FileNotFoundException e) {}
        return null;
    }

    public static File resizeImage(File f ) {
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE= 200;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true) {
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }

            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, o2);

            OutputStream outStream = null;
            String namaFilex = f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf("/") + 1);
            String ektensi = AzrielHelper.getExtension(f);
            String filename = AzrielHelper.tglJamSekarangFile() + "." + ektensi;
            AzrielHelper.pre("nama file " + filename);
            File sdCardDirectory = new File(Environment
                    .getExternalStorageDirectory() + File.separator + "projectMonitoring");
            sdCardDirectory.mkdirs();
            File file = new File(sdCardDirectory, filename);

//            if (file.exists()) {
//                file.delete();
//                file = new File(Environment
//                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename + ".png");
//                Log.e("file exist", "" + file + ",Bitmap= " + filename);
//            }
            try {


                outStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 40, outStream);
                outStream.flush();
                outStream.close();
                bitmap.recycle();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("file", "" + file.toString());
            return file;


        } catch (FileNotFoundException e) {}
        return null;
    }
}
