package befaster

import java.util

import befaster.solutions.ARRS.ArraySum
import befaster.solutions.CHK.Checkout
import befaster.solutions.CHL.Checklite
import befaster.solutions.FIZ.FizzBuzz
import befaster.solutions.HLO.Hello
import befaster.solutions.IRNG.IntRange
import befaster.solutions.SUM.Sum
import com.google.gson.JsonElement

import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer

//noinspection ZeroIndexToHead
object EntryPointMapping {

  def sum(p: util.List[JsonElement]): AnyRef = Sum.sum(p.get(0).getAsInt, p.get(1).getAsInt).asInstanceOf[AnyRef]
  def hello(p: util.List[JsonElement]): AnyRef = Hello.hello(p.get(0).getAsString).asInstanceOf[AnyRef]
  def array_sum(p: util.List[JsonElement]): AnyRef = {
    var intArray = new ListBuffer[Int]()
    p.get(0).getAsJsonArray.forEach(jsonElement => intArray += jsonElement.getAsInt)
    ArraySum.sum(intArray.toList).asInstanceOf[AnyRef]
  }
  def int_range(p: util.List[JsonElement]): AnyRef = {
    IntRange.generate(p.get(0).getAsInt, p.get(1).getAsInt).asJava.asInstanceOf[AnyRef]
  }
  def fizz_buzz(p: util.List[JsonElement]): AnyRef = FizzBuzz.fizzBuzz(p.get(0).getAsInt).asInstanceOf[AnyRef]
  def checkout(p: util.List[JsonElement]): AnyRef = Checkout.checkout(p.get(0).getAsString).asInstanceOf[AnyRef]
  def checklite(p: util.List[JsonElement]): AnyRef = Checklite.checklite(p.get(0).getAsString).asInstanceOf[AnyRef]

}
