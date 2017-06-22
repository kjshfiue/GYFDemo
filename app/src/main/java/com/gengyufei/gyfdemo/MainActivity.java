package com.gengyufei.gyfdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gengyufei.gyfdemo.adapter.ListAdapter;
import com.gengyufei.gyfdemo.db.DBManager;
import com.gengyufei.gyfdemo.entity.NoteBean;
import com.gengyufei.gyfdemo.ui.LoginActivity;
import com.gengyufei.gyfdemo.ui.NoteActivity;
import com.gengyufei.gyfdemo.utils.UserHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity {
    private ListView mListView;
    private ListAdapter mAdapter;
    private DBManager dbManager;

    private DrawerLayout drawer;
    private TextView tv_title;
    private TextView tv_nick;
    private TextView tv_nodata;
    private TextView tv_change;

    private String mType;
    private String mKeyword;
    private long mMillis;
    private List<String> typeList;

    private ImageView iv_head;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UserHelper.getInstance().init(mActivity);
        dbManager = DBManager.getInstance(this);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_nick = (TextView) findViewById(R.id.tv_nick);
        tv_nodata = (TextView) findViewById(R.id.tv_nodata);
        tv_change = (TextView) findViewById(R.id.tv_change);

        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setEmptyView(tv_nodata);
        mAdapter = new ListAdapter(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, NoteActivity.class);
                intent.putExtra("note", mAdapter.getItem(position));
                startActivityForResult(intent, 2);
            }
        });

        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(mActivity, NoteActivity.class), 1);
            }
        });
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        ImageView iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawer.isDrawerOpen(Gravity.LEFT)) {
                    drawer.openDrawer(Gravity.LEFT);
                }
            }
        });

        String[] typeArray = getResources().getStringArray(R.array.typeArray);
        typeList = new ArrayList<>(Arrays.asList(typeArray));
        typeList.add(0, "全部");
        ListView drawer_list = (ListView) findViewById(R.id.drawer_list);
        drawer_list.setSelection(0);
        drawer_list.setAdapter(new ArrayAdapter<>(mActivity, R.layout.item_drawer_list, typeList));
        drawer_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mType = typeList.get(position);
                if ("全部".equals(mType)) {
                    mType = "";
                }
                drawer.closeDrawers();
                loadData();

                tv_title.setText("我的" + mType + "日记");
                tv_nodata.setText("还没有添加" + mType + "日记哦~~");
            }
        });


        iv_head = (ImageView) findViewById(R.id.iv_head);

        tv_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, LoginActivity.class);
                startActivity(intent);
                drawer.closeDrawers();
            }
        });


        if (UserHelper.getInstance().isLoggedIn()) {
            loadData();
        } else {
            startActivity(new Intent(mActivity, LoginActivity.class));
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode > 0) {
            if (UserHelper.getInstance().isLoggedIn()) {
                loadData();
            } else {
                startActivity(new Intent(mActivity, LoginActivity.class));
                finish();
            }
        }
    }

    private void loadData() {
        List<NoteBean> data = dbManager.loadNoteList(mType, mKeyword, mMillis);
        mAdapter.refresh(data);
        tv_nick.setText(UserHelper.getInstance().getNickName());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbManager.closeDB();
    }
}
