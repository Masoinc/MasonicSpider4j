package me.masonic.datamining.Qiyi;

import me.masonic.datamining.Utility.SqlUtility;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.*;

/**
 * Masonic Project
 * 2017-7-8-0008
 * 用于爬取爱奇艺热度排行
 */
public class TopSpider extends BreadthCrawler {
    private static PreparedStatement statement;
    private static final String RESULT_SHEET = "top";


    public TopSpider(String crawlPath, boolean autoParse) {
        super(crawlPath, autoParse);
    }

    //爬虫处理
    @Override
    public void visit(Page page, CrawlDatums next) {

        Elements soaps = page.select("li[class=clearfix]");

        for (Element soap : soaps) {
            String title = soap.select("a").attr("title");

            String tv$str = soap.select("p").select("em").get(1).text();
            double tv$int = Double.parseDouble(tv$str.replace("万","").replace("亿","")) * (tv$str.contains("万") ? 10000 : 1) * (tv$str.contains("亿") ? 100000000 : 1);

            System.out.println("剧名: " + soap.select("a").attr("title"));
            System.out.println("爱奇艺总播放量: " + soap.select("p").select("em").get(1).text());

            String sql = "INSERT INTO " + RESULT_SHEET + " (title,tv) VALUES (?,?);";

            try {
                statement = SqlUtility.getConnection().prepareStatement(sql);
                statement.setObject(1, title);
                statement.setObject(2, tv$int);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }


    public static void main(String[] args) throws Exception {
        SqlUtility.registerSQL("iqiyi_statistic");

        TopSpider spider = new TopSpider("Iqiyi", false);

        spider.setThreads(1);

        //爱奇艺-电视剧风云榜
        spider.addSeed("http://top.iqiyi.com/dianshiju.html");
        spider.addRegex("http://www.iqiyi.com/a_.*html");

        spider.start(4);
    }


}
