package com.uvg.gt.labfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertionActivity : AppCompatActivity() {
    private lateinit var etchocName: EditText
    private lateinit var etchocAge: EditText
    private lateinit var etchocSalary: EditText
    private lateinit var btnSaveData: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)

        etchocName = findViewById(R.id.etEmpName)
        etchocAge = findViewById(R.id.etEmpAge)
        etchocSalary = findViewById(R.id.etEmpSalary)
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("Chocolates")

        btnSaveData.setOnClickListener {
            saveEmployeeData()
        }
    }

    private fun saveEmployeeData() {

        //getting values
        val cName = etchocName.text.toString()
        val cAge = etchocAge.text.toString()
        val cSalary = etchocSalary.text.toString()

        if (cName.isEmpty()) {
            etchocName.error = "Please enter name"
        }
        if (cAge.isEmpty()) {
            etchocAge.error = "Please enter ExpireDate"
        }
        if (cSalary.isEmpty()) {
            etchocSalary.error = "Please enter price"
        }

        val empId = dbRef.push().key!!

        val employee = ChocolateModel(empId, cName, cAge, cSalary)

        dbRef.child(empId).setValue(employee)
            .addOnCompleteListener {
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                etchocName.text.clear()
                etchocAge.text.clear()
                etchocSalary.text.clear()


            }.addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }

    }
}