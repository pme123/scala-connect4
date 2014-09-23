package pme.connect4.gui

import scalafx.scene.layout.Pane
import scalafx.scene.shape._
import scalafx.scene.paint.Color
import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import scalafx.scene.effect.InnerShadow
import scalafx.animation.TranslateTransition
import scalafx.util.Duration


/**
 * Created by pascal.mengelt on 18.09.2014.
 */
class GameBoard(val gameSize: (Double, Double)) extends Pane {
  val paneOffsetX = 40
  val paneOffsetY = 80
  val horFieldCount: Int = 7
  val verFieldCount: Int = 6
  val slotMargin = 2

  content = showBoard(gameSize._1, gameSize._2)


  def changeSize: Unit = {
    content = showBoard(width.get, height.get)
  }

  def showBoard(gameWidth: Double, gameHeight: Double) = {
    val boardWidth = gameWidth - paneOffsetX * 2
    val boardHeight = gameHeight - paneOffsetY * 2

    val fieldWidth = boardWidth / horFieldCount
    val fieldHeight = boardHeight / verFieldCount
    def createInputRow = {
      for {
        col <- 0 until horFieldCount
      } yield {
        val chip: Ellipse = createChip(col, fieldWidth, fieldHeight, Color.Red)
        chip.setOnMouseClicked(new EventHandler[MouseEvent] {
          override def handle( event:MouseEvent  ) {
            val newChip = createChip(col, fieldWidth, fieldHeight, Color.Red)
            
            val transition = new TranslateTransition {
              duration = Duration(1000)
              node = newChip
              byY= 6 * fieldHeight
            }
            transition.play()
            switchPlayer
          }
        })

        chip
      }
    }
    def createSpots = {
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
        new Path(shape)
      }
    }
    val fields = createInputRow ++ createSpots



    fields
  }

  def createChip(col: Int, fieldWidth: Double, fieldHeight: Double, color: Color): Ellipse = {
    val chip: Ellipse = new Ellipse {
      centerX = paneOffsetX + col * fieldWidth + fieldWidth / 2
      centerY = paneOffsetY - fieldHeight / 2
      radiusX = fieldWidth / 2 - 4 * slotMargin
      radiusY = fieldHeight / 2 - 4 * slotMargin
      fill = color
      effect = new InnerShadow {
        offsetX = -3
        offsetY = -3
        radius = 12
      }
    }
    chip
  }

  var myTurn=false
  def switchPlayer = {
    myTurn= !myTurn
  }
}
