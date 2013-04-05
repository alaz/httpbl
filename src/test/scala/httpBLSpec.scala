package com.osinka.httpbl

import org.scalatest.fixture.FunSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.prop.PropertyChecks
import org.scalacheck.Gen

/**
 * sbt -DKEY=accessKey test
 */
class httpBLSpec extends FunSpec with ShouldMatchers with PropertyChecks {
  type FixtureParam = HttpBL

  implicit override val generatorDrivenConfig = PropertyCheckConfig(maxSize = 3)

  val types   = Gen.chooseNum(0,7)
  val threats = Table("threat", 10, 20, 40, 80)
  val days    = Table("days",   10, 20, 40, 80)

  override def withFixture(test: OneArgTest) {
    System.getProperty("KEY") match {
      case null =>
        // skip test

      case accessKey =>
        val api = HttpBL(accessKey)
        withFixture(test.toNoArgTest(api))
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
