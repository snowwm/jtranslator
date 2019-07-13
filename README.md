# JTranslator

A tool for translating between Russian (*technically, any language with less than 2^19 words*)
and a language invented by me, consisting of the only word "ГРЁЗОБЛАЖЕНСТВУЮЩИЙ". 
Encoding is based on the binary system. Written circa 2013 (I was 15 years old).

## Running

**Doesn't work on Java 9+ due to poorly written resource loading.**

#### Windows:

Just use the provided executable, or

    > java -jar JTranslator_1.0.jar
    
#### Unix:

    $ java -Dfile.encoding=cp1251 -jar JTranslator_1.0.jar 
