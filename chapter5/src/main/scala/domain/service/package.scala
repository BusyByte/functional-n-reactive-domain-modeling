package net.nomadicalien.ch5.domain

import scalaz._
import Scalaz._

package object service {
  type Valid[A] = NonEmptyList[String] \/ A
}
