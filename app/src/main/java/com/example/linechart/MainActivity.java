package com.example.linechart;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.linechart.helper.AppController;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Entry> x = new ArrayList<>();
    ArrayList<String> y = new ArrayList<>();
    private LineChart mChart;
    String Token;

    private final String url = "http://192.168.1.66/ujicoba/data.json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mChart = findViewById(R.id.lineChart);
        mChart.setDrawGridBackground(false);
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setPinchZoom(true);
        mChart.setDrawBorders(true);

        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        YAxis xAxis = mChart.getAxisRight();
        xAxis.setEnabled(false);
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMinValue(0f);
        leftAxis.setAxisMaxValue(40f);
        leftAxis.setDrawLimitLinesBehindData(true);
        LimitLine limitLine = new LimitLine(14, "Jarang Hadir");
        limitLine.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        Legend l = mChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        Graph_List();
    }

    private void Graph_List() {
        JsonArrayRequest jArr = new JsonArrayRequest(url, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject obj = response.getJSONObject(i);
                    int score      = obj.getInt("jumlah");
                    String date    = obj.getString("bulan");
                    x.add(new Entry(score, i));
                    y.add(date);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            LineDataSet set1 = new LineDataSet(x, Token);
            set1.setCircleColor(Color.BLACK);
            set1.setCircleColorHole(Color.BLACK);
            set1.setColors(ColorTemplate.COLORFUL_COLORS);
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setLineWidth(3f);
            set1.setCircleRadius(4f);
            LineData data = new LineData(y, set1);
            mChart.setData(data);
            mChart.invalidate();

        }, (Response.ErrorListener) error -> VolleyLog.d("error respon" + error, "Error: " + error.getMessage()));

        AppController.getInstance().addToRequestQueue(jArr);
    }
}