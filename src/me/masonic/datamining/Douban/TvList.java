package me.masonic.datamining.Douban;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import com.csvreader.CsvWriter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Mason Project
 * 2017-10-30-0030
 */
public class TvList extends BreadthCrawler {
    private static final String OUTPUT = ".\\output\\DouBan\\";
    private static final String SPIDER_SUM = "120";

    public TvList(String crawlPath, boolean autoParse) {
        super(crawlPath, autoParse);
    }

    private static List<Integer> getTvDouBanID(String data) {
        String regEx = "\\\"id\\\":\\\"\\d+.\\d";
        // 匹配形如"rate":"x.y"
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(data);

        List<Integer> id = new ArrayList<>();

        while (matcher.find()) {
            id.add(Integer.parseInt((matcher.group().split(":\\\"")[1])));
        }
        if (id.size() == 0) {
            System.out.println("未能取得电视剧ID数据");
            return null;
        }
        return (id);
    }

    private static List<Double> getTvRate(String data) {
        String regEx = "\\\"rate\\\":\\\"\\d+.\\d";
        // 匹配形如"rate":"x.y"
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(data);

        List<Double> rate = new ArrayList<>();

        while (matcher.find()) {
            rate.add(Double.parseDouble(matcher.group().split(":\\\"")[1]));
        }
        if (rate.size() == 0) {
            System.out.println("未能取得电视剧评分数据");
            return null;
        }
        return (rate);

    }

    private static List<String> getTvName(String data) {

        String regEx = "\\\"title\\\":\\\"[\\u4e00-\\u9fa5]+(\\d+)?";
        // 匹配形如"title":"多个汉字+(多个数字)"
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(data);

        List<String> names = new ArrayList<>();

        while (matcher.find()) {
//            data_tv = matcher.group().split(":\\\"")[1];
            names.add(matcher.group().split(":\\\"")[1]);
//            data_tv = matcher.group();
        }
        if (names.size() == 0) {
            System.out.println("未能取得电视剧名称数据");
            return null;
        }
        return (names);

    }

    @Override
    public void visit(Page page, CrawlDatums crawlDatums) {
        String data = page.html();
        List<String> names = getTvName(data);
        List<Double> rates = getTvRate(data);
        List<Integer> id = getTvDouBanID(data);
        System.out.println(names);
        System.out.println(rates);
        System.out.println(id);

//        String csvp = OUTPUT + "豆瓣热播电视剧.csv";
        String csvp = OUTPUT + "豆瓣近期电视剧.csv";

        CsvWriter cw = new CsvWriter(csvp, ',', Charset.forName("GBK"));
        try {
            assert names != null;
            for (int i = 0; i < names.size(); i++) {
                assert id != null;
                assert rates != null;
                String[] content = {names.get(i), id.get(i).toString(), rates.get(i).toString()};
                cw.writeRecord(content);
            }
            cw.close();
            System.out.println(csvp + "已保存");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


//        System.out.println(data_tv);
//        Elements soaps = page.select("div[class=list]");
//        System.out.println(page.html());
//        System.out.println(soaps.text());


    public static void main(String[] args) throws Exception {

        TvList spider = new TvList("crawl", true);

        spider.setThreads(1);
        spider.setTopN(5000);

        spider.setExecuteInterval(2000);
        // 按热度排序
//        spider.addSeed("https://movie.douban.com/j/search_subjects?type=tv&tag=%E5%9B%BD%E4%BA%A7%E5%89%A7&sort=recommend&page_limit=" + SPIDER_SUM + "&page_start=0");

        // 按时间排序
        spider.addSeed("https://movie.douban.com/j/search_subjects?type=tv&tag=%E5%9B%BD%E4%BA%A7%E5%89%A7&sort=time&page_limit=" + SPIDER_SUM + "&page_start=0");

        spider.start(1);
    }
}
