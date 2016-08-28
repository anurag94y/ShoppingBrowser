package history;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by anurag.yadav on 8/1/16.
 */
public class History implements Parcelable {
    private String title;
    private String url;
    private byte[] image;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public byte[] getImage() {
        return this.image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }


    public History(Parcel in) {
        title = in.readString();
        url = in.readString();
        image = new byte[in.readInt()];
        in.readByteArray(image);
    }


    public static final Creator<History> CREATOR = new Creator<History>() {
        @Override
        public History createFromParcel(Parcel in) {
            return new History(in);
        }

        @Override
        public History[] newArray(int size) {
            return new History[size];
        }
    };

    public History() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(url);
        parcel.writeInt(image.length);
        parcel.writeByteArray(image);
    }

}
