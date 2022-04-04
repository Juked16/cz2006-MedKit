package com.example.medkit2006.control;

import androidx.annotation.NonNull;

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

    private String verificationCode;
    private final HashMap<String, User> users = new HashMap<>();
    private User loggedInUser;

    public boolean validateConfirmPassword(@NonNull String password, @NotNull String confirmPassword) {//TODO: remove?
        return password.equals(confirmPassword);
    }

    public boolean validEmail(@NonNull String email) {
        return email.matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
    }

    /**
     *
     * @param email Email
     * @param callback Called when no error
     * @param error Called when error
     */
    public void emailExist(@NotNull String email, Consumer<Boolean> callback, Consumer<Exception> error) {
        DB.instance.executeQuery("select email from account where email = \"" + email + "\"", resultSet -> callback.accept(resultSet.getNextRow() != null), error);
    }

    /**
     *
     * @param username Username
     * @param callback Called when no error
     * @param error Called when error
     */
    public void usernameExist(@NotNull String username, Consumer<Boolean> callback, Consumer<Exception> error) {
        DB.instance.executeQuery("select username from account where username = \"" + username + "\"", resultSet -> callback.accept(resultSet.getNextRow() != null), error);
    }

    /**
     *
     * @param username Username
     * @param email Email
     * @param password Password (Not hashed)
     * @param callback Called when no error
     * @param error Called when error
     */
    public void createAccount(@NotNull String username, String email, @NotNull String password, @NotNull Runnable callback, Consumer<Exception> error) {
        DB.instance.execute("insert into account (username,email,passwordHash) values (\"" + username + "\",\"" + email + "\",\"" + hash(password) + "\")", callback, error);
    }

    public void sendVerificationCode(@NotNull String email, @NotNull String verificationCode) {
        this.verificationCode = verificationCode;
        // TODO - implement email sending
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public boolean validateVerificationCode(@NotNull String verificationCode) {
        return verificationCode.equals(this.verificationCode);
    }

    public boolean isAccountVerified(@NonNull User user) {//TODO: remove?
        return user.getVerified();
    }

    /**
     *
     * @param username Username
     * @param password Password (Not hashed)
     * @param callback Called when no error
     * @param error Called when error
     */
    public void validateAccount(@NotNull String username, @NotNull String password, Consumer<Boolean> callback, Consumer<Exception> error) {
        DB.instance.executeQuery("select username from account where username = \"" + username + "\" and passwordHash = cast('" + hash(password) + "' as BINARY(32))", resultSet -> callback.accept(resultSet.getNextRow() != null), error);
    }

    /**
     *
     * @param username Username
     * @param callback Called when no error
     * @param error Called when error
     */
    public void getUserDetails(@NotNull String username, @NotNull Consumer<User> callback, Consumer<Exception> error) {
        User tmp = users.get(username);
        if (tmp != null) {
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
            users.put(username, user);
            callback.accept(user);
        }, error);
    }

    /**
     *
     * @param s String to hash
     * @return SHA-256 hash
     */
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