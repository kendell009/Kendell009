package salma.mah.se.funinmalmo;
import java.util.ArrayList;
/**
 * Created by User on 4/23/2017.
 */

public class RSSFeed {
    private ArrayList<RSSArticle> feed;
    private String title;
    private String link;

    /**
     * The constructor.
     * Initializes variables.
     *
     * @param title: The title of the feed
     * @param link:  The link to the feed
     * @author Joakim
     */
    public RSSFeed(String title, String link) {
        feed = new ArrayList<>();
        this.title = title;
        this.link = link;
    }

    /**
     * Adds an RSSArticle to the feed.
     *
     * @param article: The RSSArticle to be added to the feed
     * @author Joakim
     */
    public void add(RSSArticle article) {
        feed.add(article);
    }

    /**
     * Returns the feed.
     *
     * @return ArrayList<RSSArticle>: All RSSArticles in the feed
     * @author Joakim
     */
    public ArrayList<RSSArticle> getFeed() {
        return feed;
    }

    /**
     * Returns the size of the feed.
     *
     * @return int: The size of the feed
     * @author Joakim
     */
    public int size() {
        return feed.size();
    }

    /**
     * Returns the title of the feed.
     *
     * @return String: The title of the feed.
     * @author Joakim
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the .xml link of the feed.
     *
     * @return String: The link of the feed
     * @author Joakim
     */
    public String getLink() {
        return link;
    }

}
