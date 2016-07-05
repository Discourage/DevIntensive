package com.softdesign.devintensive.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.ValidationText.EditTextMail;
import com.softdesign.devintensive.utils.ValidationText.EditTextTelefonMask;
import com.softdesign.devintensive.utils.ValidationText.EditTextUri;
import com.softdesign.devintensive.utils.RoundedAvatarDrawable;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private int mCurrentEditMode = 0;

    public static final String TAG = ConstantManager.TAG_PREFIX + "Main Activity";

    private DataManager mDataManager;
    private ImageView mProfileAvatar;
    private List<EditText> mUserInfoViews;

    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.profile_placeholder)
    RelativeLayout mProfilePlaceholder;
    @BindView(R.id.call_phone_user)
    ImageView mCallPhone;
    @BindView(R.id.send_mail_user)
    ImageView mSendMail;
    @BindView(R.id.open_vk_user)
    ImageView mOpenVk;
    @BindView(R.id.open_git_user)
    ImageView mOpenGit;
    @BindView(R.id.mobile_et)
    EditText mUserPhone;
    @BindView(R.id.email_et)
    EditText mUserMail;
    @BindView(R.id.vk_et)
    EditText mUserVk;
    @BindView(R.id.git_et)
    EditText mUserGit;
    @BindView(R.id.about_et)
    EditText mUserBio;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.user_foto)
    ImageView mProfileImage;
    @BindView(R.id.navigation_drawer)
    DrawerLayout mNavigationDrawer;
    @BindView(R.id.main_coordinator_container)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar_layout)
    AppBarLayout mAppBarLayout;

    private AppBarLayout.LayoutParams mAppBarParams = null;
    private File mPhotoFile = null;
    private Uri mSelectedImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Log.d(TAG, "onCreate");

        mDataManager = DataManager.getINSTANCE();

        mUserInfoViews = new ArrayList<>();
        mUserInfoViews.add(mUserPhone);
        mUserInfoViews.add(mUserMail);
        mUserInfoViews.add(mUserVk);
        mUserInfoViews.add(mUserGit);
        mUserInfoViews.add(mUserBio);

        mFab.setOnClickListener(this);
        mProfilePlaceholder.setOnClickListener(this);

        mCallPhone.setOnClickListener(this);
        mSendMail.setOnClickListener(this);
        mOpenVk.setOnClickListener(this);
        mOpenGit.setOnClickListener(this);

        mUserPhone.addTextChangedListener(EditTextTelefonMask.insert(mUserPhone));
        mUserMail.addTextChangedListener(EditTextMail.insert(mUserMail));
        mUserVk.addTextChangedListener(EditTextUri.insert(mUserVk));
        mUserGit.addTextChangedListener(EditTextUri.insert(mUserGit));
        setupToolbar();
        setupDrawer();
        loadUserInfoValue();
        makeRoundAvatar();

        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.white));
        mCollapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
        Picasso.with(this)
                .load(mDataManager.getPreferenceManager().loadUserPhoto())
                .placeholder(R.mipmap.user_bg)
                .into(mProfileImage);

        if (savedInstanceState == null) {
//            активити запускается впервые
        } else {
            mCurrentEditMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY, 0);
            changeEditMode(mCurrentEditMode);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");

        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        saveUserInfoValue();
    }


    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart");
        super.onRestart();
    }

    //  Если включен NavigationDrawer, кнопка "назад" закрывает его
    @Override
    public void onBackPressed() {
        if (mNavigationDrawer != null) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //Обработка нажатий пользователя
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                // открытие/закрытие режима редактирования профиля
                if (mCurrentEditMode == 0) {
                    changeEditMode(1);
                    mCurrentEditMode = 1;
                } else {
                    changeEditMode(0);
                    mCurrentEditMode = 0;
                }
                break;
            case R.id.profile_placeholder:
                // сделать выбор откуда загружать фото
                showDialog(ConstantManager.LOAD_PROFILE_PHOTO);
                break;

            case R.id.call_phone_user:
                // сделать звонок по введеному телефону
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                    Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mUserPhone.getText().toString()));
                    startActivity(callIntent);

                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}
                            , ConstantManager.CALL_PHONE_REQUEST_PERMISSION_CODE);
                }
                break;
            case R.id.send_mail_user:
                //  отправить письмо по указаннму email
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + mUserMail.getText().toString()));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Тема");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Текст");
                startActivity(Intent.createChooser(emailIntent, "Chooser Title"));
                break;
            case R.id.open_vk_user:
                // открыть страницу vk
                Intent vkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + mUserVk.getText().toString()));
                startActivity(vkIntent);
                break;
            case R.id.open_git_user:
                // открыть страницу github
                Intent gitIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + mUserGit.getText().toString()));
                startActivity(gitIntent);
                break;
        }

    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);
    }

    //Реализация  Snackbar
    private void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    //Реализация toolbar
    private void setupToolbar() {
        setSupportActionBar(mToolbar);

        mAppBarParams = (AppBarLayout.LayoutParams) mCollapsingToolbar.getLayoutParams();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    //Реализация navigation drawer
    private void setupDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                showSnackBar(item.getTitle().toString());
                item.setChecked(true);
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    //получение фото из другой Activity (от камеры или галереи)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ConstantManager.REQUEST_GALLERY_PICTURE:
                if (resultCode == RESULT_OK && data != null) {
                    mSelectedImage = data.getData();
                    insertProfileImage(mSelectedImage);
                }
                break;
            case ConstantManager.REQUEST_CAMERA_PICTURE:
                if (resultCode == RESULT_OK && mPhotoFile != null) {
                    mSelectedImage = Uri.fromFile(mPhotoFile);
                    insertProfileImage(mSelectedImage);
                }
                break;
        }
    }

    //  Реализация программного скругления аватара
    private void makeRoundAvatar() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        mProfileAvatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.user_avatar);
        Bitmap btMap = BitmapFactory.decodeResource(getResources(), R.drawable.user_avatar);
        mProfileAvatar.setImageBitmap(RoundedAvatarDrawable.getRoundedCornerBitmap(btMap));
    }

    /*перключает режим редактирования
    *@param mode если 1 режим редактирования,если 0 режим просмотра
    * */
    private void changeEditMode(int mode) {
        if (mode == 1) {
            mFab.setImageResource(R.drawable.ic_done_black_24dp);

            for (EditText userValue : mUserInfoViews) {
                userValue.setEnabled(true);
                userValue.setFocusable(true);
                userValue.setFocusableInTouchMode(true);
                showProfilePlaceholder();

                mCallPhone.setEnabled(false);
                mSendMail.setEnabled(false);
                mOpenVk.setEnabled(false);
                mOpenGit.setEnabled(false);

                mUserPhone.requestFocus();
                lockToolbar();
                mCollapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);

            }
        } else {
            mFab.setImageResource(R.drawable.ic_create_black_24dp);
            for (EditText userValue : mUserInfoViews) {
                userValue.setEnabled(false);
                userValue.setFocusable(false);
                userValue.setFocusableInTouchMode(false);

                mCallPhone.setEnabled(true);
                mSendMail.setEnabled(true);
                mOpenVk.setEnabled(true);
                mOpenGit.setEnabled(true);

                saveUserInfoValue();
                hideProfilePlaceholder();
                unlockToolbar();

                mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.white));
            }

        }
    }

    // Загружаем данные профиля из PreferenceManager
    private void loadUserInfoValue() {
        List<String> userData = mDataManager.getPreferenceManager().loadUserProfileData();
        for (int i = 0; i < userData.size(); i++) {
            mUserInfoViews.get(i).setText(userData.get(i));

        }
    }

    // Сохраняем данные профиля из PreferenceManager
    private void saveUserInfoValue() {
        List<String> userData = new ArrayList<>();
        for (EditText userFieldView : mUserInfoViews) {
            userData.add(userFieldView.getText().toString());
        }
        mDataManager.getPreferenceManager().saveUserProfileData(userData);
    }

    // Загрузка фото из галлереи
    private void loadPhotoFromGallery() {
        Intent takeGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        takeGalleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(takeGalleryIntent, getString(R.string.user_profile_choose_message)), ConstantManager.REQUEST_GALLERY_PICTURE);
    }

    // Загрузка снимка с камеры телефона
    private void loadPhotoFromCamera() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            Intent takeCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                mPhotoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
                // Обработать ошибку
            }
            if (mPhotoFile != null) {
                // передать фото в интент
                takeCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(takeCaptureIntent, ConstantManager.REQUEST_CAMERA_PICTURE);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, ConstantManager.CAMERA_REQUEST_PERMISSION_CODE);
        }

        Snackbar.make(mCoordinatorLayout, getString(R.string.load_foto_not_cancel_message), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.allow_message), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openApplicationSettings();
                    }
                }).show();
    }

    // Обработка разрешений(разрешение получено)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ConstantManager.CAMERA_REQUEST_PERMISSION_CODE && grantResults.length == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
        if (requestCode == ConstantManager.CALL_PHONE_REQUEST_PERMISSION_CODE && grantResults.length == 2) {
            if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

    //Скрыть место для загрузки фотографии профиля
    private void hideProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.GONE);
    }

    //Показать место для загрузки фотографии профиля
    private void showProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.VISIBLE);
    }

    //Блокировка toolbar в режиме редактирования
    private void lockToolbar() {
        mAppBarLayout.setExpanded(true, true);
        mAppBarParams.setScrollFlags(0);
        mCollapsingToolbar.setLayoutParams(mAppBarParams);
    }

    //Разблокировка toolbar при выходе из режима редактирования
    private void unlockToolbar() {
        mAppBarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        mCollapsingToolbar.setLayoutParams(mAppBarParams);
    }

    //  Метод создания диалога для выбора загрузки фото профиля
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case ConstantManager.LOAD_PROFILE_PHOTO:
                String[] selectItems = {
                        getString(R.string.user_profile_photo_gallery),
                        getString(R.string.user_profile_photo_camera),
                        getString(R.string.user_profile_photo_cancel)};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.user_profile_photo_title)
                        .setItems(selectItems, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int choiceItem) {
                                switch (choiceItem) {
                                    case 0:
                                        //TODO "Загрузить из галлереи"
                                        loadPhotoFromGallery();
//                                        showSnackBar("Загрузить из галлереи");
                                        break;
                                    case 1:
                                        //TODO "Сделать снимок"
                                        loadPhotoFromCamera();
//                                        showSnackBar("Сделать снимок");
                                        break;
                                    case 2:
                                        //TODO "Отмена"
                                        dialogInterface.cancel();
//                                        showSnackBar("Отмена");
                                        break;
                                }
                            }
                        });
                return builder.create();

            default:
                return null;
        }
    }

    // Создание снимка
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
        values.put(MediaStore.MediaColumns.DATA, image.getAbsolutePath());

        this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        return image;

    }

    //Загрузка выбранного изображения в профиль
    private void insertProfileImage(Uri selectedImage) {
        Picasso.with(this)
                .load(selectedImage)
                .into(mProfileImage);
        mDataManager.getPreferenceManager().saveUserPhoto(selectedImage);
    }

    //Открытие системных настроек
    public void openApplicationSettings() {
        Intent appSettingIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
        startActivityForResult(appSettingIntent, ConstantManager.PERMISSION_REQUEST_SETTINGS_CODE);
    }
}
