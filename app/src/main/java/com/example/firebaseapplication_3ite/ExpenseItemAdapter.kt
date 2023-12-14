package com.example.firebaseapplication_3ite

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class ExpenseItemAdapter (var context: Context, var expenseArrayList: ArrayList<ExpenseList>) : BaseAdapter(){
    private var mContext = context
    private var mList = expenseArrayList
    lateinit var dbReference: DatabaseReference

        fun removeFromDatabase(inpName: String, inpPrice: String, position: Int){
            dbReference = FirebaseDatabase.getInstance().getReference("ExpenseList")
            //var expenseDatabaseClass = DatabaseClass(inpName, inpPrice)
            //var dataKey: String = keyArray[position]
            val list = ArrayList<String>()
            dbReference.child("ExpenseDay").get().addOnSuccessListener {task ->
                for (document in task.children) {
                    list.add(document.key.toString())
                    //Log.d("ExpenseTracker", document.key.toString())
                }
                //Log.d("ExpenseTracker", list.size.toString())//Do what you need to do with your list
                //Remove
                dbReference.child("ExpenseDay").child(list[position]).removeValue().addOnSuccessListener {
                    Log.d("ExpenseTracker", "removeWorks")

                }

            }

            var expenseToBeSubtracted = inpPrice.toInt()
            var currentTotal = 0
            dbReference.child("TotalExpense").get().addOnSuccessListener {task ->
                currentTotal = task.getValue().toString().toInt()
                var totalPriceSub = currentTotal - expenseToBeSubtracted

                dbReference.child("TotalExpense").setValue(totalPriceSub).addOnSuccessListener {
                    Log.d("ExpenseTracker", "Total expense subtracted")
                }
                (context as MainActivity).refreshData(expenseToBeSubtracted)
            }
    }


    override fun getCount(): Int {
        return expenseArrayList.size
    }

    override fun getItem(position: Int): Any {
        return expenseArrayList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view:View = View.inflate(context, R.layout.list_item, null)

        var getExpName:TextView = view.findViewById(R.id.expName)
        var getExpPrice:TextView = view.findViewById(R.id.expPrice)
        var deleteExpense: ImageButton = view.findViewById(R.id.btnExpCancel)

        var expenseList:ExpenseList = expenseArrayList.get(position)
        getExpName.text = expenseList.exName
        getExpPrice.text = expenseList.exPrice.toString()

        deleteExpense.setOnClickListener{
            removeFromDatabase(getExpName.text.toString(), getExpPrice.text.toString(), position)
            expenseArrayList?.removeAt(position)
            notifyDataSetChanged()

        }

        return view
    }




}