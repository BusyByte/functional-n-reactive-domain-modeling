
/**
  * Power of purity
  */
object Listing2_5 {
  def calculateInterest: SavingsAccount => BigDecimal = { a =>
    a.balance.amount * a.rate
  }
  def deductTax: BigDecimal => BigDecimal = { interest =>
    if (interest < 1000) interest else (interest - 0.1 * interest)
  }
  trait Account {
    def no: String
    def name: String
    def balance: Balance
  }

  case class SavingsAccount(no: String, name: String,
    balance: Balance, rate: BigDecimal) extends Account
  case class Balance(amount: BigDecimal)
  val a1 = SavingsAccount("a-0001", "ibm", Balance(100000), 0.12)
  val a2 = SavingsAccount("a-0002", "google", Balance(2000000), 0.13)
  val a3 = SavingsAccount("a-0003", "chase", Balance(125000), 0.15)
  val accounts = List(a1, a2, a3)

  // Maps twice over the collections
  accounts.map(calculateInterest).map(deductTax)

 /*
  Fuses the maps by composing the functions.
  Note we use “andThen” here; f andThen g means g(f(x)).
  There’s another combinator,“compose” — f compose g means f(g(x)).
  */
  accounts.map(calculateInterest andThen deductTax)
}
