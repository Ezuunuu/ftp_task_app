package com.pointmobile.ftptaskapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.pointmobile.ftptaskapp.model.FtpFileItem;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

public class FtpHelper {
    private static String SERVER = "";
    private static int PORT = 21;
    private static String USER = "";
    private static String PASS = "";
    private static String REMOTE_DIR = "";

    // FTP 정보 가져오기
    public static void getFTPInfo(Context context) throws Exception {
        AssetManager assetManager = context.getAssets();
        Properties props = new Properties();
        props.load(assetManager.open("ftp_config.properties"));

        SERVER = requireProperty(props, "ftp.server");
        PORT = Integer.parseInt(requireProperty(props,"ftp.port"));
        USER = requireProperty(props, "ftp.user");
        PASS = requireProperty(props, "ftp.pass");
        REMOTE_DIR = requireProperty(props, "ftp.dir");
    }
    
    // Property 가져오기
    private static String requireProperty(Properties props, String key) throws IOException {
        String value = props.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IOException("Missing FTP configuration key: " + key);
        }
        return value;
    }

    // FTP 연결
    public static FTPClient connect(Context context) throws Exception {
        getFTPInfo(context);
        FTPClient ftp = new FTPClient();
        ftp.connect(SERVER, PORT);
        ftp.login(USER, PASS);
        ftp.enterLocalPassiveMode();
        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
        
        if (!ftp.changeWorkingDirectory(REMOTE_DIR)) { // 디렉토리를 찾지 못했을 시 생성 필요
            boolean created = ftp.makeDirectory(REMOTE_DIR);
            if (!created) throw new IOException("Cn not create working directory: " + REMOTE_DIR);
            ftp.changeWorkingDirectory(REMOTE_DIR);
        }

        return ftp;
    }

    // 파일 업로드
    public static boolean uploadFile(String filename, String content, Context context) {
        try {
            FTPClient ftp = connect(context);
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            byte[] data = content.getBytes();
            boolean result = ftp.storeFile(filename, new java.io.ByteArrayInputStream(data));
            ftp.logout();
            ftp.disconnect();
            return result;
        } catch (Exception e) {
            Log.e("FTP", "Upload failed: " + e.getMessage(), e);
            return false;
        }
    }

    // 파일 리스트 다운로드
   public static List<FtpFileItem> listFilesWithMetadata(Context context) {
        List<FtpFileItem> result = new ArrayList<>();
        try {
            FTPClient ftp = connect(context);
            FTPFile[] files = ftp.listFiles();
            SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            for (FTPFile file : files) {
                if (file.isFile()) {
                    String name = file.getName();
                    time.setTimeZone(TimeZone.getDefault()); // UTC 타임으로 오는 것 같은데 확인 필요
                    String modified = time.format(file.getTimestamp().getTime());
                    result.add(new FtpFileItem(name, modified));
                }
            }

            ftp.logout();
            ftp.disconnect();
        } catch (Exception e) {
            Log.e("FTP", "List files with metadata failed: " + e.getMessage(), e);
        }
        return result;
    }
}