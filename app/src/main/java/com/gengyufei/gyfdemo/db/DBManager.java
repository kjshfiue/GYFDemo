package com.gengyufei.gyfdemo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.gengyufei.gyfdemo.entity.NoteBean;
import com.gengyufei.gyfdemo.entity.User;
import com.gengyufei.gyfdemo.utils.UserHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by FineFan on 2017/6/12.
 */

public class DBManager {

    private DBHelper helper;
    private SQLiteDatabase db;

    private volatile static DBManager instance;

    private DBManager() {
    }

    public static DBManager getInstance(Context context) {
        if (instance == null) {
            synchronized (DBManager.class) {
                if (instance == null) {
                    instance = new DBManager();
                    Context sContext = context.getApplicationContext();
                    instance.helper = new DBHelper(sContext);
                    instance.db = instance.helper.getWritableDatabase();
                }
            }
        }
        return instance;
    }


    private DBManager(Context context) {
        helper = new DBHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }


    /**
     * update noteBean
     *
     * @param noteBean
     */
    public void updateNote(NoteBean noteBean) {
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.NOTE_CONTENT, noteBean.content);
        cv.put(DBHelper.NOTE_UPDATE_TIME, System.currentTimeMillis());
        cv.put(DBHelper.NOTE_TYPE, noteBean.type);
        db.update(DBHelper.TABLE_NOTE, cv, "_ID = ?", new String[]{String.valueOf(noteBean.id)});
    }

    public void insertNote(NoteBean noteBean) {
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.NOTE_CONTENT, noteBean.content);
        cv.put(DBHelper.NOTE_USER_ID, UserHelper.getInstance().getUserId());
        cv.put(DBHelper.NOTE_CREATE_TIME, System.currentTimeMillis());
        cv.put(DBHelper.NOTE_UPDATE_TIME, System.currentTimeMillis());
        cv.put(DBHelper.NOTE_TYPE, noteBean.type);

        //插入ContentValues中的数据
        long rowId = db.insert(DBHelper.TABLE_NOTE, null, cv);
        noteBean.id = (int) rowId;
    }


    /**
     * delete Note
     *
     * @param id
     */
    public void deleteNoteById(int id) {
        db.delete(DBHelper.TABLE_NOTE, "_ID = ?", new String[]{String.valueOf(id)});
    }


    /**
     * query all persons, return list
     *
     * @return List<Person>
     */
    public List<NoteBean> loadNoteList(String type, String keyword, long time) {
        String sql = "SELECT * FROM " + DBHelper.TABLE_NOTE + " WHERE USER_ID = ? ";
        ArrayList<String> args = new ArrayList<>();
        args.add(String.valueOf(UserHelper.getInstance().getUserId()));

        if (!TextUtils.isEmpty(type)) {
            sql += " AND TYPE LIKE ? ";
            args.add("%" + type + "%");
        }
        if (!TextUtils.isEmpty(keyword)) {
            sql += " AND CONTENT LIKE ? ";
            args.add("%" + keyword + "%");
        }
        if (time != 0) {
            sql += " AND UPDATE_TIME > ? AND UPDATE_TIME < ? ";

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            long millis = calendar.getTimeInMillis();
            args.add(String.valueOf(millis));
            args.add(String.valueOf(millis + 24 * 60 * 60 * 1000L));
        }

        ArrayList<NoteBean> noteList = new ArrayList<>();
        Cursor c = db.rawQuery(sql, args.toArray(new String[args.size()]));

        while (c.moveToNext()) {
            NoteBean note = new NoteBean();
            note.id = c.getInt(c.getColumnIndex(DBHelper.NOTE_ID));
            note.content = c.getString(c.getColumnIndex(DBHelper.NOTE_CONTENT));
            note.type = c.getString(c.getColumnIndex(DBHelper.NOTE_TYPE));
            note.userId = c.getInt(c.getColumnIndex(DBHelper.NOTE_USER_ID));
            note.updateTime = c.getLong(c.getColumnIndex(DBHelper.NOTE_UPDATE_TIME));
            note.createTime = c.getLong(c.getColumnIndex(DBHelper.NOTE_CREATE_TIME));
            noteList.add(note);
        }
        c.close();
        return noteList;
    }


    public User loadUser(String token, int id) {
        String sql = "SELECT * FROM " + DBHelper.TABLE_USER + " WHERE _ID = ? AND TOKEN = ?";
        Cursor c = db.rawQuery(sql, new String[]{String.valueOf(id), token});

        if (c.moveToNext()) {
            User user = new User();
            user.setId(c.getInt(c.getColumnIndex(DBHelper.USER_ID)));
            user.setToken(c.getString(c.getColumnIndex(DBHelper.USER_TOKEN)));
            user.setName(c.getString(c.getColumnIndex(DBHelper.USER_NAME)));
            user.setNickName(c.getString(c.getColumnIndex(DBHelper.USER_NICKNAME)));
            c.close();
            return user;
        }
        c.close();
        return null;
    }

    public User loadUser(String name, String pwd) {
        String sql = "SELECT * FROM " + DBHelper.TABLE_USER + " WHERE NAME = ? AND PASSWORD = ?";
        Cursor c = db.rawQuery(sql, new String[]{name, pwd});

        if (c.moveToNext()) {
            User user = new User();
            user.setId(c.getInt(c.getColumnIndex(DBHelper.USER_ID)));
            user.setToken(c.getString(c.getColumnIndex(DBHelper.USER_TOKEN)));
            user.setName(c.getString(c.getColumnIndex(DBHelper.USER_NAME)));
            user.setNickName(c.getString(c.getColumnIndex(DBHelper.USER_NICKNAME)));
            c.close();
            return user;
        }
        c.close();
        return null;
    }


    public int insertUser(User user) {
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.USER_NAME, user.getName());
        cv.put(DBHelper.USER_PASSWORD, user.getPassword());
        cv.put(DBHelper.USER_TOKEN, user.getToken());
        cv.put(DBHelper.USER_NICKNAME, user.getNickName());

        //插入ContentValues中的数据
        long rowId = db.insert(DBHelper.TABLE_USER, null, cv);
        user.setId((int) rowId);
        return user.getId();
    }




    /**
     * close database
     */
    public void closeDB() {
        db.close();
        instance = null;
    }

}