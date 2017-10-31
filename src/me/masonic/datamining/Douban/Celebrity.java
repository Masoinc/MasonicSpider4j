package me.masonic.datamining.Douban;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Mason Project
 * 2017-10-30-0030
 */
public class Celebrity extends BreadthCrawler {
    private static final String OUTPUT = ".\\output\\DouBan\\Overviews\\";
    // 运行进度缓存
    private static ArrayList<String> current_cel = new ArrayList<>();
    private static ArrayList<String> current_cel_bestv = new ArrayList<>();
    private static ArrayList<String> current_cel_recentv = new ArrayList<>();

    private static ArrayList<String[]> csv = new ArrayList<>();

    public Celebrity(String crawlPath, boolean autoParse) {
        super(crawlPath, autoParse);
    }

    //TODO:
    //完成演员/导演/编剧数据库
    //完成微博粉丝量数据采集工作
    //试图应用NLP技术判断评论
    //试图为LSTM模型共享参数
    //试图魔改卷积神经网络结构

    @Override
    public void visit(Page page, CrawlDatums crawlDatums) {
        // 名称
        String cid = current_cel.get(current_cel.size() - 1);

        Elements name = page.select("div[id=content]").select("h1");

        Elements recentpiece = page.select("div[class=info]").select("a");
        Elements masterpiece = page.select("div[class=info]").select("a");
        Elements masterpiecev = page.select("div[class=info]").select("em");
//        recentpiece.get(1).text(), recentpiece.get(2).text(), recentpiece.get(3).text(), recentpiece.get(4).text(),recentpiece.get(5).text()
        List<String> rp = new ArrayList<>();
        List<String> mp = new ArrayList<>();
        List<String> mpv = new ArrayList<>();
        List<String> rpv = new ArrayList<>();

        for (int i = 1; i < 6; i++) {
            rp.add(recentpiece.get(i).text());
        }

        for (int i = 6; i < 11; i++) {
            mp.add(masterpiece.get(i).text());
        }
        for (int i = 0; i < 5; i++) {
            rpv.add(masterpiecev.get(i).text());
        }
        for (int i = 5; i < 10; i++) {
            mpv.add(masterpiecev.get(i).text());
        }

        // 最近5部作品
        System.out.println(rp);
        System.out.println(rpv);
        // 最佳5部作品
        System.out.println(mp);
        System.out.println(mpv);

        Double rpv_avg = 0.0;
        Integer rpv_num = 5;
        for (String s : rpv) {
            if (s.equals("")) {
                rpv_num -= 1;
                continue;
            }
            rpv_avg += Double.parseDouble(s);
        }
        rpv_avg /= rpv_num;

        Double mpv_avg = 0.0;
        Integer mpv_num = 5;
        for (String s : mpv) {
            if (s.equals("")) {
                mpv_num -= 1;
                continue;
            }
            mpv_avg += Double.parseDouble(s);
        }
        mpv_avg /= mpv_num;
        current_cel_recentv.set(
                current_cel_recentv.indexOf(cid), rpv_avg.toString());
        current_cel_bestv.set(
                current_cel_bestv.indexOf(cid), mpv_avg.toString());


        // 爬取完成后写入源文件csv
//        System.out.println(masterpiecev);

    }

    private static List<String> getCelebrityList(String file) throws IOException {
        try {
            CsvReader csvReader = new CsvReader(file, ',', Charset.forName("GBK"));

            // 无表头
            // csvReader.readHeaders();
            List<String> ids_toget = new ArrayList<>();
            // 相当于reader.start和reader.stop之间的{}
            while (csvReader.readRecord()) {
                csv.add(csvReader.getValues());

                String[] first_column = csvReader.get(0).split(System.getProperty("line.separator"));
                String[] ids = csvReader.get(2).split(System.getProperty("line.separator"));

                for (int i = 0; i < ids.length; i++) {
                    if (first_column[i].contains("导演")) {
                        ids_toget.add(ids[i]);
                    } else if
                            (first_column[i].contains("编剧") || first_column[i].contains("演员")) {
                        if (Integer.parseInt(first_column[i].substring(2)) > 5) {
                            ids_toget.add("excepted");
                        } else {
                            ids_toget.add(ids[i]);
                        }
                    }
                }
//                System.out.println(ids_toget);
//                tvlist.add(csvReader.get(0));
                // System.getProperty("line.separator") 获取当前系统换行符
            }
            return ids_toget;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {


        File file = new File(OUTPUT);
        File[] flist = file.listFiles();
        assert flist != null;
        for (File f : flist) {
            List<String> clist = getCelebrityList(f.toString());
            assert clist != null;
            for (String ss : clist) {
                if (ss.equals("excepted")) {
                    current_cel.add("");
                    current_cel_bestv.add("");
                    current_cel_recentv.add("");
                } else {
                    current_cel.add(ss);
                    current_cel_bestv.add(ss);
                    current_cel_recentv.add(ss);
                    runCelebritySpider(ss);
                }
            }
            System.out.print("最佳" + current_cel_bestv);
            System.out.println("最近" + current_cel_recentv);
            writeCelebrityv(f.getName());

        }
    }

    //        String test = OUTPUT + "白夜追凶.csv";
//        System.out.println(getCelebrityList(test));
    private static void writeCelebrityv(String fname) throws IOException {
        String csvp = OUTPUT + fname;

        System.out.println(current_cel_bestv.indexOf(""));
        CsvWriter cw = new CsvWriter(csvp, ',', Charset.forName("GBK"));

        for (int i = 0; i < csv.size() + 1; i++) {
            if (i > current_cel_bestv.indexOf("")) {
                break;
            }
            String[] line = new String[csv.get(i).length + 1];
            for (int p = 0; p < csv.get(i).length; p++) {
                line[p] = csv.get(i)[p];
            }
            line[csv.get(i).length] = current_cel_bestv.get(i);
            cw.writeRecord(line);

        }

//        for (int i = 0; i < current_cel_bestv.size(); i++) {
//            String[] line = new String[csv.get(i).length + 1];
//            for (int p = 0; p < csv.get(i).length; p++) {
//                line[p] = csv.get(i)[p];
//            }
//            line[csv.get(i).length + 1] = current_cel_bestv.get(i);
//            cw.writeRecord(line);
//        }
        cw.close();
        csv.clear();
        current_cel_bestv.clear();
        current_cel_recentv.clear();
        current_cel.clear();
        System.out.println(csvp + "已保存");

    }

    private static void runCelebritySpider(String cid) throws Exception {
        Celebrity spider = new Celebrity("crawl", true);

        spider.setThreads(1);
        spider.setTopN(5000);

        spider.setExecuteInterval(2000);

        spider.addSeed("https://movie.douban.com" + cid);

        spider.start(1);
    }

}
