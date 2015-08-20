package pw.redalliance.MarketAPI.EveCentral;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by Lynx on 26.06.2015.
 */
public class MarketStatHandler extends DefaultHandler {
    public int id = 0;
    public double maxBye = 0;
    public double minSell = 0;
    public double volume = 0;

    private enum TagT { BUY, SELL, ALL, NONE }
    private TagT group = TagT.NONE;

    private enum StatT { MAX_BUY, MIN_SELL, VOLUME, NONE }
    private StatT stat = StatT.NONE;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("type")) {
            if (attributes.getLocalName(0).equals("id")) {
                id = Integer.parseInt(attributes.getValue(0));
                return;
            }
        }

        else if (qName.equals("buy")) group = TagT.BUY;
        else if (qName.equals("sell")) group = TagT.SELL;
        else if (qName.equals("all")) group = TagT.ALL;

        else if (qName.equals("max") && group.equals(TagT.BUY)) stat = StatT.MAX_BUY;
        else if (qName.equals("min") && group.equals(TagT.SELL)) stat = StatT.MIN_SELL;
        else if (qName.equals("volume") && group.equals(TagT.ALL)) stat = StatT.VOLUME;
    }

    @Override
    public void characters(char[] chars, int start, int length) throws SAXException {
        if (stat == StatT.NONE) return;
        Double d = Double.valueOf(String.valueOf(chars, start, length));
        switch (stat) {
            case MAX_BUY:
                maxBye = d;
                break;
            case MIN_SELL:
                minSell = d;
                break;
            case VOLUME:
                volume = d;
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("bye") || qName.equals("sell") || qName.equals("all")) {
            group = TagT.NONE;
        }
        stat = StatT.NONE;
    }
}
