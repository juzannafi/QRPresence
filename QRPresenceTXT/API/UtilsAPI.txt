package com.example.retrofittest.API;

public class UtilsAPI {

    public static final String BASE_URL_API = "http://qrpresence.herokuapp.com/api/user/";


    public static GetService getAPIService() {
        return RetrofitClient.getClient(BASE_URL_API).create(GetService.class);
    }
}
