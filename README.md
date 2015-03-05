# http:BL API in Scala

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.osinka.httpbl/httpbl_2.11/badge.png)](https://maven-badges.herokuapp.com/maven-central/com.osinka.httpbl/httpbl_2.11)

"http:BL" API is similar to DNSBL, but for web traffic rather than mail traffic. The specification is [here](http://www.projecthoneypot.org/httpbl_api.php)

## Using

In SBT:

```scala
libraryDependencies += "com.osinka.httpbl" %% "httpbl" % "2.0.0"
```

In your code:

```scala
import com.osinka.httpbl._

val api = HttpBL(accessKey)

val response = api("127.0.0.1")
response match {
  case Some(found @ HttpBL.Result) =>
    found.isSuspicious
    found.isHarvester
    found.isCommentSpammer
    found.days   // how many days ago this IP has been seen
    found.threat // threat level

  case Some(searchEngine @ HttpBL.SearchEngine) =>
    searchEngine.serial == SearchEngines.Google

  case None =>
    // not found
}
```
