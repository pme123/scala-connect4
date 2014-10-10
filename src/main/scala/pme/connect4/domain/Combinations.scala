package pme.connect4.domain

import pme.connect4.domain.GameConfig._

import scala.collection.immutable.IndexedSeq



object Combinations {
type Combination = Boolean

  def evalBestMove(game: Game, activeChip: Chip): Int = {

    def evalPointsForSlot(spot: Spot): Int = {
      allCombinations(game, activeChip, spot).takeWhile(comb => !comb).size
    }
   val pointsForSlots: List[(Int, Spot)] = for {
      slot <- game.slots
      spot <- slot.findFirstEmpty
    } yield {
      evalPointsForSlot(spot) -> spot
    }
    val chosenSpot = pointsForSlots.foldLeft((Int.MaxValue, None: Option[Spot]))((a, b) => if (a._1 > b._1) (b._1, Some(b._2)) else a)
    println(s"chosenSpot: $chosenSpot")
    chosenSpot._2 match {
      case Some(spot: Spot) => spot.col
      case None => throw new IllegalArgumentException
    }
  }

  def allCombinations(game: Game, activeChip: Chip, spot: Spot): Stream[Combination] = {
    AllCombinations(game, activeChip, spot).asList
  }

  case class AllCombinations(game: Game, activeChip: Chip, spot: Spot) {
    // VERTICAL
    lazy val vertWin = VertCombination.eval(activeChip)

    lazy val vertLost = VertCombination.eval(activeChip.other)


    object VertCombination {
      protected[domain] def eval(chip: Chip): Boolean = (winningChips - 1) == game.countSpotsBelow(spot, chip)
    }

    // HORIZONTAL
    lazy val horWin:Combination =      HorCombination.findChips(activeChip)

    lazy val horLost =HorCombination.findChips(activeChip.other)

    object HorCombination {
      private def minSlot(spot: Spot) = spot.col - winningChips + 1 max 0
      private def maxSlot(spot: Spot) = spot.col + winningChips max cols

      private val neighbors: Iterable[IndexedSeq[Spot]] = {
        (for {
          attempt <- minSlot(spot) to maxSlot(spot) - winningChips
          col <- attempt until attempt + winningChips
          neighbor = game.slots(col).spots(spot.row)
        } yield {
          (attempt, neighbor)
        })
          .groupBy(neighbor => neighbor._1)
          .values
          .map(attempt => attempt.map(entry => entry._2))
          .filter(_.size == winningChips)
      }

      protected[domain] def findChips(chip: Chip): Boolean = {
        neighbors.filter(spots => spots.count(_.chip == chip) == winningChips - 1).nonEmpty
      }
    }

    // DIAGONAL up (from left-down to right-up)
    lazy val diagUpWin:Combination =      DiagUpCombination.findChips(activeChip)
    lazy val diagUpLost =DiagUpCombination.findChips(activeChip.other)

    object DiagUpCombination {
      private def minSpot(spot: Spot):Spot = {
        val minCol = (spot.col - winningChips + 1 max 0)
        val minRow = (spot.row - winningChips + 1 max 0)
        def inner(sp: Spot):Spot =spot match {
          case sp if sp.col == minCol || sp.row == minRow => sp
          case sp => inner(game.retrieveSpot(spot.col-1, spot.row-1))
        }
        inner(spot)
      }
      private def allSpots(spot: Spot):List[Spot] = {
        val maxCol = (spot.col + winningChips max cols)
        val maxRow = (spot.row + winningChips  max rows)
        def inner(sp: Spot):List[Spot] = spot match {
          case sp if sp.col == maxCol || sp.row == maxRow => List(sp)
          case sp => println(sp); spot::inner(game.retrieveSpot(spot.col+1, spot.row+1))
        }
        inner(minSpot(spot))
      }

      private val neighbors: Iterable[List[Spot]] = {
        val spots:List[Spot] = allSpots(spot)
        (for {
          aSpot:Spot <- spots
          col <- aSpot.col until aSpot.col + winningChips
        } yield {
          (aSpot.col, spots(col))
        })
          .groupBy(neighbor => neighbor._1)
          .values
          .map(attempt => attempt.map(entry => entry._2))
          .filter(_.size == winningChips)
      }

      protected[domain] def findChips(chip: Chip): Boolean = {
        neighbors.filter(spots => spots.count(_.chip == chip) == winningChips - 1).nonEmpty
      }
    }



    def asList: Stream[Combination] = Stream(horWin, vertWin, horLost, vertLost)

  }


}
