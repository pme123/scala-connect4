package pme.connect4.domain

import pme.connect4.util.FeatureTester

class ConnectFourGameTest extends FeatureTester {



  feature("Create Game") {
    scenario("Create Game.") {
      Given("Nothing")
      When("Create the new Game")
      val game: Game = Game(4,5)
      Then("All Slots are 'Empty'")
      for {
        slot <- game.slots
        spot <- slot.spots
      } yield assert(spot.chip === SpaceChip)

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
      val spots =List( Spot(YellowChip,1,0), Spot(SpaceChip,1,1))
      val slot = new Slot(1, spots)
      When("Find first Empty Spot Position")
      val emptySpot = slot.findFirstEmpty
      Then("Then this is 1")
      assert(emptySpot.get.row === 1)
    }
    scenario("full Slot") {
      Given("Slot filled with Yellow- and RedChips")
      val spots =List( Spot(YellowChip,1,0), Spot(RedChip,1,1))
      val slot = Slot(2,spots)
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
      val emptySpot = game.findFirstEmptySlot(3)
      Then("Then this is 0")
      assert(emptySpot.get.row === 0)
    }
    scenario("Invalid Slot") {
      Given(" Game")
      val game = new ConnectFourGame()
      When("Find first Empty Spot Position in an invalid Slot")
      val emptySpot = game.findFirstEmptySlot(10)
      Then("Then this is None")
      assert(emptySpot === None)
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
      When("Find first Empty Spot Position")
      val positions = game.winningPositions(YellowChip)
      Then("Then this is None")
      assert(positions === None)
    }


  }
}