package alimov.com.metrafirebase.util.firebase.storage;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import alimov.com.metrafirebase.util.Util;

/**
 * Created by Andrey on 17.12.2016.
 */

public class StorageHelper {

    private static final String STORAGE_URL = "gs://galleryfirebase.appspot.com";

    private static StorageHelper mInstance = null;
    private static StorageReference mStorageRef;

    private HashMap<String, UploadTask> mLoadingTasks = new HashMap<>();

    private StorageHelper() {
    }

    public static StorageHelper getInstance() {
        if (mInstance == null) {
            mInstance = new StorageHelper();
            mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl(STORAGE_URL);
        }
        return mInstance;
    }

    public void uploadImage(Bitmap bitmap, String section, final String name, final @Nullable OnDownloadPhotoCallback callback) {
        StorageReference mountainImagesRef = mStorageRef.child(section + "/" + name);

        final UploadTask task = mountainImagesRef.putBytes(Util.toByteArray(bitmap));
        mLoadingTasks.put(name, task);

        task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                if (mLoadingTasks.containsKey(name)) {
                    mLoadingTasks.remove(task);
                }

                if (callback != null) {
                    callback.onSuccess(taskSnapshot.getDownloadUrl());
                }
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                if (callback != null) {
                    callback.onFailure(e);
                }
            }
        });
        task.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                long progress = (long) (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                if (callback != null) {
                    callback.onProgress(progress);
                }
            }
        });
    }

    public void deleteImage(String selection, String name, final @Nullable OnDeletePhotoCallback callback) {
        StorageReference desertRef = mStorageRef.child(selection + "/" + name);

        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (callback != null) {
                    callback.onSuccess();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (callback != null) {
                    callback.onFailure(e);
                }
            }
        });
    }

    public void cancel(String name) {
        if (mLoadingTasks.containsKey(name)) {
            mLoadingTasks.get(name).cancel();
            mLoadingTasks.remove(name);
        }
    }

    public interface OnDownloadPhotoCallback {
        void onSuccess(Uri downloadUrl);
        void onProgress(long percent);
        void onFailure(Exception e);
    }

    public interface OnDeletePhotoCallback {
        void onSuccess();
        void onFailure(Exception e);
    }

}
