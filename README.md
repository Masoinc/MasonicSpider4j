# MasonicSpider4j
### 登峰杯决赛-数据挖掘-爱奇艺指数爬虫
### 17.7.25 By Masonic
### 

------

- http://index.iqiyi.com/ 
	- 爱奇艺指数
	- 利用 Url 参数
		- get_index_trend?album_id=[电视剧ID] 指定电视剧
		- &time_window=[时间] 指定时间范围
		- 获取爱奇艺指数数据
	- 程序文件位于 
		- me.masonic.datamining.Qiyi.IndexSpider
- http://top.iqiyi.com/dianshiju.html
	- 爱奇艺电视剧风云榜
	- 利用正则匹配页面上类似http://www.iqiyi.com/a_.*html的
	- 并爬取对应的播放量
	- 程序文件位于 
		- me.masonic.datamining.Qiyi.TopSpider
- 杂项
	- me.masonic.datamining.Utility.JsonToCsv
	- 将取得的爱奇艺指数数据转为Csv
	- me.masonic.datamining.Utility.SqlUtility
	- 将取得的爱奇艺指数数据存入Mysql数据库

