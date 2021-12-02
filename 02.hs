import Text.Read (readMaybe)
import Debug.Trace (traceShow)

main :: IO ()
main = do
  input <- readFile "input.txt"
  print $ do
    (x, y) <- fmap solve (parse input)
    return (x * y)

data Direction = Up Int | Down Int | Forward Int deriving Show

data Aim = Aim Int deriving Show

parse :: String -> Maybe [Direction]
parse string = sequence $ parseLine <$> lines string

parseLine :: String -> Maybe (Direction)
parseLine input = case words input of
  ("down" : (num : [])) -> Down <$> readMaybe num
  ("forward" : (num: [])) -> Forward <$> readMaybe num
  ("up" : (num: [])) -> Up <$> readMaybe num
  _ -> Nothing

solve :: [Direction] -> (Int, Int)
solve xs = let (_, x, y) = foldr addDirection ((Aim 0), 0, 0) (reverse xs) in (x, y)
  where
    addDirection :: Direction -> (Aim, Int, Int) -> (Aim, Int, Int)
    addDirection (Up x) ((Aim a), horz, vert) = ((Aim (a - x)), horz, vert)
    addDirection (Down x) ((Aim a), horz, vert) = ((Aim (a + x)), horz, vert)
    addDirection (Forward x) ((Aim a), horz, vert) = ((Aim a), horz + x, vert + (a * x))
