import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;
import java.util.regex.*;

public class GrepTool {
    public GrepTool() {

    }

    class GrepMatch {
        private String filename;
        private int lineNo;
        private String text;
        public GrepMatch(String filename, int lineNo, String text) {
            this.filename = filename;
            this.lineNo = lineNo;
            this.text = text;
        }

        public String toString(GrepOptions options) {
            if (options.printFileNames) {
                return this.filename;
            }
            StringBuilder results = new StringBuilder();
            if (options.multifile) {
                results.append(this.filename + ":");
            }
            if (options.printLineNumbers) {
                results.append(this.lineNo + ":");
            }
            results.append(this.text);
            return results.toString();
        }
    }

    class GrepOptions {
        public boolean printLineNumbers;
        public boolean printFileNames;
        public boolean ignoreCase;
        public boolean exactMatch;
        public boolean invert;
        public boolean multifile = false;
        public GrepOptions(List<String> flags) {
            this.printLineNumbers = flags.contains("-n");
            this.ignoreCase = flags.contains("-i");
            this.printFileNames = flags.contains("-l");
            this.exactMatch = flags.contains("-x");
            this.invert = flags.contains("-v");
        }
        public GrepOptions(List<String> flags, boolean multifile) {
            this(flags);
            this.multifile = multifile;
        }
        public Pattern getPattern(String needle) {
            int searchFlags = 0;
            if (this.ignoreCase) {
                searchFlags |= Pattern.CASE_INSENSITIVE;                
            }
            if (this.exactMatch) {
                needle = "^" + needle + "$";
            } else {
                needle = ".*" + needle + ".*";
            }
            return Pattern.compile(needle, searchFlags);
        }
    }

    String grep(String needle, List<String> flags, List<String> files) {
        List<GrepMatch> results = new ArrayList<>();
        GrepOptions options = new GrepOptions(flags, files.size() > 1);
        Pattern pattern = options.getPattern(needle);
        for (String filename : files) {
            List<String> lines = new ArrayList<>();;
            lines.add("");
            try (Stream<String> stream = Files.lines(Paths.get(filename))) {
                lines.addAll(stream.collect(Collectors.toList()));
            } catch (IOException ex) {
                return null;
            }
            ListIterator<String> iter = lines.listIterator();
            iter.next(); // Drop blank line used for incrementing line number
            while (iter.hasNext()) {
                int lineNo = iter.nextIndex();
                String text = iter.next();
                if (pattern.matcher(text).matches() != options.invert) {
                    results.add(new GrepMatch(filename, lineNo, text));
                }
            }
        }
        return results.stream()
            .map(m -> m.toString(options))
            .distinct()
            .collect(Collectors.joining("\n"));
    }
}