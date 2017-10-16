package imastudio.rizki.com.polingapps.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import imastudio.rizki.com.polingapps.MainActivity;
import imastudio.rizki.com.polingapps.R;
import imastudio.rizki.com.polingapps.helper.No_Internet;
import imastudio.rizki.com.polingapps.helper.PolingHelper;
import imastudio.rizki.com.polingapps.model.ModelDokumentasi;

import static com.androidquery.util.AQUtility.getContext;

public class ActivityGaleri extends AppCompatActivity {

    private ListView lvData;
    private ArrayList<ModelDokumentasi> data;
    String idWilayah, status_ops;
    private GaleriAdapter adapter;
    Context context = this;
    TextView txtPoint;
    String idDok;
    String nPoint;
    int newPoint;



    AQuery aq;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeri);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        data = new ArrayList<>();
        lvData = (ListView) findViewById(R.id.listGaleri);


        aq = new AQuery(getApplicationContext());
        if (!PolingHelper.isOnline(getApplicationContext())) {
            startActivity(new Intent(getApplicationContext(), No_Internet.class));
            finish();

        } else {

            getDataGaleri();

        }


    }

    private void getDataGaleri() {
        data.clear();
        //ambil data dari server
        String url = PolingHelper.BASE_URL + "get_galeri";
        Map<String, String> parampa = new HashMap<>();


        //menambahkan progres dialog loading
        ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setInverseBackgroundForced(false);
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setMessage("Loading..");
        try {
            //mencari url dan parameter yang dikirimkan
            PolingHelper.pre("Url : " + url + ", params " + parampa.toString());
            //koneksi ke server meggunakan aquery
            aq.progress(progressDialog).ajax(url, parampa, String.class,
                    new AjaxCallback<String>() {
                        @Override
                        public void callback(String url, String hasil, AjaxStatus status) {
                            //cek apakah hasilnya null atau tidak
                            if (hasil != null) {
                                PolingHelper.pre("Respon : " + hasil);
                                //merubah string menjadi json
                                try {
                                    JSONObject json = new JSONObject(hasil);
                                    String result = json.getString("result");
                                    String pesan = json.getString("msg");
                                    // NurHelper.pesan(getActivity(), pesan);
                                    if (result.equalsIgnoreCase("true")) {
                                        JSONArray jsonArray = json.getJSONArray("data");
                                        for (int a = 0; a < jsonArray.length(); a++) {
                                            JSONObject object = jsonArray.getJSONObject(a);
                                            //ambil data perbooking dan masukkan ke kelas object model
                                            ModelDokumentasi b = new ModelDokumentasi();
                                            b.setNama_dok(object.getString("nama_dok"));
                                            b.setFoto_dok(object.getString("foto_dok"));
                                            b.setPoint_dok(object.getString("point_dok"));
                                            b.setId_dok(object.getString("id_dok"));



                                            //memasukkan data kedalam model booking
                                            data.add(b);
                                            //masukkan data arraylist kedalam custom adapter
                                            adapter = new GaleriAdapter(ActivityGaleri.this, data);
                                            lvData.setAdapter(adapter);

                                        }
                                    } else {
                                        //  NurHelper.pesan(getActivity(), pesan);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    // NurHelper.pesan(getActivity(), "Error parsing data");
                                }
                            }
                        }
                    });

        } catch (Exception e) {
            //  NurHelper.pesan(getActivity(), "Error get data");
            e.printStackTrace();
        }
    }

    private class GaleriAdapter extends BaseAdapter {
        private Activity c;
        private ArrayList<ModelDokumentasi> datas;
        private LayoutInflater inflater = null;

        public GaleriAdapter(Activity c, ArrayList<ModelDokumentasi> data) {
            this.c = c;
            datas = new ArrayList<>();
            this.datas = data;
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {
            TextView judul, point;
            ImageView imgGambar, imgPoint;

        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = convertView;
            ViewHolder holder = null;
            if (v == null) {
                inflater = (LayoutInflater) c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.item_galeri, null);
                holder = new ViewHolder();
                holder.judul = (TextView) v.findViewById(R.id.txtJudul);
                holder.point = (TextView)v.findViewById(R.id.TeksPoint);
                holder.imgPoint = (ImageView) v.findViewById(R.id.imgLove);
                holder.imgGambar = (ImageView) v.findViewById(R.id.imgGambar);

                v.setTag(holder);

            } else {
                holder = (ViewHolder) v.getTag();
            }
            //masukkan data booking
             ModelDokumentasi b = datas.get(position);

            holder.judul.setText(b.getNama_dok());
            holder.point.setText("Point : " + b.getPoint_dok());
            idDok = b.getId_dok();
            nPoint = b.getPoint_dok();
            int a = Integer.parseInt(nPoint);
            newPoint = a + 10;




            Picasso.with(context).load(PolingHelper.BASE_URL_IMAGE+b.getFoto_dok()).placeholder(R.drawable.no_image).
                    into(holder.imgGambar);
            holder.imgPoint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    updateLove();


                }
            });

//
            return v;
        }
    }

    private void updateLove() {

        final boolean cancel = false;
        View focusView = null;


        if (cancel) {
            focusView.requestFocus();
        } else {
            //check jam inputnya

            String url = PolingHelper.BASE_URL + "update_love_dok";

            Map<String, Object> params = new HashMap<String, Object>();

            params.put("id_dok", idDok);
            params.put("point_dok", newPoint);


            ProgressDialog dialog = new ProgressDialog(context);
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.setInverseBackgroundForced(false);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setMessage("Loading...");
            try {
                PolingHelper.log("url : " + url + ", param :" + params.toString());
                aq.progress(dialog).ajax(url, params, String.class,
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
                                        PolingHelper.pesan(context, pesan);
                                        Intent a = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(a);
                                        onBackPressed();
                                    } else {
                                        PolingHelper.pesan(context, pesan);

                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();


                                    PolingHelper.pesan(context,
                                            "Error parsing data, please try again.");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    PolingHelper.pesan(context,
                                            "Error submit upload photo , silahkan coba lagi.");
                                }

                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
                PolingHelper.pesan(context,
                        "Error get task, please try again.");
            }


        }

    }

}
