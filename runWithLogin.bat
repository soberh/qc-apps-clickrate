@rem 参数说明：第一个参数为url，第2个参数为重复次数，第3个参数为重复的时间间隔(默认5秒，可设5ms、5s、5m、5h)
@rem 例：java -jar target/qc-apps-clickrate-1.0.jar http://localhost:8082/logout.do 2 2
java -jar target/qc-apps-clickrate-1.0.jar "http://localhost:8082/login.do,name=dragon&password=password;http://localhost:8082/debug.do" 2 2
