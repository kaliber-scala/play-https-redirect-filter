package net.kaliber.httpsRedirect

import javax.inject.Inject

import akka.stream.{ActorMaterializer, Materializer}
import play.api.Configuration
import play.api.mvc.Filter
import play.api.mvc.RequestHeader
import play.api.mvc.Result
import play.api.mvc.Results.Redirect
import HttpsRedirectFilter._
import akka.actor.ActorSystem

import scala.concurrent.Future

class HttpsRedirectFilter @Inject() (configuration: Configuration, implicit val mat: Materializer) extends Filter {

  def apply(nextFilter: RequestHeader => Future[Result])(request: RequestHeader): Future[Result] = {
    val enabled = configuration.getBoolean("httpsRedirectFilter.enabled").getOrElse(DEFAULT_ENABLED)
    val sslPort = configuration.getString("httpsRedirectFilter.sslPort").getOrElse(DEFAULT_SSL_PORT)

    if (enabled && !request.secure)
      Future successful Redirect(s"https://${request.domain}:${sslPort}${request.uri}")
    else nextFilter(request)
  }
}

object HttpsRedirectFilter {

  val DEFAULT_SSL_PORT = "443"
  val DEFAULT_ENABLED = true

  def fromConfiguration(configuration: Configuration) = {
    implicit val system = ActorSystem()
    implicit val mat = ActorMaterializer()
    new HttpsRedirectFilter(configuration, mat)
  }
}