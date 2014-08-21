#Software Development Support for Shared Sensing Infrastructures: a Generative and Dynamic Approach
##Article abstract
This companion page is attached to the article *Software Development Support for Shared Sensing Infrastructures: a Generative and Dynamic Approach* written by Cyril Cecchinel, Philippe Collet and Sebastien Mosser, submitted to ICSR 2015. 

Sensors networks are the backbone of large sensing infrastructures such as
*Smart Cities* or *Smart Building*. Classical
  approaches suffer from several limitations hampering
  developers' work (e.g. lack of sensor sharing, lack of dynamicity in data collection policies, need to dig inside big data sets, absence of reuse between implementation platforms). 
  This paper presents a tooled approach to tackle these issues. It couples (i) an abstract model of developers' requirements in a given infrastructure to (ii) timed automata and code
  generation techniques, so to support the efficient deployment of reusable data collection policies on different infrastructures.
  The approach has been validated on several real-world scenarios and is currently experimented on a smart academic campus.
  
  
##Introduction
On this companion page, we illustrate the current COSmIC implementation by modeling the running example. We follow the same step-by-step process described in the illustration (Sec 4.2)
###Targeted plateform
We target the SmartCampus physical infrastructure based on two layers: the micro-controller (sensors and sensor boards) layer implemented by Arduino and the bridge layer
(bridging the sensor network and the Internet) implemented by Raspberry Pi nano-computer.

###Alice and Bob's scenario 
We consider two users, Alice and Bob, who need to use the same sensors to build
their own application.
* **Alice** develops an application exploiting the associated LSI by collecting data from a temperature sensor every couple of second
* **Bob** develops an application exploiting a temperature sensor each second and a humidity sensor every three seconds.

Both of them will use the same temperature sensor

### Policies definition
#### Alice
Alice's system has to receive temperature values every couple of seconds which corresponds to the following operations: (i) Get the value coming from the sensor, (ii) Emit this value on Internet

### Decomposition process
### Composition process
### Code generation
