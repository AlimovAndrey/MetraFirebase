package alimov.com.metrafirebase.db.entity;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrey on 16.12.2016.
 */

@IgnoreExtraProperties
public class Section {

    private String mId;
    private String mName;
    private String mDescription;
    private long mCreatedAt;
    @Exclude
    private List<Photo> mPhoto = new ArrayList<>();
    private List<String> mPhotoIds = new ArrayList<>();

    public Section(String mId, String mName, String mDescription, long mCreatedAt, List<Photo> mPhoto, List<String> mPhotoIds) {
        this.mId = mId;
        this.mName = mName;
        this.mDescription = mDescription;
        this.mCreatedAt = mCreatedAt;
        this.mPhoto = mPhoto;
        this.mPhotoIds = mPhotoIds;
    }

    public Section() {
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public long getmCreatedAt() {
        return mCreatedAt;
    }

    public void setmCreatedAt(long mCreatedAt) {
        this.mCreatedAt = mCreatedAt;
    }

    @Exclude
    public List<Photo> getmPhoto() {
        return mPhoto;
    }

    @Exclude
    public void setmPhoto(List<Photo> mPhoto) {
        this.mPhoto.clear();
        this.mPhoto.addAll(mPhoto);
    }

    public List<String> getmPhotoIds() {
        return mPhotoIds;
    }

    public void setmPhotoIds(List<String> mPhotoIds) {
        this.mPhotoIds.clear();
        this.mPhotoIds.addAll(mPhotoIds);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Section section = (Section) o;

        if (mId != section.mId) return false;
        if (mCreatedAt != section.mCreatedAt) return false;
        if (mName != null ? !mName.equals(section.mName) : section.mName != null) return false;
        return mDescription != null ? mDescription.equals(section.mDescription) : section.mDescription == null;

    }

    @Override
    public int hashCode() {
        int result = mId != null ? mId.hashCode() : 0;
        result = 31 * result + (mName != null ? mName.hashCode() : 0);
        result = 31 * result + (mDescription != null ? mDescription.hashCode() : 0);
        result = 31 * result + (int) (mCreatedAt ^ (mCreatedAt >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Section{" +
                "mId=" + mId +
                ", mName='" + mName + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mCreatedAt=" + mCreatedAt +
                ", mPhoto=" + mPhoto +
                ", mPhotoIds=" + mPhotoIds +
                '}';
    }
}
