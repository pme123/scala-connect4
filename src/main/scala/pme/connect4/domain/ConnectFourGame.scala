package pme.connect4.domain

import scala.util.{Success, Failure, Try}


/**
 * Created by mengelpa on 21.09.14.
 */
class ConnectFourGame {
  val rows = 6
  val cols = 7
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
  
  def winningPositions(chip: Chip): Option[(Spot,Spot,Spot,Spot)] = {
    None
  }

}
object Game {
  def apply(cols: Int,rows: Int):Game =
    Game((for (col <- 0 until cols) yield (Slot(col, rows))).toList)
}

case class Game(val slots: List[Slot]) {
  override def toString = (for(slot<-slots)yield(slot.toString)).mkString("\n")

  def findFirstEmpty(slotIndex: Int) : Option[Spot] = {
    if(slotIndex<slots.length){
      slots(slotIndex).findFirstEmpty
    }  else None
  }
  def dropChip(slotIndex: Int, chip:Chip): Try[Spot] = {
    slots(slotIndex).dropChip(chip)
  }

}

object Slot {
  def apply(col:Int, rows:Int):Slot = {
    new Slot(col, (for (row <- 0 until rows) yield (new Spot(SpaceChip, col,row))).toList)
  }
}


case class Slot(val col:Int, pSpots: List[Spot])  {
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



