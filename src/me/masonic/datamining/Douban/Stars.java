package me.masonic.datamining.Douban;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Mason Project
 * 2017-10-30-0030
 * 豆瓣评分
 */

public class Stars extends BreadthCrawler {
    private static final String OUTPUT = ".\\output\\DouBan\\";

    public Stars(String crawlPath, boolean autoParse) {
        super(crawlPath, autoParse);
    }

    @Override
    public void visit(Page page, CrawlDatums next) {
        // 导演
//        Elements director = page.select("a[rel=v:directedBy]");
        Elements spans = page.select("span[class=attrs]");
        String director = spans.get(0).text();
        String director_id = spans.get(0).select("a[href]").attr("href");
        String[] scripter = spans.get(1).text().split("/");
        List<String> scripter_id = spans.get(1).select("a[href]").eachAttr("href");
        String[] actor = spans.get(2).text().split("/");
        List<String> actor_id = spans.get(2).select("a[href]").eachAttr("href");

        Elements ratenum = page.select("span[property=v:votes]");
        Elements rates = page.select("span[class=rating_per]");
        String csvp = OUTPUT + "" +".csv";



//        CsvWriter cw = new CsvWriter(csvp, ',', Charset.forName("GBK"));
//        try {
//            assert names != null;
//            for (int i = 0; i < names.size(); i++) {
//                assert id != null;
//                assert rates != null;
//                String[] content = {names.get(i), id.get(i).toString(), rates.get(i).toString()};
//
//                cw.writeRecord(content);
//
//
//            }
//            cw.close();
//            System.out.println(csvp + "已保存");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }



        System.out.println();
        Celebrity spider = new Celebrity("crawl", true);
//        for (Element span : spans) {
//            System.out.println(span.text());
////            System.out.println(span.select("a[href]").attr("href"));
//            System.out.println(span.select("a[href]").eachAttr("href"));
//
//        }

//        for(String s:stars) {
//            System.out.println(s);
//        }
//        System.out.println(director);
//        System.out.println(scripter);
//        System.out.println(sc);

    }
//    HashMap<String, String>
    private static void getTvList() throws IOException {
        try {
            // 创建CSV读对象
            CsvReader csvReader = new CsvReader(OUTPUT + "豆瓣热播电视剧.csv", ',', Charset.forName("GBK"));

            // 读表头
            csvReader.readHeaders();
            while (csvReader.readRecord()){
                // 读一整行
                System.out.println(csvReader.getValues()[1]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {


        Stars spider = new Stars("crawl", true);

        spider.setThreads(1);
        spider.setTopN(5000);

        spider.setExecuteInterval(2000);

        spider.addSeed("https://movie.douban.com/subject/26883064");
        spider.addSeed("https://movie.douban.com/subject/26930540");

    }

//        spider.start(1);
    }

