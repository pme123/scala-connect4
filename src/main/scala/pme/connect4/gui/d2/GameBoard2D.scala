package pme.connect4.gui.d2

import javafx.scene.shape.Path

import pme.connect4.domain.GameConfig._
import pme.connect4.domain._
import pme.connect4.gui.ConnectFourConfig._
import pme.connect4.gui.GameBoard
import pme.connect4.gui.d2.ChipView2D._
import pme.connect4.gui.d2.ConnectFourConfig2D._

import scalafx.scene.layout.Pane
import scalafx.scene.paint.Color
import scalafx.scene.shape._

class GameBoard2D extends Pane with GameBoard[ChipView2D, SpotView2D] {

  override def startNewGame() = {
    super.startNewGame()
    content = chipsToPlay ++ gameSpots.values
  }

  protected def createChip(col: Int, chip: Chip): ChipView2D = {
    val chipView: ChipView2D = new ChipView2D(chip) {
      centerX = paneOffsetX + col * fieldWidth + fieldWidth / 2
      centerY = paneOffsetY - fieldWidth / 2
      radiusX = fieldWidth / 2 - 4 * slotMargin
      radiusY = fieldWidth / 2 - 4 * slotMargin
      fill = colorMap(chip)
    }
    chipView
  }
  protected def createSpot(col: Int, row: Int): SpotView2D = {

    val rect = new Rectangle {
      x = paneOffsetX + col * fieldWidth + slotMargin
      y = paneOffsetY + row * fieldWidth + slotMargin
      width = fieldWidth - 2 * slotMargin
      height = fieldWidth - 2 * slotMargin
      fill = Color.DeepSkyBlue
    }
    val hole = new Ellipse() {
      centerX = paneOffsetX + col * fieldWidth + fieldWidth / 2
      centerY = paneOffsetY + row * fieldWidth + fieldWidth / 2
      radiusX = fieldWidth / 2 - 4 * slotMargin
      radiusY = fieldWidth / 2 - 4 * slotMargin
    }
    val shape = Shape.subtract(rect, hole).asInstanceOf[Path]
    new SpotView2D(fourConnect.game.slots(col).spots(rows - 1 - row), shape)
  }


  protected def changeMaterial(chip: ChipView2D): Unit = chip.fill = colorMap(activeChip)

  protected def dropHeight(spotView: SpotView2D): Double = (rows-spotView.getSpot.row) * fieldWidth

  protected def addNewChipView(newChip: ChipView2D): Unit = content.add(newChip)
}

