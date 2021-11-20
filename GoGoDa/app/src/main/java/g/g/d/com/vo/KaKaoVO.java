package g.g.d.com.vo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class KaKaoVO {
    /*
          "address_name": "서울 종로구 부암동 258-3",
          "category_group_code": "FD6",
          "category_group_name": "음식점",
          "category_name": "음식점 > 치킨",
          "distance": "",
          "id": "21303463",
          "phone": "02-391-3566",
          "place_name": "계열사",
          "place_url": "http://place.map.kakao.com/21303463",
          "road_address_name": "서울 종로구 백석동길 7",
          "x": "126.965627898906",
          "y": "37.5928724659406"
     */
    @SerializedName("meta")
    @Expose
    private Meta meta;
    @SerializedName("documents")
    @Expose
    private List<Document> documents = null;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public KaKaoVO(Meta meta, List<Document> documents) {
        this.meta = meta;
        this.documents = documents;
    }
}
