package me.masonic.datamining.Douban;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Mason Project
 * 2017-10-30-0030
 * 豆瓣评分
 */

public class TvOverview extends BreadthCrawler {
    private static final String OUTPUT = ".\\output\\DouBan\\Overviews\\";

    public TvOverview(String crawlPath, boolean autoParse) {
        super(crawlPath, autoParse);
    }

    @Override
    public void visit(Page page, CrawlDatums next) {

        Elements name = page.select("span[property=v:itemreviewed]");

//        // 导演
//        Elements director = page.select("a[rel=v:directedBy]");

        Elements spans = page.select("span[class=attrs]");
        String director = spans.get(0).text();
        String director_id = spans.get(0).select("a[href]").attr("href");
        String[] scripter = spans.get(1).text().split("/");
        List<String> scripter_id = spans.get(1).select("a[href]").eachAttr("href");
        String[] actor = spans.get(2).text().split("/");
        List<String> actor_id = spans.get(2).select("a[href]").eachAttr("href");

        Elements rate_num = page.select("span[property=v:votes]");
        Elements rate_per = page.select("span[class=rating_per]");

        // 类型
        Elements tags = page.select("span[property=v:genre]");
        String time = page.select("span[property=v:initialReleaseDate]").text();

        // 集数(待解决)
        // TODO: 集数是Textcode格式，位于div[id=info]下
        // List len = page.select("div[id=info]").;

        String[] rate = rate_per.text().split(" ");
        String[] tag = tags.text().split(" ");

        // 写入CSV文件
        String csvp = OUTPUT + name.text() + ".csv";
        CsvWriter cw = new CsvWriter(csvp, ',', Charset.forName("GBK"));
        try {
            String[] dir = {"导演", director, director_id};
            cw.writeRecord(dir);
            for (int i = 0; i < scripter.length; i++) {
                String[] s = {"编剧" + String.valueOf(i + 1), scripter[i], scripter_id.get(i)};
                cw.writeRecord(s);
            }
            for (int i = 0; i < actor.length; i++) {
                String[] act = {"演员" + String.valueOf(i + 1), actor[i], actor_id.get(i)};
                cw.writeRecord(act);
            }
            String[] rn = {"总评价数", rate_num.text()};
            cw.writeRecord(rn);

            for (int i = 0; i < rate.length; i++) {
                String[] r = {"评价" + String.valueOf(i + 1), rate[i]};
                cw.writeRecord(r);
            }
            for (int i = 0; i < tag.length; i++) {
                String[] r = {"标签" + String.valueOf(i + 1), tag[i]};
                cw.writeRecord(r);
            }
            String[] t = {"播出时间", time};
            cw.writeRecord(t);
            cw.close();
            System.out.println(csvp + "已保存");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static List<String> getTvList() throws IOException {
        try {
//            CsvReader csvReader = new CsvReader(".\\output\\DouBan\\" + "豆瓣热播电视剧.csv", ',', Charset.forName("GBK"));
            CsvReader csvReader = new CsvReader(".\\output\\DouBan\\" + "豆瓣近期电视剧.csv", ',', Charset.forName("GBK"));

            // 读取表头
            csvReader.readHeaders();
            List<String> tvlist = new ArrayList<>();
            while (csvReader.readRecord()) {
                tvlist.add(csvReader.get(1));
                // System.getProperty("line.separator") 获取当前系统换行符
            }
            return tvlist;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        TvOverview spider = new TvOverview("crawl", true);

        spider.setThreads(1);
        spider.setTopN(5000);

        spider.setExecuteInterval(2000);

        List<String> tvlist = getTvList();
        assert tvlist != null;
        for (String tv : tvlist) {
            spider.addSeed("https://movie.douban.com/subject/" + tv);
        }

//        spider.addSeed("https://movie.douban.com/subject/26363830");

        spider.start(1);
    }

}

