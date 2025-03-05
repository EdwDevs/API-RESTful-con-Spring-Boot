API RESTful con Interfaz HTML Mejorada

Este proyecto extiende la API RESTful desarrollada anteriormente, añadiendo una interfaz de usuario HTML moderna y funcional que permite interactuar con todos los endpoints de la API de manera intuitiva y visualmente atractiva.

# 🎯 Características Principales

- ✅ Interfaz de usuario moderna con diseño responsive
- ✅ Sistema de pestañas para organizar las diferentes funcionalidades
- ✅ Soporte multilingüe integrado en la interfaz
- ✅ Visualización de resultados en formato JSON formateado
- ✅ Notificaciones de éxito y error
- ✅ Integración completa con la API RESTful reactiva

# 🛠️ Tecnologías Utilizadas

## Frontend

- HTML5
- CSS3 (con diseño responsive)
- Bootstrap 5
- JavaScript (ES6+)
- Fetch API para comunicación asíncrona

## Backend

- Spring Boot 3+
- Spring WebFlux
- Programación reactiva
- Internacionalización (i18n)

# 📸 Capturas de Pantalla

Interfaz principal con sistema de pestañas

# 📁 Estructura del Proyecto

src/main/resources/  
├── static/  
│ ├── css/  
│ │ └── styles.css # Estilos personalizados  
│ ├── js/  
│ │ └── app.js # Lógica de la aplicación  
│ └── index.html # Página principal  
├── templates/ # Plantillas Thymeleaf (opcional)  
└── application.properties # Configuración de la aplicación

# 🚀 Instalación y Ejecución

## Prerrequisitos

- JDK 17 o superior
- Maven 3.6 o superior
- Navegador web moderno (Chrome, Firefox, Edge, etc.)

## Pasos para ejecutar

1. Clona el repositorio:

git clone <https://github.com/EdwDevs/API-RESTful-con-Spring-Boot.git>

1. Navega al directorio del proyecto:

cd API-RESTful-con-Spring-Boot

1. Compila y ejecuta el proyecto:

mvn spring-boot:run

1. Abre tu navegador y visita: _<http://localhost:8080>_

# 📝 Funcionalidades de la Interfaz

## 1\. Sistema de Pestañas

La interfaz organiza las funcionalidades en pestañas para una mejor experiencia de usuario:

- **Saludos:** Permite obtener saludos en diferentes idiomas
- **Productos:** Muestra, crea, actualiza y elimina productos
- **Pedidos:** Lista y gestiona pedidos existentes
- **Crear Pedido:** Interfaz dedicada para la creación de nuevos pedidos

## 2\. Soporte Multilingüe

La interfaz permite seleccionar el idioma para las respuestas de la API:

- 🇪🇸 Español
- 🇺🇸 Inglés
- 🇫🇷 Francés

## 3\. Visualización de Resultados

Los resultados de las peticiones a la API se muestran en un formato JSON legible y formateado, con resaltado de sintaxis para facilitar la lectura.

## 4\. Notificaciones

El sistema incluye notificaciones visuales para:

- ✅ Operaciones exitosas
- ❌ Errores y excepciones
- ℹ️ Información y advertencias

# 📋 Guía de Uso

## Sección de Saludos

1. Selecciona la pestaña "Saludos"
2. Elige el idioma deseado del menú desplegable
3. Haz clic en "Obtener Saludo"
4. El resultado se mostrará en el área de respuesta

## Sección de Productos

1. Selecciona la pestaña "Productos"
2. Para listar productos: haz clic en "Listar Productos"
3. Para crear un producto: completa el formulario y haz clic en "Crear Producto"
4. Para actualizar o eliminar: primero lista los productos, luego usa los botones correspondientes

## Sección de Pedidos

1. Selecciona la pestaña "Pedidos"
2. Para listar pedidos: selecciona el idioma y haz clic en "Listar Pedidos"
3. Para ver detalles: haz clic en el ID del pedido en la lista

## Crear Pedido

1. Selecciona la pestaña "Crear Pedido"
2. Primero carga los productos disponibles con "Cargar Productos"
3. Selecciona productos y cantidades
4. Ingresa el ID del cliente
5. Selecciona el idioma para la respuesta
6. Haz clic en "Crear Pedido"

# 🔧 Personalización

La interfaz puede personalizarse fácilmente:

- Modifica \`styles.css\` para cambiar la apariencia
- Ajusta \`app.js\` para modificar el comportamiento
- Edita \`index.html\` para añadir o quitar elementos

# 👨‍💻 Autor

\[EdwDevs\](<https://github.com/EdwDevs>)

# 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo \[LICENSE\](LICENSE) para más detalles.