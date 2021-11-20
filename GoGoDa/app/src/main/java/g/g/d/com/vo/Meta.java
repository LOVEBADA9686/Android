package g.g.d.com.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Meta {
    @SerializedName("same_name")
    @Expose
    private SameName sameName;
    @SerializedName("pageable_count")
    @Expose
    private Integer pageableCount;
    @SerializedName("total_count")
    @Expose
    private Integer totalCount;
    @SerializedName("is_end")
    @Expose
    private Boolean isEnd;
}
