this is a gui application for repeat visit url.
after packaged,just run qc-apps-clickrate-2.0.jar,you will see the gui window what you want.


release win32 x86 package:(test ok in winXP)
  >mvn clean assembly:assembly -Dproject.os=win32

release gtk linux x86 package:(not test yet)
  >mvn clean assembly:assembly -Dproject.os=linux-gtk

release mac os x package:(not test yet)
  >mvn clean assembly:assembly -Dproject.os=macosx


if want to run in console, see below:
  >java -classpath qc-apps-clickrate-2.0.jar qc.apps.clickrate.ConsoleMain {url} {repeatCount} {interval}
    {url} -- the url to repeat visit
    {repeatCount} -- number of replications
    {interval} -- interval (default is second) of each replication. can use 5ms 5s 5m or 5h to mark a time unit.
    etc:>java -classpath qc-apps-clickrate-2.0.jar qc.apps.clickrate.ConsoleMain "http://opendragon2010.blog.163.com/blog/static/1775342832010113114815909/" 2 2
    
if want to run in console with login first, see below:
  >java -classpath qc-apps-clickrate-2.0.jar qc.apps.clickrate.ConsoleMain {lognUrl,loginParams;url} {repeatCount} {interval}
    {lognUrl} -- the url to login at first.
    {loginParams} -- the login data,format like 'key1=value1&key2=value2&...'.
    {url} -- the url to repeat visit
    {repeatCount} -- @see above
    {interval} -- @see above
    etc:>java -classpath qc-apps-clickrate-2.0.jar qc.apps.clickrate.ConsoleMain "http://my.domain.com/login,name=myName&password=myPassword;http://opendragon2010.blog.163.com/blog/static/1775342832010113114815909/" 2 2
