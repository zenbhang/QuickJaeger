# QuickJaeger

An quick and easy way to implement Jaeger APM into your codebase!
Proven to also work in a docker container on Kubernetes (speicifically EKS).

## Setup
Copy over the classes and merge the POM file, and you will have a working implementation of Jaeger!


## Usage
To use the implementation of Jaeger, do the following:

### In Java
1. Import the QuickJaeger class
```
import [path].QuickJaeger;
```
2. Call upon QuickJaeger as an object with the process name as a string argument.
```
QuickJaeger qj = new QuickJaeger("exampleProcess");
```
3. Identify where in your code you would like to implement a span and create it with the createSpan() method.
```
qj.createSpan("Task");
```
4. Add any logs to the span with the log method
```
qj.log("Process", "THIS IS A LOG");
```
5. Add any tags with the setTag() method
```
qj.setTag("TaskType", "Example")
```
6. Call upon the endSpan() method to end the span
```
qj.endSpan("Task")
```
7. Call upon the error() method for usage in exceptions and other error cases
```
try{

}catch(exception e){
  qj.error("EXCEPTION FOUND" + e);
}
```

With that, you will now have proper working spans from your code base being exported to the Jaeger instance that you have set up!

Creator's note 6/24/2020
Currently supporting Java, but planning to create versions for python, Ruby, and whatever is requested.
