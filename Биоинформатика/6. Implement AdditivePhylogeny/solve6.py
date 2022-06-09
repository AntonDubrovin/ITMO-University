def symmetrical(i, j, val):
    ans[i][j] = val
    ans[j][i] = val


def addNode(ind, limb_l, j, i, x):
    dist = [-1 for _ in range(len(ans))]
    dist[j] = 0
    parents = [-1 for _ in range(len(ans))]
    q = [j]
    while len(q) != 0:
        cur = q[0]
        q = q[1:]
        for node, w in ans[cur].items():
            if dist[node] == -1:
                dist[node] = dist[cur] + w
                parents[node] = cur
                q.append(node)
                if i == node:
                    prev = node
                    while x < dist[prev]:
                        cur = prev
                        prev = parents[cur]
                    if x == dist[prev]:
                        symmetrical(prev, ind, limb_l)
                    else:
                        ans.append(dict())
                        new = len(ans) - 1
                        symmetrical(ind, new, limb_l)
                        ans[prev].pop(cur)
                        ans[cur].pop(prev)
                        symmetrical(prev, new, x - dist[prev])
                        symmetrical(cur, new, dist[cur] - x)
                    return


def limb_length(ind):
    ans = []
    j = ind
    if ind > 0:
        j -= 1
    else:
        j += 1
    for i in range(j + 1):
        if i != ind and j != ind:
            ans.append(((matrix[j][ind] + matrix[ind][i] - matrix[j][i]) // 2, i, j))
    return min(ans, key=lambda x: x[0])


f = open('rosalind_ba7c.txt', 'r')
n = int(f.readline())
matrix = []
for _ in range(n):
    matrix.append(list(map(int, f.readline().rstrip().split())))

ans = [{} for _ in range(n)]
ans[0][1] = matrix[0][1]
ans[1][0] = matrix[1][0]

for ind in range(2, n):
    limb_l, i, j = limb_length(ind)
    x = matrix[i][ind] - limb_l
    addNode(ind, limb_l, i, j, x)

for i, dicts in enumerate(ans):
    for dict, w in dicts.items():
        print(f'{i}->{dict}:{w}')
