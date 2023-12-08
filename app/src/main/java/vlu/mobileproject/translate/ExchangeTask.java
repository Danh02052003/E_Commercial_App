package vlu.mobileproject.translate;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ExchangeTask extends AsyncTask<String, Void, String> {
    private static final String EXCHANGE_API_KEY = "ec1a37fb3dmshfeb3c5ab0629056p13643bjsn0c8feabc97bc";

    private final TextView targetTextView;
    private final String fromCurrency;
    private final String toCurrency;

    public ExchangeTask(TextView targetTextView, String fromCurrency, String toCurrency) {
        this.targetTextView = targetTextView;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
    }

    @Override
    protected String doInBackground(String... params) {
        OkHttpClient client = new OkHttpClient();


        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://currency-exchange.p.rapidapi.com/exchange").newBuilder();
        urlBuilder.addQueryParameter("from", fromCurrency);
        urlBuilder.addQueryParameter("to", toCurrency);
        urlBuilder.addQueryParameter("q", "1.0");

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .header("X-RapidAPI-Key", EXCHANGE_API_KEY)
                .header("X-RapidAPI-Host", "currency-exchange.p.rapidapi.com")
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 429) {
                return "Quota Exceeded";
            }

            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                return responseBody;
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
            try {
                if ("VND".equals(toCurrency)) {
                    Double resultValue = Double.parseDouble(result);
                    targetTextView.setText(formatCurrency(targetTextView.getText().toString(), resultValue));
                } else if ("USD".equals(toCurrency)) {
                    targetTextView.setText(formatCurrencyEngWithoutSymbol(targetTextView.getText().toString(), Double.parseDouble(result)));
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    private String formatCurrency(String value, double result) {
        String cleanValue = value.replace(",", "").replace("$", "");
        double exchangeRate = result;
        double targetValue = Double.parseDouble(cleanValue);
        double multipliedValue = exchangeRate * targetValue;

        DecimalFormat decimalFormat = new DecimalFormat("#,###đ");
        return decimalFormat.format(multipliedValue);
    }

    private String formatCurrencyEngWithoutSymbol(String value, double result) {
        // Remove commas and "đ" from the value
        String cleanValue = value.replace(".", "").replace("đ", "");

        double doubleValue = Double.parseDouble(cleanValue);

        // Perform your calculations
        double resultValue = result;
        double dividedValue = doubleValue * resultValue;

        // Format the result
        DecimalFormat decimalFormat = new DecimalFormat("$#,###");
        return decimalFormat.format(dividedValue);
    }
}