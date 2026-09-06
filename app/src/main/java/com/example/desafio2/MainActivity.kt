package com.example.desafio2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.collections.get

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

data class Question(
    val id: Int,
    val text: String,
    val options: List<String>,
    val correctIndex: Int,
    val category: String,
    val difficulty: String
)

object QuizRepository {

    private val allQuestions = listOf(

        // =====================================================
        // CULTURA GENERAL - FÁCIL
        // =====================================================

        Question(
            1,
            "¿Cuál es la capital de Francia?",
            listOf("Madrid", "París", "Roma", "Berlín"),
            1,
            "Cultura General",
            "Fácil"
        ),

        Question(
            2,
            "¿Cuál es el océano más grande del planeta?",
            listOf("Atlántico", "Índico", "Pacífico", "Ártico"),
            2,
            "Cultura General",
            "Fácil"
        ),

        Question(
            3,
            "¿Cuántos días tiene una semana?",
            listOf("5", "6", "7", "8"),
            2,
            "Cultura General",
            "Fácil"
        ),

        Question(
            4,
            "¿Cuál es el idioma más hablado en Brasil?",
            listOf("Español", "Portugués", "Inglés", "Francés"),
            1,
            "Cultura General",
            "Fácil"
        ),

        Question(
            5,
            "¿Cuál es el continente donde se encuentra Egipto?",
            listOf("Asia", "Europa", "África", "Oceanía"),
            2,
            "Cultura General",
            "Fácil"
        ),

        // =====================================================
        // CULTURA GENERAL - MEDIO
        // =====================================================

        Question(
            6,
            "¿Cuál es el país más grande del mundo por superficie?",
            listOf("Canadá", "China", "Rusia", "Estados Unidos"),
            2,
            "Cultura General",
            "Medio"
        ),

        Question(
            7,
            "¿Cuál es la moneda oficial de Japón?",
            listOf("Won", "Yuan", "Yen", "Ringgit"),
            2,
            "Cultura General",
            "Medio"
        ),

        Question(
            8,
            "¿Cuál es el río más largo de Sudamérica?",
            listOf("Amazonas", "Orinoco", "Paraná", "Magdalena"),
            0,
            "Cultura General",
            "Medio"
        ),

        Question(
            9,
            "¿En qué país se encuentra la Torre Eiffel?",
            listOf("Italia", "Francia", "España", "Bélgica"),
            1,
            "Cultura General",
            "Medio"
        ),

        Question(
            10,
            "¿Cuál es el planeta conocido como el planeta rojo?",
            listOf("Venus", "Júpiter", "Marte", "Saturno"),
            2,
            "Cultura General",
            "Medio"
        ),

        // =====================================================
        // CULTURA GENERAL - DIFÍCIL
        // =====================================================

        Question(
            11,
            "¿Cuál es el elemento químico cuyo símbolo es W?",
            listOf("Wolframio", "Tungsteno", "Titanio", "Vanadio"),
            1,
            "Cultura General",
            "Difícil"
        ),

        Question(
            12,
            "¿Quién escribió la novela Don Quijote de la Mancha?",
            listOf(
                "Miguel de Cervantes",
                "Gabriel García Márquez",
                "Lope de Vega",
                "Federico García Lorca"
            ),
            0,
            "Cultura General",
            "Difícil"
        ),

        Question(
            13,
            "¿Cuál es la capital de Mongolia?",
            listOf("Astana", "Ulán Bator", "Taskent", "Biskek"),
            1,
            "Cultura General",
            "Difícil"
        ),

        Question(
            14,
            "¿Qué filósofo fue maestro de Alejandro Magno?",
            listOf("Sócrates", "Platón", "Aristóteles", "Epicuro"),
            2,
            "Cultura General",
            "Difícil"
        ),

        Question(
            15,
            "¿Cuál es el idioma oficial de Austria?",
            listOf("Alemán", "Francés", "Italiano", "Holandés"),
            0,
            "Cultura General",
            "Difícil"
        ),

        // CIENCIAS - FÁCIL
        Question(1, "¿Cuál es el planeta más cercano al Sol?",
            listOf("Venus", "Mercurio", "Marte", "Tierra"), 1, "Ciencias", "Fácil"),
        Question(2, "¿Cuántos huesos tiene el cuerpo humano adulto?",
            listOf("206", "208", "198", "215"), 0, "Ciencias", "Fácil"),
        Question(3, "¿Qué gas es esencial para la respiración humana?",
            listOf("Dióxido de carbono", "Nitrógeno", "Oxígeno", "Helio"), 2, "Ciencias", "Fácil"),
        Question(4, "¿Cuál es el símbolo químico del agua?",
            listOf("O2", "CO2", "H2O", "NaCl"), 2, "Ciencias", "Fácil"),
        Question(5, "¿Qué órgano bombea sangre por el cuerpo?",
            listOf("Pulmones", "Riñones", "Hígado", "Corazón"), 3, "Ciencias", "Fácil"),

        // CIENCIAS - MEDIO
        Question(6, "¿Cuál es la velocidad de la luz en el vacío?",
            listOf("300,000 km/s", "150,000 km/s", "299,792 km/s", "500,000 km/s"), 2, "Ciencias", "Medio"),
        Question(7, "¿Qué partícula subatómica tiene carga negativa?",
            listOf("Protón", "Neutrón", "Electrón", "Fotón"), 2, "Ciencias", "Medio"),
        Question(8, "¿Cuál es la fórmula de la glucosa?",
            listOf("C6H12O6", "C12H22O11", "CH4", "C2H5OH"), 0, "Ciencias", "Medio"),
        Question(9, "¿En qué capa de la atmósfera ocurren los fenómenos meteorológicos?",
            listOf("Estratosfera", "Mesosfera", "Troposfera", "Termosfera"), 2, "Ciencias", "Medio"),
        Question(10, "¿Cuántos cromosomas tiene el ser humano?",
            listOf("23", "44", "46", "48"), 2, "Ciencias", "Medio"),

        //  CIENCIAS - DIFÍCIL
        Question(11, "¿Qué fenómeno describe la ecuación de Schrödinger?",
            listOf("Relatividad especial", "Mecánica cuántica", "Termodinámica", "Electromagnetismo"), 1, "Ciencias", "Difícil"),
        Question(12, "¿Cuál es el número de Avogadro?",
            listOf("6.022 × 10²³", "3.14 × 10¹⁰", "9.81 × 10²³", "1.602 × 10¹⁹"), 0, "Ciencias", "Difícil"),
        Question(13, "¿Qué es la constante de Planck?",
            listOf("6.626 × 10⁻³⁴ J·s", "1.38 × 10⁻²³ J/K", "3.0 × 10⁸ m/s", "9.81 m/s²"), 0, "Ciencias", "Difícil"),
        Question(14, "¿Cuál es el principio de Heisenberg?",
            listOf("Indeterminación", "Conservación de energía", "Relatividad", "Acción-reacción"), 0, "Ciencias", "Difícil"),
        Question(15, "¿Qué tipo de enlace forman los aminoácidos para crear proteínas?",
            listOf("Enlace iónico", "Enlace covalente", "Enlace peptídico", "Puente de hidrógeno"), 2, "Ciencias", "Difícil"),

        //  HISTORIA - FÁCIL
        Question(16, "¿En qué año llegó Cristóbal Colón a América?",
            listOf("1498", "1492", "1500", "1488"), 1, "Historia", "Fácil"),
        Question(17, "¿Quién fue el primer presidente de los Estados Unidos?",
            listOf("Abraham Lincoln", "Thomas Jefferson", "George Washington", "Benjamin Franklin"), 2, "Historia", "Fácil"),
        Question(18, "¿En qué ciudad se construyó el Coliseo Romano?",
            listOf("Atenas", "Cartago", "Roma", "Florencia"), 2, "Historia", "Fácil"),
        Question(19, "¿Qué muralla fue construida para proteger China de invasiones?",
            listOf("Muralla de Adriano", "Muralla China", "Muralla de Berlín", "Muralla de Troya"), 1, "Historia", "Fácil"),
        Question(20, "¿En qué año comenzó la Primera Guerra Mundial?",
            listOf("1918", "1910", "1914", "1916"), 2, "Historia", "Fácil"),

        // HISTORIA - MEDIO
        Question(21, "¿Quién fue el último faraón de Egipto?",
            listOf("Nefertiti", "Cleopatra VII", "Tutankamón", "Ramsés II"), 1, "Historia", "Medio"),
        Question(22, "¿En qué año cayó el Imperio Romano de Occidente?",
            listOf("410 d.C.", "476 d.C.", "395 d.C.", "529 d.C."), 1, "Historia", "Medio"),
        Question(23, "¿Qué tratado puso fin a la Primera Guerra Mundial?",
            listOf("Tratado de París", "Tratado de Versalles", "Tratado de Utrecht", "Tratado de Westfalia"), 1, "Historia", "Medio"),
        Question(24, "¿Quién lideró la Revolución Francesa?",
            listOf("Napoleón Bonaparte", "Robespierre", "Luis XVI", "Fue un movimiento popular"), 3, "Historia", "Medio"),
        Question(25, "¿En qué año se produjo la Revolución Rusa?",
            listOf("1905", "1914", "1917", "1921"), 2, "Historia", "Medio"),

        // HISTORIA - DIFÍCIL
        Question(26, "¿Quién escribió el Art de la Guerre, influyendo en la estrategia militar china?",
            listOf("Confucio", "Sun Tzu", "Laozi", "Mencio"), 1, "Historia", "Difícil"),
        Question(27, "¿En qué batalla Napoleón sufrió su primera gran derrota?",
            listOf("Waterloo", "Leipzig", "Trafalgar", "Austerlitz"), 1, "Historia", "Difícil"),
        Question(28, "¿Cuál fue la causa principal del colapso del Imperio Asirio?",
            listOf("Plagas", "Coalición de medos y babilonios", "Invasión persa", "Sequía"), 1, "Historia", "Difícil"),
        Question(29, "¿En qué año se firmó la Carta Magna?",
            listOf("1066", "1215", "1348", "1189"), 1, "Historia", "Difícil"),
        Question(30, "¿Qué civilización construyó Machu Picchu?",
            listOf("Azteca", "Maya", "Inca", "Tolteca"), 2, "Historia", "Difícil"),

        // TECNOLOGÍA - FÁCIL
        Question(31, "¿Qué significa CPU?",
            listOf("Control Panel Unit", "Central Processing Unit", "Computer Power Unit", "Core Processing Utility"), 1, "Tecnología", "Fácil"),
        Question(32, "¿Cuál es el lenguaje de programación creado por Sun Microsystems?",
            listOf("Python", "C++", "Java", "PHP"), 2, "Tecnología", "Fácil"),
        Question(33, "¿Qué hace un firewall?",
            listOf("Acelera internet", "Protege la red de accesos no autorizados", "Guarda archivos", "Crea redes WiFi"), 1, "Tecnología", "Fácil"),
        Question(34, "¿Qué significa HTML?",
            listOf("Hypertext Markup Language", "High Transfer Markup Link", "Hypertext Machine Learning", "Home Text Markup Language"), 0, "Tecnología", "Fácil"),
        Question(35, "¿Qué empresa desarrolló el sistema operativo Windows?",
            listOf("Apple", "Google", "Microsoft", "IBM"), 2, "Tecnología", "Fácil"),

        //  TECNOLOGÍA - MEDIO
        Question(36, "¿Cuál es la complejidad de un algoritmo de búsqueda binaria?",
            listOf("O(n)", "O(n²)", "O(log n)", "O(1)"), 2, "Tecnología", "Medio"),
        Question(37, "¿Qué protocolo se usa para enviar correos electrónicos?",
            listOf("HTTP", "FTP", "SMTP", "SSH"), 2, "Tecnología", "Medio"),
        Question(38, "¿Qué tipo de base de datos es MongoDB?",
            listOf("Relacional", "NoSQL", "Jerárquica", "En grafo"), 1, "Tecnología", "Medio"),
        Question(39, "¿Qué significa REST en desarrollo web?",
            listOf("Remote Execution Standard Transfer", "Representational State Transfer", "Real-time Server Template", "Resource Exchange Standard Tool"), 1, "Tecnología", "Medio"),
        Question(40, "¿Cuál de estos es un lenguaje de programación funcional?",
            listOf("Java", "C", "Haskell", "PHP"), 2, "Tecnología", "Medio"),

        // TECNOLOGÍA - DIFÍCIL
        Question(41, "¿Qué es el problema NP-completo más famoso?",
            listOf("Problema del viajante (TSP)", "Ordenamiento burbuja", "Búsqueda binaria", "Algoritmo de Dijkstra"), 0, "Tecnología", "Difícil"),
        Question(42, "¿Qué es una colisión de hash?",
            listOf("Error de memoria RAM", "Dos entradas producen el mismo hash", "Fallo en el disco duro", "Bucle infinito"), 1, "Tecnología", "Difícil"),
        Question(43, "¿Qué es el teorema CAP en sistemas distribuidos?",
            listOf("Consistencia, Disponibilidad, Tolerancia a particiones", "Caché, API, Protocolo", "Control, Acceso, Persistencia", "Concurrencia, Asincronía, Paralelismo"), 0, "Tecnología", "Difícil"),
        Question(44, "¿Qué algoritmo de cifrado simétrico es considerado más seguro?",
            listOf("DES", "MD5", "AES-256", "SHA-1"), 2, "Tecnología", "Difícil"),
        Question(45, "¿Qué es el problema de los filósofos comensales?",
            listOf("Problema de seguridad", "Problema clásico de sincronización de procesos", "Algoritmo de grafos", "Técnica de compresión"), 1, "Tecnología", "Difícil"),

        //DEPORTES - FÁCIL
        Question(46, "¿Cuántos jugadores tiene un equipo de fútbol?",
            listOf("9", "10", "11", "12"), 2, "Deportes", "Fácil"),
        Question(47, "¿En qué deporte se usa el término 'home run'?",
            listOf("Fútbol americano", "Béisbol", "Cricket", "Softball"), 1, "Deportes", "Fácil"),
        Question(48, "¿En qué país se originó el judo?",
            listOf("China", "Corea", "Japón", "Vietnam"), 2, "Deportes", "Fácil"),
        Question(49, "¿Cuántos sets se necesitan para ganar en tenis (Grand Slam masculino)?",
            listOf("2", "3", "4", "5"), 1, "Deportes", "Fácil"),
        Question(50, "¿Qué deporte se practica en Wimbledon?",
            listOf("Golf", "Tenis", "Críquet", "Polo"), 1, "Deportes", "Fácil"),

        // DEPORTES - MEDIO
        Question(51, "¿Cuántos metros mide una piscina olímpica?",
            listOf("25 m", "50 m", "75 m", "100 m"), 1, "Deportes", "Medio"),
        Question(52, "¿Cuántos jugadores forman un equipo de baloncesto en cancha?",
            listOf("4", "5", "6", "7"), 1, "Deportes", "Medio"),
        Question(53, "¿Qué país ha ganado más Copas del Mundo de fútbol?",
            listOf("Alemania", "Argentina", "Brasil", "Italia"), 2, "Deportes", "Medio"),
        Question(54, "¿En qué año se celebraron los primeros Juegos Olímpicos modernos?",
            listOf("1892", "1896", "1900", "1904"), 1, "Deportes", "Medio"),
        Question(55, "¿Qué altura tiene el arco de una portería de fútbol?",
            listOf("2.24 m", "2.44 m", "2.64 m", "2.14 m"), 1, "Deportes", "Medio"),

        // DEPORTES - DIFÍCIL
        Question(56, "¿Quién tiene el récord de más medallas olímpicas individuales?",
            listOf("Usain Bolt", "Michael Phelps", "Carl Lewis", "Mark Spitz"), 1, "Deportes", "Difícil"),
        Question(57, "¿En qué año el fútbol femenino se incluyó en los Juegos Olímpicos?",
            listOf("1984", "1992", "1996", "2000"), 2, "Deportes", "Difícil"),
        Question(58, "¿Cuántos metros recorre un maratón?",
            listOf("42,000 m", "42,195 m", "42,500 m", "41,950 m"), 1, "Deportes", "Difícil"),
        Question(59, "¿Qué equipo ganó la primera Copa del Mundo de la FIFA?",
            listOf("Argentina", "Brasil", "Uruguay", "Italia"), 2, "Deportes", "Difícil"),
        Question(60, "¿Qué significa 'Grand Slam' en golf?",
            listOf("Ganar los 4 torneos majors en un año calendario", "Ganar 3 torneos consecutivos", "Hacer un hoyo en uno", "Ganar más de 10 torneos"), 0, "Deportes", "Difícil")
    )

    fun getQuestions(category: String, difficulty: String): List<Question> {
        return allQuestions.filter { it.category == category && it.difficulty == difficulty }
    }

    val categories = listOf("Cultura General", "Ciencias", "Historia", "Tecnología", "Deportes")
    val difficulties = listOf("Fácil", "Medio", "Difícil")
}