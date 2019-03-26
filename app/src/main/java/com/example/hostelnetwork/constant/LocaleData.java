package com.example.hostelnetwork.constant;

public class LocaleData {
    public static final String ROLE_USER = "user";

    public static final String AUTHENCATION_CHECKLOGIN_URL = "https://hostel-network-api.dev1star.com/authentication?";

    public static final String POST_GETALL = "https://hostel-network-api.dev1star.com/posts";

    public static final String POST_BY_ID_URL = "https://hostel-network-api.dev1star.com/posts/";

    public static final String PICTURE = "https://hostel-network-api.dev1star.com/pictures";

    public static final String PICTURE_IN_POST_URL = "https://hostel-network-api.dev1star.com/pictures?postId=";

    public static final String USER_GET_USER_BY_ID_URL = "https://hostel-network-api.dev1star.com/users/";

    public static final String USER_UPDATE_URL = "https://hostel-network-api.dev1star.com/users/";
    public static final String USER_CREATE_URL = "https://hostel-network-api.dev1star.com/users";

    public static final String WISHLIST_GET_BY_USER = "https://hostel-network-api.dev1star.com/wishLists?userId=";
    public static final String WISHLIST_ADD_NEW_URL = "https://hostel-network-api.dev1star.com/wishLists?userId=";
    public static final String WISHLIST_REMOVE_URL = "https://hostel-network-api.dev1star.com/wishLists?userId=";
    public static final String POST_SAVED_BY_USER_URL = "https://hostel-network-api.dev1star.com/posts/users?saved=true&userId=";

    public static final String POST_CREATED_BY_USER_URL = "https://hostel-network-api.dev1star.com/posts/users?created=true&userId=";

    public static final String APPOINTMENT_GETALL_CREATED_URL = "https://hostel-network-api.dev1star.com/appointments?created=";
    public static final String APPOINTMENT_CREATE_URL = "https://hostel-network-api.dev1star.com/appointments";
    public static final String APPOINTMENT_UPDATE_URL = "https://hostel-network-api.dev1star.com/appointments/";

    public static final String BENEFIT_GET_BY_USER_ID_URL = "https://hostel-network-api.dev1star.com/benefits?postId=";
    public static final String BENEFIT_GET_ALL_URL = "https://hostel-network-api.dev1star.com/benefits";


    public static final String TYPE_GET_ALL_URL = "https://hostel-network-api.dev1star.com/types";
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS";

    public static final String IMGUR_API = "https://api.imgur.com/3/upload";


    public static boolean HandleErrorMessageResponse(Integer statusResponse){
        switch (statusResponse){
            case 500:
                return false;
            case 401:
                return false;
            case 404:
                return false;
            case 204:
                return false;
            case 200:
                return true;
        }
        return true;
    }
}
