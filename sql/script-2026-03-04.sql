UPDATE reservation
SET idhotel = FLOOR(RANDOM() * 3 + 2)::int
WHERE idhotel = 1;