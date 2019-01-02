import java.util.*;
import java.util.EmptyStackException;
import java.util.function.Supplier;

enum Nationality {
    Norwegian, Japanese, Englishman, Ukrainian, Spaniard;
    public static final Nationality[] toNationality = Nationality.values();
}
enum Drink {
    Water, OrangeJuice, Tea, Milk, Coffee;
    public static final Drink[] toDrink = Drink.values();
}
enum Pet {
    Zebra, Dog, Snails, Horse, Fox;
    public static final Pet[] toPet = Pet.values();
}
enum Smoke {
    OldGold, Parliaments, Kools, LuckyStrike, Chesterfields;
    public static final Smoke[] toSmoke = Smoke.values();
}
enum Color {
    Green, Blue, Red, Ivory, Yellow;
    public static final Color[] toColor = Color.values();
}

public class ZebraPuzzle {
    private final int numHouses = 5;
    private final int numPermutations = 120; // 5!
    Map<Nationality, Integer> owners;
    Map<Color, Integer> colors;
    Map<Smoke, Integer> smokes;
    Map<Pet, Integer> pets;
    Map<Drink, Integer> drinks;

    private static void reverse(int[] nums, int left, int right){
        while(left<right){
            int temp = nums[left];
            nums[left++]=nums[right];
            nums[right--]=temp;
        }
    }

    private static void initRangeArray(int[] a) {
        for (int i=0; i < a.length; i++) a[i] = i;
    }

    private static void nextPermutation(int[] nums) {
        if(nums == null || nums.length<2) return;
     
        int p=0;            
        for(int i=nums.length-2; i>=0; i--){
            if(nums[i]<nums[i+1]){
                p=i;
                break;
            }    
        }
     
        int q = 0;
        for(int i=nums.length-1; i>p; i--){
            if(nums[i]> nums[p]){
                q=i;
                break;
            }    
        }
     
        if(p==0 && q==0){
            reverse(nums, 0, nums.length-1);
            return;
        }
     
        int temp=nums[p];
        nums[p]=nums[q];
        nums[q]=temp;
     
        if(p<nums.length-1){
            reverse(nums, p+1, nums.length-1);
        }
    }

    private static <T> Map<T, Integer> asDict(T[] a) {
        Map<T, Integer> m = new HashMap<>();
        for (int i=0; i < a.length; i++) m.put(a[i], i);
        return m;
    }

    private static boolean nextTo(int a, int b) {
        return Math.abs(a - b) == 1;
    }

    private boolean solveNationalities() {
        int[] idx = new int[numHouses];
        initRangeArray(idx);
        for (int i=0; i < numPermutations; i++) {
            this.owners = asDict(Arrays.stream(idx)
                .mapToObj(j -> Nationality.toNationality[j])
                .toArray(Nationality[]::new));
            if(
                owners.get(Nationality.Norwegian) == 0 &&
                solveColors()
            ) return true;
            this.owners = null;
            nextPermutation(idx);
        }
        return false;
    }

    private boolean solveColors() {
        int[] idx = new int[numHouses];
        initRangeArray(idx);
        for (int i=0; i < numPermutations; i++) {
            this.colors = asDict(Arrays.stream(idx)
                .mapToObj(j -> Color.toColor[j])
                .toArray(Color[]::new));
            if(
                colors.get(Color.Blue) == 1 &&
                colors.get(Color.Green) == colors.get(Color.Ivory) + 1 &&
                owners.get(Nationality.Englishman) == colors.get(Color.Red) &&
                solveSmokes()
            ) return true;
            this.colors = null;
            nextPermutation(idx);
        }
        return false;
    }

    private boolean solveSmokes() {
        int[] idx = new int[numHouses];
        initRangeArray(idx);
        for (int i=0; i < numPermutations; i++) {
            this.smokes = asDict(Arrays.stream(idx)
                .mapToObj(j -> Smoke.toSmoke[j])
                .toArray(Smoke[]::new));
            if(
                colors.get(Color.Yellow) == smokes.get(Smoke.Kools) &&
                owners.get(Nationality.Japanese) == smokes.get(Smoke.Parliaments) &&
                solvePets()
            ) return true;
            this.smokes = null;
            nextPermutation(idx);
        }
        return false;
    }

    private boolean solvePets() {
        int[] idx = new int[numHouses];
        initRangeArray(idx);
        for (int i=0; i < numPermutations; i++) {
            this.pets = asDict(Arrays.stream(idx)
                .mapToObj(j -> Pet.toPet[j])
                .toArray(Pet[]::new));
            if(
                owners.get(Nationality.Spaniard) == pets.get(Pet.Dog) &&
                smokes.get(Smoke.OldGold) == pets.get(Pet.Snails) &&
                nextTo(smokes.get(Smoke.Chesterfields), pets.get(Pet.Fox)) &&
                nextTo(smokes.get(Smoke.Kools), pets.get(Pet.Horse)) &&
                solveDrinks()
            ) return true;
            this.pets = null;
            nextPermutation(idx);
        }
        return false;
    }

    private boolean solveDrinks() {
        int[] idx = new int[numHouses];
        initRangeArray(idx);
        for (int i=0; i < numPermutations; i++) {
            this.drinks = asDict(Arrays.stream(idx)
                .mapToObj(j -> Drink.toDrink[j])
                .toArray(Drink[]::new));
            if(
                drinks.get(Drink.Milk) == 2 &&
                colors.get(Color.Green) == drinks.get(Drink.Coffee) &&
                owners.get(Nationality.Ukrainian) == drinks.get(Drink.Tea) &&
                smokes.get(Smoke.LuckyStrike) == drinks.get(Drink.OrangeJuice)
            ) return true;
            this.drinks = null;
            nextPermutation(idx);
        }
        return false;
    }

    public ZebraPuzzle() {
        solveNationalities();
    }

    public String getWaterDrinker() {
        for (Nationality owner : Nationality.values()) {
            if (owners.get(owner) == drinks.get(Drink.Water))
                return owner.name();
        }
        return null;
    }

    public String getZebraOwner() {
        for (Nationality owner : Nationality.values()) {
            if (owners.get(owner) == pets.get(Pet.Zebra))
                return owner.name();
        }
        return null;
    }
}
