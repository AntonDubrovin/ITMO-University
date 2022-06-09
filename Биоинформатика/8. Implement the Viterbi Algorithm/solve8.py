def read_transition():
    ans = {}
    for i in range(len(states)):
        line = f.readline().split()
        for j in range(1, len(line)):
            ans[(states[i], states[j - 1])] = float(line[j])
    return ans


def read_emission():
    ans = {}
    for i in range(len(states)):
        line = f.readline().split()
        for j in range(1, len(line)):
            ans[(states[i], alphabet[j - 1])] = float(line[j])
    return ans


f = open('rosalind_ba10c.txt', 'r')
string_x = f.readline().rstrip()
f.readline()
alphabet = f.readline().split()
f.readline()
states = f.readline().split()
f.readline()
f.readline()
transition_matrix = read_transition()

f.readline()
f.readline()
emission_matrix = read_emission()

probabilities = {state: 1 / len(states) for state in states}

v = {}
nexts = {}
for state in states:
    nexts[(0, state)] = ''
    v[(0, state)] = emission_matrix[state, string_x[0]] * probabilities[state]

for i in range(1, len(string_x)):
    for state in states:
        next = [(v[i - 1, si] * transition_matrix[si, state], si) for si in states]
        next_max = max(next, key=lambda x: x[0])
        v[i, state] = next_max[0] * emission_matrix[state, string_x[i]]
        nexts[i, state] = next_max[1]

end_chars = []
for state in states:
    end_chars.append((v[len(string_x) - 1, state], state))
end_char = max(end_chars, key=lambda x: x[0])

i = len(string_x) - 1
cur_char = end_char[1]
ans = []
while i >= 0:
    ans.append(cur_char)
    cur_char = nexts[i, cur_char]
    i -= 1
print(*reversed(ans), sep='')
