# hop-scotch

A self managing pipeline for processing data

# The idea

Define your processing stages with as little effort as possible:

* The stage (a numerical number)
* A Predicate / Factory to check if the code should be run with the data
* The Actual code to process the data

The hop scotch framework will take these and build a chain of actors with the lintstone actor framework and provide you
with:

* A method to feed the data in
* An interface to check the process

Everything else is up to the user to implement.

The benefit is, of course: If you add a stage in the middle, or you remove a stage, or you replace a stage, you have
to change the parameters you feed to the hop scotch and run it again, without changing the whole logic behind.

# An example

We have 3 stages.
The Data are random files.

**Stage 1**

**Hop - Image detector**

Predicate: all<br/>
If the file is an image of a known type, the Image detector adds an enrichment

image.type=\[PNG|JPEG|GIF]

**Stage 2**

**Gate: Image gate**

Predicate: all<br/>
Checks if the image.type is set, otherwise drops it

**Stage 3**

**Hop 1: Image Uploader**

Predicate: language: all<br/>

Uploads all files to a gallery


# Types of Hops

To create this hop-scotch, you can implement Two types of Hops:

## Hop

A **JudgeFactory** is used to create a **Judge** in the StageActor.
Whenever Data is received by the Stage, it asks the Judge for a **Judgment** of the Data.
A positive Judgment can be used to initialize a HopActor that processes all Data that match this Hop.

The Hop can enrich the given data; however, it is required.

## Gates

A Gate is a Special Processor that tells the next stage to drop this data.<br/>
If there are multiple gates on a stage, the next stage will drop the data if any one of them demands the drop.

---


WIP notes for future Paxel:

- [ ] Finish Enrichment
    - [ ] Aggregation unit test
    - [ ] Statistics objects
    - [x] Need a final statistics consumer for poison pill handling 
    - [x] Poison pill shutdown 
    - [x] Replace String with Stage message 
    - [x] KeyBuilder implementation and unit test 
    - [x] Javadoc and CI working 
    - [x] Implement Aggregation 
    - [x] Implement Merge Method for Aggregation 
    - [x] Implement Copy Method to create a dedicated HopData instance for all Actors 
    - [x] Add Creator and Stage to all Hops and Gates 
    - [x] Add backpressure to all StageActors 
- [ ] Implement Key Query
- [ ] Implement Creator Query
- [ ] Implement Stage Query
- [ ] Implement Value Query
    - [ ] All Queries can provide a set of their values (e.g. keys) that is filtered by the previous Queries 🛠️
    - [ ] All Queries filter the result 🛠️
    - [ ] All Queries filter for type (e.g Integer) 🛠️
