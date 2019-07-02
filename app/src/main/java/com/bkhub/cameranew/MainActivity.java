package com.bkhub.cameranew;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class MainActivity extends AppCompatActivity {

    Button btnTakePic;
    ImageView imageView;
    String pathsToFile;
    File photoFile;
    File image;
    int REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTakePic = (Button) findViewById(R.id.btn_camera);

        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        }

        btnTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispachPictureTakeAction();
            }
        });

        imageView = (ImageView) findViewById(R.id.iv_Image);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == REQUEST_CODE) {
            if (requestCode == RESULT_OK) {
                Bitmap bitmap = BitmapFactory.decodeFile(pathsToFile);
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    private void dispachPictureTakeAction() {
        Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePic.resolveActivity(getPackageManager()) != null) {
            photoFile = null;
            photoFile = createPhotoFile();

            if (photoFile != null) {
                pathsToFile = photoFile.getAbsolutePath();
                Uri photoUri = FileProvider.getUriForFile(MainActivity.this, "com.bkhub.cameranew.fileprovider", photoFile);
                takePic.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePic, REQUEST_CODE);

            }
        }
    }

    private File createPhotoFile() {
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        image = null;
        try {
            image = File.createTempFile(name, ".jpg", storageDir);
        } catch (IOException e) {

        }
        return image;
    }

}
