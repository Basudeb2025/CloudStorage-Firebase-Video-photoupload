package com.example.storagemediaupload

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.storagemediaupload.databinding.ActivityUploadBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class upload : AppCompatActivity() {
    lateinit var aut:FirebaseAuth
    lateinit var bind: ActivityUploadBinding
    lateinit var storage: FirebaseStorage
    var isSelect : Boolean = false
    lateinit var pdfUri : Uri
    lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(bind.root)
        aut = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("pdfs")
        storage = FirebaseStorage.getInstance()

        bind.media.setOnClickListener {
            selectPDF()
        }
      bind.upload.setOnClickListener {
          if(isSelect){
              upLoadPDF()
          }
          else{
              Toast.makeText(this,"Please select the PDF First",Toast.LENGTH_SHORT).show()
          }
      }

    }

    private fun upLoadPDF() {
        val pdfREf = storage.getReference("pdfs").child(aut.currentUser?.email.toString())
        pdfREf.putFile(pdfUri)
            .addOnSuccessListener {
                pdfREf.downloadUrl.addOnSuccessListener {down->
                    val datauri = down.toString()
                    Toast.makeText(this,"PDF upload succesfully",Toast.LENGTH_SHORT).show()
                    saveDatabase(datauri)
                }.addOnFailureListener {
                    Toast.makeText(this,"PDF upload Failue",Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveDatabase(datauri: String) {
          val userId = aut.currentUser!!.uid
        if(userId != null){
            val data = mapOf("data" to datauri)
            databaseReference.child(userId).setValue(data).addOnSuccessListener {
                Toast.makeText(this,"succesfully to save in database",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this,"Failur to save data",Toast.LENGTH_SHORT).show()
            }
        }
    }

    //This function is for Select the Pdf succesfully
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == pdfcode && resultCode == RESULT_OK){
            data?.data?.let {uri->
                pdfUri = uri
                bind.imageView.setImageResource(R.drawable.baseline_picture_as_pdf_24)
                Toast.makeText(this,"Succes to select",Toast.LENGTH_SHORT).show()
                isSelect= true
            }
        }
    }
    private fun selectPDF() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf" // for image "image/*" for video "video/*"
        startActivityForResult(intent,pdfcode)
    }
  companion object{
    var  pdfcode = 1
  }

}