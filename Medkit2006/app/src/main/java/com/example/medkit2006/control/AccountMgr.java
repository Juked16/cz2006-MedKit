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
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.function.Consumer;

public class AccountMgr {

    private String verificationCode;
    private User loggedInUser;

    public boolean validateConfirmPassword(@NonNull String password, @NotNull String confirmPassword) {//TODO: remove?
        return password.equals(confirmPassword);
    }

    public boolean validEmail(@NonNull String email) {
        return email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }

    /**
     * @param email    Email
     * @param callback Called when no error
     * @param error    Called when error
     */
    public void emailExist(@NotNull String email, Consumer<Boolean> callback, Consumer<Exception> error) {
        DB.instance.executeQuery("select email from account where email = \"" + email + "\"", resultSet -> callback.accept(resultSet.getNextRow() != null), error);
    }

    /**
     * @param username Username
     * @param callback Called when no error
     * @param error    Called when error
     */
    public void usernameExist(@NotNull String username, Consumer<Boolean> callback, Consumer<Exception> error) {
        DB.instance.executeQuery("select username from account where username = \"" + username + "\"", resultSet -> callback.accept(resultSet.getNextRow() != null), error);
    }

    /**
     * @param username Username
     * @param email    Email
     * @param password Password (Not hashed)
     * @param callback Called when no error
     * @param error    Called when error
     */
    public void createAccount(@NotNull String username, String email, @NotNull String password, @NotNull Runnable callback, Consumer<Exception> error) {
        byte[] salt = salt();
        DB.instance.execute("insert into account (username,email,passwordHash,passwordSalt) values (\"" + username + "\",\"" + email + "\",\"" + hash(password, salt) + "\",\"" + bytesToHex(salt) + "\")", callback, error);
    }

    public void sendVerificationCode(@NotNull String email, @NotNull String verificationCode) {
        this.verificationCode = verificationCode;
        // TODO - implement email sending
    }

    public boolean isLoggedIn() {
        return loggedInUser != null;
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
     * @param username Username
     * @param password Password (Not hashed)
     * @param callback Called when no error
     * @param error    Called when error
     */
    public void validateAccount(@NotNull String username, @NotNull String password, Consumer<Boolean> callback, Consumer<Exception> error) {
        DB.instance.executeQuery("select passwordSalt from account where username = \"" + username + "\"", saltResult -> {
            MySQLRow row = saltResult.getNextRow();
            if (row != null) {
                byte[] salt;
                try {
                    salt = hexStringToByteArray(row.getString("passwordSalt"));
                } catch (SQLColumnNotFoundException e) {
                    callback.accept(false);
                    return;
                }
                DB.instance.executeQuery("select username from account where username = \"" + username + "\" and passwordHash = cast('" + hash(password, salt) + "' as BINARY(32))",
                        resultSet -> callback.accept(resultSet.getNextRow() != null), error);
            }
        }, error);
    }

    /**
     * @param username Username
     * @param callback Called when no error
     * @param error    Called when error
     */
    public void getUserDetails(@NotNull String username, @NotNull Consumer<User> callback, Consumer<Exception> error) {
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
                    user.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(dateOfBirth));
                }
            } catch (Exception e) {
                error.accept(e);
                return;
            }
            callback.accept(user);
        }, error);
    }

    /**
     * @param callback Called when no error
     * @param error    Called when error
     */
    public void saveLoggedInUserDetails(Runnable callback, Consumer<Exception> error) {
        if (loggedInUser == null) {
            error.accept(new Exception("User not logged in"));
        }
        Date dob = loggedInUser.getDateOfBirth();
        DB.instance.execute("update account set " +
                "email = \"" + loggedInUser.getEmail() + "\"," +
                "verified = " + (loggedInUser.getVerified() ? 1 : 0) + "," +
                "firstName = \"" + loggedInUser.getFirstName() + "\"," +
                "lastName = \"" + loggedInUser.getLastName() + "\"," +
                "gender = \"" + loggedInUser.getGender() + "\"," +
                "bloodType = \"" + loggedInUser.getBloodType() + "\"," +
                "dateOfBirth = " + (dob != null ? "\"" + new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(dob) + "\"" : "null") + " " +
                "where username = \"" + loggedInUser.getUsername() + "\"", callback, error
        );
    }

    /**
     * @param email    Email
     * @param password Password (Not hashed)
     * @param callback Called when no error
     * @param error    Called when error
     */
    public void updatePassword(@NotNull String email, @NotNull String password, Runnable callback, Consumer<Exception> error) {
        byte[] salt = salt();
        DB.instance.execute("update account set passwordHash = \"" + hash(password, salt) + "\", passwordSalt = \"" + bytesToHex(salt) + "\" where email = \"" + email + "\"", callback, error);
    }

    /**
     * @param s String to hash
     * @param salt Salt
     * @return SHA-256 salted hash
     */
    @NonNull
    private String hash(@NotNull String s, @NotNull byte[] salt) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt);
            return bytesToHex(digest.digest(s.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "Should never happen";
    }

    @NonNull
    private byte[] salt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    @NonNull
    private byte[] hexStringToByteArray(@NotNull String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
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