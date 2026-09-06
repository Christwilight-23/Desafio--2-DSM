package com.example.desafio2

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class IniciarSesion : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_iniciar_sesion)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {

            val intent = Intent(this, Bienvenida::class.java)

            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
            finish()

            return
        }

        val esGmail = findViewById<EditText>(R.id.esGmail)
        val esContraseña = findViewById<EditText>(R.id.esContraseña)
        val btnIniciarSesion = findViewById<Button>(R.id.btnIniciarSesion)
        val tvIniciarSesion = findViewById<TextView>(R.id.tvIniciarSesion)

        btnIniciarSesion.setOnClickListener {

            val correo = esGmail.text.toString().trim()
            val contraseña = esContraseña.text.toString().trim()

            if (correo.isEmpty()) {
                esGmail.error = "Ingresa tu correo"
                esGmail.requestFocus()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                esGmail.error = "Ingresa un correo válido"
                esGmail.requestFocus()
                return@setOnClickListener
            }

            if (contraseña.isEmpty()) {
                esContraseña.error = "Ingresa tu contraseña"
                esContraseña.requestFocus()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(correo, contraseña)
                .addOnCompleteListener(this) { task ->

                    if (task.isSuccessful) {

                        Toast.makeText(
                            this,
                            "Inicio de sesión exitoso",
                            Toast.LENGTH_SHORT
                        ).show()


                        val intent = Intent(
                            this,
                            Bienvenida::class.java
                        )

                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK

                        startActivity(intent)
                        finish()

                    } else {

                        Toast.makeText(
                            this,
                            "Correo o contraseña incorrectos",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }

        val texto = "¿No tienes una cuenta? Regístrate"
        val spannable = SpannableString(texto)

        val inicio = texto.indexOf("Regístrate")
        val fin = inicio + "Regístrate".length

        spannable.setSpan(
            UnderlineSpan(),
            inicio,
            fin,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            ForegroundColorSpan(
                resources.getColor(
                    android.R.color.holo_blue_light,
                    theme
                )
            ),
            inicio,
            fin,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            object : ClickableSpan() {
                override fun onClick(widget: View) {

                    val intent = Intent(
                        this@IniciarSesion,
                        Registro::class.java
                    )

                    startActivity(intent)
                }
            },
            inicio,
            fin,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        tvIniciarSesion.text = spannable

        tvIniciarSesion.movementMethod =
            LinkMovementMethod.getInstance()
    }
}