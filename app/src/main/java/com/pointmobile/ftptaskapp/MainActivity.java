package com.pointmobile.ftptaskapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnUpload;
    private Button btnFetchList;
    private EditText editFileName;
    private EditText editFileContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnUpload = findViewById(R.id.btnUpload);
        btnFetchList = findViewById(R.id.btnFetchList);
        editFileName = findViewById(R.id.editFileName);
        editFileContent = findViewById(R.id.editFileContent);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = editFileName.getText().toString().trim();
                String content = editFileContent.getText().toString().trim();

                if (filename.isEmpty() || content.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter both the file name and content.", Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread(() -> {
                    boolean success = FtpHelper.uploadFile(filename, content, MainActivity.this);
                    runOnUiThread(() -> {
                        Toast.makeText(MainActivity.this,
                                success ? "Upload Success" : "Upload Fail", Toast.LENGTH_SHORT).show();
                    });
                }).start();
            }
        });

        btnFetchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FileListActivity.class));
            }
        });
    }
}
