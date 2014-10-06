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
    val cols = 7
    val rows = 4
    scenario("First column on a new Game.") {
      Given("A new game")
      val game: Game = Game(cols, rows)
      When("Evaluate the points for the first Slot(0)")
      val slotPoints = horizontalComb.eval(game, RedChip, game.findFirstEmpty(0).get)
      Then("It should deliver 4 times the pointsForHorSpace")
      assert(slotPoints === 4*pointsForHorSpace )
    }
    scenario("Last column on a new Game.") {
      Given("A new game")
      val game: Game = Game(cols, rows)
      When("Evaluate the points for the first Slot(6)")
      val slotPoints = horizontalComb.eval(game, RedChip, game.findFirstEmpty(6).get)
      Then("It should deliver 4 times the pointsForHorSpace")
      assert(slotPoints === 4*pointsForHorSpace )
    }
    scenario("3. column on a new Game.") {
      Given("A new game")
      val game: Game = Game(cols, rows)
      When("Evaluate the points for the first Slot(2)")
      val slotPoints = horizontalComb.eval(game, RedChip, game.findFirstEmpty(2).get)
      Then("It should deliver (2+4) times the pointsForHorSpace")
      assert(slotPoints === (2+4)*pointsForHorSpace )
    }

    scenario("First column on a started Game with own Chip.") {
      Given("A new game with one same Chip")
      val game: Game = Game(cols, rows)
      game.dropChip(3, RedChip)
      When("Evaluate the points for the first Slot(0)")
      val slotPoints = horizontalComb.eval(game, RedChip, game.findFirstEmpty(0).get)
      Then("It should deliver 3 times the pointsForHorSpace and 1 pointsForHorMatch")
      assert(slotPoints === 3*pointsForHorSpace+1*pointsForHorMatch )
    }

    scenario("First column on a started Game with another Chip.") {
      Given("A new game with one same Chip")
      val game: Game = Game(cols, rows)
      game.dropChip(3, YellowChip)
      When("Evaluate the points for the first Slot(0)")
      val slotPoints = horizontalComb.eval(game, RedChip, game.findFirstEmpty(0).get)
      Then("It should deliver 0 (not four slots in a row")
      assert(slotPoints === 0 )
    }
    scenario("Last column on a started Game with another Chip.") {
      Given("A new game with one same Chip")
      val game: Game = Game(cols, rows)
      game.dropChip(4, YellowChip)
      When("Evaluate the points for the first Slot(3)")
      val slotPoints = horizontalComb.eval(game, RedChip, game.findFirstEmpty(3).get)
      Then("It should deliver 4 times the pointsForHorSpace")
      assert(slotPoints === 4*pointsForHorSpace )
    }

  }
}
