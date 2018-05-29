import java.util.*;

class VariableLengthQuantity {
    final long highFlag = 0x80;

    List<String> encode(List<Long> numbers) {
        List<String> result = new ArrayList<>();
        for(Long num : numbers) {
            List<Long> vlqDigits = new ArrayList<>();
            do {
                long digit = num & 0x7f;
                num >>= 7;
                if (!vlqDigits.isEmpty()) {
                    digit = digit | highFlag;
                }
                vlqDigits.add(0, digit);
            } while (num > 0);
            for (long digit : vlqDigits) {
                result.add(String.format("0x%X", digit).toLowerCase());
            }
        }
        return result;
    }

    List<String> decode(List<Long> bytes) {
        List<String> result = new ArrayList<>();
        long current = 0;
        for(long b : bytes) {
            current = (current << 7) | (b & 0x7f);
            if ((b & 0x80) == 0) {
                result.add(String.format("0x%X", current).toLowerCase());
                current = 0;
            }
        }
        if (current != 0 || (bytes.get(bytes.size() - 1) & 0x80) != 0) {
            throw new IllegalArgumentException("Invalid variable-length quantity encoding");
        }
        return result;
    }
}
