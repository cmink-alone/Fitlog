package com.example.trio.fitlog.utils;

public class Constant {
    public static final String BASE_URL="https://fitlog-api.herokuapp.com/";

    public static class URL{
        public static String carImage(String file){
            return BASE_URL+"images/car/"+file;
        }
        public static String api(){
            return BASE_URL+"api/";
        }
    }
}
