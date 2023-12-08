package vlu.mobileproject.translate;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TranslationTask extends AsyncTask<String, Void, String> {

    private final String targetLanguage;
    @SuppressLint("StaticFieldLeak")
    private final TextView targetTextView;

    public TranslationTask(String targetLanguage, TextView targetTextView) {
        this.targetLanguage = targetLanguage;
        this.targetTextView = targetTextView;
    }

    @Override
    protected String doInBackground(String... params) {
        // Ensure there is at least one parameter to translate
        if (params.length > 0) {
            String inputText = params[0];
            return performTranslation(inputText, targetLanguage);
        }
        return null;
    }

    private String performTranslation(String inputText, String targetLanguage) {
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("text", inputText)
                .add("from", "auto")
                .add("to", targetLanguage)
                .build();

        Request request = new Request.Builder()
                .url("https://translate281.p.rapidapi.com/")
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("X-RapidAPI-Key", "ec1a37fb3dmshfeb3c5ab0629056p13643bjsn0c8feabc97bc")
                .addHeader("X-RapidAPI-Host", "translate281.p.rapidapi.com")
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                assert response.body() != null;
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

    @SuppressLint("SetTextI18n")
    @Override
    protected void onPostExecute(String result) {
        try {
            if (result != null && !result.trim().isEmpty()) {
                targetTextView.setText(result);
            } else {
                targetTextView.setText("Translation failed. Please try again.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TranslationTask", "Error updating UI with translation result: " + e.getMessage());
            targetTextView.setText("An error occurred. Please try again later.");
        }
    }

}
