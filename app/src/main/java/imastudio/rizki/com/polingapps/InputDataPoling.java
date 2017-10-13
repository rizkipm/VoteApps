package imastudio.rizki.com.polingapps;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidquery.AQuery;

import java.io.File;

import imastudio.rizki.com.polingapps.helper.AzrielHelper;
import imastudio.rizki.com.polingapps.libraryupload.FileUtility;
import imastudio.rizki.com.polingapps.libraryupload.ImageCustomize;

/**
 * Created by MAC on 9/30/17.
 */

public class InputDataPoling extends AppCompatActivity {

    ImageView imgUpload;

    protected Context c ;
    protected AQuery aQuery;
    public static final AlphaAnimation btnAnimasi = new AlphaAnimation(1F, 0.5F);
//    protected SessionManager sesi;


    private String path = "", pathItem;
    private boolean isImage = false;
    private String namaFile;
    private Bitmap bitmap = null;
    private int takeTo = 1;
    FileUtility fileUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_datapolling);

        aQuery = new AQuery(c);
        c =this;
//        sesi= new SessionManager(c);

        imgUpload = (ImageView)findViewById(R.id.upload);

//        if (sesi.getAvatar().equalsIgnoreCase("")) {
//            imgUpload.setImageResource(R.drawable.logoprofile);
//        } else {
//            aQuery.id(imgUpload).image(AzrielHelper.BASE_URL_IMAGE_PROFIL + "user/" + sesi.getAvatar());
//        }

        imgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(btnAnimasi);
                showDialogScan();
            }
        });


    }

    private void showDialogScan() {
        String[] items = {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Do something with the selection
                switch (which) {
                    case 1:
                        callgalery();
                        break;

                    case 0:
                        callCamera();
                        break;

                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void callCamera() {

        fileUtility = new FileUtility(c);
        Uri uriSavedImage = Uri.fromFile(fileUtility.getTempJpgImageFile());

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivityForResult(i, Constant.TAKE_PHOTO);
    }


    private void callgalery() {

        Intent i = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, Constant.SELECT_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == Constant.PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                updateHp();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot login", Toast.LENGTH_SHORT).show();
            }
        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageReturnedIntent) {


        switch (requestCode) {
            case Constant.TAKE_PHOTO:


                if (resultCode == Activity.RESULT_OK) {

                    File imageFile = fileUtility.getTempJpgImageFile();

                    String path = imageFile.getAbsolutePath();

                    File file = new File(path);

                    File file1  = ImageCustomize.resizeImage(file);
                    bitmap = ImageCustomize.decodeFile(file1, 200);

                    imgUpload.setImageBitmap(bitmap);
                    this.path = file1.getAbsolutePath().toString(); //path;
                    isImage = true;


                }
                break;

            case Constant.SELECT_PHOTO:
                if (requestCode == Constant.SELECT_PHOTO && resultCode == RESULT_OK
                        && null != imageReturnedIntent) {

                    Uri selectedImage = imageReturnedIntent.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    File file = new File(picturePath);
                    File file1  = ImageCustomize.resizeImage(file);

                    bitmap = ImageCustomize.decodeFile(file1, 300);
                    imgUpload.setImageBitmap(bitmap);
                    this.path = file1.getAbsolutePath().toString();


                    isImage = true;
                    AzrielHelper.log("path file" +path);
                }

                break;


        }

    }
}
