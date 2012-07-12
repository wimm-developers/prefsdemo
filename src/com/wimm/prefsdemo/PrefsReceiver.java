/*
  * Copyright (C) 2012 WIMM Labs Incorporated
 */
package com.wimm.prefsdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.util.ArrayList;

/*
 * The device might sync while your activity is running, in which case you'll
 * want to update your running activity with the new preferences. Similarly, you
 * might have a service running that needs to be informed of the updated sync
 * preferences. Due to the way that Android's PackageManager works, developers
 * need to register a receiver in the manifest file: creating one at runtime in
 * the activity or service is not an option.
 * This is the receiver registered in the manifest.
 */
public class PrefsReceiver extends BroadcastReceiver {
    private static final String TAG = PrefsReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action.equals(com.wimm.framework.service.SyncService.Intent.ACTION_NOTIFY_PREFS_MOD)) {
            
            // A list of prefs names that were modified is provided on the
            // intent.
            // These names are the names specified in the 'name' attribute on
            // the preferences schema. Only prefs that were modified will be
            // included in this list.
            ArrayList<String> modifiedPrefs = intent.getStringArrayListExtra(
                    com.wimm.framework.service.SyncService.Intent.EXTRA_MODIFIED_PREFS);

            if(modifiedPrefs != null) {
                for (String name : modifiedPrefs){
                    if (name.equals(PrefsActivity.FACEBOOK_OAUTH_PREF)) {
                        context.sendBroadcast(new Intent(PrefsActivity.ACTION_FACEBOOK_OAUTH_MOD));
                    } else if (name.equals(PrefsActivity.TWITTER_OAUTH_PREF)) {
                        context.sendBroadcast(new Intent(PrefsActivity.ACTION_TWITTER_OAUTH_MOD));
                    } else if (name.equals(PrefsActivity.INSTAGRAM_OAUTH_PREF)) {
                        context.sendBroadcast(new Intent(PrefsActivity.ACTION_INSTAGRAM_OAUTH_MOD));
                    } else if (name.equals(PrefsActivity.LOCATION_PREF)) {
                        context.sendBroadcast(new Intent(PrefsActivity.ACTION_LOCATION_MOD));
                    } else {
                        // This should only happen if you have a mismatch
                        // between the schema you've uploaded with your
                        // application to the WIMM app store and the name you
                        // expect in this receiver.
                        Log.e (TAG, "onReceive~ Don't know about prefs with name: " + name);
                    }
                }
            }
        }
    }

}
