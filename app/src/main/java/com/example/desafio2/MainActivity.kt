package com.example.desafio2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var spinnerCategory: Spinner
    private lateinit var spinnerDifficulty: Spinner
    private lateinit var btnStartQuiz: Button

    private var selectedCategory = QuizRepository.categories[0]
    private var selectedDifficulty = QuizRepository.difficulties[0]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinnerCategory = findViewById(R.id.spinnerCategory)
        spinnerDifficulty = findViewById(R.id.spinnerDifficulty)
        btnStartQuiz = findViewById(R.id.btnStartQuiz)

        setupSpinners()
        setupButton()
    }

    private fun setupSpinners() {
        val categoryAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            QuizRepository.categories
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerCategory.adapter = categoryAdapter
        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                selectedCategory = QuizRepository.categories[pos]
                // Cambiar color del texto del spinner
                (view as? TextView)?.setTextColor(getColor(R.color.text_primary))
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        val difficultyAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            QuizRepository.difficulties
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerDifficulty.adapter = difficultyAdapter
        spinnerDifficulty.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                selectedDifficulty = QuizRepository.difficulties[pos]
                (view as? TextView)?.setTextColor(getColor(R.color.text_primary))
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupButton() {
        btnStartQuiz.setOnClickListener {
            it.animate().scaleX(0.95f).scaleY(0.95f).setDuration(80).withEndAction {
                it.animate().scaleX(1f).scaleY(1f).setDuration(80).start()
                launchQuiz()
            }.start()
        }
    }

    private fun launchQuiz() {
        val intent = Intent(this, QuizActivity::class.java).apply {
            putExtra(QuizActivity.EXTRA_CATEGORY, selectedCategory)
            putExtra(QuizActivity.EXTRA_DIFFICULTY, selectedDifficulty)
        }
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}