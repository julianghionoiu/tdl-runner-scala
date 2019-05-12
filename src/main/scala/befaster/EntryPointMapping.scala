package befaster

import befaster.solutions.ARRS.ArraySum
import befaster.solutions.CHK.Checkout
import befaster.solutions.FIZ.FizzBuzz
import befaster.solutions.HLO.Hello
import befaster.solutions.IRNG.IntRange
import befaster.solutions.SUM.Sum
import com.google.gson.JsonElement

import scala.collection.JavaConverters._


import scala.collection.mutable.ListBuffer

object EntryPointMapping {

  def sum(p: Array[JsonElement]): AnyRef = Sum.sum(p(0).getAsInt, p(1).getAsInt).asInstanceOf[AnyRef]
  def hello(p: Array[JsonElement]): AnyRef = Hello.hello(p(0).getAsString).asInstanceOf[AnyRef]
  def array_sum(p: Array[JsonElement]): AnyRef = {
    var intArray = new ListBuffer[Int]()
    p(0).getAsJsonArray.forEach(jsonElement => intArray += jsonElement.getAsInt)
    ArraySum.sum(intArray.toList).asInstanceOf[AnyRef]
  }
  def int_range(p: Array[JsonElement]): AnyRef = {
    IntRange.generate(p(0).getAsInt, p(1).getAsInt).asJava.asInstanceOf[AnyRef]
  }
  def fizz_buzz(p: Array[JsonElement]): AnyRef = FizzBuzz.fizzBuzz(p(0).getAsInt).asInstanceOf[AnyRef]
  def checkout(p: Array[JsonElement]): AnyRef = Checkout.checkout(p(0).getAsString).asInstanceOf[AnyRef]

}
