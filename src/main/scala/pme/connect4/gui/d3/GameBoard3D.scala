package pme.connect4.gui.d3

import pme.connect4.domain.Chip
import pme.connect4.domain.GameConfig._
import pme.connect4.gui.{ChipView, GeneralGameBoard}
import pme.connect4.gui.d3.ConnectFourConfig3D._
import scalafx.Includes._
import scalafx.animation.TranslateTransition
import scalafx.scene.Group
import scalafx.scene.input.MouseEvent
import scalafx.util.Duration

/**
 * Created by pascal.mengelt on 15.10.2014.
 */
class GameBoard3D extends Group with GeneralGameBoard[ChipView3D, SpotView3D] {


  override protected def initGameSpots: Seq[SpotView3D] = Nil
  override protected def createChip(col: Int): ChipView3D = {
    val chipView: ChipView3D = new ChipView3D(activeChip) {
      translateX = -gameWidth/2 + col * gameWidth / cols + chipRadius
      translateY = groundSize/4
    }
    chipView.onMousePressed = (me: MouseEvent) => {
      fourConnect.dropChip(col, activeChip)
      dropChipView(col)
      if (!fourConnect.hasEmptySlot(col)) chipView.setVisible(false)
    }
    chipView
  }
  def dropChipView(col: Int): Unit = {
    val newChip = createChip(col)
    children += newChip
    val dropHeight = rows - fourConnect.findFirstTakenSpot(col).get.row
    val transition = new TranslateTransition {
      duration = Duration(1000)
      node = newChip
      byY = -dropHeight * groundSize/2/(rows+1)
    }
    transition.play()
    //    verifyTurn()
    //   switchPlayer()
    //    runNextTurn()
  }


}
