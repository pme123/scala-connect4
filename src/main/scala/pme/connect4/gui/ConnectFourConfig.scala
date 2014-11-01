package pme.connect4.gui
import pme.connect4.domain.GameConfig._
    object ConnectFourConfig {
      protected[gui] val paneOffsetX = 40
      protected[gui]  val paneOffsetY = 120
      protected[gui]  val slotMargin = 2

    }
abstract class ConnectFourConfig {
  import ConnectFourConfig._

  protected  def chipRadius: Int

  protected[gui]  val fieldWidth = 2 * (chipRadius + slotMargin)
  protected[gui]  val boardWidth = fieldWidth * cols
  protected[gui]  val boardHeight = fieldWidth * rows

}
