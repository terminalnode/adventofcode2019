module Day01 where

main :: IO ()
main = do
    input <- readFile $ "input"
    let masses = map read . lines $ input :: [Integer]
        solution1 = sum . map partOneMap $ masses
        solution2 = sum . map partTwoMap $ masses
    putStrLn $ "Part 1: " ++ show solution1
    putStrLn $ "Part 2: " ++ show solution2

partOneMap :: Integer -> Integer
partOneMap = subtract 2 . flip div 3

partTwoMap :: Integer -> Integer
partTwoMap = go 0
  where
    go acc mass
      | newMass <= 0 = acc
      | otherwise = go (acc + newMass) newMass
      where newMass = partOneMap mass
