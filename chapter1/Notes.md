Bounded Context - smaller model within the whole

Examples for banking system
- portfolio management system
- tax and regulatory reports
- term deposit management

Entities - have a business unique key
- account number is an example for a bank account
- Passes through multiple states in the lifecycle
- Usually has a definite lifecycle in the business

Value Object - the values in an object determine if it is a separate value
- All fields in address determine if it is a different address
- Change a field in address and it becomes a different address
- Does not have a business unique key
- Immutable
- Can be freely shared across entities

Business Domain Service - In a business domain service, multiple domain entities interact according to spe-cific business rules and deliver a specific functionality in the system
- It encapsulates a complete business operation
- More macro-level abstraction than entity or value object
- Involves multiple entities and value objects
- Usually models a use case of the business

Banking service
- Debit
- Credit
- Transfer
- Balance inquiry

Entities
- Account (Account number and Account holder business unique keys)
- Bank

Lifecycle of a domain object
- Creation
- Participation in behavior
- Persistence

Factories
- It keeps all creational code in one place.
- It abstracts the process of creation of an entity from the caller.

Aggregate
- Boundary in graph (sub-graph) of object which are encompassed in a consistent boundary
- Consistent meaning all objects within boundary obey rules and attributes
- Consist of one or more entities and value objects (and other primitive attributes)

Aggregate root
- Guardian or entry entity of graph within an aggregate
- Ensure the consistency boundary of business rules and transactions within the aggregate.
- Prevent the implementation of the aggregate from leaking out to its clients, acting as a facade for all the operations that the aggregate supports.
- Previous example Account as an Aggregate root

Algebraic data types (ADTs)
- Typically modelled with sealed traits and final case classes which can be type checked, complete, and can provide compile errors on incomplete matches
- In Scala we'll also model Entities as immutable case classes 

Repositories
- abstracts the underlying details of the persistent representation of the object
- interface doesn’t have any knowledge of the nature of the underlying persistent store

The ubiquitous language
- business domain vocabulary used in the objects, services, and behavior
- minimal and doesn’t contain any irrelevant details
- uses terms from the domain
- person familiar with the business domain who doesn’t know anything about the underlying implementation platform should also be able to understand what’s going on

Prefer Immutability
- easier to reason about code
- free to share across threads

Functional Purity

General principles you need to follow when designing functional domain models:
- Model the immutable state in an algebraic data type (ADT)
- Model behaviors as functions in modules
- behaviors in modules operate on types that the ADTs represent

Decouple state from behavior, behavior is easy to compose

Try, Success, and Failure instead of throwing exceptions

Pure functions compose

Higher-order functions such as map are also known as combinators.

Managing side effects

Why mixing domain logic and side effects is bad:
- Entanglement of domain logic and side effects. 
  - Violates separation of concerns. Domain logic and side effects are orthogonal to each other—entanglement violates a basic software engineering principle.
- Difficult to unit test.
  - If domain logic is entangled with side effects, unit testing becomes difficult. You need to resort to mocking that leads to other complications in your source code.
- Difficult to reason about the domain logic.
  - You can’t reason about the domain logic that’s entangled with the side effect.
- Side effects don’t compose and hinder modularity of your code.
  - Your entangled code remains an island that can’t be com- posed with other functions.
  
Referential transparency (from pure functions) -> Substitution model -> equational reasoning 

Allows function composition

Reactive domain models
- Responsive to user interaction
- Resilient
- Elastic
- Message-driven

Event-driven programming

Events and commands

For each event, you have a type in your model.
Every domain event contains all information relevant to the change that just occurred in the system.
Events are meant to be consumed for further action by downstream components of your model.
Possibly the most important characteristic of a domain event. A monotonicity of time is built into the stream of events.


Summary
- Avoid shared mutable state within your model
- Referential transparency
- Organic growth
- Focus on the core domain
- Functional makes reactive easier
- Design for failure
- Event-based modeling complements the functional model
