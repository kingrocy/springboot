cd ..
mvn clean package -Dmaven.test.skip=true
scp /Users/dushaoyun/work/workspace/java/yun/springboot/springboot-dingtalk-robot/target/dingtalk-robot.jar root@118.25.172.253:/usr/local/server/deploy                                                                                                                       
