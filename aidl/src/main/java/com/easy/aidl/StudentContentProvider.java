package com.easy.aidl;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.easy.sql.DataBaseHelper;

public class StudentContentProvider extends ContentProvider {

    //这里的AUTHORITY就是我们在AndroidManifest.xml中配置的authorities
    public static final String AUTHORITY = "com.easy.studentProvider";
    public static final int USER_DIR = 0;
    public static final int USER_ITEM = 1;
    private static UriMatcher uriMatcher;

    static {
        //匹配不成功返回NO_MATCH(-1)
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        //添加我们需要匹配的uri
        uriMatcher.addURI(AUTHORITY, "student", USER_DIR);
        uriMatcher.addURI(AUTHORITY, "student/#", USER_ITEM);
    }

    private DataBaseHelper dataHelper;

    @Override
    public boolean onCreate() {
        dataHelper = new DataBaseHelper(getContext(), "easyDb", null, 1);
        return false;
    }

    //查询数据
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dataHelper.getWritableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case USER_DIR:
                //参数1：表名  其他参数可借鉴上面的介绍
                cursor = db.query(dataHelper.getTableName(), projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case USER_ITEM:
                String queryId = uri.getPathSegments().get(1);
                cursor = db.query(dataHelper.getTableName(), projection, "id=?", new String[]{queryId}, null, null, sortOrder);
                break;
            default:
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dataHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case USER_DIR:
            case USER_ITEM:
                //参数1：表名  参数2：没有赋值的设为空   参数3：插入值
                long newUserId = db.insert(dataHelper.getTableName(), null, values);
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //创建SQLiteDatabase对象
        SQLiteDatabase db = dataHelper.getWritableDatabase();
        int deleteInt = 0;
        //匹配uri
        switch (uriMatcher.match(uri)) {
            case USER_DIR:
                //参数1：表名   参数2：约束删除列的名字   参数3：具体行的值
                deleteInt = db.delete(dataHelper.getTableName(), selection, selectionArgs);
                break;
            case USER_ITEM:
                String deleteId = uri.getPathSegments().get(1);
                deleteInt = db.delete(dataHelper.getTableName(), "id=?", new String[]{deleteId});
                break;
            default:
        }
        return deleteInt;
    }

    //更新数据
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dataHelper.getWritableDatabase();
        int updateRow = 0;
        switch (uriMatcher.match(uri)) {
            case USER_DIR:
                updateRow = db.update(dataHelper.getTableName(), values, selection, selectionArgs);
                break;
            case USER_ITEM:
                String updateId = uri.getPathSegments().get(1);
                updateRow = db.update(dataHelper.getTableName(), values, "id=?", new String[]{updateId});
                break;
            default:
        }
        return updateRow;
    }
}
