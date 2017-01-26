# Modelling with Reactive Streams

## The reactive streams model (page 214)

best of both worlds - ansynch boundaries and well defined typed api which composes

## 7.2 When to use the stream model (page 215)

operators - processing units
channels - interactions

flow - a first class abstraction

flow graph

## 7.3 The domain use case (page 216)

## 7.4 Stream-based domain interaction (page 217)

## 7.5 Implementation: front office (page 218)

## 7.6 Implementation: back office (page 220)

## 7.7 Major takeaways from the stream model (page 223)

- Modeling data that flows like a stream
- Declarative
- Modular
- Concurrent and parallel
- Back pressure

## 7.8 Making models resilient (page 224)

### 7.8.1 Supervision with Akka Streams (page 225)

### 7.8.2 Clustering for redundancy (page 226)

### 7.8.3 Persistence of data (page 226)

persistent actors
event sourcing

## 7.9 Stream-based domain models and the reactive principles (page 228)

www.reactivemanifesto.org

- Resilience
- Elasticity
- Message driven

## 7.10 Summary (page 229)

- Using reactive streams for domain modeling
- Handling asynchronous boundaries
- Better than native actors as a programming model
- Managing resiliency
- Handling back pressure

