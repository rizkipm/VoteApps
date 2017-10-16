package imastudio.rizki.com.polingapps;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import imastudio.rizki.com.polingapps.helper.PolingHelper;
import imastudio.rizki.com.polingapps.libraryupload.FileUtility;
import imastudio.rizki.com.polingapps.libraryupload.ImageCustomize;

/**
 * Created by MAC on 9/30/17.
 */

public class InputDataPoling extends AppCompatActivity {

    ImageView imgUpload;

;
    public static final AlphaAnimation btnAnimasi = new AlphaAnimation(1F, 0.5F);
//    protected SessionManager sesi;




    private int takeTo = 1;

    EditText noTps;

    Button btnSubmit ;


    Context c = this;
    public static final int TAKE_PHOTO = 354;
    public static final int SELECT_PHOTO = 1;
    private String path = "";
    private boolean isImage = false;
    private String namaFile;
    private Bitmap bitmap = null;
    private int fromWO = 1;
    protected AQuery aQuery;


    String picturePath;

    FileUtility fileUtility;



    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_datapolling);

        aQuery = new AQuery(c);

//        sesi= new SessionManager(c);
        noTps = (EditText)findViewById(R.id.notps);

        imgUpload = (ImageView)findViewById(R.id.upload);

//        if (sesi.getAvatar().equalsIgnoreCase("")) {
//            imgUpload.setImageResource(R.drawable.logoprofile);
//        } else {
//            aQuery.id(imgUpload).image(PolingHelper.BASE_URL_IMAGE_PROFIL + "user/" + sesi.getAvatar());
//        }

        imgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(btnAnimasi);
                showDialogScan();
            }
        });

        btnSubmit = (Button)findViewById(R.id.buttonsubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpanAction();

            }
        });




    }

    private void showDialogScan() {
        String[] items = {"Kamera", "Album Foto"};

        new MaterialDialog.Builder(this)
                .title("Pilih sumber gambar ")
                .items(items)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {
                            case 1:
                                callgalery();
                                break;

                            case 0:
                                callCamera();
                                break;

                        }
                    }
                })
                .show();
    }

    public void callCamera() {

        fileUtility = new FileUtility(c);
        Uri uriSavedImage = Uri.fromFile(fileUtility.getTempJpgImageFile());

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivityForResult(i, TAKE_PHOTO);


    }

    protected void callgalery() {
//        Intent i = new Intent(Intent.ACTION_PICK,
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        startActivityForResult(i, SELECT_PHOTO);
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageReturnedIntent) {


        switch (requestCode) {
            case TAKE_PHOTO:


                if (resultCode == Activity.RESULT_OK) {

                    File imageFile = fileUtility.getTempJpgImageFile();

                    String path = imageFile.getAbsolutePath();

                    File file = new File(path);
                    File file1  = PolingHelper.resizeImage(file);
                    bitmap = ImageCustomize.decodeFile(file, 300);
                    imgUpload.setImageBitmap(bitmap);

                    this.path = file1.getAbsolutePath().toString(); //path;
                    isImage = true;
                }
                break;

            case SELECT_PHOTO:

//                if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != imageReturnedIntent) {
//                    Uri selectedImage = imageReturnedIntent.getData();
//                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
//                    Cursor cursor = getContentResolver().query(selectedImage,
//                            filePathColumn, null, null, null);
//                    cursor.moveToFirst();
//                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                    picturePath = cursor.getString(columnIndex);
//                    cursor.close();
//                    ImageView imageView = (ImageView) findViewById(R.id.fotoTrans);
//                    imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//                    Toast.makeText(getApplicationContext(), "Path : " + picturePath, Toast.LENGTH_SHORT).show();

                if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK
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
                    File file1  = PolingHelper.resizeImage(file);
                    bitmap = ImageCustomize.decodeFile(file, 300);
                    imgUpload.setImageBitmap(bitmap);
                    this.path = file1.getAbsolutePath().toString(); //path;

                    isImage = true;

                    PolingHelper.log("path file : " + path);
                }

                break;


        }

    }

    private void simpanAction() {

        boolean cancel = false;
        View focusView = null;


        if (cancel) {
            focusView.requestFocus();
        } else {
            //check jam inputnya

            String url = PolingHelper.BASE_URL + "upload_gambar";

            Map<String, Object> params = new HashMap<String, Object>();
//            params.put("f_token", sesi.getToken());
//            params.put("f_device", AzrielHelper.getDeviceUUID(getApplicationContext()));

            params.put("nama_dok", noTps.getText().toString());

            params.put("foto_dok", path);
            if (isImage) {
                File f = new File(path);
                String ektensi = PolingHelper.getExtension(f);
                PolingHelper.pre("ektensi " + ektensi);
                namaFile = path.substring(path.lastIndexOf("/") + 1);
                params.put("foto_dok", namaFile);
                params.put("userfile", f);
                params.put("f_isImage", "true");
            } else {
                namaFile = "";
                params.put("foto_dok", "");
                params.put("f_isImage", "false");
            }


            ProgressDialog dialog = new ProgressDialog(c);
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.setInverseBackgroundForced(false);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setMessage("Loading...");
            try {
                PolingHelper.log("url : " + url + ", param :" + params.toString());
                aQuery.progress(dialog).ajax(url, params, String.class,
                        new AjaxCallback<String>() {

                            @Override
                            public void callback(String url, String jsonx,
                                                 AjaxStatus status) {

                                try {

                                    PolingHelper.log("log aquery respon "
                                            + jsonx);
                                    JSONObject json = new JSONObject(jsonx);
                                    String result = json.getString("result");
                                    String pesan = json.getString("msg");

                                    if (result.equalsIgnoreCase("true")) {
                                        PolingHelper.pesan(c, pesan);
                                        Intent a = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(a);
                                        onBackPressed();
                                    } else {
                                        PolingHelper.pesan(c, pesan);

                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();


                                    PolingHelper.pesan(c,
                                            "Error parsing data, please try again.");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    PolingHelper.pesan(c,
                                            "Error submit upload photo , silahkan coba lagi.");
                                }

                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
                PolingHelper.pesan(c,
                        "Error get task, please try again.");
            }


        }

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_galeri, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.show_galeri) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
