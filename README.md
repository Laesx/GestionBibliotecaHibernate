# Proyecto GestionBibliotecaHibernate 🚀

Breve descripción del proyecto.

## Integrantes del Grupo 👥

- 👨‍💻 Juan Manuel Sujar Gonzalez
- 👨‍💻 Sebastián Olea Castillo
- 👨‍💻 José María La Torre Ávila
- 👨‍💻 Eric ***REMOVED***

## Objetivo del Trabajo 🎯

El objetivo principal de este proyecto es llevar a cabo la adaptación de la biblioteca utilizando un modelo JPA Hibernate en lugar de SQL convencional. Este cambio busca mejorar la eficiencia y la flexibilidad en la gestión de la base de datos.

Además, nos proponemos mantener la integridad del modelo vista-controlador (MVC), asegurando que la arquitectura del proyecto permanezca organizada y escalable.

Otro objetivo crucial es la implementación del patrón Observer en el contexto del MVC. Esto permitirá una comunicación eficiente y desacoplada entre los componentes, proporcionando una mayor modularidad y extensibilidad al sistema.

Estos objetivos no solo mejoran la tecnología subyacente del proyecto, sino que también fortalecen los principios de diseño y la arquitectura general, contribuyendo así a un desarrollo más robusto y sostenible.

## Creación del Proyecto 🛠️



## Implementación de Hibernate 💼

En nuestro proyecto, la implementación de Hibernate se ha llevado a cabo con un enfoque estructurado y eficiente para gestionar la interacción con la base de datos. La capa de modelo, compuesta por las carpetas `modelo`, `mpl` y `dao`, desempeña un papel crucial en esta implementación, proporcionando una representación coherente y organizada de los datos en nuestra aplicación.

### Carpeta `modelo`

La carpeta `modelo` almacena las clases que representan las entidades fundamentales de nuestra aplicación, como `Autor`, `Categoria`, etc. Cada clase en esta carpeta encapsula los datos específicos y las relaciones necesarias entre las entidades. Estas clases actúan como una abstracción clara de la estructura de la base de datos relacional.

### Carpeta `mpl` (Mapeo)

Dentro de la carpeta `mpl`, hemos creado clases dedicadas al mapeo de objetos Java a entidades persistentes en la base de datos utilizando Hibernate. Cada clase MPL contiene métodos de mapeo que definen cómo se relaciona un objeto Java con su representación en la base de datos. Esta estructura garantiza una transparencia efectiva entre las clases de modelo y la estructura de la base de datos.

### Carpeta `dao` (Data Access Object)

La carpeta `dao` es esencial para las operaciones de acceso a datos en nuestra aplicación. Aquí, cada DAO corresponde a una entidad específica, como `AutorDAO`, `CategoriaDAO`, etc. Cada DAO proporciona métodos para realizar operaciones CRUD, facilitando la manipulación de las entidades en la base de datos.

### Cambio de SQL a JPA

Durante la migración de SQL a JPA Hibernate, los DAO han experimentado cambios significativos. Hemos adaptado las consultas SQL a JPQL (Java Persistence Query Language), aprovechando las capacidades de abstracción de Hibernate. Este cambio no solo simplifica las consultas, sino que también las hace más orientadas a objetos, mejorando la coherencia y la flexibilidad del acceso a datos.

### Métodos Generales en la Capa DAO

Hemos introducido una clase en la carpeta `dao` que contiene métodos generales para recuperar listas de objetos comunes. Estos métodos ofrecen una interfaz uniforme y reutilizable para acceder a datos comunes en toda la aplicación. Por ejemplo, métodos que devuelven listas de autores, categorías, etc. Esta abstracción simplifica y unifica el acceso a datos, mejorando la modularidad y la mantenibilidad del código.

### Nueva Funcionalidad Añadida

Como parte de esta implementación, hemos incorporado una nueva funcionalidad que refuerza la eficacia de nuestra aplicación. La nueva funcionalidad se centra en [describir brevemente la funcionalidad añadida y cómo mejora la experiencia del usuario o amplía las capacidades del sistema].

Esta estructura organizada y las modificaciones implementadas no solo reflejan nuestra atención a la calidad del diseño, sino que también garantizan una interacción robusta y escalable con la base de datos, contribuyendo así al éxito general de nuestro proyecto.




## Conexión y Mapeado de la Base de Datos 📊



## Implementación del Patrón Observer en el MVC 🔄



## Funcionalidad Añadida 🚀



## Recursos Utilizados 👥



## Problemas Encontrados y Soluciones Aportadas 🚧






