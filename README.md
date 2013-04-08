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
response match {
  case Some(found) =>
    found.isSearchEngine
    found.isHarvester
    found.isCommentSpammer
    found.days   // how many days ago this IP has been seen
    found.threat // threat level
    
  case None =>
    // not found
}
```
