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
    CREATE TABLE account (
        username VARCHAR(45) PRIMARY KEY,
        email VARCHAR(45) NOT NULL UNIQUE,
        passwordHash BINARY(32) NOT NULL,
        verified BIT NOT NULL,
        firstName VARCHAR(45),
        lastName VARCHAR(45),
        gender VARCHAR(10),
        bloodType VARCHAR(3),
        dateOfBirth DATE
    );
    CREATE TABLE bookmark (
        username VARCHAR(45) NOT NULL,
        medical_facility VARCHAR(45) NOT NULL,
        notes VARCHAR(100)
    );
    CREATE TABLE text (
        id INT NOT NULL AUTO_INCREMENT KEY,
        username VARCHAR(45) NOT NULL,
        content VARCHAR(1000) NOT NULL,
        timestamp DATETIME NOT NULL
    );
    CREATE TABLE post(
        username VARCHAR(45) NOT NULL,
        content VARCHAR(1000) NOT NULL,
        timestamp DATETIME NOT NULL,
        comments VARCHAR(1000) NOT NULL -- #TODO: comma separated list of text ids?
    );
    CREATE TABLE medical_facilities(
        name VARCHAR(45) PRIMARY KEY,
        type VARCHAR(45) NOT NULL,
        address VARCHAR(100) NOT NULL,
        contact VARCHAR(45) NOT NULL
    );
    CREATE TABLE rating(
        username VARCHAR(45) NOT NULL,
        medical_facility VARCHAR(45) NOT NULL,
        rating INT NOT NULL
    );
    CREATE TABLE service(
        type VARCHAR(45) NOT NULL,
        price DOUBLE NOT NULL,
        description VARCHAR(1000) NOT NULL,
    );
    CREATE TABLE chat(
        users VARCHAR(1000) NOT NULL, -- #TODO: comma separated list of usernames?
        messages VARCHAR(1000) NOT NULL -- #TODO: comma separated list of text ids?
    )

    INSERT INTO medical_facilities VALUES ("Alexandra Hospital","hospital","378 ALEXANDRA ROAD ALEXANDRA HOSPITAL Singapore 159964","64722000");
    */

    Connection conn;
    String lastMsg;

    public DB() {
        connect();
    }

    public void connect(){
        lastMsg = "connecting";
        conn = new Connection("94.74.80.1", "test", "Blue!$!$!$", 3306, "medkit", new DefaultResultInterface());
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
