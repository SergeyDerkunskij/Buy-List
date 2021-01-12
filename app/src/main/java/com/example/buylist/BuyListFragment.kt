package com.example.buylist

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.SparseBooleanArray
import android.view.Gravity
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_buy_list.*
import java.util.*


class BuyListFragment : Fragment(R.layout.fragment_buy_list) {

    private var productList = ArrayList<String?>()
    private var listView: ListView? = null
    private var arrayAdapter: ArrayAdapter<String?>? = null
    private var choicePosition = 0
    private val PREFERENCES_PRODUCTS = "PREFERENCES_PRODUCTS"



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val preferences: SharedPreferences =
            requireContext().getSharedPreferences(PREFERENCES_PRODUCTS, Context.MODE_PRIVATE)
        for (i in 0 until preferences.getInt("length", 0)) {
            productList.add(preferences.getString(i.toString(), ""))
        }
        buttonAdd.setOnClickListener{onClickButtonAdd() }
        buttonDelete.setOnClickListener{onClickButtonRemove() }
        listView = products
        arrayAdapter = ArrayAdapter<String?>(
            requireContext(),
            android.R.layout.simple_list_item_single_choice,
            productList
        )
        listView!!.adapter = arrayAdapter
        listView!!.onItemClickListener =
            OnItemClickListener { _, _, position, _ -> choicePosition = position }
    }


    private fun onClickButtonAdd() {

        val item = edit_text_products.text.toString()
        if (item == "") {
            val toast: Toast = Toast.makeText(requireContext(), "Добате покупку...", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        } else {
            productList.add(0, item)
            arrayAdapter!!.notifyDataSetChanged()
            edit_text_products.setText("")
        }
    }

    private fun onClickButtonRemove() {
        if (productList.isEmpty()) {
            val toast: Toast = Toast.makeText(requireContext(), "Список уже пуст!", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        } else {
            val sbArray: SparseBooleanArray = listView!!.checkedItemPositions
            for (i in sbArray.size()-1 downTo 0) {
                val key = sbArray.keyAt(i)
                if (sbArray.get(key)){
                    productList.removeAt(key)
                }
            }
            arrayAdapter!!.notifyDataSetChanged()
            

            for (i in 0 until sbArray.size()) {
                sbArray.setValueAt(i,false)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        onSaveData()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        onSaveData()
    }



    private fun onSaveData() {
        val item = productList.toTypedArray()
        val preferences: SharedPreferences =
            requireContext().getSharedPreferences(PREFERENCES_PRODUCTS, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        for (i in item.indices) {
            editor.putString(i.toString(), item[i])
        }
        editor.putInt("length", item.size)
        editor.apply()
    }


}