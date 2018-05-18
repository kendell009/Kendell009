package salma.mah.se.funinmalmo;

import java.io.Serializable;
import java.util.GregorianCalendar;

/**
 * Created by User on 4/23/2017.
 */

public class RSSArticle implements Serializable {
    private String description;
    private String title;
    private String link;
    private GregorianCalendar pubDate;

    /**
     * The constructor.
     * Initializes variables.
     *
     * @param title:       The title of the article
     * @param description: The description of the article
     * @param link:        The link to the article
     * @param pubDate:     The published date of the article
     * @author Joakim
     */
    public RSSArticle(String title, String description, String link, String pubDate) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.pubDate = formatDate(pubDate);
    }

    /**
     * Formats the date from a String to a GregorianCalender object.
     *
     * @param pubDate: The published date of the article, unformatted
     * @return GregorianCalender: A formatted published date
     * @author Joakim
     */
    private GregorianCalendar formatDate(String pubDate) {
        String[] splitter = pubDate.split(" ");
        if (splitter.length > 1) {
            int day = Integer.parseInt(splitter[1]);
            int month = getMonth(splitter[2]);
            int year = Integer.parseInt(splitter[3]);
            String[] time = splitter[4].split(":");
            int hour = Integer.parseInt(time[0]);
            int minute = Integer.parseInt(time[1]);
            return new GregorianCalendar(year, month, day, hour, minute);
        }
        return new GregorianCalendar();
    }

    /**
     * Returns the month of the published date.
     *
     * @param textMonth: The month in text
     * @return int: The month in numbers
     * @author Joakim
     */
    private int getMonth(String textMonth) {
        int month = 0;
        switch (textMonth) {
            case "Jan":
                month = 1;
                break;
            case "Feb":
                month = 2;
                break;
            case "Mar":
                month = 3;
                break;
            case "Apr":
                month = 4;
                break;
            case "May":
                month = 5;
                break;
            case "Jun":
                month = 6;
                break;
            case "Jul":
                month = 7;
                break;
            case "Aug":
                month = 8;
                break;
            case "Sep":
                month = 9;
                break;
            case "Oct":
                month = 10;
                break;
            case "Nov":
                month = 11;
                break;
            case "Dec":
                month = 12;
                break;
        }
        return month;
    }

    /**
     * Returns the title of the article.
     *
     * @return String: The title of the article
     * @author Joakim
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the description of the article.
     *
     * @return String: The description of the article
     * @author Joakim
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the published date of the article.
     *
     * @return GregorianCalendar: The published date of the article.
     * @author Joakim
     */
    public GregorianCalendar getPubDate() {
        return pubDate;
    }

    /**
     * Returns the link of the article.
     *
     * @return String: The link of the article
     * @author Joakim
     */
    public String getLink() {
        return link;
    }
}
