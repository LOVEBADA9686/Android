package g.g.d.com.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReviewVO implements Serializable {
    @SerializedName("kakaoid")
    @Expose
    private String kakaoid;
    @SerializedName("renum")
    @Expose
    private String renum;
    @SerializedName("renickname")
    @Expose
    private String renickname;
    @SerializedName("repass")
    @Expose
    private String repass;
    @SerializedName("recontent")
    @Expose
    private String recontent;
    @SerializedName("rephoto")
    @Expose
    private String rephoto;
    @SerializedName("rerating")
    @Expose
    private String rerating;
    @SerializedName("redeleteyn")
    @Expose
    private String redeleteyn;
    @SerializedName("reinsertdate")
    @Expose
    private String reinsertdate;
    @SerializedName("reupdatedate")
    @Expose
    private String reupdatedate;

 //   private MultipartFile file;
    private String ratingavg;
    private String amount;


    public ReviewVO() {}



    public ReviewVO(String kakaoid, String renum, String renickname, String repass, String recontent, String rephoto,
                    String rerating, String redeleteyn, String reinsertdate, String reupdatedate, //MultipartFile file,
                    String ratingavg, String amount) {
        super();
        this.kakaoid = kakaoid;
        this.renum = renum;
        this.renickname = renickname;
        this.repass = repass;
        this.recontent = recontent;
        this.rephoto = rephoto;
        this.rerating = rerating;
        this.redeleteyn = redeleteyn;
        this.reinsertdate = reinsertdate;
        this.reupdatedate = reupdatedate;
 //       this.file = file;
        this.amount = amount;
        this.ratingavg = ratingavg;
    }



//    public MultipartFile getFile() {
//        return file;
//    }



    public String getRatingavg() {
        return ratingavg;
    }



    public void setRatingavg(String ratingavg) {
        this.ratingavg = ratingavg;
    }



    public String getAmount() {
        return amount;
    }



    public void setAmount(String amount) {
        this.amount = amount;
    }



//    public void setFile(MultipartFile file) {
  //      this.file = file;
   // }



    public String getKakaoid() {
        return kakaoid;
    }
    public void setKakaoid(String kakaoid) {
        this.kakaoid = kakaoid;
    }
    public String getRenum() {
        return renum;
    }
    public void setRenum(String renum) {
        this.renum = renum;
    }
    public String getRenickname() {
        return renickname;
    }
    public void setRenickname(String renickname) {
        this.renickname = renickname;
    }
    public String getRepass() {
        return repass;
    }
    public void setRepass(String repass) {
        this.repass = repass;
    }
    public String getRecontent() {
        return recontent;
    }
    public void setRecontent(String recontent) {
        this.recontent = recontent;
    }
    public String getRephoto() {
        return rephoto;
    }
    public void setRephoto(String rephoto) {
        this.rephoto = rephoto;
    }
    public String getRerating() {
        return rerating;
    }
    public void setRerating(String rerating) {
        this.rerating = rerating;
    }
    public String getRedeleteyn() {
        return redeleteyn;
    }
    public void setRedeleteyn(String redeleteyn) {
        this.redeleteyn = redeleteyn;
    }
    public String getReinsertdate() {
        return reinsertdate;
    }
    public void setReinsertdate(String reinsertdate) {
        this.reinsertdate = reinsertdate;
    }
    public String getReupdatedate() {
        return reupdatedate;
    }
    public void setReupdatedate(String reupdatedate) {
        this.reupdatedate = reupdatedate;
    }
}
