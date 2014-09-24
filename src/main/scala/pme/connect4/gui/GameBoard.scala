package pme.connect4.gui

import javafx.event.EventHandler
import javafx.scene.input.MouseEvent

import pme.connect4.domain.{GameConfig, Chip, ConnectFourGame, RedChip, YellowChip}

import scalafx.animation.TranslateTransition
import scalafx.scene.effect.InnerShadow
import scalafx.scene.layout.Pane
import scalafx.scene.paint.Color
import scalafx.scene.shape._
import scalafx.util.Duration


/**
 * Created by pascal.mengelt on 18.09.2014.
 */
class GameBoard(val gameSize: (Double, Double)) extends Pane {

  import GameConfig._
  import ChipView._

  val paneOffsetX = 40
  val paneOffsetY = 80
  val horFieldCount: Int = 7
  val verFieldCount: Int = 6
  val slotMargin = 2
  val boardWidth = gameSize._1 - paneOffsetX * 2
  val boardHeight = gameSize._2 - paneOffsetY * 2
  val fieldWidth = boardWidth / horFieldCount
  val fieldHeight = boardHeight / verFieldCount
  val fourConnect = new ConnectFourGame
  var activeChip: Chip = RedChip

  val chipsToPlay: Seq[ChipView] = {
    for {
      col <- 0 until horFieldCount
    } yield {
      val chip: ChipView = createChip(col, fieldWidth, fieldHeight, activeChip)
      chip.setOnMouseClicked(new EventHandler[MouseEvent] {
        override def handle(event: MouseEvent) {
          val newChip = createChip(col, fieldWidth, fieldHeight, activeChip)
          content.add(0, newChip)
          val dropHeight = rows - fourConnect.findFirstEmptySlot(col).get.row
          val transition = new TranslateTransition {
            duration = Duration(1000)
            node = newChip
            byY = dropHeight * fieldHeight
          }
          transition.play()
          fourConnect.dropChip(col, activeChip)
          if (!fourConnect.hasEmptySlot(col)) chip.setVisible(false)
          checkHasWinner
          switchPlayer
        }
      })
      chip
    }
  }
  val gameSpots = {
    for {
      col <- 0 until horFieldCount
      row <- 0 until verFieldCount
    } yield {
      val rect = new Rectangle {
        x = paneOffsetX + col * fieldWidth + slotMargin
        y = paneOffsetY + row * fieldHeight + slotMargin
        width = fieldWidth - 2 * slotMargin
        height = fieldHeight - 2 * slotMargin
        fill = Color.DeepSkyBlue
      }
      val hole = new Ellipse() {
        centerX = paneOffsetX + col * fieldWidth + fieldWidth / 2
        centerY = paneOffsetY + row * fieldHeight + fieldHeight / 2
        radiusX = fieldWidth / 2 - 4 * slotMargin
        radiusY = fieldHeight / 2 - 4 * slotMargin
      }
      val shape = (Shape.subtract(rect, hole)).asInstanceOf[javafx.scene.shape.Path]
      new SpotView(fourConnect.game.slots(col).spots(verFieldCount-1-row),shape)
    }
  }


  def createChip(col: Int, fieldWidth: Double, fieldHeight: Double, chip: Chip): ChipView = {
    val chipView: ChipView = new ChipView(chip) {
      centerX = paneOffsetX + col * fieldWidth + fieldWidth / 2
      centerY = paneOffsetY - fieldHeight / 2
      radiusX = fieldWidth / 2 - 4 * slotMargin
      radiusY = fieldHeight / 2 - 4 * slotMargin
      fill = colorMap(chip)
      effect = new InnerShadow {
        offsetX = -3
        offsetY = -3
        radius = 12
      }
    }
    chipView
  }

  def switchPlayer = {
    activeChip = if (activeChip == RedChip) YellowChip else RedChip
    for (chip <- chipsToPlay) chip.fill = colorMap(activeChip)
  }

  def checkHasWinner = {
    val winners = fourConnect.winningSpots(activeChip)

    for {
      spotView <- gameSpots
      winner <- winners
      spot <- winner
     if (spot.col == spotView.spot.col && spot.row == spotView.spot.row)

    } yield {spotView.blink}
  }

  content = chipsToPlay ++ gameSpots

}
