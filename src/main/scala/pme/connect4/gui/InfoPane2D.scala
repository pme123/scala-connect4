package pme.connect4.gui

import pme.connect4.domain.SpaceChip
import pme.connect4.util.Observer

import scalafx.geometry.Insets
import scalafx.scene.control.Label
import scalafx.scene.layout.AnchorPane

/**
 * Created by pascal.mengelt on 29.09.2014.
 */
class InfoPane2D(gameBoard: GameBoard2D) extends AnchorPane {

  import pme.connect4.gui.GuiGameConfig2D._

  val info = new Label() {
    layoutX = paneOffsetX
    minWidth = boardWidth / 2
    prefWidth = boardWidth / 2
    text = "Welcome  to  4-Connect!"
 
  }




  content = List(info)
  margin = Insets(10, 0, 20, 0)
  gameBoard.addGameStartedObserver(new GameStartedObserver)
  gameBoard.addGameWinnerObserver(new GameWinnerObserver)

  private class GameStartedObserver extends Observer[GameStartedSubject]{
    def receiveUpdate(subject: GameStartedSubject) = {
      if (subject.gameStarted) info.text = "Game is running!"
      else info.text = "Game is finished!"
    }
  }

  private class GameWinnerObserver extends Observer[GameWinnerSubject]{
    def receiveUpdate(subject: GameWinnerSubject) = {
      info.text = "%s\nThe winner is %s.".format(info.text.value, subject.gameWinner.name)
    }
  }
}