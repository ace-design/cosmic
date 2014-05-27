COSmIC framework
================

The **COSmIC** (Composition Operators for Sensing InfrastuCture) is a tool aimed to support different users on a shared System Information by generating code deployed on each level composing the sensors network.

This short README is intended to give a brief overview of the framework and to provide the minimal material to understand how it works.

Architecture
------------

This framework is organised in different packages:

* fr.unice.modalis.cosmic.**actions**: actions modelisation 
* fr.unice.modalis.cosmic.**algo**: algorithms used to compose data collection behaviors (contains the COSmIC operators)
* fr.unice.modalis.cosmic.**conveter**: code generators
* fr.unice.modalis.cosmic.**core**: data structure used to build a user behavior
* fr.unice.modalis.cosmic.**exceptions**: exceptions handled by the framework
* fr.unice.modalis.cosmic.**scenario**: pre-defined scenarios which can be used to model basic user behaviors

How-to: Build a data collection behavior?
-------------------------------------------

A data collection behavior is defined as a simplification of classical timed automaton. It uses :

* Nodes: Correspond to the automaton' states on which, actions can be attached
* Transitions: Model the transition relation between two nodes. Theses transitions are conditioned by a time-to-wait (TickCondition) value before being parsed.

To be valid, a behavior **MUST** be Cyclic, Deterministic and only contain transitions conditioned by a TickCondition. 

The code bellow build a blank behavior (i.e. no actions performed) with 3 nodes and a 5 seconds period:


```scala
  /* Define nodes modeling states */
  val n1 = new Node("A")
  val n2 = new Node("B")
  val n3 = new Node("C")

  /* Define transitions */
  val t1 = new Transition(n1,n2, new TickCondition(1)) // Wait 1s
  val t2 = new Transition(n2,n3, new TickCondition(2)) // Wait 2s
  val t3 = new Transition(n3,n1, new TickCondition(2)) // Wait 2s

  /* Build behavior with n1 as entry point */
  val b = new Behavior(n1).addNodes(List(n1,n2,n3)).addTransitions(List(t1,t2,t3))
```

How-to: Use actions in a data collection behavior?
---------------------------------------------------

The COSmIC framework provides 5 kinds of actions (see in fr.unice.modalis.cosmic.actions.unit):

* ReadSensorAction: Read a value from a given sensor
* InitSerialAction: Initialize a serial port
* ReadSerialAction: Read a value on the serial port
* WriteSerialAction: Write a value on the serial port 
* EmitAction: Emit a value to an external endpoint

Some of theses actions returns results which can be saved in:

* InitSerialResult: Store the reference to a serial port
* ReadSensorResult: Store the sensor read value
* ReadSerialResult: Store the value read on the serial port

The code bellow presents how to:

1. Read value from a sensor
2. Send this value to the bridge
3. Emit the value to an external endpoint

```scala
  // Define result variables
  val readSensorResult = new ReadSensorResult()
  val refSerial = new InitSerialResult()
  val readSerialResult = new ReadSerialResult()

  // Define actions
  
  // Read value from a sensor
  val readSensorAction = new ReadSensorAction("tempSensor", readSensorResult)
  // Send this value to the bridge
  val sendSerialAction = new WriteSerialAction(readSensorResult)
  
  // Bridge serial init and read
  val initSerialAction = new InitSerialAction("COM1", refSerial)
  val readSerialValue = new ReadSerialAction(refSerial, readSerialResult)
  
  // Emit value
  val emitAction = new EmitAction(readSerialResult, "http://myserver", 8080)
```

*NB: In a further version, bridge serial initialisation and reading would be encapsulate in a SendToBridgeAction class.*

Theses action can be attached to *nodes* thanks to the `addaction(a:Action)` method.

```scala
  /* ... */
  val node = new Node("myNode").addAction(readSensorAction)
                               .addAction(sendSerialAction)
                               .addAction(initSerialAction)
                               .addAction(readSerialValue)
                               .addAction(emitAction)
  /* ... */
```

How-to: Define guards on actions?
---------------------------------

The COSmIC framework provides also a guard mechanism. We define a guard as a boolean equation which allow or disallow the action execution according to the current context.

Guards are represented under a boolean equation of constraints. The COSmIC framework allows to model the following constraints:
* TimeConstraint: an action can be executed only in a given time set)
* ValueConstraint: the action execution depends on the comparison between a value and a threshold

*NB: The code generator provided in the COSmIC Framework only handle ValueConstraint for the moment*

Different constraints can be mixed together to build a boolean equation with the following predicates:
* ANDPredicate: represents `constraint1 ∧ constraint2`
* ORPredicate: represents `constraint1 || constraint2`
* NOTPredicate: represents `~constraint`

The results of xPredicate statements are also *guards* which can be composed with others.

In the example presented bellow, we re-use the previous action definition to a model a temperature collect behavior only if the measure is lower than 20° and occurs during work time:

```scala
  /* ... */
  
  // Define result variables
  ...
  
  // Define actions
  ...
  val emitAction = new EmitAction(readSerialResult, "http://myserver", 8080)
                        .addGuard(new ANDPredicate(
                            new ValueConstraint(readSerialValue, 20, "<"), 
                            new TimeConstraint("08:00","18:00"))
                            )
```

How-to: Compose two (or more) behaviors?
---------------------------------------
**PRE-CONDITION:** Behaviors must be valid before applying the COSmIC algebra (*i.e.* **Cyclic**, **Deterministic** and **only contain transitions conditioned by a TickCondition**)

All algorithms used during the composition process are located in the **fr.unice.modalis.cosmic.algo.Transformation** object.

This class provides in particular the `+`-operator (compose(b1:Behavior,b2:Behavior):Behavior) to compose two behaviors. As an example, we define the well-known Alice & Bob behaviors and we compose them:

```scala
  val refRead1 = new ReadSensorResult()
  val n1 = new Node("Alice").addAction(new ReadSensorAction("temp", refRead1))
                            .addAction(new WriteSerialAction(refRead1, "Alice"))
  val t1 = new Transition(n1, n1, new TickCondition(3))
  val alice = new Behavior(n1).addTransition(t1) //Alice behavior

  val refRead2 = new ReadSensorResult()
  val n2 = new Node("Bob").addAction(new ReadSensorAction("temp", refRead2))
                          .addAction(new WriteSerialAction(refRead2, "Bob"))
  val t2 = new Transition(n2, n2, new TickCondition(2))
  val bob = new Behavior(n2).addTransition(t2) // Bob behavior
```

The validity of Alice and Bob behaviors can be asserted with the `isCorrectBehavior(b: Behavior)` method in the **fr.unice.modalis.cosmic.algo.Utils** object:

```scala
  assert(Utils.isCorrectBehavior(alice) && Utils.isCorrectBehavior(bob) == true)
```

If the assertion is true, Alice and Bob behaviors can be composed together. The COSmIC framework provides syntactic sugar for composition:

```scala
// Without syntaxic sugar
val composed = Transformation.compose(alice, bob)
// With syntaxic sugar
 val composed = alice + bob
```

How-to: Deploy a behavior on the sensor network?
------------------------------------------------

The COSmIC framework provides code generators for sensor platforms and bridges. In this first version, we provide an Arduino generator (Wiring) and Raspberry Pi generator (Python). 

The deployment process is the following:

1. Compose the user collect behaviors
2. Slice this composed behavior into two sub-behavior : (sensor platform behavior, bridge behavior)
3. Forward the Sensor platform behavior (time synchronization is performed on the bridge)
4. Minimize the Sensor platform behavior
5. Generate code for the sensor platform behavior and the bridge behavior

```scala
/* Alice and Bob behaviors were defined previously */
  val composed = alice + bob // Step 1

  val (spBehavior, brBehavior) = Transformation.decompose(composed) // Step 2

  val forwardedSP = Transformation.forward(spBehavior) // Step 3
  
  val minimizedSP = Transformation.minimize(forwardedSP) // Step 4
  
  ToSensorPlatform(minimizedSP) // Step 5 : generate Wiring code for Arduino
  ToBridge(brBehavior) // Step 5 : generate Python code for Raspberry
```

A sugar syntactic trick allows the user to abstract this process:

```scala
  val composed = alice + bob // Step 1

  val (spBehavior, brBehavior) = composed.deploy() //Step 2,3,4

  ToSensorPlatform(spBehavior) // Step 5 : generate Wiring code for Arduino
  ToBridge(brBehavior) // Step 5 : generate Python code for Raspberry
```