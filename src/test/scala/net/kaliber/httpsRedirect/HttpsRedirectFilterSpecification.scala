package net.kaliber.httpsRedirect

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.qirx.littlespec.Specification
import play.api.Configuration
import play.api.http.HeaderNames
import play.api.mvc.AnyContentAsEmpty
import play.api.mvc.Filter
import play.api.mvc.RequestHeader
import play.api.mvc.Results.NoContent
import play.api.test.FakeApplication
import play.api.test.FakeHeaders
import play.api.test.FakeRequest
import play.api.test.Helpers
import HttpsRedirectFilter._
import play.api.inject.guice.GuiceApplicationBuilder

import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.duration._

object HttpsRedirectFilterSpecification extends Specification {

  val host = "test.com"
  val sslPort = "1234"

  "Tests using play configuration" - {
    def createFilter(config: Map[String, Any]) = {
      val app = {
        new GuiceApplicationBuilder()
        .configure(Configuration.from(config))
        .build()
      }
      app.injector.instanceOf[HttpsRedirectFilter]
    }

    val filter = createFilter(Map(
      "httpsRedirectFilter.enabled" -> true,
      "httpsRedirectFilter.sslPort" -> sslPort
    ))

    "redirect non-secure requests" - {
      val result = await(filtered(filter, secure = false)).header

      result.status is 303
      result.headers(HeaderNames.LOCATION) is s"https://$host:$sslPort/"
    }

    "pass through secure requests" - {
      val result = await(filtered(filter, secure = true))

      result is NoContent
     }

    "defaults" - {
      val filter = createFilter(Map.empty)

      "redirect non-secure requests" - {
        val result = await(filtered(filter, secure = false)).header

        result.status is 303
        result.headers(HeaderNames.LOCATION) is s"https://$host:$DEFAULT_SSL_PORT/"
      }

      "pass through secure requests" - {
        val result = await(filtered(filter, secure = true))

        result is NoContent
      }
    }
  }

  "Tests with manual config" - {
    implicit val system = ActorSystem()
    implicit val mat = ActorMaterializer()

    "block with default config" - {
      val filter = new HttpsRedirectFilter()
      val filtered = filter(nextFilter(_))(testRequest(secure = false))

      val result = await(filtered).header

      result.status is 303
      result.headers(HeaderNames.LOCATION) is s"https://$host:$DEFAULT_SSL_PORT/"
    }

    "pass through when disabled" - {
      val filter = new HttpsRedirectFilter(enabled = false, sslPort)
      val result = await(filtered(filter, secure = false))

      result is NoContent
    }

    "block with correct ssl port" - {
      val filter = new HttpsRedirectFilter(enabled = true, sslPort)
      val result = await(filtered(filter, secure = false)).header

      result.status is 303
      result.headers(HeaderNames.LOCATION) is s"https://$host:$sslPort/"
    }
  }

  private def filtered(filter: Filter, secure: Boolean) =
    filter(nextFilter(_))(testRequest(secure = secure))

  private def await[T](f: Future[T]): T = Await.result(f, 1.seconds)

  private def nextFilter(r: RequestHeader) = Future successful NoContent

  private def testRequest(secure: Boolean) =
    new FakeRequest("GET", "/", FakeHeaders(), AnyContentAsEmpty, secure = secure)
      .withHeaders(HeaderNames.HOST -> host)
}
