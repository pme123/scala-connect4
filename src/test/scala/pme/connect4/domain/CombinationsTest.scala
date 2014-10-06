package pme.connect4.domain

import pme.connect4.util.FeatureTester

class CombinationsTest extends FeatureTester {
import Combinations._

  feature("Evaluate the best move") {
    scenario("The best move on a new Game.") {
      Given("A new game")
      val cols = 7
      val rows = 4
      val game: Game = Game(cols, rows)
      When("Evaluate the best move")
      val bestSlot = evalBestMove(game, RedChip)
      Then("It should deliver the one in the middle (3)")
      assert(bestSlot === 3)
    }

  }

  feature("Evalutate the points for the Horizontal Combinator") {
    scenario("First column on a new Game.") {
      Given("A new game")
      val cols = 7
      val rows = 4
      val game: Game = Game(cols, rows)
      When("Evaluate the points for the first Slot(0)")
      val slotPoints = horizontalComb.eval(game, RedChip, game.findFirstEmpty(0).get)
      Then("It should deliver the one in the middle (3)")
      assert(slotPoints === 4*pointsForHorSpace )
    }

  }
}
