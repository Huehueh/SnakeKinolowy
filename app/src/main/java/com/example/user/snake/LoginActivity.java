package com.example.user.snake;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by user on 17.11.2016.
 */
public class LoginActivity extends Activity{

    EditText ipEditText, loginEditText;
    private final static String IP = "IP", LOGIN = "LOGIN", REST_ADDRESS = "ADDRESS";
    String TAG = "LoginAc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        Toast.makeText(this, "loginac oncreate", Toast.LENGTH_SHORT).show();
        Log.v(TAG, "OnCreate");

        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if(!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }

        //button
        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(LOGIN, ((EditText) findViewById(R.id.loginEditText)).getText().toString());
                save(IP, ((EditText) findViewById(R.id.serverIpEditText)).getText().toString());
                new RequestLoginTask().execute();
            }
        });
        //login
        loginEditText = ((EditText)findViewById(R.id.loginEditText));
        loginEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(loginEditText, InputMethodManager.SHOW_IMPLICIT);
        loginEditText.setText(getValue(LOGIN));
        //ip
        ipEditText = (EditText) findViewById(R.id.serverIpEditText);
        ipEditText.setText(getValue(IP));
    }


    public void save(String key, String value) {
        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
        sharedPref.edit().putString(key, value).apply();
    }

    public String getValue(String key) {
        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
        return sharedPref.getString(key, "");
    }

    private void startGame(String restAddress, User user)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(REST_ADDRESS, restAddress);
        intent.putExtra(User.USER, user);

        startActivity(intent);
        finish();
    }

    public String createRestAddress()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("http://");
        builder.append(getValue(IP));
        builder.append(":3911/wonsz");
        builder.append("?login=");
        builder.append(getValue(LOGIN));

        return builder.toString();
    }

    public class RequestLoginTask extends AsyncTask<Void, Void, User> {

        String restAddress;
        @Override
        protected User doInBackground(Void... params) {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            restAddress = createRestAddress();
            User user = restTemplate.getForObject(restAddress, User.class);
            return user;
        }

        @Override
        protected void onPostExecute(final User user) {
            super.onPostExecute(user);
            TextView info = (TextView) findViewById(R.id.info);
            if(user.getId() > -1) {
                try {
                    info.setText(getText(R.string.logged));
                }
                catch (NullPointerException e)
                {
                    e.getMessage();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) { }
                        startGame(restAddress, user);
                    }
                });
            }
            else {
                info.setText(getText(R.string.change_login));
            }
        }
    }
}
