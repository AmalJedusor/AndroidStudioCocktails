package fr.uha.hassenforder.teams;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.snackbar.Snackbar;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Hassenforder on 9/02/2021.
 */
public class Permissions {

    static private Permissions instance;

    private Activity activity;

    static final private int REQUEST_ACCESS = 1;

    private MutableLiveData<Set<String>> grantedPermissions;

    private Permissions(Activity activity) {
        this.activity = activity;
        grantedPermissions = new MutableLiveData<>();
    }

    public static Permissions create (Activity activity) {
        if (instance == null) instance = new Permissions(activity);
        return instance;
    }

    public static Permissions get () {
        return instance;
    }

    public LiveData<Set<String>> getGrantedPermissions() {
        return grantedPermissions;
    }

    private void updateGrantedPermissions(Set<String> granted) {
        if (granted == null) return;
        if (grantedPermissions.getValue() != null) {
            granted.addAll(grantedPermissions.getValue());
        }
        grantedPermissions.postValue(granted);
    }

    public void requestPermissions(View view, int messageId, String permissions []) {
        Set<String> toRequest = new TreeSet<>();
        Set<String> granted = new TreeSet<>();
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(), permission) == PackageManager.PERMISSION_GRANTED) {
                granted.add(permission);
            } else {
                toRequest.add(permission);
            }
        }
        updateGrantedPermissions(granted);
        switch (toRequest.size()) {
        case 0 : // all permissions are granted
            break;
        case 1 :
            if (view != null && ActivityCompat.shouldShowRequestPermissionRationale(activity, toRequest.iterator().next())) {
                Snackbar.make(view, messageId,
                        Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityCompat.requestPermissions(activity, toRequest.toArray(new String [0]), REQUEST_ACCESS);
                    }
                }).show();
            } else {
                ActivityCompat.requestPermissions(activity, toRequest.toArray(new String [0]), REQUEST_ACCESS);
            }
            break;
        default :
            ActivityCompat.requestPermissions(activity, toRequest.toArray(new String [0]), REQUEST_ACCESS);
            break;
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissionsGranted, int[] grantResults) {
        if (requestCode != REQUEST_ACCESS) return;
        if (grantResults == null) return;
        Set<String> granted = new TreeSet<>();
        for (int i=0; i<grantResults.length;++i) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                granted.add(permissionsGranted[i]);
            }
        }
        updateGrantedPermissions(granted);
    }

}
