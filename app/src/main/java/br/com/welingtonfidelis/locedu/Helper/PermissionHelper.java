package br.com.welingtonfidelis.locedu.Helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinel on 30/11/2017.
 */

public class PermissionHelper {
    public static boolean request(int requestCode, Activity activity, String[] permissions) {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> listPermissions = new ArrayList<String>();

            for (String permission : permissions) {
                if (!(ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED)) {
                    listPermissions.add(permission);
                }
            }

            if (listPermissions.isEmpty()) return true;

            String[] newPermissions = new String[listPermissions.size()];
            listPermissions.toArray(newPermissions);

            ActivityCompat.requestPermissions(activity, newPermissions, requestCode);

            return false;
        } else {
            return true;
        }
    }
}