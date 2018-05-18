package salma.mah.se.funinmalmo;
import java.net.MalformedURLException;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlPullParser;

import android.util.Log;

/**
 * Created by User on 4/23/2017.
 */

public class RSSFactory {
    /**
 * Fetches an RSS feed from a .xml site and returns it as a RSSFeed object.
 *
 * @param url:         The .xml site to fetch a feed from
 * @param maxArticles: The number of articles to fetch from the feed
 * @return RSSFeed: The feed represented as an object
 * @author Joakim
 */
public static RSSFeed fetchFeed(String url, int maxArticles) {
    RSSFeed feed = null;
    try {
        boolean isHeader = true;
        String description = "";
        String title = "";
        String link = "";
        String pubDate = "";
        int counter = 0;

        //Saves the url as a URL object
        URL rssUrl = new URL(url);
        //Creates a connection based on the url
        HttpURLConnection connection = (HttpURLConnection) rssUrl.openConnection();
        //Sets the Request Method ("GET" from the .xml)
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        connection.connect();
        //Creates an inputStream based on the connection
        InputStream stream = connection.getInputStream();
        //Creates an XML parser
        XmlPullParserFactory input = XmlPullParserFactory.newInstance();
        XmlPullParser reader = input.newPullParser();
        reader.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        reader.setInput(stream, null);

        int event = reader.getEventType();
        while (event != XmlPullParser.END_DOCUMENT && counter < maxArticles) {
            if (event == XmlPullParser.START_TAG)
                switch (reader.getName()) {
                    case "title":
                        reader.next();
                        if (isHeader) { //Checks if this is the first title, if so, create a new RSSFeed and add the title as the feed title
                            feed = new RSSFeed(reader.getText(), url);
                            isHeader = false;
                        } else
                            title = reader.getText();
                        break;
                    case "description":
                        reader.next();
                        description = reader.getText();
                        break;
                    case "link":
                        reader.next();
                        link = reader.getText();
                        break;
                    case "pubDate":
                        reader.next();
                        pubDate = reader.getText();
                        break;
                }
            else if (event == XmlPullParser.END_TAG)
                if (reader.getName().equals("item")) {
                    if (feed != null)
                        feed.add(new RSSArticle(title, description, link, pubDate));
                    counter++;
                }
            event = reader.next();
        }
        connection.disconnect();
    } catch (MalformedURLException e) {
        Log.e("RSSFactory", "URL Exception");
    } catch (IOException e) {
        Log.e("RSSFactory", "IO Exception");
    } catch (XmlPullParserException e) {
        Log.e("RSSFactory", "XML Exception");
    }
    return feed;
}
}

