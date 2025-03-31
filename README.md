# pruebadroid    

### Descripcion
pruebadroid es una aplicacion que permite la gestión (listar, crear, editar, borrar) de notas y además tiene un catálogo online de los personajes de la serie animada *Rick y Morty* con un gestor de favoritos para la misma  
  
Esta app ha sido desarrollada enteramente en el lenguage de Kotlin, con una arquitectura MVVM y se han usado recursos tales como: 
- ViewModel y LiveData para la comunicacion de los datos con la vista y viceversa
- ViewBinding para la interaccion de las vistas xml con la clase que la gestiona
- Navegación entre pantallas gestionado por NavController
- SharedPreferences para el almacenaje local de datos en caché
- Librería Retrofit para acceder a la API de Rick y Morty (https://rickandmortyapi.com/)
- Librería Glide para la carga de imágenes en línea

<br>

### Dependencias
Estas son las dependencias principales necesarias para trabajar con el proyecto:
- Kotlin: lenguaje utilizado
- AndroidX: bibliotecas necesarias 
- Retrofit: libreria para el consumo de API
- Glide: libreria para la carga de imagenes en la vista

<br>

### Requisitos
Estos son los requisitos necesarios para poder compilar y ejecutar el proyecto:
- JDK 11 o superior
- Android SDK (API 24 o superior)

<br>

### Funcionalides por pantalla
- *Lista de tareas*
  - Obtiene la lista de tareas guardadas en memoria local (mediante el uso de las SharedPreferences) y las muestra por pantalla
  - Al pulsar en cada tarea, se abre el editor de la misma (pantalla *Detalle de tarea*)
  - Mediante una pulsación larga en cada tarea, se accede a un menu contextual que permite cambiar el estado de la misma o eliminarla
  - Si la lista de tareas que se obtiene está vacía, se muestra un texto "La lista esta vacia. Solo puedes añadir una tarea nueva." y un botón "AÑADIR NUEVA TAREA" que al pulsarlo conduce a dicha pantalla
  - En la Toolbar se sitúan dos botones: Añadir una tarea nueva y Filtros
 
- *Detalle de tarea*
  - Muestra el detalle de la tarea seleccionada o crea una nueva tarea
  - Valida que todos los campos han sido rellenados correctamente para guardar los cambios
  
- *Rick Morty*
  - Obtiene la lista de personajes de la serie en línea y las muestra por pantalla: imagen, nombre, especie y estado (vivo, muerto o desconocido)
  - Mediante una pulsación larga en cada personaje, se accede a un menu contextual para poder marcar o desmarcar el personaje como favorito
  - Si la lista de personajes que se obtiene está vacía o no es posible acceder a ella, se muestra un texto "No se puede acceder a los datos en linea. Solo puedes ver la lista de favoritos guardada." y un botón "FAVORITOS" que al pulsarlo conduce a dicha pantalla
  - En la Toolbar se sitúa el botón Favoritos 
 
- *Favoritos Rick Morty*
  - Obtiene la lista de personajes favoritos que ha sido guardada previamente en la memoria local (mediante el uso de las SharedPreferences) y las muestra por pantalla
  - Mediante una pulsación larga en cada personaje, se accede a un menu contextual para poder eliminarlo de favoritos
  - Si la lista de personajes que se obtiene está vacía, se muestra un texto "Todavia no hay favoritos"
 
<br>

### Funcionalides extras
- Modo oscuro seleccionable desde el menu lateral
- Disponibilidad de dos idiomas: ingles (por defecto) y español
