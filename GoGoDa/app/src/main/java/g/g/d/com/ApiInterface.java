package g.g.d.com;

import g.g.d.com.vo.KaKaoVO;
import g.g.d.com.vo.ReviewJson;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("v2/local/search/keyword.json")
    Call<KaKaoVO> getSearchLocation(
            @Header("Authorization") String token,
            @Query("query") String query,
            @Query("x") String x,
            @Query("y") String y,
            @Query("radius") int radius
    );

    @POST("springProject/review/reviewAndroidSelect.ggd")
    Call<ReviewJson> getDBReview(
            @Query("kakaoid") String kakaoid
    );
}
