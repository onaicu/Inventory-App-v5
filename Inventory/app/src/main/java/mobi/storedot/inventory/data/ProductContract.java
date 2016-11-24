package mobi.storedot.inventory.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * API Contract for the Inventory / Products app.
 */

public final class ProductContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.

    private ProductContract() {}

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "mobi.storedot.inventory";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://mobi.storedot.inventory/products/ is a valid path for
     * looking at product data. content://mobi.storedot.inventory/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_PRODUCTS = "prodcuts";

    /**
     * Inner class that defines constant values for the products database table.
     * Each entry in the table represents a single product.
     */
    public static final class ProductEntry implements BaseColumns {

        /** The content URI to access the pet data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCTS);
        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of products.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single product.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        /** Name of database table for inventory/product */
        public final static String TABLE_NAME = "products";

        /**
         * Unique ID number for the product (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the product.
         *
         * Type: TEXT
         */
        public final static String COLUMN_PRODUCT_NAME ="name";

        /**
         * Current Quantity of the product.
         * Type: Integer
         */
        public final static String COLUMN_PRODUCT_QUANTITY = "current_quantity";

        /**
         * Supplier of the product
         *
         * The only possible values are {@link #SUPPLIER_UNKNOWN}, {@link #SUPPLIER_USA},
         * or {@link #SUPPLIER_CHINA}.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_PRODUCT_SUPPLIER = "supplier";

        /**
         * Unit Price for the product.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_PRODUCT_PRICE = "price";

        /**
         * Uri image for the product.
         */
        public final static String COLUMN_ITEM_IMAGE = "image";

        /**
         * sold units for the product
         */
        public final static String COLUMN_SOLD = "soldButton";

        /**
         * Possible values for the supplier of the product
         */
        public static final int SUPPLIER_UNKNOWN = 0;
        public static final int SUPPLIER_USA = 1;
        public static final int SUPPLIER_CHINA = 2;

        /**Returns whether or not the given supplier is {@link #SUPPLIER_UNKNOWN}, {@link #SUPPLIER_USA},
         * or {@link #SUPPLIER_CHINA}.*/

        public static boolean isValidSupplier(int supplier) {
            if (supplier == SUPPLIER_UNKNOWN || supplier == SUPPLIER_USA || supplier == SUPPLIER_CHINA) {
                return true;
            }
            return false;
        }
    }
}
