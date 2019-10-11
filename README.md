# Information-Retrieval
SUNY-Buffalo:  CSE 535-Information Retrieval

Implementation of *DAAT(Document at a time)* and *TAAT(Term at a time)* in java.

*Lucene* Inverted Index Search was used.

##Scoring:
 TAAT: Scores for all docs computed concurrently, one query term at a time
 DAAT:Total score for each doc (incl all query terms) computed, before proceeding to the next
