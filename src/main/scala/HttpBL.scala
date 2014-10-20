package com.osinka.httpbl

import java.net.{InetAddress,UnknownHostException}
import util.control.Exception._

/**
 * @see http://www.projecthoneypot.org/httpbl_api.php
 */
class HttpBL(val accessKey: String) {
  val service = "dnsbl.httpbl.org"

  def query(ip: InetAddress) = {
    val bytes = ip.getAddress
    def octet(n: Int) = bytes(n) & 0xFF
    "%s.%d.%d.%d.%d.%s".format(accessKey, octet(3), octet(2), octet(1), octet(0), service)
  }

  def apply(ip: String): Option[HttpBL.Response] = apply(InetAddress getByName ip)

  def apply(ip: Array[Byte]): Option[HttpBL.Response] = apply(InetAddress getByAddress ip)

  def apply(ip: InetAddress): Option[HttpBL.Response] =
    catching(classOf[UnknownHostException]).opt {
      val addr = InetAddress getByName query(ip)
      HttpBL.decode(addr.getHostAddress)
    }
}

object Types {
  val SearchEngine   = 0
  val Suspicious     = 1
  val Harvester      = 2
  val CommentSpammer = 4
}

object SearchEngines {
  val Undocumented   = 0
  val AltaVista      = 1
  val Ask            = 2
  val Baidu          = 3
  val Excite         = 4
  val Google         = 5
  val Looksmart      = 6
  val Lycos          = 7
  val MSN            = 8
  val Yahoo          = 9
  val Cuil           = 10
  val InfoSeek       = 11
  val Miscellaneous  = 12
}

object HttpBL {
  import Types._

  trait Response {
    def flags: Int

    def isSearchEngine = flags == Types.SearchEngine
  }

  case class SearchEngine(serial: Int, flags: Int) extends Response

  case class Result(days: Int, threat: Int, flags: Int) extends Response {
    def isSuspicious = (flags & Suspicious) != 0
    def isHarvester = (flags & Harvester) != 0
    def isCommentSpammer = (flags & CommentSpammer) != 0
  }

  def apply(accessKey: String) = new HttpBL(accessKey)

  private[httpbl] def decode(response: String) = response split """\.""" match {
    case Array("127", _, serial, "0") => SearchEngine(serial.toInt, 0)
    case Array("127", days, threat, flags) => Result(days.toInt, threat.toInt, flags.toInt)
    case _ => throw new UnknownResponseException(response)
  }

  class UnknownResponseException(response: String) extends Exception {
    override def getMessage = "Unknown response from http:BL service: %s" format response
  }
}

