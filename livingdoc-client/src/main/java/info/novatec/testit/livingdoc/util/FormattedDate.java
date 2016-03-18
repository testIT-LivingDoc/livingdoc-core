package info.novatec.testit.livingdoc.util;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.StringUtils;


public class FormattedDate {
    private Timestamp date;

    public FormattedDate(Timestamp date) {
        this.date = date;
    }

    public FormattedDate(Date date) {
        if (date != null) {
            this.date = new Timestamp(date.getTime());
        }
    }

    public FormattedDate(String formattedDate) {
        if ( ! StringUtils.isEmpty(formattedDate)) {
            GregorianCalendar g = new GregorianCalendar();
            g.set(Calendar.YEAR, new Integer(formattedDate.substring(0, 4)));
            g.set(Calendar.MONTH, new Integer(formattedDate.substring(5, 7)) - 1);
            g.set(Calendar.DAY_OF_MONTH, new Integer(formattedDate.substring(8, 10)));
            if (formattedDate.length() > 10) {
                g.set(Calendar.HOUR, new Integer(formattedDate.substring(11, 13)));
                g.set(Calendar.MINUTE, new Integer(formattedDate.substring(14, 16)));
                g.set(Calendar.SECOND, new Integer(formattedDate.substring(17, 19)));
            }

            date = new Timestamp(g.getTime().getTime());
        }
    }

    public String getFormattedTimestamp() {
        if (date == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        GregorianCalendar g = new GregorianCalendar();
        g.setTime(date);
        sb.append(String.valueOf(g.get(Calendar.YEAR))).append('-')
                .append(twoDigitFormat(g.get(Calendar.MONTH) + 1)).append('-')
                .append(twoDigitFormat(g.get(Calendar.DAY_OF_MONTH))).append(' ')
                .append(twoDigitFormat(g.get(Calendar.HOUR))).append(':')
                .append(twoDigitFormat(g.get(Calendar.MINUTE))).append(':')
                .append(twoDigitFormat(g.get(Calendar.SECOND)));

        return sb.toString();
    }

    public String getFormattedDate() {
        if (date == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        GregorianCalendar g = new GregorianCalendar();
        g.setTime(date);
        sb.append(String.valueOf(g.get(Calendar.YEAR))).append('-')
                .append(twoDigitFormat(g.get(Calendar.MONTH) + 1)).append('-')
                .append(twoDigitFormat(g.get(Calendar.DAY_OF_MONTH)));

        return sb.toString();
    }

    public Date asDate() {
        if (date == null) {
            return null;
        }
        return new Date(date.getTime());
    }

    public Timestamp asTimestamp() {
        return date;
    }

    private String twoDigitFormat(int num) {
        return num > 9 ? String.valueOf(num) : "0" + num;
    }
}
