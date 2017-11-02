package me.masonic.datamining.BigDataSet;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Mason Project
 * 2017-11-1-0001
 */
public class Stacker {
    private static final String OUTPUT = ".\\output\\Big_DataSet\\";
    private static final String INPUT = ".\\output\\DouBan\\Overviews_OIH_Full\\";

    private static void initializeDataSet() throws IOException {
        String csvp = OUTPUT + "Scalar_backup.csv";

        CsvWriter cw = new CsvWriter(csvp, ',', Charset.forName("GBK"));
        String[] head = {"剧名", "豆瓣ID", "豆瓣评分", "豆瓣评价数",
                "豆瓣标签1", "豆瓣标签2", "豆瓣标签3",
                "播出时间",
                "5分评价", "4分评价", "3分评价", "2分评价", "1分评价",
                "导演最佳评分", "导演最近评分",
                " 编剧1最佳评分", "编剧1最近评分",
                "编剧2最佳评分", "编剧2最近评分",
                "演员1最佳评分", "演员1最近评分",
                "演员2最佳评分", "演员2最近评分",
                "演员3最佳评分", "演员3最近评分",
                "演员4最佳评分", "演员4最近评分",
                "演员5最佳评分", "演员5最近评分"
        };
        cw.writeRecord(head);
        cw.close();
        System.out.println("Scalar.csv已创建");
    }

    private static void writeRecord(String[] con) throws IOException {
        String csvp = OUTPUT + "Scalar_backup.csv";
        CsvWriter cw = new CsvWriter(csvp, ',', Charset.forName("GBK"));
        cw.writeRecord(con);
        cw.close();
    }

    public static void main(String[] args) throws IOException {

//        if (!file.exists()) {
//            initializeDataSet();
//        }

        File dir = new File(INPUT);
        File[] flist = dir.listFiles();
        assert flist != null;

        String csvp = OUTPUT + "Scalar_backup.csv";
        CsvWriter cw = new CsvWriter(csvp, ',', Charset.forName("GBK"));

        String[] head = {"剧名", "豆瓣ID", "豆瓣评分", "豆瓣评价数",
                "豆瓣标签1", "豆瓣标签2", "豆瓣标签3",
                "播出时间",
                "5分评价", "4分评价", "3分评价", "2分评价", "1分评价",
                "导演最佳评分", "导演最近评分",
                " 编剧1最佳评分", "编剧1最近评分",
                "编剧2最佳评分", "编剧2最近评分",
                "演员1最佳评分", "演员1最近评分",
                "演员2最佳评分", "演员2最近评分",
                "演员3最佳评分", "演员3最近评分",
                "演员4最佳评分", "演员4最近评分",
                "演员5最佳评分", "演员5最近评分"
        };
        cw.writeRecord(head);

        for (File f : flist) {
            String name = f.getName().replace(".csv", "").replace("Full_", "");
            CsvReader csvReader = new CsvReader(f.toString(), ',', Charset.forName("GBK"));
//            csvReader.readHeaders();
            ArrayList<String[]> csv = new ArrayList<>();
            while (csvReader.readRecord()) {
                csv.add(csvReader.getValues());
            }
            csvReader.close();
            String[] con = new String[head.length];
            con[0] = name;
            for (String[] record : csv) {
                for (String aRecord : record) {
                    switch (aRecord) {
                        case "总评价数":
                            con[3] = record[1];
                            break;
                        case "标签1":
                            con[4] = record[1];
                            break;
                        case "标签2":
                            con[5] = record[1];
                            break;
                        case "标签3":
                            con[6] = record[1];
                            break;
                        case "播出时间":
                            con[7] = record[1];
                            break;
                        case "评价1":
                            con[8] = record[1];
                            break;
                        case "评价2":
                            con[9] = record[1];
                            break;
                        case "评价3":
                            con[10] = record[1];
                            break;
                        case "评价4":
                            con[11] = record[1];
                            break;
                        case "评价5":
                            con[12] = record[1];
                            break;
                        case "导演":
                            if (record.length >= 4) {
                                con[13] = record[2];
                                con[14] = record[3];
                            }

                            break;
                        case "编剧1":
                            if (record.length >= 4) {
                                con[15] = record[2];
                                con[16] = record[3];
                            }
                            break;
                        case "编剧2":
                            if (record.length >= 4) {
                                con[17] = record[2];
                                con[18] = record[3];
                            }
                            break;
                        case "演员1":
                            if (record.length >= 4) {
                                con[19] = record[2];
                                con[20] = record[3];
                            }
                            break;
                        case "演员2":
                            if (record.length >= 4) {
                                con[21] = record[2];
                                con[22] = record[3];
                            }
                            break;
                        case "演员3":
                            if (record.length >= 4) {
                                con[23] = record[2];
                                con[24] = record[3];
                            }
                            break;
                        case "演员4":
                            if (record.length >= 4) {
                                con[25] = record[2];
                                con[26] = record[3];
                            }
                            break;
                        case "演员5":
                            if (record.length >= 4) {
                                con[27] = record[2];
                                con[28] = record[3];
                            }
                            break;
                    }
                }
            }
            cw.writeRecord(con);
        }
        cw.close();


    }
}
