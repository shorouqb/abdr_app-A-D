package com.example.vip.abdr_app_a_d;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


public class scan extends BaseActivity implements View.OnClickListener{
    ImageButton captureButton,back,done,add;
    TextView lc,con;
ImageView bar;
    private static final int CAM_REQUEST = 1313;
    String img_btm;
    Activity activity;
    int lcount=0;
    int lcountintent=0;
    int[]lid;
    ArrayList<Luggage_info> array;
    luggageAdapter adapter;
    GridView gv;
    Bitmap bitmap;
    int sid;
    String contents;
   ProgressDialog prgDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
                activity = this;
                captureButton = (ImageButton)findViewById(R.id.capture);
                captureButton.setOnClickListener(this);

        add = (ImageButton)findViewById(R.id.add);
        add.setOnClickListener(this);
        add.setVisibility(View.GONE);
        prgDialog = new ProgressDialog(this);
        back = (ImageButton)findViewById(R.id.back_btn);
        back.setOnClickListener(this);
        done = (ImageButton)findViewById(R.id.done_btn);
        done.setOnClickListener(this);
                lc=(TextView)findViewById(R.id.lcount);
        con=(TextView)findViewById(R.id.content);
        bar=(ImageView) findViewById(R.id.imv);
        Bundle  h=getIntent().getExtras();
        sid=h.getInt("sid",-1);
        lid=h.getIntArray("lid");
        lcountintent=lid.length;
                lc.setText(lcountintent+"");
        array = new ArrayList<Luggage_info>();
        adapter = new luggageAdapter(scan.this,R.layout.lugrow,array);

        //***********************************************************************
        gv=(GridView)findViewById(R.id.bargrid);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(scan.this);
                builder.setMessage("would like to cancel this luggage barcode ");
                builder.setTitle("Cancel luggage barcode");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(array.size()>0)

                                    array.remove(position);
                                    lcount--;
                        lcountintent++;
                                    for(int ii = lcount-1 ; ii < -1 ;ii++){
                                        array.get(ii).setLcount(ii+1);
                                    }
                        adapter.notifyDataSetChanged();
                        lc.setText(lcountintent+"");
                        if(lcountintent>0) {
                            captureButton.setEnabled(true);
                        }
                    }
                });
                builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
            }

            public void onActivityResult(int requestCode, int resultCode, Intent intent) {

                switch (requestCode) {
                    case IntentIntegrator.REQUEST_CODE:
                        if (resultCode == Activity.RESULT_OK) {

                            IntentResult intentResult =IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

                            if (intentResult != null) {

                                 contents = intentResult.getContents();

                                Toast.makeText(scan.this,contents , Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(scan.this,"null" , Toast.LENGTH_LONG).show();
                            }
                        } else if (resultCode == Activity.RESULT_CANCELED) {
                            Toast.makeText(scan.this,"canceled" , Toast.LENGTH_LONG).show();
                        }
                        break;
                    case CAM_REQUEST:
                        if(requestCode==CAM_REQUEST && resultCode == RESULT_OK){

                            bitmap = (Bitmap) intent.getExtras().get("data");

                            img_btm = getStringImage(bitmap);
                           con.setText(contents);
                            bar.setImageBitmap(bitmap);
                            add.setVisibility(View.VISIBLE);

                        }
                        else if (resultCode == Activity.RESULT_CANCELED)
                        {
                            Toast.makeText(scan.this,"canceled" , Toast.LENGTH_LONG).show();
                        }
                     break;
    }
}

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.capture:
                if (ContextCompat.checkSelfPermission(scan.this, android.Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED){
                    AlertDialog.Builder builder = new AlertDialog.Builder(scan.this);
                    builder.setTitle("Passenger Flight Information");
                    builder.setMessage("you need to allow the camera on this app go to settings and activate it for this application\n " +
                            "settings -> application -> ABDR App -> permissions -> camera -> on");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();}
                else {
                    Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraintent, CAM_REQUEST);
                    IntentIntegrator integrator = new IntentIntegrator(activity);
                    integrator.initiateScan();
                }
                break;
            case R.id.done_btn:
                if(lcountintent==0) {
                    prgDialog = ProgressDialog.show(this, "Wait Please...", "Adding barcode's to the booking...", false, true);
                    prgDialog.setCancelable(false);
                    prgDialog.setCanceledOnTouchOutside(false);
                    for (int i = 0; i < array.size(); i++) {
                        BackGround1 b = new BackGround1();
                        b.execute(lid[i],  array.get(i).getBitmapstring(),array.get(i).getBarnumber());
                    }
                    BackGround bb=new BackGround();
                    bb.execute("Scanned",sid);
                    startActivity(new Intent(scan.this, current_booking.class));
                }
                else{
                    Toast.makeText(scan.this,"You Still Have Barcode's To Scan", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.add:
                Luggage_info li = new Luggage_info((lcount),img_btm,bitmap,contents);
                array.add(0,li);
                adapter.notifyDataSetChanged();
                con.setText("");
                bar.setImageBitmap(null);
                add.setVisibility(View.GONE);
                lcount++;
                lcountintent--;
                lc.setText(lcountintent+"");
                if (lcountintent==0){
                    captureButton.setEnabled(false);
                }
                break;
            case R.id.back_btn:
                startActivity(new Intent(scan.this, current_booking.class));
                break;
        }
    }
    class BackGround1 extends AsyncTask<Object, String, String> {

        @Override
        protected String doInBackground(Object... params) {
            String number = (String) params[2];
            String bitmap = (String) params[1];

            int lid = (int) params[0];


            String data = "";
            int tmp;

            try {
                URL url = new URL("http://abdr.000webhostapp.com/editluggage.php");
                String urlParams = "LID=" + lid + "&bar=" + URLEncoder.encode(bitmap, "UTF-8")+ "&num=" + number;
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();


                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;
                }

                is.close();
                httpURLConnection.disconnect();

                return data;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("false")) {
                Toast.makeText(scan.this, "Failed to edit", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(scan.this, "Scanned barcode uploaded", Toast.LENGTH_LONG).show();
            }
            }
    }

    class BackGround extends AsyncTask<Object, String, String> {

        @Override
        protected String doInBackground(Object... params) {
            String state = (String) params[0];
            int sid = (int) params[1];


            String data = "";
            int tmp;

            try {
                URL url = new URL("http://abdr.000webhostapp.com/updatebookingstate.php");
                String urlParams = "state=" + state + "&sid=" + sid;
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();


                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;
                }

                is.close();
                httpURLConnection.disconnect();

                return data;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            prgDialog.dismiss();
            if (s.equals("false")) {
                Toast.makeText(scan.this, "Failed to edit", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(scan.this, "Barcode's scanned", Toast.LENGTH_LONG).show();
            }

        }
    }
}