def algo(x):
    path.append(x)
    ifFound = False
    for node in graph[x]:
        edge = (x, node)
        if edge in edges_graph:
            edges_graph.remove(edge)
            nodes = set()
            for eedge in edges_graph:
                nodes.add(eedge[0])
                nodes.add(eedge[1])
            visited = set()
            dfs(visited, x)
            if nodes == visited:
                ifFound = True
                algo(node)
            else:
                edges_graph.add(edge)
    if not ifFound:
        for node in graph[x]:
            edge = (x, node)
            if edge in edges_graph:
                edges_graph.remove(edge)
                algo(node)


def dfs(visited, x):
    visited.add(x)
    for node in graph[x]:
        edge = (x, node)
        if edge in edges_graph and node not in visited:
            dfs(visited, node)


f = open('rosalind_ba3j.txt', 'r')
k, d = map(int, f.readline().split())
graph = dict()
in_d = dict()
out_d = dict()
path = []
for line in f.readlines():
    pattern = line.rstrip().split('|')
    pattern1 = (pattern[0][:-1], pattern[1][:-1])
    pattern2 = (pattern[0][1:], pattern[1][1:])
    graph[pattern1] = [pattern2]
    in_d[pattern1] = 0
    out_d[pattern1] = 0
    for i in graph[pattern1]:
        if i not in graph:
            graph[i] = []
for i in graph:
    for node in graph[i]:
        if node in in_d:
            in_d[node] += 1
    out_d[i] = len(graph[i])

start = ()
for i in graph:
    if i in in_d and i in out_d:
        if out_d[i] - 1 == in_d[i]:
            start = i
            break

edges_graph = set()
for i in graph:
    for node in graph[i]:
        if (i, node) not in edges_graph:
            edges_graph.add((i, node))
algo(start)

ans1 = path[0][0][:k - 2]
ans2 = path[0][1][:k - 2]
count = 0
for i in path:
    count += 1
    ans1 += i[0][k - 2:]
    ans2 += i[1][k - 2:]
print(ans1 + ans2[len(ans1) - (d + k):])
