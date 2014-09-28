package pme.connect4.domain

import pme.connect4.domain.Game.{WinDirection, Winner, _}
import pme.connect4.domain.GameConfig._

import scala.collection.immutable.::
import scala.util.{Failure, Success, Try}


/**
 * Created by mengelpa on 21.09.14.
 */
class ConnectFourGame {


  val game = Game(cols, rows)

  def findFirstEmptySlot(slotIndex: Int): Option[Spot] = {
    game.findFirstEmpty(slotIndex)
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
    Game((for (col <- 0 until cols) yield (Slot(col, rows))).toList)

  sealed abstract class WinDirection

  case object Hor extends WinDirection

  case object Ver extends WinDirection

  case object Diag extends WinDirection

}

case class Game(val slots: List[Slot]) {
  override def toString = (for (slot <- slots) yield (slot.toString)).mkString("\n")

  def findFirstEmpty(slotIndex: Int): Option[Spot] = {
    if (slotIndex < slots.length) {
      slots(slotIndex).findFirstEmpty
    } else None
  }

  def dropChip(slotIndex: Int, chip: Chip): Try[Spot] = {
    slots(slotIndex).dropChip(chip)
  }

  def winningSpots(chip: Chip): List[Winner] = {
    val matchedSpots = (for {
      slot <- slots
      spot <- slot.spots
    } yield (spot)).filter(spot => spot.chip == chip)

    def winningSpots(spots: List[Spot], attempt: Winner): List[Winner] = {

      def nextSpots(spot: Spot): List[Option[Spot]] = {
        println(s"spot: $spot")

        def nextSpot(colOffset: Int, rowOffset: Int): Option[Spot] = {
          if (spot.col + colOffset < cols && spot.row + rowOffset < rows) {
            spots
              .filter(_.col == spot.col + colOffset)
              .filter(_.row == spot.row + rowOffset)
            match {
              case x :: xs =>
                Some(x)
              case _ => None
            }
          } else None
        }
        List(nextSpot(0, 1), nextSpot(1, 0), nextSpot(1, 1), nextSpot(-1, 1))
      }
      println(s"spots: $spots")
      println(s"attempt: $attempt")
      spots match {
        case spot :: tail =>
          val solutions = (for {nextSpotOpt <- nextSpots(attempt.last)
                                nextSpot <- nextSpotOpt

          } yield {
            println(s"nextSpot: $nextSpot")
            winningSpots(spots.filterNot(_==nextSpot), attempt ++ List(nextSpot))
          }).flatten
          solutions

        case Nil => List(attempt)
      }
    }
    if (matchedSpots isEmpty) Nil
    else {
      val allWinners = for(spot <- matchedSpots)yield(winningSpots(matchedSpots.filterNot(_==spot), List(spot)))
      println(s"allWinners: "+allWinners)
      allWinners.flatten.filter(_.length == winningChips)
    }
  }

  /*   def winningSpots(chip: Chip): List[Winner] = {
       def winningSpots(attempt: List[Spot], direction: WinDirection): List[Winner] = attempt match {
       case x :: y :: xs if (attempt.size == winningChips) => List(attempt)
       case x :: y :: xs if (direction == Hor) => {
         for {
           nextSpot <- nextSpot(attempt.last, direction)
         } {
           println(s"nextSpot: $nextSpot")
         }
         List(attempt)
       }
       case x :: xs => nextAttempts(attempt, direction)
       case Nil => throw IllegalAccessError
     }
     def nextSpot(spot: Spot, direction: WinDirection): Option[Spot] = (direction match {
       case Hor if (spot.col + 1 < cols) => Some(slots(spot.col + 1).spots(spot.row))
       case Ver if (spot.row + 1 < rows) => Some(slots(spot.col + 1).spots(spot.row + 1))
       case Diag if (spot.col + 1 < cols && spot.row + 1 < rows) => Some(slots(spot.col + 1).spots(spot.row + 1))
       case _ => None
     }).filter(nextSpot => spot.chip == nextSpot.chip)

     def nextAttempts(attempt: List[Spot], direction: WinDirection) = {
       nextSpot(attempt.last, direction) match {
         case Some(spot) => winningSpots(attempt ++ List(spot), direction)
         case None =>
       }
       direction match {
         case Hor => for (nextSpot <- nextSpot(attempt.last, direction)) yield (nextSpot)
         case Ver if (spot.row + 1 < rows) => Some(slots(spot.col + 1).spots(spot.row + 1))
         case Diag if (spot.col + 1 < cols && spot.row + 1 < rows) => Some(slots(spot.col + 1).spots(spot.row + 1))
         case _ => None
       }
     }
     winningSpots(List(slots(0).spots(0)), Diag)
   }
  */
  def winningSpots2(chip: Chip): List[Winner] = {
    val vertWinners = for {
      slot <- slots
      winner: Winner <- slot.verticalWinningSpots(chip)
    } yield {
      winner
    }
    val horSpots = (for {
      slot <- slots
      spot <- slot.spots
    } yield (spot))
      .groupBy(spot => spot.row).values

    val horWinners = for {
      horSpot <- horSpots
      winner: Winner <- Slot.horWinningSpots(chip, horSpot)
    } yield (winner)

    vertWinners ++ horWinners
  }

}

object Slot {
  def apply(col: Int, rows: Int): Slot = {
    new Slot(col, (for (row <- 0 until rows) yield (new Spot(SpaceChip, col, row))).toList)
  }

  def horWinningSpots(chip: Chip, spots: List[Spot]): Option[Winner] = {
    val matchedSpots = (spots filter (spot => spot.chip == chip))
      .foldLeft(Nil: List[Spot])((r, c: Spot) => r match {
      case x :: xs => if (c.col - 1 == x.col) c :: r else List(c)
      case Nil => List(c)
    })
    if (matchedSpots.length < winningChips) None
    else Some(matchedSpots)
  }
}


case class Slot(val col: Int, pSpots: List[Spot]) {

  var spots = pSpots

  override def toString = spots.mkString(",")

  def findFirstEmpty: Option[Spot] = {
    spots.filter(spot => spot.chip == SpaceChip).headOption
  }

  def dropChip(chip: Chip): Try[Spot] = {
    findFirstEmpty match {
      case Some(spot) =>
        val newSpot = new Spot(chip, spot.col, spot.row)
        spots = (newSpot :: (spots.filterNot(oldSpot => spot == oldSpot)).toList).sortWith((left, right) => left.row < right.row)
        Success(newSpot)
      case None => Failure(new IllegalArgumentException(s"No Empty Spot in the column $spots.head.col"))
    }

  }

  def verticalWinningSpots(chip: Chip): Option[Winner] = {
    val matchedSpots = (spots filter (spot => spot.chip == chip))
      .foldLeft(Nil: List[Spot])((r, c: Spot) => r match {
      case x :: xs => if (c.row - 1 == x.row) c :: r else List(c)
      case Nil => List(c)
    })
    if (matchedSpots.length < winningChips) None
    else Some(matchedSpots)
  }
}

case class Spot(chip: Chip, col: Int, row: Int) {
}


sealed abstract class Chip

case object SpaceChip extends Chip {
  override def toString = "[ ]"
}

case object RedChip extends Chip {
  override def toString = "[r]"
}

case object YellowChip extends Chip {
  override def toString = "[y]"
}





