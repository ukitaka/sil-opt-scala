package me.waft

import fastparse.all._

package object parser {
  def upper = CharIn('A' to 'Z')
  def lower = CharIn('a' to 'z')
  def digit = CharIn('0' to '9')
}
