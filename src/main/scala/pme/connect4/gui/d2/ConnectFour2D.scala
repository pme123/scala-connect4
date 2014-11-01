package pme.connect4.gui.d2

import pme.connect4.gui._

import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color

object ConnectFour2D extends JFXApp {

  import pme.connect4.gui.d2.ConnectFourConfig2D._



  stage = new JFXApp.PrimaryStage {
    title = "Akka Connect Four"
    width = paneSize._1
    height = paneSize._2
    resizable = false
    scene = new Scene(rootPane) {
      fill = Color.LightGreen
    }
  }

  lazy val rootPane = new VBox() {
    content = Seq(controlPane, gameBoard, infoPanel)
  }

  lazy val gameBoard:GameBoard[_ <: ChipView, _ <: SpotView] = new GameBoard2D

  lazy val controlPane = new ControlPane(gameBoard)

  lazy val infoPanel = new InfoPane(gameBoard)


}
