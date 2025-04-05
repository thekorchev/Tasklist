# Tasklist

This is the last Stage 6/6 stage for program from this project [https://hyperskill.org/projects/335](https://hyperskill.org/projects/237?track=3)

My account in Hyperskill: https://hyperskill.org/my-learning/495086218

## Save the list Stage 6/6
## Description
A task listing program is meaningless if each time you terminate it, the list of tasks is erased. We need a method of keeping tasks when the program isn't running. There are a good number of solutions: a database, several files, or even a cloud. In this stage, we are going to save the tasklist data locally into a file.

Saving data into a file can also take many forms. You can save data in XML, JSON, or any other custom format. For this project, we will opt for JSON with the help of the JSON Moshi library. If you're striving to learn more about it, take a look at Github Moshi.

To access the Moshi library, it should be added to your project dependencies, but this has already been done for you. In order to use it, you should just import it to your program with:

```bash
  import com.squareup.moshi.*
```
The tasklist data should be saved into a file named tasklist.json without using any paths:

```bash
  val jsonFile = File("tasklist.json")
```
As mentioned in stage 3, LocalDate and LocalDateTime classes need to be adapted for registration with Moshi. It is easier if you don't try to serialize them right away but use strings and integers for the date and time and only then serialize the tasklist data.

## Objectives
 - Before you terminate the program, it should create a file named tasklist.json and write all data relating to tasks in the JSON format.
 - When your program starts, it should check whether the tasklist.json file exists. If it does, read the JSON data with tasks.
## Example
The greater-than symbol followed by a space (> ) represents the user input. Note that it's not part of the input.

We will use images instead of snippets where colors can't be shown

Example 1: data is saved and loaded after a restart

```bash
  Input an action (add, print, edit, delete, end):
> print
No tasks have been input
Input an action (add, print, edit, delete, end):
> add
Input the task priority (C, H, N, L):
> N
Input the date (yyyy-mm-dd):
> 2022-1-21
Input the time (hh:mm):
> 12:00
Input a new task (enter a blank line to end):
> Pay bills
>
Input an action (add, print, edit, delete, end):
> add
Input the task priority (C, H, N, L):
> L
Input the date (yyyy-mm-dd):
> 2022-1-12
Input the time (hh:mm):
> 20:00
Input a new task (enter a blank line to end):
> Buy new book
>
Input an action (add, print, edit, delete, end):
> print
```
![Process](https://ucarecdn.com/163917e2-6f25-4adc-b92b-5d5a49c83756/-/stretch/off/-/resize/1100x/-/format/webp/)

```bash
  Input an action (add, print, edit, delete, end):
> end
Tasklist exiting!
```
```bash
  Input an action (add, print, edit, delete, end):
> print
```
![Process](https://ucarecdn.com/6ae4d2d1-81a3-4a8f-a11b-9070aa3d614f/-/stretch/off/-/resize/1100x/-/format/webp/)

```bash
Input an action (add, print, edit, delete, end):
> end
Tasklist exiting!
```
