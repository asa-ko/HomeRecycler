package app.shigenawa.ass.homerecycler

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TimePicker
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.home_data.*
import java.util.Date
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class RegisterActivity : AppCompatActivity() {
    val realm:Realm=Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val time:Time?=read()

       val DataFormat  =SimpleDateFormat("yyyy/MM/dd").format(Date())
      // dataText.text=DataFormat
       // val dateGet=LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))

       // dataText.text=todayDate()
        //mainDate()

        if(time!=null){
            val uried: Uri= Uri.parse(time.uri)
            imageView.setImageURI(uried)
        }

        val button=findViewById<Button>(R.id.button)
        button.setOnClickListener {
            selectPhoto()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }


    fun save(uri: String){
        val time:Time?=read()
        //保存する処理
        realm.executeTransaction {
            if (time != null) {
                time.uri=uri
            }else{
                val newItem: Time=realm.createObject(Time::class.java)
                newItem.uri=uri
            }
            Snackbar.make(imageView,"保存しました", Snackbar.LENGTH_SHORT).show()
        }
        //保存したuriのStringをUriに戻す
        val newUri:Uri= Uri.parse(uri)

    }


    fun read():Time?{
        return realm.where(Time::class.java).findFirst()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) {
            return
        }
        when (requestCode) {
            READ_REQUEST_CODE -> {
                try {
                    data?.data?.also { uri ->

                        /* realm.executeTransaction {
                             val stringUri:String=uri.toString()
                             //val newItem = realm.createObject(Item::class.java)
                             //newItem.uri=item.uri
                             save(stringUri)
                         }
                         */
                        val stringUri:String=uri.toString()
                        save(stringUri)



                        val inputStream = contentResolver?.openInputStream(uri)
                        val image = BitmapFactory.decodeStream(inputStream)
                        val imageView = findViewById<ImageView>(R.id.imageView)
                        imageView.setImageBitmap(image)

                    }

                } catch (e: Exception) {
                    Toast.makeText(this, "エラーが発生しました", Toast.LENGTH_LONG).show()
                }

            }
        }

    }

    private fun selectPhoto() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        startActivityForResult(intent, READ_REQUEST_CODE)
    }

    companion object {
        private const val READ_REQUEST_CODE: Int = 42
    }

   /* fun todayDate():String{
        //val onlyDate: LocalDate =LocalDate.now()
        val date:Date = Date()
        val format = SimpleDateFormat("yyyy/MM/dd",Locale.getDefault())
        return format.format(date)
    }
    */

   /* fun mainDate(args: Array<String>) {
        val cal = Calendar.getInstance()
        cal.time = Date()
        val df: DateFormat = SimpleDateFormat("yyyy/MM/dd")
        dataText.text=df.toString()
       // println("current: ${df.format(cal.time)}")

      //  cal.add(Calendar.MONTH, 2)
      //  cal.add(Calendar.DATE, -3)
      //  println("after: ${df.format(cal.time)}")
    }*/

  /*  override fun onTimeSet(view: TimePicker){
        val str =String.format(Locale.JAPAN, "%d%d")
        dataText.text=str
    }

    fun showTimePickerDialog(v: View){
        val newFragment=TimePick()
        newFragment.show(supportFragmentManager,"timePicker")
    }
   */

}



