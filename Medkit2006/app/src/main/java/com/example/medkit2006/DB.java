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
        verified BIT DEFAULT 0,
        firstName VARCHAR(45),
        lastName VARCHAR(45),
        gender ENUM("M","F","O"),
        bloodType ENUM("A+","A-","B+","B-","AB+","AB-","O+","O-"),
        dateOfBirth DATE
    );
    CREATE TABLE medical_facilities(
        name VARCHAR(45) PRIMARY KEY,
        type VARCHAR(45) NOT NULL, -- #TODO: ENUM?
        address VARCHAR(100) NOT NULL,
        contact VARCHAR(45) NOT NULL
    );
    CREATE TABLE bookmark (
        username VARCHAR(45) NOT NULL,
        medical_facility VARCHAR(45) NOT NULL,
        notes VARCHAR(100) DEFAULT "",
        FOREIGN KEY (username) REFERENCES account(username),
        FOREIGN KEY (medical_facility) REFERENCES medical_facilities(name),
        CONSTRAINT user_mf PRIMARY KEY (username,medical_facility)
    );
    CREATE TABLE text (
        id INT AUTO_INCREMENT PRIMARY KEY,
        username VARCHAR(45) NOT NULL,
        content VARCHAR(1000) NOT NULL,
        timestamp DATETIME NOT NULL,
        postId INT CHECK (chatId = NULL),
        chatId INT CHECK (postId = NULL),
        FOREIGN KEY (username) REFERENCES account(username)
    );
    CREATE TABLE rating(
        username VARCHAR(45) NOT NULL,
        medical_facility VARCHAR(45) NOT NULL,
        rating INT NOT NULL CHECK(rating >= 1 AND rating <= 5),
        FOREIGN KEY (username) REFERENCES account(username),
        FOREIGN KEY (medical_facility) REFERENCES medical_facilities(name),
        CONSTRAINT user_mf PRIMARY KEY (username,medical_facility)
    );
    CREATE TABLE service(
        medical_facility VARCHAR(45) NOT NULL,
        type VARCHAR(45) NOT NULL, -- #TODO: ENUM?
        price DOUBLE NOT NULL CHECK (price > 0),
        description VARCHAR(1000) NOT NULL,
        FOREIGN KEY (medical_facility) REFERENCES medical_facilities(name)
    );
    CREATE TABLE chat(
        id INT NOT NULL,
        username VARCHAR(45) NOT NULL,
        FOREIGN KEY (username) REFERENCES account(username),
        CONSTRAINT id_username PRIMARY KEY (id,username)
    )

    INSERT INTO medical_facilities VALUES ("Alexandra Hospital","hospital","378 ALEXANDRA ROAD ALEXANDRA HOSPITAL Singapore 159964","64722000");
    */

    public Connection conn;
    public String lastMsg;
    public static DB instance = null;

    public DB() {
        connect();
        instance = this;
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

    public void executeQuery(@NotNull String query, @NotNull Consumer<ResultSet> whenDone) {
        conn.createStatement().executeQuery(query, new DefaultResultInterface() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void executionComplete(ResultSet resultSet) {
                try {
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
