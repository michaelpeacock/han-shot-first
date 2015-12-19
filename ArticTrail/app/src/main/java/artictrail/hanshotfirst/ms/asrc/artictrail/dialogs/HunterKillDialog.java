package artictrail.hanshotfirst.ms.asrc.artictrail.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import artictrail.hanshotfirst.ms.asrc.artictrail.R;

/**
 * Created by Dad on 12/18/2015.
 */
public class HunterKillDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button ok, cancel;
    public ImageButton camera_button;

    public HunterKillDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Tag Kill");
        setContentView(R.layout.kill_dialogue);
        ok = (Button) findViewById(R.id.ok_button);
        cancel = (Button) findViewById(R.id.cancel_action);
        camera_button = (ImageButton) findViewById(R.id.camera_button);
        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
        camera_button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera_button:
                doCameraAction();
                break;
            case R.id.ok_button:
                doSave();
                break;
            case R.id.cancel_action:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

    private void doCameraAction() {

    }

    private void doSave() {

    }
}