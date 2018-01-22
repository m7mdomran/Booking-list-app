package com.example.sayedsalah.book_listing_app;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Sayed Salah on 10/26/2017.
 */
public class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    public static ArrayList<Book> fetchbookdata(String requestUrl) {
        String jsonresponse = null;
        Log.i(LOG_TAG, " here at fetchbookdata method ");

        URL url = createUrl(requestUrl);
        jsonresponse = makeHttpRequest(url);
        ArrayList<Book> books = extractFeatureFromJson(jsonresponse);
        return books;
    }


    private static URL createUrl(String requesturl) {
        URL url = null;
        try {
            url = new URL(requesturl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, " Error with creating URL ", e);
        }
        return url;

    }

    private static String makeHttpRequest(URL url) {
        String jsonResponse = "";
        HttpsURLConnection urlConnection = null;
        InputStream inputStream = null;
        final int READ_TIMEOUT = 10000;

        if (url == null) {
            return jsonResponse;
        }
        try {
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readfromstram(inputStream);
            } else {
                Log.e(LOG_TAG, " Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Book JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return jsonResponse;
    }

    private static String readfromstram(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();

            }
        }
        return output.toString();
    }

    private static ArrayList<Book> extractFeatureFromJson(String bookJSON) {
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }
        ArrayList<Book> books = new ArrayList<>();

        try {
            JSONObject basejson = new JSONObject(bookJSON);
            if (basejson.has("items")) {
                JSONArray itemArray = basejson.getJSONArray("items");

                for (int i = 0; i < itemArray.length(); i++) {

                    JSONObject cuurentItem = itemArray.getJSONObject(i);
                    JSONObject bookInfo = cuurentItem.getJSONObject("volumeInfo");

                    String title = bookInfo.getString("title");

                    String[] authors = new String[]{};

                    JSONArray authorJsonArray = bookInfo.optJSONArray("authors");
                    ArrayList<String> authorList = new ArrayList<String>();
                    if (authorJsonArray != null) {

                        for (int j = 0; j < authorJsonArray.length(); j++) {
                            authorList.add(authorJsonArray.get(j).toString());
                        }
                        authors = authorList.toArray(new String[authorList.size()]);
                    } else {
                        authorList.add("not found authors");
                        authors = authorList.toArray(new String[authorList.size()]);
                    }


                    String description = "";
                    if (bookInfo.optString("description") != null)
                        description = bookInfo.optString("description");


                    books.add(new Book(title, authors, description));
                }
            } else
                return null;

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the book JSON results", e);
        }
        return books;

    }

}
