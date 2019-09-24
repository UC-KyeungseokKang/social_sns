package com.example.social_sns;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private LineChart lineChart;
    static List<Entry> entries = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lineChart = (LineChart)findViewById(R.id.chart);

        //공공데이터 전기 판매량 api 불러오기
        PowersellApi powersellApi = new PowersellApi();
        powersellApi.execute("", "", "");

        //딜레이는 공공데이터 api의 응답속도가 10초~15초 사이이기에 20초의 딜레이를 주고 그래프로 표현
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                //entries.add(new Entry(1, 1));
                for(int i=0;i<24;i++){
                    //temp_pwrQty=i+0.1;
                    double random=Math.random()*10;
                    entries.add(new Entry(i+0.1f, (float) (random+0.1f)));

                }
                LineDataSet lineDataSet = new LineDataSet(entries, "속성명1");
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
                LineData lineData = new LineData(lineDataSet);
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
            }
        }, 3000);



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
                /*
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
                */
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
