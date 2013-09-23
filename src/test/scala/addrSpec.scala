package com.osinka.httpbl

import java.net.InetAddress
import org.scalatest.{FunSpec, Matchers}
import org.scalatest.prop.PropertyChecks
import org.scalacheck.Gen

class addrSpec extends FunSpec with Matchers with PropertyChecks {
  val apiKey = "temp"
  val api = HttpBL(apiKey)

  val octetGen = Gen.chooseNum(0, 255)
  val ipGen =
    for {a <- octetGen
         b <- octetGen
         c <- octetGen
         d <- octetGen}
    yield List(a,b,c,d)

  describe("http:BL address") {
    it("is correct") {
      forAll(ipGen) { ip =>
        val h = ip.mkString(".")
        val rev = ip.reverse.mkString(".")
        api.query(InetAddress.getByName(h)) should equal("%s.%s.%s".format(apiKey,rev,api.service))
      }
    }
  }
}
