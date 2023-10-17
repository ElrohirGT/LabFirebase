package com.uvg.gt.labfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FetchingActivity : AppCompatActivity() {
    private lateinit var rvChocolates: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var empList: ArrayList<ChocolateModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetching)

        rvChocolates = findViewById(R.id.rvChocolates)
        rvChocolates.layoutManager = LinearLayoutManager(this)
        rvChocolates.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        empList = arrayListOf<ChocolateModel>()

        getEmployeesData()

    }

    private fun getEmployeesData() {

        rvChocolates.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Chocolates")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                empList.clear()
                if (snapshot.exists()){
                    for (empSnap in snapshot.children){
                        val empData = empSnap.getValue(ChocolateModel::class.java)
                        empList.add(empData!!)
                    }
                    val mAdapter = EmpAdapter(empList)
                    rvChocolates.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : EmpAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@FetchingActivity, EmployeeDetailsActivity::class.java)

                            //put extras
                            intent.putExtra("empId", empList[position].chocolateId)
                            intent.putExtra("empName", empList[position].name)
                            intent.putExtra("empAge", empList[position].expireDate)
                            intent.putExtra("empSalary", empList[position].price)
                            startActivity(intent)
                        }

                    })

                    rvChocolates.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Fetching Activity Error", error.toString())
            }

        })
    }
}