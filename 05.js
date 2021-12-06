const fs = require('fs');

const data = fs.readFileSync('input.txt', 'utf8');

const parsed = data
    .split('\n')
    .filter(line => line.length > 0)
    .map(line => line.split("->"))
    .map(coords => coords
        .map(coord => coord.split(","))
        .map(([x, y]) => [x.trim(), y.trim()])
        .map(([x, y]) => [parseInt(x), parseInt(y)])
    )
    .map(([[xi, yi], [xf, yf]]) => {
        let incrX
        let incrY
        let length
        if (xi === xf) {
            incrX = 0
        } else {
            incrX = xi > xf ? -1 : 1
        }
        if (yi === yf) {
            incrY = 0
            length = Math.abs(xi - xf) + 1
        } else {
            incrY = yi > yf ? -1 : 1
            length = Math.abs(yi - yf) + 1
        }
        return Array.from({length}, (_, i) => [xi + i * incrX, yi + i * incrY]);
    })
    .reduce((acc, x) => {
        for (const coord of x) {
            if (coord in acc) {
                acc[coord] += 1
            } else {
                acc[coord] = 1
            }
        }
        return acc
    }, {})

console.log(Object.values(parsed).filter(v => v > 1).length);