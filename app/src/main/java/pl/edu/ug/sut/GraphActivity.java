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

        barChart.getDescription().setEnabled(false);
        ArrayList<BarEntry> OnEntries = new ArrayList<>();
        ArrayList<BarEntry> OffEntries = new ArrayList<>();

        createEntries("ON",OnEntries);
        createEntries( "OFF", OffEntries );
//        for(DataModel k:db.getAll()){
//            if (k.getState().equals("ON" ))
//                OnEntries.add(new BarEntry( k.getId() , Float.parseFloat(k.getDuration() ) ));
//        }



        BarDataSet set1 = new BarDataSet( OnEntries, "Ekran włączony" );
        BarDataSet set2 = new BarDataSet( OffEntries, "Ekran wyłączony" );
        ArrayList<Integer> colors = new ArrayList<Integer>();


        // sets colors for the dataset, resolution of the resource name to a "real" color is done internally
        set1.setColor( 0xFF34C6EB );
        set2.setColor( 0xFFEB6B34 );
        float groupSpace = 0.06f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.45f; // x2 dataset
// (0.02 + 0.45) * 2 + 0.06 = 1.00 -> interval per "group"
        BarData data = new BarData(set1, set2);
//        set1.setColors( colors );

        data.setBarWidth(barWidth); // set the width of each bar
        barChart.setData(data);
        barChart.groupBars( 1f,groupSpace,barSpace );


    }

    private void createEntries(String s,ArrayList arrayList) {
        DatabaseHelper db = new DatabaseHelper( this);
        for(DataModel k:db.getAll()) {

            if (k.getState().equals( s ))
                arrayList.add( new BarEntry( k.getId(), Float.parseFloat( k.getDuration() ) ) );
        }
    }

}
