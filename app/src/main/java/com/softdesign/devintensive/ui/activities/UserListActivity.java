package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.UsersAdapter;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.RoundedAvatarDrawable;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListActivity extends AppCompatActivity {
    private static final String TAG = ConstantManager.TAG_PREFIX + " UserListActivity";

    @BindView(R.id.main_coordinator_container)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.navigation_drawer)
    DrawerLayout mNavigationDrawer;
    @BindView(R.id.user_list)
    RecyclerView mRecyclerView;

    private ImageView mProfileAvatar;
    private TextView mUserName;
    private TextView mUserNameEmail;
    private DataManager mDataManager;
    private UsersAdapter mUsersAdapter;
    private List<UserListRes.UserData> mUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        ButterKnife.bind(this);

        mDataManager = DataManager.getInstance();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        setupToolbar();
        setupDrawer();
        makeRoundAvatar();
        loadUsers();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }
    //Реализация  Snackbar
    private void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }
    //Реализация navigation drawer
    private void setupDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        mProfileAvatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.user_avatar);
        mUserName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_name_text);
        mUserNameEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.user_email_text);
        initUserName();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.navdrawer_user_profile_menu:
                        Intent teamIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(teamIntent);
                        break;

                    case R.id.navdrawer_team_menu:
                        showSnackBar(item.getTitle().toString());
                        item.setChecked(true);
                        mNavigationDrawer.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.navdrawer_user_logout_menu:
                        Intent logoutIntent = new Intent(getApplicationContext(), AuthActivity.class);
                        startActivity(logoutIntent);
                        break;

                }

                return false;
            }
        });
    }
    //  Реализация программного скругления аватара
    private void makeRoundAvatar() {
        Picasso.with(this)
                .load(mDataManager.getPreferenceManager().loadAvatarImage())
                .placeholder(R.drawable.user_avatar)
                .transform(new RoundedAvatarDrawable())
                .into(mProfileAvatar);
    }
    //Реализация toolbar
    private void setupToolbar() {
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    // Загружаем данные команды
    private void loadUsers() {
        Call<UserListRes> call = mDataManager.getUserList();
        call.enqueue(new Callback<UserListRes>() {

            @Override
            public void onResponse(Call<UserListRes> call, Response<UserListRes> response) {
                try {
                    mUsers = response.body().getData();
                    mUsersAdapter = new UsersAdapter(mUsers, new UsersAdapter.UserViewHolder.CustomClickListener() {
                        @Override
                        public void onUserItemClickListener(int position) {
                            UserDTO userDTO = new UserDTO(mUsers.get(position));

                            Intent profileIntent = new Intent(UserListActivity.this, ProfileUserActivity.class);
                            profileIntent.putExtra(ConstantManager.PARCELABLE_KEY, userDTO);

                            startActivity(profileIntent);
                        }
                    });

                    mRecyclerView.setAdapter(mUsersAdapter);
                } catch (NullPointerException e) {
                    Log.d(TAG, e.toString());
                    showSnackBar("Что-то пошло не так");
                }


            }

            @Override
            public void onFailure(Call<UserListRes> call, Throwable t) {

            }
        });
    }
    // Загружаем имя,фамилию и почту из PreferenceManager
    private void initUserName() {
        List<String> userNameValues = mDataManager.getPreferenceManager().loadUserName();
        mUserName.setText(userNameValues.get(0).toString() + " " + userNameValues.get(1).toString());
        mUserNameEmail.setText(userNameValues.get(2).toString());
    }
}
