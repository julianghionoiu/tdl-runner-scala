package befaster.solutions

import org.scalatest.{FlatSpec, Matchers}

class SumTest extends FlatSpec with Matchers {

  it should "compute sum" in {
    Sum.sum(1,2) shouldBe 3
  }
}
