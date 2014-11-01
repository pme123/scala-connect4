package pme.connect4.gui

import pme.connect4.util.Observer

import scalafx.geometry.Insets
import scalafx.scene.control.Label
import scalafx.scene.layout.AnchorPane

class InfoPane(gameBoard: GameBoard[_ <: ChipView,_ <: SpotView]) extends AnchorPane {

  import pme.connect4.gui.ConnectFourConfig._

  val info = new Label() {
    layoutX = paneOffsetX
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
