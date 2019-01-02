import java.util.Arrays;
import java.util.function.*;
import java.util.Map;
import java.util.stream.*;
import java.util.stream.Collectors.*;

class Yacht {
    int[] dice;
    YachtCategory category;
    Yacht(int[] dice, YachtCategory yachtCategory) {
        this.dice = dice;
        this.category = yachtCategory;
    }

    int score() {
        Map<Integer, Long> grouped = Arrays.stream(dice)
            .boxed()
            .collect(Collectors.groupingBy(
                Function.identity(),
                Collectors.counting()
            ));
        String counts = grouped.entrySet()
            .stream()
            .map(Map.Entry::getValue)
            .sorted()
            .map(d -> d.toString())
            .collect(Collectors.joining(","));
        int straightStart = category.getValue() - 8;
        int straightStop = straightStart + 5;
        boolean isStraight = Arrays.stream(dice)
            .sorted()
            .boxed()
            .collect(Collectors.toList())
            .equals(
                IntStream.range(straightStart, straightStop)
                .boxed()
                .collect(Collectors.toList())
            );
        switch(category)
        {
            case FULL_HOUSE:
                if (counts.equals("2,3")) return Arrays.stream(dice).sum();
                break;
            case FOUR_OF_A_KIND:
                switch(counts)
                {
                    case "1,4":
                    case "5":
                        return 4 * grouped.entrySet()
                            .stream()
                            .filter(e -> e.getValue() > 1)
                            .findFirst()
                            .get()
                            .getKey();
                }
                break;
            case LITTLE_STRAIGHT:
            case BIG_STRAIGHT:
                if (isStraight) return 30;
                break;
            case CHOICE:
                return Arrays.stream(dice).sum();
            case YACHT:
                if (Arrays.stream(dice).distinct().count() == 1) return 50;
                break;
            default:
                return Arrays.stream(dice).filter(i -> i == category.getValue()).sum();
        }
        return 0;
    }

}
