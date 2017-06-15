package behdad.samplebookmarkseekbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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

    private int pageCount;
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
                printBookMArk();
                break;
        }
    }

    private void setTotalPage() {
        if (etTotalPage.getText().toString().isEmpty()) {
            return;
        }
        pageCount = Integer.parseInt(etTotalPage.getText().toString());
    }

    private void addBookMark() {
        if (etInput.getText().toString().isEmpty()) {
            return;
        }

        int page = Integer.valueOf(etInput.getText().toString());

        if (page > pageCount || page <= 0) {
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
}
