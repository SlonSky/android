package slon.sky.dev.postcard;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CardInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_info);


        String title = getIntent().getStringExtra("title");
        final String message = getIntent().getStringExtra("message");
        String imageUri = getIntent().getStringExtra("imageUri");

        final TextView tvTitle = (TextView) findViewById(R.id.card_info_title);
        ImageView ivImage = (ImageView) findViewById(R.id.card_info_image);
        Button choose = (Button) findViewById(R.id.choose);

        tvTitle.setText(title);

        int resId = getResources().getIdentifier(imageUri, null, getPackageName());
        Drawable res = getResources().getDrawable(resId);
        ivImage.setImageDrawable(res);

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CardInfoActivity.this, MainActivity.class);
                intent.putExtra("imageUri", "@drawable/" + tvTitle.getText().toString().toLowerCase());
                intent.putExtra("message", message);
                startActivity(intent);
            }
        });


    }
}
