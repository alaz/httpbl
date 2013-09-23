package com.osinka.httpbl

import org.scalatest.fixture.FunSpec
import org.scalatest.{Canceled, Matchers}
import org.scalatest.prop.PropertyChecks
import org.scalacheck.Gen

/**
 * sbt -DKEY=accessKey test
 */
class apiSpec extends FunSpec with Matchers with PropertyChecks {
  type FixtureParam = HttpBL

  implicit override val generatorDrivenConfig = PropertyCheckConfig(maxSize = 3)

  val types   = Gen.chooseNum(0,7)
  val threats = Table("threat", 10, 20, 40, 80)
  val days    = Table("days",   10, 20, 40, 80)

  override def withFixture(test: OneArgTest) = {
    System.getProperty("KEY") match {
      case null => Canceled("requires API key")
      case accessKey => test(HttpBL(accessKey))
    }
  }

  describe("http:BL API") {
    it("parses `not found`") { api =>
      api("127.0.0.1") should be('empty)
    }
    it("parses types") { api =>
      forAll(types) { (i: Int) =>
        val r = api("127.1.1.%d" format i)
        r should be('defined)
        r.get should have('flags (i))
      }
    }
    it("parses threat level") { api =>
      forAll(threats) { (i: Int) =>
        val r = api("127.1.%d.1" format i)
        r should be('defined)
        r.get should have('threat (i))
      }
    }
    it("parses days") { api =>
      forAll(days) { (i: Int) =>
        val r = api("127.%d.1.1" format i)
        r should be('defined)
        r.get should have('days (i))
      }
    }
  }
}
