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

  def allCombinations(game: Game, activeChip: Chip, spot: Spot): List[Combination] = { AllCombinations(game, activeChip, spot).asList }

case class AllCombinations(game: Game, activeChip: Chip, spot: Spot) {
      lazy val horWin = new HorCombination {
        def eval: Int = {
 /*         var points = 0
          val minSlot = super.minSlot(spot)
          val maxSlot = super.maxSlot(spot)
          breakable(for {
            col <- minSlot until maxSlot
            neighbor = game.slots(col).spots(spot.row)
          } yield {
            neighbor.chip match {
              case SpaceChip => game.findFirstEmpty(col) match {
                case Some(neighbor: Spot) if neighbor.row == spot.row => points += pointsForHorSpace
                case Some(neighbor: Spot) => points += pointsForHorSpace / 2
                case None => throw new IllegalArgumentException
              }
              case neighborChip if neighborChip == activeChip => points += pointsForHorMatch
              case _ => if (col - minSlot < winningChips) points = 0
                if (maxSlot - col < winningChips) break()
            }
          })
          points*/

          var maxCount =0
          for {
            attempts <- neighbors.groupBy(neighbor => neighbor._1)
          .filter(neighbor => {
              val exists = !neighbor._2.exists(spotEntry => spotEntry._2.chip == activeChip.other)
              exists
            })
            if attempts._2.length == winningChips

              countChips = attempts._2.count(entry => entry._2.chip == activeChip)
            countSpace = attempts._2.count(entry => entry._2.chip == SpaceChip && game.checkSpotBelow(entry._2))

            countBeforeBorder = (for{spotBefore <- game.findSpotInRowBefore(attempts._2.head._2)
            if spotBefore.chip == SpaceChip
          if  game.checkSpotBelow(spotBefore)} yield 1).getOrElse(0)
            /* countBeforeBorder = 0 if(game.findSpotInRowBefore(attempts._2.head._2))
             if((for {belowSpot <- game.findSpotInColBelow(attempts._2.head._2)
                                       if belowSpot.chip == SpaceChip } yield belowSpot).isEmpty) 1 min game.countSpaceBefore(attempts._2.head._2)*/
            countAfterBorder =(for{spotAfter <- game.findSpotInRowAfter(attempts._2.last._2)
                                   if spotAfter.chip == SpaceChip
                                   if  game.checkSpotBelow(spotAfter)} yield 1).getOrElse(0)
          } {
            maxCount = maxCount max( pointsForHorSpace * countSpace + pointsForHorMatch * countChips + countBeforeBorder+ countAfterBorder)
          }

           maxCount
        }
      }
      lazy val horDefence = new HorCombination {
        def eval: Int = {
          def hasNoChipsOnEdge(attempts: (Int, IndexedSeq[(Int, Spot)])): Boolean = {
            val filtered = attempts._2.filter(attempt =>
              attempt._2.chip != SpaceChip)
            val exists = filtered.exists {
              attempt =>
                val col = attempt._2.col
                val row = attempt._2.row
                val result = (
                  col == 0
                    || col == cols
                    || (col == attempts._2(0)._2.col && game.retrieveSpot(col - 1, row).chip == activeChip.other)
                    || (col == attempts._2(attempts._2.size - 1)._2.col && game.retrieveSpot(col + 1, row).chip == activeChip.other)
                  )
                result
            }
            !exists
          }

          var maxCount = 0
          if ((for {
            attempts <- neighbors.groupBy(neighbor => neighbor._1)
            if attempts._2.length == winningChips
            count = attempts._2.count(entry => entry._2.chip == activeChip.other)
            if {
              count >= winningChips - 1 ||
                (count >= winningChips - 2 && hasNoChipsOnEdge(attempts))
            }
          } yield {
            maxCount = count max maxCount
            attempts._2
          }).isEmpty)
            0
          else pointsMax * maxCount
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
          if (winningChips - count == 1) pointsMax else 0
        }
      }

      abstract class HorCombination extends Combination {
        def minSlot(spot: Spot) = Math.max(spot.col - winningChips + 1, 0)

        def maxSlot(spot: Spot) = Math.min(spot.col + winningChips, cols)

        val neighbors = for {
          attempt <- minSlot(spot) to maxSlot(spot) - winningChips
          col <- attempt until attempt + winningChips
          neighbor = game.slots(col).spots(spot.row)
         // if neighbor.chip != activeChip
        } yield {
          (attempt, neighbor)
        }
      }
      
      def asList =       List(horWin, horDefence, vertDefence)

    }


}
