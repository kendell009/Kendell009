package salma.mah.se.funinmalmo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

/**
 * This class contains the basic logic for the application.
 * Its main purpose is to create and show the layouts.
 */
public class MainActivity extends AppCompatActivity {
    public static String CURRENT_LANGUAGE = Constants.LANGUAGE_SV;
    private ArrayAdapter<String> rssAdapter;
    private ArrayAdapter<String> savedRssAdapter;
    private ArrayList<String> rssItemList;
    private ArrayList<String> savedRssItemList;
    private ArrayList<RSSFeed> feeds;
    private ArrayList<RSSArticle> savedRssArticles;
    private ArrayList<StaticArticle> staticArticles;
    private Button btnCloseArticle;
    private Button btnSaveArticle;
    private Button browserButton;
    private LinearLayout museumsLayoutView;
    private LinearLayout savedLayoutView;
    private LinearLayout rssLayoutView;
    private LinearLayout startScreen;
    private LinearLayout grayoutScreen;
    private ListView savedView;
    private ListView rssView;
    private ListView staticView;
    private PopupWindow popView;
    private RSSArticle currentArticle;
    private RSSFeed currentFeed;
    private String sortingTechnique;
    private TextView articleTitle;
    private TextView articleDescription;

    /**
     * Methods that gets called when the class is created.
     * Initializes the application.
     *
     * @param savedInstance: Saved state of the application(?)
     * @author Joakim
     */
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        initAppLanguage();
        setContentView(R.layout.activity_main);
        initVariables();
        initTabBar();
        initSavedArticles();
    }

    /**
     * Initializes the language for the application.

     */
    private void initAppLanguage() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        android.content.res.Configuration conf = getResources().getConfiguration();
        conf.locale = new Locale(CURRENT_LANGUAGE);
        getResources().updateConfiguration(conf, dm);
    }

    /**
     * Loads saved articles from a local file and saves them to a instance variable.
     *
     * @author Joakim
     */
    private void initSavedArticles() {
        savedRssArticles = SavedArticleHandler.GetSavedArticles();
        savedRssItemList.clear();
        for (String article : SavedArticleHandler.SortBy(savedRssArticles, sortingTechnique))
            savedRssItemList.add(article);
        savedRssAdapter.notifyDataSetChanged();
    }

    /**
     * Saves the list of saved articles to a local file.
     *
     * @author Joakim
     */
    private void saveArticles() {
        SavedArticleHandler.SetSavedArticles(savedRssArticles);
    }

    /**
     * Initializes the application.
     *
     * @author Joakim, Hanna
     */
    private void initVariables() {
        sortingTechnique = Constants.SORTBYALPHABET;
        startScreen = (LinearLayout) findViewById(R.id.startScreen);
        rssLayoutView = (LinearLayout) findViewById(R.id.rssView);
        savedLayoutView = (LinearLayout) findViewById(R.id.savedView);
        museumsLayoutView = (LinearLayout) findViewById(R.id.museumsView);
        grayoutScreen = (LinearLayout) findViewById(R.id.grayout);
        if (rssView == null)
            rssView = (ListView) findViewById(R.id.rssListView);
        if (savedView == null)
            savedView = (ListView) findViewById(R.id.savedListView);
        if (staticView == null)
            staticView = (ListView) findViewById(R.id.staticListView);
        rssItemList = new ArrayList<>();
        feeds = new ArrayList<>();
        savedRssItemList = new ArrayList<>();
        savedRssArticles = new ArrayList<>();
        ArrayList<String> staticItemList = new ArrayList<>();
        staticArticles = new ArrayList<>();
        rssAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, rssItemList);
        savedRssAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, savedRssItemList);
        initStaticArticles(staticItemList);
        rssView.setAdapter(rssAdapter);
        savedView.setAdapter(savedRssAdapter);
        savedLayoutView.setVisibility(View.INVISIBLE);
        museumsLayoutView.setVisibility(View.INVISIBLE);
        grayoutScreen.setVisibility(View.INVISIBLE);

        savedView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                savedRssItemClicked(position);
            }
        });
        staticView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                staticArticleItemClicked(position);
            }
        });

        rssItemList.add("Loading feeds");
        rssAdapter.notifyDataSetChanged();
        new RSSThread().start();
    }

    /**
     * Initializes static articles.
     *
     * @param staticItemList: The itemList that shall contain the articles
     * @author Joakim
     */
    private void initStaticArticles(ArrayList<String> staticItemList) {
        StaticArticleHandler.SaveStaticArticles();
        for (StaticArticle article : StaticArticleHandler.GetStaticArticles()) {
            staticArticles.add(article);
            staticItemList.add(article.getTitle());
        }
        ArrayAdapter<String> staticAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, staticItemList);
        staticView.setAdapter(staticAdapter);
        staticAdapter.notifyDataSetChanged();
    }

    /**
     * Creates a tab bar that allows the user to change views.
     */
    private void initTabBar() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.rssTab));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.savedTab));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.museumTab));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    rssLayoutView.setVisibility(View.VISIBLE);
                    savedLayoutView.setVisibility(View.INVISIBLE);
                    museumsLayoutView.setVisibility(View.INVISIBLE);
                } else if (tab.getPosition() == 1) {
                    rssLayoutView.setVisibility(View.INVISIBLE);
                    savedLayoutView.setVisibility(View.VISIBLE);
                    museumsLayoutView.setVisibility(View.INVISIBLE);
                } else if (tab.getPosition() == 2) {
                    rssLayoutView.setVisibility(View.INVISIBLE);
                    savedLayoutView.setVisibility(View.INVISIBLE);
                    museumsLayoutView.setVisibility(View.VISIBLE);
                }
            }

            public void onTabUnselected(TabLayout.Tab tab) {

            }

            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * Displays the chosen article.
     *
     * @author Hanna, Joakim
     */
    private void showArticle(RSSArticle article) {
        try {
            LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popLayout = inflater.inflate(R.layout.layout_article, (ViewGroup) findViewById(R.id.rssArticleView));
            articleTitle = (TextView) popLayout.findViewById(R.id.rssArticleTitle);
            articleDescription = (TextView) popLayout.findViewById(R.id.rssArticleDescription);
            btnCloseArticle = (Button) popLayout.findViewById(R.id.closeButton);
            btnCloseArticle.setOnClickListener(popListener);
            btnSaveArticle = (Button) popLayout.findViewById(R.id.saveButton);
            btnSaveArticle.setOnClickListener(saveListener);
            browserButton = (Button) popLayout.findViewById(R.id.browserButton);
            browserButton.setOnClickListener(popListener);
            articleTitle.setText(article.getTitle());
            articleDescription.setText(article.getDescription());
            currentArticle = article;

            for (RSSArticle savedArticle : savedRssArticles)
                if (savedArticle.getTitle().equals(article.getTitle()))
                    btnSaveArticle.setText(R.string.removeArticle);
            grayoutScreen.setVisibility(View.VISIBLE);
            popView = new PopupWindow(popLayout, 900, 1300, true);
            popView.showAtLocation(popLayout, Gravity.CENTER, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the chosen static article
     *
     * @author Hanna, Joakim
     */
    private void showArticle(StaticArticle article) {
        try {
            LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popLayout = inflater.inflate(R.layout.layout_article, (ViewGroup) findViewById(R.id.rssArticleView));
            articleTitle = (TextView) popLayout.findViewById(R.id.rssArticleTitle);
            articleDescription = (TextView) popLayout.findViewById(R.id.rssArticleDescription);
            TextView articleLink = (TextView) popLayout.findViewById(R.id.rssArticleLink);
            TextView articleOpenHours = (TextView) popLayout.findViewById(R.id.rssArticleOpenHours);
            btnCloseArticle = (Button) popLayout.findViewById(R.id.closeButton);
            btnSaveArticle = (Button) popLayout.findViewById(R.id.saveButton);
            browserButton = (Button) popLayout.findViewById(R.id.browserButton);
            btnCloseArticle.setOnClickListener(popListener);
            browserButton.setOnClickListener(popListener);
            btnSaveArticle.setVisibility(View.INVISIBLE);
            articleTitle.setText(article.getTitle() + "\n");
            articleDescription.setText(article.getDescription() + "\n");
            articleLink.setText(article.getLink() + "\n");
            articleOpenHours.setText(article.getOpenHours());
            grayoutScreen.setVisibility(View.VISIBLE);
            popView = new PopupWindow(popLayout, 900, 1300, true);
            popView.showAtLocation(popLayout, Gravity.CENTER, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * If the RSS tab is active, goes back to the RSS feed view.
     *
     * @param view: The view that invokes the method(?)
     * @author Joakim
     */
    public void goBack(View view) {
        if (rssLayoutView.getVisibility() == View.VISIBLE) {
            addFeeds();
            rssAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Adds all available RSS feeds to the ListView (rssItemList).
     *
     * @author Joakim
     */
    private void addFeeds() {
        rssItemList.clear();
        for (int i = 0; i < feeds.size(); i++)
            rssItemList.add(feeds.get(i).getTitle());
    }

    /**
     * Checks whether the clicked object is an RSSFeed or an RSSArticle.
     * If feed, updates rssItemList to show articles from the feed.
     * If article, shows the article.
     *
     * @param position: The x and y coordinates of the click/touch
     * @author Joakim
     */
    private void rssViewItemClicked(int position) {
        Object o = rssView.getItemAtPosition(position);
        if (o.toString().equals(getResources().getString(R.string.showMore))) {
            new FeedThread(currentFeed.getTitle()).start();
            return;
        }

        for (RSSFeed tempFeed : feeds)
            if (tempFeed.getTitle().equals(o.toString())) {
                showListOfArticles(tempFeed);
                currentFeed = tempFeed;
                return;
            }
        for (RSSArticle article : currentFeed.getFeed())
            if (article.getTitle().equals(o)) {
                showArticle(article);
                return;
            }
    }

    /**
     * Gets invoked when a saved article gets clicked on.
     * Shows the chosen article:
     *
     * @param position: The x and y coordinates of the click/touch
     * @author Joakim
     */
    private void savedRssItemClicked(int position) {
        Object o = savedView.getItemAtPosition(position);
        for (RSSArticle article : savedRssArticles)
            if (article.getTitle().equals(o)) {
                showArticle(article);
                return;
            }
    }

    /**
     * Method for opening an article in a browser.
     *
     * @param link: The article link
     * @author Hanna
     */
    public void openBrowser(String link) {
        for (RSSFeed feed : feeds)
            for (RSSArticle article : feed.getFeed())
                if (article.getLink().equals(link)) {
                    String url = article.getLink();
                    Uri webPage = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
                    if (intent.resolveActivity(getPackageManager()) != null)
                        startActivity(intent);
                }
    }

    /**
     * Checks what static article is clicked and shows it.
     *
     * @param position: The position of the clicked article
     * @author Joakim
     */
    private void staticArticleItemClicked(int position) {
        Object o = staticView.getItemAtPosition(position);
        for (StaticArticle article : staticArticles)
            if (article.getTitle().equals(o)) {
                showArticle(article);
                return;
            }
    }

    /**
     * A listener for the popView that shows articles.
     * Closes the popup view when clicked.
     *
     * @author Hanna
     */
    private OnClickListener popListener = new OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.closeButton:
                    popView.dismiss();
                    grayoutScreen.setVisibility(View.INVISIBLE);
                    break;
                case R.id.browserButton:
                    openBrowser(currentArticle.getLink());
                    break;
            }
        }
    };

    /**
     * Shows a popup menu with settings.
     *
     * @author Hanna
     */
    public void popMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_changeLanguage:
                        if (MainActivity.this.getResources().getConfiguration().locale.getLanguage().equals(Constants.LANGUAGE_EN))
                            CURRENT_LANGUAGE = Constants.LANGUAGE_SV;
                        else if (MainActivity.this.getResources().getConfiguration().locale.getLanguage().equals(Constants.LANGUAGE_SV))
                            CURRENT_LANGUAGE = Constants.LANGUAGE_EN;
                        else
                            CURRENT_LANGUAGE = Constants.LANGUAGE_SV;
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    /**
     * A specific OnClickListener for when the btnSaveArticle button gets clicked.
     * Saves the currently viewed article.
     *
     * @author Joakim
     */
    private OnClickListener saveListener = new OnClickListener() {
        public void onClick(View v) {
            if (btnSaveArticle.getText().toString().equals(getResources().getString(R.string.saveButton))) {
                savedRssArticles.add(currentArticle);
                btnSaveArticle.setText(R.string.removeArticle);
                savedRssItemList.clear();
                for (String article : SavedArticleHandler.SortBy(savedRssArticles, sortingTechnique))
                    savedRssItemList.add(article);
                savedRssAdapter.notifyDataSetChanged();
                saveArticles();
            } else if (btnSaveArticle.getText().toString().equals(getResources().getString(R.string.removeArticle)))
                for (RSSArticle article : savedRssArticles)
                    if (article.getTitle().equals(currentArticle.getTitle())) {
                        savedRssArticles.remove(article);
                        btnSaveArticle.setText(R.string.saveButton);
                        savedRssItemList.clear();
                        for (String title : SavedArticleHandler.SortBy(savedRssArticles, sortingTechnique))
                            savedRssItemList.add(title);
                        savedRssAdapter.notifyDataSetChanged();
                        saveArticles();
                        break;
                    }
        }
    };

    /**
     * Gets the articles from a chosen feed and shows them in the ListView.
     *
     * @param feed: The feed to show the articles from
     * @author Joakim
     */
    private void showListOfArticles(RSSFeed feed) {
        rssItemList.clear();
        for (RSSArticle tempArticle : feed.getFeed())
            rssItemList.add(tempArticle.getTitle());
        rssItemList.add(getResources().getString(R.string.showMore));
        rssAdapter.notifyDataSetChanged();
    }

    /**
     * A new Thread.
     * Expands the feed showing articles.
     *
     * @author Sara
     */
    private class FeedThread extends Thread {
        private String rssTitle;

        /**
         * Constructor.
         * Initializes variables.
         *
         * @param rssTitle: The articleTitle of the feed to expand.
         * @author Sara
         */
        public FeedThread(String rssTitle) {
            this.rssTitle = rssTitle;
        }

        /**
         * Method that allows for threading.
         *
         * @author Sara
         */
        public void run() {
            expandFeed();
        }

        /**
         * Expands a feed, allowing for more articles to be shown at a time.
         *
         * @author Sara
         */
        private void expandFeed() {
            for (RSSFeed feed : feeds)
                if (feed.getTitle().equals(rssTitle))
                    currentFeed = RSSFactory.fetchFeed(feed.getLink(), (feed.getFeed().size() + Constants.ARTICLES));
            runOnUiThread(new Runnable() {
                public void run() {
                    showListOfArticles(currentFeed);
                    rssAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    /**
     * A class made for threading.
     * Contains methods for fetching RSS feeds from .xml URLs and for displaying them.
     *
     * @author Joakim
     */
    private class RSSThread extends Thread {

        /**
         * Gets called as part of the threading.
         * Calls every method in order.
         *
         * @author Joakim
         */
        public void run() {
            readFeeds();
            addFeeds();
            showFeeds();
        }

        /**
         * Fetches every available RSS feed, converts them to an RSSFeed object, and saves them in an ArrayList (feeds).
         */
        private void readFeeds() {
            for (String feed : Constants.FEEDS)
                feeds.add(RSSFactory.fetchFeed(feed, Constants.ARTICLES));
        }

        /**
         * Hides the startScreen.
         * Invokes the GUI thread and displays the RSS feeds.
         *
         * @author Joakim
         */
        private void showFeeds() {
            runOnUiThread(new Runnable() {
                public void run() {
                    startScreen.setVisibility(View.INVISIBLE);
                    rssView.setClickable(true);
                    rssView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            rssViewItemClicked(position);
                        }
                    });
                    rssAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}