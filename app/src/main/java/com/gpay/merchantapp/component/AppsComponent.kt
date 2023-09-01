package com.gpay.merchantapp.component

import com.gpay.merchantapp.MainApp
import com.gpay.merchantapp.fcm.MyFirebaseInstanceIDService
import com.gpay.merchantapp.network.NetworkModule
import com.gpay.merchantapp.pages.changepin.ChangePinActivity
import com.gpay.merchantapp.pages.changepin.CreatePinActivity
import com.gpay.merchantapp.pages.changepin.ReConfirmPinActivity
import com.gpay.merchantapp.pages.changepin.SetNewPinActivity
import com.gpay.merchantapp.pages.home.HomePage
import com.gpay.merchantapp.pages.home.receivepayment.ReceivePaymentActivity
import com.gpay.merchantapp.pages.home.settlementhistory.SettlementHistoryActivity
import com.gpay.merchantapp.pages.home.transhistory.TransHistoryActivity
import com.gpay.merchantapp.pages.login.LoginActivity
import com.gpay.merchantapp.pages.loginpin.LoginPinActivity
import com.gpay.merchantapp.pages.register.RegistMerchantActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(NetworkModule::class))
interface AppsComponent {

    fun inject(mainApp: MainApp)
    fun inject(homePage: HomePage)
    fun inject(loginActivity: LoginActivity)
    fun inject(changePinActivity:ChangePinActivity)
    fun inject(createPinActivity: CreatePinActivity)
    fun inject(loginPinActivity: LoginPinActivity)
    fun inject(reConfirmPinActivity: ReConfirmPinActivity)
    fun inject(setNewPinActivity: SetNewPinActivity)
    fun inject(receivePaymentActivity: ReceivePaymentActivity)
    fun inject(transHistoryActivity: TransHistoryActivity)
    fun inject(settlementHistoryActivity: SettlementHistoryActivity)
    fun inject(myFirebaseInstanceIDService: MyFirebaseInstanceIDService)
    fun inject(registMerchantActivity: RegistMerchantActivity)
}
