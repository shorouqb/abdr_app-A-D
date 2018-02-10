package com.example.vip.abdr_app_a_d;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class support_info extends BaseActivityA implements View.OnClickListener  {

    ImageView answer;
    int pos=0;
    TextView bref,title,stat,answ,des,line,anstv;
    String []titles;
    String []ref;
    String []com;
    String []answers;
    String []state;
    int []id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_info);
        answer = (ImageView) findViewById(R.id.answer);
        answer.setOnClickListener(this);


        Intent i = getIntent();

        pos=i.getExtras().getInt("pos");
        id =new int[i.getIntArrayExtra("id").length];
        id=i.getIntArrayExtra("id");
        titles=new String [i.getStringArrayExtra("titles").length];
        titles=i.getStringArrayExtra("titles");

        ref=new String [i.getStringArrayExtra("ref").length];
        ref=i.getStringArrayExtra("ref");

        com=new String [i.getStringArrayExtra("desc").length];
        com=i.getStringArrayExtra("desc");

        answers=new String [i.getStringArrayExtra("answer").length];
        answers=i.getStringArrayExtra("answer");

        state=new String [i.getStringArrayExtra("state").length];
        state=i.getStringArrayExtra("state");

        bref=(TextView)findViewById(R.id.bookref);
        title=(TextView)findViewById(R.id.title);
        stat=(TextView)findViewById(R.id.state);
        answ=(TextView)findViewById(R.id.ans);
        des=(TextView)findViewById(R.id.com);
        line=(TextView)findViewById(R.id.v);

        anstv=(TextView)findViewById(R.id.s4);

        bref.setText(ref[pos]);
        title.setText(titles[pos]);
        stat.setText(state[pos]);
        des.setText(com[pos]);

        if(state[pos].equals("Answerd")){
            answ.setText(answers[pos]);
            answer.setVisibility(View.GONE);
        }
        else{
            answ.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
            anstv.setVisibility(View.GONE);
        }

    }
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.answer:
                Intent intent = new Intent(support_info.this,support_answer.class);
                intent.putExtra("id",id[pos]);
                intent.putExtra("ref",ref[pos]);
                intent.putExtra("title",titles[pos]);
                intent.putExtra("complain",com[pos]);
                startActivity(intent);
                break;


        }

    }

}
