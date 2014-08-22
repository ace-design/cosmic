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
For this illustration, SmartCampus provides two shared sensors : a temperature sensor (*TEMP_SENSOR* plugged in pin 1) and a humidity sensor (*HUM_SENSOR* plugged in pin 2)
###Alice and Bob's scenario 
We consider two users, Alice and Bob, who need to use the same sensors to build
their own application.
* **Alice** develops an application exploiting the associated LSI by collecting data from a temperature sensor every couple of second
* **Bob** develops an application exploiting a temperature sensor each second and a humidity sensor every three seconds.

Both of them will use the same temperature sensor

### Policies definition
In a first step, both users have to define their data collection policies in term of timed automaton. 
#### Alice
Alice's system has to receive temperature values every couple of seconds which corresponds to the following operations: (i) Get the value coming from the sensor, (ii) Emit this value on Internet
The code bellow presents the implementation of the timed automaton presented in FIG.1.

```scala
val pa = {
    // Variable definition
    val v_t = new ReadSensorResult()
            
    // Action definition
    val read = new ReadSensorAction("TEMP_SENSOR", v_t)
    val emit = new EmitAction(v_t, "alice", 8080)
        
    // States definition
    val s1 = new Node().addAction(read)
    val s2 = new Node().addAction(emit)
            
    // Transition definition
    val t1 = new Transition(s1, s2, new TickCondition(1))
    val t2 = new Transition(s2, s1, new TickCondition(1))
            
    // Policy definition
    new Behavior(s1).addNode(s2).addTransition(t1).addTransition(t2)
}     
```
#### Bob
Bob has to express two policies: (i) Temperature collection policy and (ii) Humidity collection policy.

```scala
val bob_temperature = {
    // Variable definition
    val v_t = new ReadSensorResult()
            
    // Action definition
    val read = new ReadSensorAction("TEMP_SENSOR", v_t)
    val emit = new EmitAction(v_t, "bob", 8080)
        
    // State definition
    val s1 = new Node().addAction(read).addAction(emit)
            
    // Transition definition
    val t = new LoopTransition(s1, new TickCondition(1))
            
    // Policy definition
    new Behavior(s1).addTransition(t)
}
     
val bob_humidity = {
    // Variable definition
    val v_h = new ReadSensorResult()
            
    // Action definition
    val read = new ReadSensorAction("HUM_SENSOR", v_h)
    val emit = new EmitAction(v_h, "bob", 8080)
        
    // State definition
    val s1 = new Node().addAction(read).addAction(emit)
    val s2 = new Node()
    val s3 = new Node()
    // Transition definition
    val t1 = new Transition(s1, s2, new TickCondition(1))
    val t2 = new Transition(s2, s3, new TickCondition(1))
    val t3 = new Transition(s3, s1, new TickCondition(1))
    
    // Policy definition
    new Behavior(s1).addNodes(List(s2,s3)).addTransitions(List(t1,t2,t3))
}     
``` 
Bob uses the composition operator to build a single policy containing both temperature collection and humidity collection policies.

```scala
val pb = bob_temperature + bob_humidity
``` 

### Decomposition process
The next step is related to the decomposition process thanks to the decompose operator. pa and pb are global policies that contain incompatible actions for the Arduino microcontroller (e.g. the emit action) platform and for the Raspberry nanocomputer (e.g. the read sensor action).
The code bellow shows this decomposition operation. The *deploy* function is configured to return two sub-policies: an Arduino sub-policy and a Raspberry sub-policy.
```scala
val (pamic, pabri) = pa.deploy()
val (pbmic, pbbri) = pb.deploy()
``` 

After this decomposition process, four layer specific sub-policies are obtained: (pamic, pabri, pbmic, pbbri).
### Composition process
These four sub-policies are then deployed on the shared infrastructure. The composition operator composes those policies and allows Alice and Bob to exploit the same piece of hardware. pamic is composed with pbmic and pabri is composed with pbbri:
```scala
val pSensor_platform = pamic + pbmic
val pBridge = pabri + pbbri
``` 
*pSensor_platform* and *pBridge* are the policies that will be instanced on the SmartCampus infrastructure.
### Code generation
The final step of the deployment process is handled by code generators working directly on these two latter policies.
```scala 
Utils.writefile("arduino", ToArduino(pSensor_platform))
Utils.writefile("raspberry", ToRaspberryThreaded(pBridge))
``` 
Codes resulting from this generation process are presented in annexes.
###Annexes

###Arduino Code
The code bellow is produced with the **Arduino** code generator applied on the *pSensor_platform* COSmIC code:
```C
/*
 *  GENERATED CODE
 *  DO NOT MODIFY
 *  Generated: Fri Aug 22 17:57:05 CEST 2014
 *  Language: C
*/
void setup() {
Serial.begin(9600);
}
void loop() {
int var_T73uP;int var_PShbs;int var_se8as;
var_T73uP = analogRead(2);
Serial.println("v=" + String(var_T73uP));
var_PShbs = analogRead(1);
Serial.println("v=" + String(var_PShbs));
var_se8as = analogRead(1);
delay(1000);

var_PShbs = analogRead(1);
Serial.println("v=" + String(var_PShbs));
Serial.println("v=" + String(var_se8as));
delay(1000);

var_PShbs = analogRead(1);
Serial.println("v=" + String(var_PShbs));
var_se8as = analogRead(1);
delay(1000);

var_T73uP = analogRead(2);
Serial.println("v=" + String(var_T73uP));
var_PShbs = analogRead(1);
Serial.println("v=" + String(var_PShbs));
Serial.println("v=" + String(var_se8as));
delay(1000);

var_PShbs = analogRead(1);
Serial.println("v=" + String(var_PShbs));
var_se8as = analogRead(1);
delay(1000);

var_PShbs = analogRead(1);
Serial.println("v=" + String(var_PShbs));
Serial.println("v=" + String(var_se8as));
delay(1000);


}
// END FILE
```

###Rasperry Code
The code bellow is produced with the **Raspberry Threaded** code generator applied on the *pSensor_platform* COSmIC code:
```python
'''
   GENERATED CODE
   DO NOT MODIFY
   Generated: Fri Aug 22 17:57:05 CEST 2014
   Language: Python
'''
import serial
import re
import threading
import array
import time
import datetime

'''''''''''''''''''''''''''''''''
        Global variables
'''''''''''''''''''''''''''''''''

ser = serial.Serial('/dev/ttyUSB0',9600, timeout=1)
buffer = 0
currentTime = datetime.datetime.now().time()

'''''''''''''''''''''''''''''''''
        Common methods
'''''''''''''''''''''''''''''''''

'''
Read value from serial
'''
def readValue():
    while True:
        x = ser.readline().decode("ascii")
        x = x.split("\r\n")[0]
        if re.match(r"v=[0-9]+",x):
            bufferize(int(re.findall(r'\d+', x)[0]))

'''
Bufferize a value
'''
def bufferize(v):
    global buffer
    buffer = v

'''
Emit value
'''
def emit(v,user,port):
    print("Emit " + str(v) + " to " + user + ":" + str(port))


'''
Update current time
'''
def updateTime():
    while True:
        global currentTime
        currentTime = datetime.datetime.now().time()
        time.sleep(1)

'''
Check if current time is in range
'''
def checkTime(time1, time2):
    if time1 <= time2:
        return time1 <= currentTime <= time2
    else:
        return time1 <= currentTime <= datetime.time(23,59,59) or datetime.time(0,0,0) <= currentTime <= time2

'''''''''''''''''''''''''''''''''
        Util threads
'''''''''''''''''''''''''''''''''
serialReadThread = threading.Thread(None, readValue, "readValue")
updateTimeThread = threading.Thread(None, updateTime, "updateTime")
serialReadThread.start()
updateTimeThread.start()
'''''''''''''''''''''''''''''''''
        User behaviors
'''''''''''''''''''''''''''''''''
def process_B5U9u():
	while True:	
		var_se8as = buffer
		time.sleep(1);

		emit(var_se8as,"alice",8080)
		time.sleep(1);


def process_Ia1Nz():
	while True:	
		var_T73uP = buffer
		emit(var_T73uP,"bob",8080)

		var_PShbs = buffer
		emit(var_PShbs,"bob",8080)
		time.sleep(1);


		var_PShbs = buffer
		emit(var_PShbs,"bob",8080)
		time.sleep(1);


		var_PShbs = buffer
		emit(var_PShbs,"bob",8080)
		time.sleep(1);




'''''''''''''''''''''''''''''''''
        Auto-start
'''''''''''''''''''''''''''''''''
process_B5U9u = threading.Thread(None, process_B5U9u,"process_B5U9u")
process_Ia1Nz = threading.Thread(None, process_Ia1Nz,"process_Ia1Nz")
process_B5U9u.start()
process_Ia1Nz.start()

'''''''''''''''''''''''''''''''''
        END FILE
'''''''''''''''''''''''''''''''''
``` 
