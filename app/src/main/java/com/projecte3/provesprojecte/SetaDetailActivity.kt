package com.projecte3.provesprojecte.com.projecte3.provesprojecte

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class SetaDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val name = intent.getStringExtra("name")
        val imageRes = intent.getIntExtra("imageRes", 0)
        val descriptions = mapOf(
            "Agaricus xanthodermus" to " Información adicional:\n" +
                    "Nombre común:Champiñón amarilleante.\n" +
                    "Sinónimo:Psalliota xanthoderma (Gen.) Rich. y Roze.\n" +
                    "Clase:Homobasidiomycetes\n" +
                    "Orden:Agaricales\n" +
                    "Familia:Agaricaceae\n" +
                    "Características:\n" +
                    "Sombrero que puede superar los 10 cm, de color predominantemente blanco, aunque existen variedades con tonos más grises. La parte superior es típicamente aplanada confiriéndole al sombrero una forma de trapecio o \"casco alemán\". El margen es un poco lobulado y a veces resquebrajado. Se mancha vivamente de amarillo con solo frotarlo.\n" +
                    "\n" +
                    "Láminas apretadas, de color gris, color que además se mantiene durante mucho tiempo, solo al final rosa-pardas y marrón -negruzcas como todos los Agaricus. Libres con respecto al pie.\n" +
                    "\n" +
                    "Pie central y cilíndrico, bastante alargado y terminado en forma bulbosa en la base, de color blanco y con un anillo persistente del mismo color. Si lo cortamos por la base se tiñe rápidamente de un amarillo muy vivo.\n" +
                    "\n" +
                    "Carne blanca y espesa, amarilleante y con un olor nada agradable, que sin embargo a veces es débil si el espécimen es muy joven y fresco. Sabor también desagradable.\n" +
                    "\n" +
                    "Hábitat:\n" +
                    "Acostumbra a salir desde finales del verano hasta avanzado el otoño, normalmente en \"corros de bruja\". Suele aparecer en prados, jardines, zonas ruderales, y también la hemos visto salir con profusión en escombreras.",
            "Agaricus campestris" to "Información adicional:\n" +
                    "Nombre común:Champiñón silvestre, champiñón de campa, barren-gorri, camperol.\n" +
                    "Sinónimo:Psalliota campestris (L.) Quél.\n" +
                    "Clase:Homobasidiomycetes\n" +
                    "Orden:Agaricales\n" +
                    "Familia:Agaricaceae" +
                    "Características:\n" +
                    "Sombrero de entre 5 y 10 cm de diámetro, en una primera etapa de forma más o menos globosa, después convexo, solo aplanado cuando el ejemplar es muy viejo. Su color fundamental es el blanco, aunque en ocasiones las escamas que recubren la superficie se agrisan, e incluso a veces adquieren tonos ocráceos, dando lugar a algunas variedades. El borde del sombrero suele presentar flecos que no son otra cosa que restos del velo.\n" +
                    "\n" +
                    "Láminas libres con respecto al pie, más bien apretadas, blanquecinas al principio, rápidamente de color rosado, aunque con la maduración van oscureciendo hasta adoptar un color marrón casi negro de viejas.\n" +
                    "\n" +
                    "Pie cilíndrico y proporcionado al tamaño del sombrero, concoloro, lleno y sin escamas. Una de las características fundamentales es que es fácilmente separable del sombrero. Tiene un anillo simple y ascendente del mismo color que el resto del carpóforo.\n" +
                    "\n" +
                    "Carne espesa y consistente de color blanco, algo rosada al corte zonalmente, con olor fúngico agradable y sabor dulce igualmente agradable.\n" +
                    "\n" +
                    "Hábitat:\n" +
                    "Es una especie que fructifica tanto en primavera como en el otoño, propia de pastizales de las zonas bajas, en campas abonadas por el ganado, ya que necesita de terrenos nitrogenados para su crecimiento. Es muy frecuente y, en algunas zonas, muy abundante.\n",
            "Amanita Muscaria" to "Información adicional:\n" +
                    "Nombre común:Matamoscas, falsa oronja, kuleto faltsu.\n" +
                    "Sinónimo:Ninguno.\n" +
                    "Clase:Homobasidiomycetes\n" +
                    "Orden:Agaricales\n" +
                    "Familia:Amanitaceae\n" +
                    "Amanita muscaria - toxica\n" +
                    "Características:\n" +
                    "Sombrero que en abierto puede superar los 15 cm, en un principio globoso pero abriéndose hasta quedar extendido, con el margen acanalado al llegar a la madurez, algo viscoso en tiempo lluvioso. Su color fundamental es el rojo, aunque a veces se difumina hacia tonos anaranjados, y está recubierto de copos blancos, a veces amarillentos y lábiles (pueden perderse con la lluvia). Cutícula separable.\n" +
                    "\n" +
                    "Láminas blancas, libres y bastante anchas.\n" +
                    "\n" +
                    "Pie blanco, central y cilíndrico, engrosado en la base en un bulbo que se encuentra adornado por ribetes concéntricos de consistencia algodonosa, a veces tintados de amarillo. Anillo blanco moteado de copos, colgante y persistente.\n" +
                    "\n" +
                    "Carne blanca, solo bajo la cutícula adquiere tintes naranjas superficialmente, con sabor suave y olor ligeramente rafanoide.\n" +
                    "\n" +
                    "Hábitat:\n" +
                    "Es una especie muy frecuente y cosmopolita, se encuentra sobre todo durante el otoño, aunque no es descartable encontrársela en otras épocas del año si las condiciones son favorables. Aparece por igual en coníferas o caducifolios, sin preferencia especial por ningún tipo de suelo.",
            "Amanita Phalloides" to "Información adicional:\n" +
                    "Nombre común:Amanita pantera, lanperna txar, pixacá.\n" +
                    "Sinónimo:Ninguno.\n" +
                    "Clase:Homobasidiomycetes\n" +
                    "Subclase:Agaricomycetidae\n" +
                    "Orden:Agaricales\n" +
                    "Familia:Amanitaceae\n" +
                    "Características:\n" +
                    "Sombrero que puede sobrepasar los 10 cm de diámetro, de convexo a aplanado, de color pardo gris, variable en los tonos. Su cutícula está recubierta de escamas de tacto harinoso completamente blancas que son los restos de la volva de la que emerge el carpóforo, en tiempo lluvioso estas escamas pueden llegar a desaparecer. En la especie tipo el borde del sombrero es netamente estriado.\n" +
                    "\n" +
                    "Láminas muy blancas, libres con respecto al pie y más bien apretadas.\n" +
                    "\n" +
                    "Pie cilíndrico y central, de color blanco, engrosado en la base. Presenta un anillo blanco colgante liso o débilmente estriado. La volva es floconosa y adherida al pie, prolongándose en forma de brazaletes helicoidales hacia arriba.\n" +
                    "\n" +
                    "Carne poco espesa, de color blanco, con olor rafanoide aunque muy débil, y sabor dulce, no desagradable.\n" +
                    "\n" +
                    "Hábitat:\n" +
                    "Es una especie frecuente y otoñal, que sale por igual en bosques de coníferas y de planifolios, sobre todo en los claros de los propios bosques.",
            "Calocybe gambosa" to "Información adicional:\n" +
                    "Nombre común:Seta de San Jorge, perretxiko, seta de Orduña, moixernó.\n" +
                    "Sinónimo:Tricholoma georgii (L.) Quel.\n" +
                    "Clase:Homobasidiomycetes\n" +
                    "Orden:Tricholomatales\n" +
                    "Familia:Tricholomataceae\n" +
                    "Características:\n" +
                    "Sombrero que puede sobrepasar los 10 cm, de joven tiene el borde enrollado hacia dentro, luego es convexo y al final plano, de color crema blanquecino mate. Es muy carnoso.\n" +
                    "\n" +
                    "Láminas blancas de joven, algo más cremas cuando la seta es madura, bastante numerosas y apretadas, adnatas o ligeramente escotadas.\n" +
                    "\n" +
                    "Pie blanquecino, corto en ocasiones, central y cilíndrico aunque muchas veces se le encuentra engrosado en la base, macizo y pruinoso.\n" +
                    "\n" +
                    "Carne firme y compacta, blanca y con un olor fuerte y característico a harina. Su sabor también es marcadamente harinoso lo que la hace muy apetecible al paladar.\n" +
                    "\n" +
                    "Hábitat:\n" +
                    "Esta especie sale exclusivamente en la primavera, la encontramos en praderas de montaña, junto al brezo, endrinos o rosales silvestres, pero también la encontramos en la zona costera, en campas, huertas, jardines, es bastante frecuente sobre todo en el norte de la península ibérica.",
            "Craterellus cornucopioides" to "Características Craterellus cornucopioides\n" +
                    "Sombrero de hasta 10 cm de diámetro, de color variable según el grado de humedad del espécimen, desde negro a gris mate, con una cutícula lisa o ligeramente veteada de fibrillas y con el borde lobulado de manera irregular. Su forma es de trompeta y posee una cavidad en el centro que se prolonga casi hasta la base del pie.\n" +
                    "\n" +
                    "Láminas inexistentes, el himenio de esta especie es completamente liso, y de color gris ceniciento, si acaso débilmente arrugado.\n" +
                    "\n" +
                    "Pie que pudiéramos considerar como una mera prolongación del sombrero, como hemos mencionado es hueco, y su color es similar al del himenio o ligeramente más oscuro.\n" +
                    "\n" +
                    "Carne  escasa, de consistencia elástica, de gris a negruzca, con olor aromático agradable y buen sabor.\n" +
                    "\n" +
                    "Hábitat: La recolectamos en bosque de alcornoques,  robles y hayas, donde aparece sobre todo en los meses otoñales y invernales, en placas de numerosos ejemplares. Le gusta los terrenos calcareos muy húmedos.",
            "Infundibulicybe geotropa" to "Información adicional:\n" +
                    "Nombre común:Platera, San Martin ziza.\n" +
                    "Sinónimo:Clitocybe geotropa (Bull) Quél.\n" +
                    "Clase:Homobasidiomycetes\n" +
                    "Orden:Tricholomatales\n" +
                    "Familia:Tricholomataceae\n" +
                    "Características:\n" +
                    "Sombrero que en ocasiones alcanza los 25 cm, deprimido y claramente mamelonado de color beige uniforme, algo más blanquecino en su nacimiento, carnoso y de borde regular.\n" +
                    "\n" +
                    "Láminas apretadas que decurren de forma notable sobre el pie, blanquecinas u ocre pálido.\n" +
                    "\n" +
                    "Pie ligeramente claviforme, muy duro y fibroso, central con respecto al sombrero y concoloro con el mismo o ligeramente más pálido.\n" +
                    "\n" +
                    "Carne mas bien blanquecina, tenaz y consistente, excesivamente fibrosa la del pie, lo cual lo hace desechable en la recolección. De olor ciánico y sabor dulce.\n" +
                    "\n" +
                    "Hábitat:\n" +
                    "Aparece en otoño en pradera de montaña, aunque nosotros la recolectamos cada año bajo pinos, donde aparece de manera masiva en setales cuya sola visión resulta alucinante, en algunos de ellos se pueden llegar a recolectar hasta 30 kilos.",
            "Leccinellumg riseum" to "Información adicional:\n" +
                    "Nombre común:Boleto agradable, artadi-onddo belzkor.\n" +
                    "Sinónimo:Leccinum lepidum Bouchet ex Essette\n" +
                    "Clase:Homobasidiomycetes\n" +
                    "Subclase:Agaricomycetidae\n" +
                    "Orden:Boletales\n" +
                    "Familia:Boletaceae\n" +
                    "Características:\n" +
                    "Sombrero de buen porte, algunos ejemplares llegan a los 15 cm de diámetro, de forma hemisférica en su nacimiento, pronto se vuelve convexo, forma en la que permanece, ya que no se aplana. El color es muy variable, hemos encontrado ejemplares de color marrón oscuro, hasta casi amarillentos, e incluso en un mismo ejemplar ambas circunstancias por zonas. Su cutícula está lubrificada en época de lluvias, aunque no es viscosa, y el borde normalmente es involuto.\n" +
                    "\n" +
                    "Tubos adnatos de color amarillo, más bien largos, que permanecen inmutables tanto al roce como al corte.\n" +
                    "\n" +
                    "Poros del mismo color que los tubos, aunque en la vejez se oscurecen y se ponen feos, también inmutables.\n" +
                    "\n" +
                    "Pie central y por lo general grueso, algo más fino junto al sombrero y más engrosado en la base. Es de color amarillento y está recubierto por unas granulaciones del mismo color que con el tiempo pueden llegar a pardear.\n" +
                    "\n" +
                    "Carne de color amarillo pálido prácticamente inmutable, tan solo algún matiz rosado sobre todo en la base. Es espesa y de textura agradable, su olor y sabor son así mismo agradables.\n" +
                    "\n" +
                    "Hábitat:\n" +
                    "Esta especie forma micorriza con las encinas, Quercus ilex, donde empieza a aparecer allá por el mes de mayo, en plena primavera, y la hemos visto hasta noviembre. Frecuente en su hábitat exclusivo.",
        )
        val description = descriptions[name]
        setContent {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(16.dp)) {
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = null
                    )
                    Text(
                        text = name ?: "",
                        color = Color.Black,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    Text(
                        text = description ?: "",
                        color = Color.Black,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
                Button(
                    onClick = { finish() },

                    ) {
                    Text(text = "Volver", fontSize = 20.sp) // Aumenta el tamaño del texto a 30sp
                }
            }
        }
    }
    override fun onStart() {
        super.onStart()
        Log.d("SetaDetailActivity", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("SetaDetailActivity", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("SetaDetailActivity", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("SetaDetailActivity", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("SetaDetailActivity", "onDestroy")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("SetaDetailActivity", "onRestart")
        // Re-initialize your resources here
    }
}