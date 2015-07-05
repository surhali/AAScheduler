# AASchedulerThis project is AM Scheduler for JPMC.
The main class of this project is com\jpmc\am\scheduler\messaging\client\AMSchedulerClient.java.
The execution begins from this point.
The basic features are

1) Messages are received in Queue
2) Messages are processed once the Resources are available.
3) The main thread continuously looks out for Messages and Resources.
4) Processed messages are sent to the gateway.
5) When a message is terminated, further messages are subject to exception.
6) Messages of Cancelled Group are not sent to the gateway. 


Build.

This is a fully compiled eclipse project. Can be downloaded onto eclipse IDE and can be buit and executed.

Please download the source code as zip File from GitHub.
Extract the Zip and Import the project into Eclipse.
Add the two Junit Libraries in the build path and compile.
Run AMSchedulerClient.java, as Java Application to view the output in the Console.

