package g.g.d.com.board;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import g.g.d.com.R;

// 게시판 글 작성 화면
public class BoardWrite extends AppCompatActivity {

    private Button btn_write; // 작성 완료 버튼
    private Button btn_back;  // 게시판 목록으로 돌아가는 버튼
    private EditText bsubject, bname, bcontent, bpw; // 컬럼 목록
    private final int GET_GALLERY_IMAGE = 200;       // 갤러리 이미지 가져오기
    private ImageView bfile;  // 이미지 뷰 클릭시 사진 불러오기

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_write);


        btn_write = findViewById(R.id.btn_write);
        btn_back = findViewById(R.id.btn_back);
        bsubject = findViewById(R.id.bsubject);
        bname = findViewById(R.id.bname);
        bcontent = findViewById(R.id.bcontent);
        bpw = findViewById(R.id.bpw);
        bfile = (ImageView)findViewById(R.id.bfile);

        // 이미지 클릭 시 파일 첨부
        bfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

        // 작성 후 게시글 리스트 이동 버튼
        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Toast.makeText(getApplicationContext(), "작성 완료", Toast.LENGTH_SHORT).show();
                    String result;
                    String subject = bsubject.getText().toString();
                    String name = bname.getText().toString();
                    String content = bcontent.getText().toString();
                    String pw = bpw.getText().toString();
                    Drawable drawable = bfile.getDrawable();
                    Bitmap file = ((BitmapDrawable)drawable).getBitmap();
                    BoardItem writeItem = new BoardItem(subject, name, content, pw, file);


                    System.out.println("subject >>> : " + subject);
                    System.out.println("name >>> : " + name);
                    System.out.println("content >>> : " + content);
                    System.out.println("pw >>> : " + pw);
                    // System.out.println("file >>> : " + file);

                    RegisterTask task = new RegisterTask();
                    // execute 명령어를 통해 AsyncTastk 실행
                    // 클래스.execute(인자).get() 을 하면 결과가 자동으로 들어간다.
                    result = task.execute(writeItem).get();
                    System.out.println("result >>> : " + result);
                }catch (Exception e){
                    Log.i("DBTEST", "..........ERROR.........");
                }
                // 화면 전환
                Intent intent = new Intent(BoardWrite.this, BoardListView.class);
                startActivity(intent);
                finish();
            }
        });

        // 작성 취소 후 게시글 리스트 이동 버튼
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BoardWrite.this, BoardListView.class);
                Toast.makeText(getApplicationContext(), "작성 취소", Toast.LENGTH_SHORT).show();
                startActivity(intent); // 액티비티 이동
                finish();
            }
        });
    }
    // 사진 업로드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            bfile.setImageURI(selectedImageUri);
        }
    }
}