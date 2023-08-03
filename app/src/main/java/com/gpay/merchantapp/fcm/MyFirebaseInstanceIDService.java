/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gpay.merchantapp.fcm;

import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.gpay.merchantapp.MainApp;
import com.gpay.merchantapp.network.Service;
import com.gpay.merchantapp.network.response.ResponseVerifyPswHash;
import org.jetbrains.annotations.NotNull;
import java.util.Locale;
import javax.inject.Inject;
import rx.subscriptions.CompositeSubscription;


public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseIIDService";
    @Inject
    Service service;
    CompositeSubscription subscription = new CompositeSubscription();

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("NEW_TOKEN",s);

        sendRegistrationToServer(s);
    }

   /* @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        Log.e(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }*/
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {

        if(MainApp.instance.getSharedPreferences().getString("customer_id","").length()>0){

            String defLang = "";
            if(Locale.getDefault().getISO3Language().equals("ind")){
                defLang = "ind";
            }else {
                defLang = "eng";
            }
            // TODO: Implement this method to send token to your app server.
            subscription.add(service.registerToken(token, defLang, new Service.Callbackverify_psw_hash() {
                @Override
                public void onSuccess(@NotNull ResponseVerifyPswHash response) {

                }

                @Override
                public void onError(@NotNull String message, @NotNull String status) {

                }
            }));

        }
    }
}