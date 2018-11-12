package pl.edu.ug.sut;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DatabaseActivity extends AppCompatActivity {
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.activity_database);
        tv = findViewById( R.id.tv );
        DatabaseHelper db = new DatabaseHelper( this );
        tv.setText( "" );
        LinearLayout linearLayout = new LinearLayout(this);
        setContentView(linearLayout);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        for(DataModel k:db.getAll()){
            TextView textView = new TextView( this );
            textView.setText(k.getId().toString() + " | " + k.getState() + " | " +  k.getDate() + " | " +  k.getDuration() );
            linearLayout.addView( textView );
//            Log.d("Dane z bazy" ,k.getId() + " | " + k.getState() + " | " +  k.getDate() + " | " +  k.getDuration());
        }

        }


}
