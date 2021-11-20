package g.g.d.com.board;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import g.g.d.com.common.GogodaUtil;

// 게시글 작성 데이터
public class BoardItem {
    private String num;
    private String insertdate;
    private String subject;
    private String name;
    private String content;
    private String password;
    private Bitmap file;
    private String filename;

    /*********************** Server에서 Board 정보를 받을 때 *************************************/
    public BoardItem(String num, String subject, String name, String content, String insertdate, String filename) {
        this.num = num;
        this.subject = subject;
        this.content = content;
        this.password = password;
        this.filename = filename;
        this.insertdate = insertdate;
        try {
            // Server에서 Bitmap을 바로 받아올 수 있길래 여기로 옮김
            file = Picasso.get()
                    .load("http://" + GogodaUtil.URL_PATH + "/springProject/uploadStorage/" + filename)
                    .get();
        } catch(Exception e)
        {
            e.printStackTrace();
        };
    }
    public BoardItem(JSONObject jsonObject) throws JSONException {
        this.num = jsonObject.getString("bnum");
        this.name = jsonObject.getString("bname");
        this.subject = jsonObject.getString("bsubject");
        this.content = jsonObject.getString("bcontent");
        this.filename = jsonObject.getString("bfile");
        this.insertdate = jsonObject.getString("binsertdate");
        try {
            // Server에서 Bitmap을 바로 받아올 수 있길래 여기로 옮김
            file = Picasso.get()
                    .load("http://" + GogodaUtil.URL_PATH + "/springProject/uploadStorage/" + filename)
                    .get();
        } catch(Exception e)
        {
            e.printStackTrace();
        };
    }


    /*********************** Server에서 Board 정보를 보낼 때 *************************************/
    public BoardItem(String subject, String name, String content, String password, Bitmap file) {
        this.subject = subject;
        this.name = name;
        this.content = content;
        this.password = password;
        this.file = file;
        this.filename = this.subject + "__" + this.name + "__" + this.content + ".jpg";
    }

    static public String boundary = "----BoardAttribute";
    static private String startBoundary= "--" + boundary + "\r\n";
    static private String endBoundary = "\r\n--" + boundary + "--\r\n";

    // Transform multipart/form-data
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private byte[] exportString(String key, String value)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(startBoundary);
        builder.append("Content-Disposition: form-data; name=\"" + key + "\"\r\n");
        builder.append("\r\n");
        builder.append(value);
        builder.append("\r\n");
        String string = builder.toString();
        byte[] result = string.getBytes(StandardCharsets.UTF_8);
        return result;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private byte[] exportFile()
    {
        StringBuilder builder = new StringBuilder();

        builder.append(startBoundary);
        builder.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + this.filename + "\"\r\n");
        builder.append("Content-Type: image/jpeg\r\n");
        builder.append("\r\n");
        String prefixString = builder.toString();
        byte[] prefix = prefixString.getBytes(StandardCharsets.UTF_8);

        byte[] content = new byte[0];
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            file.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            content = byteArrayOutputStream.toByteArray();
        } catch(Exception e)
        {
            e.printStackTrace();
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byteArrayOutputStream.write(prefix);
            byteArrayOutputStream.write(content);
        } catch(Exception e)
        {
            e.printStackTrace();
        }
        byte[] result = byteArrayOutputStream.toByteArray();
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public byte[] export() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byte[] bytes = exportString("bsubject", subject);
            byteArrayOutputStream.write(bytes);

            bytes = exportString("bname", name);
            byteArrayOutputStream.write(bytes);

            bytes = exportString("bcontent", content);
            byteArrayOutputStream.write(bytes);

            bytes = exportString("bpw", password);
            byteArrayOutputStream.write(bytes);

            bytes = exportFile();
            byteArrayOutputStream.write(bytes);

            bytes = endBoundary.getBytes(StandardCharsets.UTF_8);
            byteArrayOutputStream.write(bytes);

        } catch(Exception e)
        {
            e.printStackTrace();
        }
        byte[] result = byteArrayOutputStream.toByteArray();

        return result;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Bitmap getFile() {
        return file;
    }

    public void setFile(Bitmap file) {
        this.file = file;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public static String getStartBoundary() {
        return startBoundary;
    }

    public static void setStartBoundary(String startBoundary) {
        BoardItem.startBoundary = startBoundary;
    }

    public static String getEndBoundary() {
        return endBoundary;
    }

    public static void setEndBoundary(String endBoundary) {
        BoardItem.endBoundary = endBoundary;
    }

    public String getInsertdate() {
        return insertdate;
    }

    public String getNum() {
        return num;
    }
}
