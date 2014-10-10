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
    val chosenSpot = pointsForSlots.foldLeft((0, None: Option[Spot]))((a, b) => if (a._1 < b._1) (b._1, Some(b._2)) else a)
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
      private def minSlot(spot: Spot) = Math.max(spot.col - winningChips + 1, 0)

     private def maxSlot(spot: Spot) = Math.min(spot.col + winningChips, cols)

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


    def asList: Stream[Combination] = Stream(horWin, vertWin, horLost, vertLost)

  }


}
