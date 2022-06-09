f = open('rosalind_ba5j.txt', 'r')
first_line = f.readline().rstrip()
second_line = f.readline().rstrip()
f_blosum = open('blosum62.txt', 'r')
proteins = f_blosum.readline().rstrip().split()
score_alignments_blosum = []
for _ in range(len(proteins)):
    cur_line = f_blosum.readline().split()
    score_alignments_blosum.append(list(map(int, cur_line[1:])))

epsilon = 1
sigma = 11

dynamic = [[[0, ''] for _ in range(len(second_line) + 1)] for _ in range(len(first_line) + 1)]
gap_right = [[[epsilon - sigma, ''] for _ in range(len(second_line) + 1)] for _ in range(len(first_line) + 1)]
gap_down = [[[epsilon - sigma, ''] for _ in range(len(second_line) + 1)] for _ in range(len(first_line) + 1)]

for i in range(1, len(first_line) + 1):
    for j in range(1, len(second_line) + 1):
        gap_right[i][j][0] = max(gap_right[i][j - 1][0] - epsilon, dynamic[i][j - 1][0] - sigma)
        if gap_right[i][j][0] == gap_right[i][j - 1][0] - epsilon:
            gap_right[i][j][1] = 'right'
        else:
            gap_right[i][j][1] = 'right-dynamic'

        gap_down[i][j][0] = max(gap_down[i - 1][j][0] - epsilon, dynamic[i - 1][j][0] - sigma)
        if gap_down[i][j][0] == gap_down[i - 1][j][0] - epsilon:
            gap_down[i][j][1] = 'down'
        else:
            gap_down[i][j][1] = 'down-dynamic'

        cur_score = score_alignments_blosum[proteins.index(first_line[i - 1])][proteins.index(second_line[j - 1])]
        dynamic[i][j][0] = max(dynamic[i - 1][j - 1][0] + cur_score,
                               gap_down[i][j][0], gap_right[i][j][0])
        if dynamic[i][j][0] == gap_down[i][j][0]:
            dynamic[i][j][1] = gap_down[i][j][1]
        elif dynamic[i][j][0] == gap_right[i][j][0]:
            dynamic[i][j][1] = gap_right[i][j][1]
        else:
            dynamic[i][j][1] = 'right-down'

first = len(first_line)
second = len(second_line)
ans1 = ''
ans2 = ''
directions = ''
while first != 0 and second != 0:
    if dynamic[first][second][1] == 'right-down':
        ans1 += first_line[first - 1]
        ans2 += second_line[second - 1]
        first -= 1
        second -= 1
    elif dynamic[first][second][1] == 'down-dynamic':
        ans1 += first_line[first - 1]
        ans2 += '-'
        first -= 1
    elif dynamic[first][second][1] == 'right-dynamic':
        ans1 += '-'
        ans2 += second_line[second - 1]
        second -= 1
    elif dynamic[first][second][1] == 'down':
        ans1 += first_line[first - 1]
        ans2 += '-'
        first -= 1
        while gap_down[first][second][1] == 'down':
            ans1 += first_line[first - 1]
            ans2 += '-'
            first -= 1
    elif dynamic[first][second][1] == 'right':
        ans1 += '-'
        ans2 += second_line[second - 1]
        second -= 1
        while gap_right[first][second][1] == 'right':
            ans1 += '-'
            ans2 += second_line[second - 1]
            second -= 1
    else:
        continue

print(dynamic[-1][-1][0])
print(ans1[::-1])
print(ans2[::-1])
