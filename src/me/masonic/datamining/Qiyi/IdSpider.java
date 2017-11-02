package me.masonic.datamining.Qiyi;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import me.masonic.datamining.Utility.SqlUtility;

import java.net.URLEncoder;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Mason Project
 * 2017-11-2-0002
 */
public class IdSpider extends BreadthCrawler {
    public IdSpider(String crawlPath, boolean autoParse) {
        super(crawlPath, autoParse);
    }

    private static ArrayList<String> ids = new ArrayList<>();

    @Override
    public void visit(Page page, CrawlDatums crawlDatums) {
        String id = "";
        String pg = page.html();
        String regEx = "\\{\"aid\":\"\\d+";

        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(pg);

        if (matcher.find()) {
            id = matcher.group(0);
        }
        ids.add(id);
    }

    public static void main(String[] args) throws Exception {
        List<String> tvlist = new ArrayList<>();
        try {
            CsvReader csvReader = new CsvReader("E:\\IdeaProjects\\MasonicSpider4j\\output\\Big_DataSet\\Scalar_backup.csv", ',', Charset.forName("GBK"));
            // 读取表头
            csvReader.readHeaders();
            while (csvReader.readRecord()) {
                tvlist.add(csvReader.get(0));
                // System.getProperty("line.separator") 获取当前系统换行符
            }
            csvReader.close();
            System.out.println(tvlist);
        } catch (IOException e) {
            e.printStackTrace();
        }
//
//
        IdSpider spider = new IdSpider("crawl", true);

        spider.setThreads(1);
        spider.setTopN(5000);

        spider.setExecuteInterval(2000);


        for (String s : tvlist) {
            spider.addSeed("https://uaa.if.iqiyi.com/video_index/v2/filtered_suggest_album?key=" + URLEncoder.encode(s, "UTF-8") + "&platform=11&rltnum=10");
        }
        spider.start(1);

        String csvp = "E:\\IdeaProjects\\MasonicSpider4j\\output\\Big_DataSet\\Qiyi_id.csv";
        CsvWriter cw = new CsvWriter(csvp, ',', Charset.forName("GBK"));

        for (int i = 0; i <ids.size(); i++) {
            String[] con = new String[2];
            con[0] = tvlist.get(i);
            con[1] = ids.get(i);
            cw.writeRecord(con);
        }
        cw.close();
    }

}
