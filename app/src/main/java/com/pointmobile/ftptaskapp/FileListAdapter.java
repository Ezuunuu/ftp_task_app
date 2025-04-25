package com.pointmobile.ftptaskapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pointmobile.ftptaskapp.model.FtpFileItem;

import java.util.List;

public class FileListAdapter extends ArrayAdapter<FtpFileItem> {

    private final LayoutInflater inflater;

    public FileListAdapter(Context context, List<FtpFileItem> items) {
        super(context, 0, items);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FtpFileItem item = getItem(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_file, parent, false);
        }

        TextView tvFileName = convertView.findViewById(R.id.tvFileName);
        TextView tvFileDate = convertView.findViewById(R.id.tvFileDate);

        tvFileName.setText(item.getFileName());
        tvFileDate.setText("Edit: " + item.getModifiedDate());

        return convertView;
    }
}
