# Riders Attack

## Integrantes
- Miguel Ciavato C.I. 30.541.929
- Sofia Marcano C.I. 29.765.263

## Consideraciones
Se debe sincronizar las interacciones entre los usuarios y conductores. Debido a la cantidad finita de los mismo, los conductores en ésta simulación serán los recursos críticos que los usuarios deberan de compartir, para lograr ésta sincronización se empleará un despachador de conductores que funge como monitor del programa y regula el acceso de los usuarios a los conductores.

Debido a que las preferencias de los usuarios y los vehículos de los conductores son asignados de manera aleatoria, debemos considerar el improbable caso en el que un usuario solicite un viaje con un vehículo para el cual no existe un conductor, ya sea disponible u ocupado.
Este caso se manejará permitiendo que los usuarios cambien el vehículo del viaje que han solicitado para permitirles llegar a su destino de todas formas.

## Clases implementadas
![Diagrama de Clases](out/docs/ClassDiagram/Riders_attack.png)
### Main
Ésta es la clase principal del programa y contiene el método main.

Se recibe el nombre del archivo de entrada como argumento y se abre dicho archivo. Luego se procede a leer el archivo para obtener el número de usuarios y el número de conductores de cada aplicación. Estos datos son usados para crear e inicializar el despachador de viajes o Dispatcher y los usuarios.

Por último, este método se encarga de arrancar los hilos de usuarios. 

### Person
Ésta es la clase padre de tanto `User` como de `Rider`, se encarga de inicializar el `ID` de cada uno y definir un servicio de manera aleatoria.

### User
Para realizar la simulación y permitir que se ejecuten de manera concurrente, esta clase implementa `Runnable`. Ésta clase interactúa de manera directa únicamente con `Dispatcher` y no con los `Riders`.

### Rider
Se inicializa su `status` como disponible o `AVAILABLE`. Además se definen su tiempo de llegada o `arrivalTime` y tiempo de viaje o `travelTime` como enteros positivos aleatorios no mayores a 30 y 50 respectivamente.
Hace uso de un `RiderTimer` para simular los tiempos de espera mientras viaja de una locación a otra.
#### Verificar estado del conductor
Se emplean dos métodos `isAvailable()`. El primero se usa para verificar si el conductor se encuentra disponible, brinda el servicio que el usuario desea y además es de la aplicación de preferencia del usuario.

Por otro lado, el segundo método `isAvailable()` solo verifica si el conductor se encuentra disponible y brinda el servicio que el usuario solicitó, independientemente si es de una aplicación diferente a la preferida por el usuario.

#### Simulación de llegada
Emplea el método `arrive()` que cambia el estado del conductor a `BUSY` y hace uso del `RiderTimer` para simular la espera hasta la llegada del conductor a la ubicación del usuario. El método `arrivalFinished()` se usa para comprobar si ya se llegó a la ubicación.

Por otro lado, el método `cancelTravel()` resetea el `RiderTimer` y establece el estado del conductor como `AVAILABLE`.

#### Simulación de viaje
El método `travel()` arranca el temporizador `RiderTimer` con el tiempo de viaje para simular el tiempo que le toma al conductor llevar al usuario a su destino. Por otro lado el método `travelFinished()` se usa para verificar si el viaje ya se terminó, además establece el estado del conductor en `AVAILABLE` si ya se completó el viaje.

#### Terminar trabajo
El método `finishWork()` se usa para dar por terminada la jornada de trabajo del conductor. Éste método es llamado cuando ya no hay más usuarios por atender y el programa debe acabar, por tanto es necesario terminar los hilos asociados al conductor, es decir, se debe finalizar el `RiderTimer`.

### RiderTimer
Como mencionamos anteriormente, ésta clase se utiliza como temporizador para la clase `Rider`, esto se logra con la clase interna privada `Counter`. 

La clase `Counter` define un hilo para permitir la ejecución concurrente de varios temporizadores, y simplemente utilizan la función `sleep()` para implementar el temporizador. 

### Dispatcher
Es la clase monitor, encargada de la sincronización.
Como tal lleva el registro de los conductores y funciona como interfaz por la cual los usuarios interactuan con los conductores.

#### Inicialización de los conductores
Se hace por medio del constructor de la clase y del método `initRiders()`. Se crea un arreglo donde se guardan los conductores, ordenados por la aplicación para la que trabajan.

Para llevar control del caso en el que no existan conductores del vehículo que solicite un usuario se emplean las flags `justCars` y `justMotorcycles`, que indican si solo existe un tipo de vehículo. El método `initRiders()` hace uso de `verifyVehicles()` para actualizar dichas flags a medida que se crean los conductores.

#### Solicitar conductores
Los usuarios emplean el método `requestRiders()` para solicitar un conductor. Éste método se encarga primeramente de verificar que el usuario solicita un vehículo para el cual existen conductores con ayuda de las flags mencionadas anteriormente. En caso de que no, se indica en un mensaje que el usuario no puede solicitar su vehículo de preferencia y solicita el que haya disponible. Si su vehículo de preferencia si está disponible simplemente se indica que el usuario solicita un viaje en dicho vehículo.

Luego, se emplea `requestFirstRider()` para seleccionar un primer conductor que acepte el viaje del usuario. En caso de que no hayan conductores disponibles esta acción se seguirá intentando hasta poder asignar un conductor. 

Para seleccionar el conductor se hace uso del primer método sincronizado del monitor, `getMinRider()`. Éste método recorre el arreglo de conductores y selecciona el índice del conductor disponible de menor tiempo de viaje y que mejor se ajuste a las preferencias del usuario. En caso de no conseguir ningún conductor se devuelve el valor -1 para indicar que se debe volver a intentar cuando se desocupe un conductor.
Si por el contrario se logra encontrar un conductor, se le asigna el viaje por medio de su propio método `arrive()` y se decrementa la cantidad de conductores disponibles.

Al lograr encotrar un primer conductor se indica por consola que éste acepta el viaje del usuario. Sin embargo, aún haciendo uso del método `requestRiders()`, el usuario procede a intentar conseguir un nuevo conductor más cercano a su ubicación.

#### Solicitar un nuevo conductor
Mientras que el conductor seleccionado no haya llegado a la ubicación del usuario, se estará verificando si existe un conductor más cercano a éste en el método `waitForArrive()`. Ésto se logra haciendo uso del segundo método sincronizado del monitor: `getNewRider()`.

El funcionamiento de `getNewRider()` es similar al de `getMinRider()`, con la diferencia de que en el caso de encontrar un nuevo conductor, cancela el viaje con el conductor anterior.

#### Espera de viaje
Una vez un conductor llega a la ubicación del usuario, éste último ya no puede cancelar el viaje y se procede a esperar la duración del viaje, lo que se simula poniendo a dormir el hilo de usuario por esa duración de tiempo. Transcurrido éste periodo de tiempo, se indica gracias a cual conductor el usuario logró llegar a su destino y se incrementa la cantidad de usuarios atendidos.

Si la cantidad de usuarios que han llegado a su destino es igual al total de usuarios, entonces significa que ya se han atendido todos y por lo tanto el programa puede darse por finalizado. Para ello se hace uso del método `endRiders()` que se encarga de indicarle a los conductores que ya han terminado su trabajo.

