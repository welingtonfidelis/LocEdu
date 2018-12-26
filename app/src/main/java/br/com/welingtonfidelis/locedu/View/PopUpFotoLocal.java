package br.com.welingtonfidelis.locedu.View;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import br.com.welingtonfidelis.locedu.Helper.ReferencesHelper;
import br.com.welingtonfidelis.locedu.Helper.StorageHelper;
import br.com.welingtonfidelis.locedu.Model.Local;
import br.com.welingtonfidelis.locedu.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class PopUpFotoLocal extends AppCompatActivity {

    private ImageView imgv_foto, imgv_upload, imageView;
    private Bitmap imagemSalva;
    private Local local;
    private String photoUrl;
    private Bitmap imagemServidor;
    private Bitmap imagemLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_foto_local);

        Gson gson = new Gson();
        local = gson.fromJson(getIntent().getStringExtra("LOCAL"), Local.class);

        photoUrl = ReferencesHelper.getDatabaseReference().push().getKey();
        imgv_foto = findViewById(R.id.imv_camera);
        imgv_upload = findViewById(R.id.imv_upload);
        imageView = findViewById(R.id.imgView);

        if(ReferencesHelper.getFirebaseAuth().getCurrentUser() == null){
            imgv_foto.setVisibility(View.GONE);
            imgv_upload.setVisibility(View.GONE);
        }

        if(local.getImagem()!= null){
            loadImage(local.getImagem());
        }

        imgv_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".jpg");
                Uri outputFileUri = Uri.fromFile(file);
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                startActivityForResult(intent, 123);
            }
        });

        imgv_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImage();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.gc(); // garbage colector
        if (requestCode == 123) {
            if (resultCode == RESULT_OK) {
                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 3;
                    imagemSalva = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/arquivo.jpg", options);

                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    boolean validaCompressao = imagemSalva.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
                    byte[] fotoBinario = outputStream.toByteArray();

                    String encodedImage = Base64.encodeToString(fotoBinario, Base64.DEFAULT);

                    imageView.setImageBitmap(imagemSalva); // ImageButton, seto a foto como imagem do botão

                    boolean isImageTaken = true;
                } catch (Exception e) {
                    Toast.makeText(this, "Picture Not taken",Toast.LENGTH_LONG).show();e.printStackTrace();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Picture was not taken 1 ", Toast.LENGTH_SHORT);
            } else {
                Toast.makeText(this, "Picture was not taken 2 ", Toast.LENGTH_SHORT);
            }
        }
    }

    private void loadImage(String id) {
        final StorageHelper storageHelper = new StorageHelper(PopUpFotoLocal.this, id);
        imagemLocal = storageHelper.openImage();

        if (imagemLocal != null) {
            imageView.setImageBitmap(imagemLocal);
            imageView.setVisibility(View.VISIBLE);
            Log.e("BuscaLocal", "Imagem Local");
        } else {
            ReferencesHelper.getStorageReference().child("Imagens").child( id+".jpg").getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    imagemServidor = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                    if (imagemServidor != null) {
                        imageView.setImageBitmap(imagemServidor);
                        imageView.setVisibility(View.VISIBLE);

                        storageHelper.save(bytes);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure( Exception exception) {
                    try {

                        imgv_foto.setVisibility(View.VISIBLE);
                        imgv_upload.setVisibility(View.VISIBLE);

                        throw exception;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }

    private void saveImage() {
        if (imagemSalva != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imagemSalva.compress(Bitmap.CompressFormat.JPEG, 65, stream);
            byte[] data = stream.toByteArray();

            if (data != null) {
                //PopUp de carregamento
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Enviando...");
                final StorageHelper iStorageHelper = new StorageHelper(getApplicationContext(), photoUrl);
                progressDialog.show();iStorageHelper.save(data);

                //Excluindo imagem anterior para upar a nova
                ReferencesHelper.getStorageReference().child("Imagens").child(local.getImagem()+".jpg").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("EXCLUIDO", "onSuccess: deleted file");
                    }
                });

                //Upload da imagem nova
                UploadTask uploadTask = ReferencesHelper.getStorageReference().child("Imagens").child(photoUrl+".jpg").putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception exception) {
                        progressDialog.dismiss();
                        Toast.makeText(PopUpFotoLocal.this, exception.toString(), Toast.LENGTH_SHORT).show();
                        //ProgressBarHelper.stopProgress(ProfileActivity.this);
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(PopUpFotoLocal.this, "Imagem enviada com sucesso", Toast.LENGTH_SHORT).show();

                        // Atualizando key da nova imagem no objeto local
                        if(local.getKey() != null){
                            ReferencesHelper.getDatabaseReference().child("Local").child(local.getKey()).child("imagem").
                                    setValue(photoUrl);

                        }
                    }
                });
            }
        }else{
            Toast.makeText(this, "Você deve selecionar uma imagem", Toast.LENGTH_SHORT).show();
        }
    }

}
