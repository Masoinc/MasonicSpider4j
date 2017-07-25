# MasonicSpider4j
### 登峰杯决赛-数据挖掘-爱奇艺指数爬虫
### Posted Time: 17.7.25
### Author: Masonic
----
- 原理
  - 数据来源网页: http://index.iqiyi.com/
  - 利用 Url 参数
  - get_index_trend?album_id=[电视剧ID] 指定电视剧
  - &time_window=[时间] 指定时间范围
  - 获取爱奇艺指数数据
- 结构
  - me.masonic.datamining.Qiyi
    - IndexSpider
    - 爱奇艺指数爬虫
    - TopSpider
    - 爱奇艺电视剧风云榜爬虫
  - me.masonic.datamining.Utility
    - JsonToCsv
    - 将取得的爱奇艺指数数据转为Csv
    - 方便后续使用
    - SqlUtility
    - 将取得的爱奇艺指数数据存入Mysql数据库
