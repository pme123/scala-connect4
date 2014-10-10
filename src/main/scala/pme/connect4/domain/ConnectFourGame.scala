package pme.connect4.domain

import pme.connect4.domain.Game.Winner
import pme.connect4.domain.GameConfig._

import scala.collection.immutable.::
import scala.util.{Failure, Success, Try}

class ConnectFourGame {


  val game = Game(cols, rows)

  def findFirstEmptySpot(slotIndex: Int): Option[Spot] = {
    game.findFirstEmpty(slotIndex)
  }

  def findFirstTakenSpot(slotIndex: Int): Option[Spot] = {
    game.findFirstTaken(slotIndex)
  }

  def dropChip(slotIndex: Int, chip: Chip): Try[Spot] = {
    game.dropChip(slotIndex, chip)
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

  def apply(cols: Int, rows: Int): Game =
    Game((for (col <- 0 until cols) yield Slot(col, rows)).toList)

  sealed abstract class WinDirection

  case object Hor extends WinDirection

  case object Ver extends WinDirection

  case object Diag extends WinDirection

}

case class Game(slots: List[Slot]) {



  case class Attempt(offset: (Int, Int), spots: List[Spot])

  override def toString = (for (slot <- slots) yield slot.toString).mkString("\n")

  def retrieveSpot(col: Int, row: Int): Spot = {
    findSpot(col, row) match {
      case Some(spot) => spot
      case None => throw new IllegalArgumentException
    }
  }

  def findSpot(col: Int, row: Int): Option[Spot] =
    if (col < 0 || col >= cols || row < 0 || row >= rows) None
    else Some(slots(col).findSpot(row))

  def findSpotInRowBefore(spot: Spot): Option[Spot] = {
    findSpot(spot.col - 1, spot.row)
  }

  def findSpotInRowAfter(spot: Spot): Option[Spot] = {
    findSpot(spot.col + 1, spot.row)
  }

  def findSpotInColBelow(spot: Spot): Option[Spot] = {
    findSpot(spot.col, spot.row - 1)
  }

  def findSpotInColAbove(spot: Spot): Option[Spot] = {
    findSpot(spot.col, spot.row + 1)
  }

  def findFirstEmpty(slotIndex: Int): Option[Spot] = {
    if (slotIndex < slots.length) {
      slots(slotIndex).findFirstEmpty
    } else None
  }

  def findFirstTaken(slotIndex: Int): Option[Spot] = {
    if (slotIndex < slots.length) {
      slots(slotIndex).findFirstTaken
    } else None
  }
  def countSpaceBefore(spot: Spot):Int = {
    findSpotInRowBefore(spot) match {
      case Some(newSpot) if newSpot.chip == SpaceChip =>  1 + countSpaceBefore(newSpot)
      case _ =>0
    }
  }
  def countSpaceAfter(spot: Spot):Int = {
    findSpotInRowAfter(spot) match {
      case Some(newSpot) if newSpot.chip == SpaceChip =>  1 + countSpaceAfter(newSpot)
      case _ =>0
    }
  }

  def checkSpotBelow(spot: Spot): Boolean = {
    (for {belowSpot <- findSpotInColBelow(spot)
          if belowSpot.chip == SpaceChip} yield belowSpot).isEmpty
  }

  def dropChip(slotIndex: Int, chip: Chip): Try[Spot] = {
    slots(slotIndex).dropChip(chip)
  }

  def winningSpots(chip: Chip): List[Winner] = {

    val matchedSpots = (for {
      slot <- slots
      spot <- slot.spots
    } yield spot).filter(spot => spot.chip == chip)

    def nextAttempts(spot: Spot): List[Option[Attempt]] = {

      def nextAttempt(attempt: Attempt): Option[Attempt] = {
        val spot = attempt.spots.last
        val colOffset = attempt.offset._1
        val rowOffset = attempt.offset._2
        if (spot.col + colOffset < cols && spot.row + rowOffset < rows) {
          matchedSpots
            .filter(_.col == spot.col + colOffset)
            .filter(_.row == spot.row + rowOffset)
          match {
            case x :: xs =>
              val next = Attempt((colOffset, rowOffset), attempt.spots ++ List(x))
              if (next.spots.length == winningChips) Some(next) else nextAttempt(next)
            case _ => None
          }
        } else None
      }
      List(nextAttempt(Attempt((0, 1), List(spot)))
        , nextAttempt(Attempt((1, 0), List(spot)))
        , nextAttempt(Attempt((1, 1), List(spot)))
        , nextAttempt(Attempt((-1, 1), List(spot))))
    }

    if (matchedSpots.isEmpty) Nil
    else {
      for {
        matchedSpot <- matchedSpots
        nextAttemptOpt <- nextAttempts(matchedSpot)
        nextAttempt <- nextAttemptOpt
      } yield {
        nextAttempt.spots
      }
    }
  }


}

object Slot {
  def apply(col: Int, rows: Int): Slot = {
    new Slot(col, (for (row <- 0 until rows) yield new Spot(SpaceChip, col, row)).toList)
  }
}


case class Slot(col: Int, pSpots: List[Spot]) {

  var spots = pSpots

  override def toString = spots.mkString(",")

  def findSpot(row: Int) = {
    spots(row)
  }

  def findFirstEmpty: Option[Spot] = {
    spots.find(spot => spot.chip == SpaceChip)
  }

  def findFirstTaken: Option[Spot] = {
    spots.filter(spot => spot.chip != SpaceChip).lastOption
  }

  def dropChip(chip: Chip): Try[Spot] = {
    findFirstEmpty match {
      case Some(spot) =>
        val newSpot = new Spot(chip, spot.col, spot.row)
        spots = (newSpot :: spots.filterNot(oldSpot => spot == oldSpot).toList).sortWith((left, right) => left.row < right.row)
        Success(newSpot)
      case None => Failure(new IllegalArgumentException(s"No Empty Spot in the column $spots.head.col"))
    }

  }

}

case class Spot(chip: Chip, col: Int, row: Int) {
}








