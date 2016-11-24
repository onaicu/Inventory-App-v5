package mobi.storedot.inventory;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import mobi.storedot.inventory.data.ProductContract.ProductEntry;

import static mobi.storedot.inventory.R.id.quantity;
import static mobi.storedot.inventory.R.id.supplier;

/**
 * {@link ProductCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of product data as its data source. This adapter knows
 * how to create list items for each row of pet data in the {@link Cursor}.
 */
public class ProductCursorAdapter extends CursorAdapter  {

    /**
     * Constructs a new {@link ProductCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */


    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the product data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current product can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView quantityTextView = (TextView) view.findViewById(quantity);
        TextView supplierTextView = (TextView) view.findViewById(supplier);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        Button soldButton = (Button) view.findViewById(R.id.sell_button);
        final TextView saleTextView = (TextView) view.findViewById(R.id.sale);

        // Find the columns of product attributes that we're interested in
        final int rowId = cursor.getInt(cursor.getColumnIndexOrThrow(ProductEntry._ID));
        int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        final int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
        int supplierColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER);
        int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        int soldColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_SOLD);

        // Read the product attributes from the Cursor for the current product
        String productName = cursor.getString(nameColumnIndex);

        // If the product name is empty string or null, then use some default text
        // that says "Unknown name", so the TextView isn't blank.
        if (TextUtils.isEmpty(productName)){
            productName = context.getString(R.string.unknown_product);
        }

        final int productQuantity = cursor.getInt(quantityColumnIndex);

        /* if quantity is less or 0 set color accordingly*/
        if (productQuantity == 0) {
            view.setBackgroundColor(context.getResources().getColor(R.color.quantity_zero));
        } else if (productQuantity <= 5) {
            view.setBackgroundColor(context.getResources().getColor(R.color.quantity_below_five));
        } else
            view.setBackgroundColor(context.getResources().getColor(R.color.background));


        int productSupplier = cursor.getInt(supplierColumnIndex);
        int productPrice = cursor.getInt(priceColumnIndex);
        final int soldColumn = cursor.getInt(soldColumnIndex);

        // Update the TextViews with the attributes for the current product
        nameTextView.setText(productName);
        quantityTextView.setText("Quantity: " + productQuantity);
        supplierTextView.setText("Supplier: " + productSupplier);
        priceTextView.setText("Price:       " + productPrice + " CHF");
        saleTextView.setText("Sold:        " + soldColumn);

        switch(productSupplier){
            case 0:
                supplierTextView.setText(R.string.supplier_unknown);
                break;
            case 1: supplierTextView.setText(R.string.supplier_us);
                break;
            case 2: supplierTextView.setText(R.string.supplier_china);
                break;
        }

        // Set OnClickListener to track when the user has click the Sell button and display a toast message.
        soldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                if (productQuantity > 0) {
                    values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, productQuantity - 1);
                    values.put(ProductEntry.COLUMN_SOLD, soldColumn + 1);
                    Uri mCurrentQuantityUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, rowId);
                    int rowsAffected = context.getContentResolver().update(mCurrentQuantityUri, values, null, null);
                    if (rowsAffected == 0) {
                        Toast.makeText(context, context.getString(R.string.sale_failed), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, context.getString(R.string.sale_success), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, context.getString(R.string.out_of_stock), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}