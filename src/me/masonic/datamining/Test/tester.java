package me.masonic.datamining.Test;

import com.csvreader.CsvReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Mason Project
 * 2017-10-31-0031
 */
public class tester {
    public static void main(String[] args) throws IOException {
        ArrayList<String[]> csv = new ArrayList<>();
        CsvReader csvReader = new CsvReader(".\\output\\DouBan\\Overviews\\白鹿原.csv", ',', Charset.forName("GBK"));
        // csvReader.readHeaders();
        //如果需要第一行的数据的话，不要写这一行
        while (csvReader.readRecord()) {
            csv.add(csvReader.getValues());
        }
        System.out.println(Arrays.toString(csv.get(0)));
    }
}
