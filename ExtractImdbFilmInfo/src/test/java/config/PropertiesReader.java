package config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertiesReader {

    private Properties countryProperties;

    public PropertiesReader() {
        this(System.getProperty("country"));
    }

    public PropertiesReader(String country) {
        Country countryCode;

        if (country == null) {
            countryCode = Country.uk;
        } else {

            switch (country) {
                case "uk":
                    countryCode = Country.uk;
                    break;
                case "f1":
                    countryCode = Country.f1;
                    break;
                case "fr":
                    countryCode = Country.f1;
                    break;
                case "de":
                    countryCode = Country.de;
                    break;
                case "sg":
                    countryCode = Country.sg;
                    break;
                case "jp":
                    countryCode = Country.jp;
                    break;
                case "cn":
                    countryCode = Country.cn;
                    break;
                case "china":
                    countryCode = Country.cn;
                    break;
                case "at":
                    countryCode = Country.at;
                    break;
                case "be01":
                    countryCode = Country.be01;
                    break;
                case "be02":
                    countryCode = Country.be02;
                    break;
                case "benl":
                    countryCode = Country.be01;
                    break;
                case "befr":
                    countryCode = Country.be02;
                    break;
                case "au":
                    countryCode = Country.au;
                    break;
                case "hk01":
                    countryCode = Country.hk01;
                    break;
                case "hk02":
                    countryCode = Country.hk02;
                    break;
                case "my":
                    countryCode = Country.my;
                    break;
                case "nz":
                    countryCode = Country.nz;
                    break;
                case "ph":
                    countryCode = Country.ph;
                    break;
                case "za":
                    countryCode = Country.za;
                    break;
                case "kr":
                    countryCode = Country.kr;
                    break;
                case "tw01":
                    countryCode = Country.tw01;
                    break;
                case "tw02":
                    countryCode = Country.tw02;
                    break;
                case "th":
                    countryCode = Country.th;
                    break;
                case "cz":
                    countryCode = Country.cz;
                    break;
                case "dk":
                    countryCode = Country.dk;
                    break;
                case "nl":
                    countryCode = Country.nl;
                    break;
                case "hu":
                    countryCode = Country.hu;
                    break;
                case "it":
                    countryCode = Country.it;
                    break;
                case "ie":
                    countryCode = Country.ie;
                    break;
                case "no":
                    countryCode = Country.no;
                    break;
                case "pl":
                    countryCode = Country.pl;
                    break;
                case "pt":
                    countryCode = Country.pt;
                    break;
                case "es":
                    countryCode = Country.es;
                    break;
                case "se":
                    countryCode = Country.se;
                    break;
                case "dech":
                    countryCode = Country.dech;
                    break;
                default:
                    countryCode = Country.uk;
                    System.out.println("Country '" + country + "' not recognised, using 'uk' properties file.");
            }
        }

        init(countryCode);
    }

    public PropertiesReader(Country country) {
        init(country);
    }

    private static char hexDigit(char ch, int offset) {
        int value = (ch >> offset) & 0xF;

        if (value <= 9)
            return (char) ('0' + value);

        return (char) ('A' + value - 10);
    }

    private static String escapeString(String str) {
        StringBuilder result = new StringBuilder();

        int length = str.length();
        for (int i = 0; i < length; i++) {
            char character = str.charAt(i);
            if (character <= 0x007e) {
                result.append(character);
                continue;
            }

            result.append('\\');
            result.append('u');
            result.append(hexDigit(character, 12));
            result.append(hexDigit(character, 8));
            result.append(hexDigit(character, 4));
            result.append(hexDigit(character, 0));
        }
        return result.toString();
    }

    private void init(Country country) {
        Properties generalProperties = new Properties();
        String generalPropertiesFileName = "data/general.properties";
        initProperties(generalProperties, generalPropertiesFileName);

        countryProperties = new Properties(generalProperties);
        String countryPropertiesFileName = "data/" + getPropertiesFileNameFor(country);
        initProperties(countryProperties, countryPropertiesFileName);
    }

    private void initProperties(Properties properties, String propertiesFileName) {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFileName);

        if (inputStream != null) {

            StringBuilder stringBuilder = new StringBuilder();

            try {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");

                while (true) {
                    int character = inputStreamReader.read();
                    if (character < 0)
                        break;

                    stringBuilder.append((char) character);
                }

                String inputString = escapeString(stringBuilder.toString());
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputString.getBytes("ISO-8859-1"));

                properties.load(byteArrayInputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Properties file not found '" + propertiesFileName + "'");
        }
    }

    public String getProperty(String property) {
        return countryProperties.getProperty(property);
    }

    private String getPropertiesFileNameFor(Country country) {
        String fileName;

        switch (country) {
            case uk:
                fileName = "uk.properties";
                break;
            case f1:
                fileName = "f1.properties";
                break;
            case de:
                fileName = "de.properties";
                break;
            case sg:
                fileName = "sg.properties";
                break;
            case jp:
                fileName = "jp.properties";
                break;
            case cn:
                fileName = "cn.properties";
                break;
            case at:
                fileName = "at.properties";
                break;
            case be01:
                fileName = "be01.properties";
                break;
            case be02:
                fileName = "be02.properties";
                break;
            case my:
                fileName = "my.properties";
                break;
            case au:
                fileName = "au.properties";
                break;
            case hk01:
                fileName = "hk01.properties";
                break;
            case hk02:
                fileName = "hk02.properties";
                break;
            case cz:
                fileName = "cz.properties";
                break;
            case dk:
                fileName = "dk.properties";
                break;
            case nl:
                fileName = "nl.properties";
                break;
            case hu:
                fileName = "hu.properties";
                break;
            case it:
                fileName = "it.properties";
                break;
            case ie:
                fileName = "ie.properties";
                break;
            case no:
                fileName = "no.properties";
                break;
            case ph:
                fileName = "ph.properties";
                break;
            case pl:
                fileName = "pl.properties";
                break;
            case pt:
                fileName = "pt.properties";
                break;
            case es:
                fileName = "es.properties";
                break;
            case se:
                fileName = "se.properties";
                break;
            case nz:
                fileName = "nz.properties";
                break;
            case dech:
                fileName = "dech.properties";
                break;
            case kr:
                fileName = "kr.properties";
                break;
            case tw01:
                fileName = "tw01.properties";
                break;
            case tw02:
                fileName = "tw02.properties";
                break;
            case th:
                fileName = "th.properties";
                break;
            case za:
                fileName = "za.properties";
                break;
            default:
                fileName = "uk.properties";
        }

        return fileName;
    }

    private enum Country {uk, f1, de, sg, jp, cn, at, be01, be02, au, hk01, hk02, my, nz, ph, za, kr, tw01, tw02, th, cz, dk, nl, hu, it, ie, no, pl, pt, es, se, dech}
}
