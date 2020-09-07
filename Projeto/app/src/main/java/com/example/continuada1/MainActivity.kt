package com.example.continuada1

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun verificarCampos(v: View){
        if (et_inicial.text.length == 0 || et_mensal.text.length == 0 || et_periodo.text.length == 0 || et_desejado.text.length == 0 || et_taxa.text.length == 0 ){
            tv_resultado.text = "Insira valores válidos a todos os campos"
            tv_resultado.setTextColor(Color.MAGENTA)
        } else {
            calcularRendimento(v)
        }

    }

    fun calcularRendimento(v: View) {
        val aporteInicial = et_inicial.text.toString().toDouble()
        val aporteMensal = et_mensal.text.toString().toDouble()
        val periodo = et_periodo.text.toString().toInt()
        val desejado = et_desejado.text.toString().toDouble()
        val taxa = et_taxa.text.toString().toLowerCase()

        fun escolhaTaxa(v: View): Double {

            when (taxa) {
                "selic" -> return 0.02
                "cdi" -> return 0.0384
                "ipca" -> return 0.0231
                else -> return -1.0
            }
        }

        val valorTaxa = escolhaTaxa(v)

        if (valorTaxa == -1.0) {
            tv_resultado.text = "Insira uma taxa válida"
            tv_resultado.setTextColor(Color.MAGENTA)
        } else if (aporteInicial < 0 || aporteMensal < 0) {
            tv_resultado.text = "Insira um valor válido no aporte!"
        } else {
            if (periodo >= 24) {
                var lucro = 0.0
                var resultado = 0.0
                var periodo_alteravel = periodo

                while (periodo_alteravel !== 0) {
                    if (periodo_alteravel == periodo) {
                        resultado += ((aporteInicial + aporteMensal) + lucro) * (1.0 + valorTaxa)
                        lucro += ((aporteInicial + aporteMensal) + lucro) * valorTaxa
                        periodo_alteravel -= 1
                    } else {
                        resultado += (aporteMensal + lucro) * (1.0 + valorTaxa)
                        lucro += (aporteMensal + lucro) * valorTaxa
                        periodo_alteravel -= 1
                    }
                }

                if (resultado < desejado) {
                    tv_resultado.text =
                        "Valor adquirido em ${periodo} meses com a taxa ${taxa} será R$ ${String.format("%.2f",resultado)} \n Você NÃO atingirá o valor desejado!"
                    tv_resultado.setTextColor(Color.RED)
                } else {
                    tv_resultado.text =
                        "Valor adquirido em ${periodo} meses com a taxa ${taxa} será R$ ${String.format("%.2f",resultado)} \n Você atingirá o valor desejado, com sucesso!"
                    tv_resultado.setTextColor(Color.GREEN)
                }

            } else {
                tv_resultado.text = "Insira valores válidos para o período! Minimo de 24 meses. "
                tv_resultado.setTextColor(Color.MAGENTA)
            }

        }
    }

}