package pme.connect4.gui

import java.util.Date
import javafx.event.{ActionEvent, EventHandler}

import scalafx.geometry.{Pos, Insets}
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.layout.{VBox, AnchorPane}
import scalafx.scene.text.Text
import scalafx.stage.{Modality, Stage}

/**
 * Created by pascal.mengelt on 29.09.2014.
 */
class ControlPane extends AnchorPane {

  import pme.connect4.gui.GuiGameConfig._

  val newGameButton = new Button {
    layoutX = paneOffsetX
    layoutY = gameSize._2 - paneOffsetY / 2
    minWidth = boardWidth/2
    prefWidth = boardWidth/2
    text = "Start new Game"
    defaultButton = true
  }
  val changeColorButton = new Button {
    layoutX = paneOffsetX+boardWidth/2
    layoutY = gameSize._2 - paneOffsetY / 2
    minWidth = boardWidth/2
    prefWidth = boardWidth/2
    text = "Change Color"
  }


  def popupErrorMsg {
  val myDialog = new Stage();
    myDialog.initModality(Modality.APPLICATION_MODAL);
    val okButton = new Button("Ok");
    okButton.setOnAction(new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent) {
        myDialog.close
      }
    })
    val myDialogScene = new Scene(){new VBox {
      content = Seq(new Text{      text="Game has already started!"    }, okButton)
      spacing=30
      alignment =Pos.Center
      padding= Insets(10)
    }}

    myDialog.setScene(myDialogScene);
    myDialog.show();
  }

  minWidth = gameSize._1
  prefWidth = gameSize._1
  content = List(newGameButton, changeColorButton)
margin = Insets(0,0,10,0)


}
