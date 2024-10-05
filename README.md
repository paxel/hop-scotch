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

The benefit is of course: If you add a stage in the middle, or you remove a stage, or you replace a stage, you just have
to change the parameters you feed to the hop scotch and run it again, without changing the whole logic behind.

# An example

We have 3 stages.
The Data are sentences.

**Stage 1**

**Hop 1 - Language detector**

Predicate: all<br/>
Enriches the sentence with the "language" of the text (e.g. French)

**Stage 2**

**Hop 1 - French translator**

Predicate: language: French<br/>
Enriches the sentence with an english translation of the French original

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

If you add more Hops in stage 2, you automatically support more languages (if the detector can detect them)
And if you replace the PDF builders with just a French one, you will only have the French PDF.
Additionally, if you insert a translator from English to French between stage 2 and 3, you will have all texts in French
as PDF.

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

