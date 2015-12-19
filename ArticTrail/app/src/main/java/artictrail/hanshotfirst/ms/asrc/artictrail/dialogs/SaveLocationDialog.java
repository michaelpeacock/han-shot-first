package artictrail.hanshotfirst.ms.asrc.artictrail.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import artictrail.hanshotfirst.ms.asrc.artictrail.R;
import artictrail.hanshotfirst.ms.asrc.artictrail.database.DatabaseManager;
import artictrail.hanshotfirst.ms.asrc.artictrail.database.model.tables.Prey;

/**
 * Created by trapletreat on 12/19/15.
 */
public class SaveLocationDialog extends Dialog implements
        android.view.View.OnClickListener {

    private Button ok, cancel;
    private EditText title_text, description_text;

    public SaveLocationDialog(Activity a,
                            DatabaseManager db_mgr,
                            double lat,
                            double lon ) {
        super(a);
        // TODO Auto-generated constructor stub

    }
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setTitle("Save Location Type");
    setContentView(R.layout.save_location_dialogue);
    ok = (Button) findViewById(R.id.save_current_ok_button);
    cancel = (Button) findViewById(R.id.save_current_location_cancel_action);
    ok.setOnClickListener(this);
    cancel.setOnClickListener(this);
}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //case R.id.e:
              //  doCameraAction();
                //break;
            case R.id.save_current_ok_button:
                doSave();
                dismiss();
                break;
            case R.id.save_current_location_cancel_action:
                dismiss();
                break;
            default:
                break;
        }
    }

    public void doSave()
    {


    }


}
