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

`independent` effects like possibly Future and parallelize

### 4.2.3 Monadic effects - a variant on the applicative pattern (page 125)

monad errors fall out on left of tree, short circuits (rail-road track with transfers off for errors)

- function application
- function composition
- failure handling

Applicative or Monad?

use the `least powerful abstraction` that works for your `use case`

fall back to monad when you need flatMap

state monad

Listing 4.5 why are we using a `modify `to create a state monad?
Why not use `set` since we are creating a new monad?
not really updating anything

manage mutable state in referential transparent way

## 4.3 How patterns shape your domain model (page 134)

[Phil Wadler’s paper “Theorems for Free”](http://homepages.inf.ed.ac.uk/wadler/topics/parametricity.html)

latter of goodness (Figure 4.4 page 135)

[“Haskell in the Large” by Don Stewart](http://web.archive.org/web/20150129012424/http://code.haskell.org/~dons/talks/dons-google-2015-01-27.pdf)

Design patterns compose - `Validation` abstraction

`correct by construction`

### EXERCISE 4.2 ACCUMULATING VALIDATION ERRORS (APPLICATIVELY) (page 137)

For the following method:
```scala
 def savingsAccount(no: String, name: String, rate: BigDecimal,
           openDate: Option[Date], closeDate: Option[Date],
           balance: Balance): ValidationNel[String, Account] = ???
```

You need to implement the following validation rules: 
- (1) account numbers must have a minimum length of 10 characters, 
- (2) the rate of interest has to be positive, and 
- (3) the open date (default to today if not specified) must be before the close date.

### EXERCISE 4.3 FAIL-FAST VALIDATION (MONADIC) (page 138)
```scala
def savingsAccount(no: String, name: String, rate: BigDecimal,
  openDate: Option[Date], closeDate: Option[Date],
  balance: Balance): ??? = {...
```


## 4.4 Evolution of an API with algebra, types, and patterns (page 139)

http://www.investopedia.com/

### 4.4.1 The algebra — first draft (page 140)

### 4.4.2 Refining the algebra (page 141)

Kleisli arrow 28 that allows functions 
f: A => M[B] and 
g: B => M[C] to compose and 
yield A => M[C], where M is a Monad

```scala
 case class Kleisli[M[_], A, B](run: A => M[B]) {
     def andThen[C](k: Kleisli[M, B, C])(implicit b: Monad[M]) =
       Kleisli[M, A, C]((a: A) => b.bind(this(a))(k.run))

    def compose[C](k: Kleisli[M, C, A])(implicit b: Monad[M]) =
      k andThen this
 }
```
Why do we need this extra layer of composition?  List as a Monad already composes?

### 4.4.3 Final composition - follow the types

qualities of a good domain model
- Intention-revealing interfaces drawn from the ubiquitous language
- Making implicit concepts explicit
- Side-effect-free functions
- Declarative design

#### EXERCISE 4.4 DEMYSTIFYING KLEISLI

## 4.5 Tighten up domain invariants with patterns and types (page 144)

use type system and generic invariants for additional tests by the compiler

### 4.5.1 A model for loan processing (page 144)

### 4.5.2 Making illegal states unrepresentable (page 146)

I like the idea but not sure about the `Phantom Type Pattern` as presented

Traits are not even sealed or related.

Why use phantom types of Applied, Approved, and Enriched?

Why not use additional case classes based sealed trait because usually 
is extra data to track with each state transition

## Summary

- What are functional design patterns?
- The ubiquitous monoid
- Patterns for effectful programming
- Pattern goodness
- Implementation from the field #1 - Kleislis
- Implementation from the field #2 - phantom types

