package com.achiever.menschenfahren.admin.common;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Nonnull;

public class Helper {

    public static String convertDate(@Nonnull final Date date) {
        final String pattern = "yyyy-MM-dd HH:mm:ss";
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        final String dateString = simpleDateFormat.format(date);
        return dateString;
    }

}
