package org.wcy.android.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

public class ExampleUtil {

    private static final String INSTALLATION = "INSTALLATION";

    @SuppressLint("MissingPermission")
    public static String getImei(Context context) {
        String sID = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            sID = telephonyManager.getDeviceId();
        } catch (Exception e) {
        }
        if (RxDataTool.isNullString(sID)) {
            try {
                File installation = new File(context.getFilesDir(), INSTALLATION);
                if (!installation.exists()) writeInstallationFile(installation);
                sID = readInstallationFile(installation);
            } catch (Exception e) {
            }
        }
        return sID;
    }

    private static String readInstallationFile(File installation) throws IOException {
        RandomAccessFile f = new RandomAccessFile(installation, "r");
        byte[] bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();
        return new String(bytes);
    }

    private static void writeInstallationFile(File installation) throws IOException {
        FileOutputStream out = new FileOutputStream(installation);
        String id = UUID.randomUUID().toString();
        out.write(id.getBytes());
        out.close();
    }
}
