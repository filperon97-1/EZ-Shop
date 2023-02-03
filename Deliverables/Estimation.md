# Project Estimation  
Authors: Andrea Colli Vignarelli, Filippo Peron, Roberto Comella, Stefano Palmieri   
Date: 30/04/2021
Version: 1.0
# Contents
- [Estimate by product decomposition]
- [Estimate by activity decomposition ]
# Estimation approach
Estimantion approach is based on our judgment.
# Estimate by product decomposition
### 
|             | Estimate                        |             
| ----------- | ------------------------------- |  
| NC =  Estimated number of classes to be developed   |             22                |             
|  A = Estimated average size per class, in LOC       |             138            | 
| S = Estimated size of project, in LOC (= NC * A) |    3036     |
| E = Estimated effort, in person hours (here use productivity 10 LOC per person hour)  |                    304                  |   
| C = Estimated cost, in euro (here use 1 person hour cost = 30 euro) | 9120 | 
| Estimated calendar time, in calendar weeks (Assume team of 4 people, 8 hours per day, 5 days per week ) |         1.9           |               
# Estimate by activity decomposition
### 
|         Activity name    | Estimated effort (person hours)   |             
| ----------- | ------------------------------- | 
|  Perform work analysis  | 16 |
|  Model process  | 16 |
|  Identify requirements  | 24 |
|  Consult customer for requirements    |   16   |
|  Model design  | 24 |
|  Review model  | 8 |
|  Code implementation  | 304 |
|  Testing  | 60 |
|   Revision & retesting | 40 |
|   Produce user documentation  |   48  |
|   Setup infrastructure    |   16  |
|   Testing total system    |   16  |
|  Total  | 469 |
###
```plantuml
saturday are closed
sunday are closed
Project starts the 12nd of april 2021
[Perform work analysis] lasts 2 day
then [Model process] lasts 2 day

[Identify requirements] starts at [Model process]'s end and lasts 3 days
[Consult customer for requirements] starts at [Model process]'s end and lasts 2 days
note bottom
  Customer should be consulted during requirements definition
end note
[Start coding phase] happens at [Identify requirements]'s end
then [Model design] lasts 3 days
then [Review model] lasts 1 day
then [Code implementation] lasts 10 days
then [Testing] lasts 4 days
then [Revision & retesting] lasts 3 days
[Setup infrastructure] starts at [Testing]'s end and lasts 2 days
note bottom
  Such as local db
end note
[Testing total system] starts at [Revision & retesting]'s end and lasts 2 days
[First product distribution] happens at [Testing total system]'s end
```
In the Gantt chart many activities are considered to be executed by one/two person only, due to the impossibility of the parallelization of the task.
