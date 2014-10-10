package pme.connect4.domain

import pme.connect4.util.FeatureTester

class ConnectFourGameTest extends FeatureTester {

  import pme.connect4.domain.GameConfig._


  feature("Create Game") {
    scenario("Create Game.") {
      Given("Nothing")
      When("Create the new Game")
      val cols = 4
      val rows = 5
      val game: Game = Game(cols, rows)
      Then("All Slots are 'Empty'")
      for {
        slot <- game.slots
        spot <- slot.spots
      } yield assert(spot.chip === SpaceChip)
      assert(game.slots.length === cols)
      assert(game.slots(0).spots.length === rows)
    }
  }

  feature("Find first empty Spot in Slot") {
    scenario("Empty Slot") {
      Given("Empty Slot")
      val slot = Slot(1, 4)
      When("Find first Empty Spot Position")
      val emptySpot = slot.findFirstEmpty
      Then("Then this is 0")
      assert(emptySpot.get.row === 0)
    }
    scenario("half filled") {
      Given("Slot with one Yellow")
      val spots = List(Spot(YellowChip, 1, 0), Spot(SpaceChip, 1, 1))
      val slot = new Slot(1, spots)
      When("Find first Empty Spot Position")
      val emptySpot = slot.findFirstEmpty
      Then("Then this is 1")
      assert(emptySpot.get.row === 1)
    }
    scenario("full Slot") {
      Given("Slot filled with Yellow- and RedChips")
      val spots = List(Spot(YellowChip, 1, 0), Spot(RedChip, 1, 1))
      val slot = Slot(2, spots)
      When("Find first Empty Spot Position")
      val emptySpot = slot.findFirstEmpty
      Then("Then this is None")
      assert(emptySpot === None)
    }

  }

  feature("Find first empty Spot in Game") {
    scenario("Valid Slot") {
      Given("Game")
      val game = new ConnectFourGame()
      When("Find first Empty Spot Position")
      val emptySpot = game.findFirstEmptySpot(3)
      Then("Then this is 0")
      assert(emptySpot.get.row === 0)
    }
    scenario("Invalid Slot") {
      Given(" Game")
      val game = new ConnectFourGame()
      When("Find first Empty Spot Position in an invalid Slot")
      val emptySpot = game.findFirstEmptySpot(10)
      Then("Then this is None")
      assert(emptySpot === None)
    }

  }

  feature("Find Spot in Game") {
    scenario("Valid Spot") {
      Given("new Game")
      val game = new ConnectFourGame().game
      When("Find Spot")
      val emptySpot = game.findSpot(0, 0)
      Then("Then this finds something")
      assert(emptySpot.get.chip === SpaceChip)
    }
    scenario("Invalid Spot") {
      Given("new Game")
      val game = new ConnectFourGame().game
      When("Find Spot outside the game")
      val emptySpot = game.findSpot(0, rows)
      Then("Then this finds nothing")
      assert(emptySpot === None)
    }
  }
  feature("Find Spot in the same row before this Spot") {
    scenario("Valid Spot") {
      Given("new Game")
      val game = new ConnectFourGame().game
      When("Find Spot in the same row before this Spot.")
      val emptySpot = game.findSpotInRowBefore(game.retrieveSpot(1,0))
      Then("Then this finds the Spot")
      assert(emptySpot.get.col === 0)
      assert(emptySpot.get.row === 0)
    }
    scenario("Invalid Spot") {
      Given("new Game")
      val game = new ConnectFourGame().game
      When("Find Spot in the same row before this Spot.")
      val emptySpot = game.findSpotInRowBefore(game.retrieveSpot(0, 0))
      Then("Then this finds nothing")
      assert(emptySpot === None)
    }
  }
  feature("Find Spot in the same row after this Spot") {
    scenario("Valid Spot") {
      Given("new Game")
      val game = new ConnectFourGame().game
      When("Find Spot in the same row after this Spot.")
      val emptySpot = game.findSpotInRowAfter(game.retrieveSpot(1,0))
      Then("Then this finds the Spot")
      assert(emptySpot.get.col === 2)
      assert(emptySpot.get.row === 0)
    }
    scenario("Invalid Spot") {
      Given("new Game")
      val game = new ConnectFourGame().game
      When("Find Spot in the same row after this Spot.")
      val emptySpot = game.findSpotInRowAfter(game.retrieveSpot(cols-1, 0))
      Then("Then this finds nothing")
      assert(emptySpot === None)
    }
  }
  feature("Find Spot in the same col below this Spot") {
    scenario("Valid Spot") {
      Given("new Game")
      val game = new ConnectFourGame().game
      When("Find Spot in the same col below this Spot.")
      val emptySpot = game.findSpotInColBelow(game.retrieveSpot(0,1))
      Then("Then this finds the Spot")
      assert(emptySpot.get.col === 0)
      assert(emptySpot.get.row === 0)
    }
    scenario("Invalid Spot") {
      Given("new Game")
      val game = new ConnectFourGame().game
      When("Find Spot in the same col below this Spot.")
      val emptySpot = game.findSpotInColBelow(game.retrieveSpot(0, 0))
      Then("Then this finds nothing")
      assert(emptySpot === None)
    }
  }
  feature("Find Spot in the same col above this Spot") {
    scenario("Valid Spot") {
      Given("new Game")
      val game = new ConnectFourGame().game
      When("Find Spot in the same col above this Spot.")
      val emptySpot = game.findSpotInColAbove(game.retrieveSpot(0,1))
      Then("Then this finds the Spot")
      assert(emptySpot.get.col === 0)
      assert(emptySpot.get.row === 2)
    }
    scenario("Invalid Spot") {
      Given("new Game")
      val game = new ConnectFourGame().game
      When("Find Spot in the same col above this Spot.")
      val emptySpot = game.findSpotInColAbove(game.retrieveSpot(0, rows-1))
      Then("Then this finds nothing")
      assert(emptySpot === None)
    }
  }
  feature("Retrieve Spot in Game") {
    scenario("Valid Spot") {
      Given("new Game")
      val game = new ConnectFourGame().game
      When("Retrieve first Empty Spot Position")
      val emptySpot = game.retrieveSpot(0, 0)
      Then("Then this retrieves something")
      assert(emptySpot.chip === SpaceChip)
    }
    scenario("Retrieve Invalid Spot") {
      Given("new Game")
      val game = new ConnectFourGame().game
      When("Retrieve first Empty Spot Position")
      Then("Then this throws an Exception")
      intercept[IllegalArgumentException] {
        game.retrieveSpot(0, rows)
      }
    }
  }

  feature("Has empty Spot in Game") {
    scenario("it has empty spot") {
      Given("Game")
      val game = new ConnectFourGame()
      When("check to have empty spot")
      val hasEmptySpot = game.hasEmptySlot(3)
      Then("Then this is 0")
      assert(hasEmptySpot)
    }

    scenario("it has not an empty spot") {
      Given("Game with a filled Slot")
      val game = new ConnectFourGame()
      for (count <- 0 to rows) game.dropChip(3, RedChip)
      When("check to have empty spot")
      val hasEmptySpot = game.hasEmptySlot(3)
      Then("Then this is 0")
      assert(!hasEmptySpot)
    }


  }

  feature("Find first taken Spot in Game") {
    scenario("No taken Spot") {
      Given("new Game")
      val game = new ConnectFourGame()
      When("Find first taken Spot Position, where there is none.")
      val takenSpot = game.findFirstTakenSpot(3)
      Then("Then this is None")
      assert(takenSpot === None)
    }
    scenario("Correct Spot") {
      Given("Game with a Chip")
      val game = new ConnectFourGame()
      game.dropChip(3, RedChip)
      When("Find first taken Spot Position")
      val takenSpot = game.findFirstTakenSpot(3)
      Then("Then this is on row 0")
      assert(takenSpot.get.row === 0)
    }

  }
  feature("Count SpaceChips in the same row before this Spot") {
    scenario("There are some SpaceChips") {
      Given("new Game with one Chip")
      val game = new ConnectFourGame().game
      game.dropChip(1, RedChip)
      When("Count SpaceChips")
      val spotCount = game.countSpaceBefore(game.retrieveSpot(5,0))
      Then("Then this finds the 3 Spots")
      assert(spotCount === 3)
    }
    scenario("There are no SpaceChips") {
      Given("new Game with one Chip")
      val game = new ConnectFourGame().game
      game.dropChip(1, RedChip)
      When("Count SpaceChips")
      val spotCount = game.countSpaceBefore(game.retrieveSpot(2,0))
      Then("Then this finds the 0 Spot")
      assert(spotCount === 0)
    }
  }
  feature("Count SpaceChips in the same row after this Spot") {
    scenario("There are some SpaceChips") {
      Given("new Game with one Chip")
      val game = new ConnectFourGame().game
      game.dropChip(cols-1, RedChip)
      When("Count SpaceChips")
      val spotCount = game.countSpaceAfter(game.retrieveSpot(cols-5,0))
      Then("Then this finds the 3 Spots")
      assert(spotCount === 3)
    }
    scenario("There are no SpaceChips") {
      Given("new Game with one Chip")
      val game = new ConnectFourGame().game
      game.dropChip(cols-1, RedChip)
      When("Count SpaceChips")
      val spotCount = game.countSpaceAfter(game.retrieveSpot(cols-2,0))
      Then("Then this finds the 0 Spot")
      assert(spotCount === 0)
    }
  }

  feature("Check the Spot below a Spot, if there is a SpaceChip") {
    scenario("There is a SpaceChip") {
      Given("new Game with one Chip")
      val game = new ConnectFourGame().game
      game.dropChip(1, RedChip)
      When("Check the Spot below")
      val hasSpotBelow = game.checkSpotBelow(game.retrieveSpot(1,1))
      Then("Then there is NO SpaceChip below")
      assert(hasSpotBelow)
    }
    scenario("There is nothing below") {
      Given("new Game")
      val game = new ConnectFourGame().game
      When("Check the Spot below")
      val hasSpotBelow = game.checkSpotBelow(game.retrieveSpot(0,0))
      Then("Then there is NO SpaceChip below")
      assert(hasSpotBelow)
    }
    scenario("There is a SpaceChip below") {
      Given("new Game ")
      val game = new ConnectFourGame().game
      When("Check the Spot below")
      val hasSpotBelow = game.checkSpotBelow(game.retrieveSpot(1,1))
      Then("Then there is A SpaceChip below")
      assert(!hasSpotBelow)
    }

  }

  feature("Count the Spots below a Spot that are in row with a certain Chip.") {
    scenario("There are 3 Chips") {
      Given("new Game with 3 Chips")
      val game = new ConnectFourGame().game
      for( i <- 0 to 2) game.dropChip(1, RedChip)
      When("Count the Spots below")
      val countSpots = game.countSpotsBelow(game.retrieveSpot(1,3), RedChip)
      Then("Then there are 3 Spots below")
      assert(countSpots === 3)
    }

    scenario("There is a another Chip") {
      Given("new Game with 1 another Chip")
      val game = new ConnectFourGame().game
      game.dropChip(1, YellowChip)
      When("Count the Spots below")
      val countSpots = game.countSpotsBelow(game.retrieveSpot(1,1), RedChip)
      Then("Then there is no Spot below")
      assert(countSpots === 0)
    }
    scenario("There is nothing below") {
      Given("new Game")
      val game = new ConnectFourGame().game
      When("Count the Spots below")
      val countSpots = game.countSpotsBelow(game.retrieveSpot(1,0), RedChip)
      Then("Then there is no Spot below")
      assert(countSpots === 0)
    }


  }

  feature("Drop the Chip in a Slot") {
    scenario("In a Slot with a Spot left") {
      Given("new Game")
      val game = new ConnectFourGame()
      When("Drop Chip")
      val spot = game.dropChip(0, YellowChip)
      Then("this is a Spot with the correct Chip and row")
      assert(spot.get.chip === YellowChip)
      And("The Game has a Slot with a YellowChip")
      assert(game.game.slots(0).spots(0).chip === YellowChip)
    }


  }
  feature("Evaluate the winning Positions") {
    scenario("No solution with new Game") {
      Given("new Game")
      val game = new ConnectFourGame()
      When("check has winning Spots")
      val positions = game.winningSpots(YellowChip)
      Then("Then this is None")
      assert(positions === List.empty)
    }

    scenario("Has 4 Chips but no vertical Winner") {
      Given("Game with 4 vertical Chips")
      val game = new ConnectFourGame()
      game.dropChip(3, RedChip)
      game.dropChip(3, YellowChip)
      for (index <- 0 until 3) game.dropChip(3, RedChip)
      When("check has winning Spots")
      val positions = game.winningSpots(RedChip)
      Then("Then this is None")
      assert(positions === List.empty)
    }
    scenario("Has vertical Winner") {
      Given("Game with vertical Winner")
      val game = new ConnectFourGame()
      for (index <- 0 until 4) game.dropChip(3, RedChip)
      When("check has winning Spots")
      val positions = game.winningSpots(RedChip)
      Then("Then this is has one Winner")
      assert(positions.size === 1)
      assert(positions(0).size === winningChips)
    }


    scenario("Has horizontal Winner") {
      Given("Game with horizontal Winner")
      val game = new ConnectFourGame()
      for (index <- 0 until 4) game.dropChip(index, RedChip)
      When("check has winning Spots")
      val positions = game.winningSpots(RedChip)
      Then("Then this is has one Winner")
      assert(positions.size === 1)
      assert(positions(0).size === winningChips)
    }

    scenario("Has horizontal Winner with more than needed") {
      Given("Game with horizontal Winner")
      val game = new ConnectFourGame()
      for (index <- 0 until 5) game.dropChip(index, RedChip)
      When("check has winning Spots")
      val positions = game.winningSpots(RedChip)
      Then("Then this is has two Winners")
      assert(positions.size === 2)
      for (pos <- positions) yield assert(pos.size === winningChips)
    }


    scenario("Has diagonal Winner") {
      Given("Game with diagonal Winner")
      val game = new ConnectFourGame()
      for (index <- 1 until 4) game.dropChip(index, YellowChip)
      for (index <- 2 until 4) game.dropChip(index, YellowChip)
      for (index <- 3 until 4) game.dropChip(index, YellowChip)
      for (index <- 0 until 4) game.dropChip(index, RedChip)
      When("check has winning Spots")
      val positions = game.winningSpots(RedChip)
      Then("Then this is has one Winner")
      assert(positions.size === 1)
      assert(positions(0).size === winningChips)
    }
    scenario("Has diagonal down Winner") {
      Given("Game with diagonal down Winner")
      val game = new ConnectFourGame()
      for (index <- 0 until 3) game.dropChip(index, YellowChip)
      for (index <- 0 until 2) game.dropChip(index, YellowChip)
      for (index <- 0 until 1) game.dropChip(index, YellowChip)
      for (index <- 0 until 4) game.dropChip(index, RedChip)
      When("check has winning Spots")
      val positions = game.winningSpots(RedChip)
      Then("this has one Winner")
      assert(positions.size === 1)
      assert(positions(0).size === winningChips)
    }
    scenario("Has diagonal and horizontal Winner") {
      Given("Game with diagonal and horizontal Winner")
      val game = new ConnectFourGame()
      for (index <- 1 until 4) game.dropChip(index, RedChip)
      for (index <- 2 until 4) game.dropChip(index, YellowChip)
      for (index <- 3 until 4) game.dropChip(index, YellowChip)
      for (index <- 0 until 4) game.dropChip(index, RedChip)
      When("check has winning Spots")
      val positions = game.winningSpots(RedChip)
      Then("this has two Winners")
      assert(positions.size === 2)
      for (pos <- positions) yield assert(pos.size === winningChips)
    }
    scenario("Has lots of Winner") {
      Given("Game with a Cube of Red")
      val game = new ConnectFourGame()
      for (index2 <- 0 until 4; index <- 0 until 4) game.dropChip(index, RedChip)
      When("check has winning Spots")
      val positions = game.winningSpots(RedChip)
      Then("Then this is has 4+4+2 Winner")
      assert(positions.size === 4 + 4 + 2)
      for (pos <- positions) yield assert(pos.size === winningChips)
    }
  }

}