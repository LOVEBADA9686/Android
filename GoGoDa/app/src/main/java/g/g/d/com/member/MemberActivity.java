package g.g.d.com.member;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import g.g.d.com.R;

public class MemberActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{
    //ProgressDialog dialog;
    Button regibtn;
    Button idcheck;
    EditText mname;
    EditText mid;
    EditText mpw;
    EditText mbirth;
    EditText mgender;
    EditText mhp;
    EditText memail;
    //EditText mzipcode;
    EditText maddr;
    EditText maddrdetail;
    EditText msurvey1;
    EditText msurvey2;
    EditText msurvey3;

    RadioGroup rg;
    RadioGroup rg1;
    RadioGroup rg2;
    RadioButton rb;
    RadioButton rb1;
    RadioButton rb2;
    Button btn1;

    // 체크박스
    private TextView tv;
    private CheckBox cb1;
    private CheckBox cb2;
    private CheckBox cb3;
    private CheckBox cb4;
    private CheckBox cb5;
    private CheckBox cb6;
    private CheckBox cb7;
    private CheckBox cb8;
    private CheckBox cb9;
    private CheckBox cb10;
    private CheckBox cb11;
    private CheckBox cb12;
    private CheckBox cb13;
    private CheckBox cb14;
    private CheckBox cb15;
    private CheckBox cb16;
    private CheckBox cb17;
    private CheckBox cb18;
    private CheckBox cb19;
    private CheckBox cb20;

    String result1;

    // daum api
    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;
    private EditText et_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memeber);

        setTitle("GOGODA 회원가입");


        // 체크박스
        cb1 = (CheckBox)findViewById(R.id.checkBox1);
        cb2 = (CheckBox)findViewById(R.id.checkBox2);
        cb3 = (CheckBox)findViewById(R.id.checkBox3);
        cb4 = (CheckBox)findViewById(R.id.checkBox4);
        cb5 = (CheckBox)findViewById(R.id.checkBox5);
        cb6 = (CheckBox)findViewById(R.id.checkBox6);
        cb7 = (CheckBox)findViewById(R.id.checkBox7);
        cb8 = (CheckBox)findViewById(R.id.checkBox8);
        cb9 = (CheckBox)findViewById(R.id.checkBox9);
        cb10 = (CheckBox)findViewById(R.id.checkBox10);
        cb11 = (CheckBox)findViewById(R.id.checkBox11);
        cb12 = (CheckBox)findViewById(R.id.checkBox12);
        cb13 = (CheckBox)findViewById(R.id.checkBox13);
        cb14 = (CheckBox)findViewById(R.id.checkBox14);
        cb15 = (CheckBox)findViewById(R.id.checkBox15);
        cb16 = (CheckBox)findViewById(R.id.checkBox16);
        cb17 = (CheckBox)findViewById(R.id.checkBox17);
        cb18 = (CheckBox)findViewById(R.id.checkBox18);
        cb19 = (CheckBox)findViewById(R.id.checkBox19);
        cb20 = (CheckBox)findViewById(R.id.checkBox20);

        tv = (TextView)findViewById(R.id.textView2);
        cb1.setOnCheckedChangeListener(this);
        cb2.setOnCheckedChangeListener(this);
        cb3.setOnCheckedChangeListener(this);
        cb4.setOnCheckedChangeListener(this);
        cb5.setOnCheckedChangeListener(this);
        cb6.setOnCheckedChangeListener(this);
        cb7.setOnCheckedChangeListener(this);
        cb8.setOnCheckedChangeListener(this);
        cb9.setOnCheckedChangeListener(this);
        cb10.setOnCheckedChangeListener(this);
        cb11.setOnCheckedChangeListener(this);
        cb12.setOnCheckedChangeListener(this);
        cb13.setOnCheckedChangeListener(this);
        cb14.setOnCheckedChangeListener(this);
        cb15.setOnCheckedChangeListener(this);
        cb16.setOnCheckedChangeListener(this);
        cb17.setOnCheckedChangeListener(this);
        cb18.setOnCheckedChangeListener(this);
        cb19.setOnCheckedChangeListener(this);
        cb20.setOnCheckedChangeListener(this);

        /*
        // 화면전환
        Button imageButton = (Button) findViewById(R.id.btn1);
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SubActivity.class);
                startActivity(intent);
            }
        });

         */

        // daum api button
        et_address = (EditText)findViewById(R.id.register_addr);

        Button btn_search = (Button)findViewById(R.id.button);

        if (btn_search != null)
            btn_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MemberActivity.this, WebViewActivity.class);
                    startActivityForResult(i, SEARCH_ADDRESS_ACTIVITY);
                }
            });
/*
        // id 중복 체크 버튼 누르면 action
        idcheck.setOnClickListener(new View.OnClickListener()){
            @Override
            public void onClick(View v){
                try {
                    Toast.makeText(MainActivity.this,
                            "사용가능한 ID 입니다.",
                            Toast.LENGTH_SHORT).show();
                }catch(Exception e){
                    Toast.makeText(MainActivity.this,
                            "ID 중복체크 해주세요.",
                            Toast.LENGTH_LONG).show();
                }
            }
        };
*/
        // id 입력 에러표시
        final TextInputLayout inputLayout = findViewById(R.id.input_layout);
        inputLayout.setCounterEnabled(true);
        inputLayout.setCounterMaxLength(10);
        EditText editText = inputLayout.getEditText();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                // 정규식 필요 (아이디 유효성 검사)
                if (s.toString().contains("#")) {
                    inputLayout.setError("특수 문자는 사용할 수 없습니다.");
                } else {
                    inputLayout.setError(null); // null은 에러 메시지를 지워주는 기능
                }
            }
        });

        // pw 별표 처리
        final TextInputLayout inputLayout1 = findViewById(R.id.input_layout1);
        inputLayout1.setPasswordVisibilityToggleEnabled(true);
        inputLayout1.setCounterEnabled(true);
        inputLayout1.setCounterMaxLength(4);
        EditText editText1 = inputLayout1.getEditText();
        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence ss, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence ss, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable ss) {

            }

        });

        // pw 확인
        final TextInputLayout inputLayout2 = findViewById(R.id.input_layout2);
        inputLayout2.setPasswordVisibilityToggleEnabled(true);
        inputLayout2.setCounterEnabled(true);
        inputLayout2.setCounterMaxLength(4);
        EditText editText2 = inputLayout2.getEditText();
        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence sss, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence sss, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable sss) {

            }

        });
/*
        // progressbar
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setIndeterminate(false);
        //progressBar.setIndeterminate(true);
        //progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#00498c"), PorterDuff.Mode.MULTIPLY);
        // 60 >>> 나중에 데이터 입력 갯수로 변환
        progressBar.setProgress(50);
*/
        regibtn = (Button)findViewById(R.id.register_btn);
        idcheck = (Button)findViewById(R.id.register_idcheck);
        mname = (EditText) findViewById(R.id.register_name);
        mid = (EditText) findViewById(R.id.register_id);
        mpw = (EditText) findViewById(R.id.register_pw);
        mbirth = (EditText) findViewById(R.id.register_birth);

        idcheck.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                    String result1;
                    String id = mid.getText().toString();

                    RegisterActivity task = new RegisterActivity();
                    result1 = task.execute("IDCHECK", id).get();
                    System.out.println("id >>> : " + id);
                    System.out.println("result1 >>> : " + result1);

                    if(result1.equals("")){
                        Toast.makeText(MemberActivity.this,
                                "사용중인 ID 입니다.",
                                Toast.LENGTH_LONG).show();
                    }else if(result1.equals("ID_GOOD")){
                        Toast.makeText(MemberActivity.this,
                                "사용가능한 ID 입니다.",
                                Toast.LENGTH_LONG).show();
                    }
                }catch(Exception e){
                    Toast.makeText(MemberActivity.this,
                            "ID 중복체크 해주세요.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });



        regibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // radio button values : gender
                rg = (RadioGroup)findViewById(R.id.register_gender);
                rb = (RadioButton) findViewById(rg.getCheckedRadioButtonId());
                String mgender = rb.getText().toString();
                if(mgender.equals("남")){
                    mgender = "M";
                }else{
                    mgender = "F";
                }

                // radio button values : survey1
                rg1 = (RadioGroup)findViewById(R.id.register_survey1);
                rb1= (RadioButton) findViewById(rg1.getCheckedRadioButtonId());
                String msurvey1 = rb1.getText().toString();

                // radio button values : survey2
                rg2 = (RadioGroup)findViewById(R.id.register_survey2);
                rb2 = (RadioButton) findViewById(rg2.getCheckedRadioButtonId());
                String msurvey2 = rb2.getText().toString();

                mhp = (EditText) findViewById(R.id.register_hp);
                memail = (EditText) findViewById(R.id.register_email);
                //mzipcode = (EditText) findViewById(R.id.register_zipcode);
                maddr = (EditText) findViewById(R.id.register_addr);
                maddrdetail = (EditText) findViewById(R.id.register_addrdetail);

                try {

                    String result;
                    String name = mname.getText().toString();
                    String id = mid.getText().toString();
                    String pw = mpw.getText().toString();
                    String birth = mbirth.getText().toString();
                    String gender = mgender;
                    String hp = mhp.getText().toString();
                    String email = memail.getText().toString();
                    //String zipcode = mzipcode.getText().toString();
                    String addr = maddr.getText().toString();
                    String addrdetail = maddrdetail.getText().toString();

                    String positivefood = msurvey1;
                    String negativefood = msurvey2;
                    String movietaste = result1;

                    String addrStr[] = addr.split(", ");
                    String addrsub = "";
                    String zipcode = addrStr[0];
                    addrsub = addrStr[1];
/*
                    String poStr[] = positivefood.split(".");
                    String ppp = "";
                    ppp = poStr[0];
                    System.out.println("ppp >>> : " + ppp);

 */
                    String positive = positivefood.substring(0,positivefood.indexOf("."));
                    System.out.println(">L>>>>"+positive);

                    String negative = negativefood.substring(0,negativefood.indexOf("."));
                    System.out.println(">L>>>>"+negative);


                    System.out.println("name >>> : " + name);
                    //System.out.println("addrsub1 >>>>>>>>>>>>>>>>>>>>>>>>> : " + addrsub.getBytes());
                    System.out.println("id >>> : " + id);
                    System.out.println("pw >>> : " + pw);
                    System.out.println("birth >>> : " + birth);
                    System.out.println("gender >>> : " + gender);
                    System.out.println("hp >>> : " + hp);
                    System.out.println("email >>> : " + email);
                    System.out.println("zipcode >>> : " + zipcode);
                    System.out.println("addrsub >>> : " + addrsub);
                    System.out.println("addrdetail >>> : " + addrdetail);
                    // System.out.println("positivefood >>> : " + positivefood);
                    // System.out.println("negativefood >>> : " + negativefood);
                    System.out.println("movietaste >>> : " + movietaste);


                    RegisterActivity task = new RegisterActivity();
                    result = task.execute("I", name, id, pw, birth, gender, hp, email, zipcode, addrsub,
                            addrdetail, positive, negative, movietaste).get();

                    System.out.println("result >>> : " + result);

                    if(result.toUpperCase().equals("GOOD")){
                        Toast.makeText(MemberActivity.this,
                                "저장되었습니다.",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }else{

                        Toast.makeText(MemberActivity.this, "회원가입을 실패 했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Log.i("DBTEST", "..........ERROR.........");
                    Toast.makeText(MemberActivity.this, "회원정보를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // 체크박스 결과 창
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        String result = "";
        /*
        if(cb1.isChecked()) result += cb1.getText().toString() + ",";
        if(cb2.isChecked()) result += cb2.getText().toString() + ",";
        if(cb3.isChecked()) result += cb3.getText().toString() + ",";
        if(cb4.isChecked()) result += cb4.getText().toString() + ",";
        if(cb5.isChecked()) result += cb5.getText().toString() + ",";
        if(cb6.isChecked()) result += cb6.getText().toString() + ",";
        if(cb7.isChecked()) result += cb7.getText().toString() + ",";
        if(cb8.isChecked()) result += cb8.getText().toString() + ",";
        if(cb9.isChecked()) result += cb9.getText().toString() + ",";
        if(cb10.isChecked()) result += cb10.getText().toString() + ",";
        if(cb11.isChecked()) result += cb11.getText().toString() + ",";
        if(cb12.isChecked()) result += cb12.getText().toString() + ",";
        if(cb13.isChecked()) result += cb13.getText().toString() + ",";
        if(cb14.isChecked()) result += cb14.getText().toString() + ",";
        if(cb15.isChecked()) result += cb15.getText().toString() + ",";
        if(cb16.isChecked()) result += cb16.getText().toString() + ",";
        if(cb17.isChecked()) result += cb17.getText().toString() + ",";
        if(cb18.isChecked()) result += cb18.getText().toString() + ",";
        if(cb19.isChecked()) result += cb19.getText().toString() + ",";
        if(cb20.isChecked()) result += cb20.getText().toString() + ",";
*/
        if(cb1.isChecked()) result += "drama" + ",";
        if(cb2.isChecked()) result += "war" + ",";
        if(cb3.isChecked()) result += "action" + ",";
        if(cb4.isChecked()) result += "family" + ",";
        if(cb5.isChecked()) result += "adult" + ",";
        if(cb6.isChecked()) result += "crime" + ",";
        if(cb7.isChecked()) result += "mystery" + ",";
        if(cb8.isChecked()) result += "animation" + ",";
        if(cb9.isChecked()) result += "comedy" + ",";
        if(cb10.isChecked()) result += "horror" + ",";
        if(cb11.isChecked()) result += "romance" + ",";
        if(cb12.isChecked()) result += "SF" + ",";
        if(cb13.isChecked()) result += "documentary" + ",";
        if(cb14.isChecked()) result += "thriller" + ",";
        if(cb15.isChecked()) result += "musical" + ",";
        if(cb16.isChecked()) result += "opera" + ",";
        if(cb17.isChecked()) result += "fantasy" + ",";
        if(cb18.isChecked()) result += "adventure" + ",";
        if(cb19.isChecked()) result += "historical" + ",";
        if(cb20.isChecked()) result += "western" + ",";

        tv.setText("체크항목: " + result);

        // 결과 창 안보이게 하기
        tv.setVisibility(View.GONE);

        result1 = result;
    }


    // daum api connect
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        switch(requestCode){
            case SEARCH_ADDRESS_ACTIVITY:
                if(resultCode == RESULT_OK){
                    String data = intent.getExtras().getString("data");
                    if (data != null)
                        et_address.setText(data);
                }
                break;
        }
    }

}