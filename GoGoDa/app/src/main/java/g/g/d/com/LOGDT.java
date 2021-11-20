package g.g.d.com;

import android.os.Parcel;
import android.os.Parcelable;

public class LOGDT implements Parcelable {

    public String mid;
    public String sType;
    public String mMail;

    public LOGDT(String ID, String schType, String mMail) {

        this.mid = ID;
        this.sType = schType;
        this.mMail = mMail;
    }

    public LOGDT(Parcel in) {
        mid = in.readString();
        sType = in.readString();
        mMail = in.readString();
    }

    public static final Creator<LOGDT> CREATOR = new Creator<LOGDT>() {
        @Override
        public LOGDT createFromParcel(Parcel in) {
            return new LOGDT(in);
        }

        @Override
        public LOGDT[] newArray(int size) {
            return new LOGDT[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(mid);
        dest.writeString(sType);
        dest.writeString(mMail);

    }
}
