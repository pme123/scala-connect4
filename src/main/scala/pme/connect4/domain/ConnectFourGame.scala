package pme.connect4.domain

import pme.connect4.domain.Game.Winner

import scala.util.{Success, Failure, Try}


/**
 * Created by mengelpa on 21.09.14.
 */
class ConnectFourGame {
import GameConfig._

  val game = Game(cols, rows)

  def findFirstEmptySlot(slotIndex: Int) : Option[Spot] = {
    game.findFirstEmpty(slotIndex)
  }

  def dropChip(slotIndex: Int, chip:Chip): Try[Spot] = {
    game.dropChip(slotIndex,chip)
  }
  def hasEmptySlot(slotIndex: Int): Boolean = {
    game.findFirstEmpty(slotIndex) != None
  }
  
  def winningSpots(chip: Chip): List[Winner] = {
    game.winningSpots(chip)
  }

}

object GameConfig {
  val rows = 6
  val cols = 7
  val winningChips = 4
}
object Game {
  type Winner = List[Spot]
  def apply(cols: Int,rows: Int):Game =
    Game((for (col <- 0 until cols) yield (Slot(col, rows))).toList)
}

case class Game(val slots: List[Slot]) {
    import GameConfig._
  override def toString = (for(slot<-slots)yield(slot.toString)).mkString("\n")

  def findFirstEmpty(slotIndex: Int) : Option[Spot] = {
    if(slotIndex<slots.length){
      slots(slotIndex).findFirstEmpty
    }  else None
  }
  def dropChip(slotIndex: Int, chip:Chip): Try[Spot] = {
    slots(slotIndex).dropChip(chip)
  }
  def winningSpots(chip: Chip): List[Winner] = {
    val vertWinners = for {
      slot <- slots
     winner: Winner <- slot.verticalWinningSpots(chip)
    }yield {
       winner
    }
    val horWinners = for {
      slot <- slots
      spot <- slot.spots
    } yield (spot)
    for {
      index <- 0 until cols
    }
    println(s"horWinners: $horWinners")
    vertWinners
  }
}

object Slot {
  def apply(col:Int, rows:Int):Slot = {
    new Slot(col, (for (row <- 0 until rows) yield (new Spot(SpaceChip, col,row))).toList)
  }
}


case class Slot(val col:Int, pSpots: List[Spot])  {
  import GameConfig._
  var spots = pSpots
  override def toString = spots.mkString(",")

  def findFirstEmpty : Option[Spot] = {
    spots.filter(spot => spot.chip==SpaceChip).headOption
  }
  def dropChip(chip:Chip): Try[Spot] = {
    findFirstEmpty  match {
     case Some(spot) =>
       val newSpot = new Spot(chip, spot.col, spot.row)
      spots= (newSpot::(spots.filterNot(oldSpot=> spot==oldSpot)).toList).sortWith((left,right)=>left.row<right.row)
       Success(newSpot)
     case None =>       Failure(new IllegalArgumentException(s"No Empty Spot in the column $spots.head.col"))
    }

  }
  def verticalWinningSpots(chip: Chip): Option[Winner] = {
    val matchedSpots = spots filter (spot => spot.chip==chip)

  val checkedSpots =  matchedSpots.foldLeft(Nil: List[Spot])((r,c:Spot) => r match {
      case x::xs => if(c.row-1==x.row) c::r else r
      case Nil => List(c)
    })

    if(matchedSpots.length<winningChips) None
    else Some(matchedSpots)
  }
}

case class Spot(chip: Chip, col:Int, row: Int)


sealed abstract class Chip

case object SpaceChip extends Chip {
  override def toString = "[ ]"
}

case object RedChip extends Chip {
  override def toString = "[x]"
}

case object YellowChip extends Chip {
  override def toString = "[o]"
}





