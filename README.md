# hop-scotch

A self managing pipeline for processing data

# The idea

Define your processing stages with as little effort as possible:

* The stage (a numerical number)
* A Predicate / Factory to check if the code should be run with the data
* The Actual code to process the data

The hop scotch framework will take these and build a chain of actors with the lintstone actor framework and provide you with:
* A method to feed the data in
* An interface to check the process

Everything else is up to the user to implement.

![](https://upload.wikimedia.org/wikipedia/commons/4/49/Amarelinhacefet.jpg)


The benefit is of course: If you add a stage in the middle or you remove a stage, or you replace a stage, you just have to change the parameters you feed to the hop scotch and run it again, without changing the whole logic behind.

# An example
 
We have 3 stages.
The Data are sentences.

**Stage 1**

**Hop 1 - Language detector**

Predicate: all<br/>
Enriches the sentence with the "language" of the text (e.g. french)

**Stage 2**

**Hop 1 - French translator**

Predicate: language: french<br/>
Enriches the sentence with an english translation of the french original

**Hop 2 - German translator**

Predicate: language: german<br/>
Enriches the sentence with an english translation of the german original

**Stage 3**

**Hop 1 - PDF Builder (original)**

Predicate: all<br/>
Aggregate all original sentences into a PDF

**Hop 2 - PDF Builder (translated)**

Predicate: translation exists<br/>
Aggregate all translated sentences into a PDF

In this example, if you add more Hops in stage 2, you automatically support more languages (if the detector can detect them)
And if you replace the PDF builders with just a french one, you will only have the french PDF. Additionally if you insert a translator from english to french between stage 2 and 3 you will have all texts in french as PDF.

# Types of Hops

To create this hop-scotch, you can implement Two types of Hops:

## Processor

The Processor is given to the hopscotch as an instance if an implementation of `ProcessorHopFactory<D,M>`.<br/>
Which provides 3 methods:

```java
int getStage();
Processor<D> create(M meta);
Predictor<M,D> createPredictor();
```

## Gates

A Gate is a Special Processor that tells the next stage to drop this data.<br/>
If there are multiple gates on a stage, the next stage will drop the data if any one of them demands the drop.

```java
int getStage();
Processor<D> create();

```

