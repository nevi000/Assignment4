Assignment 4
------------

# Team Members

# GitHub link to your (forked) repository (if submitting through GitHub)

...

# Task 2

1. Why did message D have to be buffered and can we now always guarantee that all clients
   display the same message order?

2. Note that the chat application uses UDP. What could be an issue with this design choice—and
   how would you fix it?
   
# Task 3

1. What is potential causality in Distributed Systems, and how can you model it? Why
   “potential causality” and not just “causality”?
>Potential causaulity refers to the possible relationship between two events in a distributed system. 
>Since processes do not share a global clock, we can only determine whether one event may have influenced another.
> Lamport Clocks or Vector Clocks track which event happened before

2. If you look at your implementation of Task 2.1, can you think of one limitation of Vector Clocks? How would you overcome the limitation?
>

3. Figure 4 shows an example of enforcing causal communication using Vector Clocks. You can find a detailed explanation of this example and the broadcast algorithm being used in
   the Distributed Systems book by van Steen and Tannenbaum (see Chapter 5.2.2, page 270). Would you achieve the same result if you used the same broadcast algorithm but replaced
   Vector Clocks with Lamport Clocks? If not, why not? Explain briefly. 
>
