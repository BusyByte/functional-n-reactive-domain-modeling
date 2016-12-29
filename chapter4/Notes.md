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

see how to express effects generically in your domain model and how to compose them to form larger effects

### 4.2.1 Functors - the pattern to build on (page 117)

map function on List, Option, Try all follow Functor pattern
 
can implement interpreter of Functor for List, Option, etc
 
fmap lets you map a function using an implicit Functor

### 4.2.2 The Applicative Functor pattern (page 118)

A => B is lifted into te context of F

transforms F[A] to F[B]

context is the `type constructor` F (aka the `effect` of the computation)

` Validation[String, A]`

#### EXERCISE 4.1 ALGEBRAIC THINKING (page 121)

lift should be preferred because you can define your program in abstract terms without evaluating it
- this way you can reuse the generic algebra

apply would be preferred if you were trying to run your program as you define it

`Applicative Functor` pattern - useful for `applicative effects`

effects sequenced through `all` arguments

all 3 validations will be performed regardless of failure or success of any of them

pitch for ScalaZ

Why a Generic Module for Applicative?

traverse list of accounts with Option Applicative will give list of balances only if no accounts closed (None)

What good is the applicative functor pattern for your domain model?

apply Applicative Functor pattern when you need to execute contexts that are independent of each other in sequence

### 4.2.3 Monadic effects - a variant on the applicative pattern (page 125)


