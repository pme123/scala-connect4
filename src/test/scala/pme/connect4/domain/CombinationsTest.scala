package pme.connect4.domain

import pme.connect4.util.FeatureTester

class CombinationsTest extends FeatureTester {

  import pme.connect4.domain.Combinations._
  import pme.connect4.domain.GameConfig._


  // VERTICAL
  feature("Evaluate the Vertical Win") {
    scenario("1. row on a Slot. No Win.") {
      Given("A game without Chips")
      val game: Game = Game(cols, rows)
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).vertWin
      Then("It should not succed.")
      assert(!success)
    }
    scenario("(winningChips-1). row in a Slot. No Win.") {
      Given("A game with (winningChips-2) Chips")
      val game: Game = Game(cols, rows)
      for (i <- 1 until winningChips - 1) game.dropChip(0, RedChip)
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).vertWin
      Then("It should not succed.")
      assert(!success)
    }
    scenario("(winningChips). row in a Slot. Win!") {
      Given("A game with (winningChips-1) other Chips")
      val game: Game = Game(cols, rows)
      for (i <- 1 until winningChips) game.dropChip(0, RedChip)
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).vertWin
      Then("It should succed.")
      assert(success)
    }
  }

  feature("Evaluate the Vertical Lost") {
    scenario("1. row on a Slot. No Lost.") {
      Given("A game without Chips")
      val game: Game = Game(cols, rows)
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).vertLost
      Then("It should not succed.")
      assert(!success)
    }
    scenario("(winningChips-1). row in a Slot. No Lost.") {
      Given("A game with (winningChips-2) Chips")
      val game: Game = Game(cols, rows)
      for (i <- 1 until winningChips - 1) game.dropChip(0, RedChip)
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).vertLost
      Then("It should not succed.")
      assert(!success)
    }
    scenario("(winningChips). row in a Slot. Lost!") {
      Given("A game with (winningChips-1) other Chips")
      val game: Game = Game(cols, rows)
      for (i <- 1 until winningChips) game.dropChip(0, YellowChip)
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).vertLost
      Then("It should succed.")
      assert(success)
    }
  }
  // HORIZONTAL
  feature("Evaluate the Horizontal Win") {
    scenario("1. col on a row. No Win.") {
      Given("A game without Chips")
      val game: Game = Game(cols, rows)
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).horWin
      Then("It should not succed.")
      assert(!success)
    }
    scenario("(winningChips-1). col on a row. No Win.") {
      Given("A game (winningChips-2) Chips")
      val game: Game = Game(cols, rows)
      for (i <- 1 until winningChips - 1) game.dropChip(i, RedChip)
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).horWin
      Then("It should not succed.")
      assert(!success)
    }

    scenario("(winningChips). col on a row. Win!") {
      Given("A game (winningChips-1) Chips")
      val game: Game = Game(cols, rows)
      for (i <- 1 until winningChips) game.dropChip(i, RedChip)
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).horWin
      Then("It should succed.")
      assert(success)
    }
  }

  feature("Evaluate the Horizontal Lost") {
    scenario("1. col on a row. No Lost.") {
      Given("A game without Chips")
      val game: Game = Game(cols, rows)
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).horLost
      Then("It should not succed.")
      assert(!success)
    }
    scenario("(winningChips-1). col on a row. No Lost.") {
      Given("A game (winningChips-2) Chips")
      val game: Game = Game(cols, rows)
      for (i <- 1 until winningChips - 1) game.dropChip(i, RedChip)
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).horLost
      Then("It should not succed.")
      assert(!success)
    }

    scenario("(winningChips). col on a row. Lost!") {
      Given("A game (winningChips-1) Chips")
      val game: Game = Game(cols, rows)
      for (i <- 1 until winningChips) game.dropChip(i, YellowChip)
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).horLost
      Then("It should succed.")
      assert(success)
    }
  }

  // Diagonal up (from left-down to right-up)
  feature("Evaluate the Diagonal up Win") {
    scenario("1. col on a row. No Win.") {
      Given("A game without Chips")
      val game: Game = Game(cols, rows)
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).diagUpWin
      Then("It should not succed.")
      assert(!success)
    }
    scenario("(winningChips-1). col and row. No Win.") {
      Given("A game (winningChips-2) Chips")
      val game: Game = Game(cols, rows)
      for (i <- 1 until winningChips - 1) {
        for (i <- 1 until winningChips - 1) game.dropChip(i, YellowChip)
        game.dropChip(i, RedChip)
      }
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).diagUpWin
      Then("It should not succed.")
      assert(!success)
    }

    scenario("(winningChips). col and row but without a Chip below. No Win!") {
      Given("A game (winningChips-1) Chips")
      val game: Game = Game(cols, rows)
      for (i <- 1 until winningChips) {
        game.dropChip(i-1, RedChip)
        for (i <- 1 until winningChips-1) game.dropChip(i, YellowChip)

      }
      When("Evaluate the points for the Slot(winningChips)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(winningChips-1).get).diagUpWin
      Then("It should Not succed.")
      assert(!success)
    }
    scenario("(winningChips). col and row with a Chip below. Win!") {
      Given("A game (winningChips-1) Chips")
      val game: Game = Game(cols, rows)
      for (i <- 1 until winningChips) {
        for (i <- 1 until winningChips) game.dropChip(i, YellowChip)
        game.dropChip(i, RedChip)
      }
      When("Evaluate the points for the Slot(winningChips)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(winningChips).get).diagUpWin
      Then("It should NOT succed.")
      assert(!success)
    }
    scenario("(winningChips). col and row. Win!") {
      Given("A game (winningChips-1) Chips")
      val game: Game = Game(cols, rows)
      for (i <- 1 until winningChips) {
        for (i <- 1 until winningChips) game.dropChip(i, YellowChip)
        game.dropChip(i, RedChip)
      }
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).diagUpWin
      Then("It should succed.")
      assert(success)
    }
  }

  feature("Evaluate the Diagonal up Lost") {
    scenario("1. col on a row. No Lost.") {
      Given("A game without Chips")
      val game: Game = Game(cols, rows)
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, YellowChip, game.findFirstEmpty(0).get).diagUpLost
      Then("It should not succed.")
      assert(!success)
    }
    scenario("(winningChips-1). col and row. No Lost.") {
      Given("A game (winningChips-2) Chips")
      val game: Game = Game(cols, rows)
      for (i <- 1 until winningChips - 1) {
        for (i <- 1 until winningChips - 1) game.dropChip(i, YellowChip)
        game.dropChip(i, RedChip)
      }
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, YellowChip, game.findFirstEmpty(0).get).diagUpLost
      Then("It should not succed.")
      assert(!success)
    }

    scenario("(winningChips). col and row but without a Chip below. No Lost!") {
      Given("A game (winningChips-1) Chips")
      val game: Game = Game(cols, rows)
      for (i <- 1 until winningChips) {
        game.dropChip(i-1, RedChip)
        for (i <- 1 until winningChips-1) game.dropChip(i, YellowChip)

      }
      When("Evaluate the points for the Slot(winningChips)")
      val success = new AllCombinations(game, YellowChip, game.findFirstEmpty(winningChips-1).get).diagUpLost
      Then("It should NOT succed.")
      assert(!success)
    }
    scenario("(winningChips). col and row with a Chip below. Lost!") {
      Given("A game (winningChips-1) Chips")
      val game: Game = Game(cols, rows)
      for (i <- 1 until winningChips) {
        for (i <- 1 until winningChips) game.dropChip(i, YellowChip)
        game.dropChip(i, RedChip)
      }
      When("Evaluate the points for the Slot(winningChips)")
      val success = new AllCombinations(game, YellowChip, game.findFirstEmpty(winningChips).get).diagUpLost
      Then("It should NOT succed.")
      assert(!success)
    }
    scenario("(winningChips). col and row. Lost!") {
      Given("A game (winningChips-1) Chips")
      val game: Game = Game(cols, rows)
      for (i <- 1 until winningChips) {
        for (i <- 1 until winningChips) game.dropChip(i, YellowChip)
        game.dropChip(i, RedChip)
      }
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, YellowChip, game.findFirstEmpty(0).get).diagUpLost
      Then("It should succed.")
      assert(success)
    }
  }


  // BEST MOVE
  feature("Evaluate the best move.") {
    scenario("Nothing - so take whatever there is. This should not be needed when the program is finished") {
      Given("A game with own Chips")
      val game: Game = Game(cols, rows)
      When("Evaluate the best Slot.")
      val evalCol = evalBestMove(game, RedChip)
      Then("It should take the first row.")
      assert(evalCol === 0)
    }
    scenario("A vertical Win.") {
      Given("A game with own Chips")
      val game: Game = Game(cols, rows)
      for (i <- 1 until winningChips) game.dropChip(3, RedChip)
      When("Evaluate the best Slot.")
      val evalCol = evalBestMove(game, RedChip)
      Then("It should take the vertical Win.")
      assert(evalCol === 3)
    }
    scenario("A vertical Lost.") {
      Given("A game with other Chips")
      val game: Game = Game(cols, rows)
      for (i <- 1 until winningChips) game.dropChip(3, YellowChip)
      When("Evaluate the best Slot.")
      val evalCol = evalBestMove(game, RedChip)
      Then("It should take the vertical Lost.")
      assert(evalCol === 3)
    }
    scenario("A vertical Win and Lost.") {
      Given("A game with other Chips")
      val game: Game = Game(cols, rows)
      for (i <- 1 until winningChips) {
        game.dropChip(2, YellowChip)
        game.dropChip(3, RedChip)
      }
      When("Evaluate the best Slot.")
      val evalCol = evalBestMove(game, RedChip)
      Then("It should take the vertical Win.")
      assert(evalCol === 3)
    }

    scenario("A horizontal Win.") {
      Given("A game with own Chips")
      val game: Game = Game(cols, rows)
      for (i <- 0 until winningChips - 1) game.dropChip(i, RedChip)
      When("Evaluate the best Slot.")
      val evalCol = evalBestMove(game, RedChip)
      Then("It should take the horizontal Win.")
      assert(evalCol === winningChips - 1)
    }
    scenario("A horizontal Lost.") {
      Given("A game with other Chips")
      val game: Game = Game(cols, rows)
      for (i <- 0 until winningChips - 1) game.dropChip(i, YellowChip)
      When("Evaluate the best Slot.")
      val evalCol = evalBestMove(game, RedChip)
      Then("It should take the horizontal Lost.")
      assert(evalCol === winningChips - 1)
    }
    scenario("A horizontal Win and vertical Lost.") {
      Given("A game with other Chips")
      val game: Game = Game(cols, rows)
      for (i <- 0 until winningChips - 1) {
        game.dropChip(5, YellowChip)
        game.dropChip(i, RedChip)
      }
      When("Evaluate the best Slot.")
      val evalCol = evalBestMove(game, RedChip)
      Then("It should take the horizontal Win.")
      assert(evalCol === winningChips - 1)
    }

    scenario("A diagonal up Win.") {
      Given("A game with other Chips")
      val game: Game = Game(cols, rows)
      for (i <- 2 to winningChips) {
        for (i <- 2 to winningChips) game.dropChip(i, YellowChip)
        game.dropChip(i, RedChip)
      }
      When("Evaluate the best Slot.")
      val evalCol = evalBestMove(game, RedChip)
      Then("It should take the diagonal up Win.")
      assert(evalCol === 1)
    }

  }
}
