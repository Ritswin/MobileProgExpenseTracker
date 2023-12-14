package com.example.firebaseapplication_3ite

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity(), AddExpenseDialog.DialogListener{
    override fun sendInput(inputName: String, inputPrice: String) {
        Log.d("ExpenseTracker", "sendInput: got the input: $inputName and $inputPrice")

        expenseArrayList =  setInputToListView(inputName, inputPrice)
        eItemAdapter = ExpenseItemAdapter(this@MainActivity, expenseArrayList!!)
        lstTracker?.adapter = eItemAdapter

    }

    private var lstTracker:ListView? = null
    private var expenseArrayList:ArrayList<ExpenseList>? = null
    private var eItemAdapter:ExpenseItemAdapter? = null
    private var totalPrice:Int = 0
    lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnAddExpense = findViewById<ImageButton>(R.id.buttonAddExpense)
        var btnMenu = findViewById<ImageButton>(R.id.buttonMenu)
        lstTracker = findViewById(R.id.listTracker)
        expenseArrayList = ArrayList()


//        databaseReference = FirebaseDatabase.getInstance().getReference("ReyesDatabase")
//        var databaseClass = DatabaseClass("Richwin", "SSE", "BizApps")
//        var dataKey = databaseReference.push().key
//
//        databaseReference.child("ReyesItem").setValue(databaseClass).addOnSuccessListener {
//            Toast.makeText(this, "Success - Add", Toast.LENGTH_SHORT).show()
//        }
//
//        //Update
//        var empUpdate = mapOf<String, String>("EmpName" to "Richwin Kyle", "EmpPosition" to "Staff Engineer", "EmpDepartment" to "BizApps")
//        databaseReference.child("uniqueId").updateChildren(empUpdate).addOnSuccessListener {
//            Toast.makeText(this, "Success - Update", Toast.LENGTH_SHORT).show()
//        }
//
//        //Remove
//        databaseReference.child("uniqueId").child("sampleOne").removeValue().addOnSuccessListener {
//            Toast.makeText(this, "Success - Delete", Toast.LENGTH_SHORT).show()
//        }


        btnAddExpense.setOnClickListener {

            val fragmentObject = AddExpenseDialog()
            fragmentObject.isCancelable = false

            fragmentObject.show(supportFragmentManager, "Add_Expense")

        }

    }
    private fun setInputToListView(iName: String,iPrice: String): ArrayList<ExpenseList>{
        expenseArrayList?.add(ExpenseList(iName, iPrice.toInt()))
        addToDatabase(iName, iPrice)
        return expenseArrayList!!
    }

    private fun addToDatabase(inpName: String, inpPrice: String):  Void?{
        databaseReference = FirebaseDatabase.getInstance().getReference("ExpenseList")
        var lblTotalExpense = findViewById<TextView>(R.id.labelTotalExpense)
        var expenseDatabaseClass = DatabaseClass(inpName, inpPrice)
        var expenseDataKey = databaseReference.push().key

        if (expenseDataKey != null) {
            totalPrice += inpPrice.toInt()
            var totalText = totalPrice.toString() + " pesos"
            databaseReference.child("ExpenseDay").child(expenseDataKey).setValue(expenseDatabaseClass).addOnSuccessListener {
                Toast.makeText(this, "Success - Add", Toast.LENGTH_SHORT).show()

            }
            lblTotalExpense.setText(totalText)
            databaseReference.child("TotalExpense").setValue(totalPrice).addOnSuccessListener {
                Toast.makeText(this, "Total expense updated", Toast.LENGTH_SHORT).show()
            }
        }

//        //Update
//        var empUpdate = mapOf<String, String>("EmpName" to "Richwin Kyle", "EmpPosition" to "Staff Engineer", "EmpDepartment" to "BizApps")
//        databaseReference.child("uniqueId").updateChildren(empUpdate).addOnSuccessListener {
//            Toast.makeText(this, "Success - Update", Toast.LENGTH_SHORT).show()
//        }
//

        return null
    }

    fun refreshData(toBeSubtracted: Int) {
        var lblTotalExpenseSubtract = findViewById<TextView>(R.id.labelTotalExpense)
        totalPrice -= toBeSubtracted
        var totalTextSub = totalPrice.toString() + " pesos"
        lblTotalExpenseSubtract.setText(totalTextSub)

    }
}

