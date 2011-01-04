@rem 参数说明：第一个参数为url，第2个参数为重复次数，第3个参数为重复的时间间隔(默认5秒，可设5ms、5s、5m、5h)
@rem 例1：java -jar target/qc-apps-clickrate-1.0.jar http://localhost:8082/logout.do 2 2
@rem 例2：java -classpath target/qc-apps-clickrate-1.0.jar qc.apps.clickrate.SimpleMain http://localhost:8082/logout.do 2 2
java -jar target/qc-apps-clickrate-1.0.jar http://localhost:8082/logout.do 2 2
