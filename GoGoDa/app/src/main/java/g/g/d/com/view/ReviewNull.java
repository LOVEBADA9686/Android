package g.g.d.com.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import g.g.d.com.R;

public class ReviewNull extends AppCompatActivity {


    private Button insertBtn;
    private String kakaoid, mid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_null);

        Intent getIntent = getIntent();
        kakaoid = getIntent.getStringExtra("kakaoid");
        mid = getIntent.getStringExtra("mid");

        insertBtn = (Button)findViewById(R.id.reviewNullInsertbtn);
        insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReviewNull.this, ReviewInsert.class);
                intent.putExtra("kakaoid", kakaoid);
                intent.putExtra("mid", mid);
                startActivity(intent);
            }
        });


    }



    @Override
    protected void onResume() {
        super.onResume();




    }
}
