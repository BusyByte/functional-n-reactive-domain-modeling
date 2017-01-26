# Reactive persistence and event sourcing

## 8.1 Persistence of domain models (page 231)

You have only the current snapshot of how it looks `now`. 
Your data model is the `shared mutable state`.

## 8.2 Separation of concerns (233)

two aspects that demand a clear separation are the `reads` and the `writes`

### 8.2.1 The read and write models of persistence (page 234)

context

queries

commands

### 8.2.2 Command Query Responsibility Segregation (page 235)

## 8.3 Event sourcing (events as the ground truth) (page 237)

## 8.3.1 Commands and events in an event-sourced domain model (page 238)

domain events - after occurred

command handlers

### 8.3.2 Implementing CQRS and event sourcing (page 240)

batching commands

free monad

handling side effects

## 8.4 Implementing an event-sourced domain model (functionally)(page 242)

### 8.4.1 Events as first-class entities (page 243)

events past tense with dates

snapshot

### 8.4.2 Commands as free monads over events (page 245)

### 8.4.3 Interpreters—hideouts for all the interesting stuff (page 247)

the interpreter that delivers account service

a sample run

### 8.4.4 Projections—the read side model (page 252)

projections - read models

- Push or pull
- Restarting projections


### 8.4.5 The event store (page 253)

### 8.4.6 Distributed CQRS—a short note (page 253)

### 8.4.7 Summary of the implementation (page 254)

## 8.5 Other models of persistence (page 255)

complexity of paradigm

- Different paradigm
- Operational complexity
- Versioning

slick

### 8.5.1 Mapping aggregates as ADTs to the relational tables (page 255)

Effective Aggregate Design series of articles - https://vaughnvernon.co/?p=838
    
### 8.5.2 Manipulating data (functionally) (page 257)

Slick offers more-reactive APIs by allowing you to stream your database query results directly into an Akka Streams–based pipeline

### 8.5.3 Reactive fetch that pipelines to Akka Streams (page 258)
 
## 8.6 Summary (page 259)

- CRUD isn’t the only model of persistence
- Implementing an event-sourced domain model functionally
- FRM, not ORM
- 