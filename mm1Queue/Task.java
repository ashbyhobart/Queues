//package mm1Queue;

public class Task {

 private double arrival;
 private String name;
 
 public Task(double time, String n) {
  arrival = time;
  name = n;
 }
 
 
 public double getArr() {
  return arrival;
 }
 
 
 public String getName() {
  return name;
 }
 
}
