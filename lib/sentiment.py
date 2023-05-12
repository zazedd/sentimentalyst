import stanza

nlp = stanza.Pipeline('en', verbose=False, model_dir="./stanza")

doc = nlp(input())

sum = 0
lasti = 0

for i, sentence in enumerate(doc.sentences):
    sent = sentence.sentiment
    print("%d|%d" % (i, sent))
    sum += sent
    lasti = i + 1

score = sum / lasti
print(score)

if (lasti == 1): 
    if (score == 0): print("Triste")
    elif (score == 1): print("Neutro")
    elif (score == 2): print("Feliz")

elif (score < 0.40): print("Muito Triste")
elif (score < 0.90): print("Triste")
elif (score < 1.10): print("Neutro")
elif (score < 1.60): print("Feliz")
elif (score < 2): print("Muito Feliz")


