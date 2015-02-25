# Https redirect filter

A simple Play Framework filter that redirects all non-secure traffic to its secure alternative (https)

## Installing

```scala
resolvers += "Rhinofly Internal Repository" at "http://maven-repository.rhinofly.net:8081/artifactory/libs-release-local"

libraryDependencies += "nl.rhinofly" %% "play-https-redirect-filter" % "0.1"
```

*Global.scala*
```
import nl.rhinofly.httpsredirect.HttpsRedirectFilter

object Global extends WithFilters(HttpsRedirectFilter()) with GlobalSettings
```

## Configuration

**trustxforwarded**

If set to false, redirects (from anything else but 127.0.0.1) will not be trusted,
and the initial (page) redirect will not succeed, resulting in most likely a 'too many redirects' error in the browser.

```scala
// default: true
trustxforwarded = true
```


**httpsRedirectFilter.enabled**

If set to false, the filter does not do anything.

```scala
// default: false
httpsRedirectFilter.enabled = true
```

**httpsRedirectFilter.sslPort**

Port to redirect to, defaults to 443

```scala
// default: true
httpsRedirectFilter.sslPort = 443
```




## Releasing the play-https-redirect-filter plugin

Make sure you have the correct credentials present (in `~/.sbt/0.13/credentials.sbt`)

```scala
credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")
```

Then in the `sbt` console type `release`

