package pme.connect4.gui

import pme.connect4.domain.Spot

import scalafx.animation.{Timeline, FadeTransition}
import scalafx.scene.shape.{Path, Ellipse, Shape}
import scalafx.util.Duration

/**
 * Created by pascal.mengelt on 23.09.2014.
 */
class SpotView(val spot: Spot, path: javafx.scene.shape.Path) extends Path(path) {
  self =>

  def blink {
    val  animation = new FadeTransition{
      duration = Duration(500)
      fromValue = 1.0
      toValue = 0.0
      cycleCount = Timeline.Indefinite
      autoReverse = true
      node = self
    }
    animation.play();
  }

}
