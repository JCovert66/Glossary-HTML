# Glossary-HTML
This is a Java project where an HTML glossary index is created when given a list of words and definitions.

All of the HTML files in this repository were created with the Java code supplied. The code will first ask the user for the name of the .txt file which contains a list of words and their definitions with the format as follows: the first line of the .txt file contains a word, the following line(s) contain the word's definition. Once the definition is completed, the following line should be blank, and the line after should be another word. This pattern continues until there are no more words to add to the glossary, at which the file shall end with two empty lines indicating the end of the list.

A sample .txt file is included in this repository as terms.txt.

The Java code will read through the .txt file and will group each word with its definition. It will then sort the words by alphabetical order and create an index page for the glossary which lists each word in alphabetical order. The words are each hyperlinks to their respective pages which include said word's definition. Additionally, if a word that is in the index is used in any of the word's definitions, it too will be turned into a hyperlink to that word's definition.
