package com.uvg.gt.labfirebase

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class EmployeeDetailsActivity : AppCompatActivity() {
    private lateinit var tvchocid: TextView
    private lateinit var tvchocname: TextView
    private lateinit var tvchoexpire: TextView
    private lateinit var tvchocprice: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("chocolateId").toString(),
                intent.getStringExtra("name").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("chocolateId").toString()
            )
        }

    }

    private fun initView() {
        tvchocid = findViewById(R.id.tvchocid)
        tvchocname = findViewById(R.id.tvchocname)
        tvchoexpire = findViewById(R.id.tvchoexpire)
        tvchocprice = findViewById(R.id.tvchocprice)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        tvchocid.text = intent.getStringExtra("chocolateId")
        tvchocname.text = intent.getStringExtra("name")
        tvchoexpire.text = intent.getStringExtra("expireDate")
        tvchocprice.text = intent.getStringExtra("price")

    }

    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Chocolates").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Chocolate data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, FetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun openUpdateDialog(
        empId: String,
        empName: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialogView)

        val etEmpName = mDialogView.findViewById<EditText>(R.id.etEmpName)
        val etEmpAge = mDialogView.findViewById<EditText>(R.id.etEmpAge)
        val etEmpSalary = mDialogView.findViewById<EditText>(R.id.etEmpSalary)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etEmpName.setText(intent.getStringExtra("name").toString())
        etEmpAge.setText(intent.getStringExtra("expireDate").toString())
        etEmpSalary.setText(intent.getStringExtra("price").toString())

        mDialog.setTitle("Updating $empName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateEmpData(
                empId,
                etEmpName.text.toString(),
                etEmpAge.text.toString(),
                etEmpSalary.text.toString()
            )

            Toast.makeText(applicationContext, "Chocolate Data Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            tvchocname.text = etEmpName.text.toString()
            tvchoexpire.text = etEmpAge.text.toString()
            tvchocprice.text = etEmpSalary.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updateEmpData(
        id: String,
        name: String,
        expireDate: String,
        price: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Chocolates").child(id)
        val empInfo = ChocolateModel(id, name, expireDate, price)
        dbRef.setValue(empInfo)
    }

}