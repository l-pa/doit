package com.lp.doit.adapters

interface RecyclerEvents {
    fun deleteRecyclerItem(id: Int, item: Any)
    fun clickRecyclerItem(id: Int, item: Any)
}