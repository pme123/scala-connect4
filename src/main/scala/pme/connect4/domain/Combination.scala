package pme.connect4.domain

import pme.connect4.domain.GameConfig._

import scala.util.control.Breaks._

object Combination {
  type Evaluator = (Game, Chip, Spot)
}

abstract class Combination {
  def eval(game: Game, chip: Chip, spot: Spot): Int
}

object Combinations {

  val pointsForHorMatch = 6
  val pointsForHorSpace = 2
  val pointsMax = 2000

  def evalBestMove(game: Game, activeChip: Chip): Int = {

    def evalPointsForSlot(spot: Spot): Int = {
      (for {
        comb <- allCombinations
      } yield {
        comb.eval(game, activeChip, spot)
      }).sum
    }

    val pointsForSlots: List[(Int, Spot)] = (for {
      slot <- game.slots
      spot <- slot.findFirstEmpty
    } yield {
      evalPointsForSlot(spot) -> spot
    })
    val chosenSpot = pointsForSlots.foldLeft((0, None: Option[Spot]))((a, b) => if (a._1 < b._1) (b._1, Some(b._2)) else a)
    println(s"chosenSpot: $chosenSpot")
    chosenSpot._2 match {
      case Some(spot: Spot) => spot.col
      case None => throw new IllegalArgumentException
    }
  }

  lazy val allCombinations: List[Combination] = {
    List(horWin, horDefence, vertDefence)
  }

  val horWin = new HorCombination {

    def eval(game: Game, chip: Chip, spot: Spot): Int = {
      var points = 0
      val minSlot = super.minSlot(spot)
      val maxSlot = super.maxSlot(spot)
      breakable(for {
        col <- minSlot until maxSlot
        neighbor = game.slots(col).spots(spot.row)
      } yield {
        neighbor.chip match {
          case SpaceChip => game.findFirstEmpty(col) match {
            case Some(neighbor: Spot) if (neighbor.row == spot.row) => points += pointsForHorSpace
            case Some(neighbor: Spot) => points += pointsForHorSpace / 2
            case None => throw new IllegalArgumentException
          }
          case neighborChip if neighborChip == chip => points += pointsForHorMatch
          case _ => if (col - minSlot < winningChips) points = 0
            if (maxSlot - col < winningChips) break
        }
      })
      points
    }
  }

  val horDefence = new HorCombination {

    def eval(game: Game, chip: Chip, spot: Spot): Int = {
      println("minSlot(spot): " + minSlot(spot))
      println("maxSlot(spot): " + maxSlot(spot))
      val neighbors = for {
        attempt <- minSlot(spot) to maxSlot(spot) - winningChips
        col <- attempt until attempt + winningChips
        neighbor = game.slots(col).spots(spot.row)
        if (neighbor.chip != chip)
      } yield {
        (attempt, neighbor)
      }

      if ((for {
        attempts <- neighbors.groupBy(neighbor => neighbor._1)
        attempt<-attempts._2
        if(attempts._2.length == winningChips)
        if(attempts._2.count(entry => entry._2.chip == chip.other) >= winningChips - 2)
      } yield {
        attempts._2
      }).isEmpty)
        0
      else pointsMax
    }
  }
  val vertDefence = new Combination {

    def eval(game: Game, chip: Chip, spot: Spot): Int = {
      var count = 0
      breakable(for {
        row <- spot.row - 1 to 0 by -1
        neighbor = game.slots(spot.col).spots(row)

      } yield {
        if (neighbor.chip == chip.other) count += 1 else break
      })
      if (winningChips - count == 1) pointsMax else 0
    }
  }

  abstract class HorCombination extends Combination {
    def minSlot(spot: Spot) = Math.max(spot.col - winningChips + 1, 0)

    def maxSlot(spot: Spot) = Math.min(spot.col + winningChips, cols)
  }

}
