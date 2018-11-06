# mpen-api
mpen web api

# run it in local
  - `mvn spring-boot:run`
  - open url http://localhost:8680/async in browser
  
# Genarate project files(Eclipse)

   run `mvn eclipse:eclipse`
   对于外网，需要做以下的事情：
   ## pom.xml 文件里面把 <repositories> 这个部分删除
   ## 运行以上命令
   ## 把 jai_codec-1.1.3.jar whatycache-3.2.0-beta.jar jai-imageio-1.1.jar 这几个文件加入
   按照 mvn 提示用 mvn install:install-file 命令加入
   
# run unit test
   run `mvn test`
  
# checkstyle before checkin the code
   run `mvn checkstyle:checkstyle`
   
# swagger ui
http://127.0.0.1:8680/swagger-ui.html
