import _root_.io.gatling.core.scenario.Simulation
import ch.qos.logback.classic.{Level, LoggerContext}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.slf4j.LoggerFactory

import scala.concurrent.duration._

/**
 * Performance test for the SipPeer entity.
 */
class SipPeerGatlingTest extends Simulation {

    val context: LoggerContext = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]
    // Log all HTTP requests
    //context.getLogger("io.gatling.http").setLevel(Level.valueOf("TRACE"))
    // Log failed HTTP requests
    //context.getLogger("io.gatling.http").setLevel(Level.valueOf("DEBUG"))

    val baseURL = Option(System.getProperty("baseURL")) getOrElse """http://localhost:8080"""

    val httpConf = http
        .baseURL(baseURL)
        .inferHtmlResources()
        .acceptHeader("*/*")
        .acceptEncodingHeader("gzip, deflate")
        .acceptLanguageHeader("fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3")
        .connectionHeader("keep-alive")
        .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:33.0) Gecko/20100101 Firefox/33.0")
        .silentResources // Silence all resources like css or css so they don't clutter the results

    val headers_http = Map(
        "Accept" -> """application/json"""
    )

    val headers_http_authentication = Map(
        "Content-Type" -> """application/json""",
        "Accept" -> """application/json"""
    )

    val headers_http_authenticated = Map(
        "Accept" -> """application/json""",
        "Authorization" -> "${access_token}"
    )

    val scn = scenario("Test the SipPeer entity")
        .exec(http("First unauthenticated request")
        .get("/api/account")
        .headers(headers_http)
        .check(status.is(401))
        ).exitHereIfFailed
        .pause(10)
        .exec(http("Authentication")
        .post("/api/authenticate")
        .headers(headers_http_authentication)
        .body(StringBody("""{"username":"admin", "password":"admin"}""")).asJSON
        .check(header.get("Authorization").saveAs("access_token"))).exitHereIfFailed
        .pause(2)
        .exec(http("Authenticated request")
        .get("/api/account")
        .headers(headers_http_authenticated)
        .check(status.is(200)))
        .pause(10)
        .repeat(2) {
            exec(http("Get all sipPeers")
            .get("/api/sip-peers")
            .headers(headers_http_authenticated)
            .check(status.is(200)))
            .pause(10 seconds, 20 seconds)
            .exec(http("Create new sipPeer")
            .post("/api/sip-peers")
            .headers(headers_http_authenticated)
            .body(StringBody("""{
                "id":null
                , "name":"SAMPLE_TEXT"
                , "host":"SAMPLE_TEXT"
                , "nat":"SAMPLE_TEXT"
                , "type":"SAMPLE_TEXT"
                , "accountcode":"SAMPLE_TEXT"
                , "amaflags":"SAMPLE_TEXT"
                , "calllimit":"0"
                , "callgroup":"SAMPLE_TEXT"
                , "callerid":"SAMPLE_TEXT"
                , "cancallforward":"SAMPLE_TEXT"
                , "canreinvite":"SAMPLE_TEXT"
                , "context":"SAMPLE_TEXT"
                , "defaultip":"SAMPLE_TEXT"
                , "dtmfmode":"SAMPLE_TEXT"
                , "fromuser":"SAMPLE_TEXT"
                , "fromdomain":"SAMPLE_TEXT"
                , "insecure":"SAMPLE_TEXT"
                , "language":"SAMPLE_TEXT"
                , "mailbox":"SAMPLE_TEXT"
                , "md5secret":"SAMPLE_TEXT"
                , "deny":"SAMPLE_TEXT"
                , "permit":"SAMPLE_TEXT"
                , "mask":"SAMPLE_TEXT"
                , "musiconhold":"SAMPLE_TEXT"
                , "pickupgroup":"SAMPLE_TEXT"
                , "qualify":"SAMPLE_TEXT"
                , "regexten":"SAMPLE_TEXT"
                , "restrictcid":"SAMPLE_TEXT"
                , "rtptimeout":"SAMPLE_TEXT"
                , "rtpholdtimeout":"SAMPLE_TEXT"
                , "secret":"SAMPLE_TEXT"
                , "setvar":"SAMPLE_TEXT"
                , "disallow":"SAMPLE_TEXT"
                , "allow":"SAMPLE_TEXT"
                , "fullcontact":"SAMPLE_TEXT"
                , "ipaddr":"SAMPLE_TEXT"
                , "port":"0"
                , "regserver":"SAMPLE_TEXT"
                , "regseconds":"0"
                , "lastms":"0"
                , "username":"SAMPLE_TEXT"
                , "defaultuser":"SAMPLE_TEXT"
                , "subscribecontext":"SAMPLE_TEXT"
                , "useragent":"SAMPLE_TEXT"
                }""")).asJSON
            .check(status.is(201))
            .check(headerRegex("Location", "(.*)").saveAs("new_sipPeer_url"))).exitHereIfFailed
            .pause(10)
            .repeat(5) {
                exec(http("Get created sipPeer")
                .get("${new_sipPeer_url}")
                .headers(headers_http_authenticated))
                .pause(10)
            }
            .exec(http("Delete created sipPeer")
            .delete("${new_sipPeer_url}")
            .headers(headers_http_authenticated))
            .pause(10)
        }

    val users = scenario("Users").exec(scn)

    setUp(
        users.inject(rampUsers(Integer.getInteger("users", 100)) over (Integer.getInteger("ramp", 1) minutes))
    ).protocols(httpConf)
}
