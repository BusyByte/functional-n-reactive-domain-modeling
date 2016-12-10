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