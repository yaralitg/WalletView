package com.walletview.app

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.walletview.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // categorias y gastos de ejemplo
    private val categorias = listOf("Comida", "Transporte", "Renta", "Ocio", "Servicios")
    private val gastosOctubre = listOf(3500f, 1200f, 5000f, 900f, 1500f)
    private val gastosNoviembre = listOf(4100f, 950f, 5000f, 1600f, 1400f)

    private var esOctubre = true

    // colores pastel para las graficas
    private val colores = listOf(
        Color.parseColor("#CDB4DB"),
        Color.parseColor("#FFC8DD"),
        Color.parseColor("#FFAFCC"),
        Color.parseColor("#BDE0FE"),
        Color.parseColor("#A2D2FF")
    )
    private val colorTexto = Color.parseColor("#4A3F5C")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actualizar()

        binding.btnMes.setOnClickListener {
            esOctubre = !esOctubre
            actualizar()
        }
    }

    private fun actualizar() {
        val gastos = if (esOctubre) gastosOctubre else gastosNoviembre
        val mes = if (esOctubre) "Octubre" else "Noviembre"

        binding.tvMes.text = "Gastos de $mes"
        binding.tvTotal.text = "Total: $${gastos.sum().toInt()}"

        dibujarPastel(gastos, mes)
        dibujarBarras(gastos)
    }

    private fun dibujarPastel(gastos: List<Float>, mes: String) {
        val entradas = ArrayList<PieEntry>()
        for (i in gastos.indices) {
            entradas.add(PieEntry(gastos[i], categorias[i]))
        }

        val set = PieDataSet(entradas, "")
        set.colors = colores
        set.sliceSpace = 3f
        set.valueTextSize = 13f
        set.valueTextColor = colorTexto

        val pie = binding.pieChart
        pie.data = PieData(set).apply { setValueFormatter(PercentFormatter(pie)) }
        pie.setUsePercentValues(true)
        pie.setDrawEntryLabels(false)
        pie.description.isEnabled = false
        pie.legend.textColor = colorTexto
        pie.setHoleColor(Color.WHITE)
        pie.centerText = mes
        pie.setCenterTextColor(colorTexto)
        pie.setCenterTextSize(16f)
        pie.animateY(800)
        pie.invalidate()
    }

    private fun dibujarBarras(gastos: List<Float>) {
        val entradas = ArrayList<BarEntry>()
        for (i in gastos.indices) {
            entradas.add(BarEntry(i.toFloat(), gastos[i]))
        }

        val set = BarDataSet(entradas, "Gastos")
        set.colors = colores
        set.valueTextColor = colorTexto
        set.valueTextSize = 11f

        val barras = binding.barChart
        barras.data = BarData(set).apply { barWidth = 0.6f }
        barras.description.isEnabled = false
        barras.legend.isEnabled = false
        barras.axisRight.isEnabled = false
        barras.axisLeft.textColor = colorTexto
        barras.axisLeft.axisMinimum = 0f
        barras.axisLeft.gridColor = Color.parseColor("#EEE6F5")

        val x = barras.xAxis
        x.position = XAxis.XAxisPosition.BOTTOM
        x.valueFormatter = IndexAxisValueFormatter(categorias)
        x.granularity = 1f
        x.textColor = colorTexto
        x.textSize = 10f
        x.setDrawGridLines(false)

        barras.animateY(800)
        barras.invalidate()
    }
}
