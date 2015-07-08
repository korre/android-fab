package se.kmdev.android_fab;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import se.kmdev.android_fab.fab.FloatingActionButton;


public class MainActivity extends Activity {

    private FloatingActionButton floatingActionButton;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingActionButton.setCenterIconResource(R.drawable.filter_icon);
        floatingActionButton.addOnFloatingActionButtonPressedListener(new FloatingActionButton.OnFloatingActionButtonPressedListener() {
            @Override
            public void onPress() {
                Toast.makeText(MainActivity.this, "Button pressed!", Toast.LENGTH_SHORT).show();
            }
        });

        listView = (ListView) findViewById(R.id.listView);
        setupListview();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupListview() {
        List<String> items = new ArrayList<>();

        for (int i = 0 ; i < 100 ; i++) {
            items.add("This is listview item: " + i);
        }

        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        floatingActionButton.attachToListView(listView);
    }
}
