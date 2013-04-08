# http:BL API in Scala

"http:BL" API is similar to DNSBL, but for web traffic rather than mail traffic. The specification is [here](http://www.projecthoneypot.org/httpbl_api.php)

## Using

In SBT:

```scala
libraryDependencies += "com.osinka.httpbl" %% "httpbl" % "1.0.0"
```

In Maven:

```xml
<dependency>
  <groupId>com.osinka.httpbl</groupId>
  <artifactId>httpbl_2.10.0</artifactId>
  <version>1.0.0</version>
</dependency>
```

In your code:

```scala
import com.osinka.httpbl._

val api = HttpBL(accessKey)

val response = api("127.0.0.1")
api.isSearchEngine
api.isHarvester
api.isCommentSpammer
api.days   // how many days this IP has been seen
api.threat // threat level
```
