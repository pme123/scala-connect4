package pme.connect4.domain

import pme.connect4.domain.GameConfig._
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
      assert(bestSlot === 1)
    }

  }

  feature("Evaluate the points for the Horizontal Combinator") {
    scenario("First column on a new Game.") {
      Given("A new game")
      val game: Game = Game(cols, rows)
      When("Evaluate the points for the first Slot(0)")
      val slotPoints = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).horWin.eval
      Then("It should deliver 4 times the pointsForHorSpace and one border")
      assert(slotPoints === 4*pointsForHorSpace + 1 )
    }
    scenario("Last column on a new Game.") {
      Given("A new game")
      val game: Game = Game(cols, rows)
      When("Evaluate the points for the first Slot(cols-1)")
      val slotPoints = new AllCombinations(game, RedChip, game.findFirstEmpty(cols-1).get).horWin.eval
      Then("It should deliver 4 times the pointsForHorSpace and 1 border")
      assert(slotPoints === 4*pointsForHorSpace + 1)
    }
    scenario("3. column on a new Game.") {
      Given("A new game")
      val game: Game = Game(cols, rows)
      When("Evaluate the points for the first Slot(2)")
      val slotPoints = new AllCombinations(game, RedChip, game.findFirstEmpty(2).get).horWin.eval
      Then("It should deliver (2+4) times the pointsForHorSpace and 2 borders")
      assert(slotPoints === 4*pointsForHorSpace +2 )
    }

    scenario("First column on a started Game with own Chip.") {
      Given("A new game with one same Chip")
      val game: Game = Game(cols, rows)
      game.dropChip(3, RedChip)
      When("Evaluate the points for the first Slot(0)")
      val slotPoints = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).horWin.eval
      Then("It should deliver 3 times the pointsForHorSpace and 1 pointsForHorMatch and 1 border")
      assert(slotPoints === 3*pointsForHorSpace+1*pointsForHorMatch + 1 )
    }

    scenario("First column on a started Game with another Chip.") {
      Given("A new game with one same Chip")
      val game: Game = Game(cols, rows)
      game.dropChip(3, YellowChip)
      When("Evaluate the points for the first Slot(0)")
      val slotPoints = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).horWin.eval
      Then("It should deliver 0 (not four slots in a row")
      assert(slotPoints === 0 )
    }
    scenario("Last column on a started Game with another Chip.") {
      Given("A new game with one same Chip")
      val game: Game = Game(cols, rows)
      game.dropChip(4, YellowChip)
      When("Evaluate the points for the first Slot(3)")
      val slotPoints = new AllCombinations(game, RedChip, game.findFirstEmpty(3).get).horWin.eval
      Then("It should deliver 4 times the pointsForHorSpace")
      assert(slotPoints === 4*pointsForHorSpace )
    }

    scenario("Space is not in the same row.") {
      Given("A game with some set in the first row")
      val game: Game = Game(cols, rows)
      for (i <- 0 to 1) game.dropChip(i, YellowChip)
      When("Evaluate the points for the first Slot(0)")
      val slotPoints = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).horWin.eval
      Then("It should deliver 2 * pointsForHorSpace")
      assert(slotPoints === 2*pointsForHorSpace )
    }
    scenario("Space is between 2 other Chips.") {
      Given("A game with 2 other Chips.")
      val game: Game = Game(cols, rows)
      game.dropChip(0, YellowChip)
      game.dropChip(2, YellowChip)
      When("Evaluate the points for the first Slot(1)")
      val slotPoints = new AllCombinations(game, RedChip, game.findFirstEmpty(1).get).horWin.eval
      Then("It should deliver 0")
      assert(slotPoints === 0 )
    }
    scenario("Space is between 2 Chips. If only one left.") {
      Given("A game with 2 Chips and another Chip.")
      val game: Game = Game(cols, rows)
      game.dropChip(0, RedChip)
      game.dropChip(1, YellowChip)
      game.dropChip(3, YellowChip)
      game.dropChip(4, RedChip)
      When("Evaluate the points for the first Slot(2)")
      val slotPoints = new AllCombinations(game, YellowChip, game.findFirstEmpty(2).get).horWin.eval
      Then("It should deliver 0")
      assert(slotPoints === 0 )
    }
  }

  feature("Evaluate the points for the Horizontal defence Combinator") {
    scenario("1. col on a new Game.") {
      Given("A new game")
      val game: Game = Game(cols, rows)
      When("Evaluate the points for the first Slot(0)")
      val slotPoints = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).horDefence.eval
      Then("It should deliver 0")
      assert(slotPoints === 0)
    }
    scenario("3. col on a new Game.") {
      Given("A game with 2 other Chips")
      val game: Game = Game(cols, rows)
      for (i <- 0 to 1) game.dropChip(i, YellowChip)
      When("Evaluate the points for the first Slot(2)")
      val slotPoints = new AllCombinations(game, RedChip, game.findFirstEmpty(2).get).horDefence.eval
      Then("It should deliver 0")
      assert(slotPoints === 0)
    }
    scenario("3. col on a new Game with SpaceChips on either side.") {
      Given("A game with 2 other Chips")
      val game: Game = Game(cols, rows)
      for (i <- 1 to 2) game.dropChip(i, YellowChip)
      When("Evaluate the points for the first Slot(3)")
      val slotPoints = new AllCombinations(game, RedChip, game.findFirstEmpty(3).get).horDefence.eval
      Then("It should deliver max. points")
      assert(slotPoints === pointsMax*2)
    }
    scenario("4. col on a Game.") {
      Given("A game with 3 other Chips")
      val game: Game = Game(cols, rows)
      for (i <- 0 to 2) game.dropChip(i, YellowChip)
      When("Evaluate the points for the first Slot(3)")
      val slotPoints = new AllCombinations(game, RedChip, game.findFirstEmpty(3).get).horDefence.eval
      Then("It should deliver max. points *3")
      assert(slotPoints === pointsMax*3)
    }

    scenario("4. col on a Game (between).") {
      Given("A game with 3 other Chips")
      val game: Game = Game(cols, rows)
      game.dropChip(3, YellowChip)
      for (i <- 0 to 1) game.dropChip(i, YellowChip)
      When("Evaluate the points for the first Slot(2)")
      val slotPoints = new AllCombinations(game, RedChip, game.findFirstEmpty(2).get).horDefence.eval
      Then("It should deliver max. points")
      assert(slotPoints === pointsMax*3)
    }
    scenario("4. col on a Game next to.") {
      Given("A game with 3 other Chips")
      val game: Game = Game(cols, rows)
      game.dropChip(0, YellowChip)
      for (i <- 2 to 3) game.dropChip(i, YellowChip)
      When("Evaluate the points for the Slot(4)")
      val slotPoints = new AllCombinations(game, RedChip, game.findFirstEmpty(4).get).horDefence.eval
      Then("It should deliver pointsMax*2")
      assert(slotPoints === pointsMax*2)
    }
    scenario("5. col on a Game next to.") {
      Given("A game with 3 other Chips")
      val game: Game = Game(cols, rows)
      game.dropChip(4, YellowChip)
      for (i <- 1 to 2) game.dropChip(i, YellowChip)
      When("Evaluate the points for the Slot(5)")
      val slotPoints = new AllCombinations(game, RedChip, game.findFirstEmpty(5).get).horDefence.eval
      Then("It should deliver 0")
      assert(slotPoints === 0)
    }
    scenario("Game with 2 other Chips - Space next to Space that is needed.") {
      Given("A game with 2 other Chips")
      val game: Game = Game(cols, rows)
      game.dropChip(0, RedChip)
      for (i <- 3 to 4) game.dropChip(i, YellowChip)
      When("Evaluate the points for the Slot(1)")
      val slotPoints = new AllCombinations(game, RedChip, game.findFirstEmpty(1).get).horDefence.eval
      Then("It should deliver 0")
      assert(slotPoints === 0)
    }
    scenario("Game with 2 other Chips - our Chip on the other side.") {
      Given("A game with 2 other Chips")
      val game: Game = Game(cols, rows)
      game.dropChip(2, RedChip)
      for (i <- 3 to 4) game.dropChip(i, YellowChip)
      When("Evaluate the points for the Slot(5)")
      val slotPoints = new AllCombinations(game, RedChip, game.findFirstEmpty(5).get).horDefence.eval
      Then("It should deliver 0")
      assert(slotPoints === 0)
    }
  }

  feature("Evaluate the points for the Vertical Combinator") {
    scenario("First row on a new Game.") {
      Given("A new game")
      val game: Game = Game(cols, rows)
      When("Evaluate the points for the first Slot(0)")
      val slotPoints = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).vertComb.eval
      Then("It should deliver 0")
      assert(slotPoints === 0)
    }
    scenario("Third row on a new Game.") {
      Given("A game with 3 other Chips")
      val game: Game = Game(cols, rows)
      for (i <- 0 to 2) game.dropChip(0, RedChip)
      When("Evaluate the points for the first Slot(0)")
      val slotPoints = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).vertComb.eval
      Then("It should deliver 3*pointsForVerMatch")
      assert(slotPoints === 3*pointsForVerMatch)
    }
  }

  feature("Evaluate the points for the Vertical defence Combinator") {
    scenario("First row on a new Game.") {
      Given("A new game")
      val game: Game = Game(cols, rows)
      When("Evaluate the points for the first Slot(0)")
      val slotPoints = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).vertDefence.eval
      Then("It should deliver 0")
      assert(slotPoints === 0)
    }
    scenario("Third row on a new Game.") {
      Given("A game with 2 other Chips")
      val game: Game = Game(cols, rows)
      for (i <- 0 until 2) game.dropChip(0, YellowChip)
      When("Evaluate the points for the first Slot(0)")
      val slotPoints = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).vertDefence.eval
      Then("It should deliver 0")
      assert(slotPoints === 0)
    }
    scenario("Fourth row on a new Game.") {
      Given("A game with 3 other Chips")
      val game: Game = Game(cols, rows)
      for (i <- 0 until 3) game.dropChip(0, YellowChip)
      When("Evaluate the points for the first Slot(0)")
      val slotPoints = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).vertDefence.eval
      Then("It should deliver (winningChips-1)* max. points")
      assert(slotPoints === (winningChips-1)*pointsMax)
    }
  }
}
