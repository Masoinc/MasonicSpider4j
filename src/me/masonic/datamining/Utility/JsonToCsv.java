package me.masonic.datamining.Utility;

import com.csvreader.CsvWriter;
import com.google.gson.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Masonic Project
 * 2017-7-8-0008
 * 将MasonicSpider4p爬取的json格式文件转为csv格式文件
 */

public class JsonToCsv {
    public static void main(String args[]) {
        String input = "E:\\PyCharmProjects\\MasonicSpider4p\\Output\\14to17";

        String output = ".\\output\\Qiyi_14to17\\";

        File dir = new File(input);
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if ((files != null ? files.length : 0) == 0) {
                System.out.println("目录下无文件");
            } else {
                for (File file : files) {
                    JsonParser parse = new JsonParser();

                    try {

                        String csvp = output + file.getName().replace(".json", ".csv");

                        CsvWriter cw = new CsvWriter(csvp, ',', Charset.forName("GBK"));

                        JsonObject json = (JsonObject) parse.parse(new FileReader(file));  //创建jsonObject对象

                        JsonArray data = json.get("zt").getAsJsonArray();


                        for (Object aData : data) {

                            JsonObject jo = (JsonObject) aData;
                            if (jo.has("day_key")) {
                                String date = jo.get("day_key").getAsString();
                                String index = jo.get("value").getAsString();

                                String[] content = {date, index};

                                try {
                                    cw.writeRecord(content);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                //System.out.println(date + "-" + index);
                            }
                        }
                        //System.out.println("——————————");
                        cw.close();
                    } catch (JsonIOException e) {
                        e.printStackTrace();
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    System.out.println(file.getName() + "转换已完成");
                }

            }
        } else {
            System.out.println("指定的目录有误");
        }
    }
}


