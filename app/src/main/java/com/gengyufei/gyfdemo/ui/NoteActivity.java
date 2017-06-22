package com.gengyufei.gyfdemo.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.gengyufei.gyfdemo.BaseActivity;
import com.gengyufei.gyfdemo.R;
import com.gengyufei.gyfdemo.db.DBManager;
import com.gengyufei.gyfdemo.entity.NoteBean;
import com.gengyufei.gyfdemo.utils.Util;

import java.lang.ref.WeakReference;

public class NoteActivity extends BaseActivity {
    private EditText et_note;
    private Spinner spinner;
    private NoteBean mNoteBean;


    private boolean contentChanged = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        if (getIntent() != null) {
            mNoteBean = (NoteBean) getIntent().getSerializableExtra("note");
        }

        if (mNoteBean == null) {
            mNoteBean = new NoteBean();
            mNoteBean.type = "个人";
        }
        et_note = (EditText) findViewById(R.id.et_note);
        TextView tv_time = (TextView) findViewById(R.id.tv_time);

        if (mNoteBean.id == 0) {
            //新建日记
            ((TextView) findViewById(R.id.tv_title)).setText("创建日记");
            tv_time.setText(Util.formatDate(System.currentTimeMillis()));
        } else {
            //修改日记
            ((TextView) findViewById(R.id.tv_title)).setText("日记");
            tv_time.setText(Util.formatDate(mNoteBean.updateTime));
        }

        et_note.setText(mNoteBean.content);
        et_note.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().trim().equals(mNoteBean.content)) {
                    mNoteBean.content = s.toString().trim();
                    mHandler.sendEmptyMessageDelayed(0, 1000);
                }
            }
        });
        final String[] typeArray = getResources().getStringArray(R.array.typeArray);
        spinner = (Spinner) findViewById(R.id.type_spinner);
        int position = 0;
        if (!TextUtils.isEmpty(mNoteBean.type)) {
            for (int i = 0; i < typeArray.length; i++) {
                if (typeArray[i].equals(mNoteBean.type)) {
                    position = i;
                    break;
                }
            }
        }

        spinner.setSelection(position);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!TextUtils.isEmpty(mNoteBean.type) && !mNoteBean.type.equals(typeArray[position])) {
                    mHandler.sendEmptyMessage(0);
                }
                mNoteBean.type = typeArray[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mActivity)
                        .setTitle("温馨提示")
                        .setMessage("真的要删除该日记吗？")
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                            }
                        })
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteNote();
                            }
                        })
                        .create()
                        .show();
            }
        });
    }


    private MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        WeakReference<NoteActivity> weak;

        MyHandler(NoteActivity object) {
            weak = new WeakReference<>(object);
        }

        @Override
        public void handleMessage(Message msg) {
            NoteActivity bean = weak.get();
            switch (msg.what) {
                case 0:
                    bean.saveNote();
                    break;
            }
        }
    }

    private void deleteNote() {
        if (mNoteBean.id != 0) {
            DBManager.getInstance(this).deleteNoteById(mNoteBean.id);
            toast("删除成功");
        } else {
            toast("还没有记录日记");
        }
    }

    private void saveNote() {
        if (mNoteBean.id == 0) {
            DBManager.getInstance(this).insertNote(mNoteBean);
        } else {
            DBManager.getInstance(this).updateNote(mNoteBean);
        }
        contentChanged = true;
    }

    @Override
    public void onBack(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (contentChanged) {
            setResult(RESULT_OK);
        }
        super.onBackPressed();
    }
}
