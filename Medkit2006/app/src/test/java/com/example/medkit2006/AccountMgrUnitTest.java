package com.example.medkit2006;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.medkit2006.control.AccountMgr;
import com.example.medkit2006.entity.User;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class AccountMgrUnitTest {

    AccountMgr mgr = new AccountMgr();

    @Test
    public void samePassword() {
        assertTrue(mgr.validateConfirmPassword("P@ssw0rd", "P@ssw0rd"));
    }

    @Test
    public void differentPassword(){
        assertFalse(mgr.validateConfirmPassword("P@ssword","Nope"));
    }

    @Test
    public void validEmail(){
        assertTrue(mgr.validEmail("test@gmail.com"));
        assertTrue(mgr.validEmail("someEmail@yahoo.com.sg"));
    }

    @Test
    public void invalidEmail(){
        assertFalse(mgr.validEmail("test"));
        assertFalse(mgr.validEmail("test@"));
        assertFalse(mgr.validEmail("test@gmail"));
        assertFalse(mgr.validEmail("test@gmail.c"));
        assertFalse(mgr.validEmail("@gmail.c"));
    }

    @Test
    public void isLoggedIn(){
        assertFalse(mgr.isLoggedIn());
        mgr.setLoggedInUser(new User("test"));
        assertTrue(mgr.isLoggedIn());
    }

    @Test
    public void isUserVerified(){
        User user = new User("test");
        user.setVerified(false);
        assertFalse(mgr.isAccountVerified(user));
        user.setVerified(true);
        assertTrue(mgr.isAccountVerified(user));
    }
}