# Designing functional domain models

## 3.1 The algebra of API design (page 74)

algebra of module
- one ore more sets - data types 
- functions that operate on objects of those sets
- axioms and laws assumed to be true; used to derive other theorems

implementation is not part of the algebra
contract of behaviors published forms the algebra of the API

### 3.1.1 Why an algebraic approach? (page 75)

set of laws which are verifiable

advantages
- loud and clear - focuses on behaviors and not classes or objects
- compositionality - when function types align. OO composability at class level is not well defined
- verifiability - set of properties include in core and assure you don't violate those

## 3.2 Designing functional domain models Defining an algebra for a domain service (page 76)

need to represent validation errors or an account

Either, Option, Try

### 3.2.1 Abstracting over evaluation (page 76)

Try - effect of failures
Option - effect of optionality

### 3.2.2 Composing abstractions (page 77)

for comprehension

monadic model of computation

### 3.2.3 The final algebra of types (page 79)

not talk about the implementation

type constructor - M[A]

monad
- identity
- unit
- associativity

### 3.2.4 Laws of the algebra (page 81)

example equal debit and credit retain the same balance

Scala Check (QuickCheck for Java)

verifiable properties

Exercises:
- Rewrite the definition of AccountService by using Option as the return type of the methods. Hint: Option also implements flatMap and can be used for chaining computations. 
  - Q: Do you think Option is a better way to model your use case than Try?
  - A: No, you loose the information the the exception about what failed

- Use Either to model the failure of the operations. Like Try, Either is a sum type and can be used to separate the success and failure branches. 
  - Q: Is this a better option than Try when it comes to chaining the operations in a sequence?
  - A: Either lets you have the flexibility to define what the error side should look like
  
### 3.2.5 The interpreter for the algebra (page 82)

each implementation is known as an interpreter of the algebra
can have multiple interpreters of a single algebra

## 3.3 Patterns in the lifecycle of a domain model (page 83)

lifecycle of domain object from ch1
- creation
- participation
- persistence

minimally valid on creation

### 3.3.1 Factories—where objects come from (page 85)

case class has default companion object

### 3.3.2 The smart constructor idiom (page 86)

exceptions break referential transparency

sealed traits

### 3.3.3 Get smarter with more expressive types (page 88)

- smart constructor - honor a set of constraints
- constructor of the class needs to be protected from users - private specifier in the class declaration
- published API needs to be explicit about failure

### 3.3.4 Aggregates with algebraic data types (page 89)

can move accountNumber into `Position` so don't need to depend whole account entity

- algebraic data types that form the structure of the entity
- modules that offer the algebra of manipulating the aggregates in a compositional manner

In many situations, you may want to consider using Scala’s Extractor pattern. 
For more information on why extractors are an improvement over pattern matching, 
take a look at the paper by Burak Emir, Martin Odersky, and John Williams.
[Matching Objects with Patterns, by Burak Emir et al.](http://lampwww.epfl.ch/~emir/written/Matching- ObjectsWithPatterns-TR.pdf)

### 3.3.5 Updating aggregates functionally with lenses (page 92)

avoid `var`'s and in-place mutability in general

lens 
- abstraction for nested copy statements 
- decent API
- maintain `goodness` of immutability

- Parametricity
  - `case class Lens[O, V]` 
  - `O` is the object
  - `V` is the field being updated
- One lens per field
- Getter - `O => V`
- Setter - `(O, V) => O`

```scala
case class Lens[O, V](
  get: O => V,
  set: (O, V) => O
)
```

Scalaz, Shapeless, Zipper

compose function - compose inner (eg Address) and outer (Customer) lenses 

Expose top-level lenses that allow transformation of lower-level objects only through the root element

when to use:
- need to perform updates within deeply nested ADTs
- need to compose updates of ADTs with other abstraction

libraries that offer boilerplate-free implementation of lenses using Scala

[Monocle](https://github.com/julien-truffaut/Monocle)

Scalaz and Shapeless offer lenses

LENS LAWS
- Identity
- Retention
- Double set

EXERCISE 3.2 VERIFYING LENS LAWS

### 3.3.6 Repositories and the timeless art of decoupling (page 97)

passing repo on method call in service is:
- verbose
- coupling API of service with the context
- lack of compositionality

curry it


building abstractions through incremental composition - compose it with any other computation that returns 
a `Function1[AccountRepository, _]` and defer the evaluation until you’ve built the whole computation pipeline

reader monad - `case class Reader[R, A](run: R => A)`

EXERCISE 3.3 INJECTING MULTIPLE DEPENDENCIES
A: Since this is just nested curring you can turn repository inputs denoted R as `R => A => R2 => B => R3 => C` into
`(R1, R2, R3) => C` which is a reversal of the curry effect

[MacWire] (https://github.com/adamw/macwire)

### 3.3.7 Using lifecycle patterns effectively—the major takeaways (page 104)

- ADT and pattern matching
- lenses
- reader monad
- smart constructor

## 3.4 Summary (page 105)

- Thinking in algebra
- Type-driven composition
- Separation of concerns
- Aggregate as the unit of consistency
- Functional implementation of domain object patterns