import stanza
import sys

nlp = stanza.Pipeline('en', verbose=False, model_dir="./stanza")

args = sys.argv

doc = nlp(args[1])
for i, sentence in enumerate(doc.sentences):
    print("%d|%d" % (i, sentence.sentiment))
