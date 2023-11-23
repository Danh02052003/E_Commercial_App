package vlu.mobileproject.translate;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@SuppressLint("StaticFieldLeak")
public class TranslationTask extends AsyncTask<String, Void, String> {
    private static final String TRANSLATOR_API_KEY = "ec1a37fb3dmshfeb3c5ab0629056p13643bjsn0c8feabc97bc";

    private final TextView targetTextView;
    private final String lang;

    public TranslationTask(TextView targetTextView, String lang) {
        this.targetTextView = targetTextView;
        this.lang = lang;
    }
    @Override
    protected String doInBackground(String... params) {
        String inputText = params[0];

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("text", inputText)
                .add("from", "auto")
                .add("to", lang)
                .build();

        Request request = new Request.Builder()
                .url("https://translate281.p.rapidapi.com/")
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("X-RapidAPI-Key", TRANSLATOR_API_KEY)
                .addHeader("X-RapidAPI-Host", "translate281.p.rapidapi.com")
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 429) {
                return "Quota Exceeded";
            }

            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
                return jsonObject.get("response").getAsString();
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            targetTextView.setText(result);
        }
    }
}
