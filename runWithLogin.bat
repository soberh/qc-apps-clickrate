@rem ����˵������һ������Ϊurl����2������Ϊ�ظ���������3������Ϊ�ظ���ʱ����(Ĭ��5�룬����5ms��5s��5m��5h)
@rem ����java -jar target/qc-apps-clickrate-1.0.jar http://localhost:8082/logout.do 2 2
java -jar target/qc-apps-clickrate-1.0.jar "http://localhost:8082/login.do,name=dragon&password=password;http://localhost:8082/debug.do" 2 2
