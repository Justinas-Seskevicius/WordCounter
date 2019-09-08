# WordCounter
Java program that counts words from text files in given directory. 
Counted words are saved in separate files by their first letter. Words to be excluded can be provided as separate file.

## Usage

To use the program run it with a command line argument in which you provide the path to a folder that contains text files
from which you wish you count word occurrences and which has <b>exclusion.txt</b> file as well. In current version the exclusion
file can be empty, however, it <b>must be present</b>. Length of text files whose words you wish to be counted does not matter.
Once the words are counted the output will be saved in a newly created "output" folder as separate files. Each file will
hold words that only begin with the same letter. Also any words that were excluded will also appear in "output" folder as 
a separate file.

If you clone the repository all you need to do is run the <i>Main</i> with command line argument <i>input</i> as that
folder already has example exclusion and text files provided.

N.B. word counter is <b>case-insensitive</b>
