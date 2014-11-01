package pme.connect4.gui.d3

import pme.connect4.domain.GameConfig._
import pme.connect4.gui.ConnectFourConfig
import scalafx.scene.paint.{Color, PhongMaterial}

object ConnectFourConfig3D {
  protected[d3] val panelSize = (1024, 768)
  protected[d3] val groundSize = 400

  protected[d3] val chipRadius = 12
  protected[d3] val chipThickness = 2
  protected[d3] val fieldWidth = ConnectFourConfig3D().fieldWidth


  protected[d3] val gameOffsetX = -rows / 2 * fieldWidth
  protected[d3] val gameOffsetY = -150

  protected[d3] val groundMaterial = new PhongMaterial {
    diffuseColor = Color.DarkGreen
    specularColor = Color.Green
  }

  protected[d3] val yellowChipMaterial = new PhongMaterial {
    diffuseColor = Color.Yellow
    specularColor = Color.LightYellow
  }
  protected[d3] val redChipMaterial = new PhongMaterial {
    diffuseColor = Color.DarkRed
    specularColor = Color.Red
  }
  protected[d3] val gameMaterial = new PhongMaterial {
    diffuseColor = Color.DarkBlue
    specularColor = Color.Blue
  }
  def apply() = new ConnectFourConfig3D
}

class ConnectFourConfig3D extends ConnectFourConfig{




  protected[d3] def chipRadius = ConnectFourConfig3D.chipRadius



}
