package com.example.test

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_additem.view.*
import kotlinx.android.synthetic.main.layout_additem.view.datePicker
import kotlinx.android.synthetic.main.layout_additem.view.text_text
import kotlinx.android.synthetic.main.layout_additem.view.text_topic
import kotlinx.android.synthetic.main.layout_additem.view.timePicker
import kotlinx.android.synthetic.main.layout_changeitem.view.*
import android.util.Log


class MainActivity : AppCompatActivity(), RecyclerViewAdapter.OnItemClickListener {
    private val exampleList = ArrayList<ExampleItem>()
    private val adapter = RecyclerViewAdapter(exampleList, this)
    private var newItem = ExampleItem("", "", "", "", 0)

    lateinit var alarmManager: AlarmManager
    lateinit var alarmIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("START", "STARTING********")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recycler_view.adapter = this.adapter
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)
    }

    fun insertItem(view: View) {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.layout_additem, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("Add new activity")

        mDialogView.timePicker.setIs24HourView(true)
        val mAlertDialog = mBuilder.show()
        val calendar = Calendar.getInstance()

        val id: Int = if (this.exampleList.size == 0) {
            0
        } else {
            this.exampleList[exampleList.size - 1].notificationId + 1
        }

        mDialogView.button_addNewReminder.setOnClickListener {
            val topic = mDialogView.text_topic.text.toString()
            val text = mDialogView.text_text.text.toString()

            val hour =
                if (mDialogView.timePicker.hour.toString().length == 1) {
                    "0" + mDialogView.timePicker.hour.toString()
                } else {
                    mDialogView.timePicker.hour.toString()
                } + ":" +
                        if (mDialogView.timePicker.minute.toString().length == 1) {
                            "0" + mDialogView.timePicker.minute.toString()
                        } else {
                            mDialogView.timePicker.minute.toString()
                        }

            val day =
                if (mDialogView.datePicker.dayOfMonth.toString().length == 1) {
                    "0" + mDialogView.datePicker.dayOfMonth.toString()
                } else {
                    mDialogView.datePicker.dayOfMonth.toString()
                }

            var month = (mDialogView.datePicker.month + 1).toString()
            month = if (month.length == 1) {
                "0$month"
            } else {
                month
            }
            //set Up notification
            calendar.set(
                mDialogView.datePicker.year, mDialogView.datePicker.month, mDialogView.datePicker.dayOfMonth,
                mDialogView.timePicker.hour, mDialogView.timePicker.minute
            )

            alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            Log.d("CREATEING NEW PENDING INTENT", "WITH EXTRAS TOPIC: $topic, TEXT: $text, id: $id")

            val intentTest = Intent(applicationContext, AlarmReceiver::class.java)
            alarmIntent = PendingIntent.getBroadcast(
                applicationContext, id,
                intentTest
                    .putExtra("notification_topic", topic)
                    .putExtra("notification_text", text)
                    .putExtra("notification_id", id), PendingIntent.FLAG_UPDATE_CURRENT
            )
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                alarmIntent
            )

            val date = mDialogView.datePicker.year.toString() + ":" + month + ":" + day
            this.newItem = ExampleItem(topic, hour, date, text, id)
            mAlertDialog.dismiss()
            this.exampleList.add(exampleList.size, newItem)
            adapter.notifyItemInserted(exampleList.size)
        }
    }

    override fun onItemClick(position: Int) {
        //Toast.makeText(this, "Item $position clicked", Toast.LENGTH_SHORT).show()
        val clickedItem: ExampleItem = exampleList[position]

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.layout_changeitem, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("Add new activity")

        mDialogView.timePicker.setIs24HourView(true)
        mDialogView.text_topic.setText(clickedItem.topic)
        mDialogView.text_text.setText(clickedItem.text)
        val delimiter: Int = clickedItem.hour.indexOf(':')
        mDialogView.timePicker.hour = clickedItem.hour.substring(0, delimiter).toInt()
        mDialogView.timePicker.minute = clickedItem.hour.substring(delimiter + 1).toInt()
        mDialogView.datePicker.updateDate(
            clickedItem.date.substring(0, 4).toInt(),
            clickedItem.date.substring(5, 7).toInt() - 1,
            clickedItem.date.substring(8, 10).toInt()
        )

        val mAlertDialog = mBuilder.show()

        mDialogView.button_updateReminder.setOnClickListener {
            val topic = mDialogView.text_topic.text.toString()
            val text = mDialogView.text_text.text.toString()
            val hour =
                if (mDialogView.timePicker.hour.toString().length == 1) {
                    "0" + mDialogView.timePicker.hour.toString()
                } else {
                    mDialogView.timePicker.hour.toString()
                } + ":" +
                        if (mDialogView.timePicker.minute.toString().length == 1) {
                            "0" + mDialogView.timePicker.minute.toString()
                        } else {
                            mDialogView.timePicker.minute.toString()
                        }

            val day =
                if (mDialogView.datePicker.dayOfMonth.toString().length == 1) {
                    "0" + mDialogView.datePicker.dayOfMonth.toString()
                } else {
                    mDialogView.datePicker.dayOfMonth.toString()
                }

            var month = (mDialogView.datePicker.month + 1).toString()
            month = if (month.length == 1) {
                "0$month"
            } else {
                month
            }

            val date = mDialogView.datePicker.year.toString() + ":" + month + ":" + day
            clickedItem.date = date
            clickedItem.hour = hour
            clickedItem.topic = topic
            clickedItem.text = text

            val calendar = Calendar.getInstance()
            calendar.set(
                mDialogView.datePicker.year, mDialogView.datePicker.month, mDialogView.datePicker.dayOfMonth,
                mDialogView.timePicker.hour, mDialogView.timePicker.minute
            )
            alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

            val intentTest = Intent(applicationContext, AlarmReceiver::class.java)
            alarmIntent = PendingIntent.getBroadcast(
                applicationContext, clickedItem.notificationId,
                intentTest
                    .putExtra("notification_topic", clickedItem.topic)
                    .putExtra("notification_text", clickedItem.text)
                    .putExtra("notification_id", clickedItem.notificationId), PendingIntent.FLAG_UPDATE_CURRENT
            )
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                alarmIntent
            )

            mAlertDialog.dismiss()
            adapter.notifyItemChanged(position)
        }

        mDialogView.button_deleteReminder.setOnClickListener {
            val intentTest = Intent(applicationContext, AlarmReceiver::class.java)
            PendingIntent.getBroadcast(
                applicationContext, clickedItem.notificationId,
                intentTest
                    .putExtra("notification_topic", clickedItem.topic)
                    .putExtra("notification_text", clickedItem.text)
                    .putExtra("notification_id", clickedItem.notificationId), 0
            ).cancel()

            mAlertDialog.dismiss()
            exampleList.removeAt(position)
            adapter.notifyItemRemoved(position)
        }
    }

}