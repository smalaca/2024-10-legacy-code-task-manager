# 0001: Component Based Architecture

## Status
Accepted

## Context
Task Manager is legacy monolith application with complex and coupled code.
To make maintenance easier are gain control over it complexity we want to introduce architecture 
to put the logic in a boundaries so that navigation will get easier.

## Solutions
- Component-Based Architecture
  - helps to define boundaries of contexts
  - used as an entry point for other architectures
- Layered Architecture
  - helps to define layers within context 
  - used for whole project or context
- Port and Adapters Architecture
  - helps to define layers within context 
  - used for whole project or context

## Decision
Use Component-Based Architecture as a high level architecture. Thanks to this it will get an opportunity 
to visualize boundaries between contexts. Other solutions can be used later within given context.

## Consequences
- Each context will require its own architecture. That is contradiction to most developers' 
  habits and may make it more difficult to learn how to use such approach.
- Communication between packages have to be managed carefully.
- Due to the size of the project this re-architecture will continue for several years. 
