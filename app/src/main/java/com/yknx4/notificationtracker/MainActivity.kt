package com.yknx4.notificationtracker

import android.Manifest
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.view.Menu
import android.view.MenuItem
import com.yknx4.notificationtracker.events.StatusBarNotificationEvent
import com.yknx4.notificationtracker.network.endpoints.EchoService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import okhttp3.OkHttpClient
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: StatusBarNotificationEvent) {
        large_text.append(event.tag + event.message)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(API.API_URL).build()
            var service = retrofit?.create(EchoService::class.java)
//           service?.create("jiji")?.enqueue(RetrofitCallbackTest())

        }
        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS,
            Manifest.permission.ACCESS_FINE_LOCATION),
        1)
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

//    class RetrofitCallbackTest : Callback<String>{
//        override fun onFailure(call: Call<String>?, t: Throwable?) {
//            Log.d(getTag(), t?.message)
//        }
//
//        override fun onResponse(call: Call<String>?, response: Response<String>?) {
//            Log.d(getTag(), response?.body())
//            Log.d(getTag(), response?.errorBody()?.string())
//            Log.d(getTag(), response?.message())
//        }
//
//    }

}
