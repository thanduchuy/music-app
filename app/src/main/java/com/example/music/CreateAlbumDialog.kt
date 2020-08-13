package com.example.mediaplayer

import android.R.*
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatDialogFragment


class CreateAlbumDialog() : AppCompatDialogFragment() {
    private  var listener: DialogListener? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater;

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.create_new_album, null))
                // Add action buttons
                .setPositiveButton("Đồng ý",
                    DialogInterface.OnClickListener { dialog, id ->
                        val dialogObj = Dialog::class.java.cast(dialog) as Dialog
                        val text =
                            dialogObj.findViewById<EditText>(R.id.editText_album).text.toString()
                        listener?.applyText(text)
                    })
                .setNegativeButton("Bỏ qua",
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })
            builder.setTitle("Tạo album mới")
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as DialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "must implement DialogListener")
        }
    }
    interface DialogListener {
        fun applyText(text: String)
    }
}