package net.kaliber.httpsRedirect

import javax.inject.Inject

import akka.stream.Materializer
import play.api.Configuration
import play.api.mvc.Filter
import play.api.mvc.RequestHeader
import play.api.mvc.Result
import play.api.mvc.Results.Redirect
import HttpsRedirectFilter._
import akka.actor.ActorSystem

import scala.concurrent.Future

class HttpsRedirectFilter(
  enabled: Boolean = HttpsRedirectFilter.DEFAULT_ENABLED,
  sslPort: String = HttpsRedirectFilter.DEFAULT_SSL_PORT
)(implicit val mat: Materializer) extends Filter {

  @Inject
  def this(configuration: Configuration)(implicit mat: Materializer) =
    this(
      configuration.getBoolean("httpsRedirectFilter.enabled").getOrElse(DEFAULT_ENABLED),
      configuration.getString("httpsRedirectFilter.sslPort").getOrElse(DEFAULT_SSL_PORT)
    )

  def apply(nextFilter: RequestHeader => Future[Result])(request: RequestHeader): Future[Result] =
    if (enabled && !request.secure)
      Future successful Redirect(s"https://${request.domain}:${sslPort}${request.uri}")
    else nextFilter(request)
}

object HttpsRedirectFilter {

  val DEFAULT_SSL_PORT = "443"
  val DEFAULT_ENABLED = true

  def fromConfiguration(configuration: Configuration)(implicit mat: Materializer) =
    new HttpsRedirectFilter(configuration)
}
