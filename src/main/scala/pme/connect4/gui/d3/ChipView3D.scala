package pme.connect4.gui.d3

import pme.connect4.domain.GameConfig._
import pme.connect4.domain.{Chip, RedChip, YellowChip}
import pme.connect4.gui.ChipView
import pme.connect4.gui.d3.ChipView3D._
import pme.connect4.gui.d3.ConnectFourConfig3D._

import scalafx.scene.effect.InnerShadow
import scalafx.scene.paint.Material
import scalafx.scene.shape.Cylinder
import scalafx.scene.transform.Rotate


object ChipView3D {
  val materialMap: Map[Chip, Material] = Map(RedChip -> redChipMaterial, YellowChip -> yellowChipMaterial)

}

class ChipView3D(chip: Chip) extends Cylinder(chipRadius, chipThickness) with ChipView {
  visible = true
  effect = new InnerShadow {
    offsetX = -3
    offsetY = -3
    radius = 12
  }
 translateY = gameOffsetY + (3+rows)*fieldHeight
  material = materialMap(chip)
  rotationAxis = Rotate.XAxis
  rotate = 90.0
}
