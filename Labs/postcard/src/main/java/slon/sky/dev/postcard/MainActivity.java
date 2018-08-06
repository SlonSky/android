package slon.sky.dev.postcard;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.BitmapCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView ivPicture;
    private EditText etMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivPicture = (ImageView) findViewById(R.id.picture);
        etMessage = (EditText) findViewById(R.id.message);
        Button btnChooseAsset = (Button) findViewById(R.id.chooseAsset);

        btnChooseAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CardsActivity.class);
                startActivity(intent);
            }
        });

        if (getIntent().getStringExtra("message") != null) {
            etMessage.setText(getIntent().getStringExtra("message"));
        }

        if(getIntent().getStringExtra("imageUri") != null) {
            int resId = getResources().getIdentifier(getIntent().getStringExtra("imageUri"), null, getPackageName());
            ivPicture.setImageDrawable(getResources().getDrawable(resId));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.photo:
                Intent intentPhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(intentPhoto.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intentPhoto, REQUEST_IMAGE_CAPTURE);
                } else {
                    Toast.makeText(this, "Can't open camera", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.share:
                Intent intentShare = new Intent(Intent.ACTION_SEND);

                String message = etMessage.getText().toString();

                Bitmap bitmap = ((BitmapDrawable) ivPicture.getDrawable()).getBitmap();
                Uri imgUri = FileProvider.getUriForFile(this, "slon.sky.dev.fileprovider", saveImg(bitmap));

                intentShare.setType("image/*");
                intentShare.putExtra(Intent.EXTRA_SUBJECT, message);
                intentShare.putExtra(Intent.EXTRA_STREAM, imgUri);

                List<ResolveInfo> infos = getPackageManager().queryIntentActivities(intentShare, 0);
                if (infos.size() > 0) {
                    startActivity(intentShare);
                } else {
                    Toast.makeText(this, "Can't send postcard", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ivPicture.setImageBitmap(imageBitmap);
        }
    }

    @Nullable
    private File saveImg(Bitmap bitmap) {

        String name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        name = "card_" + name + ".jpeg";

        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = new File(dir, name);
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
