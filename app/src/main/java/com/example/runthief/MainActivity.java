package com.example.runthief;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class MainActivity extends Activity {

    static final LatLng SEOUL = new LatLng(37.724, 126.7167);
    private GoogleMap map;
    private JSONArray cctvJsonArray;
    private final String CCTV_API_URL = "http://openapi.seoul.go.kr:8088/524579466a6a776338365770585161/json/TB_GC_VVTV_INFO_ID01/1/373";
    private final String DATA_OK_CODE = "TB_GC_VVTV_INFO_ID01";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        Marker seoul = map.addMarker(new MarkerOptions().position(SEOUL)
                .title("Seoul")/*.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))*/);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 15));

        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

        cctvJsonArray = getCctvJsonArray();
        if (cctvJsonArray.length() > 0) {
            map.clear();
            for (int i = 0; i < cctvJsonArray.length(); i++) {
                String latitude, longitude;
                try {
                    latitude = cctvJsonArray.getJSONObject(i).getString("GC_MAPX");
                    longitude = cctvJsonArray.getJSONObject(i).getString("GC_MAPY");
                    Marker temp = map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude))));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private JSONArray getCctvJsonArray() {
        JSONArray temp = null;
        AsyncGetParticipantsRequest getParticipantsRequest = new AsyncGetParticipantsRequest();
        try {
            JSONObject jsonObject =
                    getParticipantsRequest.execute(CCTV_API_URL).get();
            JSONObject tempObject = jsonObject.getJSONObject(DATA_OK_CODE);
            if (tempObject.getInt("list_total_count") > 0) {
                temp = tempObject.getJSONArray("row");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return temp;
    }

    private class AsyncGetParticipantsRequest extends
            AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            /* 아래는 요청하는 서버 url */
            String url = params[0];
            JSONObject jObject;
            String result = null;

            GetCCTVInfo getCCTVInfo = new GetCCTVInfo();

            try {
                result = getCCTVInfo.run(url);


                try {
                    jObject = new JSONObject(result);
                    return jObject;

					/* 요청으로 받은 JSONObject를 리턴한다. */

                } catch (Exception e) {
                } finally {

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

    }

    public class GetCCTVInfo {
        OkHttpClient client = new OkHttpClient();

        String run(String url) throws IOException {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        }
    }
}

