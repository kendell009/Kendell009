package salma.mah.se.funinmalmo;
import android.os.Environment;
import android.util.Log;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
/**
 * Created by User on 4/23/2017.
 */

public class StaticArticleHandler {
    /**
     * Reads static articles from a file and saves them in a variable.
     *
     * @return ArrayList<StaticArticle>: A list of static articles
     * @author Joakim
     */
    public static ArrayList<StaticArticle> GetStaticArticles() {
        ArrayList<StaticArticle> tempList = new ArrayList<>();
        try {
            String rootPath = Environment.getExternalStorageDirectory().toString();
            File dirPath = new File(rootPath + "/assets");
            String filePath = "static_articles.txt";
            File file = new File(dirPath, filePath);
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));

            Object o;
            while ((o = ois.readObject()) != null)
                if (o instanceof StaticArticle) {
                    StaticArticle article = (StaticArticle) o;
                    if (article.getLanguage().equals(MainActivity.CURRENT_LANGUAGE))
                        tempList.add(article);
                }
            ois.close();
        } catch (EOFException e) {
            Log.d("GetStaticArticles", "Loaded");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return tempList;
    }

    /**
     * Writes the static articles to a file.
     * Intended as a programmer tool, stayed because of necessity.
     *
     * @author Joakim
     */
    public static void SaveStaticArticles() {
        ArrayList<StaticArticle> articleList = new ArrayList<>();
        articleList.add(new StaticArticle("sv", "Malmö Museer", "Malmö Museer grundades 1841. Utställningarna visas i byggnader på Slottsholmen (med Malmöhus slott), Teknikens och Sjöfartens hus samt Kommendanthuset.", "http://malmo.se/Kultur--fritid/Kultur--noje/Museer--utstallningar/Malmo-Museer/Om-Malmo-Museer.html", "Öppettider: Klockan 10—17 dagligen. stängt: 1 maj, midsommarafton och midsommardag, julafton och juldag, nyårsafton och nyårsdag."));
        articleList.add(new StaticArticle("sv", "Teknikens och Sjöfartens hus", "På teknikens och Sjöfartens hus hittar du utställningar om bland annat teknik, sjöfart och motorer. Kryp in i den riktiga ubåten U3 och besök det populära Båtlekrummet!", "http://malmo.se/Kultur--fritid/Kultur--noje/Museer--utstallningar/Malmo-Museer/Aktuella-utstallningar/Teknikens-och-Sjofartens-hus.html", "Öppettider: Klockan 10—17 dagligen. stängt: 1 maj, midsommarafton och midsommardag, julafton och juldag, nyårsafton och nyårsdag."));
        articleList.add(new StaticArticle("sv", "Konsthallen", "Malmö Konsthall, som öppnades 1975, har ett av Nordeuropas största rum för den samtida konsten.", "http://malmo.se/Kultur--fritid/Kultur--noje/Konst--design/Malmo-Konsthall.html", "Öppettider: Se aktuellt kalendarium på Malmö konsthalls hemsida."));
        articleList.add(new StaticArticle("sv", "Moderna Museet", "Moderna Museet är ett av Europas ledande museer för modern och samtida konst, med en samling i absolut världsklass.", "http://www.modernamuseet.se/malmo/sv/", "Öppettider: Tisdag–söndag 11–18, Måndag stängt. För öppettider på helgdagar, se Moderna Museets hemsida."));
        articleList.add(new StaticArticle("sv", "Malmö Opera", "Malmö Opera är ett fantastiskt funkispalats som invigdes 1944, mitt under brinnande världskrig! Scenen är en av de större i Europa och salongen rymmer hela 1511 personer.", "http://www.malmoopera.se/", "Öppettider: Biljettkassan och foajén är öppen, vardagar kl 12-17."));
        articleList.add(new StaticArticle("sv", "Moriska Paviljongen", "Moriska Paviljongen är ett kulturhus där alla ska känna sig välkomna. Tillsammans med samarbetspartner, föreningar och eldsjälar skapar vi en mötesplats av sällan skådat slag. Huset rymmer en uppsjö av program på fyra scener, barer och inte minst restaurang.", "http://www.moriskapaviljongen.se/", "Öppettider: Onsdag-Torsdag: kl 18-02, Fredag: kl 17-03 (Afterwork kl 17-19), Lördag: kl 18-03 (Köket stänger ca 22)."));
        articleList.add(new StaticArticle("sv", "Mitt Möllan", "Mitt Möllan är en inspirerande mötesplats på framväxt där färgstark mat och alternativa butiker blandas med konst och kreativa kontor.", "http://www.mittmollan.se/", "Öppettider: Mån-Lör 09-20, Sön 12-16, Bemärk att butiker och kök har individuella öppettider."));
        articleList.add(new StaticArticle("sv", "Etage Nattklubb", "Etage i Malmö har alltid varit öppet för alla gäster som känner för att roa sig på sitteget vis. De olika alternativen gör att alla gäster kan umgås tillsammans under ett och samma tak.", "http://etagegruppen.se/web/etage/", "Öppettider: Måndag 18 +, 23.30-04.00, onsdag 23.30-04.00 (endast öppet på specialkvällar), torsdag 20 år, 23.30-04.00, fredag 23 år, 23.30-05.00, lördag 25 år, 23.30-05.00."));
        articleList.add(new StaticArticle("sv", "Ribersborgs Kallbadhus", "Hos kallbadhuset hittar du två stora utomhusbassänger (havsbassänger). Dessutom finner du totalt 5st bastur. Det finns 1st vedeldad bastu samt 1st torrbastu på respektive sida av anläggningen (dam/herr). Dessutom kan vi erbjuda en gemensam fuktbastu. Har vi öppet finns det alltid varm bastu att besöka.", "http://www.ribersborgskallbadhus.se/", "Öppettider: Sommartid (1 Maj - 31 Augusti). Måndag, tisdag, torsdag och fredag 09:00 - 20:00. Onsdag 09:00 - 21:00. Lördag, söndag och helgdag 09:00 - 18:00. Vintertid, se Kallbadhusets hemsida."));
        articleList.add(new StaticArticle("sv", "Emporia", "Emporia är ett köpcentrum med cirka 230 butiker och restauranger som har byggts nära Malmö Arena och citytunnelstationen i Hyllievång i sydvästra delen av Malmö.", "http://se.club-onlyou.com/Emporia", "Öppettider: Alla dagar 10 - 20. För avvikande öppettider, se Emporias hemsida.   "));
        articleList.add(new StaticArticle("en", "Malmö Museer", "At Malmö Museer you can see everything from the Nordic region's oldest surviving Renaissance castle to a real submarine and fantastic vehicles. The museum's permanent exhibitions focus on history, natural history, technology and seafaring.", "http://malmo.se/Kultur--fritid/Kultur--noje/Museer--utstallningar/Malmo-Museer/Sprak/In-English.html", "Opening hours: \n" +
                "Daily 10-17. \n" +
                "The museum is closed on Christmas Eve, Christmas Day, New Year's Eve, New Year's Day, 1 May, Midsummer's Eve, Midsummer's Day."));
        articleList.add(new StaticArticle("en", "Teknikens och Sjöfartens hus", "The Science and Maritime House. The building houses exhibitions on shipping, aviation and engines, and you can crawl into a real submarine! If you like to experiment, the Ideas Planet is an obvious favourite. In the Boat Playroom all children are invited to play with and experience ships from various time periods plus beautiful objects from our collections.", "http://malmo.se/Kultur--fritid/Kultur--noje/Museer--utstallningar/Malmo-Museer/Sprak/In-English.html", "Opening hours: \n" +
                "Daily 10-17. \n" +
                "The museum is closed on Christmas Eve, Christmas Day, New Year's Eve, New Year's Day, 1 May, Midsummer's Eve, Midsummer's Day."));
        articleList.add(new StaticArticle("en", "Konsthallen", "Malmö Konsthall opened in 1975 and is one of Europe's largest exhibition halls for contemporary art. Architect Klas Anshelm has created an exhibition hall with great flexibility, generous space and fantastic light.", "http://www.konsthall.malmo.se/o.o.i.s/2741", "Opening hours: \n" +
                "See homepage."));
        articleList.add(new StaticArticle("en", "Moderna Museet", "Moderna Museet is one of Europe's leading museums of modern and contemporary art, with a collection of high international standing.", "http://www.modernamuseet.se/malmo/en/", "Opening hours:\n" +
                "Monday closed.\n" +
                "Tuesday-sunday 11-18.\n" +
                "For opening hours on holidays, see the Moderna Museet's website."));
        articleList.add(new StaticArticle("en", "Malmö Opera", "Malmö Opera presents the whole range of music theatre with main emphasis on opera and musical. We perform the great opera classics, musicals, contemporary musical drama, concerts and dance, always meeting the highest international standards of quality.", "http://www.malmoopera.se/in-english", "Opening hours: \n" +
                "The box office and foyer is open weekdays at 12-17."));
        articleList.add(new StaticArticle("en", "Moriska Paviljongen", "At Moriska Paviljongen everyone should feel welcome. Together with partners, associations and enthusiasts, we are creating a fun meeting point. The house can accommodate a variety of programs on four stages, bars and not least the restaurant.", "http://www.moriskapaviljongen.se/", "Opening hours: \n" +
                "Monday-Thursday 18-02 pm. \n" +
                "Friday 17-03 pm (after work at 17-19).\n" +
                "Saturday: 18-03 pm (kitchen closes about 22)."));
        articleList.add(new StaticArticle("en", "Mitt Möllan", "Mitt Möllan is an inspiring meeting place where colorful food and alternative shops mixed with art and creative offices.", "http://www.mittmollan.se/", "Opening hours: \n" +
                "Monday-Saturday 09am-20. \n" +
                "Sunday 12-16. \n" +
                "Note that shops and kitchens have individual opening hours."));
        articleList.add(new StaticArticle("en", "Etage Nattklubb", "Etage in Malmö has always been open to all guests who know how to enjoy themselves in their own way. The various options allow guests to socialize together under one roof.", "http://etagegruppen.se/web/etage/", "Opening hours: \n" +
                "Monday + 18, 23:30 to 04 am. \n" +
                "Wednesday, 23:30 to 04 am (only open on special events). \n" +
                "Thursday 20, 23:30 to 04 am. \n" +
                "Friday 23, 23:30 to 05 am. \n" +
                "Saturday 25, 23:30 to 05 am."));
        articleList.add(new StaticArticle("en", "Ribersborgs Kallbadhus", "Here you will find two large outdoor pools (sea basins) and if we are open, there is always a hot sauna too visit.", "http://www.ribersborgskallbadhus.se/", "Opening hours: \n" +
                "Summer (May 1 to August 31). \n" +
                "Monday, Tuesday, Thursday and Friday 09 am to 08 pm. \n" +
                "Wednesday 09 am to 09 pm. \n" +
                "Saturday, Sunday and public holiday 09 am to 06 pm. \n" +
                "During the winter, see Kallbadhuset's website."));
        articleList.add(new StaticArticle("en", "Emporia", "Emporia is a shopping center with about 230 shops and restaurants, that have been built near Malmo Arena and the City Tunnel station in Hyllievång in the southwestern part of Malmo.", "http://se.club-onlyou.com/Emporia", "Opening hours: \n" +
                "Every day 10 - 20. \n" +
                "For exceptional opening hours, see Emporia's website. "));

        try {
            String rootPath = Environment.getExternalStorageDirectory().toString();
            File dirPath = new File(rootPath + "/assets");
            dirPath.mkdir();
            String filePath = "static_articles.txt";
            File file = new File(dirPath, filePath);
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));

            for (StaticArticle article : articleList)
                oos.writeObject(article);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
