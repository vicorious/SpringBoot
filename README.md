# SpringBoot

1. Obtenga el código
  Para empezar, tiene que clonar dos proyectos desde sus repositorios de GitHub. El primero, llamado odotCore, contiene la lógica     empresarial de la aplicación, que está escrita como una aplicación de POJO basada en Spring. La otra, llamada SpringBootDemo, es una aplicación de envoltorio de Spring Boot que envuelve odotCore.

  Para clonar el repositorio odotCore, abra una ventana terminal de Mac o una línea de comandos de Windows, navegue hasta carpeta raíz en la que quiere que el código resida y ejecute el comando:

  * git clone https://github.com/makotogo/odotCore
Para clonar el repositorio de SpringBootDemo, ejecute el comando:

  * git clone https://github.com/makotogo/SpringBootDemo
Observe que los dos proyectos se subordinan inmediatamente al directorio raíz de la aplicación. Después, usted importará el código a su área de trabajo.


2. Importe el código a Eclipse
Vaya a Archivo > Importar... y elija Maven > Proyectos Existentes de Maven.

En el siguiente diálogo, utilice el botón Examinar para buscar el directorio raíz. Los dos proyectos que se clonaron en el paso anterior deberían aparecer en el diálogo, tal como se muestra aquí:

Haga clic en Finalizar para importar los proyectos a su área de trabajo de Eclipse. Después desarrollará el JAR ejecutable.


3. Construir el JAR ejecutable
El desarrollo de SpringBootDemo requiere que desarrolles ambos proyectos odotCore y SpringBootDemo. Es posible desarrollar los proyectos desde la línea de comando, tal como vio en la aplicación HelloSpringBoot. En este caso, le mostraré como hacerlo con Eclipse.

En Eclipse, haga clic derecho en el proyecto odotCore. Elija Ejecutar como > Compilación de Maven y especifique las metas limpiar e instalar . La meta instalar instalará el archivo JAR odotCore-1.0-SNAPSHOT.jar en su repositorio local de Maven. Desde ahí, estará disponible para ser obtenida como dependencia cuando se ejecute la compilación SpringBootDemo Maven.

Después de que la compilación odotCore Maven se ejecute correctamente, haga clic derecho en el proyecto SpringBootDemo, elija Ejecutar como > Compilación de Maven y especifique las metas limpiar y empaquetar .

Nota: El proyecto odotCore contiene varias pruebas unitarias. Aunque siempre digo que nunca (jamás) hay que saltarse las pruebas unitarias, es posible configurar la Configuración de Ejecución que construye el proyecto odotCore en Eclipse para que se las salte (en el diálogo Configuración de Ejecución existe un recuadro de selección para eso).

Después de que la compilación de SpringBootDemo se haya ejecutado correctamente, podrá ejecutar el über JAR de SpringBootDemo desde la línea de comando.

4. 
Ejecutar el JAR ejecutable
Desde una ventana terminal de Mac o desde el indicador de comandos de Windows, navegue hasta el directorio SpringBootDemo. Suponiendo que el directorio de salida de la compilación se llama target (es lo predeterminado), ejecute el siguiente comando:

java -jar target/SpringBootDemo-1.0-SNAPSHOT.jar
Ahora, acomódese para asombrarse mientras Spring Boot ejecuta la aplicación. Cuando vea el texto "Aplicación Iniciada" estará listo para emplear la aplicación.

5
Emplear la aplicación
Como prueba rápida para asegurarnos de que la aplicación está funcionando correctamente, abra una ventana del navegador e ingrese el siguiente URL:

http://localhost:8080/CategoryRestService/FindAll
Esto accede al método FindAll de CategoryRestService y devuelve todos los objetos de la Categoría de la base de datos en formato JSON.



Gracias a 

https://www.ibm.com/developerworks/ssa/library/j-spring-boot-basics-perry/index.html
