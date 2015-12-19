package artictrail.hanshotfirst.ms.asrc.artictrail.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import artictrail.hanshotfirst.ms.asrc.artictrail.R;
import artictrail.hanshotfirst.ms.asrc.artictrail.database.DatabaseManager;
import artictrail.hanshotfirst.ms.asrc.artictrail.database.model.tables.Location;
import artictrail.hanshotfirst.ms.asrc.artictrail.database.model.tables.Prey;

/**
 * Created by Owner on 12/19/2015.
 */
public class LocationListDialog extends Dialog implements android.view.View.OnClickListener {

    private Activity parent_activity;
    private int image_capture_id;
    public Dialog d;
    private Button ok;
    private ListView listView;
    private DatabaseManager database_manager;
    private List<Location> locationList;


    public LocationListDialog(Activity a,
                          DatabaseManager db_mgr) {
        super(a);
        // TODO Auto-generated constructor stub
        this.parent_activity = a;

        database_manager = db_mgr;

        locationList = db_mgr.getLocationTable().queryForAll();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Location");
        setContentView(R.layout.list_view);
        ok = (Button) findViewById(R.id.list_view_button);
        ok.setText("Ok");
        ok.setOnClickListener(this);
        listView = (ListView)findViewById(R.id.list_view);


        ArrayAdapter adapter = new ArrayAdapter(parent_activity, android.R.layout.simple_list_item_2, android.R.id.text1, locationList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(locationList.get(position).getLocationType().name());
                double lat = locationList.get(position).getLatitude();
                double lon = locationList.get(position).getLongitude();
                String t = lat + ", " + lon;
                text2.setText(t);
                return view;
            }
        };

        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.list_view_button:
                dismiss();
                break;
            default:
                break;
        }
    }

}
