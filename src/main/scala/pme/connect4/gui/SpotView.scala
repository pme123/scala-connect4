package pme.connect4.gui

import pme.connect4.domain.Spot

import scalafx.animation.{FadeTransition, Timeline}
import scalafx.scene.Node
import scalafx.util.Duration

trait SpotView extends Node {
  self =>

  def blink() {
    val animation = new FadeTransition {
      duration = Duration(500)
      fromValue = 1.0
      toValue = 0.0
      cycleCount = Timeline.Indefinite
      autoReverse = true
      node = self
    }
    animation.play()
  }

  def getSpot: Spot
}
