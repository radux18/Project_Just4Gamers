package com.example.project_just4gamers.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

public class Constants {
    private static final String  USERS = "users";
    private static final String PRODUCTS = "products";
    public static final String ORDERS = "orders";
    public static final String ADDRESSES = "addresses";
    public static final String CART_ITEMS = "cart_items";
    public static final String SOLD_PRODUCTS = "sold_products";
    public static final String DISCOUNTCOUPONS = "discount_coupons";
    public static final String MESSAGES = "messages";
    public static final String REVIEWS = "reviews";

    public static final String POINTS = "points";

    private static final String PREFS = "prefs";
    private static final String USERNAME = "username";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    private static final String EXTRA_USER_DETAILS = "extra_user_details";
    private static final int READ_STORAGE_PERMISSION_CODE = 213;
    private static final int PICK_IMAGE_REQUEST_CODE = 214;

    private static final String MALE = "Barbat";
    private static final String FEMALE = "Femeie";

    private static final String NEW = "Nou";
    private static final String USED = "Uzat";

    private static final String HOME = "Home";
    private static final String OFFICE = "Office";
    private static final String OTHER = "Other";

    private static final String MOBILE = "mobile";
    private static final String GENDER = "gender";
    private static final String IMAGE = "image";
    private static final String COMPLETE_PROFILE = "profileCompleted";

    private static final String PRODUCT_IMAGE = "Product_Image";

    private static final String USER_ID = "user_id";

    public static final String USER_PROFILE_IMAGE = "User_Profile_Image";

    public static final String EXTRA_PRODUCT_ID = "extra_product_id";

    public static final String EXTRA_PRODUCT_OWNER_ID = "extra_product_owner_id";

    public static final String DEFAULT_CART_QUANTITY = "1";

    public static final String PRODUCT_ID = "product_id";

    public static final String CART_QUANTITY = "cart_quantity";

    public static final String EXTRA_ADDRESS_DETAILS = "AddressDetails";
    public static final String EXTRA_SELECT_ADDRESS = "extra_select_address";
    public static final String EXTRA_SELECTED_ADDRESS = "extra_selected_address";
    public static final int ADD_ADDRESS_REQUEST_CODE = 121;

    public static final String STOCK_QUANTITY = "stock_quantity";

    public static final String EXTRA_MY_ORDER_DETAILS = "extra_my_order_details";

    public static final String EXTRA_DETAILS_MESSAGE = "extra_details_message";

    public static final String EXTRA_SOLD_PRODUCT_DETAILS = "extra_sold_product_details";

    public static final String EXTRA_COUPON = "extra_coupon";

    public static final String RECEIVER_ID = "receiver_id";
    public static final String SENDER_ID = "sender_id";

    public static final String EXTRA_SELECT_MESSAGE = "extra_select_message";
    public static final String EXTRA_BOOLEAN = "extra_boolean";

    public static final String EXTRA_CURRENT_USER = "extra_current_user";
    public static final String EXTRA_SENDER_USER = "extra_sender_user";

    public static final String EXTRA_PRODUCT_REVIEW = "extra_product_review";

    public static final String PRODUCT_OWNER_ID = "productOwner_id";

    public static final String EXTRA_PROFILE_DETAILS = "extra_profile_details";

    public static final String EXTRA_PROFILE_DETAILSV2 = "extra_profile_detailsV2";

    public static final String EXTRA_PROFILE_DETAILS_VISITOR = "extra_profile_details_visitor";

    public static final String EXTRA_RECEIVER_ID = "userProfile_id";

    public static String getExtraProfileDetailsv2() {
        return EXTRA_PROFILE_DETAILSV2;
    }

    public static String getExtraReceiverId() {
        return EXTRA_RECEIVER_ID;
    }

    public static String getExtraProfileDetailsVisitor() {
        return EXTRA_PROFILE_DETAILS_VISITOR;
    }

    public static String getExtraProfileDetails() {
        return EXTRA_PROFILE_DETAILS;
    }

    public static String getProductOwnerId() {
        return PRODUCT_OWNER_ID;
    }

    public static String getExtraProductReview() {
        return EXTRA_PRODUCT_REVIEW;
    }

    public static String getREVIEWS() {
        return REVIEWS;
    }

    public static String getExtraCurrentUser() {
        return EXTRA_CURRENT_USER;
    }

    public static String getExtraSenderUser() {
        return EXTRA_SENDER_USER;
    }

    public static String getExtraBoolean() {
        return EXTRA_BOOLEAN;
    }

    public static String getExtraSelectMessage() {
        return EXTRA_SELECT_MESSAGE;
    }

    public static String getSenderId() {
        return SENDER_ID;
    }

    public static String getReceiverId() {
        return RECEIVER_ID;
    }

    public static String getExtraDetailsMessage() {
        return EXTRA_DETAILS_MESSAGE;
    }


    public static String getMESSAGES() {
        return MESSAGES;
    }

    public static String getExtraCoupon() {
        return EXTRA_COUPON;
    }

    public static String getDISCOUNTCOUPONS() {
        return DISCOUNTCOUPONS;
    }

    public static String getPOINTS() {
        return POINTS;
    }

    public static String getExtraSoldProductDetails() {
        return EXTRA_SOLD_PRODUCT_DETAILS;
    }

    public static String getSoldProducts() {
        return SOLD_PRODUCTS;
    }

    public static String getExtraMyOrderDetails() {
        return EXTRA_MY_ORDER_DETAILS;
    }

    public static String getStockQuantity() {
        return STOCK_QUANTITY;
    }

    public static String getORDERS() {
        return ORDERS;
    }

    public static String getExtraSelectedAddress() {
        return EXTRA_SELECTED_ADDRESS;
    }

    public static int getAddAddressRequestCode() {
        return ADD_ADDRESS_REQUEST_CODE;
    }

    public static String getExtraSelectAddress() {
        return EXTRA_SELECT_ADDRESS;
    }

    public static String getExtraAddressDetails() {
        return EXTRA_ADDRESS_DETAILS;
    }

    public static String getADDRESSES() {
        return ADDRESSES;
    }

    public static String getHOME() {
        return HOME;
    }

    public static String getOFFICE() {
        return OFFICE;
    }

    public static String getOTHER() {
        return OTHER;
    }

    public static String getCartQuantity() {
        return CART_QUANTITY;
    }

    public static String getProductId() {
        return PRODUCT_ID;
    }

    public static String getCartItems() {
        return CART_ITEMS;
    }

    public static String getDefaultCartQuantity() {
        return DEFAULT_CART_QUANTITY;
    }

    public static String getExtraProductOwnerId() {
        return EXTRA_PRODUCT_OWNER_ID;
    }

    public static String getExtraProductId() {
        return EXTRA_PRODUCT_ID;
    }

    public static String getUserId() {
        return USER_ID;
    }

    public static String getPRODUCTS() {
        return PRODUCTS;
    }

    public static String getNEW() {
        return NEW;
    }

    public static String getUSED() {
        return USED;
    }

    public static String getProductImage() {
        return PRODUCT_IMAGE;
    }

    public static String getIMAGE() {
        return IMAGE;
    }

    public static String getUSERS() {
        return USERS;
    }

    public static String getPREFS() {
        return PREFS;
    }

    public static String getUSERNAME() {
        return USERNAME;
    }

    public static String getExtraUserDetails() {
        return EXTRA_USER_DETAILS;
    }

    public static int getReadStoragePermissionCode() {
        return READ_STORAGE_PERMISSION_CODE;
    }

    public static String getMALE() {
        return MALE;
    }

    public static String getFEMALE() {
        return FEMALE;
    }

    public static String getMOBILE() {
        return MOBILE;
    }

    public static String getGENDER() {
        return GENDER;
    }

    public static int getPickImageRequestCode() {
        return PICK_IMAGE_REQUEST_CODE;
    }

    public static String getUserProfileImage() {
        return USER_PROFILE_IMAGE;
    }

    public static String getCompleteProfile() {
        return COMPLETE_PROFILE;
    }

    public static String getFirstName() {
        return FIRST_NAME;
    }

    public static String getLastName() {
        return LAST_NAME;
    }

    public Constants() {
    }

    public void showImageChooser(Activity activity){
        //An intent for launching the image selection of phone storage.
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(galleryIntent,Constants.getPickImageRequestCode());
    }

    public String getFileExtension(Activity activity, Uri uri){

        //MimeTypeMap : Two-way map that maps MIME-types to file extensions and vice-versa.

        //getSingleton() : Get the singleton instance of MimeTypeMap;

        //getExtensionFromMimeType: Return the registered extension from the given MIME type;

        //contentResolver.getType: Return the MIME type of the given content URL.

        return MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(activity.getContentResolver().getType(uri));
    }


}
