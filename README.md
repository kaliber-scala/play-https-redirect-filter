# Https redirect filter

A simple Play Framework filter that redirects all non-secure traffic to its secure alternative (https)

## Installing

```scala
resolvers += "Kaliber Repository" at "https://jars.kaliber.io/artifactory/libs-release-local"

libraryDependencies += "net.kaliber" %% "play-https-redirect-filter" % "0.4"
```

Please consult the Play Framework documentation on using filters.

## Configuration

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

