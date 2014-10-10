package pme.connect4.domain

import pme.connect4.domain.GameConfig._

import scala.collection.immutable.IndexedSeq
import scala.util.control.Breaks._

object Combination {
  type Evaluator = (Game, Chip, Spot)
}

abstract class Combination {
  def eval: Int
}

object Combinations {

  val pointsForVerMatch = 5
  val pointsForHorMatch = 6
  val pointsForHorSpace = 2
  val pointsMax = 2000

  def evalBestMove(game: Game, activeChip: Chip): Int = {

    def evalPointsForSlot(spot: Spot): Int = {
      (for {
        comb <- allCombinations(game, activeChip, spot)
      } yield {
        comb.eval
      }).sum
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

  def allCombinations(game: Game, activeChip: Chip, spot: Spot): List[Combination] = {
    AllCombinations(game, activeChip, spot).asList
  }

  case class AllCombinations(game: Game, activeChip: Chip, spot: Spot) {
    lazy val horWin = new HorCombination {
      def eval: Int = {
        var maxCount = 0
        for {
          attempt <- neighbors
            .filter(neighbor => {
            val exists = !neighbor.exists(spotEntry => spotEntry.chip == activeChip.other)
            exists
          })
          if attempt.length == winningChips

          countChips = attempt.count(entry => entry.chip == activeChip)
          countSpace = attempt.count(entry => entry.chip == SpaceChip && game.checkSpotBelow(entry))

          countBeforeBorder = (for {spotBefore <- game.findSpotInRowBefore(attempt.head)
                                    if spotBefore.chip == SpaceChip
                                    if game.checkSpotBelow(spotBefore)} yield 1).getOrElse(0)
          countAfterBorder = (for {spotAfter <- game.findSpotInRowAfter(attempt.last)
                                   if spotAfter.chip == SpaceChip
                                   if game.checkSpotBelow(spotAfter)} yield 1).getOrElse(0)
        } {
          maxCount = maxCount max (pointsForHorSpace * countSpace + pointsForHorMatch * countChips + countBeforeBorder + countAfterBorder)
        }
        maxCount
      }
    }
    lazy val horDefence = new HorCombination {
      def eval: Int = {
        def hasNoChipsOnEdge(attempts: IndexedSeq[Spot]): Boolean = {
          val filtered = attempts.filter(attempt =>
            attempt.chip != SpaceChip)
          val exists = filtered.exists {
            attempt =>
              val col = attempt.col
              val row = attempt.row
              val result = (
                col == 0
                  || col == cols
                  || (col == attempts(0).col && game.retrieveSpot(col - 1, row).chip == activeChip.other)
                  || (col == attempts(attempts.size - 1).col && game.retrieveSpot(col + 1, row).chip == activeChip.other)
                )
              result
          }
          !exists
        }
        // [o][?][x][x] || [x][x][?][o]
        def hasSpaceBetween(attempt: IndexedSeq[Spot]): Boolean = {
          ((spot.col == attempt.head.col && attempt.tail.head.chip == SpaceChip)
            || (spot.col == attempt.last.col && attempt(winningChips - 2).chip == SpaceChip))
        }


        var maxCount = 0
        if ((for {
          attempt <- neighbors
          if attempt.length == winningChips
          count = attempt.count(entry => entry.chip == activeChip.other)
          if {
            count >= winningChips - 1 ||
              (count >= winningChips - 2 && hasNoChipsOnEdge(attempt) && !hasSpaceBetween(attempt))
          }
        } yield {
          maxCount = count max maxCount
          attempt
        }).isEmpty)
          0
        else pointsMax * maxCount
      }
    }

    abstract class HorCombination extends Combination {
      def minSlot(spot: Spot) = Math.max(spot.col - winningChips + 1, 0)

      def maxSlot(spot: Spot) = Math.min(spot.col + winningChips, cols)

      val neighbors: Iterable[IndexedSeq[Spot]] = (for {
        attempt <- minSlot(spot) to maxSlot(spot) - winningChips
        col <- attempt until attempt + winningChips
        neighbor = game.slots(col).spots(spot.row)
      // if neighbor.chip != activeChip
      } yield {
        (attempt, neighbor)
      })
        .groupBy(neighbor => neighbor._1)
        .values
        .map(attempt => attempt.map(entry => entry._2))
      .filter(_.size == winningChips)

    }

    lazy val vertComb = new Combination {
      def eval: Int = {
        var count = game.countSpotsBelow(spot, activeChip)
        count * pointsForVerMatch
      }
    }

    lazy val vertDefence = new Combination {
      def eval: Int = {
        var count = 0
        breakable(for {
          row <- spot.row - 1 to 0 by -1
          neighbor = game.slots(spot.col).spots(row)

        } yield {
          if (neighbor.chip == activeChip.other) count += 1 else break()
        })
        if (winningChips - count == 1) count * pointsMax else 0
      }
    }

    def asList = List(horWin, horDefence, vertComb, vertDefence)

  }


}
