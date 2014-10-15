package pme.connect4.gui.d3

import scalafx.scene.paint.{Color, PhongMaterial}


/**
 * Created by pascal.mengelt on 15.10.2014.
 */
object ConnectFourConfig3D {
  val groundSize = 400
  val groundOffset = 100
  val gameWidth = groundSize-2*groundOffset
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

  val chipRadius = 13
  val chipThickness = 2
}
