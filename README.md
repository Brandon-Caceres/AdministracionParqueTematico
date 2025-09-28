# ⚠️ Advertencia: Configuración Necesaria para Ejecutar el Proyecto

Al clonar este repositorio, recuerda que **JavaFX no viene incluido en el JDK**.  
Por lo tanto, antes de ejecutar el proyecto en NetBeans debes configurar manualmente la librería de JavaFX.  

Sigue las instrucciones detalladas en este documento para instalar y enlazar correctamente el SDK de JavaFX.

# 📄 Guía de Configuración del Entorno

Este proyecto fue desarrollado con **JavaFX 17**.  
Debido a que las versiones modernas del JDK no incluyen JavaFX por defecto, se requiere una configuración manual en el IDE **NetBeans** para poder ejecutarlo.

---

## 📥 1. Descargar el SDK de JavaFX
1. Ve al sitio oficial de Gluon para descargar el SDK de JavaFX: [https://gluonhq.com/products/javafx/](https://gluonhq.com/products/javafx/)  
2. Busca la versión **17.0.16** (o la que corresponda al proyecto) y selecciona el SDK para tu sistema operativo (Windows, macOS, o Linux).  
3. Descarga el archivo `.zip` y descomprímelo en una ubicación estable y de fácil acceso.  
   > Se recomienda el Escritorio o la raíz de tu disco (ejemplo: `C:\`), pero **no** la carpeta de Descargas.

---

## 🛠️ 2. Crear la Librería Global en NetBeans
Este paso se hace **una sola vez** y servirá para todos tus proyectos de JavaFX.

1. Abre NetBeans y ve al menú superior:  
   **`Tools -> Libraries`**
2. Haz clic en **New Library...**  
3. Asígnale un nombre descriptivo, por ejemplo: `JavaFX-17-SDK`. Haz clic en **OK**.  
4. Con la nueva librería seleccionada, ve a la pestaña **ClassPath** y haz clic en **Add JAR/Folder...**  
5. Navega hasta la carpeta donde descomprimiste el SDK, entra en la carpeta **lib** y selecciona **TODOS los archivos `.jar`**.  
6. Haz clic en **Add JAR/Folder** y luego en **OK** para cerrar el administrador de librerías.

---

## ⚙️ 3. Configurar el Proyecto Clonado
Ahora vamos a decirle al proyecto que use la librería creada.

1. Con el proyecto abierto en NetBeans, haz clic derecho sobre el nombre del proyecto en el panel **Projects** y selecciona **Properties**.  
2. En la ventana de propiedades, ve a la categoría **Libraries**.  
3. En la pestaña **Modulepath**, haz clic en el botón **+** y selecciona **Add Library...**  
4. Selecciona la librería `JavaFX-17-SDK` creada anteriormente y haz clic en **Add Library**.  
5. Ahora ve a la categoría **Run**.  
6. En el campo **VM Options**, pega la siguiente línea:

```bash
--module-path "[RUTA_A_TU_SDK]\lib" --add-modules javafx.controls,javafx.fxml
```
⚠️ **IMPORTANTE**: Reemplaza `[RUTA_A_TU_SDK]` con la ruta real donde descomprimiste el SDK.  
Ejemplo en Windows si lo dejaste en el escritorio:

```bash
--module-path "C:\Users\TuUsuario\Desktop\javafx-sdk-17.0.16\lib" --add-modules javafx.controls,javafx.fxml
```
Haz clic en **OK** para guardar los cambios.

---

## 🚀 4. Ejecutar el Proyecto

¡Listo! 🎉  

Con la librería configurada y las opciones de la VM establecidas, ahora puedes ejecutar el proyecto de la forma habitual:

- Haciendo clic en el botón **Play ▶️**  
- O presionando **F6**  

La aplicación debería compilar y lanzarse sin problemas.
