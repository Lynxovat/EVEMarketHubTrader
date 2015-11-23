package pw.redalliance.MarketAPI.EveCentral;

import pw.redalliance.MarketAPI.ItemMarketData;
import pw.redalliance.MarketAPI.MarketAPI;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Lynx on 22.07.2015.
 */
public class EveCentralMarketAPI implements MarketAPI {
    private static final SAXParserFactory saxFactory = SAXParserFactory.newInstance();
    private static final String urlInitStr = "http://api.eve-central.com/api/marketstat?typeid=";
    private static final String urlUseSysStr = "&usesystem=";

    @Override
    public ItemMarketData getData(int typeID, int systemID) {
        try {
            URL url = new URL(makeUrlRequestString(typeID, systemID));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            return parseResponse(con.getInputStream());
        } catch (Exception e) {
            System.out.println("No market data for type: " + typeID);
        }
        return new ItemMarketData(0.0, 0.0, 0.0);
    }

    private static String makeUrlRequestString(int typeID, int systemID) {
        return urlInitStr + typeID + urlUseSysStr + systemID;
    }

    private static ItemMarketData parseResponse(InputStream is) {
        try {
            SAXParser saxParser = saxFactory.newSAXParser();
            MarketStatHandler handler = new MarketStatHandler();
            saxParser.parse(is, handler);
            return fillData(handler);
        } catch (Throwable err) {
            err.printStackTrace();
        }
        return null;
    }

    private static ItemMarketData fillData(MarketStatHandler handler) {
        return new ItemMarketData(
                handler.maxBye,
                handler.minSell,
                handler.volume
        );
    }
}
