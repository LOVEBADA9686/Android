package g.g.d.com.board;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import g.g.d.com.R;

// 게시글 상세 페이지
public class RboardActivity extends AppCompatActivity {

    private Button btn_list; // 게시판 목록으로 돌아가는 버튼
    private Button rb_input;  // 댓글 작성 버튼
    private Intent intent;
    private TextView bnum;   // 게시글 상세 목록의 글번호
    private TextView bname;  // 작성자
    private TextView binsertdate; // 게시글 등록일자
    private TextView bsubject;    // 글 제목
    private TextView bcontent;    // 글 내용
    private ImageView bfile;      // 사진
    private EditText rbname, rbcontent;  // 댓글 작성자, 내용
    private ListView rboard_listView; // 댓글 리스트 뷰
    private RboardAdapter adapter;    // 댓글 리스트 뷰 어댑터

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rboard);


        bnum = (TextView)findViewById(R.id.bnum);
        bname = (TextView)findViewById(R.id.bname);
        binsertdate = (TextView)findViewById(R.id.binsertdate);
        bsubject = (TextView)findViewById(R.id.bsubject);
        bcontent = (TextView)findViewById(R.id.bcontent);
        bfile = (ImageView)findViewById(R.id.bfile);
        rbname = (EditText)findViewById(R.id.rbname);
        rbcontent = (EditText)findViewById(R.id.rbcontent);

        String number = getIntent().getStringExtra("bnum");

        BoardDetailTask task = new BoardDetailTask();
        BoardItem item = null;
        try {
            item = task.execute(number).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (item != null)
        {
            bnum.setText(item.getNum());
            bname.setText(item.getName());
            binsertdate.setText(item.getInsertdate());
            bsubject.setText(item.getSubject());
            bcontent.setText(item.getContent());
            bfile.setImageBitmap(item.getFile());
        }



        // Adapter 생성


        // 리스트뷰 참조 및 Adapter 담기
        rboard_listView = (ListView) findViewById(R.id.rboard_listView);


        // 댓글 첫 번째 아이템 추가

        // 게시판 댓글 (작성자, 내용, 작성일)
        RboardJsonTask rboardTask = new RboardJsonTask();
        try {
            ArrayList<RboardListViewItem> rBoardResult = rboardTask.execute(number).get();
            for(int i=0; i<rBoardResult.size(); i++){
                System.out.println(new String(rBoardResult.get(i).getRbcontent().getBytes("UTF-8"),"8859_1"));
                System.out.println(new String(rBoardResult.get(i).getRbname().getBytes("UTF-8"),"8859_1"));
                System.out.println(new String(rBoardResult.get(i).getRbcontent().getBytes("8859_1"),"UTF-8"));
                System.out.println(new String(rBoardResult.get(i).getRbname().getBytes("8859_1"),"UTF-8"));
            }
            adapter = new RboardAdapter(rBoardResult);
            rboard_listView.setAdapter(adapter);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        // 댓글 : 작성버튼 누르면 리스트뷰에 추가
        rb_input = findViewById(R.id.rb_input);
        rb_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Toast.makeText(getApplicationContext(), "작성 완료", Toast.LENGTH_SHORT).show();
                    String result;
                    String name = rbname.getText().toString();
                    String content = rbcontent.getText().toString();

                    System.out.println("name >>> : " + name);
                    System.out.println("content >>> : " + content);

                    RboardTask task = new RboardTask();
                    result = task.execute(number, name, content, "password").get();
                    System.out.println("result >>> : " + result);

                    RboardJsonTask rboardTask = new RboardJsonTask();
                    try {
                        ArrayList<RboardListViewItem> rBoardResult = rboardTask.execute(number).get();
                        adapter.reset(rBoardResult);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }catch(Exception e){
                    Log.i("DBTEST", "..........ERROR.........");
                }
            }
        });


        // 게시판 목록으로 돌아가는 버튼
        btn_list = findViewById(R.id.btn_list);
        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RboardActivity.this, BoardListView.class);
                startActivity(intent);
                finish();
            }
        });
    }
}