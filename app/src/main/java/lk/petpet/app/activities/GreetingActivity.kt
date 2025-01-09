package lk.petpet.app.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import lk.petpet.app.databinding.ActivityGreetingBinding

class GreetingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGreetingBinding

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityGreetingBinding.inflate(layoutInflater)
            setContentView(binding.root)

            binding.btnStart.setOnClickListener {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }

            binding.btnExit.setOnClickListener {
                finish()
            }
        }
}
