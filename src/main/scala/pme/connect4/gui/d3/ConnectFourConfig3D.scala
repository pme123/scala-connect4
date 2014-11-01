package pme.connect4.gui.d3

import pme.connect4.domain.GameConfig._
import scalafx.scene.paint.{Color, PhongMaterial}


object ConnectFourConfig3D {

  protected[d3] val panelSize = (1024, 768)
  protected[d3] val groundSize = 400


  protected[d3] val chipRadius = 12
  protected[d3] val chipThickness = 2

  protected[d3] val slotMargin = 2
  protected[d3] val fieldWidth = 2 * (chipRadius + slotMargin)
  protected[d3] val fieldHeight = 2 * (chipRadius + slotMargin)
  protected[d3] val boardWidth = fieldWidth * cols
  protected[d3] val boardHeight = fieldHeight * rows
  protected[d3] val gameOffsetX = -rows / 2 * fieldWidth
  protected[d3] val gameOffsetY = -150

  protected val groundMaterial = new PhongMaterial {
    diffuseColor = Color.DarkGreen
    specularColor = Color.Green
  }

  protected val yellowChipMaterial = new PhongMaterial {
    diffuseColor = Color.Yellow
    specularColor = Color.LightYellow
  }
  protected val redChipMaterial = new PhongMaterial {
    diffuseColor = Color.DarkRed
    specularColor = Color.Red
  }
  protected val gameMaterial = new PhongMaterial {
    diffuseColor = Color.DarkBlue
    specularColor = Color.Blue
  }


}
