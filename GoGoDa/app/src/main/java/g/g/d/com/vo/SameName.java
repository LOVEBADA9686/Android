package g.g.d.com.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SameName {

    @SerializedName("region")
    @Expose
    private List<Object> region = null;
    @SerializedName("keyword")
    @Expose
    private String keyword;
    @SerializedName("selected_region")
    @Expose
    private String selectedRegion;
}
