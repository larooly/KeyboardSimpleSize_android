package com.example.keyboardsheet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    ConstraintLayout _total;
    int totalHeight = 0;
    TextView show;
    Rect screenStart;
    Rect screenBefore;
    View view;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _total = findViewById(R.id.main);

        show = findViewById( R.id.showmw);
        view = findViewById(R.id.view);

        editText = findViewById(R.id.editText);

        screenStart = new Rect();
        _total.getWindowVisibleDisplayFrame(screenStart);
        totalHeight = screenStart.bottom;



        _total.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
            @Override
            public void onGlobalLayout() {//여기서 액션을 다 처리한다면?

                Rect r = new Rect();
                _total.getWindowVisibleDisplayFrame(r);
                if(screenStart.right !=r.right){
                    Log.d("@@2","what the ;;;;");
                }
                int heightNow =  r.bottom;//현재 크기
                int differ = totalHeight-heightNow;
                show.setText("전체 "+String.valueOf(totalHeight)+" 가림  "+String.valueOf(heightNow)+" 키보드 "+String.valueOf(differ));
                Log.d("@@2",String.valueOf(r));
                //view.setLayoutParams(new ConstraintLayout.LayoutParams(differ,differ));


                if(differ >200){//키보드가 올라오면
                    //	키보드 열림
                    //MiniMenuHeight = differ;//크기 계산
                    view.setBackgroundColor(Color.argb(255,255,0,0));//빨강
                }
                else {
                    view.setBackgroundColor(Color.argb(255,255,255,0));
                }



            }



        });

    }

    private int checkKeyHeight() {//20201116 KJS 전체화면 높이 구하기
        int total =0;
        int nowSee = 0;
        int Statusbar = 0;
        //전체
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Display display = getWindowManager().getDefaultDisplay();
        display.getMetrics(displayMetrics);
        total = displayMetrics.heightPixels;
        int heightPixels = 0;
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.R){// 만약 30 미만이 들어가면 오류남
            WindowMetrics windowMetrics = getWindowManager().getCurrentWindowMetrics();
            heightPixels = windowMetrics.getBounds().height();//width는 좌우 빼야 같아 짐
        }
        else {
            final WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            final DisplayMetrics metrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(metrics);//버전 나누는 걸로해결
            heightPixels =metrics.heightPixels;
        }
        total = heightPixels;

        //보이는 영역
        Rect k = new Rect();

        _total.getWindowVisibleDisplayFrame(k);
        nowSee = k.bottom - k.top;//현재 보여지는 영역 구하기
        //상태바 영역
        int rID = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if(rID>0){
            Statusbar= getResources().getDimensionPixelSize(rID);
        }
        int keyHeight= total-nowSee-Statusbar;
        if(keyHeight> 0){
            nowSee=  keyHeight;//키보드를 구할수는 있긴한데 문제가 좀있어서 안쓴다
        }
        return  nowSee;
    }

}