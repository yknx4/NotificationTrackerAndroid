package com.yknx4.notificationtracker.serializers

import android.content.Context
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices

import com.google.gson.JsonSerializer

import java.lang.ref.WeakReference


/**
 * Created by yknx4 on 7/13/16.
 */
abstract class LocationAwareSerializer internal constructor(c: Context) : GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onConnected(p0: Bundle?) {
    }

    protected fun getLocation(): Location? {
        val mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            return mLastLocation
        }
        else{
            // Get the location manager
            val locationManager = findContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            // Define the criteria how to select the locatioin provider -> use
            // default
            val criteria = Criteria()
            val provider = locationManager.getBestProvider(criteria, false);
            val location = locationManager.getLastKnownLocation(provider);
            if (location != null){
                return  location
            }
        }
        return null
    }

    fun tryConnect(){
        if(mGoogleApiClient != null && !mGoogleApiClient!!.isConnected && !mGoogleApiClient!!.isConnecting){
            mGoogleApiClient!!.connect()
        }
    }

    fun tryDisconnect(){
        if(mGoogleApiClient != null && mGoogleApiClient!!.isConnected){
            mGoogleApiClient!!.disconnect()
        }
    }

    private val contextReference: WeakReference<Context>
    fun findContext():Context{
        return contextReference.get()
    }

    init {
        contextReference = WeakReference(c)
        if (mGoogleApiClient == null) {
            mGoogleApiClient = GoogleApiClient.Builder(findContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    companion object{
        var mGoogleApiClient:GoogleApiClient? = null
    }
}
