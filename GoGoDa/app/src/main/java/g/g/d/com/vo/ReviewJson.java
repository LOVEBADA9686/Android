package g.g.d.com.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewJson {
    @SerializedName("review_VO")
    @Expose
    private List<ReviewVO> review_VO;

    public ReviewJson(){}

    public ReviewJson(List<ReviewVO> review_VO) {
        this.review_VO = review_VO;
    }

    public List<ReviewVO> getReview_VO() {
        return review_VO;
    }

    public void setReview_VO(List<ReviewVO> review_VO) {
        this.review_VO = review_VO;
    }
}
