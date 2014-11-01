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
    content = chipsToPlay ++ gameSpots.values
  }


  protected def createChip(col: Int): ChipView3D = {
    val chipView: ChipView3D = new ChipView3D(activeChip) {
      translateX = SpotView3D.calcOffsetX(col)
    }
    chipView.onMousePressed = (me: MouseEvent) => handleChipSelected(col, chipView)
    chipView
  }
  protected def createSpot(col: Int, row: Int): SpotView3D = {
    val spot = new SpotView3D(fourConnect.game.slots(col).spots(row), SpotView3D.createSpotView(col,row))

    spot
  }
   protected def changeMaterial(chip: ChipView3D): Unit = chip.material = materialMap(activeChip)

  protected def addNewChipView(newChip: ChipView3D): Unit = content.add(newChip)

  protected def dropHeight(spotView: SpotView3D): Double =  -(rows-spotView.getSpot.row)*fieldHeight

}
