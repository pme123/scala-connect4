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
      Then("It should not succeed.")
      assert(!success)
    }
    scenario("(winningChips-1). row in a Slot. No Win.") {
      Given("A game with (winningChips-2) Chips")
      val game: Game = Game(cols, rows)
      for (i <- 1 until winningChips - 1) game.dropChip(0, RedChip)
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).vertWin
      Then("It should not succeed.")
      assert(!success)
    }
    scenario("Slot with 3 Chips and 1 another Chip. No Win.") {
      Given("A game with Chips")
      val game: Game = Game(cols, rows)
      for (i <- 1 until winningChips) game.dropChip(0, RedChip)
      game.dropChip(0, YellowChip)
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).vertWin
      Then("It should not succeed.")
      assert(!success)
    }
    scenario("(winningChips). row in a Slot. Win!") {
      Given("A game with (winningChips-1) other Chips")
      val game: Game = Game(cols, rows)
      for (i <- 1 until winningChips) game.dropChip(0, RedChip)
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).vertWin
      Then("It should succeed.")
      assert(success)
    }
  }

  feature("Evaluate the Vertical Lost") {
    scenario("1. row on a Slot. No Lost.") {
      Given("A game without Chips")
      val game: Game = Game(cols, rows)
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).vertLost
      Then("It should not succeed.")
      assert(!success)
    }
    scenario("(winningChips-1). row in a Slot. No Lost.") {
      Given("A game with (winningChips-2) Chips")
      val game: Game = Game(cols, rows)
      for (i <- 1 until winningChips - 1) game.dropChip(0, RedChip)
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).vertLost
      Then("It should not succeed.")
      assert(!success)
    }
    scenario("(winningChips). row in a Slot. Lost!") {
      Given("A game with (winningChips-1) other Chips")
      val game: Game = Game(cols, rows)
      for (i <- 1 until winningChips) game.dropChip(0, YellowChip)
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).vertLost
      Then("It should succeed.")
      assert(success)
    }
  }
  // HORIZONTAL
  // Win
  feature("Evaluate the Horizontal Win") {
    scenario("1. col on a row. No Win.") {
      Given("A game without Chips")
      val game: Game = Game(cols, rows)
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).horWin
      Then("It should not succeed.")
      assert(!success)
    }
    scenario("(winningChips-1). col on a row. No Win.") {
      Given("A game (winningChips-2) Chips")
      val game: Game = Game(cols, rows)
      for (i <- 1 until winningChips - 1) game.dropChip(i, RedChip)
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).horWin
      Then("It should not succeed.")
      assert(!success)
    }

    scenario("(winningChips). col on a row. Win!") {
      Given("A game (winningChips-1) Chips")
      val game: Game = Game(cols, rows)
      for (i <- 1 until winningChips) game.dropChip(i, RedChip)
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).horWin
      Then("It should succeed.")
      assert(success)
    }
  }

  feature("Evaluate the Horizontal Win in two steps") {
    scenario("There is only one Chip [ ][r][ ][ ]. No Win.") {
      Given("A game without Chips")
      val game: Game = Game(cols, rows)
      game.dropChip(1, RedChip)
      When("Evaluate the points for the first Slot(2)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(2).get).horWinWith2
      Then("It should not succeed.")
      assert(!success)
    }

    scenario("There are 2 Chips [ ][r][r][ ]. Win!") {
      Given("A game (winningChips-1) Chips")
      val game: Game = Game(cols, rows)
      for (i <- 2 until winningChips) game.dropChip(i, RedChip)
      When("Evaluate the points for the first Slot(winningChips)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(winningChips).get).horWinWith2
      Then("It should succeed.")
      assert(success)
    }
  }
  feature("Evaluate the Horizontal Win in two steps with space") {
    scenario("There is only one Chip [ ][r][ ][ ][ ]. No Win.") {
      Given("A game without Chips")
      val game: Game = Game(cols, rows)
      game.dropChip(1, RedChip)
      When("Evaluate the points for the first Slot(2)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(2).get).horWinWith2AndSpace
      Then("It should not succeed.")
      assert(!success)
    }

    scenario("There are 2 Chips [ ][r][ ][r][ ]. Win!") {
      Given("A game (winningChips-1) Chips")
      val game: Game = Game(cols, rows)
      game.dropChip(1, RedChip)
      game.dropChip(3, RedChip)
      When("Evaluate the points for the first Slot(winningChips)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(2).get).horWinWith2AndSpace
      Then("It should succeed.")
      assert(success)
    }
  }
  // Lost
  feature("Evaluate the Horizontal Lost") {
    scenario("1. col on a row. No Lost.") {
      Given("A game without Chips")
      val game: Game = Game(cols, rows)
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).horLost
      Then("It should not succeed.")
      assert(!success)
    }
    scenario("(winningChips-1). col on a row. No Lost.") {
      Given("A game (winningChips-2) Chips")
      val game: Game = Game(cols, rows)
      for (i <- 1 until winningChips - 1) game.dropChip(i, YellowChip)
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).horLost
      Then("It should not succeed.")
      assert(!success)
    }

    scenario(s"$winningChips. col on a row. Lost!") {
      Given(s"A game ${winningChips - 1} Chips")
      val game: Game = Game(cols, rows)
      for (i <- 1 until winningChips) game.dropChip(i, YellowChip)
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).horLost
      Then("It should succeed.")
      assert(success)
    }
    scenario(s"Last chip is empty. Lost!") {
      Given(s"A game with [y][y][y][ ]")
      val game: Game = Game(cols, rows)
      for (i <- cols - winningChips until cols - 1) game.dropChip(i, YellowChip)
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(cols - 1).get).horLost
      Then("It should succeed.")
      assert(success)
    }
  }

  feature("Evaluate the Horizontal Lost with only 2 other Chips.") {
    scenario("No chip [?][ ][ ][ ]. No Lost.") {
      Given("A game without Chips")
      val game: Game = Game(cols, rows)
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).horLostWith2
      Then("It should not succeed.")
      assert(!success)
    }
    scenario("1 Chip - No Lost.") {
      Given("A game with [?][y][ ][ ]")
      val game: Game = Game(cols, rows)
      game.dropChip(1, YellowChip)
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).horLostWith2
      Then("It should not succeed.")
      assert(!success)
    }
    scenario("2 Chips - No Lost.") {
      Given("A game with [?][y][y][r]")
      val game: Game = Game(cols, rows)
      for (i <- 1 until winningChips - 1) game.dropChip(i, YellowChip)
      game.dropChip(winningChips - 1, RedChip)
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).horLostWith2
      Then("It should Not succeed.")
      assert(!success)
    }
    scenario("2 Chips - Lost.") {
      Given("A game with [?][y][y][ ]")
      val game: Game = Game(cols, rows)
      for (i <- 1 until winningChips - 1) game.dropChip(i, YellowChip)
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).horLostWith2
      Then("It should succeed.")
      assert(success)
    }
    scenario("2 Chips with spaces but the first space is not the one- No Lost.") {
      Given("A game with [?][y][ ][y][ ]")
      val game: Game = Game(cols, rows)
      game.dropChip(2, YellowChip)
      game.dropChip(4, YellowChip)
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(1).get).horLostWith2AndSpace
      Then("It should succeed.")
      assert(!success)
    }
    scenario("2 Chips with spaces but the last space is not the one- No Lost.") {
      Given("A game with [ ][y][ ][y][?]")
      val game: Game = Game(cols, rows)
      game.dropChip(2, YellowChip)
      game.dropChip(4, YellowChip)
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(5).get).horLostWith2AndSpace
      Then("It should succeed.")
      assert(!success)
    }
    scenario("2 Chips with spaces - Lost.") {
      Given("A game with [ ][y][?][y][ ]")
      val game: Game = Game(cols, rows)
      game.dropChip(2, YellowChip)
      game.dropChip(4, YellowChip)
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(3).get).horLostWith2AndSpace
      Then("It should succeed.")
      assert(success)
    }
  }
  // Other
  feature("Evaluate the Horizontal Other with 4 spaces.") {
    scenario("Only 3 spaces [?][ ][ ][y]. No success.") {
      Given("A game without Chips")
      val game: Game = Game(cols, rows)
      game.dropChip(3, YellowChip)
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).horOtherSpace4
      Then("It should not succeed.")
      assert(!success)
    }
    scenario("4 spaces [?y][ ][ ][ ][ ][y] but take the one before. No success.") {
      Given("A game without Chips")
      val game: Game = Game(cols, rows)
      game.dropChip(0, YellowChip)
      game.dropChip(5, YellowChip)
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).horOtherSpace4
      Then("It should not succeed.")
      assert(!success)
    }
    scenario("4 spaces [ ][?][ ][ ][y]. Success.") {
      Given("A game without Chips")
      val game: Game = Game(cols, rows)
      game.dropChip(4, YellowChip)
      When("Evaluate the points for the first Slot(1)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(1).get).horOtherSpace4
      Then("It should succeed.")
      assert(success)
    }
  }

  feature("Evaluate the Horizontal Other with 5 spaces.") {
    scenario("Only 4 spaces [ ][?][ ][ ][y]. No success.") {
      Given("A game with another Chips")
      val game: Game = Game(cols, rows)
      game.dropChip(4, YellowChip)
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(1).get).horOtherSpace5
      Then("It should not succeed.")
      assert(!success)
    }
    scenario("5 spaces [?][ ][ ][ ][ ][y] but at the edge. No success.") {
      Given("A game with another Chips")
      val game: Game = Game(cols, rows)
      game.dropChip(5, YellowChip)
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).horOtherSpace5
      Then("It should not succeed.")
      assert(!success)
    }
    scenario("5 spaces [ ][?][ ][ ][ ][y]. Success.") {
      Given("A game with another Chips")
      val game: Game = Game(cols, rows)
      game.dropChip(5, YellowChip)
      When("Evaluate the points for the first Slot(1)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(1).get).horOtherSpace5
      Then("It should succeed.")
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
      Then("It should not succeed.")
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
      Then("It should not succeed.")
      assert(!success)
    }

    scenario("(winningChips). col and row but without a Chip below. No Win!") {
      Given("A game (winningChips-1) Chips")
      val game: Game = Game(cols, rows)
      for (i <- 1 until winningChips) {
        game.dropChip(i - 1, RedChip)
        for (i <- 1 until winningChips - 1) game.dropChip(i, YellowChip)

      }
      When("Evaluate the points for the Slot(winningChips)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(winningChips - 1).get).diagUpWin
      Then("It should Not succeed.")
      assert(!success)
    }
    scenario("(winningChips). col and row with a Chip below. Win!") {
      Given("A game (winningChips-1) Chips")
      val game: Game = Game(cols, rows)
      for (i <- 1 until winningChips) {
        game.dropChip(i, RedChip)
        for (i <- 1 until winningChips) game.dropChip(i, YellowChip)
        game.dropChip(winningChips, RedChip)
      }
      When("Evaluate the points for the Slot(winningChips)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(winningChips).get).diagUpWin
      Then("It should succeed.")
      assert(success)
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
      Then("It should succeed.")
      assert(success)
    }
  }

  feature("Evaluate the Diagonal up Lost") {
    scenario("1. col on a row. No Lost.") {
      Given("A game without Chips")
      val game: Game = Game(cols, rows)
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, YellowChip, game.findFirstEmpty(0).get).diagUpLost
      Then("It should not succeed.")
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
      Then("It should not succeed.")
      assert(!success)
    }

    scenario("(winningChips). col and row but without a Chip below. No Lost!") {
      Given("A game (winningChips-1) Chips")
      val game: Game = Game(cols, rows)
      for (i <- 1 until winningChips) {
        game.dropChip(i - 1, RedChip)
        for (i <- 1 until winningChips - 1) game.dropChip(i, YellowChip)

      }
      When("Evaluate the points for the Slot(winningChips)")
      val success = new AllCombinations(game, YellowChip, game.findFirstEmpty(winningChips - 1).get).diagUpLost
      Then("It should NOT succeed.")
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
      Then("It should NOT succeed.")
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
      Then("It should succeed.")
      assert(success)
    }
  }

  // Diagonal down (from left-up to right-down)
  feature("Evaluate the Diagonal down Win") {
    scenario("1. col on a row. No Win.") {
      Given("A game without Chips")
      val game: Game = Game(cols, rows)
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).diagUpWin
      Then("It should not succeed.")
      assert(!success)
    }
    scenario("(winningChips-1). col and row. No Win.") {
      Given("A game (winningChips-2) Chips")
      val game: Game = Game(cols, rows)
      for (i <- winningChips - 2 to 1 by -1) {
        for (i <- winningChips - 2 to 1 by -1) game.dropChip(i, YellowChip)
        game.dropChip(i, RedChip)
      }
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).diagDownWin
      Then("It should not succeed.")
      assert(!success)
    }

    scenario(s"$winningChips. col and row but without a Chip below. No Win!") {
      Given(s"A game ${winningChips - 1} Chips")
      val game: Game = Game(cols, rows)
      for (i <- winningChips - 1 to 1 by -1) {
        game.dropChip(i, RedChip)
        for (i <- winningChips - 1 to 1 by -1) game.dropChip(i, YellowChip)
      }
      When(s"Evaluate the points for the Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).diagDownWin
      Then("It should Not succeed.")
      assert(!success)
    }
    scenario("(winningChips). col and row with a Chip below. Win!") {
      Given("A game (winningChips-1) Chips")
      val game: Game = Game(cols, rows)
      for (i <- winningChips - 1 to 1 by -1) {
        game.dropChip(i, RedChip)
        for (i <- winningChips - 1 to 1 by -1) game.dropChip(i, YellowChip)
        game.dropChip(0, RedChip)
      }
      When("Evaluate the points for the Slot(winningChips)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(0).get).diagDownWin
      Then("It should succeed.")
      assert(success)
    }
    scenario("(winningChips). col and row. Win!") {
      Given("A game (winningChips-1) Chips")
      val game: Game = Game(cols, rows)
      for (i <- winningChips - 1 to 1 by -1) {
        for (i <- winningChips - 1 to 1 by -1) game.dropChip(i, YellowChip)
        game.dropChip(i, RedChip)
      }
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, RedChip, game.findFirstEmpty(winningChips).get).diagDownWin
      Then("It should succeed.")
      assert(success)
    }
  }

  feature("Evaluate the Diagonal down Lost") {
    scenario("1. col on a row. No Lost.") {
      Given("A game without Chips")
      val game: Game = Game(cols, rows)
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, YellowChip, game.findFirstEmpty(0).get).diagDownLost
      Then("It should not succeed.")
      assert(!success)
    }
    scenario("(winningChips-1). col and row. No Lost.") {
      Given("A game (winningChips-2) Chips")
      val game: Game = Game(cols, rows)
      for (i <- winningChips - 2 to 1 by -1) {
        for (i <- winningChips - 2 to 1 by -1) game.dropChip(i, YellowChip)
        game.dropChip(i, RedChip)
      }
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, YellowChip, game.findFirstEmpty(0).get).diagDownLost
      Then("It should not succeed.")
      assert(!success)
    }

    scenario("(winningChips). col and row but without a Chip below. No Lost!") {
      Given("A game (winningChips-1) Chips")
      val game: Game = Game(cols, rows)
      for (i <- winningChips - 1 to 1 by -1) {
        game.dropChip(i, RedChip)
        for (i <- winningChips - 1 to 1 by -1) game.dropChip(i, YellowChip)
      }
      When("Evaluate the points for the Slot(winningChips)")
      val success = new AllCombinations(game, YellowChip, game.findFirstEmpty(winningChips - 1).get).diagDownLost
      Then("It should NOT succeed.")
      assert(!success)
    }
    scenario("(winningChips). col and row with a Chip below. Lost!") {
      Given("A game (winningChips-1) Chips")
      val game: Game = Game(cols, rows)
      for (i <- winningChips - 1 to 1 by -1) {
        game.dropChip(i, RedChip)
        for (i <- winningChips - 1 to 1 by -1) game.dropChip(i, YellowChip)
        game.dropChip(0, RedChip)
      }
      When("Evaluate the points for the Slot(0)")
      val success = new AllCombinations(game, YellowChip, game.findFirstEmpty(0).get).diagDownLost
      Then("It should succeed.")
      assert(success)
    }
    scenario(s"$winningChips. col and row. Lost!") {
      Given("A game (winningChips-1) Chips")
      val game: Game = Game(cols, rows)
      for (i <- winningChips - 1 to 1 by -1) {
        for (i <- winningChips - 1 to 1 by -1) game.dropChip(i, YellowChip)
        game.dropChip(i, RedChip)
      }
      When("Evaluate the points for the first Slot(0)")
      val success = new AllCombinations(game, YellowChip, game.findFirstEmpty(winningChips).get).diagDownLost
      Then("It should succeed.")
      assert(success)
    }
  }


  // BEST MOVE
  feature("Evaluate the best move.") {
    // Vertical
    scenario("Nothing - so take whatever there is. This should not be needed when the program is finished") {
      Given("A game with own Chips")
      val game: Game = Game(cols, rows)
      game.dropChip(0, YellowChip)
      game.dropChip(4, YellowChip)
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
    scenario("Slot with 3 Chips and 1 another Chip. No Win.") {
      Given("A game with Chips")
      val game: Game = Game(cols, rows)
      for (i <- 1 until winningChips) game.dropChip(1, RedChip)
      game.dropChip(1, YellowChip)
      When("Evaluate the best Slot.")
      val evalCol = evalBestMove(game, RedChip)
      Then("It should take the vertical Win.")
      assert(evalCol != 1)
    }
    // Hor
    scenario("A horizontal Win.") {
      Given("A game with own Chips")
      val game: Game = Game(cols, rows)
      for (i <- 0 until winningChips - 1) game.dropChip(i, RedChip)
      When("Evaluate the best Slot.")
      val evalCol = evalBestMove(game, RedChip)
      Then("It should take the horizontal Win.")
      assert(evalCol === winningChips - 1)
    }
    scenario("A horizontal Win in 2 steps") {
      Given("A game with own Chips")
      val game: Game = Game(cols, rows)
      for (i <- 2 to 3) game.dropChip(i, RedChip)
      When("Evaluate the best Slot.")
      val evalCol = evalBestMove(game, RedChip)
      Then("It should take the horizontal Win in 2 steps.")
      assert(evalCol === 1)
    }
    scenario("A horizontal Win in 2 steps with space") {
      Given("A game with own Chips")
      val game: Game = Game(cols, rows)
      game.dropChip(2, RedChip)
      game.dropChip(4, RedChip)
      When("Evaluate the best Slot.")
      val evalCol = evalBestMove(game, RedChip)
      Then("It should take the horizontal Win in 2 steps.")
      assert(evalCol === 3)
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
    scenario("A horizontal Lost with 2 chips.") {
      Given("A game with 2 other Chips")
      val game: Game = Game(cols, rows)
      for (i <- 2 to winningChips - 1) game.dropChip(i, YellowChip)
      When("Evaluate the best Slot.")
      val evalCol = evalBestMove(game, RedChip)
      Then("It should take the horizontal Lost with 2 chips.")
      assert(evalCol === 1)
    }
    scenario("A horizontal Lost with 2 chips and Space.") {
      Given("A game with 2 other Chips")
      val game: Game = Game(cols, rows)
      game.dropChip(2, YellowChip)
      game.dropChip(4, YellowChip)
      When("Evaluate the best Slot.")
      val evalCol = evalBestMove(game, RedChip)
      Then("It should take the horizontal Lost with 2 chips.")
      assert(evalCol === 3)
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
    // Diagonal
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

    scenario("A diagonal down Win.") {
      Given("A game with other Chips")
      val game: Game = Game(cols, rows)
      for (i <- winningChips - 1 to 1 by -1) {
        for (i <- winningChips - 1 to 1 by -1) game.dropChip(i, YellowChip)
        game.dropChip(i, RedChip)
      }
      When("Evaluate the best Slot.")
      val evalCol = evalBestMove(game, RedChip)
      Then("It should take the diagonal down Win.")
      assert(evalCol === winningChips)
    }
    // Other
    scenario("Takes default but in the row above there is a lost.") {
      Given("A game with other Chips")
      val game: Game = Game(cols, rows)
      game.dropChip(1, RedChip)
      game.dropChip(2, YellowChip)
      game.dropChip(3, RedChip)
      for (i <- 1 until winningChips) {
        game.dropChip(i, YellowChip)
      }
      When("Evaluate the best Slot.")
      val evalCol = evalBestMove(game, RedChip)
      Then("It should not take the 0.")
      assert(evalCol != 0)
    }

    scenario("Takes the 4 spaces.") {
      Given("A game without other Chips")
      val game: Game = Game(cols, rows)
      game.dropChip(0, YellowChip)
      game.dropChip(5, YellowChip)
      When("Evaluate the best Slot.")
      val evalCol = evalBestMove(game, RedChip)
      Then("It should take the 2.")
      assert(evalCol === 2)
    }
    scenario("Takes the 5 spaces.") {
      Given("A game without other Chips")
      val game: Game = Game(cols, rows)
      game.dropChip(0, YellowChip)
      game.dropChip(5, YellowChip)
      When("Evaluate the best Slot.")
      val evalCol = evalBestMove(game, RedChip)
      Then("It should take the 2.")
      assert(evalCol === 2)
    }
  }

}
