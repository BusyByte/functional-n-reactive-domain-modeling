## 6.1 Reactive Domain Models (page 181)

`bounded latency` - system must respond to the user’s request within a specific time delay
 
 `actors all-the-way-down`
 
## 6.2 Nonblocking API design with futures (page 184)

### 6.2.1 Asynchrony as a stackable effect (page 185)

`scalaz.\/` is a `sum type`

layering them using `monad transformers`

`monads don’t compose`

` to make your type as close as possible to the effect that you’re trying to implement`

### 6.2.2 Monad transformer-based implementation (page 187)

## Exercise 6.1 HANDLING EFFECTS (ALGEBRAICALLY) (page 188)

### 6.2.3 Reducing latency with parallel fetch—a reactive pattern

### 6.2.4 Using scalaz.concurrent.Task as the reactive construct (page 193)

`Because our interpreter returns a Task, the implementation is already reactive`

Per the reactive manifesto there is more needed to be reactive rather than 
just asynchronous

EXERCISE 6.2 FUTURE AND SCALAZ.CONCURRENT.TASK (page 195)

## 6.4 The stream model (page 197)

### 6.4.1 A sample use case (page 198)

pretty much same as other examples
reactive-kafka -> alpakka for hbase


### 6.4.2 A graph as a domain pipeline (page 202)

### 6.4.3 Back-pressure handling (page 204)

don't need back pressure if you can scale horizontal infinitely or have infinite resources
don't need back pressure if load on system is not enough to overwhelm consumer

## 6.5 The actor model (page 205)

### 6.5.1 Domain models and actors (page 206)

While actors have a generic signature of `PartialFunction[Any, Unit]` you can wrap 
it inside an interface so that you'll only ever get one response per request
 
for example you can wrap an actor inside of the method signature 
`HttpRequest => Future[HttpResponse]`

says not good for end `user-api's`, 
most traits I see are internal to an application anyway

`some specific use cases, you can have actor-based implementation of some end-user APIs`

`especially true when you need to enforce mutual exclusion on a mutable state`

- Protect shared mutable state
- Resilience
- Prefer akka streams over actors

CQRS

# 6.6 Summary (page 2.11)

- What is a reactive domain model? 
- Futures and promises
- Asynchronous messaging
- The stream model
- The actor model



