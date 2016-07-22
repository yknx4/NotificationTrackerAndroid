package com.yknx4.notificationtracker.activities

import android.Manifest
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.gson.JsonElement
import com.yknx4.lib.yknxtools.convert.getPixelsForDp
import com.yknx4.notificationtracker.*
import com.yknx4.notificationtracker.events.LocationChangedEvent
import com.yknx4.notificationtracker.events.StatusBarNotificationEvent
import com.yknx4.notificationtracker.network.LoginService
import com.yknx4.notificationtracker.network.endpoints.AuthService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private var mapView: GoogleMap? = null

    override fun onMapReady(googleMap: GoogleMap?) {
        mapView = googleMap
        mapView?.uiSettings?.setAllGesturesEnabled(false)
        mapView?.uiSettings?.isMapToolbarEnabled = false
        mapView?.uiSettings?.isMyLocationButtonEnabled = false
        mapView?.uiSettings?.isZoomControlsEnabled = false
        mapView?.animateCamera(CameraUpdateFactory.newLatLng(LatLng(19.24997, -103.72714)))
        mapView?.setPadding(0,0,0,getPixelsForDp(24).toInt())

    }

    fun setMapLocation(location: Location){
        if(mapView!=null){
            val cameraUpdate = CameraUpdateFactory.newLatLng(location.toLatLng())
            mapView!!.animateCamera(cameraUpdate)
        }
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNewLocation(event: LocationChangedEvent) {
        setMapLocation(event.newLocation)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: StatusBarNotificationEvent) {
        large_text.append(event.tag + event.message)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(loggedOut()){
            val login_activity = Intent(this, LoginActivity::class.java)
            startActivity(login_activity)
        }
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(API.BASE_URL).build()
            var service = retrofit?.create(AuthService::class.java)
            service?.login("test@dev.com", "123456123")?.enqueue(LoginCallback())

        }
        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE),
        1)

        map.onCreate(savedInstanceState?: Bundle())
        map.getMapAsync(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        when(id){
            R.id.action_settings -> return true
            R.id.action_logout -> {
                LoginService(this).logout()
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}

class LoginCallback : Callback<JsonElement> {
    override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
        NotificationLogger.d(getTag(), response?.message()?:"")
        NotificationLogger.d(getTag(), response?.body()?.toString()?:"")
        NotificationLogger.d(getTag(), response?.errorBody()?.string()?:"")
    }

    override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
        NotificationLogger.d(getTag(), call?.request()?.url().toString()?:"")
        NotificationLogger.d(getTag(), call?.request()?.body().toString())
    }

}
