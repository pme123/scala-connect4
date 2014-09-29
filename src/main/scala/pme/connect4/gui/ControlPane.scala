package pme.connect4.gui

import scalafx.scene.control.Button
import scalafx.scene.layout.AnchorPane

/**
 * Created by pascal.mengelt on 29.09.2014.
 */
class ControlPane extends AnchorPane {

  import pme.connect4.gui.GuiGameConfig._

  val newGameButton = new Button {
    layoutX = paneOffsetX
    layoutY = gameSize._2 - paneOffsetY / 2
    minWidth = boardWidth
    prefWidth = boardWidth
    text = "Start new Game"
    defaultButton = true


  }
  minWidth = gameSize._1
  prefWidth = gameSize._1
  content = newGameButton

}
