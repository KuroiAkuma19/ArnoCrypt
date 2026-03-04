package com.example.arnocrypt

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val morseMap = mapOf(
        'A' to ".-", 'B' to "-...", 'C' to "-.-.", 'D' to "-..", 'E' to ".", 'F' to "..-.",
        'G' to "--.", 'H' to "....", 'I' to "..", 'J' to ".---", 'K' to "-.-", 'L' to ".-..",
        'M' to "--", 'N' to "-.", 'O' to "---", 'P' to ".--.", 'Q' to "--.-", 'R' to ".-.",
        'S' to "...", 'T' to "-", 'U' to "..-", 'V' to "...-", 'W' to ".--", 'X' to "-..-",
        'Y' to "-.--", 'Z' to "--..", '1' to ".----", '2' to "..---", '3' to "...--",
        '4' to "....-", '5' to ".....", '6' to "-....", '7' to "--...", '8' to "---..",
        '9' to "----.", '0' to "-----", ' ' to "/"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etInput = findViewById<EditText>(R.id.etInput)
        val etKey = findViewById<EditText>(R.id.etKey)
        val tvResult = findViewById<TextView>(R.id.tvResult)
        val spinner = findViewById<Spinner>(R.id.cipherSpinner)

        val options = arrayOf("Additive Cipher", "Morse Code", "Rail Fence", "Playfair")
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, options)

        findViewById<Button>(R.id.btnEncrypt).setOnClickListener {
            process(etInput.text.toString(), etKey.text.toString(), spinner.selectedItem.toString(), true, tvResult)
        }

        findViewById<Button>(R.id.btnDecrypt).setOnClickListener {
            process(etInput.text.toString(), etKey.text.toString(), spinner.selectedItem.toString(), false, tvResult)
        }

        findViewById<Button>(R.id.btnClear).setOnClickListener {
            etInput.text.clear(); etKey.text.clear(); tvResult.text = "System cleared."
        }
    }

    private fun process(text: String, key: String, method: String, encrypt: Boolean, resultView: TextView) {
        if (text.isEmpty()) { resultView.text = "Error: Input required"; return }

        resultView.text = when (method) {
            "Additive Cipher" -> additive(text, if (encrypt) key.toIntOrNull() ?: 0 else -(key.toIntOrNull() ?: 0))
            "Morse Code" -> if (encrypt) toMorse(text) else fromMorse(text)
            "Rail Fence" -> if (encrypt) railEncrypt(text, key.toIntOrNull() ?: 2) else railDecrypt(text, key.toIntOrNull() ?: 2)
            "Playfair" -> playfair(text, key, encrypt)
            else -> ""
        }
    }

    private fun additive(t: String, s: Int): String {
        val shift = ((s % 26) + 26) % 26
        return t.uppercase().map { if (it in 'A'..'Z') ((it - 'A' + shift) % 26 + 'A'.toInt()).toChar() else it }.joinToString("")
    }

    private fun toMorse(t: String) = t.uppercase().map { morseMap[it] ?: "" }.filter { it.isNotBlank() }.joinToString(" ")
    private fun fromMorse(m: String) = m.trim().split(" ").map { code -> morseMap.entries.find { it.value == code }?.key ?: "" }.joinToString("")

    private fun railEncrypt(t: String, r: Int): String {
        if (r <= 1) return t
        val fence = Array(r) { StringBuilder() }; var cur = 0; var d = 1
        for (c in t) { fence[cur].append(c); if (cur == 0) d = 1 else if (cur == r - 1) d = -1; cur += d }
        return fence.joinToString("")
    }

    private fun railDecrypt(c: String, r: Int): String {
        if (r <= 1) return c
        val mark = Array(r) { CharArray(c.length) { '\u0000' } }; var row = 0; var d = 1
        for (i in c.indices) { mark[row][i] = '*'; if (row == 0) d = 1 else if (row == r - 1) d = -1; row += d }
        var idx = 0
        for (i in 0 until r) for (j in c.indices) if (mark[i][j] == '*' && idx < c.length) mark[i][j] = c[idx++]
        val res = StringBuilder(); row = 0; d = 1
        for (i in c.indices) { res.append(mark[row][i]); if (row == 0) d = 1 else if (row == r - 1) d = -1; row += d }
        return res.toString()
    }

    private fun playfair(t: String, k: String, enc: Boolean): String {
        val matrix = (k.uppercase().replace("J", "I") + "ABCDEFGHIKLMNOPQRSTUVWXYZ").filter { it in 'A'..'Z' }.toList().distinct().joinToString("")
        val input = t.uppercase().replace("J", "I").filter { it in 'A'..'Z' }.toMutableList()
        var i = 0; while (i < input.size) { if (i + 1 < input.size && input[i] == input[i+1]) input.add(i+1, 'X'); i += 2 }
        if (input.size % 2 != 0) input.add('X')
        val res = StringBuilder(); val s = if (enc) 1 else 4
        for (j in input.indices step 2) {
            val a = input[j]; val b = input[j+1]; val i1 = matrix.indexOf(a); val i2 = matrix.indexOf(b)
            val r1 = i1/5; val c1 = i1%5; val r2 = i2/5; val c2 = i2%5
            when {
                r1 == r2 -> res.append(matrix[r1*5 + (c1+s)%5]).append(matrix[r2*5 + (c2+s)%5])
                c1 == c2 -> res.append(matrix[((r1+s)%5)*5 + c1]).append(matrix[((r2+s)%5)*5 + c2])
                else -> res.append(matrix[r1*5 + c2]).append(matrix[r2*5 + c1])
            }
        }
        return res.toString()
    }
}