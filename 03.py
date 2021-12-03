with open("03test.txt", 'r') as f:
    lines = [line.rstrip() for line in f.readlines()]
    gamma_rate = []
    for i in range(len(lines[0])):
        count = {
            '0': 0,
            '1': 1,
        }
        for line in lines:
            count[line[i]] += 1

        gamma_rate.append(max(count.items(), key=lambda pair: pair[1])[0])
    # print("p1:", int("".join([str((int(not(int(i))))) for i in gamma_rate]), 2) * int("".join(gamma_rate), 2))

def getColumn(arr2d, n):
    return [x[n] for x in arr2d]

def most_common(arr1d):
    return 0 if len([i for i in arr1d if int(i) == 0]) > len([i for i in arr1d if int(i) == 1]) else 1

def least_common(arr1d):
    return 0 if len([i for i in arr1d if int(i) == 0]) <= len([i for i in arr1d if int(i) == 1]) else 1


with open("03test.txt", 'r') as f:
    lines = [line.rstrip() for line in f.readlines()]
    for x in range(len(lines[0])):
        most_common_res = most_common(getColumn(lines, x))
        lines = [line for line in lines if most_common_res == int(line[x])]
        if len(lines) == 1:
            break
    oxygen = int(lines[0], 2)
with open("03test.txt", 'r') as f:
    lines = [line.rstrip() for line in f.readlines()]
    for x in range(len(lines[0])):
        least_common_res = least_common(getColumn(lines, x))
        lines = [line for line in lines if least_common_res == int(line[x])]
        if len(lines) == 1:
            break
    scrubber = int(lines[0], 2)
print(scrubber * oxygen)