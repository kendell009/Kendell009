package salma.mah.se.funinmalmo;

/**
 * Created by User on 4/23/2017.
 */

public class Constants {
    //Number of articles to fetch at a time
    public static final int ARTICLES = 10;
    //URLs
    public final static String MAKONSERT = "http://malmolivekonserthus.mynewsdesk.com/rss/current_news/67325";
    public final static String MAINIT = "http://initiativet.malmo.se/epetition_core/feed/local_petitions";
    public final static String MAARENA = "http://www.mynewsdesk.com/se/rss/source/56723/pressrelease";
    public final static String REDCROSS = "http://kommun.redcross.se/malmo/evenemang/?format=Rss";
    public final static String MABIBLA = "http://malmo.stadsbibliotek.org/feeds/malmo.xml";
    public final static String MAEVENT = "http://www.mynewsdesk.com/rss/source/424/event";
    public final static String SF = "http://www.sf.se/sfmedia/external/rss/premieres.rss";
    public final static String INKONST = "http://www.inkonst.com/category/music/feed/";
    public final static String SVEFAN = "http://www.svenskafans.com/rss/team/92.aspx";
    public final static String KONTPUNKT = "http://www.kontrapunktmalmo.net/feed/";
    public final static String BABEL = "http://babelmalmo.se/program/klubb/feed";
    public final static String MAH = "http://www.mah.se/rss/nyheter";
    public final static String KB = "http://kulturbolaget.se/feed/";
    //URLs as an array
    public static String[] FEEDS = {MAH, MAKONSERT, MAEVENT, MABIBLA, KB, SF, SVEFAN, MAINIT, KONTPUNKT, REDCROSS, MAARENA, BABEL, INKONST};
    //Sorting cases
    public final static String SORTBYALPHABET = "SortByAlpha";
    public final static String SORTBYDATE = "SortByDate";
    //Locale values
    public final static String LANGUAGE_SV = "sv";
    public final static String LANGUAGE_EN = "en";
}

