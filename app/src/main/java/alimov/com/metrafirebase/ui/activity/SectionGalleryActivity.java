package alimov.com.metrafirebase.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import alimov.com.metrafirebase.R;
import alimov.com.metrafirebase.adapter.AdapterGallery;
import alimov.com.metrafirebase.db.entity.Photo;
import alimov.com.metrafirebase.db.entity.Section;
import alimov.com.metrafirebase.util.Util;
import alimov.com.metrafirebase.util.firebase.database.DatabaseHelper;
import alimov.com.metrafirebase.util.firebase.storage.StorageHelper;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SectionGalleryActivity extends AppCompatActivity {


    private static final int CAMERA_REQUEST = 121;
    private static final int GALLERY_REQUEST = 131;

    public static final String KEY_SECTION_ID = "section_id";


    @BindView(R.id.recycler_view_photos)
    RecyclerView mRecyclerViewPhotos;

    @BindView(R.id.text_view_description)
    TextView mTextViewDescription;

    @BindView(R.id.text_view_photo_count)
    TextView mTextViewCountPhoto;

    @BindView(R.id.text_view_date)
    TextView mTextViewDate;

    @BindView(R.id.text_view_empty)
    TextView mTextViewEmpty;

    private Section mSection;

    private AdapterGallery mAdapterGallery;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_details);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorActionBar)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorStatusBar));
        }
        actionBar.setDisplayShowHomeEnabled(true);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getResources().getString(R.string.gallery_uploading));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMax(100);
        mProgressDialog.setIndeterminate(true);

        if (getIntent().getExtras() != null) {
            String id =  getIntent().getExtras().getString(KEY_SECTION_ID);

            DatabaseHelper.getInstance().addSectionCallback(id, new DatabaseHelper.OnGetSectionCallback() {
                @Override
                public void onGet(Section section) {
                    mSection = section;

                    initSection();
                }
            });

            DatabaseHelper.getInstance().addPhotoCallback(id, new DatabaseHelper.OnGetPhotosCallback() {
                @Override
                public void onGetPhotos(List<Photo> photos) {
                    if (photos.size() != 0) {
                        mRecyclerViewPhotos.setVisibility(View.VISIBLE);
                        mTextViewEmpty.setVisibility(View.INVISIBLE);
                        photos = Util.sortByDate(photos);
                        mSection.setmPhoto(photos);
                        mAdapterGallery.updatePhotos(photos);
                    } else {
                        mRecyclerViewPhotos.setVisibility(View.INVISIBLE);
                        mTextViewEmpty.setVisibility(View.VISIBLE);
                    }
                    showCountPhoto(mSection.getmPhoto().size());
                }
            });

        }
    }

    private void initSection() {

        mAdapterGallery = new AdapterGallery(mSection.getmPhoto(), new AdapterGallery.AdapterGalleryCallback() {
            @Override
            public void onClick(int position) {

            }

            @Override
            public void onRemove(int position) {
                StorageHelper.getInstance()
                        .deleteImage(mSection.getmName(), mSection.getmPhoto().get(position).getmId(), null);

                DatabaseHelper.getInstance().removePhoto(mSection.getmPhoto().get(position).getmId());

                mSection.getmPhotoIds().remove(mSection.getmPhoto().get(position).getmId());
                mSection.getmPhoto().remove(position);

                if (mSection.getmPhoto().size() == 0 && mRecyclerViewPhotos.getVisibility() == View.VISIBLE) {
                    mRecyclerViewPhotos.setVisibility(View.INVISIBLE);
                    mTextViewEmpty.setVisibility(View.VISIBLE);
                }

                showCountPhoto(mSection.getmPhoto().size());
            }
        });


        mRecyclerViewPhotos.setAdapter(mAdapterGallery);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        mRecyclerViewPhotos.setLayoutManager(manager);

        mTextViewDescription.setText(mSection.getmDescription());
        mTextViewDescription.setMovementMethod(new ScrollingMovementMethod());

        mTextViewDate.setText(getResources().getString(R.string.gallery_section_date)
                + " "
                + new SimpleDateFormat("dd.MM.yyyy").format(mSection.getmCreatedAt()));
        if (mSection.getmName().length() > 0) {
            setTitle(mSection.getmName().substring(0, 1).toUpperCase()
                    + mSection.getmName().substring(1, mSection.getmName().length()));
        }
    }

    private void showCountPhoto(int count) {
        mTextViewCountPhoto.setText(getResources().getString(R.string.gallery_section_photo_count)
                + " "
                + count);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right_defalut, R.anim.slide_out_right_default);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.item_make_photo:
                makePhoto();
                return true;
            case R.id.item_choose_photo:
                chosePhoto();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void makePhoto() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private void chosePhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            final Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            addPhoto(bitmap);

        } else if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                try {
                    InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                    if (inputStream != null) {

                        addPhoto(BitmapFactory.decodeStream(new BufferedInputStream(inputStream)));

                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void addPhoto(final Bitmap bitmap) {
        final String name = String.valueOf(System.currentTimeMillis());

        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                StorageHelper.getInstance().cancel(name);
            }
        });
        mProgressDialog.show();

        StorageHelper.getInstance().uploadImage(bitmap, mSection.getmName(),
                name, new StorageHelper.OnDownloadPhotoCallback() {
                    @Override
                    public void onSuccess(Uri downloadUrl) {

                        final Photo photo = new Photo("", mSection.getmId(), downloadUrl.toString(), bitmap, System.currentTimeMillis(), "name");

                        String id = DatabaseHelper.getInstance().addPhoto(photo);
                        photo.setmId(id);

                        mSection.getmPhoto().add(0, photo);
                        mSection.getmPhotoIds().add(0, id);
                        DatabaseHelper.getInstance().updateSection(mSection);

                        mProgressDialog.dismiss();

                        if (mRecyclerViewPhotos.getVisibility() != View.VISIBLE) {
                            mRecyclerViewPhotos.setVisibility(View.VISIBLE);
                            mTextViewEmpty.setVisibility(View.INVISIBLE);
                        }

                        mAdapterGallery.addPhoto(photo);
                        showCountPhoto(mSection.getmPhoto().size());

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                            }
                        }, 5000);

                    }

                    @Override
                    public void onProgress(long percent) {
                        mProgressDialog.setIndeterminate(false);
                        mProgressDialog.incrementProgressBy((int)percent - mProgressDialog.getProgress());
                        mProgressDialog.incrementSecondaryProgressBy((int)percent - mProgressDialog.getProgress());
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(SectionGalleryActivity.this, "Fail to loading photo", Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gallery_selection_menu, menu);
        return true;
    }
}
