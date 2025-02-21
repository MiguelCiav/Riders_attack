# Riders Attack

## Integrantes
- Miguel Ciavato C.I. 30.541.929
- Sofia Marcano C.I. 29.765.263

## Consideraciones

Debido a que las preferencias de los usuarios y los vehículos de los conductores son asignados de manera aleatoria, debemos considerar el improbable caso en el que un usuario solicite un viaje con un vehículo para el cual no existe un conductor, ya sea disponible u ocupado.
Este caso se manejará permitiendo que los usuarios cambien el vehículo del viaje que han solicitado para permitirles llegar a su destino de todas formas.

## Clases implementadas
![Diagrama de Clases](out/docs/ClassDiagram/Riders_attack.png)
### Main
Ésta es la clase principal del programa y contiene unicamente el método main.
Se recibe el nombre del archivo de entrada como argumento y se abre dicho archivo. Luego se procede a leer el archivo para obtener el número de usuarios y el número de conductores de cada aplicación. Estos datos son usados para crear e inicializar el despachador de viajes o Dispatcher y los usuarios.
Por último, este método se encarga de arrancar los hilos de usuarios. 

### Dispatcher
Es la clase monitor, encargada de la sincronización.
Como tal lleva el registro de los conductores y funciona como interface por la cual los usuarios interactuan con los conductores.

#### Inicialización de los conductores
Se hace por medio del constructor de la clase y del método `initRiders()`. Se crea un arreglo donde se guardan los conductores, ordenados por la aplicación para la que trabajan.

Para llevar control del caso en el que no existan conductores del vehículo que solicite un usuario se emplean las flags `justCars` y `justMotorcycles`, que indican si solo existe un tipo de vehículo. El método `initRiders()` hace uso de `verifyVehicles()` para actualizar dichas flags a medida que se crean los conductores.

#### Solicitar conductores
Los usuarios emplean el método `requestRiders()` para solicitar un conductor. Éste método se encarga primeramente de verificar que el usuario solicita un vehículo para el cual existen conductores. En caso de que no, se indica en un mensaje que el usuario no puede solicitar su vehículo de preferencia y solicita el que haya disponible. Si su vehículo de preferencia si está disponible simplemente se indica que el usuario solicita un viaje en dicho vehículo.

Luego, se emplea `requestFirstRider()` para seleccionar un primer conductor que acepte el viaje del usuario. En caso de que no hayan conductores disponibles esta acción se seguirá intentando hasta poder asignar un conductor. 

Para seleccionar el conductor se hace uso del primer método sincronizado del monitor, `getMinRider()`. Éste método recorre el arreglo de conductores y selecciona el índice del conductor disponible de menor tiempo de viaje y que mejor se ajuste a las preferencias del usuario. En caso de no conseguir ningún conductor se devuelve el valor -1 para indicar que se debe volver a intentar cuando se desocupe un conductor.

