package pme.connect4.domain

import pme.connect4.domain.GameConfig._

import scala.Stream._
import scala.collection.immutable.IndexedSeq


object Combinations {
  type Combination = Boolean

  def evalBestMove(game: Game, activeChip: Chip): Int = {

    def evalPointsForSlot(spot: Spot): Int = {
      val combs = allCombinations(spot).takeWhile(comb => !comb)
      println(s"spot: $spot: ${combs.toList}")
      combs.size
    }
    def allCombinations(spot: Spot): Stream[Combination] = {
      AllCombinations(game, activeChip, spot).asStream
    }
    def isALost(spot: Spot) = {
      AllCombinations(game, activeChip, spot).isALost
    }
    def isAWin(spot: Spot) = {
      AllCombinations(game, activeChip, spot).isAWin
    }

    val pointsForSlots: List[(Int, Spot)] = for {
      slot <- game.slots
      spot <- slot.findFirstEmpty
    } yield evalPointsForSlot(spot) -> spot
    val allSpots = pointsForSlots.sortBy(entry => entry._1)
      .map(entry => entry._2)
      .filter(spot => game.findSpotInColAbove(spot) match {
      case Some(spAbove) if !isAWin(spot) && isALost(spAbove) => false
      case _ => true
    })

    allSpots match {
      case Nil => throw new IllegalArgumentException
      case head :: tail => head.col
    }



  }

  case class AllCombinations(game: Game, activeChip: Chip, spot: Spot) {
    // VERTICAL
    lazy val vertWin = VertCombination.eval(activeChip)

    lazy val vertLost = VertCombination.eval(activeChip.other)


    object VertCombination {
      protected[domain] def eval(chip: Chip): Boolean = (winningChips - 1) == game.countSpotsBelow(spot, chip)
    }

    // HORIZONTAL
    lazy val horWin = new HorCombination(winningChips).findChips(activeChip)
    lazy val horLost = new HorCombination(winningChips).findChips(activeChip.other)
    lazy val horLostWith2 = new HorWith2Combination(winningChips).findChips(activeChip.other)
    lazy val horLostWith2AndSpace = new HorWith2Combination(winningChips + 1).findChips(activeChip.other)
    lazy val horOtherSpace5 = new HorWithSpaces(winningChips+1).findChips(activeChip)
    lazy val horOtherSpace4 = new HorWithSpaces(winningChips).findChips(activeChip)

    class HorCombination(nrChips: Int) {
      private def minSpot(spot: Spot) = spot.col - nrChips + 1 max 0

      private def maxSpot(spot: Spot) = spot.col + nrChips - 1 min cols - 1

      protected def neighbors: Iterable[IndexedSeq[Spot]] = {
        (for {
          attempt <- minSpot(spot) to maxSpot(spot) - (nrChips - 1)
          col <- attempt until attempt + nrChips
          neighbor = game.slots(col).spots(spot.row)
        } yield {
          (attempt, neighbor)
        })
          .groupBy(neighbor => neighbor._1)
          .values
          .map(attempt => attempt.map(entry => entry._2))
          .filter(_.size == nrChips)
      }

      protected[domain] def findChips(chip: Chip): Boolean = {
        neighbors.filter(spots => spots.count(_.chip == chip) == winningChips - 1).nonEmpty
      }
    }

    class HorWith2Combination(nrChips: Int) extends HorCombination(nrChips) {

      override def findChips(chip: Chip): Boolean = {
        neighbors.filter(spots => spots.count(_.chip == chip) == 2)
        .filter(spots => List(spots.head.chip, spots.last.chip).forall(_ == SpaceChip))
          .nonEmpty
      }
    }

    class HorWithSpaces(nrChips: Int) extends HorCombination(nrChips) {

      override def findChips(chip: Chip): Boolean = {
        neighbors.filter(spots => spots.forall(spot => spot.chip == SpaceChip && game.checkSpotBelow(spot)))
       .nonEmpty
      }
    }

    // DIAGONAL up (from left-down to right-up)
    lazy val diagUpWin: Combination = DiagUpCombination().findChips(activeChip)
    lazy val diagUpLost = DiagUpCombination().findChips(activeChip.other)

    case class DiagUpCombination() extends DiagCombination {
      protected def nextRowOffset = 1

    }

    // Diagonal down (from left-up to right-down)
    lazy val diagDownWin: Combination = DiagDownCombination().findChips(activeChip)
    lazy val diagDownLost = DiagDownCombination().findChips(activeChip.other)

    case class DiagDownCombination() extends DiagCombination {
      protected override def minRow = super.maxRow

      protected override def maxRow = super.minRow

      protected def nextRowOffset = -1
    }

    abstract class DiagCombination {
      protected def minSpot: Spot = {
        def innerMinSpot(sp: Spot): Spot = {
          if (sp.col == minCol || sp.row == minRow) sp
          else innerMinSpot(game.retrieveSpot(sp.col - 1, sp.row - nextRowOffset))
        }
        innerMinSpot(spot)
      }

      private def allSpots: List[Spot] = {
        def innerAllSpots(sp: Spot): List[Spot] = {
          if (sp.col == maxCol || sp.row == maxRow) List(sp)
          else sp :: innerAllSpots(game.retrieveSpot(sp.col + 1, sp.row + nextRowOffset))
        }
        innerAllSpots(minSpot)
      }

      protected val minCol: Int = spot.col - winningChips + 1 max 0
      protected val maxCol = spot.col + winningChips min cols - 1

      protected def minRow: Int = spot.row - winningChips + 1 max 0

      protected def maxRow = spot.row + winningChips min rows - 1

      protected def nextRowOffset: Int


      private val neighbors: Iterable[List[Spot]] = {
        val attempts = (for {
          aSpot: Spot <- allSpots
          index <- 0 until winningChips
          if aSpot.col + index < cols && aSpot.row + nextRowOffset * index < rows && aSpot.row + nextRowOffset * index >= 0
          if game.checkSpotBelow(aSpot)
        } yield (aSpot.col, game.slots(aSpot.col + index).spots(aSpot.row + nextRowOffset * index))
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

    // If no strategy found
    lazy val takeWhatever: Combination = true

    def asStream: Stream[Combination] = (
      horWin #:: vertWin #:: diagUpWin #:: diagDownWin // wins
        #:: horLost #:: vertLost #:: diagUpLost #:: diagDownLost #:: horLostWith2 #:: horLostWith2AndSpace // losts
        #:: horOtherSpace5 #:: horOtherSpace4 #:: takeWhatever #:: empty // other
      )

    def isALost = {
      horLost || vertLost || diagUpLost || diagDownLost || horLostWith2 || horLostWith2AndSpace
    }
    def isAWin = {
      horWin || vertWin || diagUpWin || diagDownWin
    }

  }


}
