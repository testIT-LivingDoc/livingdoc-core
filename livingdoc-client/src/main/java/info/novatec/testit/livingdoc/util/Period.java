package info.novatec.testit.livingdoc.util;

import java.io.Serializable;
import java.util.Date;


@SuppressWarnings("serial")
public class Period implements Serializable {
    private final Date start;

    private final Date end;

    private Period(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    public static Period to(Date end) {
        return new Period(null, end);
    }

    public static Period from(Date start) {
        return new Period(start, null);
    }

    public static Period fromTo(Date start, Date end) {
        return new Period(start, end);
    }

    public boolean includes(Date date) {
        return ( start == null || ! start.after(date) ) && ( end == null || date.before(end) );
    }

    public boolean beforeEnd(Date date) {
        return ( end == null || date.before(end) );
    }

    public int daysCount() {
        if (start == null || end == null) {
            return - 1;
        }
        return ( int ) ( ( end.getTime() - start.getTime() ) / 86400000 );
    }
}
