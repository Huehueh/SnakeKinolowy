package com.example.user.snake.communication.Answers;

import android.os.AsyncTask;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by user on 29.12.2016.
 */
public class SendingTask<T> extends AsyncTask<Object, Void, T> {

    public RestTemplate restTemplate;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }

    @Override
    protected T doInBackground(Object... params) {
        return null;
    }

    @Override
    protected void onPostExecute(T t) {
        super.onPostExecute(t);
    }
}
