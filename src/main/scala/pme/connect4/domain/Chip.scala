package pme.connect4.domain

 abstract class Chip {
  def name: String

  def other: Chip
}

case object SpaceChip extends Chip {
  override def toString = "[ ]"

  def name = "Space"

  def other = SpaceChip
}

case object RedChip extends Chip {
  override def toString = "[r]"

  def name = "Red"

  def other = YellowChip
}

case object YellowChip extends Chip {
  override def toString = "[y]"

  def name = "Yellow"

  def other = RedChip
}
