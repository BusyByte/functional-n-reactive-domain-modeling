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