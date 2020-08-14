package com.example.password_encryption;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    EditText textToEncrypt, key;
    TextView outputText;
    Button encryptButton, decryptButton;

    String outputString;
    String AES = "AES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textToEncrypt = findViewById(R.id.text_to_encrypt);
        key = findViewById(R.id.encrypt_pass);

        outputText = findViewById(R.id.output_text);

        encryptButton = findViewById(R.id.encrypt_button);
        decryptButton = findViewById(R.id.decrypt_button);

        encryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    outputString = encrypt(textToEncrypt.getText().toString(), key.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                outputText.setText(outputString);
            }
        });

        decryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    outputString = decrypt(outputString, key.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "It looks like encryption/decryption key has been changed!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                outputText.setText(outputString);
            }
        });

    }

    private String decrypt(String outputString, String password) throws Exception {
        SecretKeySpec key = generateKey(password);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedValue = Base64.decode(outputString, Base64.DEFAULT);
        byte[] decValue = cipher.doFinal(decodedValue);
        return new String(decValue);

    }

    private String encrypt(String text, String keyPassword) throws Exception {
        SecretKeySpec key = generateKey(keyPassword);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = cipher.doFinal(text.getBytes());
        return Base64.encodeToString(encVal, Base64.DEFAULT);

    }

    private SecretKeySpec generateKey(String keyPassword) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = keyPassword.getBytes(StandardCharsets.UTF_8);
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();

        return new SecretKeySpec(key, "AES");

    }
}
