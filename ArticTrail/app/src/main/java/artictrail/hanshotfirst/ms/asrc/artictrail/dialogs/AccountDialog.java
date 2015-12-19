/**
 * Created by Dad on 12/19/2015.
 */
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
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageButton;
        import android.widget.ImageView;

        import java.io.File;
        import java.io.IOException;
        import java.text.SimpleDateFormat;
        import java.util.Date;

        import artictrail.hanshotfirst.ms.asrc.artictrail.R;
        import artictrail.hanshotfirst.ms.asrc.artictrail.database.DatabaseManager;
        import artictrail.hanshotfirst.ms.asrc.artictrail.database.model.PreyType;
        import artictrail.hanshotfirst.ms.asrc.artictrail.database.model.tables.Prey;
        import artictrail.hanshotfirst.ms.asrc.artictrail.database.model.tables.User;

/**
 * Created by Dad on 12/18/2015.
 */
public class AccountDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Activity parent_activity;
    public Dialog d;
    private Button ok;
    private User current_user_entry;
    private EditText account_name_text, device_name_text;
    private DatabaseManager database_manager;


    public AccountDialog(Activity a,
                            DatabaseManager db_mgr ) {
        super(a);
        // TODO Auto-generated constructor stub
        this.parent_activity = a;

        database_manager = db_mgr;

        current_user_entry = new User();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Account Info");
        setContentView(R.layout.account_dialog);
        account_name_text = (EditText) findViewById(R.id.account_name);
        device_name_text = (EditText) findViewById(R.id.device_name);
        ok = (Button) findViewById(R.id.ok_button);
        ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok_button:
                doSave();
                dismiss();
                break;
            default:
                break;
        }
    }

    private void doSave() {
        current_user_entry.setmName(account_name_text.getText().toString());
        current_user_entry.setDeviceName(device_name_text.getText().toString());

        // save to database  //TODO: Primary User??
        database_manager.getUserTable().create(current_user_entry);
    }


}