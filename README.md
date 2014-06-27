APE Predicate Evolver
=====================

APE takes as input two training datasets and evolves a predicate function using
a genetic algorithm that accepts a "clean" dataset and rejects a "dirty"
dataset. This predicate can then be used as a discriminator for other "clean"
and "dirty" data points.

This project originally accepted performance metrics of a running
application. The clean dataset was collected when the program was running under
normal operating characteristics. The dirty dataset was collected when the
program was running with a known fault injected, or an otherwise anomalous
state. The evolved predicate, as per the experiemnt hypothesis, could then be
used to detect new anomalous runtime behavior.

A predicate is an expression comprising of variables (a variable represents a
runtime metric such as `tcp_bytes_written` or `memory_heap`), comparison
operators (< > <= >=), and logical operators (AND, OR, XOR, NAND, NOR). Each
predicate's fitness is measured by the fitness function:

    F_β = ((1 + β^2) * pos) / ((1 + β^2) * pos + β^2 · fneg + fpos)

Choosing `β = 1` weighs false positives and false negatives equally. Choosing
`β < 1` prefers false positives over false negatives. Choosing `β > 1` prefers
false negatives over false positives. Fitness is calculated by evaluating each
data point from the traing datasets.

The genetic algorithm employs the island model of evolution, where several
populations are evolved independently, and predicates are occasionally migrated
randomly. The next generation of a population is evolved by randomly "breeding"
predicates. This is done by replacing a randomly selected subexpression from
the first parent with a randomly selected subexpression from the second. The
resulting predicate may be randomly mutated too. Elitism is employed to carry
the best fit predicate to the next generation.


Implementation
==============

The file `Evolve.java` serves as the main entry point. It takes in a properties file
as a parameter with the following properties:

 * `clean.file` - string - A tab separated values file containing the "good" data to fit against. Data should be normalized to 0 -> 1
 * `dirty.file` - string - A tab separated values file containing the "bad" data to fit against. Data should be normalized to 0 -> 1
 * `report.rate` - integer - Report run statistics every x seconds
 * `thread.count` - integer - number of workers/islands
 * `population.size` - integer - The size of the island's population
 * `max.generations` - integer - The number of generations to simulate
 * `mutation.rate` - double - The probability of mutating an organism
 * `migration.rate` - double - The probability of migrating an organism to a different island
 * `random.generation` - boolean - If true, All seeds are randomly generated. Otherwise, a hyperbox is added.
 * `generate.multivariate` - boolean - multivariate nonterminals are used in addition to single variable
 * `expr.depth` - integer - the maximum dept of an evolved predicate

The file `Chronos.java` keeps track of the generations of the experiment and
halts when terminal conditions are met. Termination occurs when a perfectly fit
individual is generated (this is not usually a good sign!) or the maximum
number of generations is reached.

The file `PredicateDesigner.java` defines all algorithms for generating,
evaluating, and breeding predicates.

The file `Island.java` migrates predicates with other islands between
simulation generations and listens for experiment termination.

The file `Environment.java` manages the Island's population, and handles the
selction of organisms for the next generation.

The file `FMeasure.java` is a container for a predicate expression's calculated
fitness.
