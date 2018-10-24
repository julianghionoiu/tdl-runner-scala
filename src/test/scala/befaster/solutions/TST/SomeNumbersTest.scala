package befaster.solutions.TST

import org.scalatest.{FlatSpec, Matchers}

class SomeNumbersTest extends FlatSpec with Matchers {

  it should "show one" in {
    One.apply shouldBe 1
  }
}
