package pme.connect4.domain

import pme.connect4.util.FeatureTester

class CombinationsTest extends FeatureTester {
import Combinations._
  val cols = 7
  val rows = 4
  feature("Evaluate the best move") {
    scenario("The best move on a new Game.") {
      Given("A new game")

      val game: Game = Game(cols, rows)
      When("Evaluate the best move")
      val bestSlot = evalBestMove(game, RedChip)
      Then("It should deliver the one in the middle (3)")
      assert(bestSlot === 3)
    }

  }

  feature("Evaluate the points for the Horizontal Combinator") {
    scenario("First column on a new Game.") {
      Given("A new game")
      val game: Game = Game(cols, rows)
      When("Evaluate the points for the first Slot(0)")
      val slotPoints = horWin.eval(game, RedChip, game.findFirstEmpty(0).get)
      Then("It should deliver 4 times the pointsForHorSpace")
      assert(slotPoints === 4*pointsForHorSpace )
    }
    scenario("Last column on a new Game.") {
      Given("A new game")
      val game: Game = Game(cols, rows)
      When("Evaluate the points for the first Slot(6)")
      val slotPoints = horWin.eval(game, RedChip, game.findFirstEmpty(6).get)
      Then("It should deliver 4 times the pointsForHorSpace")
      assert(slotPoints === 4*pointsForHorSpace )
    }
    scenario("3. column on a new Game.") {
      Given("A new game")
      val game: Game = Game(cols, rows)
      When("Evaluate the points for the first Slot(2)")
      val slotPoints = horWin.eval(game, RedChip, game.findFirstEmpty(2).get)
      Then("It should deliver (2+4) times the pointsForHorSpace")
      assert(slotPoints === (2+4)*pointsForHorSpace )
    }

    scenario("First column on a started Game with own Chip.") {
      Given("A new game with one same Chip")
      val game: Game = Game(cols, rows)
      game.dropChip(3, RedChip)
      When("Evaluate the points for the first Slot(0)")
      val slotPoints = horWin.eval(game, RedChip, game.findFirstEmpty(0).get)
      Then("It should deliver 3 times the pointsForHorSpace and 1 pointsForHorMatch")
      assert(slotPoints === 3*pointsForHorSpace+1*pointsForHorMatch )
    }

    scenario("First column on a started Game with another Chip.") {
      Given("A new game with one same Chip")
      val game: Game = Game(cols, rows)
      game.dropChip(3, YellowChip)
      When("Evaluate the points for the first Slot(0)")
      val slotPoints = horWin.eval(game, RedChip, game.findFirstEmpty(0).get)
      Then("It should deliver 0 (not four slots in a row")
      assert(slotPoints === 0 )
    }
    scenario("Last column on a started Game with another Chip.") {
      Given("A new game with one same Chip")
      val game: Game = Game(cols, rows)
      game.dropChip(4, YellowChip)
      When("Evaluate the points for the first Slot(3)")
      val slotPoints = horWin.eval(game, RedChip, game.findFirstEmpty(3).get)
      Then("It should deliver 4 times the pointsForHorSpace")
      assert(slotPoints === 4*pointsForHorSpace )
    }

    scenario("Space is not in the same row.") {
      Given("A game with some set in the first row")
      val game: Game = Game(cols, rows)
      for (i <- 0 until 2) game.dropChip(i, YellowChip)
      When("Evaluate the points for the first Slot(0)")
      val slotPoints = horWin.eval(game, RedChip, game.findFirstEmpty(0).get)
      Then("It should deliver 3 for pointsForHorSpace (2 + 2*0.5)")
      assert(slotPoints === 3*pointsForHorSpace )
    }
    scenario("Space is between 2 other Chips.") {
      Given("A game with 2 other Chips.")
      val game: Game = Game(cols, rows)
      game.dropChip(0, YellowChip)
      game.dropChip(2, YellowChip)
      When("Evaluate the points for the first Slot(1)")
      val slotPoints = horWin.eval(game, RedChip, game.findFirstEmpty(1).get)
      Then("It should deliver 0")
      assert(slotPoints === 0 )
    }
  }

  feature("Evaluate the points for the Horizontal defence Combinator") {
    scenario("First row on a new Game.") {
      Given("A new game")
      val game: Game = Game(cols, rows)
      When("Evaluate the points for the first Slot(0)")
      val slotPoints = horDefence.eval(game, RedChip, game.findFirstEmpty(0).get)
      Then("It should deliver 0")
      assert(slotPoints === 0)
    }
    scenario("Third row on a new Game.") {
      Given("A game with 2 other Chips")
      val game: Game = Game(cols, rows)
      for (i <- 0 to 1) game.dropChip(i, YellowChip)
      When("Evaluate the points for the first Slot(2)")
      val slotPoints = horDefence.eval(game, RedChip, game.findFirstEmpty(2).get)
      Then("It should deliver 0")
      assert(slotPoints === 0)
    }
    scenario("Third row on a new Game with SpaceChips on either side.") {
      Given("A game with 2 other Chips")
      val game: Game = Game(cols, rows)
      for (i <- 1 to 2) game.dropChip(i, YellowChip)
      When("Evaluate the points for the first Slot(0)")
      val slotPoints = horDefence.eval(game, RedChip, game.findFirstEmpty(0).get)
      Then("It should deliver max. points")
      assert(slotPoints === pointsMax)
    }
    scenario("Fourth row on a new Game.") {
      Given("A game with 3 other Chips")
      val game: Game = Game(cols, rows)
      for (i <- 1 to 3) game.dropChip(i, YellowChip)
      When("Evaluate the points for the first Slot(0)")
      val slotPoints = horDefence.eval(game, RedChip, game.findFirstEmpty(0).get)
      Then("It should deliver max. points")
      assert(slotPoints === pointsMax)
    }

  }

  feature("Evaluate the points for the Vertical defence Combinator") {
    scenario("First row on a new Game.") {
      Given("A new game")
      val game: Game = Game(cols, rows)
      When("Evaluate the points for the first Slot(0)")
      val slotPoints = vertDefence.eval(game, RedChip, game.findFirstEmpty(0).get)
      Then("It should deliver 0")
      assert(slotPoints === 0)
    }
    scenario("Third row on a new Game.") {
      Given("A game with 2 other Chips")
      val game: Game = Game(cols, rows)
      for (i <- 0 until 2) game.dropChip(0, YellowChip)
      When("Evaluate the points for the first Slot(0)")
      val slotPoints = vertDefence.eval(game, RedChip, game.findFirstEmpty(0).get)
      Then("It should deliver 0")
      assert(slotPoints === 0)
    }
    scenario("Fourth row on a new Game.") {
      Given("A game with 3 other Chips")
      val game: Game = Game(cols, rows)
      for (i <- 0 until 3) game.dropChip(0, YellowChip)
      When("Evaluate the points for the first Slot(0)")
      val slotPoints = vertDefence.eval(game, RedChip, game.findFirstEmpty(0).get)
      Then("It should deliver max. points")
      assert(slotPoints === pointsMax)
    }
  }
}
