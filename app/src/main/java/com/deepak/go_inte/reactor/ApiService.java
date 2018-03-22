package com.deepak.go_inte.reactor;


import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface ApiService {

    @GET("api.php")
    Single<List<Credit>> getCredit();


}
