package pl.edu.ug.sut;

import android.arch.persistence.room.Database;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class GraphActivity extends AppCompatActivity {

    BarChart barChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.activity_graph);
        barChart = (BarChart) findViewById( R.id.barchart );


        ArrayList<BarEntry> OnEntries = new ArrayList<>();
        ArrayList<BarEntry> OffEntries = new ArrayList<>();
        DatabaseHelper db = new DatabaseHelper( this);
        for(DataModel k:db.getAll()){
            if (k.getState().equals("ON" )) {

                OnEntries.add(new BarEntry( k.getId() , Float.parseFloat(k.getDuration() ) ));

            }
            // textView.setText(k.getId().toString() + " | " + k.getState() + " | " +  k.getDate() + " | " +
//            Log.d("Dane z bazy" ,k.getId() + " | " + k.getState() + " | " +  k.getDate() + " | " +  k.getDuration());
        }
        for(DataModel k:db.getAll()) {

            if (k.getState().equals( "OFF" )) {

                OffEntries.add( new BarEntry( k.getId(), Float.parseFloat( k.getDuration() ) ) );
                            }



        }

        BarDataSet set1 = new BarDataSet( OnEntries, "DatesetOn" );
        BarDataSet set2 = new BarDataSet( OffEntries, "DatesetOn" );
        ArrayList<Integer> colors = new ArrayList<Integer>();

        colors.add(ContextCompat.getColor(this, R.color.colorAccent));
        colors.add(ContextCompat.getColor(this, R.color.design_default_color_primary));
        float groupSpace = 0.06f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.45f; // x2 dataset
// (0.02 + 0.45) * 2 + 0.06 = 1.00 -> interval per "group"
        BarData data = new BarData(set1, set2);
        set1.setColors( colors );

        data.setBarWidth(barWidth); // set the width of each bar
        barChart.setData(data);
        barChart.groupBars( 1f,groupSpace,barSpace );
//        barChart.groupBars(1980f, groupSpace, barSpace); // perform the "explicit" grouping
//        barChart.invalidate(); // refresh
//        XAxis xAxis = barChart.getXAxis();
//        xAxis.setCenterAxisLabels(true);

    }
}
