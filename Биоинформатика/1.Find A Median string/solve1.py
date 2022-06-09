import sys
from itertools import product
from nltk import ngrams


def hamming_distance(a, b):
    dist = 0
    for i in range(len(a)):
        if a[i] != b[i]:
            dist += 1
    return dist


f = open('rosalind_ba2b.txt', 'r')
k = int(f.readline())
alphabet = [''.join(i) for i in product('ACGT', repeat=k)]
dnas = []
for line in f.readlines():
    dnas.append(line.rstrip())

minD = sys.maxsize
ans = ""
for curAns in alphabet:
    s = 0
    for dna in dnas:
        tokens = [token for token in dna]
        kmers = list(ngrams(tokens, k))
        minKmer = sys.maxsize
        for kmer in kmers:
            hamming = hamming_distance(curAns, kmer)
            if hamming < minKmer:
                minKmer = hamming
        s += minKmer
    if s < minD:
        minD = s
        ans = curAns

print(ans)
