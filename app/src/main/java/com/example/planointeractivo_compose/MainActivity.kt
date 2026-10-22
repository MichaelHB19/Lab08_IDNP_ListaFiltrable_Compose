package com.example.planointeractivo_compose

import android.graphics.Paint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.planointeractivo_compose.ui.theme.PlanoInteractivo_ComposeTheme

data class Marcador(
    val offset: Offset,
    val radio: Float,
    val titulo: String,
    val descripcion: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlanoInteractivo_ComposeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    var mostrarPlano by remember { mutableStateOf(false) }

                    if (!mostrarPlano) {
                        InicioCardScreen(
                            onVerPlanoClick = { mostrarPlano = true }
                        )
                    } else {
                        Column(modifier = Modifier.fillMaxSize()) {
                            OutlinedButton(
                                onClick = { mostrarPlano = false },
                                modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                            ) {
                                Text("← Volver a Edificación")
                            }
                            PlanoAmbiente()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InicioCardScreen(onVerPlanoClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Pabellón de Sistemas",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Visualiza la distribución interior del pabellón, sus aulas teóricas, laboratorios y patio central interactivo.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onVerPlanoClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ver Plano Interior")
                }
            }
        }
    }
}

@Composable
fun PlanoAmbiente(viewModel: ArchivoViewModel = viewModel()) {
    val marcadores by viewModel.marcadores.collectAsState()
    val marcadorSeleccionado by viewModel.marcadorSeleccionado.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(420.dp)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        viewModel.seleccionarMarcadorSiAplica(offset)
                    }
                }
        ) {
            val textPaint = Paint().apply {
                color = android.graphics.Color.DKGRAY
                textSize = 28f
                textAlign = Paint.Align.CENTER
                isFakeBoldText = true
                isAntiAlias = true
            }

            val wallStroke = Stroke(width = 8f)

            // 1. Laboratorios (Rectángulo Izquierdo)
            drawRect(
                color = Color(0xFFC8E6C9), // Verde claro
                topLeft = Offset(40f, 40f),
                size = Size(180f, 340f)
            )
            drawRect(
                color = Color.Black,
                topLeft = Offset(40f, 40f),
                size = Size(180f, 340f),
                style = wallStroke
            )
            drawContext.canvas.nativeCanvas.drawText("Laboratorios", 130f, 210f, textPaint)

            // 2. Patio (Círculo Central)
            drawCircle(
                color = Color(0xFFFFF9C4), // Amarillo suave
                radius = 120f,
                center = Offset(380f, 210f)
            )
            drawCircle(
                color = Color.Black,
                radius = 120f,
                center = Offset(380f, 210f),
                style = wallStroke
            )
            drawContext.canvas.nativeCanvas.drawText("Patio", 380f, 215f, textPaint)

            // 3. Salón 202 (Rectángulo Centro-Derecha)
            drawRect(
                color = Color(0xFFFFE0B2), // Naranja suave
                topLeft = Offset(540f, 40f),
                size = Size(180f, 340f)
            )
            drawRect(
                color = Color.Black,
                topLeft = Offset(540f, 40f),
                size = Size(180f, 340f),
                style = wallStroke
            )
            drawContext.canvas.nativeCanvas.drawText("Salón 202", 630f, 210f, textPaint)

            // 4. Salón 201 (Rectángulo Extremo Derecho)
            drawRect(
                color = Color(0xFFBBDEFB), // Azul claro
                topLeft = Offset(760f, 40f),
                size = Size(180f, 340f)
            )
            drawRect(
                color = Color.Black,
                topLeft = Offset(760f, 40f),
                size = Size(180f, 340f),
                style = wallStroke
            )
            drawContext.canvas.nativeCanvas.drawText("Salón 201", 850f, 210f, textPaint)

            // 5. Marcadores interactivos circulares
            marcadores.forEach { marcador ->
                drawCircle(
                    color = Color.White,
                    radius = marcador.radio + 4f,
                    center = marcador.offset
                )
                drawCircle(
                    color = Color(0xFFD32F2F),
                    radius = marcador.radio,
                    center = marcador.offset
                )
            }
        }
    }

    marcadorSeleccionado?.let { marcador ->
        AlertDialog(
            onDismissRequest = { viewModel.cerrarDialogo() },
            title = { Text(marcador.titulo) },
            text = { Text(marcador.descripcion) },
            confirmButton = {
                TextButton(onClick = { viewModel.cerrarDialogo() }) {
                    Text("Cerrar")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PlanoAmbientePreview() {
    PlanoInteractivo_ComposeTheme {
        PlanoAmbiente()
    }
}