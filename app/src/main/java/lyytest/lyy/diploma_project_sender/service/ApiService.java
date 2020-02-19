package lyytest.lyy.diploma_project_sender.service;


import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by hzy on 2019/1/10
 **/
public interface ApiService {
    //test
    @POST("sender/hello")
    Call<Map<String,Object>> test1();

    @FormUrlEncoded
    @POST("sender/login")
    Call<Map<String,Object>> postLogin(@Field("username") String username, @Field("password") String password,
                                       @Field("timestamp") String timestamp);

    @FormUrlEncoded
    @POST("sender/savesenderlocation")
    Call<Map<String,Object>> postsaveLocation(@Field("longitude") String longitude, @Field("latitude") String latitude,
                                       @Field("location") String location,  @Field("sid") int sid);

    @FormUrlEncoded
    @POST("sender/showsenderorder")
    Call<List<Map<String,Object>>> postShowSenderOrder(@Field("orderstatus") int orderstatus, @Field("sid")int  sid);

    @FormUrlEncoded
    @POST("sender/getTheOrderInformation")
    Call<Map<String,Object>> postgetTheOrderInformation(@Field("oid") int oid);

    @FormUrlEncoded
    @POST("sender/finishorder")
    Call<Map<String,Object>> finishorder(@Field("oid") int oid);

    @FormUrlEncoded
    @POST("user/searchorderbyId")
    Call<Map<String,Object>> postgetTheOrderOstatus(@Field("oid") int oid);
}
