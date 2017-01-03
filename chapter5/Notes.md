## 5.1 Modularizing your domain model (page 150)

simpler parts - modules

- Specific and well defined
- Cohesive
- Published contract
- First class
- Vocabulary
    
## 5.2 Modular domain models — a case study (page 152)
   
### 5.2.1 Anatomy of a module (page 152)

The published algebra

- name
- operation names
- trait as container
- type aliases
- parameterized
- compositionality
- implementation independence
- making collaborations explicit

the module implementation

commit to specific implementations as late as possible

### 5.2.2 Composition of modules (page 159)

mix multiple traits into one service trait

### 5.2.3 Physical organization of modules (page 160)

domain package
- model
- repositories
- service
  - interpreter
- application

control the visibility and keep a strong check 
so that the implementation isn’t inadvertently leaked out

### 5.2.4 Modularity encourages compositionality (page 162)

import concrete modules into app

create methods to compose cross module methods together

- Committing to module implementations
- Composing methods from a module
- Composing across modules

### 5.2.5 Modularity in domain models—the major takeaways (page 163)

- Published contracts
- Private implementation
- Module organization
- Compositionality

## 5.3 Type class pattern—modularizing polymorphic behaviors (page 163)

- Modularity
- Ad hoc polymorphism
- Protocol selection

## 5.4 Aggregate modules at bounded context (page 166)

### 5.4.1 Modules and bounded context (page 167)

### 5.4.2 Communication between bounded contexts (page 168)

`anticorruption layer` as communication between two bounded contexts

messaging 

publisher-subscriber model

## 5.5 Another pattern for modularization—free monads (page 169)

proper abstractions in order to be able to change one part of your model 
without any impact (or minimal impact) on other unrelated 

advanced topic

### 5.5.1 The account repository (page 169)

### 5.5.2 Making it free (page 170)

model domain behaviors as `pure data` and then supply `interpreters` that work on that data in specific contexts

behaviors modeled as pure data without any concern for how it will be executed operationally

`Free Monad` pattern

- Building blocks - pure data using ADTs
- The magic band - have only ADTs as pure data and no monad now, need to sequence as monad though 
  - do something magical and get a `monad for free`
  - get a monad which can lift your building blocks
- The module - lift each building blocks into context of the monad, public API of your module
- What does the composed DSL return?
  - Free monad just accumulates building blocks and hands over the resulting aggregate
  - free monad has no `semantics` or `denotation`
- `Denotation` - interpreter of the resulting aggregate

layer of indirection between definition of the DSL and interpretation
free monad leaves execution sequence undefined and up to interpreter to define it as it traverses the aggregate

### 5.5.3 Account repository—monads for free (page 172)

defining the building blocks

[“Free Monoids and Free Monads” by Rúnar Bjarnason](http://blog.higher- order.com/blog/2013/08/20/free-monads-and-free-monoids/)

composing the DSL

does this actually do any `work`?, might be misleading to people unfamiliar with free monads?

### 5.5.4 Interpreters for free monads (page 175)

`execute` your free-monad-based implementation

- not for production (uses mutable map)
- single step - step gets evaluated for each node as you interpret the aggregate
- running the whold - apply takes entire aggregate and traverses it and invokes step on every node.

#### EXERCISE 5.1 EFFECTS IN INTERPRETERS (page 177)
- use State monad instead of mutable map and can be an immutable map
```scala
object AccountRepoState {
  type AccountMap = Map[String, Account]
  type Err[A] = Error \/ A
  type AccountState[A] = StateT[Err, AccountMap, A]
}
```
Implement an AccountRepoInterpreter using AccountState instead of Task. 
When you pass the free monad and execute the interpreter, it should give you back an AccountState.

### 5.5.5 Free monads—the takeaways (page 178)

- modularity and testability
- purity
- scalability

# 5.6 Summary (page 179)

- `What is modularization?`
- `Anatomy of a module`
- `Compositionality`
- `Physical organization of modules`
- `Type class pattern`
- `bounded context`
- `Free monads`