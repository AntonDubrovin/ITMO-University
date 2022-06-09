def num_start(x):
    if x >= 0:
        return x * 2
    return -x * 2 - 1


def num_end(x):
    if x >= 0:
        return x * 2 - 1
    return -x * 2


def create_graph(genome):
    graph = {0: num_end(genome[0]), num_end(genome[0]): 0}
    for i in range(len(genome)):
        if i == len(genome) - 1:
            graph[-1] = num_start(genome[i])
            graph[num_start(genome[i])] = -1
        else:
            graph[num_start(genome[i])] = num_end(genome[i + 1])
            graph[num_end(genome[i + 1])] = num_start(genome[i])
    return graph


def create_cycles():
    cycles = []
    num_graph = 1
    visited = [0 for _ in range(len(graph1))]
    for i in graph1:
        if visited[i] == 0:
            tmp = []
            while visited[i] == 0:
                if num_graph == 1:
                    new_ind = graph1[i]
                else:
                    new_ind = graph2[i]
                num_graph = (num_graph + 1) % 2
                tmp.append([i, new_ind])
                visited[i] = 1
                i = new_ind
            cycles.append(tmp)
            num_graph = 1
    return cycles


def check():
    for cycle in cycles:
        if len(cycle) > 2:
            return True
    return False


def back(n):
    if n % 2 == 1:
        return (n + 1) // 2
    else:
        return -n // 2


def ans():
    genome = [[]]
    visited = [0 for _ in range(len(graph1))]
    i = 0
    visited[-1] = 1
    while sum(len(g) for g in genome) < len(graph1) // 2 - 1:
        if visited[i] == 1:
            genome.append([])
            for j in range(len(graph1) - 1):
                if visited[j] == 0:
                    i = j
                    break
        visited[i] = 1
        visited[graph1[i]] = 1
        if graph1[i] != -1 and graph1[i] != len(graph1) - 1:
            if graph1[i] % 2 == 1:
                genome[-1].append((graph1[i] + 1) // 2)
            else:
                genome[-1].append(-graph1[i] // 2)
            i = num_start(genome[-1][-1])
    return genome


f = open('rosalind_ba6d.txt', 'r')
genome1 = list(map(int, f.readline().rstrip()[1:-1].split(' ')))
genome2 = list(map(int, f.readline().rstrip()[1:-1].split(' ')))
graph1 = create_graph(genome1)
graph2 = create_graph(genome2)
cycles = create_cycles()
path = [[genome1]]
while check():
    tmp_idx = -1
    tmp = []
    for (idx, cycle) in enumerate(cycles):
        if len(cycle) > 3:
            tmp.append([[cycle[2][0], cycle[0][1]], cycle[1]])
            tmp.append([[cycle[0][0], cycle[2][1]]] + cycle[3:])
            tmp_idx = idx
            break
    for (idx, cycle) in enumerate(cycles):
        if idx != tmp_idx:
            tmp.append(cycle)
    graph1 = {}
    cycles = tmp
    for cycle in cycles:
        num_graph = 1
        for edge in cycle:
            if num_graph == 1:
                graph1[edge[0]] = edge[1]
                graph1[edge[1]] = edge[0]
            num_graph = (num_graph + 1) % 2
    path.append(ans())

for ans in path:
    for number in ans:
        printed_ans = '('
        for n in number:
            if n > 0:
                printed_ans += f'+{n} '
            else:
                printed_ans += f'{n} '
        printed_ans = printed_ans[:-1] + ')'
        print(printed_ans, end='')
    print()
