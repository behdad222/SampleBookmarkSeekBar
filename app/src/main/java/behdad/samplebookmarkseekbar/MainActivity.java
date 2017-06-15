package behdad.samplebookmarkseekbar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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
    private List<Integer> positionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initٰViews();
        setListeners();

        bookMarkList = new ArrayList<>();
        positionList = new ArrayList<>();
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
        makePositionList();

        for (int i = 0; i < positionList.size(); i++) {
            View bookMarkItem = getLayoutInflater().inflate(R.layout.item_book_mark, null);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            layoutParams.setMargins(positionList.get(i), 0, 0, 0);

            flBookMarkList.addView(bookMarkItem, layoutParams);
        }
    }

    private void makePositionList() {
        positionList.clear();
        for (int bookMarkIndex = 0; bookMarkIndex < bookMarkList.size(); bookMarkIndex++) {
            int position = getBookMarkPosition(bookMarkList.get(bookMarkIndex)) - bookMarkItemWidth / 2;
            int positionIndex = positionList.size();

            if (positionIndex > 0) {
                int space = position - bookMarkItemWidth - positionList.get(positionIndex - 1);

                if (space < 0) {
                    int newSpace = space / -2;

                    if (positionIndex > 1) {
                        if (positionList.get(positionIndex - 2) < positionList.get(positionIndex - 1) - newSpace - bookMarkItemWidth) {
                            positionList.set(positionIndex - 1, positionList.get(positionIndex - 1) - newSpace);
                            positionList.add(position + newSpace);
                        }

                    } else {
                        if (positionList.get(positionIndex - 1) - newSpace > 0) {
                            positionList.set(positionIndex - 1, positionList.get(positionIndex - 1) - newSpace);
                            positionList.add(position + newSpace);
                        }
                    }
                } else {
                    positionList.add(position);
                }
            } else {
                positionList.add(position);
            }
        }
    }

    private int getBookMarkPosition(int page) {
        return page * 100 / totalPage * (listWidth - bookMarkItemWidth) / 100;
    }

    public int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics());
    }
}
