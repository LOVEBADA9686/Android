package g.g.d.com.board;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;

import androidx.annotation.RequiresApi;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import g.g.d.com.common.GogodaUtil;

// 게시판 목록 리스트 뷰 아이템
public class ListViewItem implements Serializable {

    private String bnum;
    private String bsubject;
    private String bname;
    private String binsertdate;
    private String bfile;
    private Bitmap file;

    public ListViewItem() {
    }

    // Server에서 받을 떄 만듬.
    public ListViewItem(String bnum, String bfile, String bsubject, String bname ,String binsertdate) {
        this.bnum = bnum;
        this.bsubject = bsubject;
        this.bname = bname;
        this.binsertdate = binsertdate;
        this.bfile = bfile;
        try {
            // Server에서 Bitmap을 바로 받아올 수 있길래 여기로 옮김
            file = Picasso.get()
                    .load("http://"+ GogodaUtil.URL_PATH +"/springProject/uploadStorage/" + bfile)
                    .get();
        } catch(Exception e)
        {
            e.printStackTrace();
        };
    }

    // Server에서 받을 때 만듬.
    public ListViewItem(JSONObject object)
    {
        try {
            this.bnum = object.getString("bnum");
            this.bsubject = object.getString("bsubject");
            this.bname = object.getString("bname");
            this.binsertdate = object.getString("binsertdate");
            this.bfile = object.getString("bfile");
            try {
                // Server에서 Bitmap을 바로 받아올 수 있길래 여기로 옮김
                if (!this.bfile.equals("null")) {
                    file = Picasso.get()
                            .load("http://"+ GogodaUtil.URL_PATH +"/springProject/uploadStorage/" + bfile)
                            .get();
                }
            } catch(Exception e)
            {
                e.printStackTrace();
            };
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String getBsubject() {
        return bsubject;
    }

    public void setBsubject(String bsubject) {
        this.bsubject = bsubject;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getBinsertdate() {
        return binsertdate;
    }

    public void setBinsertdate(String binsertdate) {
        this.binsertdate = binsertdate;
    }

    public String getBfile() {
        return bfile;
    }

    public void setBfile(String bfile) {
        this.bfile = bfile;
    }

    public Bitmap getFile() {
        return file;
    }

    public void setFile(Bitmap file) {
        this.file = file;
    }

    public String getBnum() {
        return bnum;
    }
}
