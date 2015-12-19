package artictrail.hanshotfirst.ms.asrc.artictrail.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import artictrail.hanshotfirst.ms.asrc.artictrail.R;
import artictrail.hanshotfirst.ms.asrc.artictrail.database.model.tables.Prey;

/**
 * Created by Dad on 12/18/2015.
 */
public class HunterKillDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Activity parent_activity;
    public Dialog d;
    private Button ok, cancel;
    private ImageButton camera_button;
    private Prey current_prey_entry;
    private EditText title_text, description_text;
    String current_photo_path;
    private ImageView kill_image;
    private File storageDir;


    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;

    public HunterKillDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.parent_activity = a;

        current_prey_entry = new Prey();

//        current_prey_entry.setLatitude();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Tag Kill");
        setContentView(R.layout.kill_dialogue);
        title_text = (EditText) findViewById(R.id.kill_title);
        description_text = (EditText) findViewById(R.id.kill_description);
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
                dismiss();
                break;
            case R.id.cancel_action:
                dismiss();
                break;
            default:
                break;
        }
    }

    private void doSave() {
        current_prey_entry.setName(title_text.getText().toString());
        current_prey_entry.setImagePath(storageDir.getAbsolutePath());

        // save to database
    }

    private void doCameraAction() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(parent_activity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                parent_activity.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

                galleryAddPic();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            kill_image.setImageBitmap(imageBitmap);
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        Log.d("ArcticTrail", "storageDir: " + storageDir.getName() );
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        current_photo_path = "file:" + image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(current_photo_path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        parent_activity.sendBroadcast(mediaScanIntent);
    }

}