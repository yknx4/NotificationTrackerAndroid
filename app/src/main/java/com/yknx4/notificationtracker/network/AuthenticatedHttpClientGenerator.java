package com.yknx4.notificationtracker.network;

import android.content.Context;

import com.securepreferences.SecurePreferences;
import com.yknx4.lib.yknxtools.models.ContextAware;
import com.yknx4.notificationtracker.PreferencesFields;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * Created by yknx4 on 7/19/16.
 */
public class AuthenticatedHttpClientGenerator extends ContextAware {

    private final SecurePreferences mPreferences;
    private final AuthInformation authInformation;

    AuthenticatedHttpClientGenerator(Context context) {
        super(context);
        mPreferences = new SecurePreferences(context);
        authInformation = new AuthInformation(mPreferences);
    }

    public void init(){

    }

    public OkHttpClient getAuthenticatedClient(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new AuthenticationInterceptor());
        builder.authenticator(new Authenticator());
        return builder.build();
    }

    private String getProperty(String property){
        return mPreferences.getString(property, "");
    }

    class Authenticator implements okhttp3.Authenticator{
        @Override
        public Request authenticate(Route route, Response response) throws IOException {
            return null;
        }
    }

    class AuthenticationInterceptor implements Interceptor{
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            // Add authorization header with updated authorization value to intercepted request
            Request authorisedRequest = originalRequest.newBuilder()
                    .header(PreferencesFields.ACCESS_TOKEN, authInformation.getAccessToken())
                    .header(PreferencesFields.CLIENT, authInformation.getClient())
                    .header(PreferencesFields.TOKEN_TYPE, authInformation.getTokenType())
                    .header(PreferencesFields.EXPIRY, authInformation.getExpiry())
                    .header(PreferencesFields.UID, authInformation.getUID())
                    .build();
            Response response = chain.proceed(authorisedRequest);

            if(response.isSuccessful()){
                authInformation
                        .edit()
                        .setAccessToken(response.header(PreferencesFields.ACCESS_TOKEN, authInformation.getAccessToken()))
                        .setClient(response.header(PreferencesFields.CLIENT, authInformation.getClient()))
                        .setTokenType(response.header(PreferencesFields.TOKEN_TYPE, authInformation.getTokenType()))
                        .setExpiry(response.header(PreferencesFields.EXPIRY, authInformation.getExpiry()))
                        .setUID(response.header(PreferencesFields.UID, authInformation.getUID()))
                        .apply();
            }

            return response;
        }
    }

    class AuthInformation {
        private SecurePreferences sharedPreferences;

        public AuthInformation(SecurePreferences sharedPreferences){
            this.sharedPreferences = sharedPreferences;
        }

        private String getProperty(String property){
            return sharedPreferences.getString(property, "");
        }

        public String getEmail(){
            return getProperty(PreferencesFields.EMAIL);
        }

        public String getPassword(){
            return getProperty(PreferencesFields.PASSWORD);
        }

        public String getAccessToken(){
            return getProperty(PreferencesFields.ACCESS_TOKEN);
        }

        public String getClient(){
            return getProperty(PreferencesFields.CLIENT);
        }

        public String getTokenType(){
            return getProperty(PreferencesFields.TOKEN_TYPE);
        }

        public String getExpiry(){
            return getProperty(PreferencesFields.EXPIRY);
        }

        public String getUID(){
            return getProperty(PreferencesFields.UID);
        }

        public Setter edit(){
            return new Setter(mPreferences.edit());
        }
        
        private class Setter{

            private final SecurePreferences.Editor _editor;

            Setter(SecurePreferences.Editor editor){
                _editor = editor;
            }

            private Setter setProperty(String property, Object value){
                _editor.putString(property, value.toString());
                return this;
            }

            public Setter setEmail(String value){
                return  setProperty(PreferencesFields.EMAIL, value);
            }

            public Setter setPassword(String value){
                return setProperty(PreferencesFields.PASSWORD, value);
            }

            public Setter setAccessToken(String value){
                return setProperty(PreferencesFields.ACCESS_TOKEN, value);
            }

            public Setter setClient(String value){
                return setProperty(PreferencesFields.CLIENT, value);
            }

            public Setter setTokenType(String value){
                return setProperty(PreferencesFields.TOKEN_TYPE, value);
            }

            public Setter setExpiry(String value){
                return setProperty(PreferencesFields.EXPIRY, value);
            }

            public Setter setUID(String value){
                return setProperty(PreferencesFields.UID, value);
            }

            public void apply(){
                _editor.apply();
            }

            public boolean commit(){
                return _editor.commit();
            }
            
        }

    }

}
