package pw.redalliance.PriceFile;

import java.io.*;

/**
 * Created by Lynx on 11.08.2015.
 */
public class PriceFileWriter {
    private static final String ENCODING = "utf-8";
    private static final String BLUEPRINT_STRING = "Blueprint";
    private static final String ORIGINAL_SUFFIX = " (Original)";

    Writer writer;

    public PriceFileWriter(String filename) {
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(filename), ENCODING));
        } catch (IOException ex) {
            ex.printStackTrace();
            close();
            //report
        }
    }

    public void close() {
        try {
            writer.close();
        } catch (Exception ex) {}
    }

    public void printLine(double price, double volume, String name) {
        try {
            if (price == 0) return;
            writer.write(String.join(" ", Double.toString(price), Double.toString(volume),
                    name.endsWith(BLUEPRINT_STRING)? name.concat(ORIGINAL_SUFFIX) : name));
            writer.write('\n');
        } catch (IOException ex) {
            ex.printStackTrace();
            close();
        }
    }
}
