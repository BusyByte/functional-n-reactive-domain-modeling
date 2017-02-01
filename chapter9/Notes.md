# Testing your domain model

## 9.1 Testing your domain model (page 260)

testability

manual testing is `no` testing

- Repeatable
- Scalable with an additional dataset
- Maintainable
- Noninvasively extensible with addition of features
- Capable of being integrated with the build system

pure functions

## Designing testable domain models (page 262)

testable in isolation

### 9.2.1 Decoupling side effects (page 263)

Created MockIdVerifier when could have just overridden method.
Now have another thing to track

### 9.2.2 Providing custom interpreters for domain algebra (page 264)

Free Monad custom interpreter for testing with map

### 9.2.3 Implementing parametricity and testing (page 265)

parametricity

test with any A, B, and F as long as has Monoid of B and Foldable of F

## 9.3 xUnit-based testing (page 266)

not exhaustive

## 9.4 Revisiting the algebra of your model (267)

Functions operating on types + Types + Properties / Laws / Business rules = Algebra

## 9.5 Property-based testing (page 268)

ScalaCheck

### 9.5.1 Modeling properties (page 268)

property

generator

predicate verifies

### 9.5.2 Verifying properties from our domain model (page 270)

verifying account close behavior

### 9.5.3 Data generators (page 274)

Gen.frequency

### 9.5.4 Better than xUnit-based testing? (page 277)

- Intuitive
- Thinking in properties
- Domain rule repository
- Data generation

## 9.6 Summary (page 278)

- Testability is closely related to modularity
- xUnit and its limitations
- Property based testing and its advantages

