package nl.rhinofly.httpsRedirect

import play.api.mvc.Filter
import play.api.mvc.RequestHeader
import play.api.mvc.Result
import play.api.mvc.Results
import play.api.mvc._

import scala.concurrent.Future

object HttpsRedirectFilter extends Filter {

  lazy val isSecureOnly = play.api.Play.current.configuration.getBoolean("HttpsRedirectFilter.enabled").getOrElse(false)
  lazy val sslPort = play.api.Play.current.configuration.getString("HttpsRedirectFilter.sslPort").getOrElse("443")

  def apply(nextFilter: (RequestHeader) => Future[Result])(request: RequestHeader): Future[Result] = {

    if (isSecureOnly && !request.secure) {
      Future.successful(Results.Redirect(s"https://${request.domain}:${sslPort}${request.uri}"))
    } else {
      nextFilter(request)
    }
  }
}