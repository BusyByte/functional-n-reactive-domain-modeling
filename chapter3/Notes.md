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

