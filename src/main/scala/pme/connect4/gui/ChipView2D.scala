package pme.connect4.gui

import pme.connect4.domain.{YellowChip, RedChip, Chip}

import scalafx.animation.{Timeline, FadeTransition}
import scalafx.scene.effect.InnerShadow
import scalafx.scene.paint.Color
import scalafx.scene.shape.Ellipse
import scalafx.util.Duration

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
