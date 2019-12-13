package com.lp.doit

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textview.MaterialTextView
import com.lp.doit.adapters.TagAdapter
import com.lp.doit.data.Tag
import com.lp.doit.data.Todo


class ActionBottomDialogFragment(main : TagAdapter.ITag,todos: ArrayList<Todo>) : BottomSheetDialogFragment(),
    View.OnClickListener, TagAdapter.ITag {

    private var mListener: ItemClickListener? = null
    private lateinit var nothingText : MaterialTextView
    private lateinit var tagRecyclerView : RecyclerView
    private lateinit var allTagsButton : Button

    private lateinit var tagClickEvent : TagAdapter.ITag


    private var tags = ArrayList<Tag>()
    private var todos = ArrayList<Todo>()

    private lateinit var adapter : TagAdapter

    init {
        this.todos = todos
        this.tagClickEvent = main
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nothingText = view!!.findViewById<MaterialTextView>(R.id.emptySheetBar)
        tagRecyclerView = view!!.findViewById<RecyclerView>(R.id.bottomSheetBar)
        allTagsButton = view!!.findViewById<Button>(R.id.allTagsButton)

        var tmpTags = ArrayList<String>()
        var tagsInArr = ArrayList<String>()

        for (todo in todos) {
            for (tag in todo.tags) {
                tmpTags.add(tag)
            }
        }

        for (tag in tmpTags) {
            if (!tagsInArr.contains(tag)) {
             tags.add(Tag(tag, tmpTags.count{ it == tag}))
             tagsInArr.add(tag)
            }
        }

        if (tags.size > 0) {
            nothingText.visibility = View.GONE
        }

        adapter = TagAdapter(this, tags)
        tagRecyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

        allTagsButton.setOnClickListener {
            tagClickEvent.tagClick(0)
            dismiss()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
            mListener = if (context is ItemClickListener) {
            context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement ItemClickListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onClick(view: View) {
        val tvSelected = view as TextView
        mListener!!.onItemClick(tvSelected.text.toString())
        dismiss()
    }

    interface ItemClickListener {
        fun onItemClick(item: String?)
    }

    companion object {
        const val TAG = "ActionBottomDialog"
        fun newInstance(i : TagAdapter.ITag,todos : ArrayList<Todo>): ActionBottomDialogFragment {
            return ActionBottomDialogFragment(i, todos)
        }
    }

    override fun tagClick(tag: String) {
        tagClickEvent.tagClick(tag)
        dismiss()
    }

    override fun tagClick(tag: Int) {
        tagClickEvent.tagClick(tag)
        dismiss()
    }
}