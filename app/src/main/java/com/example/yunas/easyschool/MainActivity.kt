package com.example.yunas.easyschool

import android.app.Activity
import android.app.AlertDialog.Builder
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText

class MainActivity : Activity() {
    lateinit var ename : EditText

    lateinit var eroll_no: EditText
    lateinit var ecourse: EditText
    lateinit var add: Button
    lateinit var view: Button
    lateinit var viewall: Button
    lateinit var Show1: Button
    lateinit var delete: Button
    lateinit var modify: Button
   lateinit var Photo : Button
    lateinit var db: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ename = findViewById(R.id.name) as EditText
        eroll_no = findViewById(R.id.roll_no) as EditText
        ecourse = findViewById(R.id.marks) as EditText
        add = findViewById(R.id.addbtn) as Button
        view = findViewById(R.id.viewbtn) as Button
        viewall = findViewById(R.id.viewallbtn) as Button
        delete = findViewById(R.id.deletebtn) as Button
        Show1 = findViewById(R.id.showbtn) as Button
        modify = findViewById(R.id.modifybtn) as Button
        Photo = findViewById(R.id.Photobtn) as Button

        db = openOrCreateDatabase("Student_manage", Context.MODE_PRIVATE, null)
        db.execSQL("CREATE TABLE IF NOT EXISTS student(rollno INTEGER,name VARCHAR,course VARCHAR);")


        add.setOnClickListener(OnClickListener {
            // TODO Auto-generated method stub
            if (eroll_no.text.toString().trim { it <= ' ' }.length == 0 ||
                    ename.text.toString().trim { it <= ' ' }.length == 0 ||
                    ecourse.text.toString().trim { it <= ' ' }.length == 0) {
                showMessage("Fejl", "Vær venlig indtast alle informationer")
                return@OnClickListener
            }
            db.execSQL("INSERT INTO student VALUES('" + eroll_no.text + "','" + ename.text +
                    "','" + ecourse.text + "');")
            showMessage("Success", "Person tilføjet")
            clearText()
        })
        delete.setOnClickListener(OnClickListener {
            // TODO Auto-generated method stub
            if (eroll_no.text.toString().trim { it <= ' ' }.length == 0) {
                showMessage("Fejl", "Vær venligt indtast Studie Nr.")
                return@OnClickListener
            }
            val c = db.rawQuery("SELECT * FROM student WHERE rollno='" + eroll_no.text + "'", null)
            if (c.moveToFirst()) {
                db.execSQL("DELETE FROM student WHERE rollno='" + eroll_no.text + "'")
                showMessage("Success", "Person Slettet")
            } else {
                showMessage("Fejl", "Forkert Studie Nr.")
            }
            clearText()
        })
        modify.setOnClickListener(OnClickListener {
            // TODO Auto-generated method stub

            // TODO Auto-generated method stub
            if (eroll_no.getText().toString().trim().length == 0) {
                showMessage("Fejl", "Vær venligt indtast Studie Nr.")
                return@OnClickListener
            }
            val c = db.rawQuery("SELECT * FROM student WHERE rollno='" + eroll_no.getText() + "'", null)
            if (c.moveToFirst()) {
                db.execSQL("UPDATE student SET name='" + ename.getText() + "',course='" + ecourse.getText() +
                        "' WHERE rollno='" + eroll_no.getText() + "'")
                showMessage("Success", "Person Ændred")
            } else {
                showMessage("Fejl", "Forkert Studie Nr.")
            }
            clearText()

        })
        view.setOnClickListener(OnClickListener {
            // TODO Auto-generated method stub
            if (eroll_no.text.toString().trim { it <= ' ' }.length == 0) {
                showMessage("Fejl", "Vær venlig indtast Studie Nr.")
                return@OnClickListener
            }
            val c = db.rawQuery("SELECT * FROM student WHERE rollno='" + eroll_no.text + "'", null)
            if (c.moveToFirst()) {
                ename.setText(c.getString(1))
                ecourse.setText(c.getString(2))
            } else {
                showMessage("Fejl", "Forkert Studie Nr.")
                clearText()
            }
        })
        viewall.setOnClickListener(OnClickListener {
            // TODO Auto-generated method stub
            val c = db.rawQuery("SELECT * FROM student", null)
            if (c.count == 0) {
                showMessage("Fejl", "Ingen persone fundet")
                return@OnClickListener
            }
            val buffer = StringBuffer()
            while (c.moveToNext()) {
                buffer.append("Studie Nr: " + c.getString(0) + "\n")
                buffer.append("Navn: " + c.getString(1) + "\n")
                buffer.append("Kursus: " + c.getString(2) + "\n\n")
            }
            showMessage("Elev Info", buffer.toString())
        })
        Show1.setOnClickListener {
            // TODO Auto-generated method stub
            showMessage("EasySchool App", "Udviklet af Yunas & Ümit")
        }

        Photo.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, 0)
            }
        })
    }

    fun showMessage(title: String, message: String) {
        val builder = Builder(this)
        builder.setCancelable(true)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.show()
    }

    fun clearText() {
        eroll_no.setText("")
        ename.setText("")
        ecourse.setText("")
        eroll_no.requestFocus()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.student_main, menu)
        return true
    }

}
