package Services;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import Models.WeatherModel;

/**
 * Created by Tvrtko on 6.2.2017..
 */

public class WeatherDataService extends DefaultHandler {
    private String stringUrl;
    private List<WeatherModel> weathers=new ArrayList<>();

    private String location;
    private String sunSet;
    private String sunRise;

    private String timeFrom;
    private String timeTo;
    private String symbolNum;
    private String symbolName;
    private String precipitationUnit;
    private String precipitationValue;
    private String precipitationType;
    private String windDirectionCode;
    private String windDirectionName;
    private String windSpeedValue;
    private String windSpeedName;
    private String temperatureUnit;
    private String temperatureValue;
    private String temperatureMin;
    private String temperatureMax;
    private String pressureUnit;
    private String pressureValue;
    private String humidityValue;
    private String cloudsValue;
    private String cloudsAll;
    private String weatherIconID;

    private String tmpString;

    public void update(String stringUrl){
        this.stringUrl=stringUrl;
        weathers.clear();
        parse();

    }

    public WeatherDataService(){
        super();
    }

    private void parse(){
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            System.setProperty("org.xml.sax.driver","org.xmlpull.v1.sax2.Driver");
            URL url = new URL(stringUrl);
            URLConnection conn = url.openConnection ();

            SAXParser parser = factory.newSAXParser();
            XMLReader xmlIn = parser.getXMLReader();

            xmlIn.setContentHandler(this);
            InputStreamReader xmlStream = new InputStreamReader(conn.getInputStream(),"UTF-8");
            BufferedReader xmlBuff = new BufferedReader(xmlStream);

            xmlIn.parse(new InputSource(xmlBuff));

        } catch (ParserConfigurationException e) {
            System.out.println("ParserConfig error");
        } catch (SAXException e) {
            System.out.println("SAXException : xml not well formed");
        } catch (MalformedURLException e) {
            System.out.println("Url exception");
        } catch (IOException e) {
            System.out.println("IO error");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(qName.equalsIgnoreCase("time")){
            timeFrom=attributes.getValue("from");
            timeTo=attributes.getValue("to");
        } else if(qName.equalsIgnoreCase("symbol")){
            weatherIconID=attributes.getValue("var");
            symbolNum=attributes.getValue("number");
            symbolName=attributes.getValue("name");

        } else if(qName.equalsIgnoreCase("precipitation")){
            precipitationUnit=attributes.getValue("unit");
            precipitationValue=attributes.getValue("value");
            precipitationType=attributes.getValue("type");
        } else if(qName.equalsIgnoreCase("windDirection")){
            windDirectionCode=attributes.getValue("code");
            windDirectionName=attributes.getValue("name");
        } else if(qName.equalsIgnoreCase("windSpeed")){
            windSpeedValue=attributes.getValue("mps");
            windSpeedName=attributes.getValue("name");
        } else if(qName.equalsIgnoreCase("temperature")){
            temperatureUnit=attributes.getValue("unit");
            temperatureValue=attributes.getValue("value");
            temperatureMin=attributes.getValue("min");
            temperatureMax=attributes.getValue("max");
        } else if(qName.equalsIgnoreCase("pressure")){
            pressureUnit=attributes.getValue("unit");
            pressureValue=attributes.getValue("value");
        } else if(qName.equalsIgnoreCase("humidity")){
            humidityValue=attributes.getValue("value");
        } else if(qName.equalsIgnoreCase("clouds")){
            cloudsValue=attributes.getValue("value");
            cloudsAll=attributes.getValue("all");
        } else if(qName.equals("sun")){
            sunRise=attributes.getValue("rise");
            sunSet=attributes.getValue("set");
            sunRise=sunRise.substring(sunRise.indexOf("T")+1);
            sunSet=sunSet.substring(sunSet.indexOf("T")+1);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(qName.equalsIgnoreCase("name")){
            location=tmpString;
        } else if(qName.equalsIgnoreCase("time")){
            weathers.add(new WeatherModel(timeFrom, timeTo, symbolNum, symbolName, precipitationUnit, precipitationValue, precipitationType, windDirectionCode, windDirectionName, windSpeedValue, windSpeedName, temperatureUnit, temperatureValue, temperatureMin, temperatureMax, pressureUnit, pressureValue, humidityValue, cloudsValue, cloudsAll,weatherIconID));
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        tmpString=new String(ch,start,length);
    }

    public List<WeatherModel> getWeathers() {
        return weathers;
    }

    public String getLocation() {
        return location;
    }

    public String getSunSet() {
        return sunSet;
    }

    public String getSunRise() {
        return sunRise;
    }
}
