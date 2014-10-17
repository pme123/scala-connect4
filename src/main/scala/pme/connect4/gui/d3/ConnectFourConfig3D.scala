package pme.connect4.gui.d3

import pme.connect4.domain.GameConfig._
import scalafx.scene.paint.{Color, PhongMaterial}


/**
 * Created by pascal.mengelt on 15.10.2014.
 */
object ConnectFourConfig3D {
  val groundSize = 400


  val chipRadius = 12
  val chipThickness = 2

  val slotMargin = 2
  val fieldWidth = 2*(chipRadius + slotMargin)
  val fieldHeight = 2*(chipRadius + slotMargin)
  val boardWidth = fieldWidth * cols
  val boardHeight = fieldHeight * rows
  val gameOffsetX = -rows/2*fieldWidth
  val gameOffsetY = -150

  val groundMaterial = new PhongMaterial {
    diffuseColor = Color.DarkGreen
    specularColor = Color.Green
  }

  val yellowChipMaterial = new PhongMaterial {
    diffuseColor = Color.Yellow
    specularColor = Color.LightYellow
  }
  val redChipMaterial = new PhongMaterial {
    diffuseColor = Color.DarkRed
    specularColor = Color.Red
  }
  val gameMaterial = new PhongMaterial {
    diffuseColor = Color.DarkBlue
    specularColor = Color.Blue
  }



}
