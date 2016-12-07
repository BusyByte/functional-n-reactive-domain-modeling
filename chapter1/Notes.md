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