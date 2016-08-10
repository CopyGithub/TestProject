package com.adolphin.common;

import com.robotium.solo.Solo;

import android.content.Context;
import android.database.Cursor;

public class SqliteUtil {
    final private com.android.common.SqliteUtil sqliteUtil;
    final private String selectSql = "select * from %s";
    final private String whereSql = " where %s = ?";
    final private String andSql = " and %s = ?";
    final private String downloadDb = "downloads.db";
    final private String downloadTable = "downloads";
    final private String field_hint = "hint";
    final private String field_status = "status";
    final private String field_title = "title";
    final private String field_visibility = "visibility";
    final private String field_current_bytes = "current_bytes";
    final private String field_total_bytes = "total_bytes";
    final private String bookmarkDb = "browser.db";
    final private String bookmarkTable = "bookmarks";
    final private String bookmark_field_isdeleted = "deleted";
    final private String bookmark_field_isfolder = "is_folder";
    final private String bookmark_field_folder = "folder";
    final private String bookmark_field_id = "_id";

    public SqliteUtil(Solo solo) {
        Context context = solo.getInstrumentation().getTargetContext();
        sqliteUtil = new com.android.common.SqliteUtil(context);
    }

    /**
     * 返回存在的download任务数量
     * 
     * @return
     */
    public int getDownloadTaskCount() {
        String sql = String.format(selectSql, downloadTable);
        Cursor cursorDownloadTask = sqliteUtil.getAllCursor(downloadDb, sql, new String[] {});
        return cursorDownloadTask.getCount();
    }

    /**
     * 返回指定文件名的下载状态
     * <p>
     * 这里取最后一个匹配的文件名
     * </p>
     * 
     * @param fileName
     * @return
     */
    public int getDownloadStatus(String fileName) {
        int status = 0;
        // 使用于普通下载任务
        String sqlhint = String.format(selectSql, downloadTable)
                + String.format(whereSql, field_hint);
        Cursor cursorhint = sqliteUtil.getAllCursor(downloadDb, sqlhint, new String[] { fileName });
        while (cursorhint.moveToNext()) {
            status = cursorhint.getInt(cursorhint.getColumnIndex(field_status));
        }
        cursorhint.close();
        if (status != 0) {
            return status;
        }
        // 适用于壁纸的下载
        String sqltitle = String.format(selectSql, downloadTable)
                + String.format(whereSql, field_title);
        Cursor cursortitle = sqliteUtil.getAllCursor("downloads.db", sqltitle,
                new String[] { fileName });
        while (cursortitle.moveToNext()) {
            status = cursortitle.getInt(cursortitle.getColumnIndex("status"));
        }
        cursortitle.close();
        if (status != 0) {
            return status;
        }
        return 0;
    }

    /**
     * 返回指定文件名的任务是否下载成功
     * 
     * @param fileName
     * @return
     */
    public boolean isDownloadComplete(String fileName) {
        int status = getDownloadStatus(fileName);
        if (status == 200) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 返回指定文件名任务当前和总的下载的byte数,0为当前,1为总数
     * 
     * @param fileName
     * @return
     */
    public long[] getCurrentDownloadByte(String fileName) {
        long[] downloadBytes = new long[] { 0, 0 };
        // 适用于普通下载任务
        String sqlHint = String.format(selectSql, downloadTable)
                + String.format(whereSql, field_hint) + String.format(andSql, field_visibility);
        Cursor cursorHint = sqliteUtil.getAllCursor(downloadDb, sqlHint, new String[] { fileName,
                "1" });

        if (cursorHint.getCount() > 0) {
            while (cursorHint.moveToNext()) {
                downloadBytes[0] = cursorHint
                        .getInt(cursorHint.getColumnIndex(field_current_bytes));
                downloadBytes[1] = cursorHint.getInt(cursorHint.getColumnIndex(field_total_bytes));
            }
            cursorHint.close();
            return downloadBytes;
        }
        // 适用于壁纸的下载
        String sqlTitle = String.format(selectSql, downloadTable)
                + String.format(whereSql, field_title) + String.format(andSql, field_visibility);
        Cursor cursorTitle = sqliteUtil.getAllCursor(downloadTable, sqlTitle, new String[] {
                fileName, "1" });
        if (cursorTitle.getCount() > 0) {
            while (cursorTitle.moveToNext()) {
                downloadBytes[0] = cursorTitle.getInt(cursorTitle
                        .getColumnIndex(field_current_bytes));
                downloadBytes[1] = cursorTitle
                        .getInt(cursorTitle.getColumnIndex(field_total_bytes));
            }
            cursorTitle.close();
            return downloadBytes;
        }
        return downloadBytes;
    }

    public boolean isFolderDeleted(String title) {
        String sqlTitle = String.format(selectSql, bookmarkTable)
                + String.format(whereSql, field_title)
                + String.format(andSql, bookmark_field_isfolder);
        Cursor cursorTitle = sqliteUtil.getAllCursor(bookmarkDb, sqlTitle, new String[] { title,
                "1" });
        if (cursorTitle.getCount() > 0) {
            while (cursorTitle.moveToNext()) {
                int deleted = cursorTitle.getInt(cursorTitle
                        .getColumnIndex(bookmark_field_isdeleted));
                if (deleted == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isBookmarkDeleted(String bookmark, String folder) {
        int folderId = 0;
        if (folder == null) {
            folderId = 0;
        } else {
            String sqlTitle = String.format(selectSql, bookmarkTable)
                    + String.format(whereSql, field_title)
                    + String.format(andSql, bookmark_field_isfolder);
            Cursor cursorTitle = sqliteUtil.getAllCursor(bookmarkDb, sqlTitle, new String[] {
                    folder, "1" });
            if (cursorTitle.getCount() > 0) {
                while (cursorTitle.moveToNext()) {
                    folderId = cursorTitle.getInt(cursorTitle.getColumnIndex(bookmark_field_id));
                }
            }
        }
        String sqlBookmark = String.format(selectSql, bookmarkTable)
                + String.format(whereSql, field_title)
                + String.format(andSql, bookmark_field_isfolder)
                + String.format(andSql, bookmark_field_folder);
        Cursor cursorBookmark = sqliteUtil.getAllCursor(bookmarkDb, sqlBookmark, new String[] {
                bookmark, "0", String.valueOf(folderId) });
        if (cursorBookmark.getCount() > 0) {
            while (cursorBookmark.moveToNext()) {
                int deleted = cursorBookmark.getInt(cursorBookmark
                        .getColumnIndex(bookmark_field_isdeleted));
                if (deleted == 0) {
                    return false;
                }
            }
        }
        return true;
    }
}
