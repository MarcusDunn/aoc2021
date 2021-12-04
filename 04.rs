use std::str::FromStr;
use std::error::Error;
use std::convert::TryInto;
use std::fmt::{Display, Formatter};

#[derive(Debug, Clone)]
struct Square {
    value: u32,
    selected: bool,
}

impl Display for Square {
    fn fmt(&self, f: &mut Formatter<'_>) -> std::fmt::Result {
        if self.selected {
            write!(f, "{}*", self.value)
        } else {
            write!(f, "{}", self.value)
        }
    }
}

impl FromStr for Square {
    type Err = Box<dyn Error>;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        Ok(Square {
            value: s.parse()?,
            selected: false,
        })
    }
}

#[derive(Debug)]
struct Game<const N: usize> {
    numbers: Vec<u32>,
    boards: Vec<Board<N>>,
}

impl<const N: usize> Display for Game<N> {
    fn fmt(&self, f: &mut Formatter<'_>) -> std::fmt::Result {
        writeln!(f, "{:?}", self.numbers)?;
        for x in &self.boards {
            writeln!(f, "{}", x)?
        }
        Ok(())
    }
}

impl<const N: usize> Game<N> {
    fn play(&mut self) -> u32 {
        loop {
            self.turn();
            self.boards.retain(|board| !board.won());
            if self.boards.len() == 1 {
                let mut i = self.turn();
                while !self.boards[0].won() {
                    i = self.turn()
                }
                return i * self.boards[0].0
                    .iter()
                    .fold(0, |acc, x| acc + x
                        .iter()
                        .filter(|sqr| !sqr.selected)
                        .fold(0, |acc, x| acc + x.value),
                    );
            }
        }
    }

    fn turn(&mut self) -> u32 {
        let num = self.numbers.remove(0);
        for b in &mut self.boards {
            b.update(num);
        }
        return num;
    }
}

impl<const N: usize> FromStr for Game<N> {
    type Err = Box<dyn Error>;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        if let Some((numbers, rest)) = s.split_once("\n") {
            Ok(Game {
                numbers: numbers
                    .split(",")
                    .filter(|i| !i.is_empty())
                    .map(|i| i.parse()).collect::<Result<_, _>>()?,
                boards: rest
                    .split("\n\n")
                    .filter(|chunk| !chunk.is_empty())
                    .map(|board| board.parse())
                    .collect::<Result<Vec<_>, _>>()?,
            })
        } else {
            Err("heck".into())
        }
    }
}

#[derive(Debug, Clone)]
struct Board<const N: usize>([[Square; N]; N]);

impl<const N: usize> Display for Board<N> {
    fn fmt(&self, f: &mut Formatter<'_>) -> std::fmt::Result {
        for x in &self.0 {
            for sqr in x {
                write!(f, "{} ", sqr)?
            }
            writeln!(f, "")?
        }
        Ok(())
    }
}

impl<const N: usize> Board<N> {
    fn update(&mut self, num: u32) {
        for i in 0..N {
            for j in 0..N {
                let Square { selected, value } = self.0[i][j];
                self.0[i][j] = Square { value, selected: selected || value == num }
            }
        }
    }

    fn won(&self) -> bool {
        self.won_col() || self.won_row()
    }

    fn won_col(&self) -> bool {
        self.0.iter().any(|col| col.iter().all(|it| it.selected))
    }

    fn won_row(&self) -> bool {
        for i in 0..N {
            if self.0.iter().all(|col| col[i].selected) {
                return true;
            }
        }
        false
    }
}

impl<const N: usize> FromStr for Board<N> {
    type Err = Box<dyn Error>;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let vec1: Vec<[Square; N]> = s
            .lines()
            .filter(|line| !line.is_empty())
            .map(|line| {
                let result: Result<[Square; N], _> = line
                    .split(" ")
                    .filter(|s| !s.is_empty())
                    .map(|i| i.parse())
                    .collect::<Result<Vec<Square>, _>>()
                    .map(|v| v.try_into().unwrap());
                result
            })
            .map(|vec| vec.unwrap())
            .collect::<Vec<_>>();
        Ok(Board(vec1.try_into().unwrap()))
    }
}

fn main() {
    let input: &str = include_str!("input-test.txt");
    let mut board: Game<5> = input.parse().unwrap();
    println!("{:?}", board.play());
}
