package com.example.desafio2

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class ResultActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_SCORE = "extra_score"
        const val EXTRA_TOTAL = "extra_total"
        const val EXTRA_CATEGORY = "extra_category"
        const val EXTRA_DIFFICULTY = "extra_difficulty"
        const val EXTRA_USER_ANSWERS = "extra_user_answers"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val score       = intent.getIntExtra(EXTRA_SCORE, 0)
        val total       = intent.getIntExtra(EXTRA_TOTAL, 5)
        val category    = intent.getStringExtra(EXTRA_CATEGORY) ?: "Ciencias"
        val difficulty  = intent.getStringExtra(EXTRA_DIFFICULTY) ?: "Fácil"
        val userAnswers = intent.getIntArrayExtra(EXTRA_USER_ANSWERS) ?: IntArray(total) { -1 }

        val questions = QuizRepository.getQuestions(category, difficulty)

        displayHeader(score, total, category, difficulty)
        displayFeedbackMessage(score, total, difficulty)
        buildAnswerReview(questions, userAnswers)
        setupButtons(category, difficulty)
    }


    private fun displayHeader(score: Int, total: Int, category: String, difficulty: String) {
        val percent = if (total > 0) (score * 100 / total) else 0

        val (emoji, title, scoreColor) = when {
            percent == 100 && difficulty == "Difícil" ->
                Triple("🔥", "¡Imbatible!", R.color.accent_gold)
            percent == 100 ->
                Triple("🏆", "¡Perfecto!", R.color.accent_gold)
            percent >= 80 ->
                Triple("🎉", "¡Excelente!", R.color.success_green)
            percent >= 60 ->
                Triple("👍", "¡Muy bien!", R.color.accent_cyan)
            percent >= 40 ->
                Triple("📚", "¡Sigue practicando!", R.color.warning_orange)
            else ->
                Triple("💪", "¡Inténtalo de nuevo!", R.color.error_red)
        }

        findViewById<TextView>(R.id.tvResultEmoji).text = emoji
        findViewById<TextView>(R.id.tvResultTitle).text = title
        findViewById<TextView>(R.id.tvResultMeta).text = "$category  ·  $difficulty"

        // Frase completa de puntaje
        val scorePhrase = "Obtuviste $score de $total respuestas correctas"
        findViewById<TextView>(R.id.tvScorePhrase).text = scorePhrase

        // Número grande
        val tvScore = findViewById<TextView>(R.id.tvScore)
        tvScore.text = "$score / $total"
        tvScore.setTextColor(ContextCompat.getColor(this, scoreColor))

        // Porcentaje
        val tvPercentage = findViewById<TextView>(R.id.tvPercentage)
        tvPercentage.text = "$percent%"
        val percentColor = when {
            percent >= 80 -> R.color.success_green
            percent >= 60 -> R.color.accent_cyan
            percent >= 40 -> R.color.warning_orange
            else          -> R.color.error_red
        }
        tvPercentage.setTextColor(ContextCompat.getColor(this, percentColor))

        val progressResult = findViewById<ProgressBar>(R.id.progressResult)
        progressResult.max = 100
        progressResult.post { progressResult.progress = percent }
    }

    private fun displayFeedbackMessage(score: Int, total: Int, difficulty: String) {
        val message = getFeedbackMessage(score, total, difficulty)
        val tvFeedback = findViewById<TextView>(R.id.tvFeedbackMessage)
        tvFeedback.text = "\"$message\""

        val percent = if (total > 0) (score * 100 / total) else 0
        val msgColor = when {
            percent == 100 -> R.color.accent_gold
            percent >= 60  -> R.color.success_green
            percent >= 40  -> R.color.warning_orange
            else           -> R.color.error_red
        }
        tvFeedback.setTextColor(ContextCompat.getColor(this, msgColor))
    }

    private fun getFeedbackMessage(score: Int, total: Int, difficulty: String): String {
        val percent = if (total > 0) (score * 100 / total) else 0
        return when {

            score == total && difficulty == "Difícil" ->
                "¡Eres demasiado bueno, deberias de dedicarte a esto !"

            score == total ->
                "¡Eres un crack total, sin dudas!"

            percent >= 80 ->
                "¡Vas que te mereces un dulce!"

            percent >= 60 ->
                "¡Vas bien, pero el dulce se quedó a medias!"

            percent >= 40 ->
                "Hay que estudiar más antes del próximo dulce..."

            percent >= 20 ->
                "¡no te ganas nada, sigue intentándolo!"

            else ->
                "¡A estudiar que ahora no tienes ni las por participar!"
        }
    }



    private fun buildAnswerReview(questions: List<Question>, userAnswers: IntArray) {
        val container = findViewById<LinearLayout>(R.id.reviewContainer)
        container.removeAllViews()

        questions.forEachIndexed { index, question ->
            val userAnswer = if (index < userAnswers.size) userAnswers[index] else -1
            val isCorrect = userAnswer == question.correctIndex

            val card = buildReviewCard(index, question, userAnswer, isCorrect)
            container.addView(card)
        }
    }

    private fun buildReviewCard(
        index: Int,
        question: Question,
        userAnswer: Int,
        isCorrect: Boolean
    ): LinearLayout {

        val card = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            background = ContextCompat.getDrawable(
                this@ResultActivity,
                if (isCorrect) R.drawable.card_correct else R.drawable.card_wrong
            )
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { bottomMargin = dpToPx(12) }
            layoutParams = lp
            setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16))
        }

        // Fila: número + ícono correcto/incorrecto
        val headerRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { bottomMargin = dpToPx(8) }
            layoutParams = lp
        }

        val tvNumber = TextView(this).apply {
            text = "Pregunta ${index + 1}"
            setTextColor(ContextCompat.getColor(this@ResultActivity, R.color.text_hint))
            textSize = 11f
            typeface = Typeface.DEFAULT_BOLD
            letterSpacing = 0.1f
            val lp = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            layoutParams = lp
        }

        val tvStatus = TextView(this).apply {
            text = if (isCorrect) "✓ Correcto" else "✗ Incorrecto"
            textSize = 12f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(
                ContextCompat.getColor(
                    this@ResultActivity,
                    if (isCorrect) R.color.success_green else R.color.error_red
                )
            )
        }

        headerRow.addView(tvNumber)
        headerRow.addView(tvStatus)

        // Texto de la pregunta
        val tvQuestion = TextView(this).apply {
            text = question.text
            setTextColor(ContextCompat.getColor(this@ResultActivity, R.color.text_primary))
            textSize = 14f
            typeface = Typeface.DEFAULT_BOLD
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { bottomMargin = dpToPx(12) }
            layoutParams = lp
        }


        val divider = android.view.View(this).apply {
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(1)
            ).apply { bottomMargin = dpToPx(12) }
            layoutParams = lp
            setBackgroundColor(ContextCompat.getColor(this@ResultActivity, R.color.bg_surface))
        }

        card.addView(headerRow)
        card.addView(tvQuestion)
        card.addView(divider)


        question.options.forEachIndexed { optIndex, optText ->
            val isUserPick   = optIndex == userAnswer
            val isCorrectOpt = optIndex == question.correctIndex


            if (!isUserPick && !isCorrectOpt) return@forEachIndexed

            val optRow = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                val bg = when {
                    isCorrectOpt -> R.drawable.option_correct
                    isUserPick   -> R.drawable.option_incorrect
                    else         -> R.drawable.option_selector
                }
                background = ContextCompat.getDrawable(this@ResultActivity, bg)
                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { bottomMargin = dpToPx(6) }
                layoutParams = lp
                setPadding(dpToPx(12), dpToPx(10), dpToPx(12), dpToPx(10))
            }

            val icon = TextView(this).apply {
                text = when {
                    isCorrectOpt -> "✓"
                    isUserPick && !isCorrect -> "✗"
                    else -> "•"
                }
                textSize = 14f
                typeface = Typeface.DEFAULT_BOLD
                setTextColor(
                    ContextCompat.getColor(
                        this@ResultActivity,
                        if (isCorrectOpt) R.color.success_green else R.color.error_red
                    )
                )
                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { rightMargin = dpToPx(10) }
                layoutParams = lp
            }

            val label = TextView(this).apply {
                text = buildString {
                    append(optText)
                    if (isCorrectOpt) append("  ← Correcta")
                    if (isUserPick && !isCorrect) append("  ← Tu respuesta")
                }
                textSize = 13f
                setTextColor(
                    ContextCompat.getColor(
                        this@ResultActivity,
                        if (isCorrectOpt) R.color.success_green
                        else if (isUserPick) R.color.error_red
                        else R.color.text_secondary
                    )
                )
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            }

            optRow.addView(icon)
            optRow.addView(label)
            card.addView(optRow)
        }

        return card
    }



    private fun setupButtons(category: String, difficulty: String) {
        val btnRetry    = findViewById<Button>(R.id.btnRetry)
        val btnBackHome = findViewById<Button>(R.id.btnBackHome)

        btnRetry.setOnClickListener {
            it.animate().scaleX(0.95f).scaleY(0.95f).setDuration(80).withEndAction {
                it.animate().scaleX(1f).scaleY(1f).setDuration(80).start()
                val intent = Intent(this, QuizActivity::class.java).apply {
                    putExtra(QuizActivity.EXTRA_CATEGORY, category)
                    putExtra(QuizActivity.EXTRA_DIFFICULTY, difficulty)
                }
                startActivity(intent)
                finish()
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }.start()
        }

        btnBackHome.setOnClickListener {
            it.animate().scaleX(0.95f).scaleY(0.95f).setDuration(80).withEndAction {
                it.animate().scaleX(1f).scaleY(1f).setDuration(80).start()
                val intent = Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                }
                startActivity(intent)
                finish()
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }.start()
        }
    }

    private fun dpToPx(dp: Int): Int =
        (dp * resources.displayMetrics.density).toInt()
}
