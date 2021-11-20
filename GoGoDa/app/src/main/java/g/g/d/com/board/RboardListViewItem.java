package g.g.d.com.board;

import org.json.JSONException;
import org.json.JSONObject;

// 댓글 리스트 뷰 아이템
public class RboardListViewItem {

    private String rbname;
    private String rbcontent;
    private String rbdate;

    public RboardListViewItem() {}

    public RboardListViewItem(String rbname, String rbcontent, String rbdate) {
        this.rbname = rbname;
        this.rbcontent = rbcontent;
        this.rbdate = rbdate;
    }
    public RboardListViewItem(JSONObject jsonObject) throws JSONException {
        this.rbname = jsonObject.getString("rbname");
        this.rbcontent = jsonObject.getString("rbcontent");
        this.rbdate = jsonObject.getString("rbdate");
    }

    public String getRbname() {
        return rbname;
    }

    public void setRbname(String rbname) {
        this.rbname = rbname;
    }

    public String getRbcontent() {
        return rbcontent;
    }

    public void setRbcontent(String rbcontent) {
        this.rbcontent = rbcontent;
    }

    public String getRbdate() {
        return rbdate;
    }

    public void setRbdate(String rbdate) {
        this.rbdate = rbdate;
    }
}
