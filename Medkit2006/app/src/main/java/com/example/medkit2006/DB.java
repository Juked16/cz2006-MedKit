package com.example.medkit2006;

import android.util.Log;

import androidx.annotation.NonNull;

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
        passwordSalt BINARY(32) NOT NULL,
        verified INT DEFAULT 0, -- #should be BIT but lib can't get BIT datatype
        firstName VARCHAR(45),
        lastName VARCHAR(45),
        gender ENUM("M","F","O"),
        bloodType ENUM("A+","A-","B+","B-","AB+","AB-","O+","O-"),
        dateOfBirth DATE
    );
    CREATE TABLE medical_facilities(
        name VARCHAR(45) PRIMARY KEY,
        type VARCHAR(45) NOT NULL,
        address VARCHAR(100) NOT NULL,
        contact VARCHAR(45) NOT NULL,
        latitude DECIMAL(11,7) NOT NULL,
        longitude DECIMAL(12,7) NOT NULL,
        description VARCHAR(1000) DEFAULT "" NOT NULL
    );
    CREATE TABLE post(
        _id INT AUTOINCREMENT PRIMARY KEY,
        title VARCHAR(150) NOT NULL,
        content VARCHAR(1000) NOT NULL,
        timestamp DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
        username VARCHAR(45) NOT NULL,
        medical_facility VARCHAR(45) NOT NULL,
        likes INTEGER NOT NULL,
        tags VARCHAR(100) NOT NULL,
        status INTEGER NOT NULL,
        report INTEGER NOT NULL
        FOREIGN KEY (username) REFERENCES account(username),
        FOREIGN KEY (medical_facility) REFERENCES medical_facilities(name)
    );
    CREATE TABLE bookmark (
        username VARCHAR(45) NOT NULL,
        medical_facility VARCHAR(45) NOT NULL,
        notes VARCHAR(1000) DEFAULT "",
        FOREIGN KEY (username) REFERENCES account(username),
        FOREIGN KEY (medical_facility) REFERENCES medical_facilities(name),
        CONSTRAINT user_mf PRIMARY KEY (username,medical_facility)
    );
    CREATE TABLE text (
        id INT AUTO_INCREMENT PRIMARY KEY,
        username VARCHAR(45) NOT NULL,
        content VARCHAR(1000) NOT NULL,
        timestamp DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
        postId INT CHECK (chatId = NULL),
        chatId INT CHECK (postId = NULL),
        FOREIGN KEY (username) REFERENCES account(username)
    );
    CREATE TABLE rating(
        username VARCHAR(45) NOT NULL,
        medical_facility VARCHAR(45) NOT NULL,
        rating INT NOT NULL CHECK(rating >= 0 AND rating <= 5),
        postId INT NOT NULL,
        FOREIGN KEY (username) REFERENCES account(username),
        FOREIGN KEY (medical_facility) REFERENCES medical_facilities(name),
        FOREIGN KEY (postId) REFERENCES post(_ID)
    );
    CREATE TABLE chat(
        id INT NOT NULL,
        username VARCHAR(45) NOT NULL,
        FOREIGN KEY (username) REFERENCES account(username),
        CONSTRAINT id_username PRIMARY KEY (id,username)
    );

    INSERT INTO medical_facilities VALUES ("Alexandra Hospital","hospital","378 ALEXANDRA ROAD ALEXANDRA HOSPITAL Singapore 159964","64722000",1.2865882,103.7990862,"");
    */

    public Connection conn;
    public static DB instance = null;

    public DB(Runnable onConnected, Consumer<Exception> onError) {
        connect(onConnected, onError);
        instance = this;
    }

    public void connect(Runnable onConnected, Consumer<Exception> onError) {
        Log.i("DB", "Connecting");
        conn = new Connection("94.74.80.1", "test", "Blue!$!$!$", 3306, "medkit", new DefaultResultInterface(onConnected, onError));
    }

    /**
     * @param statement Sql statement without result
     * @param whenDone  Called when done without error
     * @param onError   Called when error
     */
    public void execute(@NotNull String statement, Runnable whenDone, Consumer<Exception> onError) {
        if (!conn.getPlainSocket().isConnected())
            if (onError != null)
                onError.accept(new RuntimeException("No connection"));
        conn.createStatement().execute(statement, new DefaultResultInterface(onError) {
            @Override
            public void actionCompleted() {
                try {
                    if (whenDone != null)
                        whenDone.run();
                } catch (Exception e) {
                    Log.wtf("DB", e);
                }
            }
        });
    }

    /**
     * @param query    Select statement
     * @param whenDone Called when done without error
     */
    public void executeQuery(@NotNull String query, @NotNull Consumer<ResultSet> whenDone) {
        executeQuery(query, whenDone, null);
    }

    /**
     * @param query    Select statement
     * @param whenDone Called when done with no error
     * @param onError  Called when error
     */
    public void executeQuery(@NotNull String query, @NotNull Consumer<ResultSet> whenDone, Consumer<Exception> onError) {
        if (!conn.getPlainSocket().isConnected())
            if (onError != null)
                onError.accept(new RuntimeException("No connection"));
        conn.createStatement().executeQuery(query, new DefaultResultInterface(onError) {
            @Override
            public void executionComplete(ResultSet resultSet) {
                try {
                    whenDone.accept(resultSet);
                } catch (Exception e) {
                    Log.wtf("DB", e);
                }
            }
        });
    }

    /**
     * Escape string to avoid SQL injection attacks
     * @param str String to escape
     * @return Escaped string
     */
    @NonNull
    public static String escape(@NotNull String str){
        String escaped = instance.conn.escape_string(str);
        return escaped == null ? "" : escaped;
    }

    static class DefaultResultInterface implements IConnectionInterface, IResultInterface {

        private Runnable onConnected = null;
        private final Consumer<Exception> onError;

        public DefaultResultInterface(Runnable onConnected, Consumer<Exception> onError) {
            this.onConnected = onConnected;
            this.onError = onError;
        }

        public DefaultResultInterface(Consumer<Exception> onError) {
            this.onError = onError;
        }

        public void actionCompleted() {
            if (onConnected != null)
                onConnected.run();
            Log.i("DB", "Connect OK");
        }

        @Override
        public void executionComplete(ResultSet resultSet) {

        }

        @Override
        public void handleInvalidSQLPacketException(InvalidSQLPacketException ex) {
            if (onError != null)
                onError.accept(ex);
            Log.wtf("DB", ex);
        }

        @Override
        public void handleMySQLException(MySQLException ex) {
            if (onError != null)
                onError.accept(ex);
            Log.wtf("DB", ex);
        }

        @Override
        public void handleIOException(IOException ex) {
            if (onError != null)
                onError.accept(ex);
            Log.wtf("DB", ex);
        }

        @Override
        public void handleMySQLConnException(MySQLConnException ex) {
            if (onError != null)
                onError.accept(ex);
            Log.wtf("DB", ex);
        }

        @Override
        public void handleException(Exception exception) {
            if (onError != null)
                onError.accept(exception);
            Log.wtf("DB", exception);
        }
    }
}
