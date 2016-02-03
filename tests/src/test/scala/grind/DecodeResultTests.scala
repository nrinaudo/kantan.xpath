package grind

import grind.DecodeResult._
import org.scalatest.FunSuite
import org.scalatest.prop.GeneratorDrivenPropertyChecks

class DecodeResultTests extends FunSuite with GeneratorDrivenPropertyChecks {
  // Note: map && flatMap are not explicitly tested here, as this is taken care of when testing DecodeResult's monad
  // instance in the cats and scalaz modules.

  test("Success should be a success") {
    forAll { s: Int =>
      assert(success(s).isSuccess)
      assert(!success(s).isFailure)
    }
  }

  test("Success.toOption should be a Some") {
    forAll { s: Int =>
      assert(success(s).toOption == Some(s))
    }
  }

  test("apply on value should be a Success") {
    forAll { s: Int => assert(DecodeResult(s) == Success(s)) }
  }

  test("apply on exception should be a Failure") {
    assert(DecodeResult(sys.error("error")) == Failure)
  }

  test("apply on null should be a NotFound") {
    assert(DecodeResult(null) == NotFound)
  }

  test("get on a Success should return the expected value") {
    forAll { s: Int => assert(success(s).get == s) }
  }

  test("get on a Failure should return the expected value") {
    intercept[Exception] { failure.get }
    ()
  }

  test("get on a NotFound should return the expected value") {
    intercept[Exception] { notFound.get }
    ()
  }

  test("Failure should be a failure") {
    assert(!failure.isSuccess)
    assert(failure.isFailure)
  }

  test("Failure.toOption should be a None") {
    assert(failure.toOption == None)
  }

  test("NotFound should be a failure") {
    assert(!notFound.isSuccess)
    assert(notFound.isFailure)
  }

  test("NotFound.toOption should be a None") {
    assert(notFound.toOption == None)
  }
}
