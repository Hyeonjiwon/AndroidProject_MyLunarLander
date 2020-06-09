package kr.co.company.mylunarlander;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class LunarLander extends AppCompatActivity {
    /** A handle to the thread that's actually running the animation. */
    private LunarView.LunarThread mLunarThread;

    /** A handle to the View in which the game is running. */
    private LunarView mLunarView;

    private ProgressBar mProgress;
    private int mProgressStatus = 0;
    private int count = 0;

    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // tell system to use the layout defined in our XML file
        setContentView(R.layout.activity_lunarlander);

        // get handles to the LunarView from XML, and its LunarThread
        mLunarView = (LunarView) findViewById(R.id.lunar);
        mLunarThread = mLunarView.getThread();

        // 프로그래스
        mProgress = (ProgressBar) findViewById(R.id.progressBar);
        new CounterTask().execute(0);

        text = findViewById(R.id.text);

        // 메뉴버튼에 클릭 리스너 등록
        Button menu_btn = findViewById(R.id.menu_btn);
        menu_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                PopupMenu popup= new PopupMenu(getApplicationContext(), view);//v는 클릭된 뷰를 의미

                getMenuInflater().inflate(R.menu.option_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.m1:
                                // 게임 일시 정지
                                mLunarThread.pause();
                                Toast.makeText(getApplication(),"STOP",Toast.LENGTH_SHORT).show();
                                break;

                            case R.id.m2:
                                // 게임 다시 시작
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                //mLunarThread.unpause();
                                Toast.makeText(getApplication(),"HOME",Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });

                popup.show();//Popup Menu 보이기
            }
        });
    }

    class CounterTask extends AsyncTask<Integer, Integer, Integer> {
        protected void onPreExecute() {
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            // 100초가 될 동안
            while (count < 100) {
                try{
                    Thread.sleep(100); //0.1초
                } catch (Exception e) {
                    e.printStackTrace();
                }
                count++;
                publishProgress(count);
            }
            return count;
        }

        protected void onProgressUpdate(Integer... integers) {
            mProgress.setProgress(count);
        }

        protected void onPostExecute(Integer result) {
            if(count < 100) {
                mProgress.setProgress(count);
            }
            else {
                text.setText("GAME OVER");
                onPause();
            }
        }
    }

    /**
     * Invoked when the Activity loses user focus.
     */
    protected void onPause() {
        super.onPause();
        mLunarView.getThread().pause(); // pause game when Activity pauses
    }

    /**
     * Notification that something is about to happen, to give the Activity a
     * chance to save state.
     *
     * @param outState a Bundle into which this Activity should save its state
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // just have the View's thread save its state into our Bundle
        super.onSaveInstanceState(outState);
        mLunarThread.saveState(outState);
    }
}
