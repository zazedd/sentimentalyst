import stanza

nlp = stanza.Pipeline('en', verbose=False, model_dir="./stanza")

inpt = ''
while True:
    try:
        line = input()
        inpt += line
    except EOFError:
        break

doc = nlp(inpt)

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
    if (score == 0): print("Sad")
    elif (score == 1): print("Neutral")
    elif (score == 2): print("Happy")

elif (score < 0.40): print("Very Sad")
elif (score < 0.90): print("Sad")
elif (score < 1.10): print("Neutral")
elif (score < 1.60): print("Happy")
elif (score < 2): print("Very Happy")


