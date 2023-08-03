package com.gpay.merchantapp.utils;

import android.text.TextUtils;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class StrengthPassword {
    final String pattern1 = "(\\w)\\1+";
    final String pattern2 = "/(.*)";


    public boolean pregMatch(String pattern, String content) {
        return content.matches(pattern);
    }

    public int numUnique(String[] list) {
        Set<String> hashSet = new HashSet<>();

        for (String d : list)
            hashSet.add(d);

        return hashSet.size();
    }

    public HashMap<Integer, String> passwordMeter(String password) {
        HashMap<Integer, String> result = new HashMap<>();
        int strength = 0;
        if (!TextUtils.isDigitsOnly(password)) {
            result.put(0, "Password must in numbers");
            result.put(1, ""+strength);
            return result;
        }

        if (password.length() != 6) {
            result.put(0, "Password must in 6 characters");
            result.put(1, ""+strength);
            return result;
        } else {
            strength += 1;
        }

        if (pregMatch(pattern1, password)) {
            result.put(0, "Repeated 6 chars not allowed");
            result.put(1, ""+strength);
            return result;
        } else {
            strength += 1;
        }


        if ("01234567890".contains(password)) {
            result.put(0, "sequential number not allowed");
            result.put(1, ""+strength);
            return result;
        } else if ("09876543210".contains(password)) {
            result.put(0, "sequential number not allowed");
            result.put(1, ""+strength);
            return result;
        }   else {
            strength += 2;
        }

        if (!pregMatch("(\\w)\\1{1}", password)) {
            strength += 1;
        }

        if (!pregMatch("(\\w)\\1{2}", password)) {
            strength += 1;
        }

        String[] data = new String[password.length()];
        for (int i = 0; i < data.length; i++) {
            data[i] = password.substring(i);
        }

        if (password.length() == numUnique(data)) {
            strength += 1;
        }

        if (strength == 7) {
            result.put(0, "Your password is very strong");
            result.put(1, ""+strength);
        } else if (strength == 6) {
            result.put(0, "Your password is strong");
            result.put(1, ""+strength);
        } else if (strength == 5) {
            result.put(0, "Your password is good");
            result.put(1, ""+strength);
        } else {
            result.put(0, "Your password is weak");
            result.put(1, ""+strength);
        }


        return result;
    }
}
