package net.nomadicalien.ch5.free

import net.nomadicalien.ch5.free.repository.AccountRepoF

import scalaz.Free


package object free {
  type AccountRepo[A] = Free[AccountRepoF, A]  //This is the magic band - makes AccountRepo a free monad
}
