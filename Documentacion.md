# Cosmo Framework #
La gran mayoría de aplicaciones de gestión actuales están desarrolladas sobre plataforma web, básicamente por su facilidad a nivel de sistemas (actualización de software, configuración/parametrización, integración con otros sistemas, etc.).

El desarrollo web usualmente hecha de menos la simplicidad y agilidad en el diseño UI que ofrecían diversas herramientas para el desarrollo _desktop_. Entornos de desarrollo RAD como .NET (VB/C#), Delphi, Java/Swing o similares permiten desarrollar las aplicaciones usando sólo un lenguaje de programación y todo integrado en una sola herramienta. Para muchos desarrolladores usar Java, Javascript / AJAX, XHTML y CSS,  unas cosas para el servidor y otras para el cliente, hace que para muchos desarrolladores sea tedioso y requiera esfuerzo adicional al diseño _desktop_.

De esta situación surgió la idea del desarrollo de **Cosmo Framework**: un framework que permite **crear aplicaciones web usando únicamente Java**, sin perder tiempo y esfuerzo en el GUI web.

Realmente esta idea no es original pues existen otros frameworks con esa misma premisa (por ejemplo, Google Web Toolkit o GWT) pero **Cosmo Framework** pretende ser simple, rápido y sobretodo compatible con Java (se puede compilar con Java).

## Arquitectura ##

**Cosmo Framework** tiene la siguiente arquitectura:

![https://cosmo-framework.googlecode.com/svn/wiki/images/graph_architecture.png](https://cosmo-framework.googlecode.com/svn/wiki/images/graph_architecture.png)

|**Servicio**|**Descripción**|
|:-----------|:---------------|
|**[UI](Pages.md)**|_User Interface Services_ es el servicio encargado de generar las páginas usando exclusivamente Java y de gestionar su ciclo de vida. Deje que las plantillas generen XHTML y Ud. céntrese únicamente en el desarrollo de la aplicación.|
|**Security**|_Security Services_ se encarga de dotar a las aplicaciones de autenticación y autorización. Su arquitectura abierta permite conectar con casi cualquier servicio como LDAP, CAS, JAAS, etc. o usar la implementación propia (basada en PostgreSQL).|
|**Data**|_Data Services_ ofrece una _interface_ de conexión con cualquier fuente de datos actual de forma transparente. Simplifica drásticamente el uso de múltiples bases de datos en una misma aplicación.|
|**Comm**|_Communication Services_ ofrece una _interface_ única para enviar mensajes, ya sea por correo electrónico, SMS, FAX, etc.|
|**ORM**|_Object-Relational Mapping Services_ permite, mediante anotaciones simples y comprensibles, mapear objetos y generar funcionalidades específicas, como por ejemplo crear un mantenimiento de los objetos contra una base de datos, generar un listado con filtros, etc. que permiten desarrollar determinadas funcionalidades de las aplicaciones de forma casi inmediata.|