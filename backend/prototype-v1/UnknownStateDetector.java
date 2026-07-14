public class UnknownStateDetector {

    public static String determineReason(
            StateSchema state,
            int statusCode,
            boolean firstVisit) {

        if (!firstVisit) {
            return "Previously Visited";
        }

        if (statusCode == 401 || statusCode == 403) {
            return "Authentication Required";
        }

        if (statusCode >= 500) {
            return "Server Error";
        }

        if (state.hasLoginForm) {
            return "Login Form State";
        }

        if (state.authenticationRequired) {
            return "Protected State";
        }

        if (state.discoveredByMutation) {
            return "Mutation Discovered";
        }

        return "Previously Unknown State";
    }

    public static boolean isUnknown(boolean firstVisit) {
        return firstVisit;
    }
}
