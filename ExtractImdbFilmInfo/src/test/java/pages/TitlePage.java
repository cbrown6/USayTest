package pages;

import domain.Film;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import stepdefs.SharedDriver;
import uk.co.cjb.model.Utilities.Utilities;

import javax.rmi.CORBA.Util;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertTrue;

/**
 * Created by Colin on 27/05/2020.
 */
public class TitlePage extends Page {

    public TitlePage(SharedDriver webDriver) {
        super(webDriver);
    }

    //*** URLs
    private static final String URL = ".rs-online.com/web/p/";
    private static final String DEV_URL = "/Web/p/";

    //*** BY Locators
    private static final By REGISTER_TITLE = By.cssSelector("a[title='Register']");
    private static final By HEAR_ABOUT_US = By.cssSelector("select#hearAbout");
    private static final By TEXT_RS_STOCK_NUMBER = By.cssSelector("ul.keyDetailsLL > li > span[class='keyValue bold']");
    private static final By ONLINE_ADVERTISEMENT_BANNER = By.cssSelector("select#hearAbout > option[value='2']");
    private static final By FILM_TITLE_ROWS = By.cssSelector("td[class='result_text']");
    private static final By FILM_TITLE_ROWS_HREF = By.cssSelector("td[class='result_text']>a");
    private static final By TITLE = By.cssSelector("div[class='title_wrapper']>h1");
    private static final By YEAR = By.cssSelector("span[id='titleYear']>a");
    private static final By IMDB_RATING = By.cssSelector("span[itemprop='ratingValue']");
    private static final By DESCRIPTION = By.cssSelector("div[class='summary_text']");
    private static final By DIRECTOR_AND_ACTORS = By.cssSelector("div[class='credit_summary_item']");

    private static final By CERTIFICATE_GENRES = By.cssSelector("div[class='subtext']");


    //****************************************************************************************
    //*** METHODS
    //****************************************************************************************

    //*** NAVIGATE

    public void goTo() {
        waitForPageToLoad();
        urlContainsString(getProperty("page.registration"));
    }

    //*** IS DISPLAYED / IS ENABLED / IS SELECTED

    public void isElementDisplayed(String locatorName) {
        assertEquals("Click Here!", getTextFromFirstOption(HEAR_ABOUT_US));
    }

    public void dropdownIsNotDisplayed(String locatorName) {
        String text1 = getTextFromFirstOption(HEAR_ABOUT_US);
        assertEquals("Click Here!", getTextFromFirstOption(HEAR_ABOUT_US));
    }

    public By getElementLocator(String locatorName){
        By locator = new By.ByCssSelector("");
            switch(locatorName.toUpperCase())
        {
            case "ONLINE ADVERTISEMENT / BANNER":
                locator = ONLINE_ADVERTISEMENT_BANNER;
                break;
            default:
                fail("That page does not have a goTo step def");
        }
        return locator;
    }


    //*** SEND

    //*** CLICK

    //*** SELECT

    public void selectFromDropdownByText(String locatorName){
        By locator = new By.ByCssSelector("");
        switch (locatorName.toUpperCase()){
            case "ONLINE ADVERTISEMENT / BANNER":
                selectDropdownByText(HEAR_ABOUT_US, "Online Advertisement / Banner");
                break;
            case "HEAR ABOUT":
                locator = HEAR_ABOUT_US;
                break;
            default: fail("That page does not have a goTo step def");
        }
    }

    //*** CHECKS (TESTS)

    public void checkCorrectOptionIsDisplayed(String option){
        assertEquals(option, getTextFromFirstOption(HEAR_ABOUT_US));
    }

    public void checkFilmNameByYearIsDisplayed(String filmNameAndYear){
        List<WebElement> filmTitles = getElementsList(FILM_TITLE_ROWS);
        boolean filmByYearFound = false;
        int filmByYearFoundNumber = 0;

        for (WebElement filmTitle:filmTitles){
            String temp1 = filmNameAndYear;
            String temp2 = getTextFromElement(filmTitle);
            click(filmTitle);
            if (getTextFromElement(filmTitle).toLowerCase().equals(filmNameAndYear.toLowerCase())){
                filmByYearFound = true;
                break;
            }
            filmByYearFoundNumber++;
        }
        assertTrue(filmByYearFound);

        if (filmByYearFound){
            click(getElementFromListByIndex(FILM_TITLE_ROWS_HREF, filmByYearFoundNumber));
        }
    }

    public void checkFilmNameByYearDetailsAreDisplayed(String filmNameAndYear){

        Film film = new Film();

        //title             div[class='title_wrapper']>h1
        //year              span[id='titleYear']>a
        //imdbRating        span[itemprop='ratingValue']
        //certificate       div[class='subtext']
        //genre1             "   "
        //genre2             "   "
        //genre3             "   "
        //description       div[class='summary_text']
        //director;         div[class='credit_summary_item']        - 1
        //actor1;           div[class='credit_summary_item']        - 3
        //actor2;            "   "
        //actor3;            "   "

        film.setTitle(getText(TITLE));
        film.setYear(getText(YEAR));
        film.setImdbRating(getText(IMDB_RATING));
        getCertificateAndGenres(film);
        film.setDescription(getText(DESCRIPTION));
        getDirectorAndActors(film);

        writeFilmToCsvFile(film);

    }

    public void getCertificateAndGenres(Film film){
        Utilities utilities = new Utilities();
        String certificateAndGenres = getText(CERTIFICATE_GENRES);
        List<String> certificateAndGenresTempList = utilities.splitCsvLine(certificateAndGenres, "\\|");

        //String certificateAndGenresTemp = "PG | 1h 40min | Adventure, Drama, Family | 19 February 2020 (UK)";
        //List<String> certificateAndGenresTempList = utilities.splitCsvLine(certificateAndGenresTemp, "\\|");

        film.setCertificate(certificateAndGenresTempList.get(0));
        film.setFilmDuration(certificateAndGenresTempList.get(1));

        List<String> genreList = utilities.splitCsvLine(certificateAndGenresTempList.get(2), ",");
        film.setGenre1(genreList.get(0));
        film.setGenre2(genreList.get(1));
        film.setGenre3(genreList.get(2));
    }

    public void getDirectorAndActors(Film film){
        List<WebElement> directorAndActorsList = getElementsList(DIRECTOR_AND_ACTORS);
        int webElementNumber = 0;
        String director = null;
        String actorList = null;

        for (WebElement webElement:directorAndActorsList){
            if (webElementNumber == 0){
                director = getTextFromElement(webElement);
                film.setDirector(director.substring(10, director.length()));
            }
            if (webElementNumber == 2){
                actorList = getTextFromElement(webElement);
            }
            webElementNumber++;
        }

        Utilities utilities = new Utilities();
        List<String> actors = utilities.splitCsvLine(actorList, ",");
        String actorNumber3 = actors.get(2).split("\\|")[0];
        String actor1 = actors.get(0);

        film.setActor1(actor1.substring(7, actor1.length()));
        film.setActor2(actors.get(1));
        film.setActor3(actorNumber3);
    }

    public void writeFilmToCsvFile(Film film){
        Utilities utilities = new Utilities();
        String comma = ",";

        String csvLine =
                "hdd"                   //numberOrHDD
                + comma
                + film.getTitle()
                + comma
                + film.getYear()
                + comma
                + film.getDescription().replace(",",";")
                + comma
                + film.getGenre1()
                + comma
                + film.getGenre2()
                + comma
                + film.getGenre3()
                + comma
                + film.getImdbRating()
                + comma
                + film.getActor1()
                + comma
                + film.getActor2()
                + comma
                + film.getActor3()
                + comma
                + film.getDirector()
                + comma
                + film.getFilmDuration()
                + comma
                + " "                   // film.getTvOrDvd()
                + comma
                + film.getCertificate()
                + comma
                + " "                   // film.getHddDirectory()
                + comma
                + " "                   // film.getToWatch()
                + comma
                + " ";                   // film.getMyRating()

        List<String> csvLines = new ArrayList<>();
        csvLines.add(csvLine);

        utilities.createAndWriteListToFile("C:/Apps/ProjectData/ExtractImdbFilmInfo/ExtractImdbFilmInfoFile.txt", csvLines);

    }

}

