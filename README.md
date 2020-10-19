# Ejemplo de Crud con Firebase en Android

Proyecto en el que se implementa un CRUD en Android utilizando como base de datos Firebase de Google.

## Tecnologías 🚀

* Java
* Android Studio
* Firebase
* Firebase Cloud Messaging

## Base de datos 📋

Se crea una colección Persona para una base de datos Realtime Database de Firebase. La colección estará vinculada a las clases del modelo de la aplicación. El objeto Persona tendrá 5 atributos (uuid, nombre, apellido, correo y contraseña).
El uuid será el id que utilizaremos para realizar las operaciones CRUD.

## Desarrollo de la interfaz gráfica 📋

Desarrollo sobre el Main Activity. Se implementa un menú superior para realizar las operaciones del CRUD. Se implementan 4 campos en pantalla además de un ListView para listar los registros de la base de datos.
Cuando se pulse en algún elemento de la lista se rellenan los textview con los valores del registro pulsado.

## CRUD 📋

Se implementan los métodos del CRUD a través de la API databaseReference de Firebase. Se inicializa Firebase en el método "iniciarFirebase" del activity main.

## Firebase Cloud Messaging 📋

Opcionalmente se prepara la aplicación para que reciba notificaciones tanto en primer plano como en segundo plano utilizando la tecnología Cloud Messaging de Firebase. Se prepara la aplicación
para recibir mensajes personalizados por token o por tema al que los usuarios estén suscritos.


[carky](https://github.com/carky12) 😊
