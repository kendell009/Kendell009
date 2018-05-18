package salma.mah.se.funinmalmo;

import java.io.Serializable;

/**
 * Created by User on 4/23/2017.
 */

public class StaticArticle implements Serializable {
    private String title;
    private String description;
    private String openHours;
    private String link;
    private String language;

    /**
     * Constructor.
     * Initializes variables.
     *
     * @param language:    The language the article is shown in
     * @param title:       The title of the article
     * @param description: The description of the article
     * @param openHours:   The open hours in article
     * @param link:        The link of the article
     * @author Joakim
     */
    public StaticArticle(String language, String title, String description, String link, String openHours) {
        this.language = language;
        this.title = title;
        this.description = description;
        this.link = link;
        this.openHours = openHours;
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
     * Returns the open hours in the article.
     *
     * @return String: The open hours of the article
     * @author Joakim
     */
    public String getOpenHours() {
        return openHours;
    }

    /**
     * Returns the link of the article.
     *
     * @return String:
     * @author Joakim
     */
    public String getLink() {
        return link;
    }

    /**
     * Returns the language the article is shown in.
     *
     * @return String: The language of the article
     * @author Joakim
     */
    public String getLanguage() {
        return language;
    }
}
