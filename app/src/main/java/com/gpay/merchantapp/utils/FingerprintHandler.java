/*
package com.gpay.merchantapp.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;
import com.cardnet.android.gpay.R;
import com.cardnet.android.gpay.pages.home.confirmpin.ConfirmPinActivity;
import com.cardnet.android.gpay.pages.home.qrcode.QrCodePinActivity;
import com.cardnet.android.gpay.pages.profile.profileconfirmpin.ProfileConfirmPinActivity;

*/
/**
 * Created by whit3hawks on 11/16/16.
 *//*

@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;

    // Constructor
    public FingerprintHandler(Context mContext) {
        context = mContext;
    }

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        this.update("Fingerprint Authentication error\n" + errString);
        if(errMsgId==7) {
            if(context instanceof ConfirmPinActivity){
                ((ConfirmPinActivity) context).errorFingerAndChangePin();
            }else if(context instanceof QrCodePinActivity){
                ((QrCodePinActivity) context).errorFingerAndChangePin();
            }else {
                ((ProfileConfirmPinActivity) context).errorFingerAndChangePin();
            }

        }

    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        this.update("Fingerprint Authentication help\n" + helpString);
    }

    @Override
    public void onAuthenticationFailed() {
        this.update("Fingerprint Authentication failed.");
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {

        if(context instanceof ConfirmPinActivity){
            ((ConfirmPinActivity) context).onAuthenticationSucceeded(result);
        }else if(context instanceof QrCodePinActivity){
            ((QrCodePinActivity) context).onAuthenticationSucceeded(result);
        }else{
            ((ProfileConfirmPinActivity) context).onAuthenticationSucceeded(result);
        }



    }

    private void update(String e) {
        TextView textView = (TextView) ((Activity) context).findViewById(R.id.errorText);
        textView.setText(e);
    }

}
*/
