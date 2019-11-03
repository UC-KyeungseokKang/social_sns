package com.example.social_sns;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LineChart lineChart;
    private LineDataSet lineDataSet = null;
    private LineData lineData;
    static List<Entry> entries = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lineChart = (LineChart)findViewById(R.id.chart);

        if(entries.size()!=0) {
            lineDataSet = new LineDataSet(entries, "속성명1");
            lineDataSet.setLineWidth(2);
            lineDataSet.setCircleRadius(6);
            lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC"));
            lineDataSet.setCircleColorHole(Color.BLACK);
            lineDataSet.setColor(Color.parseColor("#FFA1B4DC"));
            lineDataSet.setDrawCircleHole(true);
            lineDataSet.setDrawCircles(true);
            lineDataSet.setDrawHorizontalHighlightIndicator(false);
            lineDataSet.setDrawHighlightIndicators(false);
            lineDataSet.setDrawValues(false);

            lineData = new LineData(lineDataSet);
        }
        else
        {
            lineData = new LineData();
        }
        lineChart.setData(lineData);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(24, 24, 0);
        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);
        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);
        Description description = new Description();
        description.setText("");
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(description);
        lineChart.animateY(2000, Easing.EasingOption.EaseInCubic);
        lineChart.invalidate();
        Log.d("error","Why Error");

        model_control_api model_control_api = new model_control_api();
        model_control_api.execute("", "", "");
        //딜레이는 공공데이터 api의 응답속도가 10초~15초 사이이기에 20초의 딜레이를 주고 그래프로 표현



    }

    public class model_control_api extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }


        @Override
        protected String doInBackground(String... strings) {
            Log.d("Task3", "POST");
            String temp = "Not Gained";
            try {
                temp = GET(strings[0]);
                Log.d("REST", temp);
                return temp;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return temp;
        }

        private String GET(String x) throws IOException {
            String data2 = "";
            String test_ip = "http://10.210.32.99";
            String myUrl = String.format(test_ip, x);


            Log.d("rydetest", "11111");
            try {
                URL url = new URL(test_ip);
                Log.d("rydetest", "The response is :" + url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(70000);
                conn.setConnectTimeout(70000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();


                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line;
                StringBuilder sb = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                reader.close();
                String resultJson = sb.toString();

                Log.d("Respone",resultJson);
                //int response = conn.getResponseCode();
                //Log.d("rydetest1", "The response is :" + response);
                //InputStream inputStream;
                //inputStream = conn.getInputStream();
                //JsonObject json = new JsonParser().parse(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).getAsJsonObject();

                try{
                    //JSONObject jsonObject = new JSONObject(resultJson);
                    JSONArray js = new JSONArray(resultJson);
                    entries.clear();

                    for(int i=0;i<js.length();i++)
                    {
                        Log.d("Values",js.getString(i));

                        //temp_pwrQty=i+0.1;
                        //        double random=Math.random()*10;
                        //      entries.add(new Entry(i+0.1f, (float) (random+0.1f)));
                        entries.add(new Entry((float)(i+1),Float.valueOf(js.getString(i))));
                        //lineChart.invalidate();


                    }

                    if(entries.size()!=0) {
                        lineDataSet = new LineDataSet(entries, "속성명1");
                        lineDataSet.setLineWidth(2);
                        lineDataSet.setCircleRadius(6);
                        lineDataSet.setCircleColor(Color.parseColor("#FFA1B4DC"));
                        lineDataSet.setCircleColorHole(Color.BLACK);
                        lineDataSet.setColor(Color.parseColor("#FFA1B4DC"));
                        lineDataSet.setDrawCircleHole(true);
                        lineDataSet.setDrawCircles(true);
                        lineDataSet.setDrawHorizontalHighlightIndicator(false);
                        lineDataSet.setDrawHighlightIndicators(false);
                        lineDataSet.setDrawValues(false);

                        lineData = new LineData(lineDataSet);
                    }
                    else
                    {
                        lineData = new LineData();
                    }
                    lineChart.setData(lineData);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lineDataSet.notifyDataSetChanged();
                            lineData.notifyDataChanged();
                            lineChart.notifyDataSetChanged();
                            lineChart.invalidate();
                        }
                    });

                    //Log.d("Respone",resultJson);
                }catch(JSONException e){
                    Log.d("Error","error");
                }
            } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }

            return data2;
        }
    }


    public class PowersellApi extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            Log.d("findpath_start", "start");


            //포털에서 받은 apikey값을 아래에 넣고
            String Apikey = "FQQeXxWRomvFN8FcMh8EBwqytCxDhEDSr5%2BzzwJAMN7s3T0Oi%2F9u4QJV4kmi02BTEhjQ7i6dz7bXaFp7iXuInQ%3D%3D&pageNo=1";

            //요청 파라미터에 값을 넣습니다.
            String startDate="201202";
            String endDate="201205";

            //요청할 url를 만들고
            String requestUrl = "https://www.kdhc.co.kr/openapi-data/service/kdhcPowerSell/powerSell?serviceKey="+Apikey+"&pageNo=1&numOfRows=10&startDate="+startDate+"&endDate="+endDate;

            Log.d("findpath_distance",requestUrl);

            //try를 통해 요청을 시작합니다.
            try {
                boolean b_item = false;
                boolean b_branchId = false;
                boolean b_branchName=false;
                boolean b_pwrQty=false;
                boolean b_pwrQtyUnit=false;
                boolean b_rnum=false;
                boolean b_yyyymm=false;


                URL url = new URL(requestUrl);
                InputStream is = url.openStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(new InputStreamReader(is, "UTF-8"));

                String tag;
                String temp_item = "";
                String temp_branchId = "";
                String temp_branchName = "";
                String temp_pwrQty = "";
                String temp_pwrQtyUnit = "";
                String temp_rnum = "";
                String temp_yyyymm = "";
                int countlist=0;

                int eventType = parser.getEventType();
                for(int i=0;i<24;i++){
                    //temp_pwrQty=i+0.1;
                    entries.add(new Entry(i+0.1f, i+0.1f));
                    countlist++;
                }
                entries.add(new Entry(5+0.1f, 5+0.1f));

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    //Log.d("findpath_parser", String.valueOf(eventType)+" name : "+parser.getName()+" text : "+parser.getText());
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;

                        case XmlPullParser.START_TAG: {
                            if (parser.getName().equals("item")) {
                                b_item=true;
                                Log.d("findpath--------", "--------");
                            }
                            if (parser.getName().equals("branchId")) {
                                b_branchId = true;
                            }
                            if (parser.getName().equals("branchName")) {
                                b_branchName = true;
                            }
                            if (parser.getName().equals("pwrQty")) {
                                b_pwrQty = true;
                            }
                            if (parser.getName().equals("pwrQtyUnit")) {
                                b_pwrQtyUnit = true;
                            }
                            if (parser.getName().equals("rnum")) {
                                b_rnum = true;
                            }
                            if (parser.getName().equals("yyyymm")) {
                                b_yyyymm = true;
                            }
                            break;
                        }

                        case XmlPullParser.TEXT: {
                            if(b_item){
                                b_item=false;
                            }

                            if ( b_branchId) {
                                temp_branchId = parser.getText();
                                Log.d("temp_branchId",temp_branchId);
                                b_branchId = false;
                            }
                            if (b_pwrQty) {
                                temp_pwrQty = parser.getText();
                                Log.d("temp22", temp_pwrQty);
                                b_pwrQty = false;
                            }
                            if (b_branchName) {
                                temp_branchName = parser.getText();
                                Log.d("temp_branchName", temp_branchName);
                                b_branchName = false;
                            }
                            if (b_pwrQtyUnit) {
                                temp_pwrQtyUnit = parser.getText();
                                Log.d(" temp_pwrQtyUnit", temp_pwrQtyUnit);
                                b_pwrQtyUnit = false;
                            }
                            if ( b_rnum) {
                                temp_rnum = parser.getText();
                                Log.d("temp_rnum", temp_rnum);
                                b_rnum = false;
                            }
                            if (b_yyyymm) {
                                temp_yyyymm = parser.getText();
                                Log.d("temp_yyyymm", temp_yyyymm);
                                //여기서 그래프에 값을 넣습니다.
                                entries.add(new Entry(countlist, Float.parseFloat(temp_pwrQty)));
                                countlist++;
                                b_yyyymm = false;
                            }

                            break;
                        }
                        case XmlPullParser.END_TAG:
                            if (parser.getName().equals("item")) {

                                break;
                            }

                        case XmlPullParser.END_DOCUMENT:
                            break;


                    }
                    eventType = parser.next();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("HomeFra_transportapi_", "Error: " + e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }

    }



}
