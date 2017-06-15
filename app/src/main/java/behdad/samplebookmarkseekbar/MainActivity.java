package behdad.samplebookmarkseekbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FrameLayout flBookMarkList;
    private TextView tvSelectedPage;
    private TextView tv;
    private EditText etTotalPage;
    private EditText etInput;
    private Button btSetTotalPage;
    private Button btAdd;
    private Button btRemove;
    private int listWidth;
    private int bookMarkItemWidth;

    private int totalPage;
    private List<Integer> bookMarkList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initٰViews();
        setListeners();

        bookMarkList = new ArrayList<>();
    }

    private void initٰViews() {
        flBookMarkList = (FrameLayout) findViewById(R.id.flBookMarkList);
        tvSelectedPage = (TextView) findViewById(R.id.tvSelectedPage);
        tv = (TextView) findViewById(R.id.tv);
        etTotalPage = (EditText) findViewById(R.id.etTotalPage);
        etInput = (EditText) findViewById(R.id.etInput);
        btSetTotalPage = (Button) findViewById(R.id.btSetTotalPage);
        btAdd = (Button) findViewById(R.id.btAdd);
        btRemove = (Button) findViewById(R.id.btRemove);

        flBookMarkList.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                flBookMarkList.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                listWidth = flBookMarkList.getWidth();
            }
        });

        bookMarkItemWidth = dpToPx(32); //todo bad solution
    }

    private void setListeners() {
        btSetTotalPage.setOnClickListener(this);
        btAdd.setOnClickListener(this);
        btRemove.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btSetTotalPage:
                setTotalPage();
                break;

            case R.id.btAdd:
                addBookMark();
                break;

            case R.id.btRemove:
//                printBookMArk();
                makeList();
                break;
        }
    }

    private void setTotalPage() {
        if (etTotalPage.getText().toString().isEmpty()) {
            return;
        }
        totalPage = Integer.parseInt(etTotalPage.getText().toString());
    }

    private void addBookMark() {
        if (etInput.getText().toString().isEmpty()) {
            return;
        }

        int page = Integer.valueOf(etInput.getText().toString());

        if (page > totalPage || page <= 0) {
            return;
        }

        bookMarkList.add(page);
        etInput.setText("");
    }

    private void sortList() {
        Set<Integer> hashSet = new HashSet<>();
        hashSet.addAll(bookMarkList);
        bookMarkList.clear();
        bookMarkList.addAll(hashSet);

        Collections.sort(bookMarkList);
    }

    private void printBookMArk() {
        sortList();

        String list = "";
        for (int i = 0; i < bookMarkList.size(); i++) {
            list = list + bookMarkList.get(i) + "-";
        }
        tv.setText(list);

    }

    private void makeList() {
        flBookMarkList.removeAllViews();
        sortList();

        for (int i = 0; i < bookMarkList.size(); i++) {
            View bookMarkItem = getLayoutInflater().inflate(R.layout.item_book_mark, null);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            int marginLeft = getBookMarkPosition(bookMarkList.get(i)) - bookMarkItemWidth / 2;
            layoutParams.setMargins(marginLeft, 0, 0, 0);

            flBookMarkList.addView(bookMarkItem, layoutParams);
        }
    }

    private int getBookMarkPosition(int page) {
        return page * 100 / totalPage * listWidth / 100;
    }

    public int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics());
    }
}
