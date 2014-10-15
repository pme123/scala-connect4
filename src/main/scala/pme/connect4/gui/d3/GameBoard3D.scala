package pme.connect4.gui.d3

import pme.connect4.domain.GameConfig._
import pme.connect4.gui.GameBoard
import pme.connect4.gui.d3.ChipView3D._
import pme.connect4.gui.d3.ConnectFourConfig3D._

import scalafx.Includes._
import scalafx.scene.Group
import scalafx.scene.input.MouseEvent

class GameBoard3D extends Group with GameBoard[ChipView3D, SpotView3D] {

  override def startNewGame() = {
    super.startNewGame()
    content = chipsToPlay ++ gameSpots
  }

  override protected def initGameSpots: Seq[SpotView3D] = Nil

  override protected def createChip(col: Int): ChipView3D = {
    val chipView: ChipView3D = new ChipView3D(activeChip) {
      translateX = -gameWidth / 2 + col * gameWidth / cols + chipRadius
      translateY = groundSize / 4
    }
    chipView.onMousePressed = (me: MouseEvent) => content.add(handleChipSelected(col, chipView))
    chipView
  }

  override protected def changeMaterial(chip: ChipView3D): Unit = chip.material = materialMap(activeChip)


  protected def dropHeight(dropHeight: Int): Double = -dropHeight * groundSize / 2 / (rows + 1)

}
