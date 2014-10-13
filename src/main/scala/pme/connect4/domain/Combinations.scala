package pme.connect4.domain

import pme.connect4.domain.GameConfig._

import scala.Stream._
import scala.collection.immutable.IndexedSeq


object Combinations {
  type Combination = Boolean

  def evalBestMove(game: Game, activeChip: Chip): Int = {

    def evalPointsForSlot(spot: Spot): Int = {
      val combs = allCombinations(game, activeChip, spot).takeWhile(comb => !comb)
      combs.size
    }
    val pointsForSlots: List[(Int, Spot)] = for {
      slot <- game.slots
      spot <- slot.findFirstEmpty
    } yield (evalPointsForSlot(spot) -> spot)
    val chosenSpot = pointsForSlots.foldLeft((Int.MaxValue, None: Option[Spot]))((a, b) => if (a._1 > b._1) (b._1, Some(b._2)) else a)
    chosenSpot._2 match {
      case Some(spot: Spot) => spot.col
      case None => throw new IllegalArgumentException
    }
  }

  def allCombinations(game: Game, activeChip: Chip, spot: Spot): Stream[Combination] = {
    AllCombinations(game, activeChip, spot).asStream
  }

  case class AllCombinations(game: Game, activeChip: Chip, spot: Spot) {
    // VERTICAL
    lazy val vertWin = VertCombination.eval(activeChip)

    lazy val vertLost = VertCombination.eval(activeChip.other)


    object VertCombination {
      protected[domain] def eval(chip: Chip): Boolean = (winningChips - 1) == game.countSpotsBelow(spot, chip)
    }

    // HORIZONTAL
    lazy val horWin: Combination = HorCombination.findChips(activeChip)

    lazy val horLost = HorCombination.findChips(activeChip.other)

    object HorCombination {
      private def minSpot(spot: Spot) = spot.col - winningChips + 1 max 0

      private def maxSpot(spot: Spot) = spot.col + winningChips min cols - 1

      private val neighbors: Iterable[IndexedSeq[Spot]] = {
        (for {
          attempt <- minSpot(spot) to maxSpot(spot) - winningChips
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
    lazy val diagUpWin: Combination = DiagUpCombination.findChips(activeChip)
    lazy val diagUpLost = DiagUpCombination.findChips(activeChip.other)

    object DiagUpCombination {
      private def minSpot(spot: Spot): Spot = {
        val minCol = (spot.col - winningChips + 1 max 0)
        val minRow = (spot.row - winningChips + 1 max 0)
        def innerMinSpot(sp: Spot): Spot = {
          sp match {
            case sp if sp.col == minCol || sp.row == minRow => sp
            case sp => innerMinSpot(game.retrieveSpot((sp.col - 1), (sp.row - 1)))
          }
        }
        innerMinSpot(spot)
      }

      private def allSpots(spot: Spot): List[Spot] = {
        val maxCol = (spot.col + winningChips min cols - 1)
        val maxRow = (spot.row + winningChips min rows - 1)
        def innerAllSpots(sp: Spot): List[Spot] = {
          if (sp.col == maxCol || sp.row == maxRow) List(sp)
          else sp :: innerAllSpots(game.retrieveSpot(sp.col + 1, sp.row + 1))
        }
        innerAllSpots(minSpot(spot))
      }

      private val neighbors: Iterable[List[Spot]] = {
        val attempts = (for {
          aSpot: Spot <- allSpots(spot)
          index <- 0 until winningChips
          if aSpot.col + index < cols && aSpot.row + index < rows
          if game.checkSpotBelow(aSpot)
        } yield (aSpot.col, game.slots(aSpot.col + index).spots(aSpot.row + index))
          )
          .groupBy(neighbor => neighbor._1)
        attempts.values
          .map(attempt => attempt.map(entry => entry._2))
          .filter(_.size == winningChips)
      }

      protected[domain] def findChips(chip: Chip): Boolean = {
        neighbors.filter(spots => spots.count(_.chip == chip) == winningChips - 1).nonEmpty
      }
    }

    // VERTICAL
    lazy val takeWhatever: Combination = true

    def asStream: Stream[Combination] = horWin #:: vertWin #:: horLost #:: vertLost #:: diagUpLost #:: takeWhatever #:: empty


  }


}
