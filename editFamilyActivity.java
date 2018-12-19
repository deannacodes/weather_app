package j.edu.wasp;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class editFamilyActivity extends AppCompatActivity {
    private ArrayList<FamilyMember> family = MapsActivity.family.getFamily();
    private ListView listView;
    private static final String TAG = "ListDataActivity";
   // DataBaseHelper dataBaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_family);
        //dataBaseHelper = new DataBaseHelper(this);

        listView = (ListView) findViewById(R.id.listFam);

        ArrayList<String> listData = new ArrayList<>();

        for(FamilyMember fam: family) {
            listData.add(fam.getFName() + " " + fam.getlName());

        }
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String name = adapterView.getItemAtPosition(position).toString();
                Log.d(TAG, "onItemClick: You Clicked on " + name);

                System.out.println(adapterView.getItemAtPosition(position).toString());
                System.out.println("ID IS " + id);
                Intent intent = new Intent(editFamilyActivity.this, EditFam.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

    }

}