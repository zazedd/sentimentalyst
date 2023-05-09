import stanza
import sys

nlp = stanza.Pipeline('multilingual', verbose=False, processors="langid", model_dir="./stanza")

args = sys.argv

doc = nlp(args[1])
print(doc.lang)
