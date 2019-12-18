package com.lp.doit

import android.R.attr
import android.R.string
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import java.io.BufferedReader
import java.io.File


class ActionBottomDialogFragment(main : TagAdapter.ITag,todos: ArrayList<Todo>) : BottomSheetDialogFragment(),
    View.OnClickListener, TagAdapter.ITag {

    private var mListener: ItemClickListener? = null
    private lateinit var nothingText : MaterialTextView
    private lateinit var tagRecyclerView : RecyclerView
    private lateinit var allTagsButton : Button

    private lateinit var tagClickEvent : TagAdapter.ITag

    private lateinit var exportButton : Button
    private lateinit var importButton : Button


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
        exportButton = view!!.findViewById<Button>(R.id.exportTodos)
        importButton = view!!.findViewById<Button>(R.id.importTodos)

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

        exportButton.setOnClickListener{
            val intentShareFile = Intent(Intent.ACTION_SEND)
            val fileWithinMyDir = File(context?.getFileStreamPath("todo.json")?.toURI())

            if(fileWithinMyDir.exists()) {
                intentShareFile.setType("application/json");
                intentShareFile.putExtra(Intent.EXTRA_STREAM, context?.let { it1 -> TodosFile("todo.json", it1).loadFromFile() }
                );

                intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                    "Sharing File...json");
                intentShareFile.putExtra(Intent.EXTRA_TEXT,             context?.let { it1 -> TodosFile("todo.json", it1).loadFromFile() }
                );

                startActivity(Intent.createChooser(intentShareFile,             context?.let { it1 -> TodosFile("todo.json", it1).loadFromFile() }
                ));
            }
        }

        importButton.setOnClickListener{
            val myIntent = Intent(Intent.ACTION_GET_CONTENT, null)
            myIntent.type = "application/*"
            startActivityForResult(myIntent, 100)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode === 100 && resultCode == Activity.RESULT_OK) {
            var f = File(data?.data?.path)
            val bufferedReader: BufferedReader = f.bufferedReader()
            context?.let { TodosFile("todo.json", it).import(bufferedReader.use { it.readText() }) }
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