enum YachtCategory {

    YACHT(0),
    ONES(1),
    TWOS(2),
    THREES(3),
    FOURS(4),
    FIVES(5),
    SIXES(6),
    FULL_HOUSE(7),
    FOUR_OF_A_KIND(8),
    LITTLE_STRAIGHT(9),
    BIG_STRAIGHT(10),
    CHOICE(11);

    private final int value;
    private YachtCategory(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
