package com.example.desafio2

import android.text.method.LinkMovementMethod
import android.util.Patterns
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
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

class Registro : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)

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

        val etGmail = findViewById<EditText>(R.id.etGmail)
        val etContraseña = findViewById<EditText>(R.id.etContraseña)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
        val tvIniciarSesion = findViewById<TextView>(R.id.tvIniciarSesion)

        btnGuardar.setOnClickListener {

            val correo = etGmail.text.toString().trim()
            val contraseña = etContraseña.text.toString().trim()

            if (correo.isEmpty()) {
                etGmail.error = "Ingresa tu correo"
                etGmail.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                etGmail.error = "Ingresa un correo válido"
                etGmail.requestFocus()
                return@setOnClickListener
            }

            // Validar contraseña vacía
            if (contraseña.isEmpty()) {
                etContraseña.error = "Ingresa una contraseña"
                etContraseña.requestFocus()
                return@setOnClickListener
            }

            if (contraseña.length < 6) {
                etContraseña.error = "La contraseña debe tener al menos 6 caracteres"
                etContraseña.requestFocus()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(correo, contraseña)
                .addOnCompleteListener(this) { task ->

                    if (task.isSuccessful) {

                        Toast.makeText(
                            this,
                            "Cuenta creada correctamente",
                            Toast.LENGTH_SHORT
                        ).show()



                        val intent = Intent(
                            this,
                            IniciarSesion::class.java
                        )

                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK

                        startActivity(intent)
                        finish()


                    } else {

                        val mensaje = when {
                            task.exception?.message?.contains(
                                "email address is already in use",
                                ignoreCase = true
                            ) == true ->
                                "Este correo ya está registrado"

                            task.exception?.message?.contains(
                                "badly formatted",
                                ignoreCase = true
                            ) == true ->
                                "El correo no tiene un formato válido"

                            else ->
                                "No se pudo crear la cuenta. Intenta nuevamente."
                        }

                        Toast.makeText(
                            this,
                            mensaje,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }

        val texto = "¿Ya tienes una cuenta? Inicia sesión"
        val spannable = SpannableString(texto)

        val inicio = texto.indexOf("Inicia sesión")
        val fin = inicio + "Inicia sesión".length

        spannable.setSpan(
            UnderlineSpan(),
            inicio,
            fin,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            ForegroundColorSpan(
                resources.getColor(android.R.color.holo_blue_light, theme)
            ),
            inicio,
            fin,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            object : ClickableSpan() {
                override fun onClick(widget: View) {

                    val intent = Intent(
                        this@Registro,
                        IniciarSesion::class.java
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
