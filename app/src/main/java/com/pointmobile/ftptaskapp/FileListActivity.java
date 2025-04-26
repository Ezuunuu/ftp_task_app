package com.pointmobile.ftptaskapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pointmobile.ftptaskapp.model.FtpFileItem;

import java.util.List;

public class FileListActivity extends AppCompatActivity {

    private ListView listView;
    private Button btnBack;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);

        listView = findViewById(R.id.listView);
        btnBack = findViewById(R.id.btnBack);
        progressBar = findViewById(R.id.progressBar);

        btnBack.setOnClickListener(v -> finish());

        progressBar.setVisibility(View.VISIBLE); // 로딩 시작

        new Thread(() -> {
            List<FtpFileItem> items = FtpHelper.listFilesWithMetadata(FileListActivity.this);
            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE); // 로딩 끝
                if (items.isEmpty()) {
                    Toast.makeText(this, "Can not found file from server.", Toast.LENGTH_SHORT).show();
                } else {
                    FileListAdapter adapter = new FileListAdapter(this, items);
                    listView.setAdapter(adapter);
                    Toast.makeText(this, items.size() + "files loaded.", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}
