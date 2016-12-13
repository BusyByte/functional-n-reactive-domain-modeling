## Why Scala

birds-eye

scala feature | model concepts
--- | ---
ADT (case classes) with immutability | help modeling domain objects
Pure functions | help modelling behavior
Function compositions and higher order functions | compose functions into larger (eg debit and credit)
Advanced type system | encapsulate business rules in types themselves
Traits and Object which compose | modularization, compose an object of multiple traits; traits plugin specific business rules
Generics | PortfolioService[C] where C is specialize for ever type of Customer

## Static types and rich domain models

Make function polymorphic - pass in sealed trait rather than concrete case class

## Pure functions for domain behavior

composition using combinators

statements vs expressions

for comprehensions and illusion of sequencing

pure functions for modelling behavior

pure code = referentially transparent - same output for the same input every time you invoke it

fusion 
- andThen style of composition, can also do compose
- efficient because doesn't create intermediate list

other benefits
- testability
- parallel execution

## Algebraic Data Types and Immutability

inhabitants of data type - number of distinct values
sum type = must be one type OR another type (eg Currency, Either)

QUIZ TIME 2.1 For the Either[A, B] data type in listing 2.7, 
how many inhabitants can there be? Hint: It’s not two.

Answer: Number of type which can extend A plus number of types which extend B
    
product type 
- parameters of the class make it a product type 
    eg type CheckingAccount = String x String x Date
- Can be represented by tuple syntax eg (String, String, Date)

an ADT forces you to build abstractions strictly according to the rules that you’ve defined for it

pattern matching
- compiler checks for the exhaustiveness of the pattern match

ADTs encourage immutability

case classes and immutability

QUIZ TIME 2.2 Suppose you have the following ADT definition:
case class Address(no: Int, street: String, city: String, zip: String)
case class Identity(name: String, address: Address)
And you have an Identity created as follows:
val i = Identity("john", Address(12, "Monroe street", "Denver", "80231"))
How will you change the ZIP code to 80234 without in-place mutation? Hint: Use a nested copy.

val i2 = i.copy(address = i.address.copy(zip = "80234"))

## Functional in Small, OO in the large

modules
- traits or objects
- compose smaller traits into larger one
- add larger one to object