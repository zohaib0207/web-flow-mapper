public class CoveragePrioritizer {

    public static int calculatePriority(String url) {

        int score = 0;

        String lower = url.toLowerCase();

        if (lower.contains("login") ||
            lower.contains("signin") ||
            lower.contains("auth")) {

            score += 5;
        }

        if (lower.contains("register")) {

            score += 4;
        }

        if (lower.contains("search")) {

            score += 3;
        }

        if (lower.contains("admin")) {

            score += 5;
        }

        if (score == 0) {

            score = 1;
        }

        return score;
    }

    public static String getPriorityReason(int priority) {

        if (priority >= 6) {

            return "High Value Authentication State";

        } else if (priority >= 4) {

            return "Likely Interactive State";

        } else {

            return "Normal Navigation";
        }
    }
}
