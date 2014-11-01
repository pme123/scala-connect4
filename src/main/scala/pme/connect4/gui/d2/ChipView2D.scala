package pme.connect4.gui.d2

import pme.connect4.domain.{Chip, RedChip, YellowChip}
import pme.connect4.gui.ChipView

import scalafx.scene.effect.InnerShadow
import scalafx.scene.paint.Color
import scalafx.scene.shape.Ellipse

/**
 * Created by mengelpa on 24.09.14.
 */

object ChipView2D {
  val colorMap: Map[Chip, Color] = Map(RedChip -> Color.Red, YellowChip -> Color.Yellow)

}
class ChipView2D(chip: Chip) extends Ellipse with ChipView {
  visible = true
  effect = new InnerShadow {
    offsetX = -3
    offsetY = -3
    radius = 12
  }
}
