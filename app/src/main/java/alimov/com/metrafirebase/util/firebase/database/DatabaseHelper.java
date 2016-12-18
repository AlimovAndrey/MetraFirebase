package alimov.com.metrafirebase.util.firebase.database;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import alimov.com.metrafirebase.db.entity.Photo;
import alimov.com.metrafirebase.db.entity.Section;

/**
 * Created by Andrey on 17.12.2016.
 */

public class DatabaseHelper {

    private static final String TABLE_SECTIONS = "section";
    private static final String TABLE_PHOTO = "photo";

    private static DatabaseHelper mInstance = null;
    private static DatabaseReference mDatabase;


    private DatabaseHelper() {
    }

    public static DatabaseHelper getInstance() {
        if (mInstance == null) {
            mInstance = new DatabaseHelper();
            mDatabase = FirebaseDatabase.getInstance().getReference();

        }
        return mInstance;
    }

    public void addSection(Section section) {

        String key = mDatabase.child(TABLE_SECTIONS).push().getKey();
        section.setmId(key);

        mDatabase.child(TABLE_SECTIONS).child(key).setValue(section);
    }

    public void addSectionsListener(@NonNull final OnChangeSectionsListener listener) {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<Section> sections = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Section section = postSnapshot.getValue(Section.class);
                    sections.add(section);
                }
                listener.onChange(sections);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mDatabase.getDatabase().getReference(TABLE_SECTIONS).addValueEventListener(postListener);
    }

    public void addSectionCallback(String id, @NonNull final OnGetSectionCallback callback) {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Section section = dataSnapshot.getValue(Section.class);

                callback.onGet(section);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mDatabase.getDatabase().getReference(TABLE_SECTIONS).child(id).addListenerForSingleValueEvent(postListener);
    }


    public String addPhoto(Photo photo) {

        String key = mDatabase.child(TABLE_PHOTO).push().getKey();
        photo.setmId(key);

        mDatabase.child(TABLE_PHOTO).child(key).setValue(photo);

        return key;
    }

    public void updateSection(Section section) {

        Section updateSection = new Section();
        updateSection.setmId(section.getmId());
        updateSection.setmCreatedAt(section.getmCreatedAt());
        updateSection.setmPhotoIds(section.getmPhotoIds());
        updateSection.setmDescription(section.getmDescription());
        updateSection.setmName(section.getmName());

        mDatabase.child(TABLE_SECTIONS).child(section.getmId()).setValue(section);

    }

    public void removePhoto(String id) {
        mDatabase.child(TABLE_PHOTO).child(id).removeValue();
    }

    public void addPhotoCallback(String id, final OnGetPhotosCallback callback) {

        Query photosQuery = mDatabase.child(TABLE_PHOTO).orderByChild("mSectionId").equalTo(id);

        photosQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<Photo> photos = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Photo photo = postSnapshot.getValue(Photo.class);
                    photos.add(photo);
                }
                callback.onGetPhotos(photos);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public interface OnChangeSectionsListener {
        void onChange(List<Section> sections);
    }

    public interface OnGetSectionCallback {
        void onGet(Section section);
    }

    public interface OnGetPhotosCallback {
        void onGetPhotos(List<Photo> photos);
    }



}
