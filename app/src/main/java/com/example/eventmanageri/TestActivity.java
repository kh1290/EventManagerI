package com.example.eventmanageri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.net.Uri;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import androidx.annotation.NonNull;

public class TestActivity extends AppCompatActivity {
    private FirebaseStorage mStorage;
    private StorageReference mStoragePhotoRef;
    private StorageReference mStorageVideoRef;

    Button ch, sv;
    ImageView img;
    public Uri imgUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ch = (Button) findViewById(R.id.ch);
        sv = (Button) findViewById(R.id.sv);
        img = (ImageView) findViewById(R.id.img);

        mStorage = FirebaseStorage.getInstance();
        mStoragePhotoRef = mStorage.getReference("photo");
        mStorageVideoRef = mStorage.getReference("video");


        ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });

        sv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver Cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(Cr.getType(uri));
    }

    private void FileUplaod() {
        StorageReference ref = mStoragePhotoRef.child(System.currentTimeMillis()+"."+getFileExtension(imgUri));

        ref.putFile(imgUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        //Uri downloadUrl = taskSnapshot.getStorage().getDownloadUrl();
                        Toast.makeText(TestActivity.this,"success",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }

    private void choosePhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && requestCode == RESULT_OK && data != null && data.getData() != null) {
            imgUri = data.getData();
            img.setImageURI(imgUri);
        }

    }
}
