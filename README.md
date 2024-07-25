# BI_Word
Extend the project of the HW#!  To have the following two features:

 # 1-  BI_word index    [3 Marks]
 
 You should enter each word alone and  with an underscore with the next word
 For example
 the doc:   automated retrieval of specific information 
will be entered
 automated,  retrieval, automated_retrieval, of,  retrieval_of, specific, 
of_specific, information, specific_information 
the query: will be
 “ specific information”  and will return the document 
 
i.e. if in quotes insert underscore between the 2 words
# Bonus: mix single words with phrase 
i.e  the query:   automated  “specific information” 
will return the last document
 however if a document contains the three word but  separated will not be returened
 
# 2-  include a positional index such that 

given  doc1: Cairo  University Zayed City is a new CU branch 
and     doc2:  Zayed attending in AinShams University in Cairo
 The query “ Cairo  University Zayed City” would return doc 1 but not doc 2
