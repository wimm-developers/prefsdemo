/*
  * Copyright (C) 2012 WIMM Labs Incorporated
 */
package com.wimm.prefsdemo;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wimm.framework.app.LauncherActivity;
import com.wimm.framework.provider.SyncPreference;
import com.wimm.framework.provider.OAuthPreference;

/*
 *
 *
 */
public class PrefsActivity extends LauncherActivity{
    public static final String TAG = PrefsActivity.class.getSimpleName();

    // These are the prefs we know about. The value of these constants must
    // match the value of the 'name' attribute in the preference schema for this
    // app that was uploaded to the WIMM Micro App Store.
    protected static final String FACEBOOK_OAUTH_PREF = "my_facebook_auth";
    protected static final String TWITTER_OAUTH_PREF = "my_twitter_auth";
    protected static final String INSTAGRAM_OAUTH_PREF = "my_instagram_auth";
    protected static final String LOCATION_PREF = "my_location";
    protected static final String LOCATION_LIST_PREF = "my_location_list";

    // A list of intents that we expect to receive from PrefsReceiver.
    public static final String ACTION_FACEBOOK_OAUTH_MOD = "com.wimm.prefsdemo.action.UPDATE_FACEBOOK";
    public static final String ACTION_TWITTER_OAUTH_MOD = "com.wimm.prefsdemo.action.UPDATE_TWITTER";
    public static final String ACTION_INSTAGRAM_OAUTH_MOD = "com.wimm.prefsdemo.action.UPDATE_INSTAGRAM";
    public static final String ACTION_LOCATION_MOD = "com.wimm.prefsdemo.action.UPDATE_LOCATION";

    private ScrollView mScrollView;
    
    private TextView mFacebookUsername;
    private TextView mFacebookUserId;
    private TextView mFacebookAccessToken;
    
    private TextView mInstagramUsername;
    private TextView mInstagramUserId;
    private TextView mInstagramAccessToken;
    
    private TextView mTwitterUsername;
    private TextView mTwitterUserId;
    private TextView mTwitterAccessToken;
    private TextView mTwitterAccessTokenSecret;

	private TextView mLocationLocality;
	private TextView mLocationAdminArea;
	private TextView mLocationCountryName;
	private TextView mLocationCountryCode;
	private TextView mLocationLatitude;
	private TextView mLocationLongitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mScrollView = (ScrollView) findViewById(R.id.scroll_view);
        
        mFacebookUsername = (TextView) findViewById(R.id.facebook_username);
        mFacebookUserId = (TextView) findViewById(R.id.facebook_user_id);
        mFacebookAccessToken = (TextView) findViewById(R.id.facebook_access_token);

        mInstagramUsername = (TextView) findViewById(R.id.instagram_username);
        mInstagramUserId = (TextView) findViewById(R.id.instagram_user_id);
        mInstagramAccessToken = (TextView) findViewById(R.id.instagram_access_token);
        
        mTwitterUsername = (TextView) findViewById(R.id.twitter_username);
        mTwitterUserId = (TextView) findViewById(R.id.twitter_user_id);
        mTwitterAccessToken = (TextView) findViewById(R.id.twitter_access_token);
        mTwitterAccessTokenSecret = (TextView) findViewById(R.id.twitter_access_token_secret);
        
        mLocationLocality = (TextView) findViewById(R.id.location_locality);
        mLocationAdminArea = (TextView) findViewById(R.id.location_admin_area);
        mLocationCountryName = (TextView) findViewById(R.id.location_country_name);
        mLocationCountryCode = (TextView) findViewById(R.id.location_country_code);
        mLocationLatitude = (TextView) findViewById(R.id.location_latitude);
        mLocationLongitude = (TextView) findViewById(R.id.location_longitude);
        
        updateFacebookOAuth();
        updateTwitterOAuth();
        updateInstagramOAuth();
        updateLocation();
        
        // Register for broadcast intents.
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_FACEBOOK_OAUTH_MOD);
        filter.addAction(ACTION_TWITTER_OAUTH_MOD);
        filter.addAction(ACTION_INSTAGRAM_OAUTH_MOD);
        filter.addAction(ACTION_FACEBOOK_OAUTH_MOD);
        filter.addAction(ACTION_LOCATION_MOD);
        this.registerReceiver(mReceiver, filter);
    }
    
    void updateFacebookOAuth() {
        OAuthPreference pref = SyncPreference.getOAuth(this, FACEBOOK_OAUTH_PREF);
        if (pref != null) {
            mFacebookUsername.setText(pref.getUsername());
            mFacebookUserId.setText(pref.getUserId());
            mFacebookAccessToken.setText(pref.getAccessToken());
        } else {
            mFacebookUsername.setText("");
            mFacebookUserId.setText("");
            mFacebookAccessToken.setText("");
        }
    }

    void updateTwitterOAuth() {
        OAuthPreference pref = SyncPreference.getOAuth(this, TWITTER_OAUTH_PREF);
        if (pref != null) {
            mTwitterUsername.setText(pref.getUsername());
            mTwitterUserId.setText(pref.getUserId());
            mTwitterAccessToken.setText(pref.getAccessToken());
            mTwitterAccessTokenSecret.setText(pref.getAccessTokenSecret());
        } else {
            mTwitterUsername.setText("");
            mTwitterUserId.setText("");
            mTwitterAccessToken.setText("");
            mTwitterAccessTokenSecret.setText("");
        }
    }
    
    void updateInstagramOAuth() {
        OAuthPreference pref = SyncPreference.getOAuth(this, INSTAGRAM_OAUTH_PREF);
        if (pref != null) {
            mInstagramUsername.setText(pref.getUsername());
            mInstagramUserId.setText(pref.getUserId());
            mInstagramAccessToken.setText(pref.getAccessToken());
        } else {
            mInstagramUsername.setText("");
            mInstagramUserId.setText("");
            mInstagramAccessToken.setText("");
        }
    }

    void updateLocation() {

        Address loc = SyncPreference.getAddress(this, LOCATION_PREF, null);
        if (loc != null) {
			// For US addresses, Locality and Admin Area are city and state, respectively
            mLocationLocality.setText(loc.getLocality());
            mLocationAdminArea.setText(loc.getAdminArea());
            mLocationCountryName.setText(loc.getCountryName());
            mLocationCountryCode.setText(loc.getCountryCode());
            if (loc.hasLatitude()) {
            	mLocationLatitude.setText(Double.toString(loc.getLatitude()));
            } else {
            	mLocationLatitude.setText("");
            }
            if (loc.hasLongitude()) {
            	mLocationLongitude.setText(Double.toString(loc.getLongitude()));
            } else {
            	mLocationLongitude.setText("");
            }
            
        } else {
            mLocationLocality.setText("");
            mLocationAdminArea.setText("");
            mLocationCountryName.setText("");
            mLocationCountryCode.setText("");
            mLocationLatitude.setText("");
            mLocationLongitude.setText("");
        }
    }

    // This receiver is created at runtime to listen for intents from
    // our PrefsReceiver
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_FACEBOOK_OAUTH_MOD)) {
                updateFacebookOAuth();
            } else if (action.equals(ACTION_TWITTER_OAUTH_MOD)) {
                updateTwitterOAuth();
            } else if (action.equals(ACTION_INSTAGRAM_OAUTH_MOD)) {
                updateInstagramOAuth();
            } else if (action.equals(ACTION_LOCATION_MOD)) {
                updateLocation();
            }
        }
    };

    @Override
    public boolean dragCanExit() {
        return (mScrollView.getScrollY() == 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.unregisterReceiver(mReceiver);
    }

}