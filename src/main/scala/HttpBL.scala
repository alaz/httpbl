package com.osinka.httpbl

import java.net.{InetAddress,UnknownHostException}
import util.control.Exception._

/**
 * @see http://www.projecthoneypot.org/httpbl_api.php
 */
class HttpBL(val accessKey: String) {
  val service = "dnsbl.httpbl.org"

  def query(ip: Seq[String]) = "%s.%s.%s".format(accessKey, ip.reverse mkString ".", service)

  def apply(ip: String): Option[HttpBL.Response] =
    apply(ip split """\.""")

  def apply(ip: Seq[String]): Option[HttpBL.Response] = {
    require(ip.length == 4)
    catching(classOf[UnknownHostException]).opt {
      val addr = InetAddress getByName query(ip)
      HttpBL.decode(addr.getHostAddress)
    }
  }
}

object HttpBL {
  val SearchEngine   = 0
  val Suspicious     = 1
  val Harvester      = 2
  val CommentSpammer = 4

  case class Response(days: Int, threat: Int, flags: Int) {
    def isSearchEngine = flags == SearchEngine
    def isSuspicious = (flags & Suspicious) != 0
    def isHarvester = (flags & Harvester) != 0
    def isCommentSpammer = (flags & CommentSpammer) != 0
  }

  def apply(accessKey: String) = new HttpBL(accessKey)

  private[httpbl] def decode(response: String) = response split """\.""" match {
    case Array("127", days, threat, flags) => Response(days.toInt, threat.toInt, flags.toInt)
    case _ => throw new UnknownResponseException(response)
  }

  class UnknownResponseException(response: String) extends Exception {
    override def getMessage = "Unknown response from http:BL service: %s" format response
  }
}

