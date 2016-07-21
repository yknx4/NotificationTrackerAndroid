package com.yknx4.notificationtracker.services

import android.app.Notification
import android.location.Location
import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.yknx4.lib.yknxtools.device.getDeviceUUID
import com.yknx4.notificationtracker.*
import com.yknx4.notificationtracker.events.LocationChangedEvent
import com.yknx4.notificationtracker.events.StatusBarNotificationEvent
import com.yknx4.notificationtracker.network.AuthenticatedHttpClientGenerator
import com.yknx4.notificationtracker.network.endpoints.StatusBarNotificationService
import com.yknx4.notificationtracker.serializers.*
import okhttp3.OkHttpClient
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by yknx4 on 7/13/16.
 */
class NotificationTrackerService : NotificationListenerService(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    override fun onLocationChanged(p0: Location?) {
        Log.i(getTag(), "Updating Location")
        LocationAwareSerializer.location = p0
        EventBus.getDefault().post(LocationChangedEvent(p0))
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.e(getTag(), "Connection to Google Api failed")
    }

    override fun onConnected(p0: Bundle?) {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_LOW_POWER
        mLocationRequest.fastestInterval = 500
        mLocationRequest.interval = 1 * 1000 * 60
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
    }

    override fun onConnectionSuspended(p0: Int) {
        Log.e(getTag(), "Connection to Google Api suspended")
    }

    fun postEvent(tag:String, action:String, message:String, notification:StatusBarNotification){
        EventBus.getDefault().post(StatusBarNotificationEvent(tag, action, message, notification))
    }

    var gson: Gson = Gson()
    var status_notification_serializer:StatusBarNotificationSerializer? = null
    var notification_serializer:NotificationSerializer? = null

    private var  overpowered_http_cient: OkHttpClient? = null

    override fun onCreate() {
        initializeGoogleApi()
        initializeRestClient()
        DeviceAwareSerializer.deviceUUid = getDeviceUUID()
        status_notification_serializer = StatusBarNotificationSerializer()
        notification_serializer = NotificationSerializer()
        gson = GsonBuilder()
                .registerTypeAdapter(Location::class.java, LocationSerializer())
                .registerTypeAdapter(StatusBarNotification::class.java, status_notification_serializer)
                .registerTypeAdapter(Notification::class.java, notification_serializer)
                .setPrettyPrinting()
                .create()
        super.onCreate()
    }

    private var retrofit: Retrofit? = null

    private var service: StatusBarNotificationService? = null

    private fun initializeRestClient() {
        overpowered_http_cient = AuthenticatedHttpClientGenerator(this).authenticatedClient
        retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(API.API_URL).client(overpowered_http_cient).build()
        service = retrofit?.create(StatusBarNotificationService::class.java)
    }

    private var mGoogleApiClient: GoogleApiClient? = null

    private fun initializeGoogleApi() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build()
        }
        mGoogleApiClient?.connect()
    }

    override fun onDestroy() {
        mGoogleApiClient?.disconnect()
        super.onDestroy()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        Log.i(getTag(), "**********  onNotificationPosted")
        Log.i(getTag(), "ID :" + sbn.id + "t" + sbn.notification.tickerText + "t" + sbn.packageName)
        val json = sbn.toJson(gson)
        postEvent(getTag(), "Posted", json, sbn)
        NotificationLogger.d(getTag(), json)
        if(loggedOut()) return
        service?.create(sbn.toJsonObject(gson))?.enqueue(RetrofitNotificationPost())
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        Log.i(getTag(), "********** onNotificationRemoved")
        Log.i(getTag(), "ID :" + sbn.id + "t" + sbn.notification.tickerText + "t" + sbn.packageName)
        postEvent(getTag(), "removed", sbn.toJson(gson), sbn)
    }

    class RetrofitNotificationPost : Callback<JsonElement>{
        override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
            Log.d(getTag(), call?.request()?.url().toString())
            Log.d(getTag(), t?.message ?: "")
            Log.d(getTag(), call?.request()?.method() ?: "")
        }

        override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
            Log.d(getTag(), response?.body().toString())
            Log.d(getTag(), response?.errorBody()?.string() ?: "")
            Log.d(getTag(), response?.message() ?: "")
        }

    }

}
