package com.itheamc.meatprocessing.graphics;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.itheamc.meatprocessing.R;
import com.itheamc.meatprocessing.models.external.Users;
import com.itheamc.meatprocessing.models.internal.OrderItems;
import com.itheamc.meatprocessing.repositories.LocalRepo;
import com.itheamc.meatprocessing.utilities.PaintUtility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InvoiceGenerator {
    private static final String TAG = "InvoiceGenerator";
    private Context context;
    private int pageWidth;
    private int pageHeight;
    private List<OrderItems> orderItems;
    private String orderId;
    @SuppressLint("StaticFieldLeak")
    private static InvoiceGenerator instance;

    // Constructor
    public InvoiceGenerator(Context context, int pageWidth, int pageHeight, List<OrderItems> orderItems, String orderId) {
        this.context = context;
        this.pageWidth = pageWidth;
        this.pageHeight = pageHeight;
        this.orderItems = orderItems;
        this.orderId = orderId;
    }

    // Getters for the instance of the InvoiceGenerator object
    public static InvoiceGenerator getInstance(Context context, int pageWidth, int pageHeight, List<OrderItems> orderItems, String orderId) {
        if (instance == null) {
            instance = new InvoiceGenerator(context, pageWidth, pageHeight, orderItems, orderId);
        }

        return instance;
    }

    /*
           Function to build pdf document
            */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createPdf() {

        // Retriving user info
        Users user = LocalRepo.getInstance(context).getLocalUser();
        Date date = new Date();
        DateFormat dateFormat = SimpleDateFormat.getDateInstance(DateFormat.DEFAULT, Locale.ENGLISH);
        String invoiceDate = dateFormat.format(date);
        String[] address = user.getUserAddress().split(",");



        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();

        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        //WRite your content here with the help of paint
        // Company Name and Address
        PaintUtility.drawText(canvas, 180f, 120f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.BOLD, 75f, Color.RED, "LPG Agro Farm");
        PaintUtility.drawText(canvas, 180f, 170f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.NORMAL, 20f, Color.BLACK, "Kanchhi Gaun");
        PaintUtility.drawText(canvas, 180f, 200f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.NORMAL, 20f, Color.BLACK, "Gadhawa -07, Dang, 22400");
        PaintUtility.drawText(canvas, 180f, 230f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.NORMAL, 20f, Color.BLACK, "Lumbini Province, Nepal");
        PaintUtility.drawText(canvas, 180f, 260f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.NORMAL, 20f, Color.BLACK, "☎ 082-5400008");
        PaintUtility.drawText(canvas, 180f, 290f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.NORMAL, 20f, Color.BLACK, "✉ info@lpgagrofarm.com.np");
        PaintUtility.drawText(canvas, 180f, 320f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.NORMAL, 20f, Color.BLACK, "https://www.lpgagrofarm.com.np");

        // Invoice date and invoice number
        PaintUtility.drawText(canvas, pageWidth - 350f, 260f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.BOLD, 16f, Color.RED, "Invoice Date:");
        PaintUtility.drawText(canvas, pageWidth - 350f, 290f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.BOLD, 16f, Color.RED, "Invoice No-#");
        PaintUtility.drawText(canvas, pageWidth - 350f, 320f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.BOLD, 16f, Color.RED, "Invoice Status:");

        // Invoice date and invoice number
        PaintUtility.drawText(canvas, pageWidth - 180f, 260f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.NORMAL, 16f, Color.BLACK, invoiceDate);
        PaintUtility.drawText(canvas, pageWidth - 180f, 290f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.NORMAL, 16f, Color.BLACK, "100356ORDER");
        PaintUtility.drawText(canvas, pageWidth - 180f, 320f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.BOLD_ITALIC, 16f, Color.BLACK, "PAID");

        // Invoice Text -90 degree on the left side
        PaintUtility.drawText(canvas, 150f, (pageHeight / 2f), Paint.Align.CENTER, Typeface.MONOSPACE, Typeface.ITALIC, 50f, -90f, context.getResources().getColor(R.color.light_grey), "INVOICE OF ORDER ID# "+ orderId);


//        // Upper Line
//        PaintUtility.drawCircle(canvas, pageWidth-100f, 120f, 50f);
        // Company Logo
        PaintUtility.drawLine(canvas, 180f, 350f, pageWidth - 40f, 350f, 2f, Color.RED);

        // Buyer details
        // Billed to
        PaintUtility.drawText(canvas, 180f, 400f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.BOLD, 16f, Color.RED, "Billed To");
        PaintUtility.drawText(canvas, 180f, 430f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.BOLD_ITALIC, 12f, Color.BLACK, user.getUserName());
        PaintUtility.drawText(canvas, 180f, 450f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.NORMAL, 12f, Color.BLACK, address[0].trim() + ", "+ address[1].trim());
        PaintUtility.drawText(canvas, 180f, 470f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.NORMAL, 12f, Color.BLACK, address[2].trim() + "-22414, " + address[3].trim() + " Province");
        PaintUtility.drawText(canvas, 180f, 490f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.NORMAL, 12f, Color.BLACK, "Nepal (NP)");
        PaintUtility.drawText(canvas, 180f, 510f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.NORMAL, 12f, Color.BLACK, "☎ " + user.getUserPhone());


        // Shipped to
        PaintUtility.drawText(canvas, (pageWidth / 2f) - 100f, 400f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.BOLD, 16f, Color.RED, "Shipped To");
        PaintUtility.drawText(canvas, (pageWidth / 2f) - 100f, 430f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.BOLD_ITALIC, 12f, Color.BLACK, user.getUserName());
        PaintUtility.drawText(canvas, (pageWidth / 2f) - 100f, 450f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.NORMAL, 12f, Color.BLACK, address[0].trim() + ", "+ address[1].trim());
        PaintUtility.drawText(canvas, (pageWidth / 2f) - 100f, 470f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.NORMAL, 12f, Color.BLACK, address[2].trim() + "-22414, " + address[3].trim() + " Province");
        PaintUtility.drawText(canvas, (pageWidth / 2f) - 100f, 490f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.NORMAL, 12f, Color.BLACK, "Nepal (NP)");
        PaintUtility.drawText(canvas, (pageWidth / 2f) - 100f, 510f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.NORMAL, 12f, Color.BLACK, "☎ " + user.getUserPhone());

        // Draw rectangle
        PaintUtility.drawRectangle(canvas, 180f, pageWidth - 40f, 550f, 580f);

        // divider lines within rectangle except two outside lines
        PaintUtility.drawLine(canvas, 250f, 550f, 250f, 580f, 1f, Color.WHITE);
        PaintUtility.drawLine(canvas, pageWidth - 390f, 550f, pageWidth - 390f, 580f, 1f, Color.WHITE);
        PaintUtility.drawLine(canvas, pageWidth - 270f, 550f, pageWidth - 270f, 580f, 1f, Color.WHITE);
        PaintUtility.drawLine(canvas, pageWidth - 160f, 550f, pageWidth - 160f, 580f, 1f, Color.WHITE);

        // Heading within rectangle
        PaintUtility.drawText(canvas, 200f, 570f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.BOLD, 16f, Color.WHITE, "S.N.");
        PaintUtility.drawText(canvas, 500f, 570f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.BOLD, 16f, Color.WHITE, "Descriptions");
        PaintUtility.drawText(canvas, pageWidth - 350f, 570f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.BOLD, 16f, Color.WHITE, "QTY");
        PaintUtility.drawText(canvas, pageWidth - 265f, 570f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.BOLD, 16f, Color.WHITE, "Unit Price");
        PaintUtility.drawText(canvas, pageWidth - 125f, 570f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.BOLD, 16f, Color.WHITE, "Total");


        /*
        Main part of the invoice that is product list ordered by the user in the table
         */
        float y = 580f;
        double totalAmount = 0;
        double discount = 0;

        for (int i = 0; i < orderItems.size(); i++) {
            y += 30f;
            totalAmount += orderItems.get(i).getItemQuantity() * orderItems.get(i).getItemPrice();
            PaintUtility.drawText(canvas, 200f, y, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.NORMAL, 14f, Color.BLACK, String.valueOf(i+1));
            PaintUtility.drawText(canvas, 300f, y, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.NORMAL, 14f, Color.BLACK, orderItems.get(i).getItemName());
            PaintUtility.drawText(canvas, pageWidth - 350f, y, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.NORMAL, 14f, Color.BLACK, String.valueOf(orderItems.get(i).getItemQuantity()) + "KG");
            PaintUtility.drawText(canvas, pageWidth - 250f, y, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.NORMAL, 14f, Color.BLACK, "Rs " + String.valueOf(orderItems.get(i).getItemPrice()));
            PaintUtility.drawText(canvas, pageWidth - 130f, y, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.NORMAL, 14f, Color.BLACK, "Rs " + String.valueOf(orderItems.get(i).getItemQuantity() * orderItems.get(i).getItemPrice()));
        }
        double subTotal = totalAmount - discount;
        double cashPaid = subTotal;

                // Verticles lines from the square i.e. headings of the table
        PaintUtility.drawLine(canvas, 180f, 550f, 180f, y + 215f, 1f, Color.RED);
        PaintUtility.drawLine(canvas, 250f, 580f, 250f, y + 30f, 1f, Color.RED);
        PaintUtility.drawLine(canvas, pageWidth - 390f, 580f, pageWidth - 390f, y + 215f, 1f, Color.RED);
        PaintUtility.drawLine(canvas, pageWidth - 270f, 580f, pageWidth - 270f, y + 30f, 1f, Color.RED);
        PaintUtility.drawLine(canvas, pageWidth - 160f, 580f, pageWidth - 160f, y + 30f, 1f, Color.RED);
        PaintUtility.drawLine(canvas, pageWidth - 40f, 550f, pageWidth - 40f, y + 215f, 1f, Color.RED);
        PaintUtility.drawLine(canvas, 180f, y + 30f, pageWidth - 40f, y + 30f, 1f, Color.RED);


        // Calculation of total
        //Label texts
        PaintUtility.drawText(canvas, pageWidth - 350f, y + 60f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.BOLD, 20f, Color.RED, "Sub-Total");
        PaintUtility.drawText(canvas, pageWidth - 350f, y + 90f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.NORMAL, 20f, Color.RED, "Discount");
        //values
        PaintUtility.drawText(canvas, pageWidth - 190f, y + 60f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.BOLD, 20f, Color.BLACK, "Rs "+ String.valueOf(totalAmount));
        PaintUtility.drawText(canvas, pageWidth - 190f, y + 90f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.NORMAL, 20f, Color.BLACK, "Rs " + String.valueOf(discount));

        // Horizontal lines
        PaintUtility.drawLine(canvas, pageWidth - 390f, y + 105f, pageWidth - 40f, y + 105f, 1f, Color.RED);

        // Labels text
        PaintUtility.drawText(canvas, pageWidth - 350f, y + 130f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.BOLD_ITALIC, 20f, Color.RED, "Total");
        PaintUtility.drawText(canvas, pageWidth - 350f, y + 160f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.ITALIC, 20f, Color.RED, "Cash Paid");
        // Values
        PaintUtility.drawText(canvas, pageWidth - 190f, y + 130f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.BOLD_ITALIC, 20f, Color.BLACK, "Rs " + String.valueOf(subTotal));
        PaintUtility.drawText(canvas, pageWidth - 190f, y + 160f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.ITALIC, 20f, Color.BLACK, "(Rs "+ String.valueOf(cashPaid) + ")");

        // Horizontal lines
        PaintUtility.drawLine(canvas, pageWidth - 390f, y + 175f, pageWidth - 40f, y + 175f, 1f, Color.RED);

        // Label tsxt
        PaintUtility.drawText(canvas, pageWidth - 350f, y + 200f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.BOLD, 24f, Color.RED, "Due Amount");
        // values
        PaintUtility.drawText(canvas, pageWidth - 190f, y + 200f, Paint.Align.LEFT, Typeface.MONOSPACE, Typeface.BOLD, 24f, Color.BLACK, "Rs " + String.valueOf(subTotal - cashPaid));

        // Horizontal line
        PaintUtility.drawLine(canvas, 180f, y + 213f, pageWidth - 40f, y + 213f, 1f, Color.RED);
        PaintUtility.drawLine(canvas, 180f, y + 215f, pageWidth - 40f, y + 215f, 1f, Color.RED);


        // Values

        /*
        End of the table
         */


        pdfDocument.finishPage(page);

        if (isStored(pdfDocument, "Invoice-" + orderId)) {
            pdfDocument.close();
            Toast.makeText(context, context.getResources().getString(R.string.invoice_generated_message), Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(context, context.getResources().getString(R.string.error_message), Toast.LENGTH_SHORT).show();
        }


    }

    // function to store generated pdf file in the local storage
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean isStored(PdfDocument pdfDocument, String fileName) {
        ContextWrapper contextWrapper = new ContextWrapper(context);
        File file = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File pdfDirPath = new File(file, "/" + fileName + ".pdf");
        Log.d(TAG, "isStored: "+ pdfDirPath.toString());
        try {
            pdfDocument.writeTo(new FileOutputStream(pdfDirPath));
            return true;
        } catch (IOException e) {
            throw new RuntimeException("Error generating file", e);
        }
    }
}
