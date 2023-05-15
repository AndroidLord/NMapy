package com.example.mapy;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mapy.databinding.ActivityQrScannerBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;


public class QrScannerActivity extends AppCompatActivity {
    Context context = this;

    ActivityQrScannerBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQrScannerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyText(v);
            }
        });







        binding.qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode();
            }
        });

        scanCode();
    }

    private void genreateQrCode(String myData) throws WriterException {

        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(myData, BarcodeFormat.QR_CODE, 512, 512);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        ImageView imageView = findViewById(R.id.qr_code_image_view);
        imageView.setImageBitmap(bitmap);

    }

    private void scanCode() {

        ScanOptions options = new ScanOptions();
        options.setPrompt("Turn Volume for Flash");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);

        barLaunch.launch(options);
    }

    public void copyText(View view) {
        TextView textView = findViewById(R.id.text_view);
        String text = textView.getText().toString();

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Text to copy", text);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
    }

    ActivityResultLauncher<ScanOptions> barLaunch = registerForActivityResult(new ScanContract(), result -> {

        if (result.getContents() != null) {

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            dialogBuilder.setTitle("QR Code Scanned Successfully");
            dialogBuilder.setMessage(result.getContents());
            dialogBuilder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    binding.textView.setText(result.getContents());
                    try {
                        genreateQrCode(result.getContents().toString());
                        visibilityTurnedOn();
                    } catch (WriterException e) {
                        visibilityTurnedOff();
                        Snackbar.make(binding.textView,"Error Getting Qr",Snackbar.LENGTH_SHORT).show();
                        //Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                        throw new RuntimeException(e);
                    }

                    dialog.dismiss();
                }
            }).show();


        } else {
            visibilityTurnedOff();
            try {
                genreateQrCode("Nothing...");
            } catch (WriterException e) {
                throw new RuntimeException(e);
            }
            binding.qrCodeImageView.setVisibility(View.VISIBLE);

        }
    });

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (binding.titleQr.getVisibility() == View.GONE) {
            visibilityTurnedOff();
        } else
            finish();
    }

    private void visibilityTurnedOn() {
        binding.textView.setVisibility(View.VISIBLE);
        binding.qrButton.setVisibility(View.VISIBLE);
        binding.titleQr.setVisibility(View.VISIBLE);
        binding.copyButton.setVisibility(View.VISIBLE);
        binding.qrDescription.setVisibility(View.VISIBLE);
    }

    private void visibilityTurnedOff() {
        binding.textView.setVisibility(View.GONE);
        binding.qrButton.setVisibility(View.VISIBLE);
        binding.titleQr.setVisibility(View.GONE);
        binding.copyButton.setVisibility(View.GONE);
        binding.qrDescription.setVisibility(View.GONE);
    }



}