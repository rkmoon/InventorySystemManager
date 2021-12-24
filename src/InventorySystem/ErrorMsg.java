package InventorySystem;

/**
 * @author Ryan Moon
 */
public class ErrorMsg {
    private static final String minMax = "Min is Greater than Max";
    private static final String invHigh = "Inv is greater than Max";
    private static final String invLow = "Inv is Less than Min";
    private static final String invInvalid = "Inv is not a Valid Number";
    private static final String priceInvalid = "Price/Cost is not a Valid Number";
    private static final String maxInvalid = "Max is not a Valid Number";
    private static final String minInvalid = "Min is not a Valid Number";
    private static final String machineIDInvalid = "Machine ID is not a Valid Number";
    private static final String compNameInvalid = "Company Name is Invalid";
    private static final String noPartSelected = "No Part has been Selected";
    private static final String noProductSelected = "No Product has been Selected";
    private static final String productHasParts = "This Product has Associated Parts";
    private static final String confirmDelete = "Are you sure you would like to delete ";


    /**
     * @return inventory invalid string
     */
    public static String getInvInvalid() {
        return invInvalid;
    }

    /**
     * @return price invalid string
     */
    public static String getPriceInvalid() {
        return priceInvalid;
    }

    /**
     * @return max invalid string
     */
    public static String getMaxInvalid() {
        return maxInvalid;
    }

    /**
     * @return min invalid string
     */
    public static String getMinInvalid() {
        return minInvalid;
    }

    /**
     * @return machine ID invalid string
     */
    public static String getMachineIDInvalid() {
        return machineIDInvalid;
    }

    /**
     * @return company name invalid string
     */
    public static String getCompNameInvalid() {
        return compNameInvalid;
    }

    /**
     * @return min greater than max invalid string
     */
    public static String getMinMax() {
        return minMax;
    }

    /**
     * @return inv greater than max invalid string
     */
    public static String getInvHigh() {
        return invHigh;
    }

    /**
     * @return inv less than min invalid string
     */
    public static String getInvLow() {
        return invLow;
    }

    /**
     * @return no part selected string
     */
    public static String getNoPartSelected() {
        return noPartSelected;
    }

    /**
     * @return product has parts string
     */
    public static String getProductHasParts() {
        return productHasParts;
    }

    /**
     * @return confirm deletion string
     */
    public static String getConfirmDelete() {
        return confirmDelete;
    }

    /**
     * @return no product selected string
     */
    public static String getNoProductSelected() {
        return noProductSelected;
    }


}
