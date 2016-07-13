package com.softdesign.devintensive.data.managers;

import android.content.SharedPreferences;
import android.net.Uri;

import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevintensiveApplication;

import java.util.ArrayList;
import java.util.List;

public class PreferenceManager {

    private static final String[] USER_NAME = {
            ConstantManager.USER_FIRST_NAME,
            ConstantManager.USER_SECOND_NAME,
            ConstantManager.USER_MAIL_NAME};

    public static final String[] USER_FIELDS = {
            ConstantManager.USER_PHONE_KEY,
            ConstantManager.USER_MAIL_KEY,
            ConstantManager.USER_VK_KEY,
            ConstantManager.USER_GITHUB_KEY,
            ConstantManager.USER_BIO_KEY};

    public static final String[] USER_VALUES = {
            ConstantManager.USER_RATING_KEY,
            ConstantManager.USER_CODE_LINES_KEY,
            ConstantManager.USER_PROJECT_KEY};

    private SharedPreferences mSharedPreferences;

    public PreferenceManager() {
        this.mSharedPreferences = DevintensiveApplication.getSharedPreferences();
    }
    //Сохраняем поля в User Profile
    public void saveUserProfileData(List<String> userFields) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for (int i = 0; i < USER_FIELDS.length; i++) {
            editor.putString(USER_FIELDS[i], userFields.get(i));
        }
        editor.apply();
    }

    //Добавляем поля в User Profile
    public List<String> loadUserProfileData() {
        List<String> userFields = new ArrayList<>();
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_PHONE_KEY,"89000000000"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_MAIL_KEY,"user@mail.ru"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_VK_KEY,"vk.com/"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_GITHUB_KEY,"github.com/"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_BIO_KEY,"О себебебесебе"));
        return userFields;
    }

    public Uri loadUserPhoto() {
        return Uri.parse(mSharedPreferences.getString(ConstantManager.USER_PHOTO_KEY, ConstantManager.FIRST_USER_PHOTO));
    }
    public void saveUserPhoto(Uri uri) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_PHOTO_KEY, uri.toString());
        editor.apply();
    }

    public Uri loadAvatarImage() {
        return Uri.parse(mSharedPreferences.getString(ConstantManager.USER_AVATAR_KEY, ConstantManager.FIRST_IMAGE_AVATAR));
    }
    public void saveAvatarImage(Uri uri) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_AVATAR_KEY, uri.toString());
        editor.apply();
    }

    //Добавляем поля в редактируемые данные в User Profile
    public List<String> loadUserProfileValues() {
        List<String> userValues = new ArrayList<>();
        userValues.add(mSharedPreferences.getString(ConstantManager.USER_RATING_KEY,"0"));
        userValues.add(mSharedPreferences.getString(ConstantManager.USER_CODE_LINES_KEY,"0"));
        userValues.add(mSharedPreferences.getString(ConstantManager.USER_PROJECT_KEY,"0"));

        return userValues;
    }

    //Сохраняем поля в редактируемых данных в User Profile
    public void saveUserProfileValues(int[] userValues){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for (int i = 0; i < USER_VALUES.length; i++) {
            editor.putString(USER_VALUES[i],String.valueOf(userValues[i]));
        }
        editor.apply();
    }

    //Добавляем поля в имя пользователя
    public List<String> loadUserName() {
        List<String> userNames = new ArrayList<>();
        userNames.add(mSharedPreferences.getString(ConstantManager.USER_FIRST_NAME, " "));
        userNames.add(mSharedPreferences.getString(ConstantManager.USER_SECOND_NAME, " "));
        userNames.add(mSharedPreferences.getString(ConstantManager.USER_MAIL_NAME, " "));
        return userNames;
    }

    //Сохраняем имя пользователя
    public void saveUserName(String[] userName) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        for (int i = 0; i < USER_NAME.length; i++) {
            editor.putString(USER_NAME[i], userName[i]);
        }
        editor.apply();
    }

    public void saveAuthToken(String authToken) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.AUTH_TOKEN_KEY, authToken);
        editor.apply();
    }

    public String getAuthToken() {
        return mSharedPreferences.getString(ConstantManager.AUTH_TOKEN_KEY, "null");
    }

    public void saveUserId(String userId) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_ID_KEY, userId);
        editor.apply();
    }

    public String getUserId() {
        return mSharedPreferences.getString(ConstantManager.USER_ID_KEY, "null");
    }
}
