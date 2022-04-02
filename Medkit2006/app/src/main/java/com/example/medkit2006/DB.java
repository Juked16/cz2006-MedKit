package com.example.medkit2006;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.BoardiesITSolutions.AndroidMySQLConnector.Connection;
import com.BoardiesITSolutions.AndroidMySQLConnector.Exceptions.InvalidSQLPacketException;
import com.BoardiesITSolutions.AndroidMySQLConnector.Exceptions.MySQLConnException;
import com.BoardiesITSolutions.AndroidMySQLConnector.Exceptions.MySQLException;
import com.BoardiesITSolutions.AndroidMySQLConnector.IConnectionInterface;
import com.BoardiesITSolutions.AndroidMySQLConnector.IResultInterface;
import com.BoardiesITSolutions.AndroidMySQLConnector.ResultSet;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.function.Consumer;

public class DB {

    static Connection conn;
    String lastMsg = "";

    public DB() {
        connect();
    }

    public void connect(){
        conn = new Connection("94.74.80.1", "test", "Blue!$!$!$", 3306, "db_medical_facilities", new IConnectionInterface() {
            @Override
            public void actionCompleted() {
                lastMsg = "OK";
            }

            @Override
            public void handleInvalidSQLPacketException(InvalidSQLPacketException ex) {
                lastMsg = ex.getMessage();
            }

            @Override
            public void handleMySQLException(MySQLException ex) {
                lastMsg = ex.getMessage();
            }

            @Override
            public void handleIOException(IOException ex) {
                lastMsg = ex.getMessage();
            }

            @Override
            public void handleMySQLConnException(MySQLConnException ex) {
                lastMsg = ex.getMessage();
            }

            @Override
            public void handleException(Exception exception) {
                lastMsg = exception.getMessage();
            }
        });
    }

    public void execute(@NotNull String statement, Runnable whenDone){
        conn.createStatement().execute(statement, new IConnectionInterface() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void actionCompleted() {
                try {
                    if (whenDone != null)
                        whenDone.run();
                }catch (Exception e){
                    lastMsg = e.getMessage();
                }
            }

            @Override
            public void handleInvalidSQLPacketException(InvalidSQLPacketException ex) {
                lastMsg = ex.getMessage();
            }

            @Override
            public void handleMySQLException(MySQLException ex) {
                lastMsg = ex.getMessage();
            }

            @Override
            public void handleIOException(IOException ex) {
                lastMsg = ex.getMessage();
            }

            @Override
            public void handleMySQLConnException(MySQLConnException ex) {
                lastMsg = ex.getMessage();
            }

            @Override
            public void handleException(Exception exception) {
                lastMsg = exception.getMessage();
            }
        });
    }

    public void executeQuery(@NotNull String query, Consumer<ResultSet> whenDone) {
        conn.createStatement().executeQuery(query, new IResultInterface() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void executionComplete(ResultSet resultSet) {
                try {
                    if (whenDone != null)
                        whenDone.accept(resultSet);
                }catch (Exception e){
                    lastMsg = e.getMessage();
                }
            }

            @Override
            public void handleInvalidSQLPacketException(InvalidSQLPacketException ex) {
                lastMsg = ex.getMessage();
            }

            @Override
            public void handleMySQLException(MySQLException ex) {
                lastMsg = ex.getMessage();
            }

            @Override
            public void handleIOException(IOException ex) {
                lastMsg = ex.getMessage();
            }

            @Override
            public void handleMySQLConnException(MySQLConnException ex) {
                lastMsg = ex.getMessage();
            }

            @Override
            public void handleException(Exception ex) {
                lastMsg = ex.getMessage();
            }
        });
    }
}
