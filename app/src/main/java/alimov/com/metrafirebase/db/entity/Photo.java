package alimov.com.metrafirebase.db.entity;

import android.graphics.Bitmap;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Andrey on 16.12.2016.
 */

@IgnoreExtraProperties
public class Photo {

    private String mId;
    private String mSectionId;
    private String mUrl;
    @Exclude
    private Bitmap mBitmap;
    private long mCreatedAt;
    private String mName;

    public Photo(String mId, String mSectionId, String mUrl, Bitmap mBitmap, long mCreatedAt, String mName) {
        this.mId = mId;
        this.mSectionId = mSectionId;
        this.mUrl = mUrl;
        this.mBitmap = mBitmap;
        this.mCreatedAt = mCreatedAt;
        this.mName = mName;
    }

    public Photo() {
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    @Exclude
    public Bitmap getmBitmap() {
        return mBitmap;
    }

    @Exclude
    public void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public long getmCreatedAt() {
        return mCreatedAt;
    }

    public void setmCreatedAt(long mCreatedAt) {
        this.mCreatedAt = mCreatedAt;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmSectionId() {
        return mSectionId;
    }

    public void setmSectionId(String mSectionId) {
        this.mSectionId = mSectionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Photo photo = (Photo) o;

        if (mCreatedAt != photo.mCreatedAt) return false;
        if (mId != null ? !mId.equals(photo.mId) : photo.mId != null) return false;
        if (mSectionId != null ? !mSectionId.equals(photo.mSectionId) : photo.mSectionId != null)
            return false;
        return mUrl != null ? mUrl.equals(photo.mUrl) : photo.mUrl == null;

    }

    @Override
    public int hashCode() {
        int result = mId != null ? mId.hashCode() : 0;
        result = 31 * result + (mSectionId != null ? mSectionId.hashCode() : 0);
        result = 31 * result + (mUrl != null ? mUrl.hashCode() : 0);
        result = 31 * result + (int) (mCreatedAt ^ (mCreatedAt >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "mId='" + mId + '\'' +
                ", mUrl='" + mUrl + '\'' +
                ", mBitmap=" + mBitmap +
                ", mCreatedAt=" + mCreatedAt +
                ", mName='" + mName + '\'' +
                '}';
    }
}
