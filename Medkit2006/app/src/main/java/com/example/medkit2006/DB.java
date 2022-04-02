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

    /*
    CREATE TABLE medical_facilities(name VARCHAR(45) PRIMARY KEY,type VARCHAR(45),address VARCHAR(100), contact VARCHAR(45));
    INSERT INTO medical_facilities VALUES ("Alexandra Hospital","hospital","378 ALEXANDRA ROAD ALEXANDRA HOSPITAL Singapore 159964","64722000")
    */

    Connection conn;
    String lastMsg;

    public DB() {
        connect();
    }

    public void connect(){
        lastMsg = "connecting";
        conn = new Connection("94.74.80.1", "test", "Blue!$!$!$", 3306, "db_medical_facilities", new DefaultResultInterface());
    }

    public void execute(@NotNull String statement, Runnable whenDone){
        conn.createStatement().execute(statement, new DefaultResultInterface() {
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
        });
    }

    public void executeQuery(@NotNull String query, Consumer<ResultSet> whenDone) {
        conn.createStatement().executeQuery(query, new DefaultResultInterface() {
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
        });
    }

    class DefaultResultInterface implements IConnectionInterface,IResultInterface{
        public void actionCompleted() {
            lastMsg = "Connect OK";
        }

        @Override
        public void executionComplete(ResultSet resultSet) {
            lastMsg = "Result OK";
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
    }
}
