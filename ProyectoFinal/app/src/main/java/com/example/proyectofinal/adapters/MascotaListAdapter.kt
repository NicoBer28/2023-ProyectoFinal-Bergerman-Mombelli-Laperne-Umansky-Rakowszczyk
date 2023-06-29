package com.example.proyectofinal.adapters

import android.content.ClipData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectofinal.R
import java.util.*
import java.util.jar.Attributes.Name

abstract class JobsListAdapter (private var mascotaList : MutableList<Mascota>) :
    RecyclerView.Adapter<JobsListAdapter.PanaderiaHolder>() {

        class PanaderiaHolder(v: View) : RecyclerView.ViewHolder(v){
            private var v : View
            init {
                this.v = v
            }

            fun setName (name : String){
                val tvName : TextView = v.findViewById(R.id.textView6)
                tvName.text = name
            }

            fun getCardLayout () : CardView{
                return v.findViewById(R.id.card_package_item)
            }

            fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PanaderiaHolder {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_mascota,parent,false)
                return PanaderiaHolder(view)
            }

            fun onBindViewHolder(holder: ClipData.Item holder, position: Int) {
                //Relaciónes entre items visuales y atributos de las listas
                //Relaciones entre items visuales y clicks
                holder.setName(mascotaList[position].nombre)

                fun setData(newData: ArrayList</*Object*/>){
                    this./*List*/ = newData
                            this.notifyDataSetChanged()
                }
                //En nuestro caso
                fun setData(newData: ArrayList<Mascota>){
                    this.mascotaList = newData
                    this.notifyDataSetChanged()
                }

            }

        }

    }
