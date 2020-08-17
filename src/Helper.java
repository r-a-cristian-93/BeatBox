//convert string to number safely

public class Helper {

    public static int toNumber(int oldNumber, String inputString) {
        int outNumber;
        try {
            outNumber = Integer.parseInt(inputString);
        } catch (NumberFormatException e) {
            outNumber = oldNumber;
        }
        return outNumber;
    }
}
