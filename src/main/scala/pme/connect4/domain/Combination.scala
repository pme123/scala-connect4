package pme.connect4.domain

object Combination {
  type Evaluator = (Game, Chip, Spot)
}
abstract class Combination {
def eval(game: Game, chip: Chip, spot: Spot): Int
}

object Combinations {
import pme.connect4.domain.GameConfig._

  val pointsForHorMatch = 4
  val pointsForHorSpace = 2

  def evalBestMove(game: Game, activeChip: Chip): Int = {

    def evalPointsForSlot(spot:Spot): Int = {
      (for{
        comb <- allCombinations
      }yield {comb.eval(game, activeChip, spot)}).sum
    }

    val pointsForSlots:List[(Int,Spot)] = (for {
      slot <- game.slots
      spot <- slot.findFirstEmpty
    } yield {
      evalPointsForSlot(spot) -> spot
    })
    val chosenSpot = pointsForSlots.foldLeft((0,None:Option[Spot]))((a,b)=> if(a._1 < b._1) (b._1, Some(b._2)) else a)._2
    chosenSpot match {
      case Some(spot: Spot) => spot.col
      case  None => throw new IllegalArgumentException
    }
  }

  lazy val allCombinations: List[Combination] = {
    List(horizontalComb)
  }

  val horizontalComb = new Combination {

     def  eval(game: Game, chip: Chip, spot: Spot):Int = {
      var points=0
      val minSlot = Math.max(spot.col-winningChips+1,0)
      val maxSlot = Math.min(spot.col+winningChips,cols)
      for {
        col <- minSlot until maxSlot
       neighbor = game.slots(col).spots(spot.row)
      }yield{ neighbor.chip match {
        case SpaceChip => points+=pointsForHorSpace
        case neighborChip if neighborChip == chip  => points+=pointsForHorMatch
        case _ => if (col-minSlot < winningChips) points=0

      }}
        points
    }
  }
}
