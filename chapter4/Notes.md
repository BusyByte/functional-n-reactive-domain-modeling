# Functional patterns for domain models

design patterns

applicatives and monads

monoids

## 4.1 Patterns—the confluence of algebra, functions, and types (page 109)

- Completely generic and reusable code—we call it the algebra
- Context-specific implementation called an interpreter

Monoid laws

parametricity - T

Monoid is parparametric on type T

### 4.1.1 Mining patterns in a domain model (page 110)

find commonalities in code which can be made generic (parametric) on type T


functional pattern helps unify commonalities through the algebra 
while allowing to abstract over the differences with context-specific implementations


### 4.1.2 Using functional patterns to make domain models parametric (page 111)

Identifying the commonality
  
Abstracting over the operations

defining a money addition monoid

listing 4.3 seems too generic, how do you know you're getting the sum monoid and not the max monoid?

Abstracting over the context

Foldable

mapReduce
- maps with a function of underlying types
- then does a fold left on the underlying monoid (Money)

Summary
- More generic
- More abstract
- Parallelizable

## 4.2 Basic patterns of computation in typed functional programming (page 116)