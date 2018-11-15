# Android-based-Hyperspectral-Image-Classification-System
系统整体架构如下：

**Android Program**：实现了系统的前端设计，基于Android，界面设计如下。
基本功能：用户登录与注册，文件上传，算法执行

**MyWeb**：系统后端设计，Tomcat+MySQL。
功能：  
1、用户登录、注册,servlet接收数据之后与数据库交互；  
2、执行文件上传HDFS操作，即在服务端提交脚本命令，如Hadoop dfs -put -from -to；  
3、执行分类算法执行操作，提交Spark脚本，如spark-submit -main Main test.jar。
