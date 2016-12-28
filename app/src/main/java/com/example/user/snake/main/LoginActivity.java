package com.example.user.snake.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.snake.R;
import com.example.user.snake.communication.Queries.JsonMessage;
import com.example.user.snake.communication.User;
import com.example.user.snake.communication.Queries.Username;
import com.example.user.snake.names.BundleNames;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by user on 17.11.2016.
 */
public class LoginActivity extends Activity{

    EditText ipEditText, loginEditText;
    TextView info;
    String TAG = "LoginAc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if(!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }

        //button
        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(BundleNames.LOGIN, ((EditText) findViewById(R.id.loginEditText)).getText().toString());
                save(BundleNames.IP, ((EditText) findViewById(R.id.serverIpEditText)).getText().toString());
                new RequestLoginTask().execute();
            }
        });
        //login
        loginEditText = ((EditText)findViewById(R.id.loginEditText));
        loginEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(loginEditText, InputMethodManager.SHOW_IMPLICIT);
        loginEditText.setText(getValue(BundleNames.LOGIN));
        //ip
        ipEditText = (EditText) findViewById(R.id.serverIpEditText);
        ipEditText.setText(getValue(BundleNames.IP));

        info = (TextView) findViewById(R.id.info);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v(TAG, "Restart");
        setInfo(null);
    }

    public void save(String key, String value) {
        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
        sharedPref.edit().putString(key, value).apply();
    }

    public String getValue(String key) {
        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
        return sharedPref.getString(key, "");
    }

    private int getMyId()
    {
        String previousId = getValue(BundleNames.ID);
        if(previousId.equals(""))
        {
            return -1;
        }
        else {
            return Integer.parseInt(previousId);
        }
    }

    private void startGame(User user)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(User.USER, user);
        startActivity(intent);
        finish();
    }

    private void failedConnectionError()
    {
        setInfo("Nie mozna sie polaczyc z serwerem!");
    }

    private void setInfo(final String text)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(text!= null) {
                    info.setText(text);
                }
                else
                {
                    info.setText("");
                }
            }
        });
    }

    public class RequestLoginTask extends AsyncTask<Void, Void, User> {

        Username username;
        User user;
        RestTemplate restTemplate;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            username = new Username(getMyId(), getValue(BundleNames.LOGIN));
            username.setAddress(getValue(BundleNames.IP));
            restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        }

        @Override
        protected User doInBackground(Void... params) {
            try {
                user = restTemplate.postForObject(username.getQuery(), username, User.class);
            }
            catch (ResourceAccessException e)
            {
                failedConnectionError();
            }
            return user;
        }

        @Override
        protected void onPostExecute(final User user) {
            super.onPostExecute(user);

            if(user != null) {
                if (user.getId() > -1) {
                    try {
                        info.setText(getText(R.string.logged));
                    } catch (NullPointerException e) {
                        e.getMessage();
                    }
                    JsonMessage.setId(user.getId());
                    save(BundleNames.ID, user.getId() + "");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                            }
                            startGame(user);
                        }
                    });
                } else {
                    setInfo(getText(R.string.change_login)+"");
                }
            }
        }
    }
}
