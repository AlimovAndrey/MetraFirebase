package alimov.com.metrafirebase.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import alimov.com.metrafirebase.R;
import alimov.com.metrafirebase.db.entity.Photo;
import alimov.com.metrafirebase.ui.widget.SquareImageView;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Roma on 06.10.2015.
 */
public class AdapterGallery extends RecyclerView.Adapter<AdapterGallery.GalleryViewHolder> {

    private AdapterGalleryCallback mCallback;
    private List<Photo> mPhotos = new ArrayList<>();

    public AdapterGallery(List<Photo> photos, AdapterGalleryCallback callback) {
        mPhotos.clear();
        mPhotos.addAll(photos);
        mCallback = callback;
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GalleryViewHolder holder, final int position) {
        final int pos = holder.getAdapterPosition();

        Photo photo = mPhotos.get(pos);

        if (photo.getmBitmap() != null) {
            holder.mImageViewPhoto.setImageBitmap(photo.getmBitmap());
        } else {
            Picasso.with(holder.mImageViewPhoto.getContext())
                    .load(photo.getmUrl())
                    .into(holder.mImageViewPhoto);
        }

        holder.mTextViewDate.setText(new SimpleDateFormat("dd.MM.yyyy").format(photo.getmCreatedAt()));

        holder.mImageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mPhotos.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, getItemCount());

                if (mCallback != null) {
                    mCallback.onRemove(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    public void addPhoto(Photo photo) {
        mPhotos.add(0, photo);
        notifyItemInserted(0);
    }

    public void updatePhotos(List<Photo> photos) {
        mPhotos.clear();
        mPhotos.addAll(photos);
        notifyDataSetChanged();
    }

    public static class GalleryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_view_photo)
        SquareImageView mImageViewPhoto;

        @BindView(R.id.image_view_delete)
        ImageView mImageViewDelete;

        @BindView(R.id.text_view_date)
        TextView mTextViewDate;

        public GalleryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface AdapterGalleryCallback {
        void onClick(int position);
        void onRemove(int position);
    }

}
