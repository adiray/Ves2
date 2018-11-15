package com.example.dell.ves2.Utility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitClient {



    @GET("real_estate")
    Call<ArrayList<HousesResponse>> getAllHouses();


    @GET("real_estate")
    Call<ArrayList<HousesResponse>> getFilteredHouses(@Query(value = "where",encoded = true) String housesWhereClause);

/*


real_estate? where=Rent%3Dtrue
    @GET("group/{id}/users")
    Call<List<User>> groupList(@Path("id") int groupId, @Query("sort") String sort);
    For complex query parameter combinations a Map can be used.

    @GET("group/{id}/users")
    Call<List<User>> groupList(@Path("id") int groupId, @QueryMap Map<String, String> options);
*/

}
