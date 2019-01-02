import java.util.stream.*;

public class AffineCipher {
    private final int M = 26;
    private int gcd(int x, int y) {
        while (x != 0 && y != 0) {
            if (x > y) x %= y;
            else y %= x;
        }
        return Math.max(x, y);
    }
    private void validateKeys(int a, int b) {
        if (gcd(a, M) != 1)
            throw new IllegalArgumentException("Error: keyA and alphabet size must be coprime.");
    }
    private String filterAlphaNumeric(String s) {
        return s.codePoints()
            .filter(i -> Character.isLetterOrDigit(i))
            .mapToObj(i -> String.valueOf((char)i))
            .collect(Collectors.joining(""));
    }
    private String encodeCharacter(char ch, int i, int a, int b) {
        String result;
        if (Character.isDigit(ch)) result = String.valueOf(ch);
        else {
            int x = (int)ch - 'a';
            int ex = (a * x + b) % M;
            result = String.valueOf((char)(ex + 'a'));
        }
        if (i != 0 && i % 5 == 4) return result + " ";
        return result;
    }
    public String encode(String plainText, int a, int b) {
        validateKeys(a, b);
        String filteredText = filterAlphaNumeric(plainText).toLowerCase();
        return IntStream.range(0, filteredText.length())
            .mapToObj(i -> encodeCharacter(filteredText.charAt(i), i, a, b))
            .collect(Collectors.joining())
            .trim();
    }
    private int getMMI(int x, int y) {
        int mmi;
        for(mmi=1; mmi<y && (x * mmi) % y != 1; mmi++);
        return mmi;
    }
    private String decodeCharacter(char ch, int a, int b) {
        if (Character.isDigit(ch)) return String.valueOf(ch);
        int y = (int)ch - 'a';
        int dy = (getMMI(a, M) * (y - b + 2 * M)) % M;
        return String.valueOf((char)(dy + 'a'));
    }
    public String decode(String encoded, int a, int b) {
        validateKeys(a, b);
        return filterAlphaNumeric(encoded)
            .codePoints()
            .mapToObj(i -> decodeCharacter((char)i, a, b))
            .collect(Collectors.joining());
    }
}