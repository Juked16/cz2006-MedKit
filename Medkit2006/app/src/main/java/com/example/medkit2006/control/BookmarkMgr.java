package com.example.medkit2006.control;

import com.BoardiesITSolutions.AndroidMySQLConnector.Exceptions.SQLColumnNotFoundException;
import com.BoardiesITSolutions.AndroidMySQLConnector.MySQLRow;
import com.example.medkit2006.DB;
import com.example.medkit2006.MainActivity;
import com.example.medkit2006.entity.Bookmark;
import com.example.medkit2006.entity.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.Consumer;

public class BookmarkMgr {

    public void get(@NotNull String medicalFacilityName, @NotNull Consumer<Bookmark> bookmarks, Consumer<Exception> error) {
        User user = MainActivity.accountMgr.getLoggedInUser();
        DB.instance.executeQuery("select * from bookmark where username = '" + DB.escape(user.getUsername()) + "' and medical_facility = '" + DB.escape(medicalFacilityName) + "'", resultSet -> {
            try {
                MySQLRow row = resultSet.getNextRow();
                if (row != null)
                    bookmarks.accept(new Bookmark(row.getString("medical_facility"), row.getString("notes")));
                else
                    bookmarks.accept(null);
            } catch (Exception e) {
                error.accept(e);
            }
        }, error);
    }

    public void getAll(Consumer<ArrayList<Bookmark>> bookmarks, Consumer<Exception> error) {
        User user = MainActivity.accountMgr.getLoggedInUser();
        DB.instance.executeQuery("select * from bookmark where username = '" + DB.escape(user.getUsername()) + "'", resultSet -> {
            ArrayList<Bookmark> list = new ArrayList<>();
            MySQLRow row;
            try {
                while ((row = resultSet.getNextRow()) != null) {
                    list.add(new Bookmark(row.getString("medical_facility"), row.getString("notes")));
                }
            } catch (SQLColumnNotFoundException e) {
                error.accept(e);
                return;
            }
            bookmarks.accept(list);
        }, error);
    }

    public void add(@NotNull Bookmark bookmark, Runnable callback, Consumer<Exception> error) {
        User user = MainActivity.accountMgr.getLoggedInUser();
        DB.instance.execute("insert into bookmark values ('" + DB.escape(user.getUsername()) + "','" + DB.escape(bookmark.getFacilityName()) + "','" + DB.escape(bookmark.getNotes()) + "')", callback, error);
    }

    public void updateNotes(@NotNull Bookmark bookmark, Runnable callback, Consumer<Exception> error) {
        User user = MainActivity.accountMgr.getLoggedInUser();
        DB.instance.execute("update bookmark set notes = '" + DB.escape(bookmark.getNotes()) + "' where username = '" + DB.escape(user.getUsername()) + "' and medical_facility = '" + DB.escape(bookmark.getFacilityName()) + "'", callback, error);
    }

    public void remove(@NotNull Bookmark bookmark, Runnable callback, Consumer<Exception> error) {
        User user = MainActivity.accountMgr.getLoggedInUser();
        DB.instance.execute("delete from bookmark where username = '" + DB.escape(user.getUsername()) + "' and medical_facility = '" + DB.escape(bookmark.getFacilityName()) + "'", callback, error);
    }
}
