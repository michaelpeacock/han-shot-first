package artictrail.hanshotfirst.ms.asrc.artictrail.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import artictrail.hanshotfirst.ms.asrc.artictrail.R;
import artictrail.hanshotfirst.ms.asrc.artictrail.database.DatabaseManager;
import artictrail.hanshotfirst.ms.asrc.artictrail.database.model.PreyType;
import artictrail.hanshotfirst.ms.asrc.artictrail.database.model.tables.Prey;

/**
 * Created by Owner on 12/19/2015.
 */
public class PreyListDialog extends Dialog implements android.view.View.OnClickListener {

    private Activity parent_activity;
    private int image_capture_id;
    public Dialog d;
    private Button ok;
    private ListView listView;
    private DatabaseManager database_manager;
    private List<Prey> preyList;


    public PreyListDialog(Activity a,
                          DatabaseManager db_mgr) {
        super(a);
        // TODO Auto-generated constructor stub
        this.parent_activity = a;

        database_manager = db_mgr;

        preyList = db_mgr.getPreyTable().queryForAll();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Prey");
        setContentView(R.layout.list_view);
        ok = (Button) findViewById(R.id.list_view_button);
        ok.setText("Ok");
        ok.setOnClickListener(this);
        listView = (ListView)findViewById(R.id.list_view);


        ArrayAdapter adapter = new ArrayAdapter(parent_activity, android.R.layout.simple_list_item_2, android.R.id.text1, preyList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);

                text1.setText(preyList.get(position).getName());
                text2.setText(preyList.get(position).getDescription());
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
