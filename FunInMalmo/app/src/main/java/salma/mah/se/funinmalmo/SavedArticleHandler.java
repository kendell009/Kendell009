package salma.mah.se.funinmalmo;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
/**
 * Created by User on 4/23/2017.
 */

public class SavedArticleHandler {
    /**
     * Fetches saved articles from a local file.
     *
     * @return ArrayList<RSSArticle>: A list of saved article read from a local file
     * @author Joakim
     */
    public static ArrayList<RSSArticle> GetSavedArticles() {
        ArrayList<RSSArticle> tempList = new ArrayList<>();

        try {
            String rootPath = Environment.getExternalStorageDirectory().toString();
            File dirPath = new File(rootPath + "/assets");
            String filePath = "saved_files.txt";
            File file = new File(dirPath, filePath);
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));

            Object o;
            while ((o = ois.readObject()) != null)
                if (o instanceof RSSArticle)
                    tempList.add((RSSArticle) o);
            ois.close();
        } catch (IOException e) {
            Log.e("SavedArticleHandler", "GetSavedArticles: IOException");
        } catch (ClassNotFoundException e) {
            Log.d("SavedArticleHandler", "GetSavedArticles: Saved articles loaded");
        }
        return tempList;
    }

    /**
     * Saves a list of saved articles to a local file.
     *
     * @param articleList: The list that should be saved to a local file
     * @author Joakim
     */
    public static void SetSavedArticles(ArrayList<RSSArticle> articleList) {
        try {
            String rootPath = Environment.getExternalStorageDirectory().toString();
            File dirPath = new File(rootPath + "/assets");
            dirPath.mkdir();
            String filePath = "saved_files.txt";
            File file = new File(dirPath, filePath);
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));

            if (articleList.size() > 0)
                for (RSSArticle article : articleList)
                    oos.writeObject(article);

            oos.close();
        } catch (IOException e) {
            Log.e("SavedArticleHandler", "SetSavedArticles: IOException");
            e.printStackTrace();
        }
    }

    /**
     * Sorts a list of saved articles by date or alphabet.
     *
     * @param list:     The list to be sorted
     * @param sortCase: How the list should be sorted (by alphabet/date(
     * @return ArrayList<String>: A list of article titles that is sorted
     * @author Joakim
     */
    public static ArrayList<String> SortBy(ArrayList<RSSArticle> list, String sortCase) {
        ArrayList<String> tempList = new ArrayList<>();
        ArrayList<String> sortingList = new ArrayList<>();
        ArrayList<GregorianCalendar> dateList = new ArrayList<>();

        //Sort
        if (sortCase.equals(Constants.SORTBYALPHABET)) {
            for (RSSArticle article : list) {
                sortingList.add(article.getTitle());
            }
            Collections.sort(sortingList);
        } else if (sortCase.equals(Constants.SORTBYDATE)) {
            for (RSSArticle article : list) {
                dateList.add(article.getPubDate());
            }
            Collections.sort(dateList);
        }

        //Add article titles to list
        if (sortCase.equals(Constants.SORTBYDATE)) {
            for (GregorianCalendar date : dateList)
                for (RSSArticle article : list)
                    if (date.equals(article.getPubDate()))
                        tempList.add(article.getTitle());
        } else if (sortCase.equals(Constants.SORTBYALPHABET))
            tempList = sortingList;

        return tempList;
    }
}
