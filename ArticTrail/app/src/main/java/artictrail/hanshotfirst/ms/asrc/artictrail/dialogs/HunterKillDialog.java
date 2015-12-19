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

/**
 * Created by Dad on 12/18/2015.
 */
public class HunterKillDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Activity parent_activity;
    private int image_capture_id;
    public Dialog d;
    private Button ok, cancel;
    private ImageButton camera_button;
    private Prey current_prey_entry;
    private EditText title_text, description_text;
    String current_photo_path;
    private ImageView kill_image;
    private File storageDir;
    private File photoFile;
    private DatabaseManager database_manager;


    public HunterKillDialog(Activity a,
                            int image_capture_id_in,
                            DatabaseManager db_mgr,
                            double lat,
                            double lon ) {
        super(a);
        // TODO Auto-generated constructor stub
        this.parent_activity = a;

        image_capture_id = image_capture_id_in;

        database_manager = db_mgr;

        current_prey_entry = new Prey();
        current_prey_entry.setLatitude(lat);
        current_prey_entry.setLongitude(lon);

//        current_prey_entry.setLatitude();
    }

    public void setKillImage() {
        Log.d("ArcticTrail","In setKillImage: photoFile.getAbsolutePath(): " + photoFile.getAbsolutePath());
        Bitmap kill_bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
        int srcWidth = kill_bitmap.getWidth();
        int srcHeight = kill_bitmap.getHeight();
        int dstWidth = (int)(srcWidth*0.6f);
        int dstHeight = (int)(srcHeight*0.6f);
        Bitmap dstBitmap = Bitmap.createScaledBitmap(kill_bitmap, dstWidth, dstHeight, true);

        if ( kill_bitmap != null ) {
            kill_image.setImageBitmap(dstBitmap);
            galleryAddPic();
        }
        else {
            Log.d("ArcticTrail", "kill_bitmap is NULL!!!");
        }
    }

//    public void setKillImage(Bitmap imageBitmap) {
//        kill_image.setImageBitmap(imageBitmap);
//    }

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
        kill_image = (ImageView) findViewById(R.id.kill_image);


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
        current_prey_entry.setDescription(description_text.getText().toString());
        current_prey_entry.setType(PreyType.HUNT);

        if ( storageDir.getAbsolutePath() != null ) {
            current_prey_entry.setImagePath(storageDir.getAbsolutePath());
        }

        // save to database  //TODO: Primary User??
        database_manager.getPreyTable().create(current_prey_entry);
    }

    private void doCameraAction() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(parent_activity.getPackageManager()) != null) {
            // Create the File where the photo should go
            photoFile = null;
            try {
                photoFile = createImageFile();
                Log.d("ArcticTrail","No photoFile Exception");
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d("ArcticTrail","photoFile EXCEPTION!!!" + ex.getMessage());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Log.d("ArcticTrail", "photoFile.getAbsolutePath(): " + photoFile.getAbsolutePath());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                parent_activity.startActivityForResult(takePictureIntent, image_capture_id);
            }
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
        Log.d("ArcticTrail","current_photo_path: " + current_photo_path );

        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(current_photo_path);
        Log.d("ArcticTrail","In galleryAddPic(): current_photo_path: " + current_photo_path);
        Log.d("ArcticTrail","In galleryAddPic(): f.getAbsolutePath(): " + f.getAbsolutePath());
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        parent_activity.sendBroadcast(mediaScanIntent);
    }

}