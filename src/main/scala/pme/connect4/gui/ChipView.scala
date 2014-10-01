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

object ChipView {
  val colorMap: Map[Chip, Color] = Map(RedChip -> Color.Red, YellowChip -> Color.Yellow)

}
class ChipView(chip: Chip) extends Ellipse{
  visible = true
  effect = new InnerShadow {
    offsetX = -3
    offsetY = -3
    radius = 12
  }
}
