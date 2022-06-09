import random

symbols = {'A': 0, 'C': 1, 'G': 2, 'T': 3}


def hamming_distance(a, b):
    dist = 0
    for i in range(len(a)):
        if a[i] != b[i]:
            dist += 1
    return dist


def profile_prob(text, k, profile):
    maxP = 0
    kmer = text[0:k]
    for i in range(0, len(text) - k + 1):
        prob = 1
        pattern = text[i:i + k]
        for j in range(k):
            symb = symbols.get(pattern[j])
            prob *= profile[symb][j]
        if prob > maxP:
            maxP = prob
            kmer = pattern
    return kmer


def create_profile(motifs):
    l = len(motifs[0])
    profile = [[1 for _ in range(l)] for _ in range(4)]
    for motif in motifs:
        for i in range(len(motif)):
            symb = symbols.get(motif[i])
            profile[symb][i] += 1
    for pr in profile:
        for i in range(len(pr)):
            pr[i] /= len(motifs)

    return profile


def randomized_motif_search(dna, k, t):
    motifs = []
    for i in range(t):
        random.seed()
        index = random.randint(0, len(dna[i]) - k)
        motifs.append(dna[i][index:index + k])
    best_motifs = motifs.copy()
    count = 0
    while True:
        profile = create_profile(motifs)
        for i in range(t):
            motifs[i] = profile_prob(dna[i], k, profile)
        if score(motifs) < score(best_motifs):
            best_motifs = motifs.copy()
            count += 1
        else:
            print(count)
            return best_motifs


def consensus(profile):
    str = ""
    for i in range(len(profile[0])):
        max = 0
        loc = 0
        for j in range(4):
            if profile[j][i] > max:
                loc = j
                max = profile[j][i]
        str += {0: 'A', 1: 'C', 2: 'G', 3: 'T'}.get(loc)
    return str


def score(motifs):
    profile = create_profile(motifs)
    cons = consensus(profile)
    score = 0
    for motif in motifs:
        for i in range(len(motif)):
            if cons[i] != motif[i]:
                score += 1
    return score


f = open('rosalind_ba2f.txt', 'r')
k, t = map(int, f.readline().split())
dnas = []
for line in f.readlines():
    dnas.append(line.rstrip())

best_motifs = randomized_motif_search(dnas, k, t)
for i in range(1000):
    motifs = randomized_motif_search(dnas, k, t)
    if score(motifs) < score(best_motifs):
        best_motifs = motifs

for motif in best_motifs:
    print(motif)
