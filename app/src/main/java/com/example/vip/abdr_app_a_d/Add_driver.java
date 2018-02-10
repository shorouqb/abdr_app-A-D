package com.example.vip.abdr_app_a_d;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Add_driver extends BaseActivityA implements View.OnClickListener {

    private Bitmap bitmap;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;
    ImageView reg,btnTakePhoto,cans;
    EditText username,Pass,name,id,nationality,phone,email,c_color,c_brand,m_c,p_n;
    ImageView imgTakenPhoto;
    Spinner city;
    private static final int CAM_REQUEST = 1313;
    String img_btm=" ";
    ProgressDialog prgDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_driver);
        username = (EditText) findViewById(R.id.username);
        Pass = (EditText) findViewById(R.id.password);
        name = (EditText) findViewById(R.id.name);
        id = (EditText) findViewById(R.id.idno);
        nationality = (EditText) findViewById(R.id.nationality);
        city = (Spinner) findViewById(R.id.city);
        phone = (EditText) findViewById(R.id.phone);
        email = (EditText) findViewById(R.id.email);
        c_brand = (EditText) findViewById(R.id.car_brand);
        c_color = (EditText) findViewById(R.id.car_color);
        p_n = (EditText) findViewById(R.id.p_n);
        m_c = (EditText) findViewById(R.id.m_c);
        btnTakePhoto = (ImageView) findViewById(R.id.button1);
        imgTakenPhoto = (ImageView) findViewById(R.id.imageview1);

        btnTakePhoto.setOnClickListener(new btnTakePhotoClicker());
        reg = (ImageView) findViewById(R.id.btnregister);
        reg.setOnClickListener(this);

        cans= (ImageView)findViewById(R.id.cans);
        cans.setOnClickListener(this);

        prgDialog = new ProgressDialog(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAM_REQUEST) {

            bitmap = (Bitmap) data.getExtras().get("data");
            imgTakenPhoto.setImageBitmap(bitmap);
            img_btm = getStringImage(bitmap);

        }
        else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imgTakenPhoto.setImageBitmap(bitmap);
                img_btm = getStringImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    class btnTakePhotoClicker implements Button.OnClickListener {

        @Override
        public void onClick(View v) {

            AlertDialog.Builder builder = new AlertDialog.Builder(Add_driver.this);

            builder.setTitle("Id image");
            ImageView image = new ImageView(Add_driver.this);
            builder.setMessage("Would you like to upload image from gallery, or take new image ");
            builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraintent, CAM_REQUEST);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.getWindow().setLayout(200, 400);
            dialog.show();

        }

    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnregister:

                if (name.getText().toString().equals("")||city.getSelectedItem().toString().equals("City:")||
                        email.getText().toString().equals("")||nationality.getText().toString().equals("")
                        ||phone.getText().toString().equals("")||m_c.getText().toString().equals("")
                        ||id.getText().toString().equals("")||c_color.getText().toString().equals("")||
                        c_brand.getText().toString().equals("")||username.getText().toString().equals("")||
                        Pass.getText().toString().equals("")||img_btm.equals(" ")||p_n.getText().toString().equals("")) {
                    Toast.makeText(Add_driver.this, "Fill the missing field", Toast.LENGTH_LONG).show();

                }
                else{
                    int atpos = email.getText().toString().indexOf("@");
                    int dotpos = email.getText().toString().lastIndexOf(".");
                    if (atpos < 1 || dotpos < atpos + 2 || dotpos + 2 >= email.getText().toString().length()) {
                        Toast.makeText(Add_driver.this, "Not a valid e-mail address..", Toast.LENGTH_LONG).show();
                    }
                    else if (!phone.getText().toString().startsWith("5")||phone.getText().toString().length()!=9){
                        Toast.makeText(Add_driver.this, "Phone format is wrong..", Toast.LENGTH_LONG).show();
                    }
                    else if (id.getText().toString().length()!=10){
                        Toast.makeText(Add_driver.this, "National id is not complete..", Toast.LENGTH_LONG).show();
                    }

                    else{

                        prgDialog = ProgressDialog.show(this, "Wait Please...", "Creating Driver Account...", false, true);
                        prgDialog.setCancelable(false);
                        prgDialog.setCanceledOnTouchOutside(false);
                        BackGround1 b = new BackGround1(this);
                        b.execute(email.getText().toString(),username.getText().toString(),"d");
                    }
                }


                break;


            case R.id.cans:
                startActivity(new Intent(this,home.class));
                break;
        }
    }

    class BackGround extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String name = params[0];
            String c_color = params[11];
            String c_brand = params[10];
            String p_n = params[12];
            int m_c = Integer.parseInt(params[9]);
            long id=Long.parseLong(params[5]);
            String nationality = params[3];
            String city = params[1];
            long phone=Long.parseLong(params[4]);
            String email = params[2];
            String username = params[6];
            String password = params[7];
            String img = params[8];
            String data="";
            int tmp;
            try {
                URL url = new URL("http://abdr.000webhostapp.com/adddriver.php");
                String urlParams = "name="+name+"&id_number="+id+"&nationality="+nationality+"&city="+city+
                        "&phone="+phone+"&email="+email+"&username="+username+"&password="+password+"&c_color="+c_color
                        +"&c_brand="+c_brand
                        +"&p_n="+p_n
                        +"&m_c="+m_c
                        +"&idimg="+ URLEncoder.encode(img, "UTF-8");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();
                InputStream is = httpURLConnection.getInputStream();
                while((tmp=is.read())!=-1){
                    data+= (char)tmp;}
                is.close();
                httpURLConnection.disconnect();
                return data;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            prgDialog.dismiss();
            Toast.makeText(Add_driver.this, s, Toast.LENGTH_LONG).show();
            if (s.equals("false")){
                Toast.makeText(Add_driver.this, "Failed to add driver..", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(Add_driver.this, "Driver added..", Toast.LENGTH_LONG).show();
                startActivity(new Intent(Add_driver.this, Show_drivers.class));
            }
        }
    }

    class BackGround1 extends AsyncTask<String, String, String> {
        Add_driver ad;
        public BackGround1 (Add_driver ad){
          this.ad=ad;
        }
        @Override
        protected String doInBackground(String... params) {

            String email = params[0];
            String username = params[1];
            String flag = params[2];


            String data="";
            int tmp;

            try {
                URL url = new URL("http://abdr.000webhostapp.com/checkuseremail.php");
                String urlParams = "email="+email+"&username="+username+"&flag="+flag;
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();
                InputStream is = httpURLConnection.getInputStream();
                while((tmp=is.read())!=-1){
                    data+= (char)tmp;
                }
                is.close();
                httpURLConnection.disconnect();

                return data;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
          //  prgDialog.dismiss();
            if (s.equals("email")){
                Toast.makeText(Add_driver.this, "Failed to add driver: duplicated email..", Toast.LENGTH_LONG).show();
            }
            else if (s.equals("user")){
                Toast.makeText(Add_driver.this, "Failed to add driver: duplicated username..", Toast.LENGTH_LONG).show();
            }
            else {
                BackGround b = new BackGround();
                b.execute(ad.name.getText().toString(),ad.city.getSelectedItem().toString()
                        , ad.email.getText().toString(), ad.nationality.getText().toString(),ad.phone.getText().toString(),
                        ad.id.getText().toString(), ad.username.getText().toString(), ad.Pass.getText().toString(),ad.img_btm
                        ,ad.m_c.getText().toString(),ad.c_brand.getText().toString(),
                        ad.c_color.getText().toString(),ad.p_n.getText().toString());
            }
        }
    }
}
