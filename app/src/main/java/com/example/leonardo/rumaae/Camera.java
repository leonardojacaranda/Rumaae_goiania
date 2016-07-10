package com.example.leonardo.rumaae;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Camera extends AppCompatActivity {


    Button btnTakePhoto;
    ImageView imgTakePhoto;
    private static final int CAM_REQUEST = 1313;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnTakePhoto = (Button) findViewById(R.id.button);
        imgTakePhoto = (ImageView) findViewById(R.id.imageview);

        btnTakePhoto.setOnClickListener(new btnTakePhotoClicker());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAM_REQUEST) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imgTakePhoto.setImageBitmap(thumbnail);
        }
    }
    class btnTakePhotoClicker implements Button.OnClickListener
    {

        @Override
        public void onClick(View v) {
            Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraintent, CAM_REQUEST);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.camera, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                startActivity(new Intent(this, MapsActivity.class));
                return true;
            case R.id.enviar:
                startActivity(new Intent(this, Solicitacoes.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}