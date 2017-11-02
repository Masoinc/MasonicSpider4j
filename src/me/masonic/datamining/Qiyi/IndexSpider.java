package me.masonic.datamining.Qiyi;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import me.masonic.datamining.Utility.SqlUtility;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Masonic Project
 * 2017-7-8-0008
 * 用于爬取爱奇艺指数
 */
public class IndexSpider extends BreadthCrawler {

    private static final String OUTPUT = ".\\output\\Qiyi\\Qiyi_BigDataSet\\";

    private static HashMap<String, String> SOAPMAP = new HashMap<>();

    static {
//        SOAPMAP.put("207471001", "一粒红尘");
//        SOAPMAP.put("205171701", "深夜食堂");
//        SOAPMAP.put("204210101", "女管家");
//        SOAPMAP.put("205087501", "欢乐颂2");
//        SOAPMAP.put("203487801", "思美人");
//        SOAPMAP.put("203487801", "择天记");
//        SOAPMAP.put("204759601", "外科风云");
//        SOAPMAP.put("206264101", "继承人");
//        SOAPMAP.put("206206701", "人民的名义");
//        SOAPMAP.put("203792501", "漂洋过海来看你");
//        SOAPMAP.put("204466001", "射雕英雄传");
//        SOAPMAP.put("205017001", "因为遇见你");
//        SOAPMAP.put("204166701", "那片星空那片海");
//        SOAPMAP.put("204218901", "三生三世十里桃花");
//        SOAPMAP.put("205162101", "守护丽人");
//        SOAPMAP.put("205551601", "漂亮的李慧珍");
//        SOAPMAP.put("204358301", "夏至未至");
//        //SOAPMAP.put("205543201", "大唐荣耀");//未收录
//        //SOAPMAP.put("204691501", "孤芳不自赏");//未收录
//        //SOAPMAP.put("119658800", "白鹿原");//未收录
//        SOAPMAP.put("202526401", "少年四大名捕");
//        SOAPMAP.put("203152301", "秦时明月");
//        SOAPMAP.put("202856001", "古剑奇谭");
//        SOAPMAP.put("203164301", "老九门");
//        SOAPMAP.put("151646701", "爱情公寓4");
//        SOAPMAP.put("203034301", "小别离");
//        SOAPMAP.put("202473701", "伪装者");
//        SOAPMAP.put("202183301", "红高粱");
//        SOAPMAP.put("203143601", "麻雀");
//        SOAPMAP.put("203199301", "胭脂");
//        SOAPMAP.put("202121101", "琅琊榜");
//        SOAPMAP.put("202321801", "武媚娘传奇");
//        SOAPMAP.put("203364601", "锦绣未央");
        SOAPMAP.put("203965201", "楚乔传");
        SOAPMAP.put("205400701", "醉玲珑");
        SOAPMAP.put("205416101", "我的前半生");

    }

    public IndexSpider(String crawlPath, boolean autoParse) {
        super(crawlPath, autoParse);
    }

    //爬虫处理
    @Override
    public void visit(Page page, CrawlDatums next) {

        String data = page.html();

        //匹配剧名
        String regEx = "[^\\\"][\\u4e00-\\u9fa5]+";

        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(data);

        String title = null;
        while (matcher.find()) {
            title = matcher.group();
        }

        List<String> date = getDate(data);
        List<String> tv = getTvList(data);

        try {
//            saveDataByCsv(date, tv, title);
            CsvWriter cw = new CsvWriter("E:\\IdeaProjects\\MasonicSpider4j\\output\\Qiyi_BigDataSet\\" + title + ".csv", ',', Charset.forName("GBK"));

            for (int i = 0; i < date.size(); i++) {
                String[] content = {date.get(i), tv.get(i)};
                cw.writeRecord(content);
            }
            cw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(title + "采集完成");

        //System.out.println(data);

        //String title = page.regex("names:");
        //System.out.println(title);
        //String votescore = page.select("span[class=fenshu-r]").text();
    }

    private static List<String> getTvList(String data) {

        String regEx = "\\\"data\\\":\\[.*?\\]";

        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(data);

        String data_tv = null;

        while (matcher.find()) {
            data_tv = matcher.group().split(":")[1];
        }

        if (data_tv != null) {
            data_tv = data_tv.replace("[", "").replace("]", "");
        } else {
            System.out.println("未能取得播放量数据");
            return null;
        }
        return (Arrays.asList(data_tv.split(",")));

    }

    private static List<String> getDate(String data) {
        String regEx = "\\\"playtime\\\":\\[.*?\\]";

        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(data);

        String data_date = null;

        while (matcher.find()) {
            data_date = matcher.group().split(":")[1];
        }
        if (data_date != null) {
            data_date = data_date.replace("[", "").replace("]", "");
        } else {
            System.out.println("未能取得日期数据");
            return null;
        }
        return (Arrays.asList(data_date.split(",")));

    }

    private static void saveDataByCsv(List<String> dates, List<String> tvs, String id) throws IOException {


        String csvp = OUTPUT + id + ".csv";

        CsvWriter cw = new CsvWriter(csvp, ',', Charset.forName("GBK"));

        for (int i = 0; i < dates.size(); i++) {
            String[] content = {dates.get(i), tvs.get(i)};
            cw.writeRecord(content);
        }
        cw.close();

    }


    private static void saveDataByMysql(List<String> dates, List<String> tvs, String id) throws SQLException {

        String sql = "CREATE TABLE " + id + "(date VARCHAR(8) NOT NULL,tv VARCHAR(32) NOT NULL);";
        PreparedStatement stmt = SqlUtility.getConnection().prepareStatement(sql);
        stmt.execute();

        HashMap<String, String> datamap = new HashMap<>();

        for (int i = 0; i < dates.size(); i++) {
            datamap.put(dates.get(i), tvs.get(i));
        }

        Statement stmt2 = SqlUtility.getConnection().createStatement();

        for (String date : datamap.keySet()) {
            //INSERT INTO ID (date,tv) VALUES (datek,datev);
            stmt2.addBatch("INSERT INTO " + id + " (date,tv) VALUES (" + date + "," + datamap.get(date) + ");");
        }
//SB高效率版(?)
//        for (String date : datamap.keySet()) {
//            StringBuilder sb = new StringBuilder();
//            //INSERT INTO ID (date,tv) VALUES (datek,datev);
//            sb.append("INSERT INTO ").append(id).append(" (date,tv) VALUES (").append(date).append(",").append(datamap.get(date)).append(");");
//            stmt2.addBatch(sb.toString());
//        }
        stmt2.executeBatch();


    }


    public static void main(String[] args) throws Exception {

        SqlUtility.registerSQL("iqiyi_statistic");

        IndexSpider spider = new IndexSpider("crawl", true);

        spider.setThreads(1);
        spider.setTopN(5000);

        spider.setExecuteInterval(2000);

        ArrayList<String> ids = new ArrayList<>();
        CsvReader csvReader = new CsvReader("E:\\IdeaProjects\\MasonicSpider4j\\output\\Big_DataSet\\Qiyi_id.csv", ',', Charset.forName("GBK"));
        // 读取表头
        csvReader.readHeaders();
        while (csvReader.readRecord()) {
            ids.add(csvReader.get(1));
            // System.getProperty("line.separator") 获取当前系统换行符
        }
        csvReader.close();
        for (String id : ids) {
            spider.addSeed("https://uaa.if.iqiyi.com/video_index/v2/get_index_trend?album_id=" + id + "&time_window=-1");
        }
//        for (String soap : SOAPMAP.keySet()) {
//            spider.addSeed("https://uaa.if.iqiyi.com/video_index/v2/get_index_trend?album_id=" + soap + "&time_window=-1");
//        }
        spider.start(1);
    }
}
