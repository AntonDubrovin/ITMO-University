f = open('rosalind_ba9h.txt', 'r')
str = f.readline().rstrip()
patterns = []
for line in f.readlines():
    patterns.append(line.rstrip())

ans = []
for pattern in patterns:
    ind_start = -1
    ind_end = -1
    for i in range(len(str) - len(pattern) + 1):
        flag = True
        if str[i] == pattern[0]:
            ind_start = i
            for j in range(len(pattern)):
                if str[i + j] != pattern[j]:
                    flag = False
            if flag:
                ans.append(ind_start)
ans.sort()
print(*ans)
