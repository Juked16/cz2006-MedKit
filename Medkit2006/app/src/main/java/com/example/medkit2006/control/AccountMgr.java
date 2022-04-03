package com.example.medkit2006.control;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.BoardiesITSolutions.AndroidMySQLConnector.Exceptions.SQLColumnNotFoundException;
import com.BoardiesITSolutions.AndroidMySQLConnector.MySQLRow;
import com.example.medkit2006.DB;
import com.example.medkit2006.entity.User;

import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Consumer;

public class AccountMgr {

    private String verficationCode;
    private final HashMap<String, User> users = new HashMap<>();
    private User loggedInUser;

    public boolean validateConfirmPassword(@NonNull String password, @NotNull String confirmPassword) {//TODO: remove?
        return password.equals(confirmPassword);
    }

    public boolean validEmail(@NonNull String email) {
        return email.matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
    }

    public void emailExist(@NotNull String email, Consumer<Boolean> callback) {
        DB.instance.executeQuery("select email from account where email = \"" + email + "\"", resultSet -> callback.accept(resultSet.getNextRow() != null));
    }

    public void usernameExist(@NotNull String username, Consumer<Boolean> callback){
        DB.instance.executeQuery("select username from account where username = \""+username+"\"",resultSet -> callback.accept(resultSet.getNextRow() != null));
    }

    public void createAccount(@NotNull String username, String email, @NotNull String password, @NotNull Runnable callback){
        DB.instance.execute("insert into account (username,email,passwordHash) values (\""+username+"\",\""+email+"\",\""+hash(password)+"\")", callback);
    }

    public void sendVerificationCode(@NotNull String email,@NotNull String verificationCode) {
        this.verficationCode = verificationCode;
        // TODO - implement email sending
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public boolean validateVerificationCode(@NotNull String verificationCode) {
        return verificationCode.equals(this.verficationCode);
    }

    public boolean isAccountVerified(@NonNull User user) {//TODO: remove?
        return user.getVerified();
    }

    public void validateAccount(@NotNull String username, @NotNull String password, Consumer<Boolean> callback) {
        DB.instance.executeQuery("select username from account where username = \"" + username + "\" and passwordHash = cast('" + hash(password) + "' as BINARY(32))", resultSet -> callback.accept(resultSet.getNextRow() != null));
    }

    public void getUserDetails(@NotNull String username, @NotNull Consumer<User> callback) {
        User tmp = users.get(username);
        if(tmp != null){
            callback.accept(tmp);
            return;
        }
        DB.instance.executeQuery("select * from account where username = \"" + username + "\"", resultSet -> {
            User user = new User(username);
            MySQLRow row = resultSet.getNextRow();
            try {
                user.setEmail(row.getString("email"));
                user.setVerified(row.getInt("verified") != 0);
                //below all nullable
                user.setFirstName(row.getString("firstName"));
                user.setLastName(row.getString("lastName"));
                user.setGender(row.getString("gender"));
                user.setBloodType(row.getString("bloodType"));
                String dateOfBirth = row.getString("dateOfBirth");
                if (dateOfBirth != null) {
                    user.setDateOfBirth(Date.from(Instant.parse(dateOfBirth)));
                }
            } catch (SQLColumnNotFoundException ignored) {

            }
            users.put(username,user);
            callback.accept(user);
        });
    }

    public String hash(@NotNull String s) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            return bytesToHex(digest.digest(s.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "Should never happen";
    }

    @NonNull
    private static String bytesToHex(@NotNull byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}