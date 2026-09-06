package com.example.desafio2

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat

class QuizActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_CATEGORY = "extra_category"
        const val EXTRA_DIFFICULTY = "extra_difficulty"
    }


    private lateinit var toolbar: Toolbar
    private lateinit var tvToolbarTitle: TextView
    private lateinit var tvToolbarSubtitle: TextView
    private lateinit var tvProgressCount: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var questionsContainer: LinearLayout
    private lateinit var btnSubmit: Button
    private lateinit var btnRestart: Button

    private lateinit var category: String
    private lateinit var difficulty: String
    private lateinit var questions: List<Question>

    private val userAnswers = mutableMapOf<Int, Int>()


    private val radioGroups = mutableListOf<RadioGroup>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        category = intent.getStringExtra(EXTRA_CATEGORY) ?: "Ciencias"
        difficulty = intent.getStringExtra(EXTRA_DIFFICULTY) ?: "Fácil"
        questions = QuizRepository.getQuestions(category, difficulty)

        bindViews()
        setupToolbar()
        buildQuestions()
        setupButtons()
    }

    private fun bindViews() {
        toolbar = findViewById(R.id.toolbar)
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle)
        tvToolbarSubtitle = findViewById(R.id.tvToolbarSubtitle)
        tvProgressCount = findViewById(R.id.tvProgressCount)
        progressBar = findViewById(R.id.progressBar)
        questionsContainer = findViewById(R.id.questionsContainer)
        btnSubmit = findViewById(R.id.btnSubmit)
        btnRestart = findViewById(R.id.btnRestart)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { finish() }

        tvToolbarTitle.text = "Quiz — $category"
        tvToolbarSubtitle.text = difficulty


        val diffColor = when (difficulty) {
            "Fácil" -> ContextCompat.getColor(this, R.color.success_green)
            "Medio" -> ContextCompat.getColor(this, R.color.warning_orange)
            "Difícil" -> ContextCompat.getColor(this, R.color.error_red)
            else -> ContextCompat.getColor(this, R.color.accent_cyan)
        }
        tvToolbarSubtitle.setTextColor(diffColor)

        updateProgress()
    }



    private fun buildQuestions() {
        questionsContainer.removeAllViews()
        radioGroups.clear()
        userAnswers.clear()

        questions.forEachIndexed { index, question ->
            val cardView = buildQuestionCard(index, question)
            questionsContainer.addView(cardView)
        }
    }

    private fun buildQuestionCard(index: Int, question: Question): View {
        // Outer card
        val card = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            background = ContextCompat.getDrawable(this@QuizActivity, R.drawable.card_background)
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { bottomMargin = dpToPx(16) }
            layoutParams = lp
            setPadding(dpToPx(20), dpToPx(20), dpToPx(20), dpToPx(20))
        }


        val badgeLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { bottomMargin = dpToPx(12) }
            layoutParams = lp
        }

        val numberBadge = TextView(this).apply {
            text = "  ${index + 1}  "
            setBackgroundColor(ContextCompat.getColor(this@QuizActivity, R.color.purple_primary))
            setTextColor(ContextCompat.getColor(this@QuizActivity, R.color.white))
            textSize = 11f
            typeface = Typeface.DEFAULT_BOLD
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { rightMargin = dpToPx(10) }
            layoutParams = lp
            setPadding(dpToPx(8), dpToPx(4), dpToPx(8), dpToPx(4))
        }

        val questionLabel = TextView(this).apply {
            text = "Pregunta ${index + 1} de ${questions.size}"
            setTextColor(ContextCompat.getColor(this@QuizActivity, R.color.text_hint))
            textSize = 12f
        }

        badgeLayout.addView(numberBadge)
        badgeLayout.addView(questionLabel)

        // Texto de la pregunta
        val questionText = TextView(this).apply {
            text = question.text
            setTextColor(ContextCompat.getColor(this@QuizActivity, R.color.text_primary))
            textSize = 16f
            typeface = Typeface.DEFAULT_BOLD
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { bottomMargin = dpToPx(16) }
            layoutParams = lp
        }

        val radioGroup = RadioGroup(this).apply {
            orientation = RadioGroup.VERTICAL
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams = lp
        }

        // Opciones
        question.options.forEachIndexed { optionIndex, optionText ->
            val optionContainer = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                background = ContextCompat.getDrawable(this@QuizActivity, R.drawable.option_selector)
                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { bottomMargin = dpToPx(8) }
                layoutParams = lp
                setPadding(dpToPx(16), dpToPx(14), dpToPx(16), dpToPx(14))
                isClickable = true
                isFocusable = true
            }

            val radioBtn = RadioButton(this).apply {
                id = View.generateViewId()
                tag = optionIndex
                buttonTintList = ContextCompat.getColorStateList(this@QuizActivity, R.color.radio_tint)
                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { rightMargin = dpToPx(12) }
                layoutParams = lp
            }

            val optionLabel = TextView(this).apply {
                text = optionText
                setTextColor(ContextCompat.getColor(this@QuizActivity, R.color.text_primary))
                textSize = 14f
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
            }

            optionContainer.addView(radioBtn)
            optionContainer.addView(optionLabel)
            radioGroup.addView(optionContainer)


            optionContainer.setOnClickListener {
                radioBtn.isChecked = true
            }

            radioBtn.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {

                    for (i in 0 until radioGroup.childCount) {
                        val child = radioGroup.getChildAt(i) as? LinearLayout
                        val childRadio = child?.getChildAt(0) as? RadioButton
                        if (childRadio != radioBtn) {
                            childRadio?.isChecked = false
                        }

                        updateOptionAppearance(child, childRadio?.isChecked == true)
                    }
                    userAnswers[index] = optionIndex
                    updateOptionAppearance(optionContainer, true)
                    updateProgress()
                    card.tag = "answered"
                }
            }
        }

        radioGroups.add(radioGroup)

        card.addView(badgeLayout)
        card.addView(questionText)
        card.addView(radioGroup)

        return card
    }

    private fun updateOptionAppearance(container: LinearLayout?, isSelected: Boolean) {
        container ?: return
        if (isSelected) {
            container.background = ContextCompat.getDrawable(this, R.drawable.option_selected)
        } else {
            container.background = ContextCompat.getDrawable(this, R.drawable.option_selector)
        }
    }

    private fun updateProgress() {
        val answered = userAnswers.size
        val total = questions.size
        val percent = if (total > 0) (answered * 100 / total) else 0

        tvProgressCount.text = "$answered / $total respondidas"
        progressBar.progress = percent
    }

    private fun setupButtons() {
        btnSubmit.setOnClickListener {
            it.animate().scaleX(0.95f).scaleY(0.95f).setDuration(80).withEndAction {
                it.animate().scaleX(1f).scaleY(1f).setDuration(80).start()
                handleSubmit()
            }.start()
        }

        btnRestart.setOnClickListener {
            it.animate().scaleX(0.95f).scaleY(0.95f).setDuration(80).withEndAction {
                it.animate().scaleX(1f).scaleY(1f).setDuration(80).start()
                confirmRestart()
            }.start()
        }
    }

    private fun handleSubmit() {
        val total = questions.size
        val pendingNumbers = mutableListOf<Int>()

        for (i in 0 until total) {
            if (!userAnswers.containsKey(i)) {
                pendingNumbers.add(i + 1)
            }
        }

        if (pendingNumbers.isNotEmpty()) {

            val pendingList = pendingNumbers.joinToString(", ") { "Pregunta $it" }
            AlertDialog.Builder(this)
                .setTitle("⚠️ Preguntas pendientes")
                .setMessage(
                    "Debes responder todas las preguntas antes de enviar.\n\n" +
                    "Sin responder:\n• ${pendingNumbers.joinToString("\n• ") { "Pregunta $it" }}"
                )
                .setPositiveButton("Entendido") { dialog, _ ->
                    dialog.dismiss()
                    // Scroll a la primera pregunta pendiente
                    scrollToQuestion(pendingNumbers.first() - 1)
                }
                .show()
        } else {
            // Calcular puntaje
            var correct = 0
            questions.forEachIndexed { index, question ->
                val userAnswer = userAnswers[index] ?: -1
                if (userAnswer == question.correctIndex) correct++
            }
            launchResult(correct, total)
        }
    }

    private fun scrollToQuestion(questionIndex: Int) {
        val scrollView = findViewById<ScrollView>(R.id.scrollView)
        val card = questionsContainer.getChildAt(questionIndex)
        card?.let {
            scrollView.post {
                scrollView.smoothScrollTo(0, it.top)
            }
        }
    }

    private fun confirmRestart() {
        AlertDialog.Builder(this)
            .setTitle("🔄 ¿Reiniciar quiz?")
            .setMessage("Se borrarán todas tus respuestas. ¿Deseas continuar?")
            .setPositiveButton("Sí, reiniciar") { dialog, _ ->
                dialog.dismiss()
                restartQuiz()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun restartQuiz() {

        userAnswers.clear()

        for (radioGroup in radioGroups) {
            for (i in 0 until radioGroup.childCount) {
                val optionContainer = radioGroup.getChildAt(i) as? LinearLayout
                val radioBtn = optionContainer?.getChildAt(0) as? RadioButton
                radioBtn?.isChecked = false
                optionContainer?.background = ContextCompat.getDrawable(this, R.drawable.option_selector)
            }
        }


        updateProgress()

        val scrollView = findViewById<ScrollView>(R.id.scrollView)
        scrollView.smoothScrollTo(0, 0)
    }

    private fun launchResult(correct: Int, total: Int) {
        val answersArray = IntArray(total) { i -> userAnswers[i] ?: -1 }

        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra(ResultActivity.EXTRA_SCORE, correct)
            putExtra(ResultActivity.EXTRA_TOTAL, total)
            putExtra(ResultActivity.EXTRA_CATEGORY, category)
            putExtra(ResultActivity.EXTRA_DIFFICULTY, difficulty)
            putExtra(ResultActivity.EXTRA_USER_ANSWERS, answersArray)
        }
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
}
