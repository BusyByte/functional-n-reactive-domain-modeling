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

## 5.5 Another pattern for modularization—free monads
