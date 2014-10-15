package pme.connect4.gui

import pme.connect4.domain.Spot

import scalafx.animation.{FadeTransition, Timeline}
import scalafx.scene.shape.Path
import scalafx.util.Duration

class SpotView2D(val spot: Spot, path: javafx.scene.shape.Path) extends Path(path) with SpotView {
  self =>

  def blink() {
    val  animation = new FadeTransition{
      duration = Duration(500)
      fromValue = 1.0
      toValue = 0.0
      cycleCount = Timeline.Indefinite
      autoReverse = true
      node = self
    }
    animation.play()
  }

}
